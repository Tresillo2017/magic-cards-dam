# Manual de Usuario — Magic Cards DAM

## Descripción

Magic Cards DAM es una aplicación de escritorio desarrollada en Java que permite gestionar un juego de cartas coleccionable inspirado en Magic: The Gathering. Permite registrar cartas, jugadores, mazos y partidas, y ejecutar consultas avanzadas sobre los datos.

---

## Requisitos previos

- Java 11 o superior instalado
- Base de datos MySQL en ejecución (ver sección Configuración)
- Fichero `src/db.properties` con las credenciales de conexión

---

## Puesta en marcha

### Opción A — Docker (recomendado)

1. Asegúrate de tener Docker instalado y en ejecución.
2. Abre una terminal en la carpeta del proyecto y ejecuta:
   ```
   docker compose up -d
   ```
3. Ejecuta el esquema y los datos de prueba:
   ```
   docker exec -i magic_cards_db mysql -u magic_user -pEbHuYuWuA5EKvr21tEjuXZq < sql/schema.sql
   docker exec -i magic_cards_db mysql -u magic_user -pEbHuYuWuA5EKvr21tEjuXZq < sql/seed.sql
   docker exec -i magic_cards_db mysql -u magic_user -pEbHuYuWuA5EKvr21tEjuXZq < sql/procedimientos.sql
   ```
4. Lanza la aplicación desde IntelliJ o NetBeans ejecutando la clase `view.MainFrame`.

### Opción B — XAMPP

1. Abre el Panel de Control de XAMPP y arranca el módulo **MySQL**.
2. Accede a **phpMyAdmin** (`http://localhost/phpmyadmin`).
3. Crea la base de datos `magic_cards` con cotejamiento `utf8mb4_unicode_ci`.
4. Selecciona la base de datos, ve a la pestaña **SQL** y ejecuta el contenido de:
   - `sql/schema.sql`
   - `sql/seed.sql`
   - `sql/procedimientos.sql`
5. Edita `src/db.properties` con las credenciales de XAMPP:
   ```
   db.url=jdbc:mysql://localhost:3306/magic_cards
   db.usuario=root
   db.contrasena=
   ```
6. Lanza la aplicación ejecutando la clase `view.MainFrame`.

---

## Navegación principal

La ventana principal contiene una barra de menú con el menú **Gestión**. Desde él puedes acceder a todas las secciones:

| Opción de menú       | Descripción                                      |
|----------------------|--------------------------------------------------|
| Cartas               | Gestión completa del catálogo de cartas          |
| Jugadores            | Registro y edición de jugadores                  |
| Mazos                | Gestión de mazos y sus cartas                    |
| Partidas             | Registro y consulta de partidas                  |
| Consultas avanzadas  | Consultas SQL, función y procedimiento           |

---

## Sección: Cartas

Muestra el listado de todas las cartas en una tabla con columnas ID, Nombre, Tipo, Coste de maná, Rareza y Edición.

### Buscar una carta
Escribe parte del nombre en el campo **Buscar por nombre** y pulsa el botón **Buscar**. Para volver a ver todas las cartas, pulsa **Mostrar todos**.

### Añadir una carta
1. Pulsa **Nueva** para limpiar el formulario.
2. Rellena los campos obligatorios: Nombre, Coste de maná, Rareza, Tipo y Edición.
3. Los campos Fuerza y Resistencia solo son necesarios para cartas de tipo **Criatura**.
4. Pulsa **Guardar**. La carta aparecerá en la tabla.

### Editar una carta
1. Haz clic sobre la carta en la tabla; los datos se cargarán en el formulario.
2. Modifica los campos necesarios y pulsa **Guardar**.

### Eliminar una carta
1. Selecciona la carta en la tabla.
2. Pulsa **Eliminar** y confirma la acción en el diálogo.

---

## Sección: Jugadores

Muestra el listado de jugadores con sus datos y el número de mazos que posee cada uno.

### Añadir un jugador
1. Pulsa **Nuevo**.
2. Rellena **Nombre** y **Email** (obligatorios). La fecha de registro la asigna la base de datos automáticamente.
3. Pulsa **Guardar**.

### Editar un jugador
1. Selecciona el jugador en la tabla.
2. Modifica nombre o email y pulsa **Guardar**.

### Eliminar un jugador
Selecciona el jugador y pulsa **Eliminar**. Se eliminarán también sus mazos asociados.

---

## Sección: Mazos

La pantalla está dividida en dos partes: la lista de mazos (izquierda) y las cartas del mazo seleccionado (derecha).

### Crear un mazo
1. Pulsa **Nuevo**.
2. Escribe el nombre del mazo y selecciona el jugador propietario.
3. Pulsa **Guardar**.

### Añadir cartas a un mazo
1. Selecciona el mazo en la tabla izquierda.
2. Elige la carta en el combo **Carta** y escribe la cantidad.
3. Pulsa **Añadir carta**. Si la carta ya estaba en el mazo, se actualiza la cantidad.

### Quitar una carta del mazo
1. Selecciona la carta en la tabla de la derecha.
2. Pulsa **Quitar carta**.

### Eliminar un mazo
Selecciona el mazo y pulsa **Eliminar**. Se eliminarán también todas sus cartas.

---

## Sección: Partidas

Muestra el historial de partidas con fecha, jugadores y resultado.

### Registrar una partida
1. Pulsa **Nueva**.
2. Selecciona **Jugador 1** y **Jugador 2** (deben ser distintos).
3. Si la partida ya tiene resultado, selecciona el **Ganador**; si no, deja **(en curso)**.
4. Pulsa **Guardar**.

### Actualizar el resultado
1. Selecciona la partida en la tabla.
2. Cambia el ganador en el combo y pulsa **Guardar**.
3. Para marcar una partida como "en curso" de nuevo, selecciona **(en curso)** y pulsa **Guardar**.

### Eliminar una partida
Selecciona la partida y pulsa **Eliminar**.

---

## Sección: Consultas avanzadas

Esta sección agrupa 6 operaciones sobre la base de datos.

| Nº | Descripción | Parámetro necesario |
|----|-------------|---------------------|
| 1  | Cartas por tipo y color | Ninguno |
| 2  | Mazos con más cartas por jugador | Ninguno |
| 3  | Jugadores con más victorias | Ninguno |
| 4  | Cartas sobre la media de coste | Seleccionar edición |
| 5  | Coste total de maná de un mazo (función SQL) | Seleccionar mazo |
| 6  | Registrar partida (procedimiento almacenado) | Jugador 1, Jugador 2, Ganador |

Para ejecutar cualquier consulta, haz clic en su botón. Los resultados aparecen en la tabla inferior. La barra de estado muestra el número de registros encontrados.

---

## Mensajes de error frecuentes

| Mensaje | Causa | Solución |
|---------|-------|----------|
| No se puede conectar a la base de datos | MySQL no está en ejecución o las credenciales son incorrectas | Arranca Docker / XAMPP y revisa `db.properties` |
| El nombre es obligatorio | Se intentó guardar un registro sin nombre | Rellena el campo Nombre |
| Los jugadores deben ser distintos | Se seleccionó el mismo jugador en J1 y J2 | Elige jugadores diferentes |
| El ganador debe ser uno de los dos jugadores | Se eligió un ganador que no participa | Selecciona J1, J2 o "(en curso)" |
| La cantidad debe ser mayor que 0 | Se introdujo un valor inválido en el campo cantidad | Escribe un número entero positivo |
