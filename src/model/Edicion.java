package model;

public class Edicion {

    private int idEdicion;
    private String nombre;
    private String fechaLanzamiento;

    public Edicion() {}

    public int getIdEdicion() { return idEdicion; }
    public void setIdEdicion(int idEdicion) { this.idEdicion = idEdicion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getFechaLanzamiento() { return fechaLanzamiento; }
    public void setFechaLanzamiento(String fechaLanzamiento) { this.fechaLanzamiento = fechaLanzamiento; }

    @Override
    public String toString() { return nombre; }
}
