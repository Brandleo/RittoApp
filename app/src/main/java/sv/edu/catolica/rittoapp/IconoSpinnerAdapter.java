package sv.edu.catolica.rittoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class IconoSpinnerAdapter extends ArrayAdapter<IconoItem> {

    private final Context context;
    private final List<IconoItem> lista;

    public IconoSpinnerAdapter(Context context, List<IconoItem> lista) {
        super(context, 0, lista);
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        return crearVista(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
        return crearVista(position, convertView, parent);
    }

    private View crearVista(int position, View convertView, ViewGroup parent) {
        View vista = LayoutInflater.from(context).inflate(R.layout.item_icono_spinner, parent, false);

        IconoItem item = lista.get(position);

        ImageView img = vista.findViewById(R.id.imgIcono);
        TextView texto = vista.findViewById(R.id.textoIcono);

        img.setImageResource(item.getRecursoDrawable());
        texto.setText(item.getNombre());

        return vista;
    }
}
