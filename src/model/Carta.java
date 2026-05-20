package model;

import java.util.List;

public class Carta {

    public enum Rareza {
        COMUN("Común"),
        INFRECUENTE("Infrecuente"),
        RARA("Rara"),
        MITICA("Mítica");

        private final String valorBD;

        Rareza(String valorBD) { this.valorBD = valorBD; }

        public String getValorBD() { return valorBD; }

        public static Rareza desdeDB(String valor) {
            for (Rareza r : values()) {
                if (r.valorBD.equalsIgnoreCase(valor)) return r;
            }
            throw new IllegalArgumentException("Rareza desconocida: " + valor);
        }
    }

    private int idCarta;
    private String nombre;
    private int costeMana;
    private Integer fuerza;
    private Integer resistencia;
    private String textoHabilidad;
    private Rareza rareza;
    private boolean legendario;
    private TipoCarta tipoCarta;
    private TipoCarta tipoSecundario;
    private Edicion edicion;
    private List<Color> colores;

    public Carta() {}

    public int getIdCarta() { return idCarta; }
    public void setIdCarta(int idCarta) { this.idCarta = idCarta; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getCosteMana() { return costeMana; }
    public void setCosteMana(int costeMana) { this.costeMana = costeMana; }

    public Integer getFuerza() { return fuerza; }
    public void setFuerza(Integer fuerza) { this.fuerza = fuerza; }

    public Integer getResistencia() { return resistencia; }
    public void setResistencia(Integer resistencia) { this.resistencia = resistencia; }

    public String getTextoHabilidad() { return textoHabilidad; }
    public void setTextoHabilidad(String textoHabilidad) { this.textoHabilidad = textoHabilidad; }

    public Rareza getRareza() { return rareza; }
    public void setRareza(Rareza rareza) { this.rareza = rareza; }

    public boolean isLegendario() { return legendario; }
    public void setLegendario(boolean legendario) { this.legendario = legendario; }

    public TipoCarta getTipoCarta() { return tipoCarta; }
    public void setTipoCarta(TipoCarta tipoCarta) { this.tipoCarta = tipoCarta; }

    public TipoCarta getTipoSecundario() { return tipoSecundario; }
    public void setTipoSecundario(TipoCarta tipoSecundario) { this.tipoSecundario = tipoSecundario; }

    public Edicion getEdicion() { return edicion; }
    public void setEdicion(Edicion edicion) { this.edicion = edicion; }

    public List<Color> getColores() { return colores; }
    public void setColores(List<Color> colores) { this.colores = colores; }

    @Override
    public String toString() {
        return nombre + " (" + (tipoCarta != null ? tipoCarta.getNombre() : "") + ")";
    }
}
