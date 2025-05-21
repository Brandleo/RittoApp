package sv.edu.catolica.rittoapp;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DetalleAlcanciaActivity extends AppCompatActivity {

    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_alcancia);

        bottomNav = findViewById(R.id.bottomNavAlcancia);

        // Obtener datos enviados desde la alcancía seleccionada
        Bundle datosAlcancia = new Bundle();
        datosAlcancia.putInt("id", getIntent().getIntExtra("id", -1)); // ESTE ES CLAVE
        datosAlcancia.putString("nombre", getIntent().getStringExtra("nombre"));
        datosAlcancia.putDouble("cantidad", getIntent().getDoubleExtra("cantidad", 0));
        datosAlcancia.putString("icono", getIntent().getStringExtra("icono"));
        datosAlcancia.putBoolean("sellada", getIntent().getBooleanExtra("sellada", false));


        // Fragmento inicial: Ahorros
        Fragment fragInicial = new AhorrosAlcanciaFragment();
        fragInicial.setArguments(datosAlcancia);
        cargarFragmento(fragInicial);

        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_ahorros) {
                fragment = new AhorrosAlcanciaFragment();
            } else if (id == R.id.nav_metas) {
                fragment = new MetasFragment();
            } else if (id == R.id.nav_historial) {
                fragment = new HistorialAlcanciaFragment();
            } else if (id == R.id.nav_ajustes) {
                fragment = new AjustesAlcanciaFragment();
            }

            if (fragment != null) {
                fragment.setArguments(datosAlcancia);
                getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .replace(R.id.fragmentContenedorAlcancia, fragment)
                        .commit();
                return true;
            }

            return false;
        });
    }

    private void cargarFragmento(Fragment fragmento) {
        getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.fragmentContenedorAlcancia, fragmento)
                .commit();
    }
}
