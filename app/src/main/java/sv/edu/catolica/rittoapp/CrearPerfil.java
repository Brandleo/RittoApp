package sv.edu.catolica.rittoapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class CrearPerfil extends AppCompatActivity {
    private EditText inputNombre;
    private Button btnGuardar;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imagenUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_perfil);

        inputNombre = findViewById(R.id.inputNombre);
        btnGuardar = findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(v -> {
            String nombre = inputNombre.getText().toString().trim();
            String pin = ((EditText) findViewById(R.id.inputPin)).getText().toString().trim();

            if (!nombre.isEmpty() && imagenUri != null && pin.length() == 4) {
                String rutaImagenLocal = copiarImagenAlInterno(imagenUri);
                if (rutaImagenLocal != null) {
                    AdminDB adminDB = new AdminDB(this);
                    adminDB.agregarPerfil(nombre, rutaImagenLocal, pin);
                    finish(); // regresa a SeleccionPerfil
                } else {
                    Toast.makeText(this, "No se pudo copiar la imagen", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Completa todos los campos y usa un PIN de 4 dígitos", Toast.LENGTH_SHORT).show();
            }
        });


        EditText inputNombre = findViewById(R.id.inputNombre);
        Button btnGuardar = findViewById(R.id.btnGuardar);
        ImageView imgSeleccionada = findViewById(R.id.imgSeleccionada);

        imgSeleccionada.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnGuardar.setOnClickListener(v -> {
            String nombre = inputNombre.getText().toString().trim();
            String pin = ((EditText) findViewById(R.id.inputPin)).getText().toString().trim();

            if (!nombre.isEmpty() && imagenUri != null && pin.length() == 4) {
                String rutaImagenLocal = copiarImagenAlInterno(imagenUri);
                if (rutaImagenLocal != null) {
                    AdminDB adminDB = new AdminDB(this);
                    adminDB.agregarPerfil(nombre, rutaImagenLocal, pin);
                    finish(); // regresa a SeleccionPerfil
                } else {
                    Toast.makeText(this, "No se pudo copiar la imagen", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Completa todos los campos y usa un PIN de 4 dígitos", Toast.LENGTH_SHORT).show();
            }
        });


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            imagenUri = data.getData();
            ImageView imgSeleccionada = findViewById(R.id.imgSeleccionada);
            imgSeleccionada.setImageURI(imagenUri);
        }
    }

    private String copiarImagenAlInterno(Uri uriOriginal) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uriOriginal);
            String nombreArchivo = "perfil_" + System.currentTimeMillis() + ".jpg";
            File archivoDestino = new File(getFilesDir(), nombreArchivo);

            OutputStream outputStream = new FileOutputStream(archivoDestino);

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }

            inputStream.close();
            outputStream.close();

            return archivoDestino.getAbsolutePath(); // ← Esta es la ruta segura
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}

