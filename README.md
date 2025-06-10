# LiterAdyu 📚

![Java](https://img.shields.io/badge/Java-21-blue.svg?style=for-the-badge&logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.0-green.svg?style=for-the-badge&logo=spring)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue.svg?style=for-the-badge&logo=postgresql)
![Maven](https://img.shields.io/badge/Maven-4.0-red.svg?style=for-the-badge&logo=apache-maven)

**Status:** Projeto Concluído ✔️

LiterAdyu é um catálogo de livros interativo via linha de comando. A aplicação consome a API [Gutendex](https://gutendex.com/) para buscar informações sobre livros e autores, persistindo os dados em um banco de dados PostgreSQL para consultas futuras e análises.

Este projeto foi desenvolvido como parte do challenge de programação da **Alura**, com foco em praticar conceitos essenciais do ecossistema Spring.

---

### Tabela de Conteúdos
1. [Sobre o Projeto](#sobre-o-projeto)
2. [Funcionalidades](#funcionalidades)
3. [Tecnologias Utilizadas](#tecnologias-utilizadas)
4. [Como Começar](#como-começar)
5. [Estrutura do Projeto](#estrutura-do-projeto)

---

### Sobre o Projeto
O objetivo principal do LiterAdyu é criar uma aplicação de console robusta que interage com uma API externa, processa os dados recebidos (JSON), e os armazena em um banco de dados relacional. A partir dos dados persistidos, a aplicação oferece diversas opções de consulta e análise, como listar autores vivos em um determinado ano ou exibir estatísticas de downloads.

---

### Funcionalidades

- [x] **Busca de Livros:** Pesquisa livros por título na API Gutendex e os salva no banco de dados, evitando duplicatas.
- [x] **Listagem de Livros:** Exibe todos os livros registrados no banco de dados.
- [x] **Listagem de Autores:** Exibe todos os autores registrados.
- [x] **Autores Vivos por Ano:** Lista todos os autores que estavam vivos em um ano específico.
- [x] **Livros por Idioma:** Filtra e exibe livros disponíveis em um determinado idioma (es, en, fr, pt).

#### Funcionalidades Avançadas
- [x] **Top 10 Livros:** Mostra os 10 livros mais baixados.
- [x] **Busca por Autor:** Permite buscar um autor pelo nome no banco de dados.
- [x] **Estatísticas:** Gera estatísticas descritivas (total, média, máximo, mínimo) sobre o número de downloads dos livros.
- [x] **Autores por Ano de Nascimento:** Lista autores nascidos em um ano específico.

---

### Tecnologias Utilizadas
- **Java 21:** Versão mais recente do Java, utilizando recursos como Records e Text Blocks.
- **Spring Boot 3.5.0:** Framework principal para a criação da aplicação.
- **Spring Data JPA & Hibernate:** Para persistência de dados e abstração do acesso ao banco de dados.
- **PostgreSQL:** Banco de dados relacional para armazenamento dos dados.
- **Maven:** Gerenciador de dependências e build do projeto.
- **Jackson:** Biblioteca para conversão de JSON para objetos Java (DTOs).

---

### Como Começar

Siga os passos abaixo para executar o projeto em seu ambiente local.

#### Pré-requisitos
- **JDK 21** ou superior
- **Apache Maven 3.8** ou superior
- **Uma instância do PostgreSQL** instalada e rodando na sua máquina.

#### Instalação
1. **Clone o repositório:**
   ```bash
   git clone [https://github.com/adil-jr/literadyu.git](https://github.com/adil-jr/literadyu.git)
   cd literadyu
   ```

2. **Prepare o Banco de Dados:**
   Acesse seu cliente PostgreSQL (como o `psql`) e crie o banco de dados e o usuário que a aplicação irá utilizar.
   
   **Atenção:** Os nomes abaixo são apenas exemplos. Você pode escolher os nomes que preferir.
   ```sql
   CREATE DATABASE <seu_banco_de_dados>;
   CREATE USER <seu_nome_de_usuario> WITH PASSWORD '<sua_senha_segura>';
   GRANT ALL PRIVILEGES ON DATABASE <seu_banco_de_dados> TO <seu_nome_de_usuario>;
   ```
   *Guarde essas três informações, pois você as usará na próxima etapa.*

3. **Configure as Variáveis de Ambiente:**
   A aplicação utiliza variáveis de ambiente para se conectar ao banco de dados, conforme definido no arquivo `src/main/resources/application.properties`:
   ```properties
   spring.application.name=literadyu
   spring.datasource.url=jdbc:postgresql://localhost:5432/${DB_NAME}
   spring.datasource.username=${DB_USERNAME}
   spring.datasource.password=${DB_PASSWORD}
   spring.jpa.hibernate.ddl-auto=update
   ```
   Você precisa definir as variáveis de ambiente `DB_NAME`, `DB_USERNAME` e `DB_PASSWORD` com os valores que você criou na etapa anterior.

   **Opção A: Via Terminal (Linux/macOS)**
   Execute os seguintes comandos no seu terminal antes de rodar a aplicação, substituindo os valores de exemplo:
   ```bash
   export DB_NAME=<seu_banco_de_dados>
   export DB_USERNAME=<seu_nome_de_usuario>
   export DB_PASSWORD=<sua_senha_segura>
   ```

   **Opção B: Via IDE (IntelliJ IDEA)**
   Se estiver executando pela IDE, você pode configurar as variáveis de ambiente na sua configuração de execução:
   - Vá em `Run` -> `Edit Configurations...`.
   - Selecione a configuração da sua aplicação `LiteradyuApplication`.
   - No campo `Environment variables`, adicione as três variáveis (`DB_NAME`, `DB_USERNAME`, `DB_PASSWORD`) e seus respectivos valores.

4. **Compile e execute a aplicação:**
   ```bash
   # Compile e empacote o projeto
   mvn clean install
   
   # Execute o arquivo .jar gerado
   java -jar target/literadyu-0.0.1-SNAPSHOT.jar
   ```
   A aplicação será iniciada e o menu interativo aparecerá no seu terminal.

---

### Estrutura do Projeto
O projeto segue uma estrutura de pacotes baseada em funcionalidades para melhor organização:
- `io.github.adil-jr.literadyu`: Pacote raiz.
  - `main`: Contém a classe `Main`, responsável pela interface com o usuário e a orquestração da aplicação.
  - `model`: Entidades JPA que mapeiam as tabelas do banco de dados (`Book`, `Author`).
  - `dto`: Data Transfer Objects (`BookDTO`, `AuthorDTO`) para mapear a resposta da API.
  - `repository`: Interfaces Spring Data JPA (`BookRepository`, `AuthorRepository`) para acesso aos dados.
  - `service`: Classes de serviço (`ApiClient`, `DataConverter`) com responsabilidades específicas.
 
---
