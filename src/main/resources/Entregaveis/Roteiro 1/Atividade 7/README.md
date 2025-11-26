# Delivery Tech API

Sistema de delivery desenvolvido com Spring Boot e Java 21.

## 🚀 Tecnologias
- **Java 21 LTS** (versão mais recente)
- Spring Boot 3.5.7
- Spring Web
- Spring Data JPA
- H2 Database (Em memória)
- Lombok
- Devtools
- Maven

## ⚡ Recursos Modernos Utilizados
- Records (Java 14+)
- Text Blocks (Java 15+)
- Pattern Matching (Java 17+)
- Virtual Threads (Java 21)

## 🏃‍♂️ Como executar
1. **Pré-requisitos:** JDK 21 instalado
2. Clone o repositório [https://github.com/VictorHFMartins/DeliveryTech_JavaSpring.git]
  `git clone https://github.com/VictorHFMartins/DeliveryTech_JavaSpring.git`
  `cd delivery-tech-api`
4. Execute: `./mvnw spring-boot:run`
5. Acessar a aplicação:
  - API: http://localhost:8080/health
  - API: http://localhost:8080/health/info
  - Console H2: http://localhost:8080/h2-console

## 📋 Endpoints Atuais
1. Health Checks
- GET /health - Status da aplicação (inclui versão Java)
- GET /info - Informações da aplicação
- GET /h2-console - Console do banco H2

2. Cliente
- GET	/clientes - Retorna a lista de todos os clientes cadastrados.
- GET	/clientes/{id}	- Busca um cliente específico pelo ID.
- POST	/clientes -	Cadastra um novo cliente.
- PUT	/clientes/{id} -	Atualiza as informações de um cliente existente.
- DELETE	/clientes/{id} - inativa um cliente do banco de dados.

## 🔧 Configuração
- Porta: 8080
- Banco: H2 em memória
- Usuário: admin
- Senha: admin
- Profile: development

## 👨‍💻 Desenvolvedor
[Victor Hugo Faria Martins]
[Universidade Anhembi Morumbi]  
[Extensão Universitária — Arquitetura de Sistemas API REST Full com Spring Boot]
[Turma: EXTESPDG-AJWW1-57999847]

---------------------------------------------------------------------------------
Desenvolvido com JDK 21 e Spring Boot 3.5.7
Licença: Uso acadêmico / educacional
 
