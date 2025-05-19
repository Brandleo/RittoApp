package sv.edu.catolica.rittoapp;

public class Alcancia {
    private int id;
    private String nombre;
    private double cantidad;

    public Alcancia(int id, String nombre, double cantidad) {
        this.id = id;
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public double getCantidad() { return cantidad; }
}
