package fagagy.szeged.hu.szegednight.pubRescources;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fagagy.szeged.hu.szegednight.R;

/**
 * Created by TheSorrow on 15/07/20.
 */
public class PubFragmentList extends ListFragment implements OnItemClickListener {

    public static final String TAG = "ListaNézet";
    private PubAdapter pubAdapter;
    ArrayList<Pub> pubList = new ArrayList<Pub>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = View.inflate(getActivity(), R.layout.pubfragmentrow, null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){

        super.onActivityCreated(savedInstanceState);

        Collections.sort(pubList, new Comparator<Pub>() {
            @Override
            public int compare(Pub p1, Pub p2) {
                return Double.compare(p1.getDistance(), p2.getDistance());
            }
        });

        PubAdapter pubAdapter = new PubAdapter(getActivity(), pubList);
        setListAdapter(pubAdapter);
        registerForContextMenu(getListView());
        getListView().setOnItemClickListener(this);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getActivity(), "Működik", Toast.LENGTH_SHORT)
                .show();
    }
}
