-- =============================================================
-- Script SQL completo — Magic Cards DAM
-- Práctica Final Programación y Bases de Datos
-- Incluye: creación de BD, tablas, relaciones, datos de prueba,
--          procedimiento almacenado y función SQL.
-- Ejecución: mysql -u usuario -p < magic_cards_completo.sql
-- =============================================================

-- ---------------------------------------------------------------
-- 1. BASE DE DATOS
-- ---------------------------------------------------------------
DROP DATABASE IF EXISTS magic_cards;
CREATE DATABASE magic_cards
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE magic_cards;

-- ---------------------------------------------------------------
-- 2. TABLAS DE CATÁLOGO
-- ---------------------------------------------------------------

CREATE TABLE tipo_carta (
    id_tipo INT          NOT NULL AUTO_INCREMENT,
    nombre  VARCHAR(50)  NOT NULL,
    PRIMARY KEY (id_tipo),
    UNIQUE KEY uq_tipo_nombre (nombre)
);

CREATE TABLE color (
    id_color INT         NOT NULL AUTO_INCREMENT,
    nombre   VARCHAR(20) NOT NULL,
    PRIMARY KEY (id_color),
    UNIQUE KEY uq_color_nombre (nombre)
);

CREATE TABLE edicion (
    id_edicion        INT          NOT NULL AUTO_INCREMENT,
    nombre            VARCHAR(100) NOT NULL,
    fecha_lanzamiento DATE,
    PRIMARY KEY (id_edicion)
);

-- ---------------------------------------------------------------
-- 3. CARTA Y RELACIONES
-- ---------------------------------------------------------------

CREATE TABLE carta (
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

CREATE TABLE carta_color (
    id_carta INT NOT NULL,
    id_color INT NOT NULL,
    PRIMARY KEY (id_carta, id_color),
    CONSTRAINT fk_cc_carta FOREIGN KEY (id_carta) REFERENCES carta (id_carta) ON DELETE CASCADE,
    CONSTRAINT fk_cc_color FOREIGN KEY (id_color) REFERENCES color (id_color)
);

-- ---------------------------------------------------------------
-- 4. JUGADOR, MAZO, CARTA_MAZO
-- ---------------------------------------------------------------

CREATE TABLE jugador (
    id_jugador     INT          NOT NULL AUTO_INCREMENT,
    nombre         VARCHAR(100) NOT NULL,
    email          VARCHAR(150) NOT NULL,
    fecha_registro DATE         NOT NULL DEFAULT (CURRENT_DATE),
    PRIMARY KEY (id_jugador),
    UNIQUE KEY uq_jugador_email (email)
);

CREATE TABLE mazo (
    id_mazo    INT          NOT NULL AUTO_INCREMENT,
    nombre     VARCHAR(100) NOT NULL,
    id_jugador INT          NOT NULL,
    PRIMARY KEY (id_mazo),
    CONSTRAINT fk_mazo_jugador FOREIGN KEY (id_jugador) REFERENCES jugador (id_jugador) ON DELETE CASCADE
);

CREATE TABLE carta_mazo (
    id_mazo  INT NOT NULL,
    id_carta INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    PRIMARY KEY (id_mazo, id_carta),
    CONSTRAINT fk_cm_mazo  FOREIGN KEY (id_mazo)  REFERENCES mazo  (id_mazo)  ON DELETE CASCADE,
    CONSTRAINT fk_cm_carta FOREIGN KEY (id_carta) REFERENCES carta (id_carta),
    CONSTRAINT chk_cantidad CHECK (cantidad > 0)
);

-- ---------------------------------------------------------------
-- 5. PARTIDA
-- ---------------------------------------------------------------

CREATE TABLE partida (
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

-- ---------------------------------------------------------------
-- 6. ÍNDICES DE RENDIMIENTO
-- ---------------------------------------------------------------

CREATE INDEX idx_carta_nombre       ON carta     (nombre);
CREATE INDEX idx_carta_tipo_rareza  ON carta     (id_tipo_carta, rareza);
CREATE INDEX idx_carta_mazo_mazo    ON carta_mazo(id_mazo);
CREATE INDEX idx_partida_j1         ON partida   (id_jugador1);
CREATE INDEX idx_partida_j2         ON partida   (id_jugador2);

-- ---------------------------------------------------------------
-- 7. DATOS DE PRUEBA
-- ---------------------------------------------------------------

INSERT INTO tipo_carta (nombre) VALUES
    ('Criatura'),('Hechizo'),('Tierra'),('Instantáneo'),('Encantamiento'),('Artefacto');

INSERT INTO color (nombre) VALUES
    ('Blanco'),('Azul'),('Negro'),('Rojo'),('Verde'),('Incoloro');

INSERT INTO edicion (nombre, fecha_lanzamiento) VALUES
    ('Dominaria United','2022-09-09'),('The Brothers War','2022-11-18'),
    ('March of the Machine','2023-04-21'),('Wilds of Eldraine','2023-09-08'),
    ('Lost Caverns of Ixalan','2023-11-17');

INSERT INTO carta (nombre, coste_mana, fuerza, resistencia, texto_habilidad, rareza, legendario, id_tipo_carta, id_tipo_secundario, id_edicion)
SELECT t.nombre, t.coste_mana, t.fuerza, t.resistencia, t.texto_habilidad, t.rareza, t.legendario,
       (SELECT id_tipo FROM tipo_carta WHERE nombre = t.tipo),
       (SELECT id_tipo FROM tipo_carta WHERE nombre = t.tipo_sec),
       (SELECT id_edicion FROM edicion WHERE nombre = t.edicion)
FROM (SELECT 'Serra Angel' nombre,5 coste_mana,4 fuerza,4 resistencia,'Vuelo, vigilancia.' texto_habilidad,'Infrecuente' rareza,FALSE legendario,'Criatura' tipo,NULL tipo_sec,'Dominaria United' edicion UNION ALL
      SELECT 'Llanowar Elves',1,1,1,'Tap: añade un maná verde.','Común',FALSE,'Criatura',NULL,'Dominaria United' UNION ALL
      SELECT 'Shivan Dragon',6,5,5,'Vuela. Tap: +1/+0 hasta fin de turno.','Rara',FALSE,'Criatura',NULL,'Dominaria United' UNION ALL
      SELECT 'Arcanis the Omnipotent',6,3,4,'Tap: roba tres cartas. Devuélvelo a tu mano.','Rara',TRUE,'Criatura',NULL,'The Brothers War' UNION ALL
      SELECT 'Tarmogoyf',2,NULL,NULL,'Fuerza/resistencia = nº de tipos de carta en cementerios.','Mítica',FALSE,'Criatura',NULL,'March of the Machine' UNION ALL
      SELECT 'Skrelv, Defector Mite',1,1,1,'Phyrexian. Da protección de un color hasta fin de turno.','Rara',TRUE,'Criatura',NULL,'March of the Machine' UNION ALL
      SELECT 'Restless Cottage',0,3,3,'Puede atacar como criatura 3/3 con mamporro.','Infrecuente',FALSE,'Criatura','Tierra','Wilds of Eldraine' UNION ALL
      SELECT 'Sentinel of Lost Lore',4,3,4,'Vuelo. Cuando entre, explora.','Infrecuente',FALSE,'Criatura',NULL,'Lost Caverns of Ixalan' UNION ALL
      SELECT 'Abuelo, Ancestry''s Echo',4,2,5,'Leyenda. Tap: devuelve artefacto o encantamiento del cementerio.','Mítica',TRUE,'Criatura',NULL,'Lost Caverns of Ixalan' UNION ALL
      SELECT 'Goblin Guide',1,2,2,'Prisa. Cuando ataque, el oponente roba una carta.','Rara',FALSE,'Criatura',NULL,'Lost Caverns of Ixalan' UNION ALL
      SELECT 'Birds of Paradise',1,0,1,'Vuelo. Tap: añade un maná de cualquier color.','Rara',FALSE,'Criatura',NULL,'March of the Machine' UNION ALL
      SELECT 'Snapcaster Mage',2,2,1,'Destello. Flashback a un instantáneo o hechizo del cementerio.','Mítica',FALSE,'Criatura',NULL,'The Brothers War' UNION ALL
      SELECT 'Lightning Bolt',1,NULL,NULL,'Inflige 3 puntos de daño a cualquier objetivo.','Común',FALSE,'Instantáneo',NULL,'Dominaria United' UNION ALL
      SELECT 'Counterspell',2,NULL,NULL,'Contrarresta el hechizo objetivo.','Infrecuente',FALSE,'Instantáneo',NULL,'The Brothers War' UNION ALL
      SELECT 'Dark Ritual',1,NULL,NULL,'Añade tres manás negros.','Común',FALSE,'Instantáneo',NULL,'The Brothers War' UNION ALL
      SELECT 'Path to Exile',1,NULL,NULL,'Exilia criatura objetivo. Su controlador puede buscar una tierra básica.','Infrecuente',FALSE,'Instantáneo',NULL,'Dominaria United' UNION ALL
      SELECT 'Naturalize',2,NULL,NULL,'Destruye artefacto o encantamiento objetivo.','Común',FALSE,'Instantáneo',NULL,'March of the Machine' UNION ALL
      SELECT 'Wrath of God',4,NULL,NULL,'Destruye todas las criaturas. No pueden regenerarse.','Rara',FALSE,'Hechizo',NULL,'Dominaria United' UNION ALL
      SELECT 'Thoughtseize',1,NULL,NULL,'El oponente muestra su mano; eliges una carta que no sea tierra y la descarta.','Rara',FALSE,'Hechizo',NULL,'Wilds of Eldraine' UNION ALL
      SELECT 'Demonic Tutor',2,NULL,NULL,'Busca cualquier carta de tu mazo y ponla en tu mano.','Rara',FALSE,'Hechizo',NULL,'The Brothers War' UNION ALL
      SELECT 'Llano de la Pradera',0,NULL,NULL,'Tierra básica. Tap: añade un maná blanco.','Común',FALSE,'Tierra',NULL,'Dominaria United' UNION ALL
      SELECT 'Isla',0,NULL,NULL,'Tierra básica. Tap: añade un maná azul.','Común',FALSE,'Tierra',NULL,'Dominaria United' UNION ALL
      SELECT 'Pantano',0,NULL,NULL,'Tierra básica. Tap: añade un maná negro.','Común',FALSE,'Tierra',NULL,'Dominaria United' UNION ALL
      SELECT 'Montaña',0,NULL,NULL,'Tierra básica. Tap: añade un maná rojo.','Común',FALSE,'Tierra',NULL,'Dominaria United' UNION ALL
      SELECT 'Bosque',0,NULL,NULL,'Tierra básica. Tap: añade un maná verde.','Común',FALSE,'Tierra',NULL,'Dominaria United' UNION ALL
      SELECT 'Roost of Drakes',1,NULL,NULL,'Cada vez que lances un hechizo azul, crea una ficha Drake 2/2.','Infrecuente',FALSE,'Encantamiento',NULL,'Wilds of Eldraine' UNION ALL
      SELECT 'Propaganda',3,NULL,NULL,'Los oponentes pagan 2 por cada criatura que ataque.','Infrecuente',FALSE,'Encantamiento',NULL,'March of the Machine' UNION ALL
      SELECT 'Wilderness Reclamation',4,NULL,NULL,'Al final de cada turno, desactiva todas tus tierras.','Infrecuente',FALSE,'Encantamiento',NULL,'Wilds of Eldraine' UNION ALL
      SELECT 'Sol Ring',1,NULL,NULL,'Tap: añade dos manás incoloros.','Infrecuente',FALSE,'Artefacto',NULL,'The Brothers War' UNION ALL
      SELECT 'Sword of Fire and Ice',3,NULL,NULL,'Equipar 2. +2/+2. Protección de rojo y azul.','Mítica',FALSE,'Artefacto',NULL,'The Brothers War'
     ) t;

INSERT INTO carta_color (id_carta, id_color)
SELECT c.id_carta, col.id_color FROM carta c JOIN color col ON 1=1
WHERE (c.nombre, col.nombre) IN (
    ('Serra Angel','Blanco'),('Llanowar Elves','Verde'),('Shivan Dragon','Rojo'),
    ('Arcanis the Omnipotent','Azul'),('Tarmogoyf','Verde'),('Tarmogoyf','Negro'),
    ('Skrelv, Defector Mite','Blanco'),('Restless Cottage','Verde'),
    ('Sentinel of Lost Lore','Negro'),('Abuelo, Ancestry''s Echo','Blanco'),
    ('Abuelo, Ancestry''s Echo','Negro'),('Goblin Guide','Rojo'),
    ('Birds of Paradise','Verde'),('Snapcaster Mage','Azul'),
    ('Lightning Bolt','Rojo'),('Counterspell','Azul'),('Dark Ritual','Negro'),
    ('Path to Exile','Blanco'),('Naturalize','Verde'),('Wrath of God','Blanco'),
    ('Thoughtseize','Negro'),('Demonic Tutor','Negro'),
    ('Llano de la Pradera','Blanco'),('Isla','Azul'),('Pantano','Negro'),
    ('Montaña','Rojo'),('Bosque','Verde'),('Roost of Drakes','Azul'),
    ('Propaganda','Azul'),('Wilderness Reclamation','Verde'),
    ('Sol Ring','Incoloro'),('Sword of Fire and Ice','Rojo'),('Sword of Fire and Ice','Azul')
);

INSERT INTO jugador (nombre, email, fecha_registro) VALUES
    ('Alejandro Vega','alejandro.vega@ejemplo.com','2024-01-15'),
    ('María Soto','maria.soto@ejemplo.com','2024-02-03'),
    ('Carlos Ruiz','carlos.ruiz@ejemplo.com','2024-03-22'),
    ('Laura Méndez','laura.mendez@ejemplo.com','2024-04-10'),
    ('Diego Fernández','diego.fernandez@ejemplo.com','2024-05-01');

INSERT INTO mazo (nombre, id_jugador)
SELECT t.nombre, (SELECT id_jugador FROM jugador WHERE nombre = t.jugador)
FROM (SELECT 'Agresión Roja' nombre,'Alejandro Vega' jugador UNION ALL
      SELECT 'Control Azul','María Soto' UNION ALL
      SELECT 'Elfos del Bosque','Carlos Ruiz' UNION ALL
      SELECT 'Ángeles Divinos','Laura Méndez' UNION ALL
      SELECT 'Artefactos Oscuros','Diego Fernández' UNION ALL
      SELECT 'Midrange Verde-Negro','Alejandro Vega') t;

INSERT INTO carta_mazo (id_mazo, id_carta, cantidad)
SELECT m.id_mazo, c.id_carta, t.cantidad
FROM (SELECT 'Agresión Roja' mazo,'Lightning Bolt' carta,4 cantidad UNION ALL SELECT 'Agresión Roja','Shivan Dragon',2 UNION ALL SELECT 'Agresión Roja','Goblin Guide',4 UNION ALL SELECT 'Agresión Roja','Montaña',12 UNION ALL
      SELECT 'Control Azul','Counterspell',4 UNION ALL SELECT 'Control Azul','Arcanis the Omnipotent',2 UNION ALL SELECT 'Control Azul','Roost of Drakes',3 UNION ALL SELECT 'Control Azul','Propaganda',2 UNION ALL SELECT 'Control Azul','Snapcaster Mage',3 UNION ALL SELECT 'Control Azul','Isla',12 UNION ALL
      SELECT 'Elfos del Bosque','Llanowar Elves',4 UNION ALL SELECT 'Elfos del Bosque','Tarmogoyf',2 UNION ALL SELECT 'Elfos del Bosque','Birds of Paradise',4 UNION ALL SELECT 'Elfos del Bosque','Naturalize',3 UNION ALL SELECT 'Elfos del Bosque','Bosque',12 UNION ALL
      SELECT 'Ángeles Divinos','Serra Angel',4 UNION ALL SELECT 'Ángeles Divinos','Wrath of God',2 UNION ALL SELECT 'Ángeles Divinos','Path to Exile',4 UNION ALL SELECT 'Ángeles Divinos','Skrelv, Defector Mite',2 UNION ALL SELECT 'Ángeles Divinos','Llano de la Pradera',12 UNION ALL
      SELECT 'Artefactos Oscuros','Sol Ring',4 UNION ALL SELECT 'Artefactos Oscuros','Sword of Fire and Ice',2 UNION ALL SELECT 'Artefactos Oscuros','Dark Ritual',4 UNION ALL SELECT 'Artefactos Oscuros','Demonic Tutor',2 UNION ALL SELECT 'Artefactos Oscuros','Pantano',10 UNION ALL
      SELECT 'Midrange Verde-Negro','Llanowar Elves',3 UNION ALL SELECT 'Midrange Verde-Negro','Tarmogoyf',4 UNION ALL SELECT 'Midrange Verde-Negro','Thoughtseize',4 UNION ALL SELECT 'Midrange Verde-Negro','Bosque',8 UNION ALL SELECT 'Midrange Verde-Negro','Pantano',8
     ) t JOIN mazo m ON m.nombre = t.mazo JOIN carta c ON c.nombre = t.carta;

INSERT INTO partida (fecha, id_jugador1, id_jugador2, id_ganador)
SELECT t.fecha,
       (SELECT id_jugador FROM jugador WHERE nombre = t.j1),
       (SELECT id_jugador FROM jugador WHERE nombre = t.j2),
       (SELECT id_jugador FROM jugador WHERE nombre = t.ganador)
FROM (SELECT '2024-06-01 18:00:00' fecha,'Alejandro Vega' j1,'María Soto' j2,'Alejandro Vega' ganador UNION ALL
      SELECT '2024-06-05 19:30:00','Carlos Ruiz','Laura Méndez','Laura Méndez' UNION ALL
      SELECT '2024-06-10 17:00:00','María Soto','Diego Fernández','María Soto' UNION ALL
      SELECT '2024-06-12 20:00:00','Alejandro Vega','Carlos Ruiz','Carlos Ruiz' UNION ALL
      SELECT '2024-06-15 18:45:00','Laura Méndez','Diego Fernández','Laura Méndez' UNION ALL
      SELECT '2024-06-20 16:00:00','María Soto','Carlos Ruiz','María Soto' UNION ALL
      SELECT '2024-06-22 21:00:00','Alejandro Vega','Diego Fernández','Alejandro Vega') t
UNION ALL
SELECT '2024-06-25 18:00:00',
       (SELECT id_jugador FROM jugador WHERE nombre='Carlos Ruiz'),
       (SELECT id_jugador FROM jugador WHERE nombre='Diego Fernández'), NULL;

-- ---------------------------------------------------------------
-- 8. PROCEDIMIENTO ALMACENADO Y FUNCIÓN
-- ---------------------------------------------------------------

DROP PROCEDURE IF EXISTS registrar_partida;
DELIMITER $$
CREATE PROCEDURE registrar_partida(
    IN  p_id_jugador1 INT,
    IN  p_id_jugador2 INT,
    IN  p_id_ganador  INT,
    OUT p_id_partida  INT
)
BEGIN
    IF p_id_jugador1 = p_id_jugador2 THEN
        SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Los dos jugadores deben ser distintos';
    END IF;
    INSERT INTO partida (fecha, id_jugador1, id_jugador2, id_ganador)
    VALUES (NOW(), p_id_jugador1, p_id_jugador2, p_id_ganador);
    SET p_id_partida = LAST_INSERT_ID();
END$$
DELIMITER ;

DROP FUNCTION IF EXISTS coste_total_mazo;
DELIMITER $$
CREATE FUNCTION coste_total_mazo(p_id_mazo INT)
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE total INT DEFAULT 0;
    SELECT SUM(c.coste_mana * cm.cantidad) INTO total
    FROM carta_mazo cm JOIN carta c ON c.id_carta = cm.id_carta
    WHERE cm.id_mazo = p_id_mazo;
    RETURN IFNULL(total, 0);
END$$
DELIMITER ;
