# Cadastro de Nomes – Java + MongoDB

## 1. Descrição do Projeto

Este projeto demonstra a integração entre a linguagem Java e o banco de dados NoSQL MongoDB, utilizando o Maven para gerenciamento de dependências.

A aplicação implementa um sistema simples de cadastro de nomes, permitindo realizar operações de inclusão, consulta, atualização e exclusão de documentos no MongoDB.

O objetivo é demonstrar na prática as operações CRUD (*Create, Read, Update e Delete*) utilizando o MongoDB Java Sync Driver.

## 2. Tecnologias Utilizadas

- **Java:** linguagem de programação utilizada no desenvolvimento.
- **Maven:** ferramenta para gerenciamento de dependências e construção do projeto.
- **MongoDB:** banco de dados NoSQL orientado a documentos.
- **MongoDB Java Sync Driver:** biblioteca utilizada para comunicação entre Java e MongoDB.

## 3. Pré-requisitos

Antes de executar o projeto, é necessário possuir:

- JDK instalado (recomendado Java 8 ou superior).
- Apache Maven instalado.
- MongoDB Server instalado e em execução.
- MongoDB configurado para aceitar conexões em `localhost:27017`.

## 4. Dependência Maven

O projeto utiliza o driver oficial do MongoDB para Java.

Adicione a seguinte dependência ao arquivo `pom.xml`:

```xml
<dependency>
    <groupId>org.mongodb</groupId>
    <artifactId>mongodb-driver-sync</artifactId>
    <version>5.5.1</version>
</dependency>
```

## 5. Estrutura do Projeto

A organização recomendada para o projeto Maven é:

```text
CadastroMongoDB/
│
├── pom.xml
├── .gitignore
├── README.md
├── LICENSE
└── src/
    └── main/
        └── java/
            └── Principal.java
```

## 6. Configuração da Conexão

A aplicação estabelece conexão com o MongoDB utilizando a seguinte URI:

```java
String uri = "mongodb://localhost:27017";
```

O banco de dados e a coleção utilizados são:

```java
MongoDatabase banco = clienteMongoDB.getDatabase("test");

MongoCollection<Document> dbTest = banco.getCollection("test");
```

- Banco de dados: `test`
- Coleção: `test`

Caso o banco de dados ou a coleção ainda não existam, o MongoDB os criará quando ocorrer a primeira operação de gravação.

**Observação:** certifique-se de que o serviço do MongoDB esteja em execução antes de iniciar a aplicação.

## 7. Funcionalidades do Sistema

A aplicação apresenta um menu interativo no terminal com as seguintes operações:

| Opção | Funcionalidade | Operação MongoDB |
|---|---|---|
| 1 | Cadastrar nome | `insertOne()` |
| 2 | Listar nomes | `find()` |
| 3 | Consultar nome por ID | `find()` |
| 4 | Atualizar nome | `updateOne()` |
| 5 | Excluir nome | `deleteOne()` |
| 9 | Encerrar programa | — |

### 7.1. Cadastrar nome

Permite inserir um novo documento na coleção `test`.

```java
Document nomedoc = new Document().append("nome", nome);

InsertOneResult resultado = dbTest.insertOne(nomedoc);
```

O MongoDB gera automaticamente o identificador `_id` do documento.

### 7.2. Listar nomes

Consulta os documentos cadastrados e apresenta os resultados em ordem crescente pelo campo `nome`.

```java
for (Document nome:dbTest.find().sort(ascending("nome"))) {
    exibirDbTest(nome);
}
```

### 7.3. Consultar nome por ID

Permite localizar um documento utilizando seu identificador `_id`.

```java
Document mensagem =
    dbTest.find(eq("_id", id)).first();
```

O programa valida o formato do identificador utilizando `ObjectId.isValid()`.

### 7.4. Atualizar nome

Permite modificar o campo `nome` de um documento existente.

```java
dbTest.updateOne(eq("_id", id), combine(set("nome", nome))
);
```

A atualização utiliza o operador `$set`, que modifica somente o campo informado.

### 7.5. Excluir nome

Permite excluir um documento pelo identificador `_id`, solicitando confirmação do usuário antes da operação.

```java
DeleteResult resultado = dbTest.deleteOne(eq("_id", id));
```

## 8. Executando o Projeto

**Passo 1 – Iniciar o MongoDB**

Verifique se o servidor MongoDB está em execução e disponível na porta `27017`.

**Passo 2 – Abrir o projeto**

Abra o projeto em uma IDE de sua preferência, como IntelliJ IDEA, Eclipse ou Visual Studio Code.

**Passo 3 – Compilar o projeto**

No terminal, dentro do diretório do projeto, execute:

```bash
mvn clean compile
```

**Passo 4 – Executar o programa**

Caso o projeto esteja configurado com o plugin Maven Exec, execute:

```bash
mvn exec:java -Dexec.mainClass="Principal"
```

## 9. Exemplo de Utilização

Ao executar o programa, será apresentado o seguinte menu:

```text
=== MENU CADASTRO DE NOMES ===
1 - Cadastrar nome
2 - Listar nome
3 - Consultar nome por ID
4 - Atualizar nome
5 - Excluir nome
9 - Sair

Escolha uma opcao:
```

Após cadastrar um nome, o MongoDB armazenará um documento semelhante a:

```json
{
    "_id": "ObjectId(...)",
    "nome": "Joao"
}
```

O campo `_id` é criado automaticamente pelo MongoDB e utilizado para identificar cada documento.

## 10. Conceitos Trabalhados

Com este projeto, são explorados os seguintes conceitos:

- Integração entre Java e MongoDB.
- Gerenciamento de dependências com Maven.
- Banco de dados NoSQL orientado a documentos.
- Estrutura de documentos BSON.
- Identificadores `ObjectId`.
- Operações CRUD.
- Inserção e consulta de documentos.
- Ordenação de resultados.
- Atualização de documentos com `$set`.
- Exclusão de documentos.
- Tratamento de exceções com `MongoException`.

## 11. Considerações Finais

O projeto apresenta uma aplicação prática de integração entre Java e MongoDB, permitindo compreender como uma aplicação pode realizar operações de persistência de dados em um banco de dados orientado a documentos.

A implementação demonstra as principais operações de manipulação de dados e serve como ponto de partida para o desenvolvimento de aplicações mais completas utilizando Java, Maven e MongoDB.