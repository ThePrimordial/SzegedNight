package fagagy.szeged.hu.szegednight.partyResources;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import fagagy.szeged.hu.szegednight.R;

/**
 * Created by TheSorrow on 15/08/04.
 */
public class SZINFragmentList extends ListFragment{


    public static final String TAG = "SZIN";
    private ArrayList<Party> partyList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = View.inflate(getActivity(), R.layout.partyfragmentrow, null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        generateRows();

        PartyAdapter partyAdapter = new PartyAdapter(getActivity(), partyList);
        setListAdapter(partyAdapter);
        registerForContextMenu(getListView());
    }

    private void generateRows() {
        List<ParseObject> serverList = null;
        ParseQuery<ParseObject> query = ParseQuery.getQuery("SZIN").fromLocalDatastore();
        try {
            serverList = query.fromPin("SZIN").find();
        } catch (ParseException e1) {
        }
        for (int i = 0; i < serverList.size(); i++) {
            String place = serverList.get(i).getString("band");
            String event = serverList.get(i).getString("stage");
            int day = serverList.get(i).getInt("Day");
            String from = "";
            String to = "";
            try {
                from = String.valueOf(serverList.get(i).getJSONArray("Time").get(0));
                to = String.valueOf(serverList.get(i).getJSONArray("Time").get(1));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Party p1 = new Party(place, event, day, from, to);
            partyList.add(p1);
        }

        Comparator<Party> compFrom = new Comparator<Party>() {
            @Override
            public int compare(Party p1, Party p2) {
                return p1.getFrom().compareTo(p2.getFrom());
            }
        };


        Comparator<Party> compDay = new Comparator<Party>() {
            @Override
            public int compare(Party p1, Party p2) {
                return Integer.valueOf(p1.getDay()).compareTo(p2.getDay());
            }
        };

        Collections.sort(partyList, new ChainedComparator(compDay, compFrom));

    }



    public class ChainedComparator<T> implements Comparator<T> {
        private List<Comparator<T>> simpleComparators;
        public ChainedComparator(Comparator<T>... simpleComparators) {
            this.simpleComparators = Arrays.asList(simpleComparators);
        }
        public int compare(T o1, T o2) {
            for (Comparator<T> comparator : simpleComparators) {
                int result = comparator.compare(o1, o2);
                if (result != 0) {
                    return result;
                }
            }
            return 0;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
