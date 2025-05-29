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
            String tipo = intent.getStringExtra("tipo");
            Log.d("NotiDebug", "Se ejecutó el receiver: " + tipo);

            if (!"recordatorio".equals(tipo)) return; // Ignorar cualquier otro tipo

            String titulo = "RittoApp";
            String mensaje = "¿Ahorraste hoy? ¡No olvides registrarlo en tu alcancía!";

            // Verificar permisos antes de mostrar
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                Log.w("NotiDebug", "Permiso de notificaciones no concedido");
                return;
            }

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ritto_channel")
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(titulo)
                    .setContentText(mensaje)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
            manager.notify(tipo.hashCode(), builder.build());
    }
}
