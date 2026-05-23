-- Procedimientos almacenados y funciones SQL
-- Ejecutar después de schema.sql y seed.sql

USE magic_cards;

-- -------------------------------------------------------
-- Procedimiento: registrar_partida
-- Registra una nueva partida entre dos jugadores.
-- Parámetros de entrada: id de jugador1, id de jugador2, id del ganador (puede ser NULL)
-- Parámetro de salida:   id de la partida creada
-- -------------------------------------------------------
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


-- -------------------------------------------------------
-- Función: coste_total_mazo
-- Devuelve la suma del coste de maná de todas las cartas
-- de un mazo, ponderada por la cantidad de copias.
-- -------------------------------------------------------
DROP FUNCTION IF EXISTS coste_total_mazo;

DELIMITER $$
CREATE FUNCTION coste_total_mazo(p_id_mazo INT)
RETURNS INT
READS SQL DATA
BEGIN
    DECLARE total INT DEFAULT 0;

    SELECT SUM(c.coste_mana * cm.cantidad)
    INTO total
    FROM carta_mazo cm
    JOIN carta c ON c.id_carta = cm.id_carta
    WHERE cm.id_mazo = p_id_mazo;

    RETURN IFNULL(total, 0);
END$$
DELIMITER ;
