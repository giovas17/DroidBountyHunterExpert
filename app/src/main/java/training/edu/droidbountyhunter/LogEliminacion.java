package training.edu.droidbountyhunter;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import training.edu.data.DBProvider;

/**
 * @author Giovani Gonzalez
 * Created by darkgeat on 27/08/2017.
 */

public class LogEliminacion extends ListActivity {

    ArrayAdapter<String> adaptador;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Log de Eliminación");

        DBProvider database = new DBProvider(this);
        ArrayList<String[]> logs = database.ObtenerLogsEliminacion();
        String[] logsRow = new String[logs.size()];
        for (int index = 0 ; index < logs.size() ; index++){
            logsRow[index] = logs.get(index)[0] + " --> " + logs.get(index)[1];
        }
        adaptador = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,logsRow);
        setListAdapter(adaptador);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Toast.makeText(this, String.valueOf(position) + " " + getListAdapter().getItem(position).toString(),Toast.LENGTH_LONG).show();
    }
}
