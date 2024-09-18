package com.example.viajes.viajes;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.viajes.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TouristPlacesActivity extends AppCompatActivity {
    private static final String API_KEY = "5ae2e3f221c38a28845f05b6cf76e8fe72a969d06442d6ce528500fa";
    private static final String BASE_URL = "https://api.opentripmap.com/0.1/en/places/radius";
    private static final double LATITUDE = -12.0464; // Lima, Perú
    private static final double LONGITUDE = -77.0428;
    private static final int RADIUS = 5000; // 5 km

    private LinearLayout placesContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_places);

        placesContainer = findViewById(R.id.placesContainer);

        // Obtener lugares turísticos
        getTouristPlaces(LATITUDE, LONGITUDE, RADIUS);
    }

    private void getTouristPlaces(double lat, double lon, int radius) {
        OkHttpClient client = new OkHttpClient();
        String url = BASE_URL + "?radius=" + radius + "&lon=" + lon + "&lat=" + lat + "&apikey=" + API_KEY;

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TouristPlacesActivity", "Error al obtener lugares turísticos", e);
                runOnUiThread(() -> Toast.makeText(TouristPlacesActivity.this, "Error al obtener lugares turísticos", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(() -> {
                        try {
                            parseAndDisplayResults(responseData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    runOnUiThread(() -> Toast.makeText(TouristPlacesActivity.this, "Error en la respuesta de la API", Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void parseAndDisplayResults(String jsonData) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonData);
        JSONArray featuresArray = jsonObject.getJSONArray("features");

        for (int i = 0; i < featuresArray.length(); i++) {
            JSONObject place = featuresArray.getJSONObject(i);
            String xid = place.getJSONObject("properties").getString("xid");
            getPlaceDetails(xid); // Llamar a la función para obtener detalles del lugar
        }
    }

    private void getPlaceDetails(String xid) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.opentripmap.com/0.1/en/places/xid/" + xid + "?apikey=" + API_KEY;

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TouristPlacesActivity", "Error al obtener detalles del lugar", e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String responseData = response.body().string();
                    runOnUiThread(() -> {
                        try {
                            parsePlaceDetails(responseData);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private void parsePlaceDetails(String jsonData) throws JSONException {
        JSONObject placeObject = new JSONObject(jsonData);
        String name = placeObject.optString("name", "Lugar sin nombre");

        // Extraer la URL de la imagen si está disponible
        String imageUrl = "";
        if (placeObject.has("preview")) {
            imageUrl = placeObject.getJSONObject("preview").getString("source");
        }

        // Crear la vista personalizada para el lugar turístico
        View placeView = getLayoutInflater().inflate(R.layout.item_place, placesContainer, false);

        // Configurar el nombre del lugar
        TextView placeNameTextView = placeView.findViewById(R.id.placeName);
        placeNameTextView.setText(name);

        // Cargar la imagen usando Picasso
        ImageView placeImageView = placeView.findViewById(R.id.placeImage);
        if (!imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).placeholder(R.drawable.placeholder_image).into(placeImageView);
        } else {
            placeImageView.setImageResource(R.drawable.placeholder_image);
        }

        // Agregar la vista al contenedor
        placesContainer.addView(placeView);
    }
}
