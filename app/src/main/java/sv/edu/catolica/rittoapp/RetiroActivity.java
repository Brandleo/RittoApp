package sv.edu.catolica.rittoapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class RetiroActivity extends AppCompatActivity {

    private int idAlcancia;
    private String nombreAlcancia;
    private String icono;
    private boolean sellada;
    private AdminDB db;

    private LinearLayout contenedorDenominaciones;
    private TextView txtTotal;
    private HashMap<Double, Integer> conteo;
    private double total = 0.0;

    private final double[] denominaciones = {
            0.01, 0.05, 0.10, 0.25, 0.50, 1.00,
            2.00, 5.00, 10.00, 20.00,50.00,100.00
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposito); // Usamos el mismo layout

        db = new AdminDB(this);

        ImageView imgIcono = findViewById(R.id.imgIconoAlcancia);
        TextView txtNombre = findViewById(R.id.txtNombreAlcancia);
        txtTotal = findViewById(R.id.txtTotalDeposito);
        contenedorDenominaciones = findViewById(R.id.contenedorDenominaciones);
        Button btnConfirmar = findViewById(R.id.btnConfirmarDeposito);
        btnConfirmar.setText("Retirar");

        // Datos recibidos
        idAlcancia = getIntent().getIntExtra("id", -1);
        nombreAlcancia = getIntent().getStringExtra("nombre");
        icono = getIntent().getStringExtra("icono");
        sellada = getIntent().getBooleanExtra("sellada", true);

        txtNombre.setText(nombreAlcancia);
        int resId = getResources().getIdentifier(icono, "drawable", getPackageName());
        imgIcono.setImageResource(resId != 0 ? resId : R.drawable.tunco);

        conteo = new HashMap<>();
        for (double d : denominaciones) conteo.put(d, 0);

        for (double valor : denominaciones) {
            View fila = getLayoutInflater().inflate(R.layout.item_denominacion, null);
            TextView txtValor = fila.findViewById(R.id.txtValorDenominacion);
            EditText txtCantidad = fila.findViewById(R.id.txtCantidad);
            Button btnMas = fila.findViewById(R.id.btnSumar);
            Button btnMenos = fila.findViewById(R.id.btnRestar);

            txtValor.setText(String.format(Locale.US, "$%.2f", valor));
            txtCantidad.setText("0");

            txtCantidad.setOnFocusChangeListener((view, hasFocus) -> {
                if (hasFocus && txtCantidad.getText().toString().equals("0")) {
                    txtCantidad.setText("");
                }
            });

            txtCantidad.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    try {
                        int cantidad = Integer.parseInt(s.toString());
                        conteo.put(valor, cantidad);
                    } catch (NumberFormatException e) {
                        conteo.put(valor, 0);
                    }
                    calcularTotal();
                }
            });

            btnMas.setOnClickListener(v -> {
                int actual = conteo.get(valor);
                conteo.put(valor, actual + 1);
                txtCantidad.setText(String.valueOf(actual + 1));
            });

            btnMenos.setOnClickListener(v -> {
                int actual = conteo.get(valor);
                if (actual > 0) {
                    conteo.put(valor, actual - 1);
                    txtCantidad.setText(String.valueOf(actual - 1));
                }
            });

            contenedorDenominaciones.addView(fila);
        }

        btnConfirmar.setOnClickListener(v -> {
            if (sellada) {
                Toast.makeText(this, "Esta alcancía está sellada y no permite retiros.", Toast.LENGTH_LONG).show();
                return;
            }

            if (total <= 0) {
                Toast.makeText(this, "Ingresa una cantidad válida para retirar", Toast.LENGTH_SHORT).show();
                return;
            }

            double actual = db.obtenerCantidadActual(idAlcancia);
            if (total > actual) {
                Toast.makeText(this, "No hay suficiente dinero en la alcancía", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validar stock disponible por denominación
            HashMap<Double, Integer> stockActual = DenominacionStockHelper.obtenerStockDeDenominaciones(db, idAlcancia);

            for (double denom : conteo.keySet()) {
                int solicitada = conteo.get(denom);
                int disponible = stockActual.getOrDefault(denom, 0);
                if (solicitada > disponible) {
                    Toast.makeText(this, "No tienes suficientes de $" + String.format(Locale.US, "%.2f", denom), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            db.actualizarAlcanciaCantidadPorId(idAlcancia, actual - total);

            String fecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date());
            long idMovimiento = db.insertarMovimiento(idAlcancia, "retiro", fecha, total);

            for (double valor : conteo.keySet()) {
                int cantidad = conteo.get(valor);
                if (cantidad > 0) {
                    db.insertarDetalleMovimiento(idMovimiento, String.valueOf(valor), cantidad);
                }
            }

            Toast.makeText(this, "Retiro registrado", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void calcularTotal() {
        total = 0;
        for (double valor : conteo.keySet()) {
            total += valor * conteo.get(valor);
        }
        txtTotal.setText(String.format(Locale.US, "Total: $%.2f", total));
    }
}
