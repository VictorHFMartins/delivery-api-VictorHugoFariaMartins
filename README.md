# 🚀 Delivery Tech API — Documentação Oficial (Versão Final)

> **API completa de sistema de delivery desenvolvida em Java 21 + Spring Boot 3.5.7**
>
> Projeto acadêmico-profissional criado por **Victor Hugo Faria Martins**, com arquitetura limpa, segurança JWT, validações, DTOs, ModelMapper, camada de serviços especializada e domínio complexo.

---

# 📌 1. Visão Geral
A **Delivery Tech API** é um backend completo para uma plataforma de delivery, permitindo o gerenciamento de:

- Clientes
- Restaurantes
- Produtos
- Pedidos e Itens do Pedido
- Avaliações
- Telefones
- Endereços e CEPs
- Administradores

A arquitetura segue padrões modernos do mercado, adequada para ambientes profissionais e acadêmicos, utilizando boas práticas REST, validações, tratamento global de erros, camadas bem separadas e segurança JWT.

---

# 🧠 2. Objetivos do Sistema
A aplicação foi idealizada para ser:

✔ **Escalável** — código preparado para crescimento
✔ **Modular** — separação forte entre camadas
✔ **Segura** — autenticação com JWT e UserDetailsService
✔ **Organizada** — DTOs, validações, exceptions, configs
✔ **Fácil de manter** — services com responsabilidades claras
✔ **Didática** — totalmente alinhada com os Roteiros da disciplina

---

# ⚙️ 3. Tecnologias e Ferramentas

| Tecnologia | Versão | Utilidade |
|-----------|---------|-----------|
| **Java** | 21 LTS | Linguagem principal |
| **Spring Boot** | 3.5.7 | Base do projeto |
| **Spring Web** | — | Criação dos controllers REST |
| **Spring Data JPA** | — | ORM para comunicação com o banco |
| **Spring Security** | — | Autenticação e autorização |
| **JWT** | — | Token de acesso seguro |
| **ModelMapper** | — | Conversão entre entidades e DTOs |
| **Jakarta Validation** | — | Validação automática |
| **H2 Database** | — | Banco em memória para testes |
| **Maven** | — | Build e dependências |
| **Lombok** | — | Redução de boilerplate |
| **Dotenv** | — | Variáveis de ambiente |
| **Swagger (OpenAPI)** | — | Documentação interativa |

---

# 🏛️ 4. Arquitetura do Projeto
O projeto segue uma arquitetura limpa baseada em camadas:

```
com.deliverytech.delivery
│
├── api
│   ├── controller        → Endpoints REST
│   ├── dto               → DTOs Request/Response
│   ├── config            → Configurações gerais (Swagger, Security, Dotenv, ModelMapper)
│   └── exceptions        → Tratamento global e exceções
│
├── domain
│   ├── enums             → Enums do sistema
│   ├── model             → Entidades JPA
│   ├── repository        → Interfaces JPARepository
│   └── services          → Interfaces de serviço
│        └── imp          → Implementações dos serviços
│
└── infra
    ├── Jwt               → Filtro, Utils e gestão de tokens
    └── security          → SecurityConfig, UserPrincipal, CustomUserDetailsService
```

Essa organização:
- reduz acoplamento
- melhora legibilidade
- segue boas práticas DDD e Clean Architecture

---

# 🧬 5. Entidades do Domínio
Abaixo, um resumo de cada entidade real do sistema:

## **5.1. Usuário (Pai Abstrato)**
- `id`, `nome`, `email`, `senha`, `tipoUsuario`, `ativo`
- Entidade abstrata herdada por **Cliente** e **Administrador**
- Relacionamentos:
  - Telefone (1:N)
  - Endereço (1:1)

## **5.2. Cliente**
- Herda de Usuário
- Relacionamentos:
  - Pedidos (1:N)
  - Avaliações (1:N)

## **5.3. Administrador**
- Herda de Usuário
- Gerencia operações administrativas

## **5.4. Restaurante**
- `nome`, `descricao`, `categoria`, `estadoRestaurante`, etc.
- Relacionamentos:
  - Produtos (1:N)
  - Pedidos (1:N)
  - Avaliações (1:N)

## **5.5. Produto**
- `nome`, `descricao`, `preco`, `disponivel`, `categoria`
- Relação com restaurante: (N:1)

## **5.6. Pedido**
- `cliente`, `restaurante`, `status`, `valorTotal`, `dataCriacao`
- Itens do pedido (1:N)

## **5.7. ItemPedido**
- `produto`, `quantidade`, `subtotal`
- Relacionamentos (N:1)

## **5.8. Avaliação**
- Cliente avalia restaurante
- Contém nota, comentário e resposta do restaurante

## **5.9. Telefone**
- `numero`, `tipoTelefone`
- Usuário → Telefone (1:N)

## **5.10. Endereço / CEP / Cidade / Estado**
Hierarquia completa:

```
Estado → Cidade → Cep → Endereco
```

---

# 🧩 6. Validadores
Presentes em:
```
/api/validator
```

Validações implementadas:
- `UsuarioValidator`
- `EnderecoValidator`
- `TelefoneValidator`

Funções comuns:
- validar formato de email
- validar telefone
- garantir integridade de dados antes de persistir

---

# ❗ 7. Exceções e Error Handling
Localizadas em:
```
/api/exceptions
```

## Handlers principais:
- `GlobalExceptionHandler`
- `EntityNotFoundException`
- `BusinessException`
- `ValidationErrorResponse`

Erros retornados automaticamente no formato:
```json
{
  "message": "Entidade não encontrada",
  "status": 404,
  "timestamp": "2025-01-14T10:20:31"
}
```

---

# 🔐 8. Segurança com JWT
Módulos localizados em:
```
/infra/Jwt
/infra/security
/api/config/SecurityConfig.java
```

### Componentes:
- `JwtAuthenticationFilter`
- `JwtUtil`
- `CustomUserDetailsService`
- `UserPrincipal`
- SecurityConfig com rotas públicas e privadas

### Fluxo de autenticação:
1. Usuário envia email + senha  
2. AuthService autentica  
3. JWT é gerado  
4. Chamadas futuras enviam `Authorization: Bearer <token>`  
5. Filtro valida  
6. Acesso liberado ou negado

---

# 📡 9. Endpoints da API

Abaixo você encontrará **todos** os endpoints reais do seu projeto.
/// (Inserir aqui conteúdo conforme necessidade de organização futura)

---

# 🗄️ 10. Banco de Dados
- **H2 em memória**
- URL: `jdbc:h2:mem:deliverydb`
- Usuário: `admin`
- Senha: `admin`
- Console: `/h2-console`

Scripts:
- `schema.sql` — criação das tabelas
- `data.sql` — dados pré-carregados
- JSONs: estados, cidades e ceps

---

# 🧪 11. Scripts SQL
Explicação clara de cada arquivo:
- **schema.sql**: define tabelas, constraints, relacionamentos
- **data.sql**: dados iniciais completos

---

# ▶️ 12. Como Executar
```bash
git clone https://github.com/seu-usuario/deliverytech-api.git
cd deliverytech-api
./mvnw spring-boot:run
```

Acesse:
- Health: http://localhost:8080/health
- H2 Console: http://localhost:8080/h2-console

---

# 🗂️ 13. Estrutura de Pastas
```
src/
├── main/
│   ├── java/com/deliverytech/delivery/
│   │   ├── api
│   │   ├── domain
│   │   └── infra
│   └── resources/
│       ├── application.properties
│       ├── schema.sql
│       └── data.sql
└── test/
```

---

# 📚 14. Conformidade com os Roteiros (1 → 8)
Todos os roteiros foram contemplados:
- Estrutura inicial ✔️
- CRUDs completos ✔️
- DTOs Request/Response ✔️
- Validações ✔️
- Relacionamentos complexos ✔️
- Tratamento de erros ✔️
- Segurança JWT ✔️
- Documentação ✔️
- Entidades e dados iniciais ✔️

---

# 🔮 15. Roadmap (Melhorias Futuras)
- Adicionar testes unitários JUnit + Mockito
- Adicionar testes de integração
- Implementar cache com Redis
- Criar módulo de administração web
- Criar filas de pedidos com RabbitMQ
- Deploy em nuvem (Railway / Render / Azure)

---

# 👨‍💻 16. Autor
**Victor Hugo Faria Martins**  
Estudante de ADS — Universidade Anhembi Morumbi  
Apaixonado por Java, backend e arquitetura limpa.

LinkedIn:  
https://www.linkedin.com/in/victorhugofariamartins/

---

<p align="center"><b>DeliveryTech — API moderna, robusta e escalável.</b></p>