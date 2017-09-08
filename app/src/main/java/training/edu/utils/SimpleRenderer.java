package training.edu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import training.edu.droidbountyhunter.ActivityOpenGLFugitivos;
import training.edu.droidbountyhunter.R;

/**
 * @author Giovani Gonzalez
 * Created by giovani on 9/8/17.
 */

public class SimpleRenderer implements GLSurfaceView.Renderer {

    private Context context;
    private FloatBuffer vertexBuffer;
    private FloatBuffer texturalBuffer;
    private ShortBuffer indexBuffer;
    private int carasLength;

    public SimpleRenderer(Context context) {
        this.context = context;
    }

    public void cargarTextura(GL10 gl){
        Bitmap bitmap;
        if (ActivityOpenGLFugitivos.fotoDefault.equalsIgnoreCase("0")){
            bitmap = PictureTools.decodeSampledBitmapFromUri(ActivityOpenGLFugitivos.foto, 200,200);
        }else {
            bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        }
        int[] textureIds = new int[1];
        gl.glGenTextures(1,textureIds,0);
        int textureId = textureIds[0];

        gl.glEnable(GL10.GL_TEXTURE_2D);
        gl.glBindTexture(GL10.GL_TEXTURE_2D, textureId);

        gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        gl.glTexParameterf(GL10.GL_TEXTURE_2D,GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_LINEAR);

        GLUtils.texImage2D(GL10.GL_TEXTURE_2D,0,bitmap,0);

    }

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        Log.d("AR", "superficie modificada: " + width + "x" + height);
        float positivo = ActivityOpenGLFugitivos.distorcion;
        float negativo = ActivityOpenGLFugitivos.distorcion * (-1.0f);

        float vertces[] = {
            negativo, 1f, 0f,
                -1f, -1f, 0f,
                0f, -1f, 0f,
                1f, -1f, 0f,
                positivo, 1f, 0f,
        };

        short caras[] = {
                0, 1, 2,
                0, 2, 4,
                2, 3, 4
        };

        carasLength = caras.length;

        float textura[] = {
                0f, 0f,
                0f, 1f,
                0.5f, 1f,
                1f, 1f,
                1f, 0f
        };
    }

    @Override
    public void onDrawFrame(GL10 gl10) {

    }
}
