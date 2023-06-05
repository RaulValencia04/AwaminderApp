package sv.edu.catolica.awaminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class plan extends AppCompatActivity {

    public DbAdmin dbAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
        dbAdmin  = new DbAdmin(plan.this,"AwaMinder",null,1);
    }

    public void recomendado(View view) {

        Intent intent = new Intent(this, Calcular.class);

        // Iniciar la nueva actividad
        startActivity(intent);
    }

    public void planP(View view) {
        Intent intent = new Intent(this, Calcular_Perzonalizado.class);

        // Iniciar la nueva actividad
        startActivity(intent);


    }

    public void oldplan(View view) {

        String nombreUsuario = obtenerUsuarioLogueado();
        boolean isMetaZero = isMetaZero(nombreUsuario);

        if (isMetaZero) {
            Toast.makeText(plan.this, "No tienes plan, tiroso! 😾", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, Meta.class);
            // Iniciar la nueva actividad
            startActivity(intent);
        }





    }
    public  boolean isMetaZero(String nombreUsuario) {
        // Obtener una instancia de la base de datos en modo lectura
        SQLiteDatabase db = dbAdmin.getReadableDatabase();

        // Realizar una consulta para obtener el registro del usuario con el nombre proporcionado
        Cursor cursor = db.query("Usuario", new String[]{"meta"}, "nombre = ?", new String[]{nombreUsuario}, null, null, null);

        boolean isZero = false;

        if (cursor.moveToFirst()) {
            int metaColumnIndex = cursor.getColumnIndex("meta");
            if (metaColumnIndex != -1) {
                float meta = cursor.getFloat(metaColumnIndex);
                isZero = meta == 0;
            }
        }

        // Cerrar el cursor y la conexión a la base de datos
        cursor.close();
        db.close();

        return isZero;
    }

    public String obtenerUsuarioLogueado() {
        SharedPreferences sharedPreferences = getSharedPreferences("MiArchivoPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("usuarioLogueado", "");

    }
}