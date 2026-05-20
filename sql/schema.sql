-- Script de creación de la base de datos Magic Cards
-- Ejecutar como usuario con permisos suficientes

CREATE DATABASE IF NOT EXISTS magic_cards
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE magic_cards;

-- -------------------------------------------------------
-- Tablas de catálogo
-- -------------------------------------------------------

CREATE TABLE IF NOT EXISTS tipo_carta (
    id_tipo INT          NOT NULL AUTO_INCREMENT,
    nombre  VARCHAR(50)  NOT NULL,
    PRIMARY KEY (id_tipo),
    UNIQUE KEY uq_tipo_nombre (nombre)
);

CREATE TABLE IF NOT EXISTS color (
    id_color INT         NOT NULL AUTO_INCREMENT,
    nombre   VARCHAR(20) NOT NULL,
    PRIMARY KEY (id_color),
    UNIQUE KEY uq_color_nombre (nombre)
);

CREATE TABLE IF NOT EXISTS edicion (
    id_edicion        INT          NOT NULL AUTO_INCREMENT,
    nombre            VARCHAR(100) NOT NULL,
    fecha_lanzamiento DATE,
    PRIMARY KEY (id_edicion)
);

-- -------------------------------------------------------
-- Carta
-- -------------------------------------------------------

CREATE TABLE IF NOT EXISTS carta (
    id_carta           INT          NOT NULL AUTO_INCREMENT,
    nombre             VARCHAR(150) NOT NULL,
    coste_mana         INT          NOT NULL DEFAULT 0,
    fuerza             INT,
    resistencia        INT,
    texto_habilidad    TEXT,
    rareza             ENUM('Común','Infrecuente','Rara','Mítica') NOT NULL,
    legendario         BOOLEAN      NOT NULL DEFAULT FALSE,
    id_tipo_carta      INT          NOT NULL,
    id_tipo_secundario INT,
    id_edicion         INT          NOT NULL,
    PRIMARY KEY (id_carta),
    CONSTRAINT fk_carta_tipo      FOREIGN KEY (id_tipo_carta)      REFERENCES tipo_carta (id_tipo),
    CONSTRAINT fk_carta_tipo_sec  FOREIGN KEY (id_tipo_secundario) REFERENCES tipo_carta (id_tipo),
    CONSTRAINT fk_carta_edicion   FOREIGN KEY (id_edicion)         REFERENCES edicion    (id_edicion),
    CONSTRAINT chk_coste_mana     CHECK (coste_mana >= 0)
);

CREATE TABLE IF NOT EXISTS carta_color (
    id_carta INT NOT NULL,
    id_color INT NOT NULL,
    PRIMARY KEY (id_carta, id_color),
    CONSTRAINT fk_cc_carta FOREIGN KEY (id_carta) REFERENCES carta (id_carta) ON DELETE CASCADE,
    CONSTRAINT fk_cc_color FOREIGN KEY (id_color) REFERENCES color (id_color)
);

-- -------------------------------------------------------
-- Jugador, Mazo, CartaMazo
-- -------------------------------------------------------

CREATE TABLE IF NOT EXISTS jugador (
    id_jugador     INT          NOT NULL AUTO_INCREMENT,
    nombre         VARCHAR(100) NOT NULL,
    email          VARCHAR(150) NOT NULL,
    fecha_registro DATE         NOT NULL DEFAULT (CURRENT_DATE),
    PRIMARY KEY (id_jugador),
    UNIQUE KEY uq_jugador_email (email)
);

CREATE TABLE IF NOT EXISTS mazo (
    id_mazo    INT          NOT NULL AUTO_INCREMENT,
    nombre     VARCHAR(100) NOT NULL,
    id_jugador INT          NOT NULL,
    PRIMARY KEY (id_mazo),
    CONSTRAINT fk_mazo_jugador FOREIGN KEY (id_jugador) REFERENCES jugador (id_jugador) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS carta_mazo (
    id_mazo  INT NOT NULL,
    id_carta INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    PRIMARY KEY (id_mazo, id_carta),
    CONSTRAINT fk_cm_mazo  FOREIGN KEY (id_mazo)  REFERENCES mazo  (id_mazo)  ON DELETE CASCADE,
    CONSTRAINT fk_cm_carta FOREIGN KEY (id_carta) REFERENCES carta (id_carta),
    CONSTRAINT chk_cantidad CHECK (cantidad > 0)
);

-- -------------------------------------------------------
-- Partida
-- -------------------------------------------------------

CREATE TABLE IF NOT EXISTS partida (
    id_partida  INT      NOT NULL AUTO_INCREMENT,
    fecha       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    id_jugador1 INT      NOT NULL,
    id_jugador2 INT      NOT NULL,
    id_ganador  INT,
    PRIMARY KEY (id_partida),
    CONSTRAINT fk_partida_j1      FOREIGN KEY (id_jugador1) REFERENCES jugador (id_jugador),
    CONSTRAINT fk_partida_j2      FOREIGN KEY (id_jugador2) REFERENCES jugador (id_jugador),
    CONSTRAINT fk_partida_ganador FOREIGN KEY (id_ganador)  REFERENCES jugador (id_jugador),
    CONSTRAINT chk_jugadores_distintos CHECK (id_jugador1 <> id_jugador2)
);

-- -------------------------------------------------------
-- Índices de rendimiento
-- -------------------------------------------------------

CREATE INDEX idx_carta_nombre       ON carta     (nombre);
CREATE INDEX idx_carta_tipo_rareza  ON carta     (id_tipo_carta, rareza);
CREATE INDEX idx_carta_mazo_mazo    ON carta_mazo(id_mazo);
CREATE INDEX idx_partida_j1         ON partida   (id_jugador1);
CREATE INDEX idx_partida_j2         ON partida   (id_jugador2);
