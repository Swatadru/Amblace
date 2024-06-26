package com.example.amblace1;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.amblace1.R;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity implements OnMapReadyCallback {

    ImageButton imgButton;
    private GoogleMap mMap;
    private final int FINE_PERMISSION_CODE = 1;
    Button b4_sourcebutton;
    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    ImageView imgExpandable;
    BottomSheetRiderFragment mBottomSheet;
    Button btnPickupRequest;
    Marker mUserMarker = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();

        b4_sourcebutton = findViewById(R.id.button4);

        imgButton = findViewById(R.id.imageButton5);
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, User_Button_Activity.class);
                startActivity(i);
            }
        });

        imgExpandable = findViewById(R.id.imageView5);
        mBottomSheet = BottomSheetRiderFragment.newInstance("Rider Bottom Sheet");
        imgExpandable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheet.show(getSupportFragmentManager(), mBottomSheet.getTag());
            }
        });

        btnPickupRequest = findViewById(R.id.button5);
        btnPickupRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPickupHere(FirebaseAuth.getInstance().getCurrentUser().getUid());
            }
        });

    }

    private void requestPickupHere(String uid) {
        DatabaseReference dbRequest = FirebaseDatabase.getInstance().getReference("PickupRequest");
        GeoFire mGeoFire = new GeoFire(dbRequest);
        mGeoFire.setLocation(uid, new GeoLocation(currentLocation.getLatitude(), currentLocation.getLongitude()));

        if (mUserMarker != null){
            boolean isvisible = mUserMarker.isVisible();
            mUserMarker.remove();
        }else{
            Log.d("Home","Pickup marker is not initialised");
        }
        mUserMarker = mMap.addMarker(new MarkerOptions()
                .title("Pickup Here")
                .snippet("")
                .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        mUserMarker.showInfoWindow();

        btnPickupRequest.setText("Getting your Driver.....");
    }

    private void getLastLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, FINE_PERMISSION_CODE);
            return;
        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){
                    currentLocation = location;

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync(Home.this);
                }
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
        mMap.addMarker(new MarkerOptions().position(sydney).title("My Location"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney,15.0f));


        Geocoder geocoder = new Geocoder(this,Locale.getDefault());
        try {
            List<Address> myaddress = geocoder.getFromLocation(sydney.latitude, sydney.longitude, 1);
            String address = myaddress.get(0).getAddressLine(0);
            b4_sourcebutton.setText(address);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == FINE_PERMISSION_CODE){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }
            else {
                Toast.makeText(this, "Permission Denied. Please allow the permission", Toast.LENGTH_SHORT).show();
            }
        }
    }
}