package sv.edu.catolica.rittoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SeleccionPerfil extends AppCompatActivity {

    private RecyclerView recyclerPerfiles;
    private TextView textoSinPerfiles;
    private Button btnCrearPerfil;
    private AdminDB adminDB;
    private List<Perfil> listaPerfiles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seleccion_perfil);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerPerfiles = findViewById(R.id.recyclerPerfiles);
        textoSinPerfiles = findViewById(R.id.textoSinPerfiles);
        btnCrearPerfil = findViewById(R.id.btnCrearPerfil);

        adminDB = new AdminDB(this);
        listaPerfiles = adminDB.obtenerPerfilesCompletos(); // Ahora devuelve List<Perfil>

        if (listaPerfiles.isEmpty()) {
            recyclerPerfiles.setVisibility(View.GONE);
            textoSinPerfiles.setVisibility(View.VISIBLE);
        } else {
            recyclerPerfiles.setVisibility(View.VISIBLE);
            textoSinPerfiles.setVisibility(View.GONE);

            PerfilAdapter adapter = new PerfilAdapter(listaPerfiles);
            recyclerPerfiles.setAdapter(adapter);
            recyclerPerfiles.setLayoutManager(new LinearLayoutManager(this));
        }


    }
    public void crearperfil(View view) {
        Intent intent = new Intent(SeleccionPerfil.this, CrearPerfil.class);
        startActivity(intent);

    }
    @Override
    protected void onResume() {
        super.onResume();

        listaPerfiles = adminDB.obtenerPerfilesCompletos(); // Recargar datos

        if (listaPerfiles.isEmpty()) {
            recyclerPerfiles.setVisibility(View.GONE);
            textoSinPerfiles.setVisibility(View.VISIBLE);
        } else {
            recyclerPerfiles.setVisibility(View.VISIBLE);
            textoSinPerfiles.setVisibility(View.GONE);

            PerfilAdapter adapter = new PerfilAdapter(listaPerfiles);
            recyclerPerfiles.setAdapter(adapter);
            recyclerPerfiles.setLayoutManager(new LinearLayoutManager(this));
        }
    }


}
