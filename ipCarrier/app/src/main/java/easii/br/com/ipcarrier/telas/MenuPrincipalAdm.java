package easii.br.com.ipcarrier.telas;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;
import java.util.Locale;

import easii.br.com.ipcarrier.R;
import easii.br.com.ipcarrier.objetos.Config;
import easii.br.com.ipcarrier.objetos.Funcionario;
import easii.br.com.ipcarrier.objetos.GPSTracker;
import easii.br.com.ipcarrier.service.AtualizaLocalizacaoService;
import easii.br.com.ipcarrier.service.GPSService;


public class MenuPrincipalAdm extends AppCompatActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMarkerDragListener,
        GoogleMap.OnMapClickListener,
        View.OnClickListener {


    private Toolbar toolbar;
    private FloatingActionMenu materialDesignFAM;
    private FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;

    private GoogleMap mMap;
    private double longitude;
    private double latitude;
    private GoogleApiClient googleApiClient;
    private Handler handler;
    private Runnable runnableCode;
    private String idUser;
  //  private  LatLng latLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal_adm);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setTitle(" ip Carrier");
        getSupportActionBar().setLogo(R.drawable.ipbranco);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        callConnetion();

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (FloatingActionButton) findViewById(R.id.material_design_floating_action_menu_item2);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //  Intent i = new Intent(MenuPrincipalAdm.this, TelaDeAdicionarFuncionario.class);
                // startActivity(i);
                Toast.makeText(MenuPrincipalAdm.this, "void 2", Toast.LENGTH_LONG).show();
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(MenuPrincipalAdm.this, "void", Toast.LENGTH_LONG).show();

            }
        });
        Firebase.setAndroidContext(this);

        Intent i = new Intent(MenuPrincipalAdm.this, GPSService.class);
        startService(i);

    }

    private synchronized void callConnetion() {

        googleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .addConnectionCallbacks(this)
                .addApi(LocationServices.API)
                .build();

        googleApiClient.connect();


    }

    private void addPontos(double latitude, double longitude, String nome) {
        Location l = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);

        Log.i("LOG", "Latitude : " + latitude);
        Log.i("LOG", "Longitude : " + longitude + "\n tá aki");

        LatLng latLng = new LatLng(latitude, longitude);
        Log.i("LATLONG", latLng.toString());
        MarkerOptions marker = new MarkerOptions()
                .position(latLng) //setting position
                .draggable(false) //Making the marker draggable
                .title(nome); //Adding a title

        mMap.addMarker(marker);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_menu_principal_adm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConnected(Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    protected void onStart() {
        googleApiClient.connect();
        super.onStart();

    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    private void getCurrentLocation() {
       // mMap.clear();
        //Creating a location object
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            //moving the map to location
            moveMap();
            // addPontos();
        }
    }

    //Function to move the map
    private void moveMap() {
        mMap.clear();
        String msg = latitude + ", " + longitude;
        LatLng latLng = new LatLng(latitude, longitude);
        LatLng the = new LatLng(-5.092, -42.8038);
        mMap.addMarker(new MarkerOptions()
                .position(latLng) //setting position
                .draggable(true) //Making the marker draggable
                .title("Minha Localização")); //Adding a title
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        getCompleteAddressString(latitude, longitude);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////
        Firebase ref = new Firebase(Config.FIREBASE_URL);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mMap.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {

                    Funcionario f = postSnapshot.getValue(Funcionario.class);

                    Log.i("LOG", " nome = " + f.getNome() + " tel = " + f.getTelefone() + " lat = " + f.getLatitude() + " lon = " + f.getLongitude());
                    addPontos(f.getLatitude(), f.getLongitude(), f.getNome());
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.i("LOG", "Localizacao Atual" + strReturnedAddress.toString());
            } else {
                Log.i("LOG", "Localizacao Atual - No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("LOG", "Canont get Address!");
        }
        return strAdd;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        moveMap();

//        SharedPreferences sharedPref = this.getSharedPreferences("CONSTANTES", Context.MODE_PRIVATE);
//        idUser = sharedPref.getString("idUser", "NULL");
//
//        Firebase ref = new Firebase(Config.FIREBASE_URL);
//        ref.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot snapshot) {
//                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
//
//                    Funcionario f = postSnapshot.getValue(Funcionario.class);
//                    Log.i("LOG", " nome = " + f.getNome() + " tel = " + f.getTelefone() + " lat = " + f.getLatitude() + " lon = " + f.getLongitude());
//                    addPontos(f.getLatitude(), f.getLongitude(), f.getNome());
//                }
//            }
//
//            @Override
//            public void onCancelled(FirebaseError firebaseError) {
//                System.out.println("The read failed: " + firebaseError.getMessage());
//            }
//        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        LatLng the = new LatLng(-5.092, -42.8038);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(the));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {

        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;
        moveMap();
    }
}
