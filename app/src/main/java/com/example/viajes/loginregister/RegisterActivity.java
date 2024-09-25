package com.example.viajes.loginregister;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.viajes.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    private EditText dniEditText, usernameEditText, phoneEditText, emailEditText, passwordEditText;
    private Button registerButton;
    private TextView loginTextView;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar Firebase Realtime Database y establecer referencia directamente en "users"
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("users");

        // Referencias a los elementos de la interfaz
        dniEditText = findViewById(R.id.dniEditText);
        usernameEditText = findViewById(R.id.usernameEditText);
        phoneEditText = findViewById(R.id.phoneEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        registerButton = findViewById(R.id.registerButton);
        loginTextView = findViewById(R.id.loginTextView);

        // Configurar el botón de registro
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener los valores de los campos de texto
                String dni = dniEditText.getText().toString();
                String username = usernameEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                // Validar campos
                if (TextUtils.isEmpty(dni) || TextUtils.isEmpty(username) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Registrar al usuario en Firebase Realtime Database
                    registerUser(dni, username, phone, email, password);
                }
            }
        });

        // Configurar el texto para iniciar sesión
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Volver a la actividad de inicio de sesión (LoginActivity)
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void registerUser(String dni, String username, String phone, String email, String password) {
        // Generar una clave única para cada usuario en Firebase
        String userId = databaseReference.push().getKey();

        // Crear un objeto usuario con los datos proporcionados
        if (userId != null) {
            User user = new User(dni, username, phone, email, password);

            // Almacenar los datos del usuario en la base de datos bajo la clave única
            databaseReference.child(userId).setValue(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Registro exitoso", Toast.LENGTH_SHORT).show();
                    // Volver a la actividad de inicio de sesión (LoginActivity)
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(RegisterActivity.this, "Error en el registro", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // Clase interna para representar un usuario
    public static class User {
        public String dni, username, phone, email, password;

        public User() {
            // Constructor vacío requerido por Firebase
        }

        public User(String dni, String username, String phone, String email, String password) {
            this.dni = dni;
            this.username = username;
            this.phone = phone;
            this.email = email;
            this.password = password;
        }
    }
}
