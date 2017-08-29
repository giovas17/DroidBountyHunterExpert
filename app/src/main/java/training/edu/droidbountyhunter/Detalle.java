package training.edu.droidbountyhunter;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import training.edu.data.DBProvider;
import training.edu.interfaces.OnTaskListener;
import training.edu.models.Fugitivo;
import training.edu.network.NetServices;
import training.edu.utils.PictureTools;

import static training.edu.utils.PictureTools.MEDIA_TYPE_IMAGE;

/**
 * @author Giovani González
 * Created by darkgeat on 09/08/2017.
 */

public class Detalle extends AppCompatActivity{

    private String titulo;
    private int mode;
    private int id;
    private Uri pathImage;
    private static final int REQUEST_CODE_PHOTO_IMAGE = 1787;

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
            ImageView photoImageView = (ImageView)findViewById(R.id.pictureFugitive);
            String pathPhoto = getIntent().getStringExtra("photo");
            if (pathPhoto != null && pathPhoto.length() > 0){
                Bitmap bitmap = PictureTools.decodeSampledBitmapFromUri(pathPhoto,200,200);
                photoImageView.setImageBitmap(bitmap);
            }
        }
    }

    public void OnCaptureClick(View view) {
        DBProvider database = new DBProvider(this);
        String pathPhoto = PictureTools.currentPhotoPath;
        if (pathPhoto == null || pathPhoto.length() == 0){
            Toast.makeText(this,"Es necesario tomar la foto antes de capturar al fugitivo",Toast.LENGTH_LONG).show();
            return;
        }
        database.UpdateFugitivo(new Fugitivo(id,titulo,"1",pathPhoto.length() == 0 ? "" : pathPhoto));
        NetServices netServices = new NetServices(new OnTaskListener() {
            @Override
            public void OnTaskCompleted(String response) {
                // despues de traer los datos del web service se actualiza la interfaz...
                String message = "";
                try {
                    JSONObject object = new JSONObject(response);
                    message = object.optString("mensaje","");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                MessageClose(message);
            }

            @Override
            public void OnTaskError(int errorCode, String message, String error) {
                Toast.makeText(Detalle.this, "Ocurrio un problema en la comunicación con el WebService!!!", Toast.LENGTH_LONG).show();
            }
        });
        netServices.execute("Atrapar", Home.UDID);
        setResult(0);
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

    public void OnFotoClick(View view) {
        if(PictureTools.permissionReadMemmory(this)) {
            dispatchPicture();
        }
    }

    private void dispatchPicture(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        pathImage = PictureTools.with(Detalle.this).getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pathImage);
        startActivityForResult(intent, REQUEST_CODE_PHOTO_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PHOTO_IMAGE){
            if (resultCode == RESULT_OK){
                ImageView imageFugitive = (ImageView) findViewById(R.id.pictureFugitive);
                Bitmap bitmap = PictureTools.decodeSampledBitmapFromUri(PictureTools.currentPhotoPath,200,200);
                imageFugitive.setImageBitmap(bitmap);
            }
        }
    }
}
