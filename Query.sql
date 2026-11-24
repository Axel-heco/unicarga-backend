drop database if exists un1carga;
create database un1carga;
use un1carga;

CREATE TABLE Usuario (
                         idUsuario VARCHAR(10) NOT NULL,
                         nombreUsuario VARCHAR(100) NOT NULL,
                         contraUsuario VARCHAR(25) NOT NULL,
                         correoUsuario VARCHAR(35) NOT NULL,
                         rol ENUM('EMPLEADO', 'ADMINISTRADOR') NOT NULL,
                         PRIMARY KEY (idUsuario),
                         UNIQUE KEY uq_usuario_correo (correoUsuario)
);

CREATE TABLE Cliente (
                         idCliente INT NOT NULL AUTO_INCREMENT,
                         empresa VARCHAR(150) NOT NULL,
                         ubicacion VARCHAR(255) NOT NULL,
                         PRIMARY KEY (idCliente)
);

CREATE TABLE Producto (
                          idProducto INT NOT NULL AUTO_INCREMENT,
                          nombreProducto VARCHAR(150) NOT NULL,
                          categoria VARCHAR(100) NOT NULL,
                          largoProducto DECIMAL(10,2) NOT NULL,
                          anchoProducto DECIMAL(10,2) NOT NULL,
                          altoProducto DECIMAL(10,2) NOT NULL,
                          pesoProducto DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                          PRIMARY KEY (idProducto)
);

CREATE TABLE UnidadTransporte (
                                  idTransporte INT NOT NULL AUTO_INCREMENT,
                                  tipoTransporte VARCHAR(100) NOT NULL,
                                  anchoTransporte DECIMAL(10,2) NOT NULL,
                                  largoTransporte DECIMAL(10,2) NOT NULL,
                                  altoTransporte DECIMAL(10,2) NOT NULL,
                                  pesoTransporte DECIMAL(10,2) NOT NULL DEFAULT 0.00,
                                  PRIMARY KEY (idTransporte)
);

CREATE TABLE Pedido (
                        idPedido INT NOT NULL AUTO_INCREMENT,
                        total INT NOT NULL DEFAULT 0,
                        espacioUsado DECIMAL(14,4) NOT NULL DEFAULT 0,
                        espacioLibre DECIMAL(14,4) NOT NULL DEFAULT 0,
                        pesoTotal DECIMAL(14,2) NOT NULL DEFAULT 0,
                        estado ENUM('PENDIENTE', 'FINALIZADO') NOT NULL,
                        fechaPedido DATETIME NOT NULL,
                        idUsuario VARCHAR(10) NOT NULL,
                        PRIMARY KEY (idPedido),
                        CONSTRAINT fk_pedido_usuario FOREIGN KEY (idUsuario)
                            REFERENCES Usuario(idUsuario)
                            ON UPDATE CASCADE
                            ON DELETE RESTRICT
);

CREATE TABLE Recibe (
                        idRecibe INT NOT NULL AUTO_INCREMENT,
                        idPedido INT NOT NULL,
                        idCliente INT NOT NULL,
                        PRIMARY KEY (idRecibe),
                        CONSTRAINT fk_recibe_pedido FOREIGN KEY (idPedido)
                            REFERENCES Pedido (idPedido)
                            ON UPDATE CASCADE
                            ON DELETE CASCADE,
                        CONSTRAINT fk_recibe_cliente FOREIGN KEY (idCliente)
                            REFERENCES Cliente (idCliente)
                            ON UPDATE CASCADE
                            ON DELETE CASCADE
);

CREATE TABLE Contiene (
                          idContiene INT NOT NULL AUTO_INCREMENT,
                          idPedido INT NOT NULL,
                          idProducto INT NOT NULL,
                          totalProducto INT NOT NULL DEFAULT 1,
                          PRIMARY KEY (idContiene),
                          CONSTRAINT fk_contiene_pedido FOREIGN KEY (idPedido)
                              REFERENCES Pedido (idPedido)
                              ON UPDATE CASCADE
                              ON DELETE RESTRICT,
                          CONSTRAINT fk_contiene_producto FOREIGN KEY (idProducto)
                              REFERENCES Producto (idProducto)
                              ON UPDATE CASCADE
                              ON DELETE RESTRICT
);

-- Unidades de transporte asignadas a un pedido (un pedido puede usar varias, con cantidad)
CREATE TABLE Transporta (
                          idTransporta INT NOT NULL AUTO_INCREMENT,
                          idPedido INT NOT NULL,
                          idTransporte INT NOT NULL,
                          cantidad INT NOT NULL DEFAULT 1,
                          PRIMARY KEY (idTransporta),
                          CONSTRAINT fk_transporta_pedido FOREIGN KEY (idPedido)
                              REFERENCES Pedido (idPedido)
                              ON UPDATE CASCADE
                              ON DELETE RESTRICT,
                          CONSTRAINT fk_transporta_unidad FOREIGN KEY (idTransporte)
                              REFERENCES UnidadTransporte (idTransporte)
                              ON UPDATE CASCADE
                              ON DELETE RESTRICT,
                          CONSTRAINT ck_transporta_cantidad CHECK (cantidad > 0)
);