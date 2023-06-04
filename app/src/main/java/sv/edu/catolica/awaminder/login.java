package sv.edu.catolica.awaminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class login extends AppCompatActivity {

    private EditText etNombre;
    private EditText etContrasenia;
    private Button btnIniciarSesion;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (obtenerUsuarioLogueadoBool()) {
            // Si hay un nombre de usuario, iniciar la actividad Meta
            iniciarActividadMeta();
        }

        etNombre = findViewById(R.id.ETnombre);
        etContrasenia = findViewById(R.id.ETcontrasenia);


        DbAdmin admin = new DbAdmin(this,"AwaMinder",null,1);
        SQLiteDatabase DB = admin.getWritableDatabase();




    }

    private boolean autenticarUsuario(String nombre, String contrasenia) {

        DbAdmin admin = new DbAdmin(this,"AwaMinder",null,1);
        SQLiteDatabase DB = admin.getWritableDatabase();
        // Consultar la base de datos para autenticar al usuario
        String[] columns = {"nombre", "contrasenia"};
        String selection = "nombre = ? AND contrasenia = ?";
        String[] selectionArgs = {nombre, contrasenia};

        Cursor cursor = DB.query("Usuario", columns, selection, selectionArgs, null, null, null);

        boolean autenticado = cursor.moveToFirst();

        cursor.close();

        return autenticado;
    }

    public void validar(View view) {


        try {
            String nombre = etNombre.getText().toString();
            String contrasenia = etContrasenia.getText().toString();

            // Consultar la base de datos para autenticar al usuario
            if (autenticarUsuario(nombre, contrasenia)) {
                // Inicio de sesión exitoso
                Toast.makeText(login.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();
                guardarUsuarioLogueado(nombre);
                Intent intent = new Intent(this, plan.class);
                // Iniciar la nueva actividad
                startService(new Intent(login.this, AlarmNotification.class));
                startActivity(intent);
                finish();
            } else {
                // Credenciales inválidas
                Toast.makeText(login.this, "Credenciales inválidas", Toast.LENGTH_SHORT).show();
            }



        }catch (Exception E){
            Toast.makeText(login.this, "   Error"+E, Toast.LENGTH_SHORT).show();

        }

    }
    public void guardarUsuarioLogueado(String nombreUsuario) {
        SharedPreferences sharedPreferences = getSharedPreferences("MiArchivoPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuarioLogueado", nombreUsuario);
        editor.apply();
    }

    public boolean obtenerUsuarioLogueadoBool() {
        SharedPreferences sharedPreferences = getSharedPreferences("MiArchivoPref", Context.MODE_PRIVATE);
        String nombreUsuario = sharedPreferences.getString("usuarioLogueado", "");
        return !nombreUsuario.isEmpty();
    }
    private void iniciarActividadMeta() {
        Intent intent = new Intent(login.this, Meta.class);
        startActivity(intent);
        finish(); // Cerrar la actividad actual para que no se pueda volver atrás al inicio de sesión
    }
    public void cambiarIngre(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }
}