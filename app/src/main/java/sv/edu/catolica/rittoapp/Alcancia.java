package sv.edu.catolica.rittoapp;

public class Alcancia {
    private int id;
    private String nombre;
    private double cantidad;
    private String icono;
    private boolean sellada;

    public Alcancia(int id, String nombre, double cantidad, String icono, boolean sellada) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.icono = icono;
        this.sellada = sellada;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getCantidad() { return cantidad; }
    public String getIcono() { return icono; }
    public boolean isSellada() { return sellada; }
}
