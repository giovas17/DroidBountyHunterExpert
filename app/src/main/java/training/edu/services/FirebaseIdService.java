package training.edu.services;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * @author Giovani Gonzalez
 * Created by darkgeat on 8/31/17.
 */

public class FirebaseIdService extends FirebaseInstanceIdService {

    private static final String TAG = FirebaseIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,"Token: " + refreshedToken);
    }
}
