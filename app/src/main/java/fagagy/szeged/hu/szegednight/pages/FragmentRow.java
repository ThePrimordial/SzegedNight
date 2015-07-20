package fagagy.szeged.hu.szegednight.pages;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fagagy.szeged.hu.szegednight.R;
import fagagy.szeged.hu.szegednight.pubList.Pub;
import fagagy.szeged.hu.szegednight.pubList.PubAdapter;

/**
 * Created by Ádám on 15/07/20.
 */
public class FragmentRow extends Fragment {

    public static final String TAG = "FragmentRow";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = View.inflate(getActivity(), R.layout.fragmentrow, null);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
