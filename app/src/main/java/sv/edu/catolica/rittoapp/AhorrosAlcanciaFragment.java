package sv.edu.catolica.rittoapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AhorrosAlcanciaFragment extends Fragment {

    private int idAlcancia;
    private String nombre;
    private String icono;
    private boolean sellada;
    private TextView txtTotal;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_ahorros_alcancia, container, false);

        TextView txtNombre = vista.findViewById(R.id.txtNombreAhorro);
        txtTotal = vista.findViewById(R.id.txtTotalAhorro);
        ImageView imgIcono = vista.findViewById(R.id.imgIconoAhorro);
        ImageButton btnDepositar = vista.findViewById(R.id.btnDepositarDinero);

        Bundle args = getArguments();
        if (args != null) {
            idAlcancia = args.getInt("id", -1);
            nombre = args.getString("nombre");
            icono = args.getString("icono");
            sellada = args.getBoolean("sellada", false);

            AdminDB db = new AdminDB(requireContext());
            double cantidad = db.obtenerCantidadActual(idAlcancia);

            txtNombre.setText(nombre);
            txtTotal.setText(String.format("Total: $%.2f", cantidad));
            int resId = requireContext().getResources().getIdentifier(icono, "drawable", requireContext().getPackageName());
            imgIcono.setImageResource(resId != 0 ? resId : R.drawable.tunco);
        }

        btnDepositar.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), DepositoActivity.class);
            intent.putExtra("id", idAlcancia);
            intent.putExtra("nombre", nombre);
            intent.putExtra("icono", icono);
            intent.putExtra("sellada", sellada);
            startActivity(intent);
        });

        return vista;
    }

    @Override
    public void onResume() {
        super.onResume();
        AdminDB db = new AdminDB(requireContext());
        double actual = db.obtenerCantidadActual(idAlcancia);
        txtTotal.setText(String.format("Total: $%.2f", actual));
    }

}
