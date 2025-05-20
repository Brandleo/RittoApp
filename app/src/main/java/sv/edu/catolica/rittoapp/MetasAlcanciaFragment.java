package sv.edu.catolica.rittoapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MetasAlcanciaFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        TextView text = new TextView(getContext());
        text.setText("Fragmento METAS");
        text.setTextSize(18);
        text.setPadding(16, 16, 16, 16);
        return text;
    }
}
