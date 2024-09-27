package com.example.viajes.inicio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.viajes.R;
import com.example.viajes.hospedaje.HotelActivity;  // Import your HotelActivity class
import com.example.viajes.loginregister.LoginActivity;
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
        TextView viewMoreHotels = findViewById(R.id.viewMoreHotels);  // Add reference to "Ver más" TextView

        // Search Button action
        searchButton.setOnClickListener(v -> {
            String searchTerm = searchEditText.getText().toString();
            if (!searchTerm.isEmpty()) {
                Toast.makeText(HomeActivity.this, "Buscando: " + searchTerm, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(HomeActivity.this, "Por favor, ingresa un término de búsqueda", Toast.LENGTH_SHORT).show();
            }
        });

        // Profile Image click action
        profileImage.setOnClickListener(v -> {
            if (sharedPreferences.contains(KEY_EMAIL)) {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // View More Recommendations click action
        viewMoreRecommendations.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, TouristPlacesActivity.class);
            startActivity(intent);
        });

        // View More Hotels click action
        viewMoreHotels.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, HotelActivity.class);  // Navigate to HotelActivity
            startActivity(intent);
        });
    }
}
