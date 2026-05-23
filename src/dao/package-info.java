/**
 * Capa de acceso a datos (DAO).
 * Los DAOs de entidad ({@link dao.CartaDAO}, {@link dao.JugadorDAO},
 * {@link dao.MazoDAO}, {@link dao.PartidaDAO}) implementan la interfaz
 * genérica {@link dao.DAO} que define las operaciones CRUD básicas.
 * Los DAOs auxiliares ({@link dao.CartaMazoDAO}, {@link dao.ConsultasDAO},
 * {@link dao.TipoCartaDAO}, {@link dao.EdicionDAO}) ofrecen operaciones
 * específicas de su dominio sin seguir el patrón CRUD completo.
 */
package dao;
