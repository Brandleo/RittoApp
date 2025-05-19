package sv.edu.catolica.rittoapp;

public class Perfil {
    private String nombre;
    private String imagenUri;
    private String pin;

    public Perfil(String nombre, String imagenUri, String pin) {
        this.nombre = nombre;
        this.imagenUri = imagenUri;
        this.pin = pin;
    }

    public String getNombre() {
        return nombre;
    }

    public String getImagenUri() {
        return imagenUri;
    }


    public String getPin() {
        return pin;
    }
}

