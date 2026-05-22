package model;

import java.time.LocalDate;

public class Jugador {

    private int idJugador;
    private String nombre;
    private String email;
    private LocalDate fechaRegistro;

    public Jugador() {}

    public Jugador(int idJugador, String nombre, String email, LocalDate fechaRegistro) {
        this.idJugador = idJugador;
        this.nombre = nombre;
        this.email = email;
        this.fechaRegistro = fechaRegistro;
    }

    public int getIdJugador() { return idJugador; }
    public void setIdJugador(int idJugador) { this.idJugador = idJugador; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDate fechaRegistro) { this.fechaRegistro = fechaRegistro; }

    @Override
    public String toString() { return nombre + " <" + email + ">"; }
}
