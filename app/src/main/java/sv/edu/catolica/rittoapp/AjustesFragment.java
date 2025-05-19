package sv.edu.catolica.rittoapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class AjustesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_ajustes, container, false);

        Button btnCerrarSesion = vista.findViewById(R.id.btnCerrarSesion);

        btnCerrarSesion.setOnClickListener(v -> {
            // 🔄 Borrar sesión
            SharedPreferences prefs = requireContext().getSharedPreferences("perfil_sesion", Context.MODE_PRIVATE);
            prefs.edit().clear().apply();

            // Limpia la variable estática también
            Sesion.perfilActualNombre = null;

            // Volver a SeleccionPerfil
            Intent intent = new Intent(requireContext(), SeleccionPerfil.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Limpia el back stack
            startActivity(intent);
        });

        return vista;
    }
}
