# Modelo Relacional

Transformación del E/R al esquema relacional MySQL.

> **Nota**: los bloques de esta sección son pseudocódigo descriptivo.
> El SQL ejecutable completo se encuentra en [`sql/schema.sql`](../sql/schema.sql).

## Tablas

### tipo_carta
```sql
tipo_carta(id_tipo INT PK AUTO_INCREMENT, nombre VARCHAR(50) NOT NULL UNIQUE)
```

### color
```sql
color(id_color INT PK AUTO_INCREMENT, nombre VARCHAR(20) NOT NULL UNIQUE)
```

### edicion
```sql
edicion(id_edicion INT PK AUTO_INCREMENT, nombre VARCHAR(100) NOT NULL, fecha_lanzamiento DATE)
```

### carta
```sql
carta(
  id_carta       INT PK AUTO_INCREMENT,
  nombre         VARCHAR(150) NOT NULL,
  coste_mana     INT NOT NULL DEFAULT 0 CHECK (coste_mana >= 0),
  fuerza         INT NULL,
  resistencia    INT NULL,
  texto_habilidad TEXT,
  rareza         ENUM('Común','Infrecuente','Rara','Mítica') NOT NULL,
  legendario     BOOLEAN NOT NULL DEFAULT FALSE,
  id_tipo_carta  INT NOT NULL FK -> tipo_carta(id_tipo),
  id_tipo_secundario INT NULL FK -> tipo_carta(id_tipo),
  id_edicion     INT NOT NULL FK -> edicion(id_edicion)
)
```
**Restricción**: fuerza y resistencia solo son obligatorios cuando el tipo principal es Criatura.

### carta_color
```sql
carta_color(id_carta INT FK -> carta, id_color INT FK -> color, PK(id_carta, id_color))
```

### jugador
```sql
jugador(
  id_jugador      INT PK AUTO_INCREMENT,
  nombre          VARCHAR(100) NOT NULL,
  email           VARCHAR(150) NOT NULL UNIQUE,
  fecha_registro  DATE NOT NULL DEFAULT (CURRENT_DATE)
)
```

### mazo
```sql
mazo(id_mazo INT PK AUTO_INCREMENT, nombre VARCHAR(100) NOT NULL, id_jugador INT NOT NULL FK -> jugador)
```

### carta_mazo
```sql
carta_mazo(
  id_mazo   INT FK -> mazo,
  id_carta  INT FK -> carta,
  cantidad  INT NOT NULL DEFAULT 1 CHECK (cantidad > 0),
  PK(id_mazo, id_carta)
)
```

### partida
```sql
partida(
  id_partida  INT PK AUTO_INCREMENT,
  fecha       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  id_jugador1 INT NOT NULL FK -> jugador,
  id_jugador2 INT NOT NULL FK -> jugador,
  id_ganador  INT NULL FK -> jugador,
  CHECK (id_jugador1 <> id_jugador2)
)
```

## Índices recomendados

- `carta(nombre)` — búsqueda por nombre
- `carta(id_tipo_carta, rareza)` — filtros de catálogo
- `carta_mazo(id_mazo)` — obtener cartas de un mazo
- `partida(id_jugador1)`, `partida(id_jugador2)` — historial de partidas
