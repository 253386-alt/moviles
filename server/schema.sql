CREATE DATABASE IF NOT EXISTS miapp CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE miapp;

CREATE TABLE IF NOT EXISTS usuarios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(100) NOT NULL,
  correo VARCHAR(150) NOT NULL UNIQUE,
  password_hash VARCHAR(255) NOT NULL,
  rol ENUM('estudiante','maestro') NOT NULL DEFAULT 'estudiante',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS libros (
  id INT AUTO_INCREMENT PRIMARY KEY,
  titulo VARCHAR(255) NOT NULL,
  autor VARCHAR(150) NOT NULL,
  categoria VARCHAR(100) NOT NULL DEFAULT 'General',
  estado VARCHAR(50) NOT NULL DEFAULT 'Disponible',
  paginas VARCHAR(50) DEFAULT '',
  archivo_nombre VARCHAR(255) DEFAULT '',
  creado_por INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_libros_creado_por FOREIGN KEY (creado_por) REFERENCES usuarios(id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS descargas_libros (
  id INT AUTO_INCREMENT PRIMARY KEY,
  libro_id INT NOT NULL,
  usuario_id INT NOT NULL,
  fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_libro_usuario (libro_id, usuario_id),
  CONSTRAINT fk_desc_libro FOREIGN KEY (libro_id) REFERENCES libros(id) ON DELETE CASCADE,
  CONSTRAINT fk_desc_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS anuncios (
  id INT AUTO_INCREMENT PRIMARY KEY,
  autor_id INT,
  autor_nombre VARCHAR(100) NOT NULL DEFAULT 'Profesor · Maestro',
  fecha_label VARCHAR(50) NOT NULL DEFAULT 'Hoy',
  titulo VARCHAR(255) NOT NULL DEFAULT '',
  contenido TEXT NOT NULL,
  destinatarios VARCHAR(255) NOT NULL DEFAULT 'Todos los estudiantes de la carrera',
  archivo_nombre VARCHAR(255) DEFAULT '',
  borrador BOOLEAN NOT NULL DEFAULT 0,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_anuncio_autor FOREIGN KEY (autor_id) REFERENCES usuarios(id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS comentarios_foro (
  id INT AUTO_INCREMENT PRIMARY KEY,
  anuncio_id INT NOT NULL,
  autor_id INT,
  autor_nombre VARCHAR(100) NOT NULL,
  texto TEXT NOT NULL,
  fecha_label VARCHAR(50) NOT NULL DEFAULT 'Hoy',
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_com_anuncio FOREIGN KEY (anuncio_id) REFERENCES anuncios(id) ON DELETE CASCADE,
  CONSTRAINT fk_com_autor FOREIGN KEY (autor_id) REFERENCES usuarios(id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS likes_foro (
  id INT AUTO_INCREMENT PRIMARY KEY,
  anuncio_id INT NOT NULL,
  usuario_id INT NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  UNIQUE KEY uk_like (anuncio_id, usuario_id),
  CONSTRAINT fk_like_anuncio FOREIGN KEY (anuncio_id) REFERENCES anuncios(id) ON DELETE CASCADE,
  CONSTRAINT fk_like_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS grupos_especiales (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(255) NOT NULL,
  integrantes VARCHAR(100) NOT NULL DEFAULT '',
  estado VARCHAR(50) NOT NULL DEFAULT 'Pendiente',
  descripcion TEXT,
  fecha VARCHAR(100) DEFAULT '',
  maestro_nombre VARCHAR(150) DEFAULT 'Sin asignación registrada',
  creado_por INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  CONSTRAINT fk_grupo_creado_por FOREIGN KEY (creado_por) REFERENCES usuarios(id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS archivos (
  id INT AUTO_INCREMENT PRIMARY KEY,
  nombre_original VARCHAR(255) NOT NULL,
  nombre_guardado VARCHAR(255) NOT NULL,
  ruta VARCHAR(500) NOT NULL,
  mime VARCHAR(100) DEFAULT '',
  tamano BIGINT DEFAULT 0,
  libro_id INT,
  anuncio_id INT,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_arch_libro FOREIGN KEY (libro_id) REFERENCES libros(id) ON DELETE CASCADE,
  CONSTRAINT fk_arch_anuncio FOREIGN KEY (anuncio_id) REFERENCES anuncios(id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- Datos demo (texto plano para pruebas - en producción usa bcrypt)
INSERT INTO usuarios (nombre, correo, password_hash, rol) VALUES
  ('Sebas', 'sebas@gmail.com', 'sebas', 'estudiante'),
  ('Maestro', 'maestro@gmail.com', 'maestro123', 'maestro')
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);

INSERT INTO libros (titulo, autor, categoria, paginas) VALUES
  ('Algebra Lineal', 'Stanley Grossman', 'Matemáticas', '680 págs.'),
  ('Introducción a la literatura', 'María Gómez', 'Literatura', '320 págs.'),
  ('Ciencias para el siglo XXI', 'Carlos Rivera', 'Ciencias', '450 págs.'),
  ('Historia institucional', 'Editorial Uniconect', 'Historia', '210 págs.');

INSERT INTO anuncios (autor_nombre, fecha_label, titulo, contenido) VALUES
  ('Director de carrera', 'Hoy', 'Revisión de avances y convocatoria a próximos talleres.', 'Revisión de avances y convocatoria a próximos talleres.'),
  ('Profesor', 'Ayer', 'Materiales de apoyo y recursos para la próxima evaluación.', 'Materiales de apoyo y recursos para la próxima evaluación.'),
  ('Informes', 'Semana pasada', 'Formato actualizado para entregar informes y dar seguimiento a las actividades.', 'Formato actualizado para entregar informes y dar seguimiento a las actividades.');

INSERT INTO grupos_especiales (nombre, integrantes, estado, descripcion, fecha, maestro_nombre) VALUES
  ('Grupo especial física', '20/30', 'Pendiente 20/30', 'Información del grupo pendiente.', 'Pendiente de definir', 'Sin asignación registrada'),
  ('Grupo pendiente', '30/30', 'Lleno 30/30', 'Información del grupo pendiente.', 'Pendiente de definir', 'Sin asignación registrada'),
  ('Grupo pendiente', 'Sin maestro', 'Sin Maestro', 'Información del grupo pendiente.', 'Pendiente de definir', 'Sin asignación registrada');
