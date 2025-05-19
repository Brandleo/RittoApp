package sv.edu.catolica.rittoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlcanciaAdapter extends RecyclerView.Adapter<AlcanciaAdapter.ViewHolder> {

    private final List<Alcancia> lista;

    public AlcanciaAdapter(List<Alcancia> lista) {
        this.lista = lista;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtNombre, txtCantidad;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNombre = itemView.findViewById(R.id.txtNombre);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alcancia, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Alcancia alc = lista.get(position);
        holder.txtNombre.setText(alc.getNombre());
        holder.txtCantidad.setText("$" + alc.getCantidad());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
