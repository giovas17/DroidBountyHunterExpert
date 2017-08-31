package training.edu.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;

import training.edu.models.Fugitivo;

/**
 * @author Giovani Gonzalez
 * Created by darkgeat on 8/30/17.
 */

public class ServicioNotificaciones extends Service {

    private static ServicioNotificaciones instance = null;
    private Timer timer;
    private ArrayList<Fugitivo> fugitivosSinNotificar = new ArrayList<>();
    private ArrayList<String[]> logsSinNotificar = new ArrayList<>();

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this,"Servicio Creado",Toast.LENGTH_LONG).show();
        instance = this;
    }

    public void EnviarNotificacion(){

    }

    public static boolean isRunning(){
        return instance != null;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
