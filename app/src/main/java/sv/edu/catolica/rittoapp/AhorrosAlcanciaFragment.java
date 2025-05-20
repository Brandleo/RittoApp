package sv.edu.catolica.rittoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AhorrosAlcanciaFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_ahorros_alcancia, container, false);

        TextView txtNombre = vista.findViewById(R.id.txtNombreAhorro);
        TextView txtTotal = vista.findViewById(R.id.txtTotalAhorro);
        ImageView imgIcono = vista.findViewById(R.id.imgIconoAhorro);

        Bundle args = getArguments();
        if (args != null) {
            String nombre = args.getString("nombre");
            double cantidad = args.getDouble("cantidad");
            String icono = args.getString("icono");

            txtNombre.setText(nombre);
            txtTotal.setText(String.format("Total: $%.2f", cantidad));

            int resId = requireContext().getResources().getIdentifier(icono, "drawable", requireContext().getPackageName());
            imgIcono.setImageResource(resId != 0 ? resId : R.drawable.tunco);
        }

        return vista;
    }


}
