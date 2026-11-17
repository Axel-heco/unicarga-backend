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
                        idPedido INT NOT NULL,
                        total INT NOT NULL DEFAULT 0,
                        espacioUsado DECIMAL(14,2) NOT NULL DEFAULT 0,
                        espacioLibre DECIMAL(14,2) NOT NULL DEFAULT 0,
                        estado ENUM('PENDIENTE', 'FINALIZADO') NOT NULL,
                        fechaPedido DATE NOT NULL,
                        idUsuario VARCHAR(10) NOT NULL,
                        idTransporte INT NOT NULL,
                        PRIMARY KEY (idPedido),
                        CONSTRAINT fk_pedido_usuario FOREIGN KEY (idUsuario)
                            REFERENCES Usuario(idUsuario)
                            ON UPDATE CASCADE
                            ON DELETE CASCADE,
                        CONSTRAINT fk_pedido_unidadTransporte FOREIGN KEY (idTransporte)
                            REFERENCES UnidadTransporte (idTransporte)
                            ON UPDATE CASCADE
                            ON DELETE CASCADE
);

CREATE TABLE Recibe (
                        idRecibe INT NOT NULL,
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
                          idContiene INT NOT NULL,
                          idPedido INT NOT NULL,
                          idProducto INT NOT NULL,
                          totalProducto INT NOT NULL DEFAULT 1,
                          PRIMARY KEY (idContiene),
                          CONSTRAINT fk_contiene_pedido FOREIGN KEY (idPedido)
                              REFERENCES Pedido (idPedido)
                              ON UPDATE CASCADE
                              ON DELETE CASCADE,
                          CONSTRAINT fk_contiene_producto FOREIGN KEY (idProducto)
                              REFERENCES Producto (idProducto)
                              ON UPDATE CASCADE
                              ON DELETE CASCADE
);
