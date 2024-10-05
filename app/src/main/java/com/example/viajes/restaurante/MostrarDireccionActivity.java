package com.example.viajes.restaurante;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.viajes.R;

public class MostrarDireccionActivity extends AppCompatActivity {

    private double latitud;
    private double longitud;
    private String mapTilerApiKey = "MmxG5Z1JwVwi4BoK7wn8"; // Tu clave API de MapTiler
    private WebView mapaWebView;
    private TextView infoRestauranteTextView;
    private ImageView imagenRestauranteView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_direccion);

        // Inicializar WebView, TextView e ImageView
        mapaWebView = findViewById(R.id.mapaWebView);
        infoRestauranteTextView = findViewById(R.id.infoRestauranteTextView);
        imagenRestauranteView = findViewById(R.id.imagenRestauranteView);

        mapaWebView.getSettings().setJavaScriptEnabled(true);
        mapaWebView.setWebViewClient(new WebViewClient());

        // Obtener los datos del restaurante desde el Intent
        String nombreRestaurante = getIntent().getStringExtra("nombreRestaurante");
        String ciudadRestaurante = getIntent().getStringExtra("ciudadRestaurante");
        latitud = getIntent().getDoubleExtra("latitud", -9.9306);
        longitud = getIntent().getDoubleExtra("longitud", -76.2422);
        int imagenRestaurante = getIntent().getIntExtra("imagenRestaurante", R.drawable.mapa);
        infoRestauranteTextView.setText("Restaurante: " + nombreRestaurante + "\nCiudad: " + ciudadRestaurante);
        imagenRestauranteView.setImageResource(imagenRestaurante);


        cargarMapaSatelital(latitud, longitud);
    }

  
    private void cargarMapaSatelital(double latitud, double longitud) {
        String content = "<html>" +
                "<head>" +
                "<link rel=\"stylesheet\" href=\"https://unpkg.com/leaflet@1.7.1/dist/leaflet.css\" />" +
                "<script src=\"https://unpkg.com/leaflet@1.7.1/dist/leaflet.js\"></script>" +
                "</head>" +
                "<body>" +
                "<div id=\"map\" style=\"width:100%; height:100%;\"></div>" +
                "<script>" +
                "var map = L.map('map').setView([" + latitud + ", " + longitud + "], 13);" +
                "L.tileLayer('https://api.maptiler.com/maps/hybrid/{z}/{x}/{y}.jpg?key=" + mapTilerApiKey + "', {" +
                "    maxZoom: 20," +
                "    attribution: '&copy; <a href=\"https://www.maptiler.com/\">MapTiler</a> contributors'" +
                "}).addTo(map);" +
                "L.marker([" + latitud + ", " + longitud + "]).addTo(map);" +
                "</script>" +
                "</body>" +
                "</html>";


        mapaWebView.loadData(content, "text/html", "UTF-8");
    }
}
