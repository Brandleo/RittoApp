package sv.edu.catolica.rittoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AlcanciasFragment extends Fragment {

    private AdminDB db;
    private AlcanciaAdapter adapter;
    private RecyclerView recycler;
    private List<Alcancia> lista;
    private String perfilActual;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_alcancias, container, false);

        db = new AdminDB(requireContext());
        recycler = vista.findViewById(R.id.recyclerAlcancias);

        // ✅ Leer perfil desde SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("perfil_sesion", Context.MODE_PRIVATE);
        perfilActual = prefs.getString("nombrePerfil", null);

        if (perfilActual == null || perfilActual.isEmpty()) {
            Toast.makeText(getContext(), "Debes seleccionar un perfil primero", Toast.LENGTH_SHORT).show();
            requireActivity().finish(); // Cierra HomeActivity
            return vista;
        }


        vista.findViewById(R.id.btnAgregarAlcancia).setOnClickListener(v -> mostrarDialogoNueva());
        cargarLista();

        return vista;
    }

    private void cargarLista() {
        lista = db.obtenerAlcanciasPorPerfil(perfilActual);
        adapter = new AlcanciaAdapter(lista);
        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        recycler.setAdapter(adapter);
    }

    private void mostrarDialogoNueva() {
        View dialogoVista = LayoutInflater.from(getContext()).inflate(R.layout.dialog_agregar_alcancia, null);
        EditText inputNombre = dialogoVista.findViewById(R.id.inputNombreAlcancia);
        EditText inputCantidad = dialogoVista.findViewById(R.id.inputCantidadInicial);

        Spinner spinnerIcono = dialogoVista.findViewById(R.id.spinnerIcono);
        CheckBox checkSellada = dialogoVista.findViewById(R.id.checkSellada);

        List<IconoItem> iconos = new ArrayList<>();
        iconos.add(new IconoItem("cerdito", R.drawable.tunco));
        iconos.add(new IconoItem("balon", R.drawable.balon));
        iconos.add(new IconoItem("barril", R.drawable.barril));

        IconoSpinnerAdapter iconAdapter = new IconoSpinnerAdapter(getContext(), iconos);
        spinnerIcono.setAdapter(iconAdapter);


        new AlertDialog.Builder(getContext())
                .setTitle("Nueva Alcancía")
                .setView(dialogoVista)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = inputNombre.getText().toString();
                    String cantidadStr = inputCantidad.getText().toString();
                    IconoItem seleccionado = (IconoItem) spinnerIcono.getSelectedItem();
                    String icono = seleccionado.getNombre();
                    boolean sellada = checkSellada.isChecked();
                    if (!nombre.isEmpty() && !cantidadStr.isEmpty()) {
                        double cantidad = Double.parseDouble(cantidadStr);
                        db.agregarAlcancia(nombre, cantidad, perfilActual, icono, sellada);
                        cargarLista();
                    } else {
                        Toast.makeText(getContext(), "Completa todos los campos", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }
}
