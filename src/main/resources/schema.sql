-- ===========================================
-- LIMPEZA
-- ===========================================
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
    CONSTRAINT fk_cidade_estado FOREIGN KEY (estado_id) REFERENCES estados(id)
);

-- ===========================================
-- CEP
-- ===========================================
CREATE TABLE ceps (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(9) NOT NULL UNIQUE,
    cidade_id BIGINT NOT NULL,
    CONSTRAINT fk_cep_cidade FOREIGN KEY (cidade_id) REFERENCES cidades(id)
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
    CONSTRAINT fk_endereco_cep FOREIGN KEY (cep_id) REFERENCES ceps(id)
);

-- ===========================================
-- USUARIO
-- ===========================================
CREATE TABLE usuarios (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    tipo_usuario enum('CLIENTE','RESTAURANTE'),
    endereco_id BIGINT NOT NULL,
    status BOOLEAN DEFAULT TRUE,
    data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_usuario_endereco FOREIGN KEY (endereco_id) REFERENCES enderecos(id)
);

-- ===========================================
-- CLIENTE
-- ===========================================
CREATE TABLE clientes (
    id BIGINT PRIMARY KEY,
    tipo_usuario enum('CLIENTE', 'RESTAURANTE') DEFAULT 'CLIENTE',
    CONSTRAINT fk_cliente_usuario FOREIGN KEY (id) REFERENCES usuarios(id)
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
    taxa_entrega DECIMAL(10, 2),
    estado VARCHAR(20),
    tipo_usuario enum('CLIENTE', 'RESTAURANTE') DEFAULT 'RESTAURANTE',
    CONSTRAINT fk_restaurante_usuario FOREIGN KEY (id) REFERENCES usuarios(id)
);

-- ===========================================
-- TELEFONE (1:N com Usuario)
-- ===========================================
CREATE TABLE telefones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    ddd VARCHAR(3) NOT NULL,
    numero VARCHAR(15) NOT NULL,
    ativo BOOLEAN DEFAULT TRUE,
    usuario_id BIGINT NOT NULL,
    tipo_usuario enum('CLIENTE', 'RESTAURANTE'),
    tipo_telefone enum('FIXO', 'CELULAR', 'WHATSAPP'),
    CONSTRAINT fk_telefone_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
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
    categoria_produto enum('BEBIDAS', 'COMIDAS', 'SOBREMESAS') NOT NULL,
    disponibilidade BOOLEAN DEFAULT FALSE,
    data_cadastro DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    restaurante_id BIGINT NOT NULL,
    CONSTRAINT fk_produtos_restaurantes 
        FOREIGN KEY (restaurante_id) REFERENCES restaurantes(id)
        ON DELETE CASCADE
);

-- ===========================================
-- ÍNDICES DE DESEMPENHO
-- ===========================================
CREATE INDEX idx_restaurantes_cnpj ON restaurantes(cnpj);
CREATE INDEX idx_produtos_nome ON produtos(nome);
CREATE INDEX idx_telefones_numero ON telefones(numero);
CREATE INDEX idx_enderecos_cep ON enderecos(cep_id);
