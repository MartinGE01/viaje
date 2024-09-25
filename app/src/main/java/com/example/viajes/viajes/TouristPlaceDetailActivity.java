package com.example.viajes.viajes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.viajes.R;
import com.squareup.picasso.Picasso;

public class TouristPlaceDetailActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "login_prefs";
    private static final String KEY_EMAIL = "user_email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_place_detail);

        // Referencias a los elementos del layout
        ImageView placeImageDetail = findViewById(R.id.placeImageDetail);
        TextView placeNameDetail = findViewById(R.id.placeNameDetail);
        TextView placeLocationDetail = findViewById(R.id.placeLocationDetail);
        TextView placeDescription = findViewById(R.id.placeDescription);
        TextView placePrice = findViewById(R.id.placePrice);
        Button chooseButton = findViewById(R.id.chooseButton);

        // Obtener los detalles del intent
        String imageUrl = getIntent().getStringExtra("image_url");
        String name = getIntent().getStringExtra("name");
        String location = getIntent().getStringExtra("location");
        String description = getIntent().getStringExtra("description");
        double price = getIntent().getDoubleExtra("price", 0.0);
        
        placeNameDetail.setText(name);
        placeLocationDetail.setText(location);
        placeDescription.setText(description);
        placePrice.setText(String.format("s/ %.2f", price));  // Formatear el precio como double

        Picasso.get().load(imageUrl).into(placeImageDetail);

        // Inicializar SharedPreferences
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        // Acción del botón Elegir
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (isUserLoggedIn()) {
                    // Si el usuario está logueado, ir a GenerateTicketActivity
                    intent = new Intent(TouristPlaceDetailActivity.this, GenerateTicketActivity.class);
                } else {
                    // Si no está logueado, ir a GenerateTripActivity
                    intent = new Intent(TouristPlaceDetailActivity.this, GenerateTripActivity.class);
                }

                // Pasar los detalles del lugar y el precio como double
                intent.putExtra("image_url", imageUrl);
                intent.putExtra("name", name);
                intent.putExtra("location", location);
                intent.putExtra("description", description);
                intent.putExtra("price", price);
                startActivity(intent);
            }
        });
    }

    // Método para verificar si el usuario ha iniciado sesión
    private boolean isUserLoggedIn() {
        String email = sharedPreferences.getString(KEY_EMAIL, null);
        return email != null;
    }
}
