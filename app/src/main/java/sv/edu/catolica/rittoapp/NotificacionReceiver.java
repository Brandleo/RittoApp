package sv.edu.catolica.rittoapp;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import java.util.Locale;

public class NotificacionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("NotiDebug", "Se ejecutó el receiver: " + intent.getStringExtra("tipo"));
        String tipo = intent.getStringExtra("tipo");

        String titulo = "RittoApp";
        String mensaje = "";

        if ("recordatorio".equals(tipo)) {
            mensaje = "¿Ahorraste hoy? ¡No olvides registrarlo en tu alcancía!";
        } else if ("racha".equals(tipo)) {
            int racha = context.getSharedPreferences("perfil_sesion", Context.MODE_PRIVATE)
                    .getInt("racha_dias", 0);
            if (racha >= 2) {
                mensaje = String.format(Locale.US, "¡Excelente! Ya llevas %d días ahorrando. ¡Sigue así!", racha);
            } else {
                return;
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ritto_channel")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(titulo)
                .setContentText(mensaje)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

// Verificar permisos antes de mostrar
        if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);
        manager.notify(tipo.hashCode(), builder.build());
    }
}
