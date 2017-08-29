package training.edu.droidbountyhunter;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import training.edu.data.DBProvider;
import training.edu.models.Fugitivo;

/**
 * @author Giovani González
 * Created by darkgeat on 09/08/2017.
 */

public class Detalle extends AppCompatActivity{

    private String titulo;
    private int mode;
    private int id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        // Se obtiene la información del intent
        titulo = getIntent().getStringExtra("title");
        mode = getIntent().getIntExtra("mode",0);
        id = getIntent().getIntExtra("id",0);
        // Se pone el nombre del fugitivo como titulo
        setTitle(titulo + " - [" + id + "]");
        TextView message = (TextView) findViewById(R.id.mensajeText);
        // Se identifica si es Fugitivo o Capturado para el mensaje...
        if (mode == 0){
            message.setText("El fugitivo sigue suelto...");
        }else {
            Button delete = (Button)findViewById(R.id.buttonEliminar);
            delete.setVisibility(View.GONE);
            message.setText("Atrapado!!!");
        }
    }

    public void OnCaptureClick(View view) {
        DBProvider database = new DBProvider(this);
        database.UpdateFugitivo(new Fugitivo(id,titulo,String.valueOf(mode)));
        setResult(0);
        finish();
    }

    public void OnDeleteClick(View view) {
        DBProvider database = new DBProvider(this);
        database.DeleteFugitivo(id);
        setResult(0);
        finish();
    }

    public void MessageClose(String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.create();
        builder.setTitle("Alerta!!!");
        builder.setMessage(message);
        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                setResult(mode);
                finish();
            }
        });
        builder.show();
    }

}
