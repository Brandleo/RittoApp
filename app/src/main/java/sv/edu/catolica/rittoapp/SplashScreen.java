package sv.edu.catolica.rittoapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences("perfil_sesion", MODE_PRIVATE);
        String nombrePerfil = prefs.getString("nombrePerfil", null);

        if (nombrePerfil != null && !nombrePerfil.isEmpty()) {
            // ✅ Ya hay perfil guardado → ir directo a HomeActivity
            Sesion.perfilActualNombre = nombrePerfil;
            startActivity(new Intent(this, HomeActivity.class));
        } else {
            // ❌ No hay perfil → ir a SeleccionPerfil
            startActivity(new Intent(this, SeleccionPerfil.class));
        }

        finish();
    }
}
