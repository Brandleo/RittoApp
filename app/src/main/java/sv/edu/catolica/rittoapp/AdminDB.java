package sv.edu.catolica.rittoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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
        db.execSQL("CREATE TABLE perfil (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT NOT NULL, imagenUri TEXT, pin TEXT)");

        db.execSQL("CREATE TABLE alcancia (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "nombre TEXT, " +
                "cantidad REAL, " +
                "perfil_nombre TEXT," +
                "icono TEXT, " +
                "sellada INTEGER)");

        db.execSQL("CREATE TABLE movimiento (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_alcancia INTEGER, " +
                "tipo TEXT, " + // 'deposito' o 'retiro'
                "fecha TEXT, " + // formato ISO8601
                "monto_total REAL, " +
                "FOREIGN KEY(id_alcancia) REFERENCES alcancia(id))");

        db.execSQL("CREATE TABLE detalle_movimiento (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "id_movimiento INTEGER, " +
                "denominacion REAL, " +
                "cantidad INTEGER, " +
                "FOREIGN KEY(id_movimiento) REFERENCES movimiento(id))");
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
    public Perfil obtenerPerfilPorNombre(String nombre) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT nombre, imagenUri, pin FROM perfil WHERE nombre = ?", new String[]{nombre});
        Perfil perfil = null;
        if (cursor.moveToFirst()) {
            perfil = new Perfil(cursor.getString(0), cursor.getString(1), cursor.getString(2));
        }
        cursor.close();
        db.close();
        return perfil;
    }

    public void actualizarPerfil(String nombreOriginal, String nuevoNombre, String nuevaImagenUri, String nuevoPin) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nuevoNombre);
        values.put("imagenUri", nuevaImagenUri);
        values.put("pin", nuevoPin);
        db.update("perfil", values, "nombre = ?", new String[]{nombreOriginal});
        db.close();
    }

//////////////////////////////////////////////////// ajustes alcancia
// Vaciar alcancía
public void vaciarAlcancia(int id) {
    SQLiteDatabase db = this.getWritableDatabase();
    ContentValues values = new ContentValues();
    values.put("cantidad", 0.0);
    db.update("alcancia", values, "id = ?", new String[]{String.valueOf(id)});
    db.close();
}


    // Borrar alcancía
    public void borrarAlcancia(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("alcancia", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Actualizar nombre e icono

    public void actualizarAlcanciaCantidad(String nombreAlcancia, double nuevaCantidad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cantidad", nuevaCantidad);
        db.update("alcancia", values, "nombre = ?", new String[]{nombreAlcancia});
        db.close();
    }


    public void actualizarAlcancia(String nombreOriginal, String nuevoNombre, String nuevoIcono) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", nuevoNombre);
        values.put("icono", nuevoIcono);
        db.update("alcancia", values, "nombre = ?", new String[]{nombreOriginal});
        db.close();
    }



    public void eliminarAlcancia(String nombre) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("alcancia", "nombre = ?", new String[]{nombre});
        db.close();
    }

    // Insertar movimiento principal y detalles
    public void registrarMovimiento(int idAlcancia, String tipo, String fecha, double montoTotal, List<DenominacionCantidad> detalles) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put("id_alcancia", idAlcancia);
            values.put("tipo", tipo);
            values.put("fecha", fecha);
            values.put("monto_total", montoTotal);

            long idMovimiento = db.insert("movimiento", null, values);

            for (DenominacionCantidad d : detalles) {
                ContentValues detalleValues = new ContentValues();
                detalleValues.put("id_movimiento", idMovimiento);
                detalleValues.put("denominacion", d.getDenominacion());
                detalleValues.put("cantidad", d.getCantidad());
                db.insert("detalle_movimiento", null, detalleValues);
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    // Obtener movimientos por alcancía
    public List<Movimiento> obtenerMovimientosDeAlcancia(int idAlcancia) {
        List<Movimiento> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Log.d("DB", "Buscando movimientos para alcancía ID: " + idAlcancia);
        Cursor cursor = db.rawQuery("SELECT * FROM movimiento WHERE id_alcancia = ? ORDER BY fecha DESC", new String[]{String.valueOf(idAlcancia)});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"));
                String fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha"));
                double monto = cursor.getDouble(cursor.getColumnIndexOrThrow("monto_total"));
                lista.add(new Movimiento(id, idAlcancia, tipo, fecha, monto));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    // Obtener detalles de un movimiento
    public List<DenominacionCantidad> obtenerDetallesDeMovimiento(int idMovimiento) {
        List<DenominacionCantidad> detalles = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT denominacion, cantidad FROM detalle_movimiento WHERE id_movimiento = ?", new String[]{String.valueOf(idMovimiento)});
        if (cursor.moveToFirst()) {
            do {
                double denom = cursor.getDouble(cursor.getColumnIndexOrThrow("denominacion"));
                int cant = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"));
                detalles.add(new DenominacionCantidad(denom, cant));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return detalles;
    }
    // Insertar movimiento principal (retorna ID)
    public long insertarMovimiento(int idAlcancia, String tipo, String fechaHora, double total) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_alcancia", idAlcancia);
        values.put("tipo", tipo);
        values.put("fecha", fechaHora);             // ✅ nombre correcto
        values.put("monto_total", total);           // ✅ nombre correcto

        Log.d("DB", "Movimiento insertado para alcancía ID: " + idAlcancia + " con total: " + total);
        long idMovimiento = db.insert("movimiento", null, values);
        db.close();
        return idMovimiento;
    }


    // Insertar detalles del movimiento (denominaciones)
    public void insertarDetalleMovimiento(long idMovimiento, String denominacion, int cantidad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("id_movimiento", idMovimiento);
        values.put("denominacion", denominacion);
        values.put("cantidad", cantidad);
        db.insert("detalle_movimiento", null, values);
        db.close();
    }
    public double obtenerCantidadActual(int idAlcancia) {
        double cantidad = 0.0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT cantidad FROM alcancia WHERE id = ?", new String[]{String.valueOf(idAlcancia)});
        if (cursor.moveToFirst()) {
            cantidad = cursor.getDouble(0);
        }
        cursor.close();
        db.close();
        return cantidad;
    }
    public void actualizarAlcanciaCantidadPorId(int idAlcancia, double nuevaCantidad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("cantidad", nuevaCantidad);
        db.update("alcancia", values, "id = ?", new String[]{String.valueOf(idAlcancia)});
        db.close();
    }








}
