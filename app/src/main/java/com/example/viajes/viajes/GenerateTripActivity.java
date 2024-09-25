package com.example.viajes.viajes;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.viajes.R;
import com.example.viajes.loginregister.LoginActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class GenerateTripActivity extends AppCompatActivity {

    private DatabaseReference usersRef;
    private String placeName, placeImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_trip);

        // Obtener las referencias de los elementos de la UI
        ImageView placeImageView = findViewById(R.id.placeImageView);
        TextView placeNameTextView = findViewById(R.id.placeNameTextView);
        EditText emailEditText = findViewById(R.id.emailEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button continueButton = findViewById(R.id.continueButton);
        TextView registerTextView = findViewById(R.id.registerTextView);

        // Obtener datos del intent (asegurarse de que los datos se obtengan correctamente)
        Intent intent = getIntent();
        placeName = intent.getStringExtra("name");
        placeImageUrl = intent.getStringExtra("image_url");

        // Mostrar el nombre del lugar y cargar la imagen con Picasso
        if (placeName != null && placeImageUrl != null) {
            placeNameTextView.setText(placeName);
            Picasso.get().load(placeImageUrl).into(placeImageView);
        } else {
            Toast.makeText(this, "Error al cargar los detalles del lugar", Toast.LENGTH_SHORT).show();
        }

        // Firebase referencia de los usuarios
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        // Acción del botón de continuar (para autenticación)
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(GenerateTripActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    authenticateUser(email, password);
                }
            }
        });

        // Acción para redirigir al registro
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GenerateTripActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void authenticateUser(String email, String password) {
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String dbPassword = userSnapshot.child("password").getValue(String.class);

                        if (dbPassword != null && dbPassword.equals(password)) {
                            Toast.makeText(GenerateTripActivity.this, "Autenticación exitosa", Toast.LENGTH_SHORT).show();

                            // Redirigir a GenerateTicketActivity si la autenticación es correcta
                            Intent intent = new Intent(GenerateTripActivity.this, GenerateTicketActivity.class);

                            // Pasar los datos del lugar a GenerateTicketActivity
                            intent.putExtra("name", placeName);
                            intent.putExtra("image_url", placeImageUrl);

                            startActivity(intent);
                            finish();  // Cierra la actividad actual para que el usuario no pueda volver atrás
                        } else {
                            Toast.makeText(GenerateTripActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(GenerateTripActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(GenerateTripActivity.this, "Error al conectar con la base de datos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
