package ar.edu.unc.famaf.redditreader.backend;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by federico on 01/11/16.
 */

public class ReeditDBHelper extends SQLiteOpenHelper{

    //Sentencia SQL para crear la tabla de Usuarios
    private String sqlCreate = "CREATE TABLE Models_table (title VARCHAR, author VARCHAR, " +
            "date VARCHAR, comments VARCHAR, url VARCHAR)";

    public ReeditDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla
        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS Models_table");

        //Se crea la nueva versión de la tabla
        db.execSQL(sqlCreate);
    }
}
