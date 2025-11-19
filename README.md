<h1 align="center">🚀 Delivery Tech API</h1>

<p align="center">
  <b>API completa de sistema de delivery desenvolvida em Spring Boot e Java 21</b><br/>
  Projeto acadêmico e profissional com herança JPA, DTOs imutáveis, relacionamentos complexos e arquitetura em camadas.
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white"/>
  <img src="https://img.shields.io/badge/Spring_Boot-3.2+-6DB33F?style=for-the-badge&logo=springboot&logoColor=white"/>
  <img src="https://img.shields.io/badge/Maven-Build-blue?style=for-the-badge&logo=apachemaven&logoColor=white"/>
  <img src="https://img.shields.io/badge/License-MIT-green?style=for-the-badge"/>
</p>

---

## 🧠 Visão Geral

A **Delivery Tech API** é um sistema de backend para delivery de restaurantes.  
Ela permite gerenciar **clientes**, **restaurantes**, **produtos**, **pedidos** e **itens de pedido** de forma organizada, usando arquitetura limpa e boas práticas RESTful.

📦 Construída com **Spring Boot 3.2+** e **Java 21**, a aplicação utiliza **JPA**, **DTOs com Records**, e **ModelMapper** para oferecer um design moderno, seguro e performático.

---

## ⚙️ Tecnologias Utilizadas

| Tecnologia | Descrição |
|-------------|------------|
| ☕ **Java 21 LTS** | Versão mais recente e otimizada da linguagem |
| 🌱 **Spring Boot 3.2+** | Framework principal para a API |
| 🧭 **Spring Web / MVC** | Estrutura REST para endpoints |
| 🗄️ **Spring Data JPA** | Persistência com ORM |
| 🧾 **Jakarta Validation** | Validação de entidades e DTOs |
| 🧰 **Lombok** | Redução de boilerplate no código |
| ⚙️ **ModelMapper** | Conversão entre entidades e DTOs |
| 🧪 **H2 Database** | Banco de dados em memória |
| 📦 **Maven** | Gerenciamento de dependências |

---

## ⚡ Recursos e Padrões Modernos

- ✅ **Records** para DTOs (`ClienteResponse`, `PedidoResponse`, etc.)  
- ✅ **Pattern Matching e Text Blocks** (Java 17+)  
- ✅ **Herança com JPA** (`@Inheritance(strategy = JOINED)`)  
- ✅ **Enums tipados** (`CategoriaRestaurante`, `EstadoRestaurante`, `StatusPedido`, etc.)  
- ✅ **Validação automática com Bean Validation**  
- ✅ **Transações com `@Transactional`**  
- ✅ **Conversão automática com ModelMapper**  
- ✅ **Relacionamentos complexos (`1:N`, `N:1`)**  
- ✅ **Script SQL completo com dados iniciais (`schema.sql` e `data.sql`)**

---

## 🧩 Estrutura das Entidades

```
Usuario (abstract)
│
├── Cliente
│   └── Pedido (1:N)
│
└── Restaurante
    ├── Produto (1:N)
    └── Pedido (1:N)
```

**Outras Entidades:**
- Telefone (N:1 com Usuário)
- Endereco → Cep → Cidade → Estado
- ItemPedido (N:1 com Pedido e Produto)

---

## 🍽️ Exemplo de Pedido (Response)

```json
{
  "id": 1,
  "cliente": { "id": 1, "nome": "Victor Martins" },
  "restaurante": { "id": 4, "nome": "Restaurante Ecully" },
  "itens": [
    { "produto": "Risoto de Cogumelos", "quantidade": 2, "subtotal": 105.80 },
    { "produto": "Vinho Branco Chileno", "quantidade": 1, "subtotal": 24.50 }
  ],
  "statusPedido": "CONFIRMADO",
  "valorTotal": 130.30,
  "observacoes": "Sem cebola, por favor."
}
```

---

## 📡 Principais Endpoints

### 🧍 Clientes
| Método | Endpoint | Descrição |
|--------|-----------|-----------|
| GET | `/clientes` | Lista todos os clientes |
| GET | `/clientes/{id}` | Busca cliente por ID |
| POST | `/clientes` | Cadastra novo cliente |
| PUT | `/clientes/{id}` | Atualiza cliente |
| PATCH | `/clientes/inativar/{id}` | Inativa cliente |
| DELETE | `/clientes/{id}` | Exclui cliente |

---

### 🍴 Restaurantes
| Método | Endpoint | Descrição |
|--------|-----------|-----------|
| GET | `/restaurantes` | Lista todos os restaurantes |
| GET | `/restaurantes/{id}` | Busca restaurante por ID |
| POST | `/restaurantes` | Cadastra novo restaurante |
| PUT | `/restaurantes/{id}` | Atualiza restaurante |
| GET | `/restaurantes/cnpj/{cnpj}` | Busca por CNPJ |

---

### 🛍️ Produtos
| Método | Endpoint | Descrição |
|--------|-----------|-----------|
| GET | `/produtos` | Lista todos os produtos |
| GET | `/produtos/{id}` | Busca produto por ID |
| POST | `/produtos/{restauranteId}` | Cadastra novo produto |
| PUT | `/produtos/{id}` | Atualiza produto |
| PATCH | `/produtos/{id}/disponibilidade` | Alterna disponibilidade |
| DELETE | `/produtos/{id}` | Remove produto |

---

### 📦 Pedidos
| Método | Endpoint | Descrição |
|--------|-----------|-----------|
| GET | `/pedidos` | Lista todos os pedidos |
| GET | `/pedidos/{id}` | Busca pedido por ID |
| GET | `/pedidos/cliente/{clienteId}` | Lista pedidos de um cliente |
| GET | `/pedidos/restaurante/{restauranteId}` | Lista pedidos de um restaurante |
| POST | `/pedidos` | Cadastra novo pedido |
| PUT | `/pedidos/{id}` | Atualiza pedido existente |
| PATCH | `/pedidos/{id}?statusPedido=CONFIRMADO` | Atualiza status do pedido |
| PATCH | `/pedidos/cancelar?idPedido=` | Cancela pedido |
| DELETE | `/pedidos/{id}` | Exclui pedido |

---

## 🗄️ Banco de Dados

| Propriedade | Valor |
|--------------|-------|
| **Banco** | H2 (em memória) |
| **Modo** | create |
| **Console** | `/h2-console` |
| **Usuário** | admin |
| **Senha** | admin |
| **URL** | `jdbc:h2:mem:deliverydb` |

---

## 🧮 Scripts SQL

### 📘 `schema.sql`
Define toda a estrutura do banco de dados (tabelas, chaves primárias e estrangeiras).

### 📗 `data.sql`
Popula automaticamente o banco com:
- 5 estados e 6 cidades  
- 6 ceps e endereços  
- 3 clientes e 3 restaurantes  
- 10 telefones  
- 10 produtos  
- 10 pedidos e itens associados  

💡 Esses dados são carregados automaticamente ao iniciar a aplicação.

---

## 💾 Execução do Projeto

1. **Pré-requisitos:**
   - Java 21
   - Maven 3.9+

2. **Clonar o repositório:**
   ```bash
   git clone https://github.com/seu-usuario/deliverytech-api.git
   cd deliverytech-api
   ```

3. **Executar o projeto:**
   ```bash
   ./mvnw spring-boot:run
   ```

4. **Acessar a aplicação:**
   - API: [http://localhost:8080/health](http://localhost:8080/health)
   - Console H2: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

---

## 🧠 Estrutura de Pastas

```
src/
├── main/
│   ├── java/com/deliverytech/delivery/
│   │   ├── api/         # Controllers e DTOs
│   │   ├── domain/      # Entidades, Enums e Services
│   │   └── infra/       # Configurações e persistência
│   └── resources/
│       ├── application.properties
│       ├── schema.sql
│       └── data.sql
└── test/
    └── ... (futuro módulo de testes)
```

---

## 👨‍💻 Desenvolvedor

**Victor Hugo Faria Martins**  
📚 Estudante de **Análise e Desenvolvimento de Sistemas — Universidade Anhembi Morumbi**  
💡 Apaixonado por backend, arquitetura de software e tecnologias Java.  
🧰 Projeto desenvolvido com **JDK 21**, **Spring Boot 3.5.7** e **arquitetura limpa baseada em camadas**.

<p align="center">
  <a href="https://www.linkedin.com/in/victorhugofariamartins/">
    <img src="https://img.shields.io/badge/LinkedIn-Victor%20Hugo-blue?style=for-the-badge&logo=linkedin">
  </a>
</p>
