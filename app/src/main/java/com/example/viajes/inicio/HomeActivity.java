package com.example.viajes.inicio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.viajes.R;
import com.example.viajes.hospedaje.HotelActivity;  // Import your HotelActivity class
import com.example.viajes.loginregister.LoginActivity;
import com.example.viajes.restaurante.MainRestaurante;
import com.example.viajes.restaurante.MostrarDireccionActivity;
import com.example.viajes.viajes.TouristPlacesActivity;


public class HomeActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String SHARED_PREF_NAME = "login_prefs";
    private static final String KEY_EMAIL = "user_email";
    private static final String KEY_USERNAME = "user_name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);

        // Get username from SharedPreferences (if it exists)
        String username = sharedPreferences.getString(KEY_USERNAME, "Usuario");

        // Initialize UI elements
        TextView welcomeText = findViewById(R.id.welcomeText);
        welcomeText.setText("Bienvenido " + username);

        EditText searchEditText = findViewById(R.id.searchEditText);
        Button searchButton = findViewById(R.id.searchButton);
        ImageView profileImage = findViewById(R.id.profileImage);
        TextView viewMoreRecommendations = findViewById(R.id.viewMoreRecommendations);
        TextView viewMoreHotels = findViewById(R.id.viewMoreHotels);
        TextView viewMoreRestaurants = findViewById(R.id.viewMoreRestaurants);// Add reference to "Ver más" TextView

        TextView butviajar = findViewById(R.id.butviajar);
        TextView buthotel = findViewById(R.id.buthotel);
        ImageButton butrestaurante = findViewById(R.id.butrestaurante);
        LinearLayout belladurmiente = findViewById(R.id.belladurmiente);
        LinearLayout puentecalicanto = findViewById(R.id.puentecalicanto);
        TextView viewMoreDestinations = findViewById(R.id.viewMoreDestinations);

        Button travelButton1 = findViewById(R.id.travelButton1);
        Button reserveButton1 = findViewById(R.id.reserveButton1);
        Button contactButton1 = findViewById(R.id.contactButton1);



        searchButton.setOnClickListener(v -> {
            String searchTerm = searchEditText.getText().toString();
            if (!searchTerm.isEmpty()) {
                Toast.makeText(HomeActivity.this, "Buscando: " + searchTerm, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HomeActivity.this, "Por favor, ingresa un término de búsqueda", Toast.LENGTH_SHORT).show();
            }
        });


        profileImage.setOnClickListener(v -> {
            if (sharedPreferences.contains(KEY_EMAIL)) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        viewMoreRecommendations.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, TouristPlacesActivity.class);
            startActivity(intent);
        });


        viewMoreHotels.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, HotelActivity.class);  // Navigate to HotelActivity
            startActivity(intent);
        });


        viewMoreRestaurants.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainRestaurante.class);  // Navigate to HotelActivity
            startActivity(intent);
        });

        butviajar.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, TouristPlacesActivity.class);  // Navigate to HotelActivity
            startActivity(intent);
        });


        buthotel.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, HotelActivity.class);  // Navigate to HotelActivity
            startActivity(intent);
        });

        butrestaurante.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MainRestaurante.class);  // Navigate to HotelActivity
            startActivity(intent);
        });



        belladurmiente.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, TouristPlacesActivity.class);  // Navigate to HotelActivity
            startActivity(intent);
        });


        puentecalicanto.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, TouristPlacesActivity.class);  // Navigate to HotelActivity
            startActivity(intent);
        });

        viewMoreDestinations.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, TouristPlacesActivity.class);  // Navigate to HotelActivity
            startActivity(intent);
        });

        travelButton1.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, TouristPlacesActivity.class);  // Navigate to HotelActivity
            startActivity(intent);
        });

        reserveButton1.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, HotelActivity.class);  // Navigate to HotelActivity
            startActivity(intent);
        });

        contactButton1.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MostrarDireccionActivity.class);  // Navigate to HotelActivity
            startActivity(intent);
        });
    }

}
