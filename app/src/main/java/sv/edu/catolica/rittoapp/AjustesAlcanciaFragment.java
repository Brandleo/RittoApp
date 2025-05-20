package sv.edu.catolica.rittoapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.Arrays;
import java.util.List;

public class AjustesAlcanciaFragment extends Fragment {

    private EditText inputNuevoNombre;
    private ImageView imgIcono;
    private Button btnAnterior, btnSiguiente, btnGuardar, btnVaciar, btnBorrar;

    private int iconoIndex = 0;
    private List<IconoItem> iconos;
    private String nombreActual;
    private AdminDB db;
    private Alcancia alcancia;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_ajustes_alcancia, container, false);

        db = new AdminDB(requireContext());

        // Obtener datos de la alcancía desde el Bundle
        Bundle bundle = getArguments();
        if (bundle == null) {
            Toast.makeText(getContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show();
            return vista;
        }

        nombreActual = bundle.getString("nombre");
        String icono = bundle.getString("icono");
        double cantidad = bundle.getDouble("cantidad");

        // Cargar alcancía desde la base de datos si se desea precisión
        List<Alcancia> lista = db.obtenerAlcanciasPorPerfil(Sesion.perfilActualNombre);
        for (Alcancia a : lista) {
            if (a.getNombre().equals(nombreActual)) {
                alcancia = a;
                break;
            }
        }

        // Configurar iconos disponibles
        iconos = Arrays.asList(
                new IconoItem("cerdito", R.drawable.tunco),
                new IconoItem("balon", R.drawable.balon),
                new IconoItem("barril", R.drawable.barril)
        );

        // Inicializar vistas
        inputNuevoNombre = vista.findViewById(R.id.inputNuevoNombreAlcancia);
        imgIcono = vista.findViewById(R.id.imgIconoAlcancia);
        btnAnterior = vista.findViewById(R.id.btnAnteriorIcono);
        btnSiguiente = vista.findViewById(R.id.btnSiguienteIcono);
        btnGuardar = vista.findViewById(R.id.btnGuardarCambiosAlcancia);
        btnVaciar = vista.findViewById(R.id.btnVaciarAlcancia);
        btnBorrar = vista.findViewById(R.id.btnBorrarAlcancia);

        // Mostrar datos actuales
        inputNuevoNombre.setText(nombreActual);
        iconoIndex = buscarIndiceIcono(icono);
        imgIcono.setImageResource(iconos.get(iconoIndex).getResId());

        // Cambiar iconos
        btnAnterior.setOnClickListener(v -> cambiarIcono(-1));
        btnSiguiente.setOnClickListener(v -> cambiarIcono(1));

        // Guardar cambios
        btnGuardar.setOnClickListener(v -> {
            String nuevoNombre = inputNuevoNombre.getText().toString().trim();
            if (nuevoNombre.isEmpty()) {
                Toast.makeText(getContext(), "Ingresa un nombre", Toast.LENGTH_SHORT).show();
                return;
            }

            String nuevoIcono = iconos.get(iconoIndex).getNombre();
            db.actualizarAlcancia(nombreActual, nuevoNombre, nuevoIcono);
            nombreActual = nuevoNombre;

            Toast.makeText(getContext(), "Alcancía actualizada", Toast.LENGTH_SHORT).show();
        });

        // Vaciar
        btnVaciar.setOnClickListener(v -> {
            db.vaciarAlcancia(nombreActual);
            Toast.makeText(getContext(), "Alcancía vaciada", Toast.LENGTH_SHORT).show();
        });

        // Borrar
        btnBorrar.setOnClickListener(v -> {
            new AlertDialog.Builder(requireContext())
                    .setTitle("¿Eliminar alcancía?")
                    .setMessage("Esta acción no se puede deshacer.")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        db.eliminarAlcancia(nombreActual);
                        requireActivity().finish();
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();
        });

        return vista;
    }

    private int buscarIndiceIcono(String nombreIcono) {
        for (int i = 0; i < iconos.size(); i++) {
            if (iconos.get(i).getNombre().equals(nombreIcono)) {
                return i;
            }
        }
        return 0;
    }

    private void cambiarIcono(int cambio) {
        iconoIndex = (iconoIndex + cambio + iconos.size()) % iconos.size();
        imgIcono.setImageResource(iconos.get(iconoIndex).getResId());
    }
}
