package model;

import java.time.LocalDate;

public class Edicion {

    private int idEdicion;
    private String nombre;
    private LocalDate fechaLanzamiento;

    public Edicion() {}

    public Edicion(int idEdicion, String nombre, LocalDate fechaLanzamiento) {
        this.idEdicion = idEdicion;
        this.nombre = nombre;
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public int getIdEdicion() { return idEdicion; }
    public void setIdEdicion(int idEdicion) { this.idEdicion = idEdicion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public LocalDate getFechaLanzamiento() { return fechaLanzamiento; }
    public void setFechaLanzamiento(LocalDate fechaLanzamiento) { this.fechaLanzamiento = fechaLanzamiento; }

    @Override
    public String toString() { return nombre; }
}
