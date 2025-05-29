package sv.edu.catolica.rittoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class IngresoPin extends AppCompatActivity {

    private String nombrePerfil, imagenUri, pinGuardado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_pin);

        ImageView imgPerfil = findViewById(R.id.imgPerfilPin);
        TextView txtNombre = findViewById(R.id.txtNombrePerfil);
        EditText inputPin = findViewById(R.id.inputPin);
        Button btnValidar = findViewById(R.id.btnValidarPin);

        // Obtener los datos desde el intent
        nombrePerfil = getIntent().getStringExtra("nombre");
        imagenUri = getIntent().getStringExtra("imagenUri");
        pinGuardado = getIntent().getStringExtra("pin");

        txtNombre.setText(nombrePerfil);

        if (imagenUri != null) {
            File archivo = new File(imagenUri);
            if (archivo.exists()) {
                imgPerfil.setImageURI(Uri.fromFile(archivo));
            }
        }

        btnValidar.setOnClickListener(v -> {
            String pinIngresado = inputPin.getText().toString().trim();
            if (pinIngresado.equals(pinGuardado)) {

                // ✅ Guardar en SharedPreferences
                SharedPreferences prefs = getSharedPreferences("perfil_sesion", MODE_PRIVATE);
                prefs.edit().putString("nombrePerfil", nombrePerfil).apply(); // ← GUARDA EL PERFIL

                Sesion.perfilActualNombre = nombrePerfil;
                startActivity(new Intent(IngresoPin.this, HomeActivity.class));
                finish();


            } else {
                Toast.makeText(this, R.string.pin_incorrecto, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
