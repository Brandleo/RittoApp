package sv.edu.catolica.rittoapp;

import android.app.DatePickerDialog;
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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
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
        configurarDatePicker();
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

        spinnerTipo.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                filtrarMovimientos();
            }
            @Override public void onNothingSelected(android.widget.AdapterView<?> parent) {}
        });

        return vista;
    }
    private void configurarDatePicker() {
        // Quitamos el TextWatcher ya que ahora seleccionaremos la fecha con el picker
        inputFecha.setOnClickListener(v -> mostrarDatePicker());
    }
    private void mostrarDatePicker() {
        // Obtener la fecha actual para mostrarla en el picker
        final Calendar calendario = Calendar.getInstance();
        int año = calendario.get(Calendar.YEAR);
        int mes = calendario.get(Calendar.MONTH);
        int dia = calendario.get(Calendar.DAY_OF_MONTH);

        // Crear el DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                (view, añoSeleccionado, mesSeleccionado, diaSeleccionado) -> {
                    // Formatear la fecha como YYYY-MM-DD
                    String fechaFormateada = String.format(Locale.US, "%04d-%02d-%02d",
                            añoSeleccionado, mesSeleccionado + 1, diaSeleccionado);

                    inputFecha.setText(fechaFormateada);
                    filtrarMovimientos(); // Aplicar el filtro automáticamente
                },
                año, mes, dia);

        datePickerDialog.show();
    }
    private void mostrarDesgloseMovimiento(Movimiento movimiento) {
        List<DenominacionCantidad> desglose = db.obtenerDetallesDeMovimiento(movimiento.getId());

        if (desglose.isEmpty()) {
            Toast.makeText(getContext(), R.string.no_hay_detalles_registrados_para_este_movimiento, Toast.LENGTH_SHORT).show();
            return;
        }

        StringBuilder mensaje = new StringBuilder();
        for (DenominacionCantidad d : desglose) {
            mensaje.append(String.format(Locale.US, "$%.2f × %d = $%.2f\n",
                    d.getDenominacion(), d.getCantidad(), d.getDenominacion() * d.getCantidad()));
        }

        new AlertDialog.Builder(requireContext())
                .setTitle(R.string.desglose_de_movimiento)
                .setMessage(mensaje.toString())
                .setPositiveButton(R.string.cerrarr, null)
                .show();
    }


    private void filtrarMovimientos() {
        String fechaFiltro = inputFecha.getText().toString().trim();
        String tipoFiltro = spinnerTipo.getSelectedItem().toString();

        List<Movimiento> filtrados = new ArrayList<>();
        for (Movimiento m : movimientosOriginales) {
            boolean coincideFecha = fechaFiltro.isEmpty() || m.getFecha().startsWith(fechaFiltro);
            boolean coincideTipo = tipoFiltro.equals(getString(R.string.todos)) ||
                    m.getTipo().equalsIgnoreCase(getLocalizedType(tipoFiltro));
            if (coincideFecha && coincideTipo) {
                filtrados.add(m);
            }
        }
        adapter.actualizarLista(filtrados);
    }

    private String getLocalizedType(String displayedType) {
        if (displayedType.equals(getString(R.string.deposito))) return getString(R.string.deposito);
        if (displayedType.equals(getString(R.string.retiro))) return getString(R.string.retiros);
        return displayedType;
    }
}
