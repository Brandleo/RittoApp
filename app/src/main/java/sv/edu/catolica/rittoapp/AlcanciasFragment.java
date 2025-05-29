package sv.edu.catolica.rittoapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
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

        // Leer perfil desde SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("perfil_sesion", Context.MODE_PRIVATE);
        perfilActual = prefs.getString("nombrePerfil", null);

        if (perfilActual == null || perfilActual.isEmpty()) {
            Toast.makeText(getContext(), "Debes seleccionar un perfil primero", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
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
        CheckBox checkSellada = dialogoVista.findViewById(R.id.checkSellada);
        ImageView imgIcono = dialogoVista.findViewById(R.id.imgIconoActual);
        Button btnAnterior = dialogoVista.findViewById(R.id.btnIconoAnterior);
        Button btnSiguiente = dialogoVista.findViewById(R.id.btnIconoSiguiente);

        inputCantidad.setVisibility(View.GONE);

        // Configurar el input para moneda (2 decimales)
        inputCantidad.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        inputCantidad.setFilters(new InputFilter[] {
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        String newText = dest.toString().substring(0, dstart)
                                + source.toString()
                                + dest.toString().substring(dend);

                        // Permitir solo números con máximo 2 decimales
                        if (newText.matches("^\\d+(\\.\\d{0,2})?$") || newText.isEmpty()) {
                            return null; // aceptar el input
                        }
                        return ""; // rechazar el input
                    }
                }
        });

        List<IconoItem> iconos = new ArrayList<>();
        iconos.add(new IconoItem("barril", R.drawable.barril));
        iconos.add(new IconoItem("huevo", R.drawable.huevo));
        iconos.add(new IconoItem("cerdoblue", R.drawable.cerdoblue));
        iconos.add(new IconoItem("cerdorosa", R.drawable.cerdorosa));
        iconos.add(new IconoItem("bolaristal", R.drawable.bolaristal));



        final int[] index = {0};
        imgIcono.setImageResource(iconos.get(index[0]).getRecursoDrawable());

        btnAnterior.setOnClickListener(v -> {
            index[0] = (index[0] - 1 + iconos.size()) % iconos.size();
            imgIcono.setImageResource(iconos.get(index[0]).getRecursoDrawable());
        });

        btnSiguiente.setOnClickListener(v -> {
            index[0] = (index[0] + 1) % iconos.size();
            imgIcono.setImageResource(iconos.get(index[0]).getRecursoDrawable());
        });

        AlertDialog dialog = new AlertDialog.Builder(getContext(), R.style.Theme_RittoApp_Dialog)
                .setView(dialogoVista)
                .setPositiveButton("Guardar", null)
                .setNegativeButton("Cancelar", null)
                .create();

        // SOLUCIÓN DEFINITIVA PARA LOS BOTONES
        dialog.setOnShowListener(dlg -> {
            Button btnGuardar = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            Button btnCancelar = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);

            // Obtener el color adecuado según el tema actual
            int color;
            int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                // Modo oscuro - usar color claro
                color = ContextCompat.getColor(requireContext(), R.color.teal_700);
            } else {
                // Modo claro - usar color oscuro
                color = ContextCompat.getColor(requireContext(), R.color.teal_700);
            }

            // Aplicar estilos a los botones
            btnGuardar.setTextColor(color);
            btnCancelar.setTextColor(color);
            btnGuardar.setAllCaps(false);
            btnCancelar.setAllCaps(false);
            btnGuardar.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
            btnCancelar.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            btnGuardar.setOnClickListener(v -> {
                String nombre = inputNombre.getText().toString().trim();
                String cantidadStr = inputCantidad.getText().toString().trim();
                boolean sellada = checkSellada.isChecked();
                String icono = iconos.get(index[0]).getNombre();

                if (nombre.isEmpty()) {
                    inputNombre.setError("Nombre requerido");
                    return;
                }

                // Validación del monto
                if (!cantidadStr.isEmpty()) {
                    try {
                        if (cantidadStr.contains(".") && cantidadStr.split("\\.")[1].length() > 2) {
                            inputCantidad.setError("Máximo 2 decimales");
                            return;
                        }

                        double cantidad = Double.parseDouble(cantidadStr);
                        if (cantidad < 0) {
                            inputCantidad.setError("El monto no puede ser negativo");
                            return;
                        }

                        // Truncar a 2 decimales sin redondear
                        cantidad = Math.floor(cantidad * 100) / 100;

                        db.agregarAlcancia(nombre, cantidad, perfilActual, icono, sellada);
                        cargarLista();
                        dialog.dismiss();
                    } catch (NumberFormatException e) {
                        inputCantidad.setError("Formato inválido (ej: 5.00)");
                    }
                } else {
                    db.agregarAlcancia(nombre, 0.0, perfilActual, icono, sellada);
                    cargarLista();
                    dialog.dismiss();
                }
            });
        });

        dialog.show();
    }
}