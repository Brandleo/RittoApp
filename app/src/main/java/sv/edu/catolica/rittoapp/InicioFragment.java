package sv.edu.catolica.rittoapp;

import android.graphics.Color;
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

        double totalGlobal = db.obtenerTotalGlobal();
        HashMap<Double, Integer> desglose = db.obtenerDesgloseGlobal();

        LinearLayout tarjetaResumen = (LinearLayout) contenedorResumen.getChildAt(1); // la tarjeta verde

        TextView totalTxt = tarjetaResumen.findViewById(R.id.totalAhorros);
        totalTxt.setText(getString(R.string.total_global_ahorradoo) + String.format(Locale.US, "%.2f", totalGlobal));

        for (double denom : desglose.keySet()) {
            int cantidad = desglose.get(denom);
            if (cantidad > 0) {
                TextView txtDenom = new TextView(getContext());
                txtDenom.setText(cantidad + " × $" + String.format(Locale.US, "%.2f", denom));
                txtDenom.setTextSize(16);
                txtDenom.setTextColor(Color.parseColor("#01200F"));
                tarjetaResumen.addView(txtDenom);
            }
        }
    }
}
