# Delivery Tech API

Sistema de delivery desenvolvido com Spring Boot e Java 21.

## 🚀 Tecnologias
- **Java 21 LTS** (versão mais recente)
- Spring Boot 3.2.x
- Spring Web
- Spring Data JPA
- H2 Database
- Maven

## ⚡ Recursos Modernos Utilizados
- Records para DTOs (ClienteResponse, EnderecoResponse, etc.)
- Pattern Matching e Text Blocks (Java 17+)
- Herança com JPA (@Inheritance) — Usuario como classe base abstrata
- Enum Mapping com @Enumerated(EnumType.STRING)
- Virtual Threads (Java 21) — suporte pronto para futuras otimizações de performance
- @PrePersist para inicialização automática de atributos (tipoUsuario, status, dataCadastro)

## 🧩 Estrutura de Entidades

**Usuario (classe abstrata)**
Classe base para todas as entidades de usuário.
Campos principais:
- id, nome, email, status, dataCadastro
- tipoUsuario (CLIENTE ou RESTAURANTE)
- Associação com Endereco
- Lista de Telefones (1:N)

**Cliente**
- Herdada de Usuario, com atribuição automática de tipoUsuario = CLIENTE.

**Restaurante**
- Herdada de Usuario, inclui:
- cnpj, categoria, horarioAbertura, horarioFechamento, taxaEntrega, estado
- Enum CategoriaRestaurante e EstadoRestaurante
- Define automaticamente tipoUsuario = RESTAURANTE.

**Telefone**
- Relacionamento N:1 com Usuario.
- O tipo (tipoUsuario) é preenchido automaticamente com base no usuário vinculado.

**Endereco**
- Associa-se a um Cep, contendo logradouro, número, complemento e bairro.

**Cep, Cidade e Estado**
- Hierarquia de localização com relacionamento em cascata:
- Estado → Cidade → Cep → Endereco.

## 🏃‍♂️ Como executar
1. **Pré-requisitos:** JDK 21 instalado
2. Clone o repositório
3. Execute: `./mvnw spring-boot:run`
4. Acessar a aplicação:
API: http://localhost:8080/health
Console H2: http://localhost:8080/h2-console

## 📋 Endpoints

**Health**
- GET	/health	- Verifica o status da aplicação
- GET	/health/info	- Exibe informações da aplicação

**Banco de dados**
- GET	/h2-console	- Acessa o console do banco em memória

**Cliente**
- GET	/clientes	- Lista todos os clientes cadastrados
- GET	/clientes/{id}	- Busca cliente por ID
- GET	/buscar?value=	- Busca cliente por parâmetros (possiveis valores para value = nome, email, cep, cidade, estado, telefone)
- POST	/clientes	- Cadastra um novo cliente
- PUT	/clientes/{id} - Atualiza dados de um cliente
- PATCH	/clientes/inativar/{id} -	Inativa um cliente
- DELETE	/clientes/{id} -	Exclui um cliente

**Estados**
- POST /estados -
- PUT /estados/{id} - 
- DELETE /estados/{id} - 
- GET /estados -
- GET /estados/{uf} -
- GET /estados/cidade -

## 🗄️ Banco de Dados

- Banco: H2 (em memória)
- Modo: create
- Console: /h2-console
- Usuário padrão: admin
- Senha padrão: admin
- URL: jdbc:h2:mem:deliverydb

## 🔧 Configuração
- Porta: 8080
- Banco: H2 em memória
- Profile: development

## Estrutura de Herança JPA
- Usuario (abstract)
  │
  ├── Cliente
  └── Restaurante

📎 O atributo tipoUsuario é herdado e preenchido automaticamente via @PrePersist:

Cliente → CLIENTE
Restaurante → RESTAURANTE
Telefone → herda o tipo do seu usuário associado.

## 🧰 Exemplo de Resposta JSON (Cliente)

{
  "id": 1,
  "nome": "Victor Martins",
  "email": "victor@cliente.com",
  "status": true,
  "telefones": [
    "99999-0001",
    "98888-1111"
  ],
  "endereco": {
    "logradouro": "Rua das Flores",
    "numero": "123",
    "bairro": "Centro",
    "cep": "01001-000",
    "cidade": "São Paulo",
    "estado": "SP"
  }
}

## 👨‍💻 Desenvolvedor
**Victor Hugo Faria Martins — Universidade Anhembi Morumbi**
- 📚 Estudante de Análise e Desenvolvimento de Sistemas
- 💡 Projeto desenvolvido com JDK 21 e Spring Boot 3.5.7
