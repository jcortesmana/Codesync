
DROP DATABASE IF EXISTS codesync;
CREATE DATABASE codesync;
USE codesync;
CREATE USER 'codesync_user'@'localhost' IDENTIFIED BY '1234';
CREATE TABLE cliente (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    domicilio VARCHAR(150),
    nif VARCHAR(15) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    tipo_cliente ENUM('ESTANDAR','PREMIUM') NOT NULL
);


CREATE TABLE cliente_premium (
    id_cliente INT PRIMARY KEY,
    cuota_anual DECIMAL(10,2) NOT NULL,
    descuento DECIMAL(5,2) NOT NULL,
    CONSTRAINT fk_cliente_premium
        FOREIGN KEY (id_cliente)
        REFERENCES cliente(id_cliente)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE TABLE articulo (
    codigo VARCHAR(50) PRIMARY KEY,
    descripcion VARCHAR(150) NOT NULL,
    precio DOUBLE NOT NULL,
    gastosEnvio DOUBLE NOT NULL,
    tiempoPreparacion INT
);

CREATE TABLE pedido (
    numero INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATE NOT NULL,
    cantidad INT NOT NULL,
    enviado BOOLEAN DEFAULT FALSE,
    id_cliente INT NOT NULL,
    CONSTRAINT fk_cliente_pedido FOREIGN KEY (id_cliente)
        REFERENCES cliente(id_cliente)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- PEDIDO_ARTICULO (relación N:M)
CREATE TABLE pedido_articulo (
    id_pedido INT NOT NULL,
    codigo_articulo VARCHAR(20) NOT NULL,
    cantidad INT NOT NULL,
    PRIMARY KEY (id_pedido, codigo_articulo),
    CONSTRAINT fk_pedido_pa FOREIGN KEY (id_pedido)
        REFERENCES pedido(numero)
        ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT fk_articulo_pa FOREIGN KEY (codigo_articulo)
        REFERENCES articulo(codigo)
        ON DELETE CASCADE ON UPDATE CASCADE
);