package sv.edu.catolica.rittoapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class DenominacionAdapter extends RecyclerView.Adapter<DenominacionAdapter.ViewHolder> {

    private final List<Denominacion> lista;

    public DenominacionAdapter(List<Denominacion> lista) {
        this.lista = lista;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgDenominacion;
        TextView txtValor, txtCantidad;
        Button btnSumar, btnRestar;

        public ViewHolder(View itemView) {
            super(itemView);
            imgDenominacion = itemView.findViewById(R.id.imgDenominacion);
            txtValor = itemView.findViewById(R.id.txtValorDenominacion);
            txtCantidad = itemView.findViewById(R.id.txtCantidad);
            btnSumar = itemView.findViewById(R.id.btnSumar);
            btnRestar = itemView.findViewById(R.id.btnRestar);
        }
    }

    @NonNull
    @Override
    public DenominacionAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_denominacion, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull DenominacionAdapter.ViewHolder holder, int position) {
        Denominacion d = lista.get(position);

        holder.imgDenominacion.setImageResource(d.getImagenResId());
        holder.txtValor.setText(String.format(Locale.US, "$%.2f", d.getValor()));
        holder.txtCantidad.setText(String.valueOf(d.getCantidad()));

        holder.btnSumar.setOnClickListener(v -> {
            d.setCantidad(d.getCantidad() + 1);
            holder.txtCantidad.setText(String.valueOf(d.getCantidad()));
        });

        holder.btnRestar.setOnClickListener(v -> {
            if (d.getCantidad() > 0) {
                d.setCantidad(d.getCantidad() - 1);
                holder.txtCantidad.setText(String.valueOf(d.getCantidad()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public double calcularTotal() {
        double total = 0;
        for (Denominacion d : lista) {
            total += d.getValor() * d.getCantidad();
        }
        return total;
    }

    public List<Denominacion> getLista() {
        return lista;
    }
}
