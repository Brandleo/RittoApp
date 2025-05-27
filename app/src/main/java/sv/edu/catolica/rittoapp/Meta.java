package sv.edu.catolica.rittoapp;

public class Meta {
    private int id;
    private int idAlcancia;
    private String nombre;
    private double montoObjetivo;

    public Meta(int id, int idAlcancia, String nombre, double montoObjetivo) {
        this.id = id;
        this.idAlcancia = idAlcancia;
        this.nombre = nombre;
        this.montoObjetivo = montoObjetivo;
    }

    public int getId() { return id; }
    public int getIdAlcancia() { return idAlcancia; }
    public String getNombre() { return nombre; }
    public double getMontoObjetivo() { return montoObjetivo; }
}

