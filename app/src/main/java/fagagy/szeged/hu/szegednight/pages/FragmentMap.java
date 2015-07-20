package fagagy.szeged.hu.szegednight.pages;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import fagagy.szeged.hu.szegednight.R;

public class FragmentMap extends Fragment {

    public static final String TAG = "TérképNézet";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = View.inflate(getActivity(), R.layout.fragmentmap, null);
        return v;
    }
}
