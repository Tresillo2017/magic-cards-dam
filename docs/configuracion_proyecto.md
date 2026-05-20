# Configuración del proyecto Java

## Requisitos

- Java JDK 11 o superior
- MySQL Server 8.x (o Docker, ver más abajo)
- NetBeans 17+ o IntelliJ IDEA

## Driver JDBC

Descargar `mysql-connector-j-8.x.x.jar` desde https://dev.mysql.com/downloads/connector/j/
y colocarlo en la carpeta `lib/` del proyecto.

### NetBeans
1. Click derecho sobre el proyecto → Properties → Libraries
2. Add JAR/Folder → seleccionar `lib/mysql-connector-j-8.x.x.jar`

### IntelliJ IDEA
1. File → Project Structure → Modules → Dependencies
2. `+` → JARs or directories → seleccionar `lib/mysql-connector-j-8.x.x.jar`

## Configuración de la base de datos

La conexión se configura mediante el fichero `src/db.properties` (no versionado) o variables de entorno.

Copiar el fichero de ejemplo:
```bash
cp src/db.properties.example src/db.properties
```

Editar `src/db.properties` con tus credenciales:
```properties
db.url=jdbc:mysql://localhost:3306/magic_cards
db.usuario=magic_user
db.contrasena=tu_contraseña
```

También se pueden usar variables de entorno (tienen prioridad):
```bash
export DB_URL=jdbc:mysql://localhost:3306/magic_cards
export DB_USUARIO=magic_user
export DB_CONTRASENA=tu_contraseña
```

### Opción A — Docker (recomendado para desarrollo)

```bash
docker compose up -d
```

Levanta MySQL 8.4 en `localhost:3306` con la base de datos `magic_cards` ya creada.

### Opción B — MySQL local

Ejecutar el script para crear las tablas:
```bash
mysql -u tu_usuario -p < sql/schema.sql
```

## Compilación

```bash
# Desde la raíz del proyecto
find src -name "*.java" | xargs javac -cp lib/mysql-connector-j-8.x.x.jar -d out

# Ejecución (pendiente hasta Sprint 4 — clase MainFrame aún no implementada)
java -cp out:lib/mysql-connector-j-8.x.x.jar view.MainFrame
```
