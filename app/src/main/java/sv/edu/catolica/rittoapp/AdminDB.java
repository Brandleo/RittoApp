package sv.edu.catolica.rittoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class AdminDB extends SQLiteOpenHelper {

    private static final String NOMBRE_BD = "rittoapp.db";
    private static final int VERSION_BD = 1;

    public AdminDB(Context context) {
        super(context, NOMBRE_BD, null, VERSION_BD);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Crear tabla perfil
        String crearTablaPerfil = "CREATE TABLE perfil (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT NOT NULL, " +
                "imagenUri TEXT, " +
                "pin TEXT)";
        db.execSQL(crearTablaPerfil);
        String crearTablaAlcancia = "CREATE TABLE alcancia (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "cantidad REAL, " +
                "perfil_nombre TEXT," +
                "icono TEXT, " +
                "sellada INTEGER)";
        db.execSQL(crearTablaAlcancia);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS perfil");
        onCreate(db);
    }

    // Insertar nuevo perfil
    public void agregarPerfil(String nombre, String imagenUri, String pin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("imagenUri", imagenUri);
        values.put("pin", pin);
        db.insert("perfil", null, values);
        db.close();
    }

    // Obtener todos los perfiles con imagen y pin
    public List<Perfil> obtenerPerfilesCompletos() {
        List<Perfil> perfiles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre, imagenUri, pin FROM perfil", null);

        if (cursor.moveToFirst()) {
            do {
                String nombre = cursor.getString(0);
                String imagenUri = cursor.getString(1);
                String pin = cursor.getString(2);
                perfiles.add(new Perfil(nombre, imagenUri, pin));
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return perfiles;
    }

    // Obtener pin de un perfil por nombre (opcional para validación directa)
    public String obtenerPinPorNombre(String nombrePerfil) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT pin FROM perfil WHERE nombre = ?", new String[]{nombrePerfil});

        String pin = null;
        if (cursor.moveToFirst()) {
            pin = cursor.getString(0);
        }

        cursor.close();
        db.close();
        return pin;
    }

    ////////////////////alcancia
    public void agregarAlcancia(String nombre, double cantidad, String perfilNombre, String icono, boolean sellada) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nombre);
        values.put("cantidad", cantidad);
        values.put("perfil_nombre", perfilNombre);
        values.put("icono", icono);
        values.put("sellada", sellada ? 1 : 0);
        db.insert("alcancia", null, values);
        db.close();
    }


    public List<Alcancia> obtenerAlcanciasPorPerfil(String perfilNombre) {
        List<Alcancia> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM alcancia WHERE perfil_nombre = ?", new String[]{perfilNombre});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                double cantidad = cursor.getDouble(cursor.getColumnIndexOrThrow("cantidad"));
                String icono = cursor.getString(cursor.getColumnIndexOrThrow("icono"));
                boolean sellada = cursor.getInt(cursor.getColumnIndexOrThrow("sellada")) == 1;

                lista.add(new Alcancia(id, nombre, cantidad, icono, sellada));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }



}
