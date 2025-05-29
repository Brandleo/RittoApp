package sv.edu.catolica.rittoapp;

import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.List;
import java.util.Locale;

public class MetasAlcanciaFragment extends Fragment {

    private int idAlcancia;
    private double saldo;
    private LinearLayout contenedorMetas;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_metas, container, false);

        contenedorMetas = vista.findViewById(R.id.contenedorMetas);
        ImageButton btnAgregar = vista.findViewById(R.id.btnAgregarMeta);

        Bundle args = getArguments();
        if (args != null) {
            idAlcancia = args.getInt("id", -1);
        }

        AdminDB db = new AdminDB(requireContext());
        saldo = db.obtenerCantidadActual(idAlcancia);

        mostrarMetas();

        btnAgregar.setOnClickListener(v -> mostrarDialogoCrearMeta());

        return vista;
    }

    private void mostrarMetas() {
        contenedorMetas.removeAllViews();
        AdminDB db = new AdminDB(requireContext());
        List<Meta> metas = db.obtenerMetasPorAlcancia(idAlcancia);

        TextView txtSaldo = new TextView(getContext());
        txtSaldo.setText(getString(R.string.saldo_actual) + String.format(Locale.US, "%.2f", saldo));
        contenedorMetas.addView(txtSaldo);

        for (Meta meta : metas) {
            View vista = getLayoutInflater().inflate(R.layout.item_meta, null);
            TextView txtNombre = vista.findViewById(R.id.txtNombreMeta);
            TextView txtMeta = vista.findViewById(R.id.txtMontoMeta);
            TextView txtFaltan = vista.findViewById(R.id.txtMontoFaltante);
            ImageButton btnEliminar = vista.findViewById(R.id.btnEliminarMeta);

            txtNombre.setText(meta.getNombre());
            txtMeta.setText(String.format(getString(R.string.la_meta_es_2ffragment), meta.getMontoObjetivo()));

            double falta = meta.getMontoObjetivo() - saldo;
            if (falta <= 0) {
                txtFaltan.setText(R.string.meta_alcanzada);
                txtFaltan.setTextColor(ContextCompat.getColor(requireContext(), R.color.teal_200));
            } else {
                txtFaltan.setText(String.format(getString(R.string.te_hacen_falta_2f), falta));
            }

            btnEliminar.setOnClickListener(view -> {
                new AlertDialog.Builder(requireContext())
                        .setTitle(R.string.eliminar_metas)
                        .setMessage(R.string.est_s_seguro_de_que_quieres_eliminar_esta_meta)
                        .setPositiveButton(R.string.s, (dialog, which) -> {
                            AdminDB dbEliminar = new AdminDB(requireContext());
                            dbEliminar.eliminarMeta(meta.getId());
                            mostrarMetas();
                        })
                        .setNegativeButton(R.string.cancelar, null)
                        .show();
            });

            contenedorMetas.addView(vista);
        }
    }


    private void mostrarDialogoCrearMeta() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_crear_meta, null);
        builder.setView(dialogView);
        builder.setTitle(R.string.nueva_meta);

        EditText inputNombre = dialogView.findViewById(R.id.inputNombreMeta);
        EditText inputMonto = dialogView.findViewById(R.id.inputMontoMeta);

        // ✅ Limitar a 2 decimales
        inputMonto.setFilters(new InputFilter[]{new DecimalDigitsInputFilter(6, 2)});

        builder.setPositiveButton(R.string.crear, (dialog, which) -> {
            String nombre = inputNombre.getText().toString().trim();
            String montoStr = inputMonto.getText().toString().trim();

            if (!nombre.isEmpty() && !montoStr.isEmpty()) {
                double monto = Double.parseDouble(montoStr);
                AdminDB db = new AdminDB(requireContext());
                db.insertarMeta(idAlcancia, nombre, monto);
                mostrarMetas();
            } else {
                Toast.makeText(getContext(), R.string.campos_incompletos, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton(R.string.cancelar, null);
        builder.create().show();
    }

}
