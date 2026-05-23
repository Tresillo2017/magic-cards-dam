-- Datos de prueba para Magic Cards DAM
-- Ejecutar después de schema.sql

USE magic_cards;

-- -------------------------------------------------------
-- Tipos de carta
-- -------------------------------------------------------
INSERT INTO tipo_carta (nombre) VALUES
    ('Criatura'),
    ('Hechizo'),
    ('Tierra'),
    ('Instantáneo'),
    ('Encantamiento'),
    ('Artefacto');

-- -------------------------------------------------------
-- Colores de maná
-- -------------------------------------------------------
INSERT INTO color (nombre) VALUES
    ('Blanco'),
    ('Azul'),
    ('Negro'),
    ('Rojo'),
    ('Verde'),
    ('Incoloro');

-- -------------------------------------------------------
-- Ediciones
-- -------------------------------------------------------
INSERT INTO edicion (nombre, fecha_lanzamiento) VALUES
    ('Dominaria United',       '2022-09-09'),
    ('The Brothers War',       '2022-11-18'),
    ('March of the Machine',   '2023-04-21'),
    ('Wilds of Eldraine',      '2023-09-08'),
    ('Lost Caverns of Ixalan', '2023-11-17');

-- -------------------------------------------------------
-- Cartas (id_tipo: 1=Criatura 2=Hechizo 3=Tierra 4=Instantáneo 5=Encantamiento 6=Artefacto)
-- id_edicion: 1=Dominaria 2=Brothers War 3=March 4=Wilds 5=Ixalan
-- -------------------------------------------------------
INSERT INTO carta (nombre, coste_mana, fuerza, resistencia, texto_habilidad, rareza, legendario, id_tipo_carta, id_tipo_secundario, id_edicion) VALUES
    -- Criaturas
    ('Serra Angel',          5, 4, 4, 'Vuelo, vigilancia.',                                          'Infrecuente', FALSE, 1, NULL, 1),
    ('Llanowar Elves',       1, 1, 1, 'Tap: añade un maná verde.',                                   'Común',       FALSE, 1, NULL, 1),
    ('Shivan Dragon',        6, 5, 5, 'Vuela. Tap: +1/+0 hasta fin de turno.',                       'Rara',        FALSE, 1, NULL, 1),
    ('Arcanis the Omnipotent', 6, 3, 4, 'Tap: roba tres cartas. Devuélvelo a tu mano.',              'Rara',        TRUE,  1, NULL, 2),
    ('Tarmogoyf',            2, NULL, NULL, 'Fuerza/resistencia = nº de tipos de carta en cementerios.', 'Mítica',  FALSE, 1, NULL, 3),
    ('Skrelv, Defector Mite',1, 1, 1, 'Phyrexian. Da protección de un color hasta fin de turno.',   'Rara',        TRUE,  1, NULL, 3),
    ('Restless Cottage',     0, 3, 3, 'Puede atacar como criatura 3/3 con mamporro.',                'Infrecuente', FALSE, 1, 3,    4),
    ('Roost of Drakes',      1, NULL, NULL, 'Cada vez que lances un hechizo azul, crea una ficha Drake 2/2.', 'Infrecuente', FALSE, 5, NULL, 4),
    ('Sentinel of Lost Lore',4, 3, 4, 'Vuelo. Cuando entre, explora.',                              'Infrecuente', FALSE, 1, NULL, 5),
    ('Abuelo, Ancestry''s Echo', 4, 2, 5, 'Leyenda. Tap: devuelve artefacto o encantamiento del cementerio.', 'Mítica', TRUE, 1, NULL, 5),
    -- Hechizos e instantáneos
    ('Lightning Bolt',       1, NULL, NULL, 'Inflige 3 puntos de daño a cualquier objetivo.',        'Común',       FALSE, 4, NULL, 1),
    ('Counterspell',         2, NULL, NULL, 'Contrarresta el hechizo objetivo.',                      'Infrecuente', FALSE, 4, NULL, 2),
    ('Dark Ritual',          1, NULL, NULL, 'Añade tres manás negros.',                              'Común',       FALSE, 4, NULL, 2),
    ('Wrath of God',         4, NULL, NULL, 'Destruye todas las criaturas. No pueden regenerarse.',   'Rara',        FALSE, 2, NULL, 1),
    ('Naturalize',           2, NULL, NULL, 'Destruye artefacto o encantamiento objetivo.',           'Común',       FALSE, 4, NULL, 3),
    -- Tierras
    ('Llano de la Pradera',  0, NULL, NULL, 'Tierra básica. Tap: añade un maná blanco.',             'Común',       FALSE, 3, NULL, 1),
    ('Isla',                 0, NULL, NULL, 'Tierra básica. Tap: añade un maná azul.',               'Común',       FALSE, 3, NULL, 1),
    ('Pantano',              0, NULL, NULL, 'Tierra básica. Tap: añade un maná negro.',              'Común',       FALSE, 3, NULL, 1),
    ('Montaña',              0, NULL, NULL, 'Tierra básica. Tap: añade un maná rojo.',               'Común',       FALSE, 3, NULL, 1),
    ('Bosque',               0, NULL, NULL, 'Tierra básica. Tap: añade un maná verde.',              'Común',       FALSE, 3, NULL, 1),
    -- Artefactos y encantamientos
    ('Sol Ring',             1, NULL, NULL, 'Tap: añade dos manás incoloros.',                        'Infrecuente', FALSE, 6, NULL, 2),
    ('Sword of Fire and Ice', 3, NULL, NULL, 'Equipar 2. +2/+2. Protección de rojo y azul.',         'Mítica',      FALSE, 6, NULL, 2),
    ('Propaganda',           3, NULL, NULL, 'Los oponentes pagan 2 por cada criatura que ataque.',   'Infrecuente', FALSE, 5, NULL, 3),
    ('Crucible of Worlds',   3, NULL, NULL, 'Puedes jugar tierras desde tu cementerio.',             'Mítica',      FALSE, 6, NULL, 3),
    ('Wilderness Reclamation',4, NULL, NULL, 'Al final de cada turno, desactiva todas tus tierras.', 'Infrecuente', FALSE, 5, NULL, 4);

-- -------------------------------------------------------
-- Colores de las cartas
-- (1=Blanco 2=Azul 3=Negro 4=Rojo 5=Verde 6=Incoloro)
-- -------------------------------------------------------
INSERT INTO carta_color (id_carta, id_color) VALUES
    (1,  1), -- Serra Angel          → Blanco
    (2,  5), -- Llanowar Elves       → Verde
    (3,  4), -- Shivan Dragon        → Rojo
    (4,  2), -- Arcanis              → Azul
    (5,  5), -- Tarmogoyf            → Verde
    (5,  3), -- Tarmogoyf            → Negro (multicolor)
    (6,  1), -- Skrelv               → Blanco
    (7,  5), -- Restless Cottage     → Verde (tierra criatura)
    (8,  2), -- Roost of Drakes      → Azul
    (9,  3), -- Sentinel             → Negro
    (10, 1), -- Abuelo               → Blanco
    (10, 3), -- Abuelo               → Negro
    (11, 4), -- Lightning Bolt       → Rojo
    (12, 2), -- Counterspell         → Azul
    (13, 3), -- Dark Ritual          → Negro
    (14, 1), -- Wrath of God         → Blanco
    (15, 5), -- Naturalize           → Verde
    (16, 1), -- Pradera              → Blanco
    (17, 2), -- Isla                 → Azul
    (18, 3), -- Pantano              → Negro
    (19, 4), -- Montaña              → Rojo
    (20, 5), -- Bosque               → Verde
    (21, 6), -- Sol Ring             → Incoloro
    (22, 4), -- Sword of Fire        → Rojo
    (22, 2), -- Sword of Fire        → Azul
    (23, 2), -- Propaganda           → Azul
    (24, 6), -- Crucible of Worlds   → Incoloro
    (25, 5); -- Wilderness Rec.      → Verde

-- -------------------------------------------------------
-- Jugadores
-- -------------------------------------------------------
INSERT INTO jugador (nombre, email, fecha_registro) VALUES
    ('Alejandro Vega',   'alejandro.vega@ejemplo.com',  '2024-01-15'),
    ('María Soto',       'maria.soto@ejemplo.com',      '2024-02-03'),
    ('Carlos Ruiz',      'carlos.ruiz@ejemplo.com',     '2024-03-22'),
    ('Laura Méndez',     'laura.mendez@ejemplo.com',    '2024-04-10'),
    ('Diego Fernández',  'diego.fernandez@ejemplo.com', '2024-05-01');

-- -------------------------------------------------------
-- Mazos
-- -------------------------------------------------------
INSERT INTO mazo (nombre, id_jugador) VALUES
    ('Agresión Roja',       1),
    ('Control Azul',        2),
    ('Elfos del Bosque',    3),
    ('Ángeles Divinos',     4),
    ('Artefactos Oscuros',  5),
    ('Midrange Verde-Negro',1);

-- -------------------------------------------------------
-- Cartas en los mazos
-- -------------------------------------------------------
INSERT INTO carta_mazo (id_mazo, id_carta, cantidad) VALUES
    -- Mazo 1: Agresión Roja
    (1, 11, 4), -- Lightning Bolt x4
    (1, 3,  2), -- Shivan Dragon x2
    (1, 19, 12),-- Montaña x12
    -- Mazo 2: Control Azul
    (2, 12, 4), -- Counterspell x4
    (2, 4,  2), -- Arcanis x2
    (2, 8,  3), -- Roost of Drakes x3
    (2, 23, 2), -- Propaganda x2
    (2, 17, 12),-- Isla x12
    -- Mazo 3: Elfos del Bosque
    (3, 2,  4), -- Llanowar Elves x4
    (3, 5,  2), -- Tarmogoyf x2
    (3, 15, 3), -- Naturalize x3
    (3, 20, 12),-- Bosque x12
    -- Mazo 4: Ángeles Divinos
    (4, 1,  4), -- Serra Angel x4
    (4, 14, 2), -- Wrath of God x2
    (4, 6,  2), -- Skrelv x2
    (4, 16, 12),-- Pradera x12
    -- Mazo 5: Artefactos Oscuros
    (5, 21, 4), -- Sol Ring x4
    (5, 22, 2), -- Sword of Fire and Ice x2
    (5, 24, 2), -- Crucible of Worlds x2
    (5, 13, 4), -- Dark Ritual x4
    (5, 18, 10),-- Pantano x10
    -- Mazo 6: Midrange Verde-Negro
    (6, 2,  3), -- Llanowar Elves x3
    (6, 5,  4), -- Tarmogoyf x4
    (6, 13, 3), -- Dark Ritual x3
    (6, 20, 8), -- Bosque x8
    (6, 18, 8); -- Pantano x8

-- -------------------------------------------------------
-- Partidas
-- -------------------------------------------------------
INSERT INTO partida (fecha, id_jugador1, id_jugador2, id_ganador) VALUES
    ('2024-06-01 18:00:00', 1, 2, 1),
    ('2024-06-05 19:30:00', 3, 4, 4),
    ('2024-06-10 17:00:00', 2, 5, 2),
    ('2024-06-12 20:00:00', 1, 3, 3),
    ('2024-06-15 18:45:00', 4, 5, 4),
    ('2024-06-20 16:00:00', 2, 3, 2),
    ('2024-06-22 21:00:00', 1, 5, 1),
    ('2024-06-25 18:00:00', 3, 5, NULL); -- partida en curso, sin ganador
