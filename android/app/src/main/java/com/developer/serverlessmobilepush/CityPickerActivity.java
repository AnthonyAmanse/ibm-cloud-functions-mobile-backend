package com.developer.serverlessmobilepush;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

public class CityPickerActivity extends AppCompatActivity {
    EditText cityInputText;
    Button continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_picker);

        cityInputText = findViewById(R.id.cityInputText);
        continueButton = findViewById(R.id.chooseCity);

        LocalData.initialize(this);
        String cityName = LocalData.getInstance().getCityName();
        Double latitude = LocalData.getInstance().getLatitude();
        Double longitude = LocalData.getInstance().getLongitude();

        if (cityName == null || latitude == null || longitude == null) {
            Log.d("ONE OF KEY DATA IS NULL", "user will choose city");
        } else {
            Log.d("PROCEED TO PICKER","done");
            startPickerActivity(cityName, latitude, longitude);
        }

        final Geocoder geocoder = new Geocoder(this);

//        Calendar cal = Calendar.getInstance();
//        TimeZone tz = cal.getTimeZone();
//        Log.d("Time zone","="+tz.getID());

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("City name is", cityInputText.getText().toString());
                try {
                    List<Address> addressList = geocoder.getFromLocationName(cityInputText.getText().toString(),1);
                    if (addressList.size() > 0) {
                        Address address = addressList.get(0);
//                        Log.d("City name from geo is", addressList.get(0).getLocality());
//                        Log.d("Latitude is", String.valueOf(addressList.get(0).getLatitude()));
//                        Log.d("Longitude is", String.valueOf(addressList.get(0).getLongitude()));
                        startPickerActivity(address.getLocality(), address.getLatitude(), address.getLongitude(), true);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void startPickerActivity(String cityName, Double latitude, Double longitude) {
        startPickerActivity(cityName, latitude, longitude, false);
    }

    private void startPickerActivity(String cityName, Double latitude, Double longitude, boolean storeData) {
        if (storeData) {
            final LocalData localData = LocalData.getInstance();
            localData.setCityName(cityName);
            localData.setLatitude(latitude);
            localData.setLongitude(longitude);
        }

        final Intent intent = new Intent(getApplicationContext(), WeatherPickerActivity.class);
        intent.putExtra("cityName", cityName);
        intent.putExtra("latitude", latitude);
        intent.putExtra("longitude", longitude);
        startActivity(intent);
    }
}
