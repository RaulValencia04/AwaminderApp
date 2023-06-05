package sv.edu.catolica.awaminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

public class logros extends BaseActivity {

    private DbAdmin dbAdmin;
    private TextView tvlogro,tvmeta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logros2);
        dbAdmin = new DbAdmin(logros.this, "AwaMinder", null, 1);
        tvlogro = findViewById(R.id.racha);
        tvmeta = findViewById(R.id.meta);

        tvmeta.setText("Meta diaria: "+obtenerMeta());
        tvlogro.setText("Racha de días: "+obtenerDiasRecord());



    }

    public float obtenerMeta() {
        // Obtener el nombre de usuario guardado en SharedPreferences
        String nombreUsuario = obtenerUsuarioLogueado();

        // Obtener una instancia de la base de datos en modo lectura
        SQLiteDatabase db = dbAdmin.getReadableDatabase();

        // Realizar una consulta para obtener el registro del usuario con el nombre obtenido
        Cursor cursor = db.query("Usuario", null, "nombre = ?", new String[]{nombreUsuario}, null, null, null);

        float meta = 0;

        if (cursor.moveToFirst()) {
            int metaColumnIndex = cursor.getColumnIndex("meta");

            // Verificar si la columna existe en el cursor
            if (metaColumnIndex != -1) {
                meta = cursor.getFloat(metaColumnIndex);
            }
        }

        // Cerrar el cursor y la conexión a la base de datos
        cursor.close();
        db.close();

        return meta;
    }

    public int obtenerDiasRecord() {
        // Obtener el nombre de usuario guardado en SharedPreferences
        String nombreUsuario = obtenerUsuarioLogueado();

        // Obtener una instancia de la base de datos en modo lectura
        SQLiteDatabase db = dbAdmin.getReadableDatabase();

        // Realizar una consulta para obtener el registro del usuario con el nombre obtenido
        Cursor cursor = db.query("Usuario", null, "nombre = ?", new String[]{nombreUsuario}, null, null, null);

        int diasRecord = 0;

        if (cursor.moveToFirst()) {
            int diasRecordColumnIndex = cursor.getColumnIndex("DiasRecord");

            // Verificar si la columna existe en el cursor
            if (diasRecordColumnIndex != -1) {
                diasRecord = cursor.getInt(diasRecordColumnIndex);
            }
        }

        // Cerrar el cursor y la conexión a la base de datos
        cursor.close();
        db.close();

        return diasRecord;
    }

    public String obtenerUsuarioLogueado() {
        SharedPreferences sharedPreferences = getSharedPreferences("MiArchivoPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("usuarioLogueado", "");

    }
}
