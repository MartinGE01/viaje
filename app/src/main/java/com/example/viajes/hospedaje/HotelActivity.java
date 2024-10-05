package com.example.viajes.hospedaje;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.viajes.R;

public class HotelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotels);


        Button reservarButton1 = findViewById(R.id.reservarButton1);
        reservarButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDetalleHotel("Águila Dorada Selva Hotel", R.drawable.hotel1, "s/. 90.00");
            }
        });

       
        Button reservarButton2 = findViewById(R.id.reservarButton2);
        reservarButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDetalleHotel("Los Gavilanes Hotel", R.drawable.hotel2, "s/. 120.00");
            }
        });

        // Configurar el botón Reservar para el tercer hotel
        Button reservarButton3 = findViewById(R.id.reservarButton3);
        reservarButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirDetalleHotel("Hotel Sol de Belén ", R.drawable.hotel3, "s/. 170.00");
            }
        });
    }

    private void abrirDetalleHotel(String hotelName, int imageResourceId, String precio) {
        String[] descripciones = {
                "Este hotel ofrece una experiencia única con vistas increíbles.",
                "Disfruta de la naturaleza y la tranquilidad en este acogedor hotel.",
                "Un lugar perfecto para relajarse y desconectar del mundo.",
                "Con modernas instalaciones, este hotel te garantiza una estancia inolvidable."
        };
        int randomIndex = (int) (Math.random() * descripciones.length);
        String descripcionAleatoria = descripciones[randomIndex];

        Intent intent = new Intent(HotelActivity.this, HotelDetailActivity.class);
        intent.putExtra("hotelName", hotelName);
        intent.putExtra("descripcion", descripcionAleatoria);
        intent.putExtra("imageResourceId", imageResourceId);
        intent.putExtra("precio", precio);  // Enviar el precio
        startActivity(intent);
    }
}
