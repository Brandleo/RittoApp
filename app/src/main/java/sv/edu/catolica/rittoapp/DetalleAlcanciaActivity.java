package sv.edu.catolica.rittoapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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
        datosAlcancia.putString("nombre", getIntent().getStringExtra("nombre"));
        datosAlcancia.putDouble("cantidad", getIntent().getDoubleExtra("cantidad", 0));
        datosAlcancia.putString("icono", getIntent().getStringExtra("icono"));

        // Cargar por defecto el fragmento de ahorros con los datos
        AhorrosAlcanciaFragment fragInicial = new AhorrosAlcanciaFragment();
        fragInicial.setArguments(datosAlcancia);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContenedorAlcancia, fragInicial)
                .commit();

        // Listener para el menú inferior
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            int id = item.getItemId();

            if (id == R.id.nav_metas) {
                fragment = new MetasFragment();
            } else if (id == R.id.nav_ahorros) {
                fragment = new AhorrosAlcanciaFragment();
                fragment.setArguments(datosAlcancia);
            } else if (id == R.id.nav_historial) {
                fragment = new HistorialAlcanciaFragment();
            } else if (id == R.id.nav_ajustes) {
                fragment = new AjustesAlcanciaFragment();
            }

            if (fragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragmentContenedorAlcancia, fragment)
                        .commit();
                return true;
            }

            return false;
        });
    }
}
