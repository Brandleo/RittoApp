package sv.edu.catolica.rittoapp;

public class DenominacionCantidad {
    private double denominacion;
    private int cantidad;

    public DenominacionCantidad(double denominacion, int cantidad) {
        this.denominacion = denominacion;
        this.cantidad = cantidad;
    }

    public double getDenominacion() { return denominacion; }
    public int getCantidad() { return cantidad; }
}

