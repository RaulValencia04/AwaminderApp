package sv.edu.catolica.awaminder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class Calcular_Perzonalizado extends AppCompatActivity {

    private EditText et;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calcular_perzonalizado);

        et = findViewById(R.id.ETlitro);



    }
    public void actualizarUsuario() {
        String nombre =obtenerUsuarioLogueado();
        double peso = Double.parseDouble(et.getText().toString());
        double meta = peso;

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Hola! "+nombre+"\nSu plan es de"+meta+" litros al dia\n\n Esta deacuerdo? ").setTitle("Elija");
        builder1.setCancelable(false);
        builder1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                DbAdmin admin = new DbAdmin(Calcular_Perzonalizado.this,"AwaMinder",null,1);
                SQLiteDatabase db = admin.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("peso", peso);


                values.put("meta", meta);
                values.put("logrado", meta);


                String whereClause = "nombre = ?";
                String[] whereArgs = {nombre};
               // mensaje(nombre,meta,peso);


                int rowsAffected = db.update("Usuario", values, whereClause, whereArgs);

                if (rowsAffected > 0) {
                    Log.d("DB", "Usuario actualizado correctamente");
                    Intent intent = new Intent(Calcular_Perzonalizado.this, Meta.class);
                    startActivity(intent);


                } else {
                    Log.d("DB", "No se pudo actualizar el usuario");
                }

                db.close();


            }
        });
        builder1.setNegativeButton("Calcular de nuevo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        builder1.create();
        builder1.show();



    }
    public String obtenerUsuarioLogueado() {
        SharedPreferences sharedPreferences = getSharedPreferences("MiArchivoPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("usuarioLogueado", "");

    }
    public void actualizarP(View view) {

        actualizarUsuario();

    }
}