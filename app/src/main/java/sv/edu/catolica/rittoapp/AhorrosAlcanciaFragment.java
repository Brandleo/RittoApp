package sv.edu.catolica.rittoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AhorrosAlcanciaFragment extends Fragment {

    private int idAlcancia;
    private String nombre;
    private String icono;
    private boolean sellada;
    private TextView txtTotal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_ahorros_alcancia, container, false);

        TextView txtNombre = vista.findViewById(R.id.txtNombreAhorro);
        txtTotal = vista.findViewById(R.id.txtTotalAhorro);
        ImageView imgIcono = vista.findViewById(R.id.imgIconoAhorro);
        ImageButton btnDepositar = vista.findViewById(R.id.btnDepositarDinero);

        Bundle args = getArguments();
        if (args != null) {
            idAlcancia = args.getInt("id", -1);
            nombre = args.getString("nombre");
            icono = args.getString("icono");
            sellada = args.getBoolean("sellada", false);

            AdminDB db = new AdminDB(requireContext());
            double cantidad = db.obtenerCantidadActual(idAlcancia);

            txtNombre.setText(nombre);
            txtTotal.setText(String.format(getString(R.string.total_2f), cantidad));
            int resId = requireContext().getResources().getIdentifier(icono, "drawable", requireContext().getPackageName());
            imgIcono.setImageResource(resId != 0 ? resId : R.drawable.tunco);
        }
        ImageButton btnRetirar = vista.findViewById(R.id.btnRetirarDinero);
        btnRetirar.setOnClickListener(v -> {
            if (sellada) {
                Toast.makeText(getContext(), R.string.esta_alcanc_a_est_sellada_y_no_permite_retiros, Toast.LENGTH_LONG).show();
                return;
            }

            Intent intent = new Intent(getContext(), RetiroActivity.class);
            intent.putExtra("id", idAlcancia);
            intent.putExtra("nombre", nombre);
            intent.putExtra("icono", icono);
            intent.putExtra("sellada", sellada);
            startActivity(intent);
        });
        imgIcono.setOnClickListener(v -> {
            AdminDB db = new AdminDB(requireContext());
            HashMap<Double, Integer> stock = DenominacionStockHelper.obtenerStockDeDenominaciones(db, idAlcancia);

            StringBuilder mensaje = new StringBuilder();
            double total = 0;
            List<Double> ordenadas = new ArrayList<>(stock.keySet());
            Collections.sort(ordenadas);

            for (double denom : ordenadas) {
                int cantidad = stock.get(denom);
                if (cantidad > 0) {
                    mensaje.append(String.format(Locale.US, getString(R.string.d_de_2f), cantidad, denom));
                    total += denom * cantidad;
                }
            }

            if (mensaje.length() == 0) {
                mensaje.append(getString(R.string.no_hay_monedas_ni_billetes_registrados));
            } else {
                mensaje.append(String.format(Locale.US, getString(R.string.total_desglosado_2f), total));
            }

            new AlertDialog.Builder(requireContext())
                    .setTitle(R.string.desglose_total)
                    .setMessage(mensaje.toString())
                    .setPositiveButton(R.string.cerrar, null)
                    .show();
        });


        btnDepositar.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), DepositoActivity.class);
            intent.putExtra("id", idAlcancia);
            intent.putExtra("nombre", nombre);
            intent.putExtra("icono", icono);
            intent.putExtra("sellada", sellada);
            startActivity(intent);
        });


        return vista;
    }

    @Override
    public void onResume() {
        super.onResume();
        AdminDB db = new AdminDB(requireContext());
        double actual = db.obtenerCantidadActual(idAlcancia);
        txtTotal.setText(String.format(getString(R.string.total_2f), actual));
    }

}
