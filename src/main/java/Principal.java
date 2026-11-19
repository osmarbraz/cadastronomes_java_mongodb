
import java.util.Scanner;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Sorts.ascending;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;

public class Principal {

    private static final Scanner scanner = new Scanner(System.in);

       public static void main(String[] args) {

        // Variáveis: URI com banco e porta.
        String uri = "mongodb://localhost:27017";

        // Processamento: estabelece a conexão com o MongoDB.
        try ( MongoClient clienteMongoDB = MongoClients.create(uri)) {

            // Variáveis: seleciona o banco e a coleção.
            MongoDatabase banco = clienteMongoDB.getDatabase("test");
            MongoCollection<Document> dbTest = banco.getCollection("test");

            //Variável de controle do menu
            int opcao;
            do {
                // Saída: apresenta o menu do sistema.
                System.out.println("=== MENU CADASTRO DE NOMES ===");
                System.out.println("1 - Cadastrar nome");
                System.out.println("2 - Listar nome");
                System.out.println("3 - Consultar nome por ID");
                System.out.println("4 - Atualizar nome");
                System.out.println("5 - Excluir nome");
                System.out.println("9 - Sair");

                // Entrada: recebe a opção escolhida pelo usuário.
                // A leitura por nextLine evita deixar a quebra de linha
                // pendente para os métodos que também leem texto.
                System.out.print("Escolha uma opcao: ");
                opcao = scanner.nextInt();
                scanner.nextLine(); // Consome a quebra de linha pendente.

                try {
                    switch (opcao) {
                        case 1:
                            cadastrar(dbTest);
                            break;
                        case 2:
                            listar(dbTest);
                            break;
                        case 3:
                            consultar(dbTest);
                            break;
                        case 4:
                            atualizar(dbTest);
                            break;
                        case 5:
                            excluir(dbTest);
                            break;
                        case 9:
                            System.out.println("Programa encerrado.");
                            break;
                        default:
                            System.out.println("Opcao invalida.");
                            break;
                    }
                } catch (MongoException e) {
                    System.out.println("Erro no MongoDB: " + e.getMessage());
                }
            } while (opcao != 9);

        } catch (MongoException e) {
            // Saída: informa falhas na conexão ou autenticação.
            System.out.println("Nao foi possível conectar ao MongoDB.");
            System.out.println(e.getMessage());

        } finally {
            scanner.close();
        }
    }
   
    private static void cadastrar(MongoCollection<Document> dbTest) {

        System.out.println("\n--- CADASTRAR NOME ---");

        // Entrada: recebe o nome.
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        
        // Processamento: monta o documento BSON.
        Document nomedoc = new Document().append("nome", nome);

        // Processamento: insere o documento na coleção.
        InsertOneResult resultado = dbTest.insertOne(nomedoc);

        // Saída: apresenta o identificador gerado.
        System.out.println("Nome cadastrado com sucesso!");
        System.out.println("ID: " + resultado.getInsertedId());
    }

     private static void listar(MongoCollection<Document> test) {

        System.out.println("\n--- LISTA DE NOMES ---");

        // Variáveis: controla se existem nomes.
        boolean encontrou = false;

        // Processamento: percorre os documentos em ordem de nome.
        for (Document nome : test.find().sort(ascending("nome"))) {
            // Saída: apresenta cada nome.
            exibirDbTest(nome);
            encontrou = true;
        }

        if (!encontrou) {
            System.out.println("Nenhum nome cadastrado.");
        }
    }
 
    private static void consultar(MongoCollection<Document> dbTest) {

        System.out.println("\n--- CONSULTAR NOME ---");

        // Entrada: recebe e valida o ID.
        System.out.print("Informe o ID do nome: ");
        String texto = scanner.nextLine().trim();

        // Verifica se o texto do id é válido
        if (!ObjectId.isValid(texto)) {
            System.out.println("ID invalido.");

        } else {
            ObjectId id = new ObjectId(texto);

            // Processamento: procura o documento pelo ID.
            Document mensagem = dbTest.find(eq("_id", id)).first();

            // Saída: apresenta o resultado da consulta.
            if (mensagem == null) {
                System.out.println("Nome nao encontrado.");
            } else {
                exibirDbTest(mensagem);
            }
        }
    }

      private static void atualizar(MongoCollection<Document> dbTest) {

        System.out.println("\n--- ATUALIZAR NOME ---");

        // Entrada: recebe o ID do nome.
        System.out.print("Informe o ID do nome: ");
        String texto = scanner.nextLine().trim();

        // Verifica se o texto do id é válido
        if (!ObjectId.isValid(texto)) {
            System.out.println("ID invalido.");
        } else {
            ObjectId id = new ObjectId(texto);

            // Processamento: verifica se o nome existe.
            Document existente = dbTest.find(eq("_id", id)).first();

            if (existente == null) {
                System.out.println("NOme nao encontrado.");
                return;
            }

            // Saída: apresenta os dados atuais.
            System.out.println("Nome atual:");
            exibirDbTest(existente);

            // Entrada: recebe os novos valores.
            System.out.println("Digite os novos dados:");
            System.out.print("Novo nome: ");
            String nome = scanner.nextLine();
            
            // Processamento: valida o campo.
            if (nome.isEmpty()) {
                System.out.println("Nome é obrigatorio.");
                return;
            }

            // Processamento: atualiza o campo no banco.
            UpdateResult resultado = dbTest.updateOne(
                    eq("_id", id),
                    set("nome", nome));

            // Saída: informa o resultado da operação.
            if (resultado.getModifiedCount() > 0) {
                System.out.println("Nome atualizado com sucesso!");
            } else {
                System.out.println("Nenhuma alteracao foi realizada.");
            }
        }
    }

    private static void excluir(MongoCollection<Document> dbTest) {

        System.out.println("\n--- EXCLUIR NOME ---");

        // Entrada: recebe o ID do nome.
        System.out.print("Informe o ID do nome: ");
        String texto = scanner.nextLine().trim();
        
        // Verifica se o texto do id é válido
        if (!ObjectId.isValid(texto)) {
            System.out.println("ID invalido.");
        } else {
            ObjectId id = new ObjectId(texto);

            // Processamento: verifica se o nome existe.
            Document mensagem = dbTest.find(eq("_id", id)).first();

            if (mensagem == null) {
                System.out.println("Nome nao encontrado.");
                return;
            }

            // Saída: mostra os dados antes da exclusão.
            exibirDbTest(mensagem);

            // Entrada: solicita confirmação.
            System.out.print("Confirma a exclusao? (S/N): ");
            String confirmacao = scanner.nextLine().trim();

            if (!confirmacao.equalsIgnoreCase("S")) {
                System.out.println("Exclusão cancelada.");
                return;
            }

            // Processamento: remove o documento do banco.
            DeleteResult resultado = dbTest.deleteOne(eq("_id", id));

            // Saída: informa o resultado da exclusão.
            if (resultado.getDeletedCount() > 0) {
                System.out.println("Nome excluido com sucesso!");
            } else {
                System.out.println("Nenhum nome foi excluido.");
            }
        }
    }

    private static void exibirDbTest(Document dbTest) {
        System.out.println("------------------------------");
        System.out.println("ID: " + dbTest.getObjectId("_id"));
        System.out.println("Nome: " + dbTest.getString("nome"));        
    }
}
