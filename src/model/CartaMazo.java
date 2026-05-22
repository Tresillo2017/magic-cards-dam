package model;

public class CartaMazo {

    private Mazo mazo;
    private Carta carta;
    private int cantidad;

    public CartaMazo() {}

    public CartaMazo(Mazo mazo, Carta carta, int cantidad) {
        this.mazo = mazo;
        this.carta = carta;
        this.cantidad = cantidad;
    }

    public Mazo getMazo() { return mazo; }
    public void setMazo(Mazo mazo) { this.mazo = mazo; }

    public Carta getCarta() { return carta; }
    public void setCarta(Carta carta) { this.carta = carta; }

    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    @Override
    public String toString() {
        return carta + " x" + cantidad;
    }
}
