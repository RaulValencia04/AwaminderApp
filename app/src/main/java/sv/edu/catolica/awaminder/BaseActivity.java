package sv.edu.catolica.awaminder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.cerraS) {
            SharedPreferences sharedPreferences = getSharedPreferences("MiArchivoPref", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            // Detener el servicio
            Intent serviceIntent = new Intent(this, AlarmNotification.class);
            stopService(serviceIntent);

            Intent intent = new Intent(this, login.class);
            startActivity(intent);
            finish();
            return true;
        } else if (id == R.id.changeP) {

            Intent intent = new Intent(this, plan.class);
            startActivity(intent);
            finish();

            return true;
        }

        else if (id == R.id.benenficios) {

            Intent intent = new Intent(this, beneficios.class);
            startActivity(intent);

        } else if (id == R.id.hora) {

            Intent intent = new Intent(this, alarmStop.class);
            startActivity(intent);
            finish();

            return true;
        }
        else if (id == R.id.logro) {

            Intent intent = new Intent(this, logros.class);
            startActivity(intent);


            return true;
        }
        else if (id == R.id.add) {

            Intent intent = new Intent(this, Consejos.class);
            startActivity(intent);


            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}