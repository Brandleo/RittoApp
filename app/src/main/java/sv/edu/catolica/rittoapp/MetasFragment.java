package sv.edu.catolica.rittoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class MetasFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView texto = new TextView(getContext());
        texto.setText("Aquí se mostrarán las metas");
        texto.setTextSize(18f);
        texto.setPadding(32, 32, 32, 32);
        return texto;
    }
}
