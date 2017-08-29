package training.edu.droidbountyhunter;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import training.edu.data.DBProvider;
import training.edu.interfaces.OnTaskListener;
import training.edu.models.Fugitivo;
import training.edu.network.NetServices;

/**
 * @author Giovani González
 * Created by darkgeat on 09/08/2017.
 */

public class Agregar extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
    }

    public void OnSaveClick(View view) {
        TextView name = (TextView)findViewById(R.id.editTextName);
        if (name.getText().toString().length() > 0){
            DBProvider database = new DBProvider(this);
            database.InsertFugitivo(new Fugitivo(0,name.getText().toString(),"0"));
            setResult(0);
            finish();
        }else {
            new AlertDialog.Builder(this)
                    .setTitle("Alerta")
                    .setMessage("Favor de capturar el nombre del fugitivo.")
                    .show();
        }
    }

    public void OnWebServiceClick(View view) {
        final DBProvider database = new DBProvider(this);
        // Se revisa si hay fugitivos en la base de datos
        if (database.ContarFugitivos() == 0){
            NetServices apiCall = new NetServices(new OnTaskListener() {
                @Override
                public void OnTaskCompleted(String response) {
                    try {
                        JSONArray array = new JSONArray(response);
                        for (int i = 0 ; i < array.length() ; i++){
                            JSONObject object = array.getJSONObject(i);
                            String nameFugitive = object.optString("name","");
                            database.InsertFugitivo(new Fugitivo(0,nameFugitive,"0"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } finally {
                        // Despues de cargar los registros en la web en la base de datos cerraremos el activity
                        setResult(0);
                        finish();
                    }
                }

                @Override
                public void OnTaskError(int errorCode, String message, String error) {
                    // Error de servicio.
                    Toast.makeText(Agregar.this,"Ocurrió un problema con el Webservice!!", Toast.LENGTH_LONG).show();
                }
            });
            apiCall.execute("Fugitivos");
        }else {
            // Error de servicio.
            Toast.makeText(this,"No se puede hacer la carga remota ya que se tiene al menos un fugitivo"
                    + " en la base de datos",Toast.LENGTH_LONG).show();
        }
    }
}
