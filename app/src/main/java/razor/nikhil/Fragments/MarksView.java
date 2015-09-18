package razor.nikhil.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.R;
import razor.nikhil.adapter.DetailAttenAdapter;
import razor.nikhil.model.DetailAtten;
import razor.nikhil.model.Marks_Model;
import razor.nikhil.model.PBL_Model;

/**
 * Created by Nikhil Verma on 7/28/2015.
 */
public class MarksView extends Fragment {
    private TextView marks_main;
    private Marks_Model modele;
    private PBL_Model sds = null;
    private RecyclerView recyclerView;

    private List<DetailAtten> ld = new ArrayList<>();

    public MarksView(PBL_Model sd, List<DetailAtten> lds) {
        this.ld = lds;
        sds = sd;
    }

    public MarksView() {
    }

    public MarksView(Marks_Model model, List<DetailAtten> ld) {
        this.ld = ld;
        modele = model;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.marks_det, container, false);
        marks_main = (TextView) view.findViewById(R.id.marks_main);
        recyclerView = (RecyclerView) view.findViewById(R.id.detail_att_list);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setAdapter(new DetailAttenAdapter(ld));
        if (sds == null) {
            marks_main.setText("\t" + modele.getCAT1() + "\t" + modele.getCAT2() + "\t" + modele.getQUIZ1() + "\t" + modele.getQUIZ2() + "\t"
                    + modele.getQUIZ3() + "\t" + modele.getASIIGN() + "\t" + modele.getLABCAM());
        } else {
            marks_main.setText(sds.getOption1_Title() + "\t" + sds.getOption1_Scored() + "\t\n" +
                            sds.getOption2_Title() + "\t" + sds.getOption2_Scored() + "\t\n"
                            + sds.getOption3_Title() + "\t" + sds.getOption3_Scored() + "\t\n"
                            + sds.getOption4_Title() + "\t" + sds.getOption4_Scored() + "\t\n"
                            + sds.getOption5_Title() + "\t" + sds.getOption5_Scored() + "\t\n"
            );
        }
        return view;
    }
}
