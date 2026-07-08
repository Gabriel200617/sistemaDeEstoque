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


INSERT INTO users (
    username,
    passwords,
    nameFirst,
    sobreNome,
    matricula,
    cpf,
    sexo,
    dtaNascimento,
    email,
    telefone,
    funcao,
    cep,
    endereco,
    cidade,
    bairro,
    estado,
    numero,
    complemento
) VALUES (
    'admin',
    '$2a$12$IA0Cn6VPiqqdEAsWGNBDu.QHD9Q8qFXkou/YWE5yb/P0dwTmtOM9q',
    'Administrador',
    'Sistema',
    '000001',
    '00000000000',
    1,
    '1990-01-01',
    'admin@empresa.com',
    '(00) 00000-0000',
    'admin',
    '00000-000',
    'Rua Principal',
    'São Paulo',
    'Centro',
    'SP',
    100,
    ''
);

INSERT INTO users (
    username,
    passwords,
    nameFirst,
    sobreNome,
    matricula,
    cpf,
    sexo,
    dtaNascimento,
    email,
    telefone,
    funcao,
    cep,
    endereco,
    cidade,
    bairro,
    estado,
    numero,
    complemento
) VALUES (
    'perfil',
    '$2a$12$XvKAK8ve.3WfJ6jadnja3.81zvUpk0IyzAi6x/F5rLn.GJFo/TE/C',
    'Usuário',
    'Padrão',
    '000002',
    '11111111111',
    1,
    '1995-01-01',
    'perfil@empresa.com',
    '(00) 99999-9999',
    'user',
    '00000-000',
    'Rua Secundária',
    'São Paulo',
    'Centro',
    'SP',
    200,
    ''
);

INSERT INTO users (
    username, 
    passwords, 
    nameFirst, 
    sobreNome, 
    matricula, 
    cpf, 
    sexo, 
    dtaNascimento, 
    email, 
    telefone, 
    funcao, 
    cep, 
    endereco, 
    cidade, 
    bairro, 
    estado, 
    numero, 
    complemento
) VALUES (
    'hugo', 
    '$2a$12$woZWZxeyoD5LJR9pYYticO1S66R6tbO9Vgo6kTlXqS.Pp7hc66RBi',            
    'Hugo', 
    'Santos',                         
    'MAT-2026-001',                   
    '123.456.789-00',                 
    1,                                
    '1995-05-15',                    
    'hugo.admin@email.com',           
    '(11) 99999-9999',                
    'admin',                          
    '01311-200',                      
    'Avenida Paulista',               
    'São Paulo',                      
    'Bela Vista',                    
    'SP',                             
    1200,                             
    'Bloco B, Ap 42'                 
);


DELIMITER $$

CREATE TRIGGER trg_saida_atualiza_estoque
BEFORE INSERT ON monitoramento
FOR EACH ROW
BEGIN
    DECLARE estoque_atual BIGINT;

    IF NEW.tipo_movimentacao = 'saida' THEN

        SELECT quantidade INTO estoque_atual
        FROM produtos
        WHERE codigo_barras = NEW.codigo_barras
        FOR UPDATE;

        IF estoque_atual IS NULL THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Produto não encontrado no estoque';
        ELSEIF estoque_atual < NEW.quantidade THEN
            SIGNAL SQLSTATE '45000'
                SET MESSAGE_TEXT = 'Estoque insuficiente para essa saída';
        ELSE
            UPDATE produtos
            SET total = (quantidade - NEW.quantidade) * valor,
                quantidade = quantidade - NEW.quantidade
            WHERE codigo_barras = NEW.codigo_barras;
        END IF;

    END IF;
END$$

DELIMITER ;