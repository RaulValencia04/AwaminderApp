package sv.edu.catolica.awaminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class Meta extends  BaseActivity {

    private ImageView bottleImageView;
    private Button decreaseButton;
    private int currentWaterLevel;
    private DbAdmin dbAdmin;
    private AlertDialog alertDialog;
    private Handler handler;
    float meta;
    float nuevaLogrado;
    private static final long DIALOG_DELAY = 3000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meta);
        bottleImageView = findViewById(R.id.imgbotella);
        decreaseButton = findViewById(R.id.btnbajar);
        currentWaterLevel = 100;
        meta = 0;
        nuevaLogrado=0;
        dbAdmin  = new DbAdmin(Meta.this,"AwaMinder",null,1);


        // Nivel inicial de agua (puede ser un valor diferente)

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseWaterLevel();
            }
        });

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
                meta = cursor.getFloat(metaColumnIndex);

                // Realizar los cálculos y actualizaciones necesarias
                nuevaLogrado = logrado - (meta * 0.1f);
                if (nuevaLogrado < 0) {
                    nuevaLogrado = 0;
                }
                updateBottleImage(meta,nuevaLogrado);
                // Formatear los valores con dos decimales
                DecimalFormat decimalFormat = new DecimalFormat("#.##");
                String tomaCantidad = decimalFormat.format(meta * 0.1f);
                String faltaCantidad = decimalFormat.format(nuevaLogrado);

                // Actualizar el registro en la base de datos
                ContentValues values = new ContentValues();
                values.put("logrado", nuevaLogrado);
                db.update("Usuario", values, "nombre = ?", new String[]{nombreUsuario});
//                Toast.makeText(Meta.this, "Toma " + tomaCantidad + " de " + meta, Toast.LENGTH_SHORT).show();

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("TOMA " + tomaCantidad + "ML")
                        .setMessage("Vamos te falta " + faltaCantidad + " para tu meta.");

                alertDialog = builder.create();
                alertDialog.show();

                // Iniciar el Handler para programar la ocultación del cuadro de diálogo después del retardo
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismissDialog();
                    }
                }, DIALOG_DELAY);

                currentWaterLevel -= 10;

                if (currentWaterLevel < 0) {
                    currentWaterLevel = 0; // Asegurarse de que el nivel de agua no sea negativo

                }

                updateBottleImage(meta,nuevaLogrado);
            }
        }

        // Cerrar el cursor y la conexión a la base de datos
        cursor.close();
        db.close();
    }


    // Método para ocultar el cuadro de diálogo
    private void dismissDialog() {
        if (alertDialog != null && alertDialog.isShowing()) {
            alertDialog.dismiss();
        }
    }
    private void actualizarDiasRecord() {
        // Obtener el nombre de usuario guardado en SharedPreferences
        String nombreUsuario = obtenerUsuarioLogueado();

        // Obtener una instancia de la base de datos en modo escritura
        SQLiteDatabase db = dbAdmin.getWritableDatabase();

        // Realizar una consulta para obtener el registro del usuario con el nombre obtenido
        Cursor cursor = db.query("Usuario", null, "nombre = ?", new String[]{nombreUsuario}, null, null, null);

        if (cursor.moveToFirst()) {
            int diasRecordColumnIndex = cursor.getColumnIndex("DiasRecord");

            // Verificar si la columna existe en el cursor
            if (diasRecordColumnIndex != -1) {
                int diasRecordActual = cursor.getInt(diasRecordColumnIndex);

                // Incrementar el valor actual en 1
                int diasRecordNuevo = diasRecordActual + 1;

                // Actualizar el registro en la base de datos
                ContentValues values = new ContentValues();
                values.put("DiasRecord", diasRecordNuevo);
                db.update("Usuario", values, "nombre = ?", new String[]{nombreUsuario});

                // Mostrar un mensaje o realizar la acción correspondiente
//                Toast.makeText(Meta.this, "Días récord actualizado: " + diasRecordNuevo, Toast.LENGTH_SHORT).show;
            }
        }

        // Cerrar el cursor y la conexión a la base de datos
        cursor.close();
        db.close();
    }


    // Asegúrate de llamar a dismissDialog() en el onDestroy() o en otro lugar adecuado
    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
    }


    private void updateBottleImage(float meta,float nuevaLogrado) {
        // Actualizar la imagen de la botella según el nivel de agua actual
        if (nuevaLogrado >=(meta*0.90)) {
            bottleImageView.setImageResource(R.drawable.bt8);
        } else if (nuevaLogrado >= (meta*0.70)) {
            bottleImageView.setImageResource(R.drawable.bt77);
        } else if (nuevaLogrado >= (meta*0.60)) {
            bottleImageView.setImageResource(R.drawable.bt66);
        }
        else if (nuevaLogrado >= (meta*0.50)) {
            bottleImageView.setImageResource(R.drawable.b55);
        }
        else if (nuevaLogrado >= (meta*0.40)) {
            bottleImageView.setImageResource(R.drawable.bt4);
        }
        else if (nuevaLogrado >= (meta*0.25)) {
            bottleImageView.setImageResource(R.drawable.bt2);
        }
        else if (nuevaLogrado ==0 ) {
            bottleImageView.setImageResource(R.drawable.btvacia1);
            Toast.makeText(Meta.this, "OPAAAA lograste tu meta de hoy", Toast.LENGTH_SHORT).show();
            actualizarDiasRecord();
        }
    }
    public String obtenerUsuarioLogueado() {
        SharedPreferences sharedPreferences = getSharedPreferences("MiArchivoPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("usuarioLogueado", "");

    }



    }