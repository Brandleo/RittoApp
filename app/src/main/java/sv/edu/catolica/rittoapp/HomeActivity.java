package sv.edu.catolica.rittoapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigation;
    private String perfilActivo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences prefs = getSharedPreferences("perfil_sesion", MODE_PRIVATE);
        perfilActivo = prefs.getString("nombrePerfil", null);
        if (perfilActivo == null) {
            Log.e("HomeActivity", "PERFIL ACTIVO ES NULL. REDIRIGIENDO.");
        } else {
            Log.d("HomeActivity", "PERFIL CARGADO: " + perfilActivo);
        }


        if (perfilActivo == null) {
            finish(); // Si no hay sesión, cerramos la actividad
            return;
        }

        Log.d("HomeActivity", "Perfil activo leído: " + perfilActivo);

        bottomNavigation = findViewById(R.id.bottomNavigation);

        // Cargar fragmento por defecto con argumentos
        Fragment fragmentInicio = new InicioFragment();
        Bundle args = new Bundle();
        args.putString("perfil", perfilActivo);
        fragmentInicio.setArguments(args);
        cargarFragmento(fragmentInicio);

        bottomNavigation.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            Bundle argsFrag = new Bundle();
            argsFrag.putString("perfil", perfilActivo);

            int itemId = item.getItemId();

            if (itemId == R.id.nav_inicio) {
                fragment = new InicioFragment();
            } else if (itemId == R.id.nav_alcancias) {
                fragment = new AlcanciasFragment();
            } else if (itemId == R.id.nav_metas) {
                fragment = new MetasFragment();
            } else if (itemId == R.id.nav_ajustes) {
                fragment = new AjustesFragment();
            }

            if (fragment != null) {
                fragment.setArguments(argsFrag);
                cargarFragmento(fragment);
                return true;
            }

            return false;
        });
    }

    private void cargarFragmento(Fragment fragmento) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.contenedorFragmentos, fragmento)
                .commit();
    }
}
