package sv.edu.catolica.rittoapp;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistorialAlcanciaFragment extends Fragment {

    private AdminDB db;
    private RecyclerView recyclerView;
    private HistorialAdapter adapter;
    private EditText inputFecha;
    private Spinner spinnerTipo;

    private int idAlcancia;
    private List<Movimiento> movimientosOriginales;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_historial_alcancia, container, false);

        inputFecha = vista.findViewById(R.id.inputFecha);
        spinnerTipo = vista.findViewById(R.id.spinnerFiltroTipo);
        recyclerView = vista.findViewById(R.id.recyclerHistorial);

        db = new AdminDB(requireContext());

        // Recibir ID de la alcancía
        Bundle args = getArguments();
        if (args != null) {
            idAlcancia = args.getInt("id", -1);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        movimientosOriginales = db.obtenerMovimientosDeAlcancia(idAlcancia);
        adapter = new HistorialAdapter(movimientosOriginales, movimiento -> {
            // Aquí llamas a un método para mostrar el desglose del movimiento
            mostrarDesgloseMovimiento(movimiento);
        });

        recyclerView.setAdapter(adapter);

        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.filtro_tipos, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(spinnerAdapter);

        inputFecha.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filtrarMovimientos();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        spinnerTipo.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                filtrarMovimientos();
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        return vista;
    }
    private void mostrarDesgloseMovimiento(Movimiento movimiento) {
        List<DenominacionCantidad> desglose = db.obtenerDetallesDeMovimiento(movimiento.getId());

        if (desglose.isEmpty()) {
            Toast.makeText(getContext(), "No hay detalles registrados para este movimiento", Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder mensaje = new StringBuilder();
        for (DenominacionCantidad d : desglose) {
            mensaje.append(String.format(Locale.US, "$%.2f × %d = $%.2f\n",
                    d.getDenominacion(), d.getCantidad(), d.getDenominacion() * d.getCantidad()));
        }

        new androidx.appcompat.app.AlertDialog.Builder(requireContext())
                .setTitle("Desglose de Movimiento")
                .setMessage(mensaje.toString())
                .setPositiveButton("Cerrar", null)
                .show();
    }


    private void filtrarMovimientos() {
        String fechaFiltro = inputFecha.getText().toString().trim();
        String tipoFiltro = spinnerTipo.getSelectedItem().toString();

        List<Movimiento> filtrados = new ArrayList<>();
        for (Movimiento m : movimientosOriginales) {
            boolean coincideFecha = fechaFiltro.isEmpty() || m.getFecha().startsWith(fechaFiltro);
            boolean coincideTipo = tipoFiltro.equals("Todos") || m.getTipo().equalsIgnoreCase(tipoFiltro);
            if (coincideFecha && coincideTipo) {
                filtrados.add(m);
            }
        }
        adapter.actualizarLista(filtrados);
    }
}
