package sv.edu.catolica.rittoapp;

public class Denominacion {
    private final double valor;
    private final int imagenResId;
    private int cantidad;

    public Denominacion(double valor, int imagenResId) {
        this.valor = valor;
        this.imagenResId = imagenResId;
        this.cantidad = 0; // por defecto
    }

    public double getValor() {
        return valor;
    }

    public int getImagenResId() {
        return imagenResId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
