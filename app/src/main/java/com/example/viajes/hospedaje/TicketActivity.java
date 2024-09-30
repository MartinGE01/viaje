package com.example.viajes.hospedaje;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.viajes.R;
import com.example.viajes.loginregister.RegisterActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class TicketActivity extends AppCompatActivity {

    private EditText dniEditText;
    private Spinner numeroHabitacionSpinner;
    private Spinner adultosSpinner;
    private Spinner ninosSpinner;
    private RadioGroup tipoViajeRadioGroup;
    private RadioButton viajeNegocios;
    private RadioButton viajeOtro;
    private Button generarTicketButton;
    private ImageView qrImage;

    // Referencia a Firebase Realtime Database
    private DatabaseReference databaseReference;

    // Variables para el hotel y el lugar
    private String hotelName;
    private String lugar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        // Recibir el nombre del hotel y el lugar desde el intent
        hotelName = getIntent().getStringExtra("hotelName");
        lugar = getIntent().getStringExtra("lugar");

        // Referencias a los componentes del layout
        dniEditText = findViewById(R.id.dniEditText);
        numeroHabitacionSpinner = findViewById(R.id.numeroHabitacionSpinner);
        adultosSpinner = findViewById(R.id.adultosSpinner);
        ninosSpinner = findViewById(R.id.ninosSpinner);
        tipoViajeRadioGroup = findViewById(R.id.tipoViajeRadioGroup);
        viajeNegocios = findViewById(R.id.viajeNegocios);
        viajeOtro = findViewById(R.id.viajeOtro);
        generarTicketButton = findViewById(R.id.generarTicketButton);
        qrImage = findViewById(R.id.qrImage);

        // Configurar Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        // Configuración de los spinners con los recursos definidos en strings.xml
        ArrayAdapter<CharSequence> habitacionAdapter = ArrayAdapter.createFromResource(this,
                R.array.habitacion_options, android.R.layout.simple_spinner_item);
        habitacionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        numeroHabitacionSpinner.setAdapter(habitacionAdapter);

        ArrayAdapter<CharSequence> adultosAdapter = ArrayAdapter.createFromResource(this,
                R.array.adultos_options, android.R.layout.simple_spinner_item);
        adultosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adultosSpinner.setAdapter(adultosAdapter);

        ArrayAdapter<CharSequence> ninosAdapter = ArrayAdapter.createFromResource(this,
                R.array.ninos_options, android.R.layout.simple_spinner_item);
        ninosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ninosSpinner.setAdapter(ninosAdapter);

        // Acción del botón para generar ticket o redirigir a RegisterActivity
        generarTicketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dni = dniEditText.getText().toString().trim();

                // Verificar si el DNI está vacío
                if (dni.isEmpty()) {
                    Toast.makeText(TicketActivity.this, "Por favor, ingrese el DNI", Toast.LENGTH_SHORT).show();
                }
                // Validar si el DNI tiene exactamente 8 dígitos
                else if (dni.length() != 8) {
                    Toast.makeText(TicketActivity.this, "El DNI debe tener 8 dígitos", Toast.LENGTH_SHORT).show();
                }
                else {
                    // Validar si el DNI está registrado en Firebase
                    validarDNI(dni);
                }
            }
        });
    }

    private void validarDNI(String dni) {
        databaseReference.orderByChild("dni").equalTo(dni).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // El DNI existe en la base de datos, generar ticket
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String username = snapshot.child("username").getValue(String.class);
                        String phone = snapshot.child("phone").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);

                        generarPDF(username, phone, dni, email);
                    }
                } else {
                    // El DNI no está registrado, redirigir a RegisterActivity
                    Toast.makeText(TicketActivity.this, "DNI no registrado, redirigiendo a registro...", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TicketActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Firebase", "Error al leer los datos de Firebase", databaseError.toException());
            }
        });
    }

    // Método para generar el PDF con los datos del usuario y hotel
    private void generarPDF(String nombre, String telefono, String dni, String correo) {
        // Obtener los datos del hotel que el usuario ingresó
        String habitacion = numeroHabitacionSpinner.getSelectedItem().toString();
        String adultos = adultosSpinner.getSelectedItem().toString();
        String ninos = ninosSpinner.getSelectedItem().toString();

        int selectedViajeId = tipoViajeRadioGroup.getCheckedRadioButtonId();
        RadioButton selectedViajeButton = findViewById(selectedViajeId);
        String tipoViaje = selectedViajeButton != null ? selectedViajeButton.getText().toString() : "No especificado";

        // Generar el código QR
        Bitmap qrCodeBitmap = generarQRCode(dni);

        // Crear un nuevo documento PDF
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint titlePaint = new Paint();

        // Crear una página en blanco para el documento
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(595, 842, 1).create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        // Configuración del estilo del título
        titlePaint.setTextSize(18);
        titlePaint.setColor(Color.BLACK);
        titlePaint.setTextAlign(Paint.Align.CENTER);

        // Añadir el título y contenido al PDF
        canvas.drawText("Ticket de Reserva", 297, 50, titlePaint);

        paint.setTextSize(14);
        paint.setColor(Color.BLACK);
        canvas.drawText("Nombre: " + nombre, 50, 100, paint);
        canvas.drawText("Teléfono: " + telefono, 50, 130, paint);
        canvas.drawText("DNI: " + dni, 50, 160, paint);
        canvas.drawText("Correo: " + correo, 50, 190, paint);
        canvas.drawText("Hotel: " + hotelName, 50, 220, paint);  // Nombre del hotel
        canvas.drawText("Lugar: " + lugar, 50, 250, paint);      // Lugar del hotel
        canvas.drawText("Habitación: " + habitacion, 50, 280, paint);
        canvas.drawText("Adultos: " + adultos, 50, 310, paint);
        canvas.drawText("Niños: " + ninos, 50, 340, paint);
        canvas.drawText("Tipo de viaje: " + tipoViaje, 50, 370, paint);
        canvas.drawText("Estado: Falta pagar", 50, 400, paint);

        // Insertar el código QR en el PDF
        if (qrCodeBitmap != null) {
            canvas.drawBitmap(qrCodeBitmap, 400, 100, paint); // Ajustar las coordenadas según lo necesites
        }

        // Finalizar la página y cerrar el documento
        pdfDocument.finishPage(page);

        // Guardar el PDF en el almacenamiento privado de la aplicación
        File directory = getExternalFilesDir(null);
        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }

        String fileName = "Ticket_Reserva_" + dni + ".pdf";
        File pdfFile = new File(directory, fileName);

        try {
            pdfDocument.writeTo(new FileOutputStream(pdfFile));
            Toast.makeText(this, "PDF generado: " + pdfFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Error al generar el PDF", Toast.LENGTH_SHORT).show();
        }

        // Cerrar el documento
        pdfDocument.close();
    }

    // Método para generar el QR Code y devolverlo como Bitmap
    private Bitmap generarQRCode(String dni) {
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            return barcodeEncoder.encodeBitmap(dni, BarcodeFormat.QR_CODE, 200, 200); // Ajusta el tamaño del QR si es necesario
        } catch (WriterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
