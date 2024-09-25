package com.example.viajes.viajes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.viajes.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TouristPlacesActivity extends AppCompatActivity {
    private static final String API_KEY = "5ae2e3f221c38a28845f05b6cf76e8fe72a969d06442d6ce528500fa";
    private static final String BASE_URL = "https://api.opentripmap.com/0.1/en/places/radius";
    private static final int RADIUS = 50000;
    private LinearLayout placesContainer;
    private Spinner citySpinner;
    private Map<String, double[]> cityCoordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tourist_places);

        placesContainer = findViewById(R.id.placesContainer);
        citySpinner = findViewById(R.id.citySpinner);

        cityCoordinates = new HashMap<>();

        cityCoordinates.put("Amazonas - Chachapoyas", new double[]{-6.2317, -77.8690});
        cityCoordinates.put("Áncash - Huaraz", new double[]{-9.5261, -77.5281});
        cityCoordinates.put("Apurímac - Abancay", new double[]{-13.6363, -72.8814});
        cityCoordinates.put("Arequipa - Arequipa", new double[]{-16.4090, -71.5375});
        cityCoordinates.put("Ayacucho - Ayacucho", new double[]{-13.1588, -74.2236});
        cityCoordinates.put("Cajamarca - Cajamarca", new double[]{-7.1617, -78.5127});
        cityCoordinates.put("Cusco - Cusco", new double[]{-13.5320, -71.9675});
        cityCoordinates.put("Huancavelica - Huancavelica", new double[]{-12.7866, -74.9726});
        cityCoordinates.put("Huánuco - Huánuco", new double[]{-9.9306, -76.2422});
        cityCoordinates.put("Ica - Ica", new double[]{-14.0678, -75.7286});
        cityCoordinates.put("Junín - Huancayo", new double[]{-12.0653, -75.2049});
        cityCoordinates.put("La Libertad - Trujillo", new double[]{-8.1118, -79.0282});
        cityCoordinates.put("Lambayeque - Chiclayo", new double[]{-6.7714, -79.8409});
        cityCoordinates.put("Lima - Lima", new double[]{-12.0464, -77.0428});
        cityCoordinates.put("Loreto - Iquitos", new double[]{-3.7491, -73.2538});
        cityCoordinates.put("Madre de Dios - Puerto Maldonado", new double[]{-12.5933, -69.1890});
        cityCoordinates.put("Moquegua - Moquegua", new double[]{-17.1939, -70.9356});
        cityCoordinates.put("Pasco - Cerro de Pasco", new double[]{-10.6771, -76.2567});
        cityCoordinates.put("Piura - Piura", new double[]{-5.1945, -80.6328});
        cityCoordinates.put("Puno - Puno", new double[]{-15.8402, -70.0219});
        cityCoordinates.put("San Martín - Moyobamba", new double[]{-6.0348, -76.9712});
        cityCoordinates.put("Tacna - Tacna", new double[]{-18.0147, -70.2538});
        cityCoordinates.put("Tumbes - Tumbes", new double[]{-3.5669, -80.4515});
        cityCoordinates.put("Ucayali - Pucallpa", new double[]{-8.3791, -74.5539});




        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, cityCoordinates.keySet().toArray(new String[0]));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);

        citySpinner.setSelection(0);
        double[] initialCoordinates = cityCoordinates.get("Amazonas - Chachapoyas");
        getTouristPlaces(initialCoordinates[0], initialCoordinates[1], RADIUS);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = (String) parent.getItemAtPosition(position);
                double[] coordinates = cityCoordinates.get(selectedCity);
                getTouristPlaces(coordinates[0], coordinates[1], RADIUS);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void getTouristPlaces(double lat, double lon, int radius) {
        placesContainer.removeAllViews();
        OkHttpClient client = new OkHttpClient();
        String url = BASE_URL + "?radius=" + radius + "&lon=" + lon + "&lat=" + lat + "&apikey=" + API_KEY;

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TouristPlacesActivity", "Error fetching tourist places", e);
                runOnUiThread(() -> Toast.makeText(TouristPlacesActivity.this, "Error fetching tourist places", Toast.LENGTH_SHORT).show());
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
                    runOnUiThread(() -> Toast.makeText(TouristPlacesActivity.this, "API response error", Toast.LENGTH_SHORT).show());
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
            getPlaceDetails(xid);
        }
    }

    private void getPlaceDetails(String xid) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.opentripmap.com/0.1/en/places/xid/" + xid + "?apikey=" + API_KEY;

        Request request = new Request.Builder().url(url).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("TouristPlacesActivity", "Error fetching place details", e);
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
        String name = placeObject.optString("name", "Unknown Place");

        String location = "Unknown Location";
        if (placeObject.has("address")) {
            JSONObject addressObject = placeObject.getJSONObject("address");
            String city = addressObject.optString("city", "");
            String state = addressObject.optString("state", "");
            location = city + ", " + state;
        }

        String description = generateRandomDescription(name, location);

        // Genera un precio aleatorio como número double
        double price = generateRandomPrice();

        String imageUrl = "";
        if (placeObject.has("preview")) {
            imageUrl = placeObject.getJSONObject("preview").optString("source", "");
        }

        if (!imageUrl.isEmpty()) {
            View placeView = getLayoutInflater().inflate(R.layout.item_place, placesContainer, false);

            TextView placeNameTextView = placeView.findViewById(R.id.placeName);
            placeNameTextView.setText(name);

            TextView placeLocationTextView = placeView.findViewById(R.id.placeLocation);
            placeLocationTextView.setText(location);

            ImageView placeImageView = placeView.findViewById(R.id.placeImage);
            Picasso.get().load(imageUrl).placeholder(R.drawable.placeholder_image).into(placeImageView);

            // Guardamos los detalles del lugar como "tag" para acceder a ellos fácilmente más tarde
            placeView.setTag(R.id.placeName, name);
            placeView.setTag(R.id.placeLocation, location);
            placeView.setTag(R.id.placeImage, imageUrl);
            placeView.setTag(R.id.placeDescription, description);
            placeView.setTag(R.id.placePrice, price);  // Ahora el precio es double

            Button travelButton = placeView.findViewById(R.id.travelButton);
            travelButton.setOnClickListener(v -> {
                String selectedName = (String) placeView.getTag(R.id.placeName);
                String selectedLocation = (String) placeView.getTag(R.id.placeLocation);
                String selectedImageUrl = (String) placeView.getTag(R.id.placeImage);
                String selectedDescription = (String) placeView.getTag(R.id.placeDescription);
                double selectedPrice = (double) placeView.getTag(R.id.placePrice);  // Recuperamos el precio como double

                Intent intent = new Intent(TouristPlacesActivity.this, TouristPlaceDetailActivity.class);
                intent.putExtra("image_url", selectedImageUrl);
                intent.putExtra("name", selectedName);
                intent.putExtra("location", selectedLocation);
                intent.putExtra("description", selectedDescription);
                intent.putExtra("price", selectedPrice);  // Pasamos el precio como double
                startActivity(intent);
            });

            placesContainer.addView(placeView);
        }
    }

    private String generateRandomDescription(String name, String location) {
        String[] templates = {
                "El lugar turístico %s en %s es conocido por su belleza y ambiente tranquilo.",
                "Descubre %s, una joya escondida en %s con mucho que ofrecer.",
                "%s en %s es un destino perfecto para relajarse y disfrutar de la naturaleza.",
                "¡Visita %s en %s! Un lugar lleno de historia y cultura.",
                "Explora %s, uno de los lugares más impresionantes de %s.",
                "%s te sorprenderá con su encanto único en %s.",
                "Un destino imperdible en %s es %s. ¡No te lo pierdas!"
        };
        Random random = new Random();
        int index = random.nextInt(templates.length);
        return String.format(templates[index], name, location);
    }

    // Generar precio aleatorio en formato double
    private double generateRandomPrice() {
        Random random = new Random();
        return 50 + random.nextInt(451);  // Precio entre 50 y 500
    }
}
