package sv.edu.catolica.awaminder;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class AlarmNotification extends Service {

        private Timer timer;
        private DbAdmin dbAdmin;
         float nuevaLogrado;
        private Handler handler;
        private Runnable runnable;

        private Handler handler1;
        private Runnable runnable1;
    private final static long INTERVAL_30_SECONDS = 30 * 1000;
        private final static String CHANNEL_ID = "NOTIFICACIONs";
    private static final long INTERVAL_24_HOURS = TimeUnit.HOURS.toMillis(24);
        private final static int NOTIFICACION_ID = 1;

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            // Crear el canal de notificación (solo es necesario hacerlo una vez)
            createNotificationChannel();
            dbAdmin  = new DbAdmin(AlarmNotification.this,"AwaMinder",null,1);

            // Inicia el temporizador para ejecutar el código cada cierto intervalo de tiempo
            // Ejecuta el código cada minuto (60000 milisegundos)

            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    verificarHoras();
//                     createNotificationChannel();
//                    createNotification();
                }
            }, 0, 10000); // Ejecuta el código cada minuto (60000 milisegundos)

            return START_STICKY;
        }


    public void verificarHoras() {
        SharedPreferences sharedPreferences = getSharedPreferences("MiArchivoPref", Context.MODE_PRIVATE);
        String horaInicio = sharedPreferences.getString("hora_inicio", "");
        String horaFin = sharedPreferences.getString("hora_fin", "");
        boolean esActivo = sharedPreferences.getBoolean("esActivo",true );

        if (esActivo) {
            if (horaInicio == null || horaInicio.isEmpty() || horaFin == null || horaFin.isEmpty()) {
                // Las horas no están guardadas correctamente, pero el estado es activo
                createNotificationChannel();
                createNotification();
            } else {
                // Obtener la hora actual
                int horaActualEnMinutos = obtenerHoraActualEnMinutos();

                // Convertir las horas de inicio y fin a minutos para facilitar la comparación
                int horaInicioEnMinutos = convertirHoraEnMinutos(horaInicio);
                int horaFinEnMinutos = convertirHoraEnMinutos(horaFin);

                if (horaActualEnMinutos >= horaInicioEnMinutos && horaActualEnMinutos <= horaFinEnMinutos) {
                    createNotificationChannel();
                    createNotification();
                }
            }
        }
    }

    private int obtenerHoraActualEnMinutos() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return hour * 60 + minute;
    }


    private int convertirHoraEnMinutos(String hora) {
        String[] partes = hora.split(":");
        int hour = Integer.parseInt(partes[0]);
        int minute = Integer.parseInt(partes[1]);
        return hour * 60 + minute;
    }


        public void onCreate() {
            super.onCreate();

            handler = new Handler();
            runnable = new Runnable() {
                @Override
                public void run() {
                    decreaseWaterLevel(); // Llama al método que deseas ejecutar cada 24 horas

                    // Vuelve a programar la ejecución del método después de 24 horas
                    handler.postDelayed(this, INTERVAL_24_HOURS);
                }
            };

            // Inicia la ejecución inicial del método después de 24 horas
            handler.postDelayed(runnable, INTERVAL_24_HOURS);
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
    private void decreaseWaterLevel() {
        // Obtener el nombre de usuario guardado en SharedPreferences
        String nombreUsuario = obtenerUsuarioLogueado();

        // Obtener una instancia de la base de datos en modo escritura
        SQLiteDatabase db = dbAdmin.getWritableDatabase();

        // Realizar una consulta para obtener el registro del usuario con el nombre obtenido
        Cursor cursor = db.query("Usuario", null, "nombre = ?", new String[]{nombreUsuario}, null, null, null);

        if (cursor.moveToFirst()) {
            int logradoColumnIndex = cursor.getColumnIndex("logrado");
            int metaColumnIndex = cursor.getColumnIndex("meta");

            // Verificar si las columnas existen en el cursor
            if (logradoColumnIndex != -1 && metaColumnIndex != -1) {
                float logrado = cursor.getFloat(logradoColumnIndex);
                float meta = cursor.getFloat(metaColumnIndex);

                // Realizar los cálculos y actualizaciones necesarias
                nuevaLogrado = logrado - (meta * 0.1f);
                if (nuevaLogrado < 0) {
                    nuevaLogrado = 0;
                }

                // Formatear los valores con dos decimales
//                DecimalFormat decimalFormat = new DecimalFormat("#.##");
//                String tomaCantidad = decimalFormat.format(meta * 0.1f);
//                String faltaCantidad = decimalFormat.format(nuevaLogrado);

                // Actualizar el registro en la base de datos
                ContentValues values = new ContentValues();
                values.put("logrado", meta);
                db.update("Usuario", values, "nombre = ?", new String[]{nombreUsuario});
               Toast.makeText(AlarmNotification.this, "Nuevo dia nueva meta 🧜🏼", Toast.LENGTH_SHORT).show();




            }
        }

        // Cerrar el cursor y la conexión a la base de datos
        cursor.close();
        db.close();
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
            builder.setContentText("Hora de tomar agua "+obtenerUsuarioLogueado());
            builder.setColor(Color.BLUE);
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            builder.setLights(Color.MAGENTA, 1000, 1000);
            builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
            builder.setDefaults(Notification.DEFAULT_SOUND);

            Intent notificationIntent = new Intent(this, Meta.class);
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
