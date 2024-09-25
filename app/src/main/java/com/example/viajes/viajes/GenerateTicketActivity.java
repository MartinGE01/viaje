package com.example.viajes.viajes;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.viajes.R;

import java.util.Calendar;

public class GenerateTicketActivity extends AppCompatActivity {

    private EditText dateEditText, totalEditText, quantityEditText;
    private double placePrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_ticket);

        EditText originEditText = findViewById(R.id.originEditText);
        EditText destinationEditText = findViewById(R.id.destinationEditText);
        dateEditText = findViewById(R.id.dateEditText);
        quantityEditText = findViewById(R.id.quantityEditText);
        CheckBox policyCheckBox = findViewById(R.id.policyCheckBox);
        Button continueButton = findViewById(R.id.continueButton);
        totalEditText = findViewById(R.id.totalEditText);

        // Obtener los datos pasados desde TouristPlaceDetailActivity
        String placeName = getIntent().getStringExtra("name");
        String placeLocation = getIntent().getStringExtra("location");
        placePrice = getIntent().getDoubleExtra("price", 0.0);


        originEditText.setText(placeName);
        destinationEditText.setText(placeLocation);


        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });


        quantityEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) { // Cuando el usuario deja de enfocar el campo de cantidad
                    calcularTotal();
                }
            }
        });

        quantityEditText.setOnKeyListener((v, keyCode, event) -> {
            calcularTotal();
            return false;
        });


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedDate = dateEditText.getText().toString().trim();
                String quantity = quantityEditText.getText().toString().trim();

                if (TextUtils.isEmpty(selectedDate) || TextUtils.isEmpty(quantity)) {
                    Toast.makeText(GenerateTicketActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else if (!policyCheckBox.isChecked()) {
                    Toast.makeText(GenerateTicketActivity.this, "Debe aceptar las políticas para continuar", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GenerateTicketActivity.this, "Ticket generado exitosamente", Toast.LENGTH_SHORT).show();
                    // Aquí puedes añadir lógica para almacenar o procesar el ticket generado
                }
            }
        });
    }


    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                GenerateTicketActivity.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // Al seleccionar la fecha, formatearla y mostrarla en el EditText
                        dateEditText.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                    }
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    // Método para calcular el total a pagar
    private void calcularTotal() {
        String quantityString = quantityEditText.getText().toString().trim();
        if (!TextUtils.isEmpty(quantityString)) {
            try {
                int quantity = Integer.parseInt(quantityString);
                double total = quantity * placePrice;
                totalEditText.setText(String.format("%.2f", total));
            } catch (NumberFormatException e) {
                Toast.makeText(GenerateTicketActivity.this, "Cantidad inválida", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
