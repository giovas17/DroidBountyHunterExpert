package training.edu.droidbountyhunter;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import training.edu.data.DBProvider;
import training.edu.interfaces.OnTaskListener;
import training.edu.models.Fugitivo;
import training.edu.network.NetServices;

/**
 * @author Giovani González
 * Created by darkgeat on 09/08/2017.
 */

public class Agregar extends AppCompatActivity{

    /**------------------------------------------------
     * Variables para el manejo de XML
     **------------------------------------------------*/
    // Instanciamos la fábrica para DOM
    DocumentBuilderFactory factory;
    // Creacion del parser DOM
    DocumentBuilder builder;
    Document dom;
    Element root;
    NodeList items;
    String valor;
    int contadorPorcentaje;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);
    }

    public void OnSaveClick(View view) {
        TextView name = (TextView)findViewById(R.id.editTextName);
        if (name.getText().toString().length() > 0){
            DBProvider database = new DBProvider(this);
            database.InsertFugitivo(new Fugitivo(0,name.getText().toString(),"0","",0));
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
                            database.InsertFugitivo(new Fugitivo(0,nameFugitive,"0","",0));
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

    void importarXML(){
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.fugitivos);
            dom = builder.parse(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void retardo(){
        // Se implementa retardo en el hilo
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void insertarFugitivo(String nameFugitivo){
        DBProvider database = new DBProvider(this);
        database.InsertFugitivo(new Fugitivo(0,nameFugitivo,"0","",0));
    }

    public void OnXMLClick(View view) {
        DBProvider database = new DBProvider(this);
        if (database.ContarFugitivos() <= 0){
            // Se ocultan los botones que no se utilizarán
            Button botonXML = (Button) findViewById(R.id.buttonAddXML);
            botonXML.setVisibility(View.GONE);
            Button botonSave = (Button) findViewById(R.id.buttonSave);
            botonSave.setVisibility(View.GONE);
            Button botonWebService = (Button) findViewById(R.id.buttonAddWebService);
            botonWebService.setVisibility(View.GONE);
            //----------------------------------------------------------------------
            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            final TextView statusProgress = (TextView) findViewById(R.id.txtProgreso);
            progressBar.setMax(100);
            // Se inicializan las variables de lectura del xml
            try {
                factory = DocumentBuilderFactory.newInstance();
                builder = factory.newDocumentBuilder();
                importarXML();
                root = dom.getDocumentElement();
                items = root.getElementsByTagName("fugitivo");
            }catch (Exception e){
                e.printStackTrace();
            }

            new Thread(){
                @Override
                public void run() {
                    progressBar.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(0);
                        }
                    });
                    for (int i = 0 ; i < items.getLength() ; i++){
                        valor = items.item(i).getFirstChild().getNodeValue();
                        contadorPorcentaje = (i + 1) * 10;
                        retardo();
                        insertarFugitivo(valor);
                        progressBar.post(new Runnable() {
                            @Override
                            public void run() {
                                statusProgress.setText(getString(R.string.progreso)
                                        + " " + contadorPorcentaje + "%");
                                progressBar.incrementProgressBy(10);
                            }
                        });
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Importación de Fugitivos finalizada!",Toast.LENGTH_LONG).show();
                            setResult(0);
                            finish();
                        }
                    });
                }
            }.start();
        }else {
            // notificacion de Fugitivos en la Base de Datos
            Toast.makeText(getApplicationContext(),
                    "No es posible solicitar carga via XML ya que se tiene al menos un fugitivo" +
                            "en la base de datos",Toast.LENGTH_LONG).show();
        }
    }
}
