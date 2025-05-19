package sv.edu.catolica.rittoapp;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class PerfilAdapter extends RecyclerView.Adapter<PerfilAdapter.ViewHolder> {
    private final List<Perfil> perfiles;

    public PerfilAdapter(List<Perfil> perfiles) {
        this.perfiles = perfiles;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPerfil;
        TextView textoPerfil;

        public ViewHolder(View itemView) {
            super(itemView);
            imgPerfil = itemView.findViewById(R.id.imgPerfil);
            textoPerfil = itemView.findViewById(R.id.textoPerfil);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_perfil, parent, false);
        return new ViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Perfil perfil = perfiles.get(position);

        // Mostrar nombre
        holder.textoPerfil.setText(perfil.getNombre());

        // Mostrar imagen con protección
        try {
            if (perfil.getImagenUri() != null && !perfil.getImagenUri().isEmpty()) {
                File archivo = new File(perfil.getImagenUri());
                if (archivo.exists()) {
                    holder.imgPerfil.setImageURI(Uri.fromFile(archivo));
                } else {
                    holder.imgPerfil.setImageResource(R.drawable.ic_launcher_foreground);
                }
            } else {
                holder.imgPerfil.setImageResource(R.drawable.ic_launcher_foreground);
            }
        } catch (Exception e) {
            holder.imgPerfil.setImageResource(R.drawable.ic_launcher_foreground);
            Log.e("PerfilAdapter", "Error cargando imagen", e);
        }

        // Al hacer clic en el perfil, abrir IngresoPin
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(v.getContext(), IngresoPin.class);
            intent.putExtra("nombre", perfil.getNombre());
            intent.putExtra("imagenUri", perfil.getImagenUri());
            intent.putExtra("pin", perfil.getPin());
            v.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return perfiles.size();
    }
}
