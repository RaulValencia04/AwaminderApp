package sv.edu.catolica.awaminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class CambiaContra extends AppCompatActivity {

    public EditText etnombre;

    public SQLiteDatabase db;
    public DbAdmin dbAdmin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cambia_contra);

        etnombre = findViewById(R.id.ETnombre);
        dbAdmin  = new DbAdmin(CambiaContra.this, "AwaMinder", null, 1);
        db = dbAdmin.getWritableDatabase();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Cerrar la conexión a la base de datos en el método onDestroy
        if (db != null && db.isOpen()) {
            db.close();
        }
    }


    public void regresarlogin(View view) {

    }

    public void cambiarContrasenia(View view) {
        String nombreUsuario = etnombre.getText().toString();

        // Realizar una consulta para obtener el registro del usuario con el nombre obtenido
        Cursor cursor = db.query("Usuario", null, "nombre = ?", new String[]{nombreUsuario}, null, null, null);

        if (cursor.moveToFirst()) {
            int contraseniaColumnIndex = cursor.getColumnIndex("contrasenia");
            int metaColumnIndex = cursor.getColumnIndex("meta");

            // Verificar si la columna existe en el cursor
            if (contraseniaColumnIndex != -1) {
                String contraseniaActual = cursor.getString(contraseniaColumnIndex);
                String metaActual = cursor.getString(metaColumnIndex);

                // Mostrar un cuadro de diálogo con la información del usuario y opciones para cambiar la contraseña
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Cambio de contraseña");
                builder.setMessage("Usuario: " + nombreUsuario + "\nMeta: " + metaActual);

                // Crear un EditText para que el usuario ingrese la nueva contraseña
                final EditText nuevaContraseniaEditText = new EditText(this);
                nuevaContraseniaEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                builder.setView(nuevaContraseniaEditText);

                builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String nuevaContrasenia = nuevaContraseniaEditText.getText().toString();

                        if (nuevaContrasenia != null && !nuevaContrasenia.isEmpty()) {
                            // Actualizar el registro en la base de datos con la nueva contraseña
                            ContentValues values = new ContentValues();
                            values.put("contrasenia", nuevaContrasenia);
                            db.update("Usuario", values, "nombre = ?", new String[]{nombreUsuario});

                            // Mostrar un mensaje de éxito
                            Toast.makeText(CambiaContra.this, "Contraseña cambiada exitosamente", Toast.LENGTH_SHORT).show();
                        } else {
                            // Mostrar un mensaje de error si la nueva contraseña es nula o vacía
                            Toast.makeText(CambiaContra.this, "La nueva contraseña no puede estar vacía", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Acción a realizar si se presiona el botón "Cancelar"
                        Toast.makeText(CambiaContra.this, "Cambio de contraseña cancelado", Toast.LENGTH_SHORT).show();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        }

        // Cerrar el cursor
        cursor.close();
    }
}