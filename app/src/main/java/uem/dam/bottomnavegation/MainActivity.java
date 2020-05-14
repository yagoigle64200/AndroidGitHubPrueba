package uem.dam.bottomnavegation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import uem.dam.bottomnavegation.fragments.HomeFragment;
import uem.dam.bottomnavegation.fragments.MapsFragment;
import uem.dam.bottomnavegation.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_LOCATION =123;

    public static final String LAT_KEY = "LAT";
    public static final String LON_KEY = "LON";

    BottomNavigationView mBottomNavigation;
    FusedLocationProviderClient flClient;
    Location miLoc;
    //prueba

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Pedimos los permisos de ubicación
        pedirPermisos();

        // Cuando se habra la app veremos primero el fragment HomeFragment
        showSelectedFragment(new HomeFragment());

        mBottomNavigation = findViewById(R.id.bottomNavigation);

        // Método para cambiar los colores del bottom navigation view
        changeColor();

        // Controlamos cuando se pulsa el bottomNavigation para mostrar su correspondiente fragment
        mBottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_home) {
                    showSelectedFragment(new HomeFragment());
                }

                if (menuItem.getItemId() == R.id.menu_maps) {

                    FragmentManager fm = getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                    // Inicializamos un nuevo fragment
                    MapsFragment mapsFragment = new MapsFragment();

                    // Guardamos la latitud y longitud de mi localización en LAT_KEY y LON_KEY para mandarlos al fragment
                    Bundle bundleLoc = new Bundle();
                    bundleLoc.putDouble(LAT_KEY, miLoc.getLatitude());
                    bundleLoc.putDouble(LON_KEY, miLoc.getLongitude());

                    // Pasamos la latitud y longitud a mapsFragment de nuestra posición actual recogida con anterioridad.
                    mapsFragment.setArguments(bundleLoc);

                    //Toast.makeText(getApplicationContext(), "lat: " + miLoc.getLatitude() + " lon: " + miLoc.getLongitude(), Toast.LENGTH_LONG).show();

                    // Abrimos el mapsFragment
                    ft.add(R.id.container, mapsFragment);
                    ft.commit();

                }

                if (menuItem.getItemId() == R.id.menu_profile) {
                    showSelectedFragment(new ProfileFragment());
                }

                return true;
            }
        });

    }

    // Con este método verificamos si tenemos los permisos de la localización eeeeeeeeeeeeee
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_ACCESS_LOCATION:
                // Si los permisos de localización son aceptados entonces el programas sigue
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED){

                    // Obtenemos la ubicacion actual nuestra
                    flClient = LocationServices.getFusedLocationProviderClient(this);

                    flClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.i("LOC", "onSuccess de localitation");
                            if (location != null) {
                                miLoc = location;
                            }
                        }
                    });

                }else{
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "No se aceptó permisos", Toast.LENGTH_SHORT).show();
                    pedirPermisos();
                }
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


    // Pedimos los permisos al usuario para acceder a su ubicación, si no los tenemos todavía
    private void pedirPermisos() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_ACCESS_LOCATION);
        } else if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Obtenemos la ubicacion actual nuestra
            flClient = LocationServices.getFusedLocationProviderClient(this);

            flClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    Log.i("LOC", "onSuccess de localitation");
                    if (location != null) {
                        miLoc = location;
                    }
                }
            });
        }
    }

    private void onNavigationItemSelected(MenuItem item) {
    }

    // Método que permite elegir el fragment
    private void showSelectedFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    // Metodo para cambiar el color del icono y texto cuando esté seleccionado
    private void changeColor() {
        ColorStateList iconsColorStates = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        Color.parseColor("#F2F2F2"),
                        Color.parseColor("#F2B705")
                });

        ColorStateList textColorStates = new ColorStateList(
                new int[][]{
                        new int[]{-android.R.attr.state_checked},
                        new int[]{android.R.attr.state_checked}
                },
                new int[]{
                        Color.parseColor("#F2F2F2"),
                        Color.parseColor("#F2B705")
                });

        mBottomNavigation.setItemIconTintList(iconsColorStates);
        mBottomNavigation.setItemTextColor(textColorStates);
    }

}
