package com.example.viajes.viajes;

import android.app.DatePickerDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.viajes.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class GenerateTicketActivity extends AppCompatActivity {

    private EditText dateEditText, totalEditText, quantityEditText, dniEditText;
    private double placePrice;
    private Button generateTicketButton;
    private DatabaseReference usersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate_ticket);

        EditText originEditText = findViewById(R.id.originEditText);
        EditText destinationEditText = findViewById(R.id.destinationEditText);
        dateEditText = findViewById(R.id.dateEditText);
        quantityEditText = findViewById(R.id.quantityEditText);
        dniEditText = findViewById(R.id.dniEditText);
        CheckBox policyCheckBox = findViewById(R.id.policyCheckBox);
        generateTicketButton = findViewById(R.id.generartiket);
        totalEditText = findViewById(R.id.totalEditText);


        String placeName = getIntent().getStringExtra("name");
        String placeLocation = getIntent().getStringExtra("location");
        placePrice = getIntent().getDoubleExtra("price", 0.0);


        originEditText.setText(placeName);
        destinationEditText.setText(placeLocation);

        // Inicializar Firebase
        usersRef = FirebaseDatabase.getInstance().getReference("users");


        dateEditText.setOnClickListener(v -> showDatePickerDialog());


        quantityEditText.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                calcularTotal();
            }
        });

        quantityEditText.setOnKeyListener((v, keyCode, event) -> {
            calcularTotal();
            return false;
        });


        generateTicketButton.setOnClickListener(v -> {
            String selectedDate = dateEditText.getText().toString().trim();
            String quantity = quantityEditText.getText().toString().trim();
            String dni = dniEditText.getText().toString().trim();

            if (TextUtils.isEmpty(dni) || TextUtils.isEmpty(selectedDate) || TextUtils.isEmpty(quantity)) {
                Toast.makeText(GenerateTicketActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            } else if (!policyCheckBox.isChecked()) {
                Toast.makeText(GenerateTicketActivity.this, "Debe aceptar las políticas para continuar", Toast.LENGTH_SHORT).show();
            } else {
                // Verificar si el DNI existe en la base de datos de Firebase
                verifyDniInDatabase(dni, placeName, placeLocation, selectedDate, quantity);
            }
        });
    }


    private void verifyDniInDatabase(String dni, String placeName, String placeLocation, String selectedDate, String quantity) {
        usersRef.orderByChild("dni").equalTo(dni).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // El DNI existe en la base de datos, generar el ticket
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                        String username = userSnapshot.child("username").getValue(String.class);
                        String email = userSnapshot.child("email").getValue(String.class);
                        String phone = userSnapshot.child("phone").getValue(String.class);

                        try {
                            int quantityValue = Integer.parseInt(quantity);
                            // Generar el QR con la información del ticket
                            Bitmap qrBitmap = generateQrCode(username, selectedDate, quantityValue);
                            // Crear el PDF con los datos del ticket y el QR, ahora con lugar y destino
                            createPdfWithQr(username, dni, email, phone, placeName, placeLocation, selectedDate, quantityValue, qrBitmap);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(GenerateTicketActivity.this, "Error al generar el ticket", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    // El DNI no existe en la base de datos
                    Toast.makeText(GenerateTicketActivity.this, "DNI no encontrado. Por favor, regístrese o verifique su DNI.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(GenerateTicketActivity.this, "Error al conectar con la base de datos", Toast.LENGTH_SHORT).show();
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
                (view, selectedYear, selectedMonth, selectedDay) -> dateEditText.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear),
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
                totalEditText.setText(String.format("%.2f", total)); // Mostrar el total formateado
            } catch (NumberFormatException e) {
                Toast.makeText(GenerateTicketActivity.this, "Cantidad inválida", Toast.LENGTH_SHORT).show();
            }
        }
    }


    private Bitmap generateQrCode(String username, String date, int quantity) throws WriterException {
        String qrData = "Usuario: " + username + "\nFecha: " + date + "\nCantidad: " + quantity;
        MultiFormatWriter writer = new MultiFormatWriter();
        BitMatrix bitMatrix = writer.encode(qrData, BarcodeFormat.QR_CODE, 400, 400);
        Bitmap bitmap = Bitmap.createBitmap(400, 400, Bitmap.Config.RGB_565);

        for (int x = 0; x < 400; x++) {
            for (int y = 0; y < 400; y++) {
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? android.graphics.Color.BLACK : android.graphics.Color.WHITE);
            }
        }
        return bitmap;
    }


    private void createPdfWithQr(String username, String dni, String email, String phone, String placeName, String placeLocation, String date, int quantity, Bitmap qrBitmap) throws IOException {
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/ticket_viaje.pdf";
        File pdfFile = new File(pdfPath);
        PdfWriter writer = new PdfWriter(new FileOutputStream(pdfFile));
        com.itextpdf.kernel.pdf.PdfDocument pdfDoc = new com.itextpdf.kernel.pdf.PdfDocument(writer);
        Document document = new Document(pdfDoc);

        // Agregar los datos del ticket al PDF
        document.add(new Paragraph("Ticket de Viaje"));
        document.add(new Paragraph("Usuario: " + username));
        document.add(new Paragraph("DNI: " + dni));
        document.add(new Paragraph("Email: " + email));
        document.add(new Paragraph("Teléfono: " + phone));
        document.add(new Paragraph("Lugar de origen: " + placeName));
        document.add(new Paragraph("Destino: " + placeLocation));
        document.add(new Paragraph("Fecha: " + date));
        document.add(new Paragraph("Cantidad: " + quantity));
        document.add(new Paragraph("Total: " + totalEditText.getText().toString()));
        document.add(new Paragraph("Pago:  Pendiente" ));

        // Convertir el código QR a imagen y agregarlo al PDF
        File qrFile = new File(getCacheDir(), "qr_image.png");
        FileOutputStream outputStream = new FileOutputStream(qrFile);
        qrBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        outputStream.flush();
        outputStream.close();

        Image qrImage = new Image(ImageDataFactory.create(qrFile.getAbsolutePath()));
        document.add(qrImage);

        // Cerrar el documento PDF
        document.close();
        Toast.makeText(this, "Ticket generado y guardado en: " + pdfPath, Toast.LENGTH_LONG).show();
    }
}
