package model;

public class Jugador {

    private int idJugador;
    private String nombre;
    private String email;
    private String fechaRegistro;
    private int numMazos;

    public Jugador() {}

    public int getIdJugador() { return idJugador; }
    public void setIdJugador(int idJugador) { this.idJugador = idJugador; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    public int getNumMazos() { return numMazos; }
    public void setNumMazos(int numMazos) { this.numMazos = numMazos; }

    @Override
    public String toString() { return nombre; }
}
