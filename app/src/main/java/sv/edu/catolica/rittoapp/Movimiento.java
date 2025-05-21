package sv.edu.catolica.rittoapp;

public class Movimiento {
    private int id, idAlcancia;
    private String tipo, fecha;
    private double montoTotal;

    public Movimiento(int id, int idAlcancia, String tipo, String fecha, double montoTotal) {
        this.id = id;
        this.idAlcancia = idAlcancia;
        this.tipo = tipo;
        this.fecha = fecha;
        this.montoTotal = montoTotal;
    }

    public int getId() { return id; }
    public int getIdAlcancia() { return idAlcancia; }
    public String getTipo() { return tipo; }
    public String getFecha() { return fecha; }
    public double getMontoTotal() { return montoTotal; }
}

