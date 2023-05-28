package sv.edu.catolica.awaminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Timer;
import java.util.TimerTask;

public class AlarmNotification extends Service {

        private Timer timer;
        private final static String CHANNEL_ID = "NOTIFICACIONs";
        private final static int NOTIFICACION_ID = 1;

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            // Crear el canal de notificación (solo es necesario hacerlo una vez)
            createNotificationChannel();

            // Inicia el temporizador para ejecutar el código cada cierto intervalo de tiempo
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    createNotificationChannel();
                    createNotification();
                }
            }, 0, 60000); // Ejecuta el código cada minuto (60000 milisegundos)

            return START_STICKY;
        }

        @Override
        public void onDestroy() {
            super.onDestroy();
            // Detén el temporizador al detener el servicio
            if (timer != null) {
                timer.cancel();
            }
        }
        public String obtenerUsuarioLogueado() {
            SharedPreferences sharedPreferences = getSharedPreferences("MiArchivoPref", Context.MODE_PRIVATE);
            return sharedPreferences.getString("usuarioLogueado", "");

        }

        @Nullable
        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        private void createNotificationChannel() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                CharSequence name = "Noticacion";
                NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }

        private void createNotification() {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
            builder.setSmallIcon(android.R.drawable.presence_away);
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.img_2));
            builder.setContentTitle("AwaMinder");
            builder.setContentText("Hora de tomar agua bebe "+obtenerUsuarioLogueado());
            builder.setColor(Color.BLUE);
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            builder.setLights(Color.MAGENTA, 1000, 1000);
            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setDefaults(Notification.DEFAULT_SOUND);

            Intent notificationIntent = new Intent(this, plan.class);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_IMMUTABLE);

            builder.setContentIntent(pendingIntent);

            NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManagerCompat.notify(NOTIFICACION_ID, builder.build());
        }

}
