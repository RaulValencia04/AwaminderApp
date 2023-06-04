package sv.edu.catolica.awaminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private EditText contrasenia, nombre;
    private TextView ET;
    private PendingIntent pendingIntent;
    private final static String CHANNEL_ID = "NOTIFICACION";
    private final static int NOTIFICACION_ID = 0;
    private Button miBoton, miBoton2;
    public static final String MY_CHANNEL_ID = "myChannel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contrasenia = findViewById(R.id.ETcontrasenia);
        nombre = findViewById(R.id.ETnombre);
        miBoton = findViewById(R.id.btnGuardar);
//        startService(new Intent(MainActivity.this, AlarmNotification.class));



    }


//    private void createNotificationChannel() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//
//            CharSequence name = "Noticacion";
//            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
//            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//    }

    private void createNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID);
        builder.setSmallIcon(android.R.drawable.ic_dialog_info);
        builder.setContentTitle("AwaMinder");
        builder.setContentText("Hora de tomar agua bebe 😘");
        builder.setColor(Color.BLUE);
        builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        builder.setLights(Color.MAGENTA, 1000, 1000);
        builder.setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});
        builder.setDefaults(Notification.DEFAULT_SOUND);

        Intent notificationIntent = new Intent(this, MainActivity.class);
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


    public void Agregar(View view) {
        DbAdmin admin = new DbAdmin(this,"AwaMinder",null,1);
        SQLiteDatabase DB = admin.getWritableDatabase();
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre.getText().toString());
        usuario.setContraseña(contrasenia.getText().toString());
        usuario.setPeso(0);
        usuario.setDiasSinFallar(0);

        String nombreText = nombre.getText().toString();
        String contraseniaText = contrasenia.getText().toString();

        if (nombreText.isEmpty()  || contraseniaText.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_LONG).show();
            return; // Agrega esta línea para salir del método en caso de campos vacíos
        }



        ContentValues values = new ContentValues();
        values.put("nombre", usuario.getNombre());
        values.put("correo", usuario.getCorreo());
        values.put("contrasenia", usuario.getContraseña());

        long resultado = DB.insert("Usuario", null, values);
        DB.close();
        nombre.setText("");
        contrasenia.setText("");

        Toast.makeText(this, "Se cargaron los datos en la base", Toast.LENGTH_SHORT).show();
    }


    public void canmbiarACT(View view) {
        Intent intent = new Intent(MainActivity.this, login.class);

        // Iniciar la nueva actividad
        startActivity(intent);
        finish();

    }



}