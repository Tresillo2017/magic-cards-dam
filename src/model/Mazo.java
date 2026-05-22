package model;

public class Mazo {

    private int idMazo;
    private String nombre;
    private Jugador jugador;

    public Mazo() {}

    public Mazo(int idMazo, String nombre, Jugador jugador) {
        this.idMazo = idMazo;
        this.nombre = nombre;
        this.jugador = jugador;
    }

    public int getIdMazo() { return idMazo; }
    public void setIdMazo(int idMazo) { this.idMazo = idMazo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Jugador getJugador() { return jugador; }
    public void setJugador(Jugador jugador) { this.jugador = jugador; }

    @Override
    public String toString() { return nombre; }
}
