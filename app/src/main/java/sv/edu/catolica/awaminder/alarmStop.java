package sv.edu.catolica.awaminder;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Locale;

public class alarmStop extends AppCompatActivity {

    public TimePicker inicio;
    public TimePicker fin;
    Button timeButton;
    Button timeButtonEnd1;
    int hour, minute;
    int hourf, minutef;

    boolean  btnclik1,btnclik2;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_stop);

        btnclik1 = false;
        btnclik2 =false;


        timeButton = findViewById(R.id.timeButton);
        timeButtonEnd1 = findViewById(R.id.timeButtonEnd);

        SharedPreferences sharedPreferences = getSharedPreferences("MiArchivoPref", Context.MODE_PRIVATE);
        String horaInicio = sharedPreferences.getString("hora_inicio", "");
        String horaFin = sharedPreferences.getString("hora_fin", "");
        boolean esActivo = sharedPreferences.getBoolean("esActivo", false);

//        if (horaInicio != "00:00")

        timeButton.setText(horaInicio);
        timeButtonEnd1.setText(horaFin);




    }

    public void guardarHoras(View view) {


        if (btnclik1 && btnclik2){
            String horaSeleccionada = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
            String horaSeleccionadaf = String.format(Locale.getDefault(), "%02d:%02d", hourf, minutef);

            SharedPreferences sharedPreferences = getSharedPreferences("MiArchivoPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("hora_inicio", horaSeleccionada);
            editor.putString("hora_fin", horaSeleccionadaf);
            editor.putBoolean("esActivo", true);
            editor.apply();

            Toast.makeText(alarmStop.this, "hora inicio "+hour+":"+minute+" hora fin "+hourf+":"+minutef, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, Meta.class);

            // Iniciar la nueva actividad
            startActivity(intent);
        }else {
            Toast.makeText(alarmStop.this, "No realizo ningun cambio, en sus horarios de noti", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, Meta.class);

            // Iniciar la nueva actividad
            startActivity(intent);
        }

    }

    public void disableHora(View view) {
        SharedPreferences sharedPreferences = getSharedPreferences("MiArchivoPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("esActivo", false);
        editor.apply();

        Intent intent = new Intent(this, Meta.class);

        // Iniciar la nueva actividad
        startActivity(intent);

    }

    public void popTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeButton.setText(String.format(Locale.getDefault(), "%02d:%02d",hour, minute));
                btnclik1 =true;

            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hour, minute, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    public void popTimePickerfin(View view) {

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener()
        {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute)
            {
                hourf = selectedHour;
                minutef = selectedMinute;
                timeButtonEnd1.setText(String.format(Locale.getDefault(), "%02d:%02d",hourf, minutef));
                btnclik2 =true;
            }
        };

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, /*style,*/ onTimeSetListener, hourf, minutef, true);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();


    }
}