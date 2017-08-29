package training.edu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import training.edu.droidbountyhunter.R;

/**
 * @author Giovani Gonzalez
 * Created by darkgeat on 8/29/17.
 */

public class LogEliminacion extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list_log_eliminacion, container, false);

        return view;
    }
}
