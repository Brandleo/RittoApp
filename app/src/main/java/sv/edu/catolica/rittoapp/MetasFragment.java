package sv.edu.catolica.rittoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Locale;

public class MetasFragment extends Fragment {

    private LinearLayout contenedorMetas;
    private boolean showButton;
    public static MetasFragment newInstance(boolean showButton) {
        MetasFragment fragment = new MetasFragment();
        Bundle args = new Bundle();
        args.putBoolean("show_button", showButton);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            showButton = getArguments().getBoolean("show_button", true);
        }

        ImageButton btnAgregarMeta = view.findViewById(R.id.btnAgregarMeta);
        btnAgregarMeta.setVisibility(showButton ? View.GONE : View.GONE);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_metas, container, false);

        contenedorMetas = vista.findViewById(R.id.contenedorMetas);
        mostrarTodasLasMetas();

        return vista;
    }

    private void mostrarTodasLasMetas() {
        contenedorMetas.removeAllViews();
        AdminDB db = new AdminDB(requireContext());
        List<Alcancia> alcancias = db.obtenerAlcanciasPorPerfil(Sesion.perfilActualNombre);

        for (Alcancia alc : alcancias) {
            double saldo = db.obtenerCantidadActual(alc.getId());
            List<Meta> metas = db.obtenerMetasPorAlcancia(alc.getId());

            TextView txtNombre = new TextView(getContext());
            txtNombre.setText("Metas de: " + alc.getNombre() + " (Saldo: $" + String.format(Locale.US, "%.2f", saldo) + ")");
            txtNombre.setTextSize(16);
            txtNombre.setPadding(0, 16, 0, 8);
            contenedorMetas.addView(txtNombre);

            for (Meta meta : metas) {
                View vista = getLayoutInflater().inflate(R.layout.item_meta, null);
                TextView txtMeta = vista.findViewById(R.id.txtMontoMeta);
                TextView txtNombreMeta = vista.findViewById(R.id.txtNombreMeta);
                TextView txtFaltan = vista.findViewById(R.id.txtMontoFaltante);

                txtNombreMeta.setText(meta.getNombre());
                txtMeta.setText(String.format("La meta es: $%.2f", meta.getMontoObjetivo()));

                double falta = meta.getMontoObjetivo() - saldo;
                if (falta <= 0) {
                    txtFaltan.setText("✅ ¡Meta alcanzada!");
                    txtFaltan.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200));
                } else {
                    txtFaltan.setText(String.format("Te hacen falta: $%.2f", falta));
                }

                contenedorMetas.addView(vista);
            }
        }
    }
}
