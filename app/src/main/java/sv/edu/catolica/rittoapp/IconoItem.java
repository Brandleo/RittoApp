package sv.edu.catolica.rittoapp;

public class IconoItem {
    private final String nombre;
    private final int recursoDrawable;

    public IconoItem(String nombre, int recursoDrawable) {
        this.nombre = nombre;
        this.recursoDrawable = recursoDrawable;
    }

    public String getNombre() {
        return nombre;
    }

    public int getRecursoDrawable() {
        return recursoDrawable;
    }

    // Alias para compatibilidad
    public int getResId() {
        return recursoDrawable;
    }

    @Override
    public String toString() {
        return nombre; // importante para guardar solo el nombre si lo necesitas
    }
}
