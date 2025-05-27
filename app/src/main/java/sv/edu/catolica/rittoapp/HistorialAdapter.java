package sv.edu.catolica.rittoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.ViewHolder> {

    public interface OnDesgloseClickListener {
        void onDesgloseClick(Movimiento movimiento);
    }

    private List<Movimiento> lista;
    private OnDesgloseClickListener listener;

    public HistorialAdapter(List<Movimiento> lista, OnDesgloseClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtFecha, txtTipo, txtMonto;
        ImageButton btnVerDesglose;

        public ViewHolder(View itemView) {
            super(itemView);
            txtFecha = itemView.findViewById(R.id.txtFechaMovimiento);
            txtTipo = itemView.findViewById(R.id.txtTipoMovimiento);
            txtMonto = itemView.findViewById(R.id.txtMontoMovimiento);
            btnVerDesglose = itemView.findViewById(R.id.btnVerDesglose);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movimiento, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Movimiento m = lista.get(position);
        holder.txtFecha.setText(m.getFecha());
        holder.txtTipo.setText(m.getTipo().substring(0, 1).toUpperCase() + m.getTipo().substring(1));
        holder.txtMonto.setText(String.format("$%.2f", m.getMontoTotal()));


        holder.btnVerDesglose.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDesgloseClick(m);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void actualizarLista(List<Movimiento> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }
}
