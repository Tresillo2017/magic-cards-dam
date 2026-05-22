package model;

import java.time.LocalDateTime;

public class Partida {

    private int idPartida;
    private LocalDateTime fecha;
    private Jugador jugador1;
    private Jugador jugador2;
    private Jugador ganador;

    public Partida() {}

    public int getIdPartida() { return idPartida; }
    public void setIdPartida(int idPartida) { this.idPartida = idPartida; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    public Jugador getJugador1() { return jugador1; }
    public void setJugador1(Jugador jugador1) { this.jugador1 = jugador1; }

    public Jugador getJugador2() { return jugador2; }
    public void setJugador2(Jugador jugador2) { this.jugador2 = jugador2; }

    public Jugador getGanador() { return ganador; }
    public void setGanador(Jugador ganador) { this.ganador = ganador; }

    @Override
    public String toString() {
        return jugador1 + " vs " + jugador2 + " — " + fecha;
    }
}
