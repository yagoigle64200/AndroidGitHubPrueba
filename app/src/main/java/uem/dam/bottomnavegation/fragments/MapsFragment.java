package uem.dam.bottomnavegation.fragments;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import uem.dam.bottomnavegation.MainActivity;
import uem.dam.bottomnavegation.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private FusedLocationProviderClient flClient;
    private GoogleMap mGoogleMap;
    private MapView mMapView;
    private View mView;
    private Double lat;
    private Double lon;

    public MapsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_maps, container, false);

        if (getArguments() != null) {
            lat = getArguments().getDouble(MainActivity.LAT_KEY);
            lon = getArguments().getDouble(MainActivity.LON_KEY);
        }

        Toast.makeText(getContext(), "lat: " + lat + " lon: " + lon, Toast.LENGTH_LONG).show();

        return  mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mMapView = mView.findViewById(R.id.map);
        if (mMapView != null) {
            mMapView.onCreate(null);
            mMapView.onResume();
            mMapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        mGoogleMap = googleMap;
        mGoogleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);

        LatLng miLoc = null;

        if (lat == null) {
            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(40.689247, -74.044502)).title("Estatua de la libertad").snippet("I hope to go there some day"));
            CameraPosition Liberty = CameraPosition.builder().target(new LatLng(40.689247, -74.044502)).zoom(14).bearing(0).build();
            mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));
        } else {
            miLoc = new LatLng(lat, lon);

            mGoogleMap.addMarker(new MarkerOptions().position(miLoc).title("MI UBICACION").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_logo)));
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(miLoc, 14));
            mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(40.689247, -74.044502)).title("Estatua de la libertad").snippet("I hope to go there some day"));
        }

        /*mGoogleMap.addMarker(new MarkerOptions().position(new LatLng(40.689247, -74.044502)).title("Estatua de la libertad").snippet("I hope to go there some day"));
        CameraPosition Liberty = CameraPosition.builder().target(new LatLng(40.689247, -74.044502)).zoom(14).bearing(0).build();
        mGoogleMap.moveCamera(CameraUpdateFactory.newCameraPosition(Liberty));*/

        mGoogleMap.getUiSettings().setZoomControlsEnabled(true);

    }
}
