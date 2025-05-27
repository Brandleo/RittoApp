package sv.edu.catolica.rittoapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DenominacionStockHelper {
    public static HashMap<Double, Integer> obtenerStockDeDenominaciones(AdminDB db, int idAlcancia) {
        List<Movimiento> movimientos = db.obtenerMovimientosDeAlcancia(idAlcancia);
        HashMap<Double, Integer> stock = new HashMap<>();

        for (Movimiento m : movimientos) {
            List<DenominacionCantidad> desglose = db.obtenerDetallesDeMovimiento(m.getId());
            for (DenominacionCantidad d : desglose) {
                double denom = d.getDenominacion();
                int cantidad = d.getCantidad();
                int actual = stock.getOrDefault(denom, 0);

                if (m.getTipo().equalsIgnoreCase("deposito")) {
                    stock.put(denom, actual + cantidad);
                } else if (m.getTipo().equalsIgnoreCase("retiro")) {
                    stock.put(denom, actual - cantidad);
                }
            }
        }

        return stock;
    }

    /**
     * Devuelve un mapa con el stock actual por denominación de una alcancía.
     * @param db AdminDB instancia de la base de datos
     * @param idAlcancia ID de la alcancía
     * @return Map con clave = denominación, valor = cantidad actual disponible
     */
    public static Map<Double, Integer> getStockDenominaciones(AdminDB db, int idAlcancia) {
        SQLiteDatabase database = db.getReadableDatabase();
        Map<Double, Integer> stock = new HashMap<>();

        Cursor cursor = database.rawQuery("SELECT tipo, denominacion, cantidad FROM movimiento m "+
                "JOIN detalle_movimiento d ON m.id = d.id_movimiento WHERE m.id_alcancia = ?", new String[]{String.valueOf(idAlcancia)});

        if (cursor.moveToFirst()) {
            do {
                String tipo = cursor.getString(cursor.getColumnIndexOrThrow("tipo"));
                double denom = cursor.getDouble(cursor.getColumnIndexOrThrow("denominacion"));
                int cant = cursor.getInt(cursor.getColumnIndexOrThrow("cantidad"));

                int actual = stock.getOrDefault(denom, 0);
                if ("deposito".equalsIgnoreCase(tipo)) {
                    stock.put(denom, actual + cant);
                } else if ("retiro".equalsIgnoreCase(tipo)) {
                    stock.put(denom, actual - cant);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        database.close();
        return stock;
    }
}
