-- ===========================================
-- LIMPEZA (ordem invertida para evitar erros de FK)
-- ===========================================
DROP TABLE IF EXISTS itens_pedido;
DROP TABLE IF EXISTS pedidos;
DROP TABLE IF EXISTS produtos;
DROP TABLE IF EXISTS telefones;
DROP TABLE IF EXISTS restaurantes;
DROP TABLE IF EXISTS clientes;
DROP TABLE IF EXISTS usuarios;
DROP TABLE IF EXISTS enderecos;
DROP TABLE IF EXISTS ceps;
DROP TABLE IF EXISTS cidades;
DROP TABLE IF EXISTS estados;

-- ===========================================
-- ESTADO
-- ===========================================
CREATE TABLE estados (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(25) NOT NULL,
    uf VARCHAR(2) NOT NULL UNIQUE
);

-- ===========================================
-- CIDADE
-- ===========================================
CREATE TABLE cidades (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(25) NOT NULL,
    estado_id BIGINT NOT NULL,
    CONSTRAINT fk_cidade_estado FOREIGN KEY (estado_id)
        REFERENCES estados(id)
);

-- ===========================================
-- CEP
-- ===========================================
CREATE TABLE ceps (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(9) NOT NULL UNIQUE,
    cidade_id BIGINT NOT NULL,
    CONSTRAINT fk_cep_cidade FOREIGN KEY (cidade_id)
        REFERENCES cidades(id)
);

-- ===========================================
-- ENDERECO
-- ===========================================
CREATE TABLE enderecos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    logradouro VARCHAR(150) NOT NULL,
    tipo_logradouro VARCHAR(20) NOT NULL,
    numero VARCHAR(10) NOT NULL,
    complemento VARCHAR(50),
    bairro VARCHAR(100) NOT NULL,
    cep_id BIGINT NOT NULL,
    CONSTRAINT fk_endereco_cep FOREIGN KEY (cep_id)
        REFERENCES ceps(id)
);

-- ===========================================
-- USUARIO
-- ===========================================
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    tipo_usuario ENUM('CLIENTE','RESTAURANTE') NOT NULL,
    endereco_id BIGINT NOT NULL,
    status BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_endereco FOREIGN KEY (endereco_id)
        REFERENCES enderecos(id)
);

-- ===========================================
-- CLIENTE
-- ===========================================
CREATE TABLE clientes (
    id BIGINT PRIMARY KEY,
    tipo_usuario ENUM('CLIENTE','RESTAURANTE') DEFAULT 'CLIENTE',
    CONSTRAINT fk_cliente_usuario FOREIGN KEY (id)
        REFERENCES usuarios(id)
);

-- ===========================================
-- RESTAURANTE
-- ===========================================
CREATE TABLE restaurantes (
    id BIGINT PRIMARY KEY,
    cnpj VARCHAR(18) UNIQUE NOT NULL,
    categoria VARCHAR(50),
    horario_abertura TIME,
    horario_fechamento TIME,
    estado VARCHAR(20),
    tipo_usuario ENUM('CLIENTE','RESTAURANTE') DEFAULT 'RESTAURANTE',
    CONSTRAINT fk_restaurante_usuario FOREIGN KEY (id)
        REFERENCES usuarios(id)
);

-- ===========================================
-- TELEFONE
-- ===========================================
CREATE TABLE telefones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ddd VARCHAR(3) NOT NULL,
    numero VARCHAR(15) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    usuario_id BIGINT NOT NULL,
    tipo_usuario ENUM('CLIENTE','RESTAURANTE'),
    tipo_telefone ENUM('FIXO','CELULAR','WHATSAPP'),
    CONSTRAINT fk_telefone_usuario FOREIGN KEY (usuario_id)
        REFERENCES usuarios(id)
);

-- ===========================================
-- PRODUTO (N:1 com Restaurante)
-- ===========================================
CREATE TABLE produtos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(120) NOT NULL,
    descricao VARCHAR(250),
    quantidade BIGINT NOT NULL CHECK (quantidade >= 0),
    preco DECIMAL(10,2) NOT NULL CHECK (preco >= 0),
    categoria_produto ENUM('BEBIDAS','COMIDAS','SOBREMESAS') NOT NULL,
    disponibilidade BOOLEAN DEFAULT FALSE,
    data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    restaurante_id BIGINT NOT NULL,
    CONSTRAINT fk_produtos_restaurantes FOREIGN KEY (restaurante_id)
        REFERENCES restaurantes(id)
        ON DELETE CASCADE
);

-- ===========================================
-- PEDIDOS (N:1 com Cliente e Restaurante)
-- ===========================================
CREATE TABLE pedidos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_id BIGINT NOT NULL,
    restaurante_id BIGINT NOT NULL,
    data_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status_pedido ENUM('PENDENTE','CONFIRMADO','DESPACHADO','ENTREGUE','CANCELADO')
        NOT NULL DEFAULT 'PENDENTE',
    valor_total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    taxa_entrega DECIMAL(10, 2)NOT NULL DEFAULT 0.00,
    observacoes VARCHAR(255),

    CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id)
        REFERENCES clientes(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_pedido_restaurante FOREIGN KEY (restaurante_id)
        REFERENCES restaurantes(id)
        ON DELETE CASCADE
);

-- ===========================================
-- ITENS_PEDIDO (N:1 com Pedido e Produto)
-- ===========================================
CREATE TABLE itens_pedido (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    pedido_id BIGINT NOT NULL,
    produto_id BIGINT NOT NULL,
    quantidade BIGINT NOT NULL CHECK (quantidade > 0),
    preco_unitario DECIMAL(10,2) NOT NULL CHECK (preco_unitario >= 0),
    subtotal DECIMAL(10,2) NOT NULL CHECK (subtotal >= 0),

    CONSTRAINT fk_item_pedido FOREIGN KEY (pedido_id)
        REFERENCES pedidos(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_item_produto FOREIGN KEY (produto_id)
        REFERENCES produtos(id)
        ON DELETE CASCADE
);

-- ===========================================
-- ÍNDICES DE DESEMPENHO
-- ===========================================
CREATE INDEX idx_restaurantes_cnpj ON restaurantes(cnpj);
CREATE INDEX idx_telefones_numero ON telefones(numero);
CREATE INDEX idx_enderecos_cep ON enderecos(cep_id);
CREATE INDEX idx_produtos_nome ON produtos(nome);
CREATE INDEX idx_pedidos_cliente ON pedidos(cliente_id);
CREATE INDEX idx_pedidos_restaurante ON pedidos(restaurante_id);
CREATE INDEX idx_itens_pedido_pedido ON itens_pedido(pedido_id);
