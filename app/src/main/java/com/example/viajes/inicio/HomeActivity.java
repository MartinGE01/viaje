package com.example.viajes.inicio;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.viajes.R;

import com.example.viajes.loginregister.LoginActivity;


public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Obtener referencias de los elementos de la UI
        EditText searchEditText = findViewById(R.id.searchEditText);
        Button searchButton = findViewById(R.id.searchButton);
        ImageView profileImage = findViewById(R.id.profileImage);
        TextView viewMoreRecommendations = findViewById(R.id.viewMoreRecommendations); // Referencia al TextView

        // Configurar el botón de búsqueda
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchTerm = searchEditText.getText().toString();
                // Implementar la lógica para buscar en la lista
            }
        });

        // Configurar el clic en la imagen de perfil
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a la actividad LoginActivity
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        // Configurar el clic en "Ver más" para ir a TouristPlacesActivity
        viewMoreRecommendations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a la actividad TouristPlacesActivity
                Intent intent = new Intent(HomeActivity.this, com.example.viajes.viajes.TouristPlacesActivity.class);
                startActivity(intent);
            }
        });
    }
}
