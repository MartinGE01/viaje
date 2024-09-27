package com.example.viajes.hospedaje;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.viajes.R;

public class TicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);


        String hotelName = getIntent().getStringExtra("hotelName");
        String lugar = getIntent().getStringExtra("lugar");
        int imageResourceId = getIntent().getIntExtra("imageResourceId", R.drawable.hotel1); // Default image


        TextView hotelNameTextView = findViewById(R.id.hotelNameTextView);
        TextView lugarTextView = findViewById(R.id.lugarTextView);
        ImageView hotelImageView = findViewById(R.id.hotelImageView);


        hotelNameTextView.setText(hotelName);
        lugarTextView.setText(lugar);
    }
}
