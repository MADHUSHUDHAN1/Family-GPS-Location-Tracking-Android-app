package com.example.familygpstracker.ui.location;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.familygpstracker.MyNavigationActivity;
import com.example.familygpstracker.R;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executor;

import static android.content.Context.LOCATION_SERVICE;


public class location_Fragment extends Fragment implements OnMapReadyCallback{
    GoogleMap mMap;
    private LocationListener locationListener;
    LocationRequest request;
    private CameraPosition cameraPosition;
    private PlacesClient placesClient;
    private LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationProviderClient;
    Context context;
    FirebaseAuth auth;
    FirebaseUser user;
    String lat,lng;
    DatabaseReference databaseReference;

    private final long MIN_TIME = 1000;
    private final long MIN_DIST = 5;

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    private Location lastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final int DEFAULT_ZOOM = 15;

    //private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);

    // Used for selecting the current place.
    private static final int M_MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;

    // private OnMapReadyCallback callback = new OnMapReadyCallback() {

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * In this case, we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to
     * install it inside the SupportMapFragment. This method will only be triggered once the
     * user has installed Google Play services and returned to the app.
     */
    //  @Override
    //  public void onMapReady(GoogleMap googleMap) {
    //     LatLng sydney = new LatLng(-34, 151);
    //     googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
    //    googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    // }
    //  };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_location_, container, false);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid());

        //LINK TO DOO
        //https://github.com/googlemaps/android-samples/blob/b056901a8b179e46f7f821ac9462d695a70dad95/tutorials/java/CurrentPlaceDetailsOnMap/app/src/main/java/com/example/currentplacedetailsonmap/MapsActivityCurrentPlace.java#L99-L131
        //https://developers.google.com/maps/documentation/android-sdk/current-place-tutorial#java_2
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);


        return view;
    }



    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap =googleMap;
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

               LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
               mMap.addMarker(new MarkerOptions().position(latLng).title("My Location"));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,14F));
                lat = String.valueOf(location.getLatitude());
                lng = String.valueOf(location.getLongitude());
                databaseReference.child("lat").setValue(lat);
                databaseReference.child("lng").setValue(lng);

            }
        };
        locationManager =
                (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DIST, locationListener);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }




   /* @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
       if (mapFragment != null) {
           mapFragment.getMapAsync(this);
        }

        //mapFragment.getMapAsync(this);
        //ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.SEND_SMS}, PackageManager.PERMISSION_GRANTED);
    }
    */

}