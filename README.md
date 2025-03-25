GESTION DE UNA TIENDA DE ROPA

Esto es un proyecto en Java que gestiona una tienda de ropa. Tiene las siguientes funciones:

Gestión de productos (añadir y eliminar)
Controlar el stock (añadir y eliminar)
Administrar ventas y clientes 
Sistema de usuarios (cliente y administrador)

USO DEL PROGRAMA 

Login: 
    - Se pedirá usuario y contraseña
    - Si ese usuario es admin entrara como empleado y si es un cliente entrara como comprador

Menus administrador:
    - Ver inventario
    - Ver ventas
    - Añadir inventario
    - Eliminar inventario

Menus cliente:
    - Ver ropa 
    - Comprar
    - Ver mis compras

-> ADMIN, nombre: admin contraseña: admin123
-> CLIENTE, nombre: cliente1 contraseña: cliente123

----- DIAGRAMA ------
<img width="853" alt="Diagrama" src="https://github.com/user-attachments/assets/177462d1-02ee-408b-bc4b-280c265a0adc">

----- BASE DE DATOS -------
CREATE DATABASE Tienda;
USE Tienda;

CREATE TABLE Productos (
    Producto_id INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(100) NOT NULL,
    Descripción TEXT,
    Categoría VARCHAR(50),
    Precio DECIMAL(10,2) NOT NULL
);


CREATE TABLE Tallas (
    Talla_id INT PRIMARY KEY AUTO_INCREMENT,
    Talla VARCHAR(10) NOT NULL
);

CREATE TABLE Inventario (
    Inventario_id INT PRIMARY KEY AUTO_INCREMENT,
    Producto_id INT,
    Talla_id INT,
    Cantidad INT NOT NULL,
    FOREIGN KEY (Producto_id) REFERENCES Productos(Producto_id) ON DELETE CASCADE,
    FOREIGN KEY (Talla_id) REFERENCES Tallas(Talla_id) ON DELETE CASCADE
);

CREATE TABLE Usuario (
    usuario_id INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(100) NOT NULL,
    Contraseña VARCHAR(255) NOT NULL,
    admin BOOLEAN DEFAULT FALSE
);

CREATE TABLE Clientes (
    Cliente_id INT PRIMARY KEY AUTO_INCREMENT,
    Nombre VARCHAR(100) NOT NULL,
    Telefono VARCHAR(15)
);

CREATE TABLE Ventas (
    Venta_id INT PRIMARY KEY AUTO_INCREMENT,
    Cliente_id INT,
    Fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    Total DECIMAL(10,2) NOT NULL,
    Metodo_pago VARCHAR(50),
    FOREIGN KEY (Cliente_id) REFERENCES Clientes(Cliente_id) ON DELETE SET NULL
);

CREATE TABLE DetalleVenta (
    DetalleVenta_id INT PRIMARY KEY AUTO_INCREMENT,
    Venta_id INT,
    Producto_id INT,
    Cantidad INT NOT NULL,
    PrecioUnitario DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (Venta_id) REFERENCES Ventas(Venta_id) ON DELETE CASCADE,
    FOREIGN KEY (Producto_id) REFERENCES Productos(Producto_id) ON DELETE CASCADE
);
-- Insertar productos
INSERT INTO Productos (Nombre, Descripción, Categoría, Precio)
VALUES 
('Camiseta Básica', 'Camiseta de algodón de manga corta', 'Ropa Casual', 19.99),
('Jeans Azul', 'Jeans de corte recto', 'Pantalones', 39.99),
('Chaqueta de Cuero', 'Chaqueta de cuero auténtico', 'Ropa de Abrigo', 99.99),
('Zapatillas Deportivas', 'Zapatillas deportivas para hombre', 'Calzado', 49.99);

-- Insertar tallas
INSERT INTO Tallas (Talla)
VALUES 
('S'),
('M'),
('L'),
('XL');

-- Insertar inventario
INSERT INTO Inventario (Producto_id, Talla_id, Cantidad)
VALUES 
(1, 1, 50), -- Camiseta Básica, talla S
(1, 2, 60), -- Camiseta Básica, talla M
(2, 3, 40), -- Jeans Azul, talla L
(3, 4, 30), -- Chaqueta de Cuero, talla XL
(4, 2, 100); -- Zapatillas Deportivas, talla M

-- Insertar usuarios
INSERT INTO Usuario (Nombre, Contraseña, admin)
VALUES 
('admin', 'admin123', TRUE), -- Usuario administrador
('cliente1', 'cliente123', FALSE), -- Cliente
('empleado1', 'empleado123', FALSE); -- Empleado

-- Insertar clientes
INSERT INTO Clientes (Nombre, Telefono)
VALUES 
('Juan Pérez', '123456789'),
('María López', '987654321');

-- Insertar ventas
INSERT INTO Ventas (Cliente_id, Total, Metodo_pago)
VALUES 
(1, 159.97, 'Tarjeta de Crédito'), 
(2, 49.99, 'Efectivo');

-- Insertar detalles de ventas
INSERT INTO DetalleVenta (Venta_id, Producto_id, Cantidad, PrecioUnitario)
VALUES 
(1, 1, 2, 19.99), -- 2 camisetas
(1, 2, 1, 39.99), -- 1 jeans
(2, 4, 1, 49.99); -- 1 zapatillas deportivas