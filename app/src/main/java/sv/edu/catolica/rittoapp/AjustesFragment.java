package sv.edu.catolica.rittoapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AjustesFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 101;
    private EditText inputNombre, inputPin;
    private ImageView imgPerfil;
    private Uri nuevaImagenUri;
    private AdminDB db;
    private String perfilActual;
    private Perfil perfilDatosActuales;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_ajustes, container, false);

        db = new AdminDB(requireContext());
        SharedPreferences prefs = requireContext().getSharedPreferences("perfil_sesion", Context.MODE_PRIVATE);
        perfilActual = prefs.getString("nombrePerfil", null);

        inputNombre = vista.findViewById(R.id.inputNuevoNombre);
        inputPin = vista.findViewById(R.id.inputNuevoPin);
        imgPerfil = vista.findViewById(R.id.imgNuevoPerfil);
        Button btnGuardar = vista.findViewById(R.id.btnGuardarCambios);
        Button btnCerrarSesion = vista.findViewById(R.id.btnCerrarSesion);

        // Cargar datos actuales del perfil
        if (perfilActual != null) {
            perfilDatosActuales = db.obtenerPerfilPorNombre(perfilActual);
            if (perfilDatosActuales != null) {
                inputNombre.setText(perfilDatosActuales.getNombre());
                inputPin.setText(perfilDatosActuales.getPin());

                if (perfilDatosActuales.getImagenUri() != null) {
                    File archivo = new File(perfilDatosActuales.getImagenUri());
                    if (archivo.exists()) {
                        imgPerfil.setImageURI(Uri.fromFile(archivo));
                    }
                }
            }
        }

        imgPerfil.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });

        btnGuardar.setOnClickListener(v -> {
            String nuevoNombre = inputNombre.getText().toString().trim();
            String nuevoPin = inputPin.getText().toString().trim();

            if (nuevoNombre.isEmpty() || nuevoPin.length() < 4) {
                Toast.makeText(getContext(), "Verifica los datos ingresados (PIN mínimo 4 dígitos)", Toast.LENGTH_SHORT).show();
                return;
            }

            String rutaImagenFinal;
            if (nuevaImagenUri != null) {
                rutaImagenFinal = guardarImagenInterna(nuevaImagenUri);
            } else {
                rutaImagenFinal = perfilDatosActuales.getImagenUri(); // mantener la actual
            }

            db.actualizarPerfil(perfilActual, nuevoNombre, rutaImagenFinal, nuevoPin);

            // Actualizar sesión si el nombre cambió
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("nombrePerfil", nuevoNombre);
            editor.apply();

            perfilActual = nuevoNombre;
            Toast.makeText(getContext(), "Perfil actualizado", Toast.LENGTH_SHORT).show();
        });

        btnCerrarSesion.setOnClickListener(v -> {
            prefs.edit().clear().apply();
            Sesion.perfilActualNombre = null;
            Intent intent = new Intent(requireContext(), SeleccionPerfil.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        return vista;
    }

    private String guardarImagenInterna(Uri uriOriginal) {
        try {
            InputStream inputStream = requireActivity().getContentResolver().openInputStream(uriOriginal);
            String nombreArchivo = "perfil_editado_" + System.currentTimeMillis() + ".jpg";
            File archivoDestino = new File(requireActivity().getFilesDir(), nombreArchivo);
            FileOutputStream outputStream = new FileOutputStream(archivoDestino);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }

            inputStream.close();
            outputStream.close();
            return archivoDestino.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            nuevaImagenUri = data.getData();
            imgPerfil.setImageURI(nuevaImagenUri);
        }
    }
}
