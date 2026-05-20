# Arquitectura MVC del proyecto

## Estructura de paquetes

```
src/
├── model/
│   ├── Carta.java
│   ├── TipoCarta.java
│   ├── Color.java
│   ├── Edicion.java
│   ├── Jugador.java
│   ├── Mazo.java
│   ├── CartaMazo.java
│   └── Partida.java
├── dao/
│   ├── CartaDAO.java
│   ├── JugadorDAO.java
│   ├── MazoDAO.java
│   ├── CartaMazoDAO.java
│   └── PartidaDAO.java
├── view/
│   ├── MainFrame.java
│   ├── CartaView.java
│   ├── JugadorView.java
│   ├── MazoView.java
│   ├── PartidaView.java
│   └── ConsultasView.java
├── controller/
│   ├── CartaController.java
│   ├── JugadorController.java
│   ├── MazoController.java
│   └── PartidaController.java
└── util/
    ├── ConexionDB.java
    └── Validaciones.java
```

## Capas

### Model (`src/model/`)
POJOs Java que representan las entidades del dominio. Sin lógica de negocio ni acceso a BD.
Cada clase tiene: atributos privados, constructores, getters/setters y `toString()`.

### DAO (`src/dao/`)
Capa de acceso a datos usando JDBC puro (sin ORM). Cada DAO implementa operaciones CRUD
sobre su entidad usando `PreparedStatement` para prevenir SQL injection.

Convención: métodos `insertar()`, `obtenerPorId()`, `listarTodos()`, `actualizar()`, `eliminar()`.

### View (`src/view/`)
Ventanas y paneles Swing. Responsables únicamente de mostrar datos y capturar eventos de usuario.
No acceden directamente a DAO ni a la BD: delegan en el Controller correspondiente.

### Controller (`src/controller/`)
Intermediarios entre View y DAO. Gestionan la lógica de validación, coordinan las operaciones
y actualizan la vista con los resultados.

### Util (`src/util/`)
- `ConexionDB`: Singleton que gestiona la conexión JDBC a MySQL.
- `Validaciones`: Métodos estáticos para validar campos de formulario.

## Flujo de datos

```
Usuario → View → Controller → DAO → MySQL
                    ↓
               Model (POJOs)
```

## Dependencias externas

- `mysql-connector-j-8.x.x.jar` en el classpath del proyecto
- Java 11 o superior
- MySQL 8.x
