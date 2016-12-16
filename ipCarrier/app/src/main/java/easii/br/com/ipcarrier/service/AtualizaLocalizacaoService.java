package easii.br.com.ipcarrier.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import easii.br.com.ipcarrier.objetos.Config;
import easii.br.com.ipcarrier.objetos.Funcionario;
import easii.br.com.ipcarrier.objetos.GPSTracker;

/**
 * Created by gustavo on 26/11/2016.
 */
public class AtualizaLocalizacaoService extends Service {

    private String idUser;
    private GPSTracker gpsTracker;
    private GoogleApiClient googleApiClient;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SharedPreferences sharedPref = this.getSharedPreferences("CONSTANTES", Context.MODE_PRIVATE);
        idUser = sharedPref.getString("idUser", "NULL");
        Log.i("LOG", "service entrou " + idUser);

        Timer t = new Timer();
        Firebase.setAndroidContext(this);
        gpsTracker = new GPSTracker(this);

        t.scheduleAtFixedRate(new TimerTask() {

                                  @Override
                                  public void run() {
                                      Firebase ref = new Firebase(Config.FIREBASE_URL);
                                      ref.child(idUser).child("latitude").setValue(gpsTracker.getLocation().getLatitude());
                                      ref.child(idUser).child("longitude").setValue(gpsTracker.getLocation().getLongitude());

                                      Log.i("LOG", "firebase *** LAT = " + gpsTracker.getLocation().getLatitude() + "," + gpsTracker.getLocation().getLongitude());

                                  }

                              },
//Set how long before to start calling the TimerTask (in milliseconds)
                0,
//Set the amount of time between each execution (in milliseconds)
                10000);


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
