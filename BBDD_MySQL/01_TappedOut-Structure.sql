-- Eliminar la BBDD si existe
DROP DATABASE IF EXISTS tapped_out;

-- Crear la BBDD
CREATE DATABASE tapped_out
-- Codificación (soporte de emojis y caracteres especiales)
CHARACTER SET utf8mb4
-- Comparación ignorando case_sensitive y caracteres acentuados
COLLATE utf8mb4_unicode_ci;

-- Seleccionar la BBDD
USE tapped_out;

-- Tabla de GÉNEROS
CREATE TABLE IF NOT EXISTS genders (
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- Tabla de TIPOS DE USUARIOS
CREATE TABLE IF NOT EXISTS user_types(
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- Tabla de USUARIOS
CREATE TABLE IF NOT EXISTS users (
	id INT AUTO_INCREMENT PRIMARY KEY,
	dni VARCHAR(20) NOT NULL UNIQUE,
	type_id INT NOT NULL,
	email VARCHAR(255) NOT NULL UNIQUE,
	password_hash VARCHAR(255) NOT NULL,
	first_name VARCHAR(100) NOT NULL,
	last_name VARCHAR(100) NOT NULL,
	date_of_birth DATE NOT NULL,
	gender_id INT NOT NULL,
	country VARCHAR(100) NOT NULL,
	city VARCHAR(100) NOT NULL,
	phone INT(20),
	avatar VARCHAR(255), -- URL
	is_verified BOOLEAN DEFAULT FALSE,
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	
	FOREIGN KEY (type_id) REFERENCES user_types(id),
	FOREIGN KEY (gender_id) REFERENCES genders(id),

	-- Index para consultas con filtrado mucho mas rapidas
	INDEX idx_dni (dni)
) ENGINE=InnoDB;

-- Tabla de DEPORTES
CREATE TABLE IF NOT EXISTS sports (
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(100) NOT NULL UNIQUE
) ENGINE=InnoDB;

-- Tabla de NIVELES DEPORTIVOS
CREATE TABLE IF NOT EXISTS sport_levels (
	id INT AUTO_INCREMENT PRIMARY KEY,
	sport_id INT NOT NULL,
	name VARCHAR(100) NOT NULL,
	
	FOREIGN KEY (sport_id) REFERENCES sports(id),
	
	UNIQUE (sport_id, name)
) ENGINE=InnoDB;

-- Tabla de CATEGORÍAS
CREATE TABLE IF NOT EXISTS categories (
	id INT AUTO_INCREMENT PRIMARY KEY,
	sport_id INT NOT NULL,
	name VARCHAR(100) NOT NULL,
	min_age INT,
	max_age INT,
	min_weight DECIMAL(5,2),
	max_weight DECIMAL(5,2),
	gender_id INT NOT NULL,
	level_id INT,
	
	FOREIGN KEY (sport_id) REFERENCES sports(id),
	FOREIGN KEY (gender_id) REFERENCES genders(id),
	FOREIGN KEY (level_id) REFERENCES sport_levels(id)
) ENGINE=InnoDB;

-- Tabla de EVENTOS
CREATE TABLE IF NOT EXISTS events (
	id INT AUTO_INCREMENT PRIMARY KEY,
	sport_id INT NOT NULL,
	organizer_id INT NOT NULL,
	name VARCHAR(255) NOT NULL,
	description TEXT,
	start_date DATETIME NOT NULL,
	end_date DATETIME NOT NULL,
	status ENUM('PLANNED', 'ONGOING', 'COMPLETED', 'CANCELLED') DEFAULT 'PLANNED',
	country VARCHAR(100) NOT NULL,
	city VARCHAR(100) NOT NULL,
	address VARCHAR(255),
	logo VARCHAR(255),
	created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	registration_fee DECIMAL(10,2),
	
	FOREIGN KEY (sport_id) REFERENCES sports(id),
	FOREIGN KEY (organizer_id) REFERENCES users(id)
) ENGINE=InnoDB;

-- Tabla de CATEGORÍAS POR EVENTO (relación muchos a muchos entre eventos y categorías)
CREATE TABLE IF NOT EXISTS event_categories (
	event_id INT NOT NULL,
	category_id INT NOT NULL,
	
	PRIMARY KEY (event_id, category_id),
	
	FOREIGN KEY (event_id) REFERENCES events(id),
	FOREIGN KEY (category_id) REFERENCES categories(id)
) ENGINE=InnoDB;

-- Tabla de INSCRIPCIONES
CREATE TABLE IF NOT EXISTS inscriptions (
	id INT AUTO_INCREMENT PRIMARY KEY,
	competitor_id INT NOT NULL,
	event_id INT NOT NULL,
	category_id INT NOT NULL,
	register_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
	payment_status ENUM('PENDING', 'PAID', 'CANCELLED') DEFAULT 'PENDING',
	
	FOREIGN KEY (competitor_id) REFERENCES users(id),
	FOREIGN KEY (event_id) REFERENCES events(id),
	FOREIGN KEY (category_id) REFERENCES categories(id)
) ENGINE=InnoDB;

-- Tabla de RESULTADOS
CREATE TABLE IF NOT EXISTS results (
	id INT AUTO_INCREMENT PRIMARY KEY,
	event_id INT NOT NULL,
	category_id INT NOT NULL,
	competitor_id INT NOT NULL,
	position INT NOT NULL,
   	notes TEXT,
   	
   	FOREIGN KEY (event_id) REFERENCES events(id),
   	FOREIGN KEY (category_id) REFERENCES categories(id),
   	FOREIGN KEY (competitor_id) REFERENCES users(id),
   	
   	UNIQUE (event_id, category_id, competitor_id),
   	UNIQUE (event_id, category_id, position)
) ENGINE=InnoDB;
