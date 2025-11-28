---BASE DE DATOS ACTUALIZADA---

CREATE TABLE persona (
    id INT AUTO_INCREMENT PRIMARY KEY,
    tipo_documento ENUM('DNI', 'CE', 'PASAPORTE') NOT NULL,
    num_documento VARCHAR(20) NOT NULL,
    nombre VARCHAR(60) NOT NULL,
    apellido_paterno VARCHAR(60) NOT NULL,
    apellido_materno VARCHAR(60),
    telefono VARCHAR(15),
    correo VARCHAR(80)
);


CREATE TABLE usuario (
    id INT PRIMARY KEY,
    rol ENUM('ADMIN', 'VENDEDOR', 'CLIENTE') NOT NULL,
    dni_Persona INT NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    FOREIGN KEY (dni_Persona) REFERENCES persona(id)
);

CREATE TABLE fidelizacion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    puntos INT DEFAULT 0,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
                      ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_cliente) REFERENCES usuario(id)
);


CREATE TABLE categoria (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_categoria VARCHAR(100) NOT NULL,
    descripcion_categoria TEXT
);

CREATE TABLE producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_categoria INT NOT NULL,
    sku_producto VARCHAR(45),
    nombre_producto VARCHAR(100) NOT NULL,
    img VARCHAR(255),
    descripcion_producto TEXT,
    estado_producto ENUM('DISPONIBLE', 'AGOTADO', 'DESCONTINUADO') NOT NULL,
    FOREIGN KEY (id_categoria) REFERENCES categoria(id)
);

create table talla (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_talla VARCHAR(10) NOT NULL
);

CREATE TABLE talla_producto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    id_talla int not null,
    stock INT DEFAULT 0,
    precio_talla_producto DECIMAL(7,2) NOT NULL,
    FOREIGN KEY (id_producto) REFERENCES producto(id),
    FOREIGN KEY (id_talla) REFERENCES talla(id)
)

CREATE TABLE pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    fecha_pedido DATE NOT NULL,
    total_pedido DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES usuario(id)
);

CREATE TABLE detalle_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_talla_producto INT NOT NULL,
    cantidad_detalle INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal_detalle DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id),
    FOREIGN KEY (id_talla_producto) REFERENCES talla_producto(id)
);

CREATE TABLE pago (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    metodo_pago ENUM('TARJETA','TRANSFERENCIA','YAPE') NOT NULL,
    monto DECIMAL(10,2) NOT NULL,
    estado_pago ENUM('PENDIENTE','APROBADO','RECHAZADO') NOT NULL,
    transaction_id VARCHAR(100.0),
    fecha_pago TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_pedido) REFERENCES pedido(id)
);







   


