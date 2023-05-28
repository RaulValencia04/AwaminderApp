package sv.edu.catolica.awaminder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

public class Meta extends AppCompatActivity {
    private ProgressBar progressBar;
    private Button decreaseButton;

    private int metaDiaria = 2000; // Meta diaria de consumo de agua
    private int consumoActual = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meta);
        progressBar = findViewById(R.id.progressBar);
        decreaseButton = findViewById(R.id.btnbajar);

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseWaterConsumption();
            }
        });


    }
    private void decreaseWaterConsumption() {
        if (consumoActual > 0) {
            consumoActual -= 100; // Disminuir en 100 ml
            updateProgressBar();
        }
    }

    private void updateProgressBar() {
        int progress = (int) ((consumoActual / (float) metaDiaria) * 100);
        progressBar.setProgress(progress);
    }


    }