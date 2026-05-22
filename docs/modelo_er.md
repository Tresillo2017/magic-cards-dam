# Modelo Entidad-Relación

## Diagrama

```mermaid
erDiagram
    CARTA {
        INT id_carta PK
        VARCHAR nombre
        INT coste_mana
        INT fuerza
        INT resistencia
        TEXT texto_habilidad
        ENUM rareza
        BOOLEAN legendario
        INT id_tipo_carta FK
        INT id_tipo_secundario FK
        INT id_edicion FK
    }

    TIPO_CARTA {
        INT id_tipo PK
        VARCHAR nombre
    }

    COLOR {
        INT id_color PK
        VARCHAR nombre
    }

    CARTA_COLOR {
        INT id_carta FK
        INT id_color FK
    }

    EDICION {
        INT id_edicion PK
        VARCHAR nombre
        DATE fecha_lanzamiento
    }

    JUGADOR {
        INT id_jugador PK
        VARCHAR nombre
        VARCHAR email
        DATE fecha_registro
    }

    MAZO {
        INT id_mazo PK
        VARCHAR nombre
        INT id_jugador FK
    }

    CARTA_MAZO {
        INT id_mazo FK
        INT id_carta FK
        INT cantidad
    }

    PARTIDA {
        INT id_partida PK
        DATETIME fecha
        INT id_jugador1 FK
        INT id_jugador2 FK
        INT id_ganador FK
    }

    CARTA ||--o{ CARTA_COLOR : "tiene"
    COLOR ||--o{ CARTA_COLOR : "pertenece a"
    TIPO_CARTA ||--o{ CARTA : "clasifica"
    TIPO_CARTA ||--o{ CARTA : "tipo secundario"
    EDICION ||--o{ CARTA : "incluye"
    JUGADOR ||--o{ MAZO : "posee"
    MAZO ||--o{ CARTA_MAZO : "contiene"
    CARTA ||--o{ CARTA_MAZO : "está en"
    JUGADOR ||--o{ PARTIDA : "juega como j1"
    JUGADOR ||--o{ PARTIDA : "juega como j2"
    JUGADOR ||--o{ PARTIDA : "gana"
```

## Entidades y atributos

### CARTA
Entidad central del sistema. Almacena todos los datos de una carta coleccionable.
- `id_carta`: Clave primaria
- `nombre`: Nombre de la carta
- `coste_mana`: Coste numérico total de maná
- `fuerza` / `resistencia`: Solo para criaturas (NULL en otros tipos)
- `texto_habilidad`: Descripción del efecto
- `rareza`: ENUM (Común, Infrecuente, Rara, Mítica)
- `legendario`: Indica si es supertipo Legendario
- `id_tipo_carta`: FK al tipo principal (Criatura, Hechizo, Tierra...)
- `id_tipo_secundario`: FK al tipo secundario opcional
- `id_edicion`: FK a la edición

### TIPO_CARTA
Catálogo de tipos: Criatura, Hechizo, Tierra, Instantáneo, Encantamiento, Artefacto.

### COLOR
Catálogo de colores de maná: Blanco, Azul, Negro, Rojo, Verde, Incoloro.

### CARTA_COLOR
Tabla puente N:M entre CARTA y COLOR (una carta puede tener varios colores).

### EDICION
Colecciones o expansiones del juego.

### JUGADOR
Usuarios registrados en el sistema.

### MAZO
Colección de cartas que pertenece a un jugador.

### CARTA_MAZO
Tabla puente N:M entre MAZO y CARTA con cantidad de copias.

### PARTIDA
Registro de enfrentamientos entre dos jugadores con resultado.

## Relaciones principales

| Relación | Cardinalidad | Descripción |
|----------|-------------|-------------|
| JUGADOR – MAZO | 1:N | Un jugador tiene varios mazos |
| MAZO – CARTA | N:M (via CARTA_MAZO) | Un mazo contiene muchas cartas; una carta puede estar en varios mazos |
| CARTA – COLOR | N:M (via CARTA_COLOR) | Una carta puede tener varios colores |
| TIPO_CARTA – CARTA | 1:N | Un tipo clasifica muchas cartas |
| EDICION – CARTA | 1:N | Una edición incluye muchas cartas |
| JUGADOR – PARTIDA | 1:N | Un jugador participa en muchas partidas |
