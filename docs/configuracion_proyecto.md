# Configuración del proyecto Java

## Requisitos

- Java JDK 11 o superior
- MySQL Server 8.x
- NetBeans 17+ o IntelliJ IDEA

## Driver JDBC

Descargar `mysql-connector-j-8.x.x.jar` desde https://dev.mysql.com/downloads/connector/j/
y colocarlo en la carpeta `lib/` del proyecto.

### NetBeans
1. Click derecho sobre el proyecto → Properties → Libraries
2. Add JAR/Folder → seleccionar `lib/mysql-connector-j-8.x.x.jar`

### IntelliJ IDEA
1. File → Project Structure → Modules → Dependencies
2. + → JARs or directories → seleccionar `lib/mysql-connector-j-8.x.x.jar`

## Base de datos

Editar `src/util/ConexionDB.java` con las credenciales correctas:

```java
private static final String URL      = "jdbc:mysql://localhost:3306/magic_cards";
private static final String USUARIO  = "tu_usuario";
private static final String CONTRASENA = "tu_contraseña";
```

Ejecutar el script `sql/schema.sql` para crear la base de datos y las tablas.

## Compilación

```bash
# Desde la raíz del proyecto
javac -cp lib/mysql-connector-j-8.x.x.jar -d out src/**/*.java
java  -cp out:lib/mysql-connector-j-8.x.x.jar view.MainFrame
```
