package razor.nikhil.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import razor.nikhil.R;

/**
 * Created by Nikhil Verma on 9/21/2015.
 */
public class SubDetailTab extends Fragment {
    public static SubDetailTab newInstance() {
        return new SubDetailTab();
    }

    public SubDetailTab() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tab1_detail, container, false);
    }
}
