package com.example.viajes.restaurante;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.viajes.R;

import java.util.ArrayList;
import java.util.List;

public class MainRestaurante extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RestauranteAdapter restauranteAdapter;
    private List<Restaurante> restauranteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_restaurante);


        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        restauranteList = new ArrayList<>();


        restauranteList.add(new Restaurante(R.drawable.parica, "Restaurante Paprika Cajamarca", "Cajamarca", -7.1560, -78.5170));


        restauranteList.add(new Restaurante(R.drawable.rhco, "Rinconcito Huanuqueño", "Huánuco", -9.9306, -76.2422));
        restauranteList.add(new Restaurante(R.drawable.jacaranda, "Centro Campestre Jacarandá", "Huánuco", -9.9306, -76.2422));
        restauranteList.add(new Restaurante(R.drawable.trapiche, "Trapiche House", "Huánuco", -9.9306, -76.2422));
        restauranteList.add(new Restaurante(R.drawable.pizza, "Piqueso Bistro Regional", "Huánuco", -9.9306, -76.2422));
        restauranteList.add(new Restaurante(R.drawable.ta, "La Piazzetta", "Huánuco", -9.9306, -76.2422));


        restauranteList.add(new Restaurante(R.drawable.tirol, "Tirol Haus Bier", "Pucallpa", -8.3791, -74.5539));
        restauranteList.add(new Restaurante(R.drawable.tirol2, "Tirol Bier & Snack", "Pucallpa", -8.3791, -74.5539));


        restauranteAdapter = new RestauranteAdapter(restauranteList, this);
        recyclerView.setAdapter(restauranteAdapter);
    }
}
