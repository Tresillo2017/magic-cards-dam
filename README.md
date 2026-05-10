# Magic: The Gathering - Gestión de Cartas Coleccionables

Práctica Final - Programación y Bases de Datos (DAM)

Aplicación de escritorio en Java con interfaz gráfica (Swing) y base de datos MySQL para gestionar un juego de cartas coleccionable inspirado en Magic: The Gathering.

## Temática y reglas básicas (alcance funcional)

### Tipos de carta
La aplicación debe contemplar, como mínimo, estos tipos:
- Criatura
- Hechizo
- Tierra
- Instantáneo
- Encantamiento
- Artefacto

### Atributos de carta (BD y app)
Cada carta deberá poder almacenar:
- Nombre
- Coste de maná
- Fuerza/Resistencia (solo obligatorio para cartas de tipo Criatura)
- Texto de habilidad
- Rareza
- Edición
- Tipo de carta

### Colores de maná
Se utilizarán los cinco colores básicos de Magic:
- Blanco
- Azul
- Negro
- Rojo
- Verde

### Reglas básicas a reflejar
- El mazo se compone de cartas válidas registradas en la base de datos.
- Las tierras son la fuente principal de maná para pagar costes.
- El coste de maná condiciona cuándo una carta puede jugarse.
- Solo las criaturas tienen fuerza y resistencia como atributos de combate.
- El texto de habilidad define efectos especiales y comportamiento de la carta.
- La rareza y la edición se usan para clasificación, consulta y filtrado.

## Tecnologías
- Java + Java Swing
- MySQL
- JDBC (sin ORM)

## Estructura del proyecto
```
src/
  model/       # Clases del modelo (Carta, Jugador, Mazo, Partida...)
  dao/         # Capa de acceso a datos (JDBC)
  view/        # Interfaz gráfica (Swing)
  controller/  # Controladores
sql/           # Scripts SQL
docs/          # Documentación (Manual de usuario, Modelo E/R, JavaDoc)
```
