package com.example.viajes.hospedaje;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.viajes.R;

public class HotelDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_detail);

        // Obtener el nombre del hotel, la descripción, la imagen y el lugar desde el intent
        String hotelName = getIntent().getStringExtra("hotelName");
        String descripcion = getIntent().getStringExtra("descripcion");
        String lugar = "Pucallpa";  // Lugar fijo, podrías cambiarlo según el hotel
        int imageResourceId = getIntent().getIntExtra("imageResourceId", R.drawable.hotel1);
        String precio = getIntent().getStringExtra("precio");

        // Mostrar los datos en los TextView e ImageView correspondientes
        TextView hotelNameTextView = findViewById(R.id.hotelNameTextView);
        TextView descripcionTextView = findViewById(R.id.descripcionTextView);
        ImageView hotelImageView = findViewById(R.id.hotelImageView);
        TextView precioTextView = findViewById(R.id.precioTextView);

        hotelNameTextView.setText(hotelName);
        descripcionTextView.setText(descripcion);
        hotelImageView.setImageResource(imageResourceId);
        precioTextView.setText(precio);

        // Botón para elegir el hotel
        Button elegirButton = findViewById(R.id.elegirButton);
        elegirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad para generar el ticket
                Intent intent = new Intent(HotelDetailActivity.this, TicketActivity.class);
                intent.putExtra("hotelName", hotelName);
                intent.putExtra("lugar", lugar);
                startActivity(intent);
            }
        });
    }
}
