package razor.nikhil.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.R;
import razor.nikhil.adapter.MarksGridAdapter;
import razor.nikhil.model.DetailAtten;
import razor.nikhil.model.Marks_Model;
import razor.nikhil.model.PBL_Model;

/**
 * Created by Nikhil Verma on 7/28/2015.
 */
public class MarksView extends Fragment {
    private TextView marks_main;
    private Marks_Model marks_cbl = null;
    private PBL_Model marks_pbl = null;
    RecyclerView mRecyclerView;
    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView.Adapter mAdapter;
    private List<DetailAtten> ld = new ArrayList<>();

    public MarksView(PBL_Model sd, List<DetailAtten> lds) {
        this.ld = lds;
        marks_pbl = sd;
    }

    public MarksView() {
    }

    public MarksView(Marks_Model model, List<DetailAtten> ld) {
        this.ld = ld;
        marks_cbl = model;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycler_marks_info, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_minfo);
        mRecyclerView.setHasFixedSize(true);
        //No. of Cols
        mLayoutManager = new GridLayoutManager(inflater.getContext(), 3);
        mRecyclerView.setLayoutManager(mLayoutManager);
        if (marks_cbl != null)
        {
            if (marks_cbl.getASIIGN().trim().equals("-")) {
                mAdapter = new MarksGridAdapter(getActivity(), marks_cbl, false);
            } else {
                mAdapter = new MarksGridAdapter(getActivity(), marks_cbl, true);
            }
        }
        if (marks_pbl != null) {
            mAdapter = new MarksGridAdapter(getActivity(), marks_pbl);
        }
        mRecyclerView.setAdapter(mAdapter);
        //  recyclerView = (RecyclerView) view.findViewById(R.id.detail_att_list);
        //recyclerView.setHasFixedSize(true);
        // final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        //llm.setOrientation(LinearLayoutManager.VERTICAL);
        //recyclerView.setLayoutManager(llm);
        //recyclerView.setAdapter(new DetailAttenAdapter(ld));
        return view;
    }
}
