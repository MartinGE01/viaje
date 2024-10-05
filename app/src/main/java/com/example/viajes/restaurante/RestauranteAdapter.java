package com.example.viajes.restaurante;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viajes.R;

import java.util.List;

public class RestauranteAdapter extends RecyclerView.Adapter<RestauranteAdapter.RestauranteViewHolder> {

    private List<Restaurante> restauranteList;
    private Context context;

    public RestauranteAdapter(List<Restaurante> restauranteList, Context context) {
        this.restauranteList = restauranteList;
        this.context = context;
    }

    @NonNull
    @Override
    public RestauranteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_restaurante, parent, false);
        return new RestauranteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestauranteViewHolder holder, int position) {
        Restaurante restaurante = restauranteList.get(position);
        holder.nombreRestaurante.setText(restaurante.getNombre());
        holder.ubicacionRestaurante.setText(restaurante.getCiudad());
        holder.imagenRestaurante.setImageResource(restaurante.getImagen());


        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, MostrarDireccionActivity.class);

            intent.putExtra("latitud", restaurante.getLatitud());
            intent.putExtra("longitud", restaurante.getLongitud());
            intent.putExtra("nombreRestaurante", restaurante.getNombre());
            intent.putExtra("ciudadRestaurante", restaurante.getCiudad());
            intent.putExtra("imagenRestaurante", restaurante.getImagen());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return restauranteList.size();
    }

    public static class RestauranteViewHolder extends RecyclerView.ViewHolder {
        ImageView imagenRestaurante;
        TextView nombreRestaurante, ubicacionRestaurante;

        public RestauranteViewHolder(@NonNull View itemView) {
            super(itemView);
            imagenRestaurante = itemView.findViewById(R.id.img_restaurante);
            nombreRestaurante = itemView.findViewById(R.id.txt_nombre_restaurante);
            ubicacionRestaurante = itemView.findViewById(R.id.txt_ubicacion);
        }
    }
}
