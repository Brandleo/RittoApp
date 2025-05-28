package sv.edu.catolica.rittoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Locale;

public class InicioFragment extends Fragment {

    private LinearLayout contenedorResumen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_inicio, container, false);
        contenedorResumen = vista.findViewById(R.id.contenedorResumen);

        mostrarResumen();

        return vista;
    }

    private void mostrarResumen() {
        AdminDB db = new AdminDB(requireContext());

        // Total global
        double totalGlobal = db.obtenerTotalGlobal();
        TextView txtTotal = new TextView(getContext());
        txtTotal.setText("Total global ahorrado: $" + String.format(Locale.US, "%.2f", totalGlobal));
        txtTotal.setTextSize(18);
        txtTotal.setPadding(0, 0, 0, 24);
        contenedorResumen.addView(txtTotal);

        // Desglose global
        HashMap<Double, Integer> desglose = db.obtenerDesgloseGlobal();
        for (double denom : desglose.keySet()) {
            int cantidad = desglose.get(denom);
            if (cantidad > 0) {
                TextView txtDenom = new TextView(getContext());
                txtDenom.setText(cantidad + " × $" + String.format(Locale.US, "%.2f", denom));
                txtDenom.setTextSize(16);
                contenedorResumen.addView(txtDenom);
            }
        }
    }
}
