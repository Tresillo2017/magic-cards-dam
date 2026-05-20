package model;

public class Color {

    private int idColor;
    private String nombre;

    public Color() {}

    public Color(int idColor, String nombre) {
        this.idColor = idColor;
        this.nombre = nombre;
    }

    public int getIdColor() { return idColor; }
    public void setIdColor(int idColor) { this.idColor = idColor; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    @Override
    public String toString() { return nombre; }
}
