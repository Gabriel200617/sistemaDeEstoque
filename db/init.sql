USE estoque_db1;

create table users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    passwords VARCHAR(100) NOT NULL,
    nameFirst VARCHAR(100) NOT NULL, 
    sobreNome VARCHAR(100) NOT NULL,
    matricula VARCHAR(100) NOT NULL,
    cpf VARCHAR(100),
    sexo TINYINT,
    dtaNascimento DATE,	
    email VARCHAR(100),
    telefone VARCHAR(100),
    funcao VARCHAR(100),
    cep VARCHAR(100),          
    endereco VARCHAR(255),    
    cidade VARCHAR(100),
    bairro VARCHAR(100),
    estado VARCHAR(50),       
    numero LONG,       
    complemento VARCHAR(100)	
);

create table produtos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_barras VARCHAR(100) NOT NULL UNIQUE,
    nome_produto VARCHAR(255) NOT NULL,
    fabricante VARCHAR(255),
    marca VARCHAR(255),
    data_fabricacao DATE,
    data_vencimento DATE,
    quantidade BIGINT,
    valor DECIMAL(10,2),
    total DECIMAL(10,2),
    status VARCHAR(255)
);

CREATE TABLE monitoramento (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_barras VARCHAR(100)  NOT NULL,
    nome_produto VARCHAR(255)  NOT NULL,
    tipo_movimentacao VARCHAR(10)   NOT NULL,
    quantidade BIGINT NOT NULL,
    valor DECIMAL(10,2),
    data_hora DATETIME NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_monitoramento_produto
        FOREIGN KEY (codigo_barras) REFERENCES produtos(codigo_barras)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

