package sv.edu.catolica.rittoapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AlcanciaAdapter extends RecyclerView.Adapter<AlcanciaAdapter.ViewHolder> {

    private final List<Alcancia> lista;

    public AlcanciaAdapter(List<Alcancia> lista) {
        this.lista = lista;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcono;
        TextView txtNombre;
        TextView txtCantidad;

        public ViewHolder(View itemView) {
            super(itemView);
            imgIcono = itemView.findViewById(R.id.imgIconoAlcancia);
            txtNombre = itemView.findViewById(R.id.txtNombreAlcancia);
            txtCantidad = itemView.findViewById(R.id.txtCantidadAlcancia);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alcancia, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Alcancia alcancia = lista.get(position);
        holder.txtNombre.setText(alcancia.getNombre());
        holder.txtCantidad.setText(String.format("$%.2f", alcancia.getCantidad()));

        // Obtener el recurso desde el nombre del icono guardado
        Context context = holder.itemView.getContext();
        int resId = context.getResources().getIdentifier(alcancia.getIcono(), "drawable", context.getPackageName());
        if (resId != 0) {
            holder.imgIcono.setImageResource(resId);
        } else {
            holder.imgIcono.setImageResource(R.drawable.tunco);
        }

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), DetalleAlcanciaActivity.class);
            intent.putExtra("id", alcancia.getId()); // 🔄 Añadido ID
            intent.putExtra("nombre", alcancia.getNombre());
            intent.putExtra("cantidad", alcancia.getCantidad());
            intent.putExtra("icono", alcancia.getIcono());
            intent.putExtra("sellada", alcancia.isSellada());
            v.getContext().startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }
}
