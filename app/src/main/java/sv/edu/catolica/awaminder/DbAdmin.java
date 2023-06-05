package sv.edu.catolica.awaminder;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DbAdmin extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "AwaMinder";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "Usuario";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOMBRE = "nombre";
    private static final String COLUMN_CORREO = "correo";
    private static final String COLUMN_CONTRASENIA = "contrasenia";
    private static final String COLUMN_PESO = "peso";
    private static final String COLUMN_DIAS_RECORD = "DiasRecord";
    private static final String COLUMN_META = "meta";
    private static final String COLUMN_LOGRADO = "logrado";



    public DbAdmin(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NOMBRE + " TEXT, " +
                COLUMN_CORREO + " TEXT, " +
                COLUMN_CONTRASENIA + " TEXT, " +
                COLUMN_PESO + " REAL, " +
                COLUMN_LOGRADO + " REAL, " +
                COLUMN_DIAS_RECORD + " INTEGER, " +
                COLUMN_META + " REAL)";
        db.execSQL(createTableQuery);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Código para actualizar la base de datos si existe una nueva versión
        // Aquí puedes implementar la lógica para migrar los datos existentes si es necesario
    }



}

