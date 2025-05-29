package sv.edu.catolica.rittoapp;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
// Verificar permiso de notificaciones (solo Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 1001);
            }
        }

        crearCanalNotificaciones();


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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permiso de notificaciones concedido", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Sin permiso para notificaciones", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void crearCanalNotificaciones() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "ritto_channel",
                    "Canal RittoApp",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Canal para recordatorios y rachas");
            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }
}
