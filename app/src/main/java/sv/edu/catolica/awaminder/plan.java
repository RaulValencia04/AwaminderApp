package sv.edu.catolica.awaminder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class plan extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan);
    }

    public void recomendado(View view) {

        Intent intent = new Intent(this, Calcular.class);

        // Iniciar la nueva actividad
        startActivity(intent);
    }
}