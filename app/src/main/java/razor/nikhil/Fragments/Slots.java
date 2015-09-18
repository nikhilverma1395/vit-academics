package razor.nikhil.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import razor.nikhil.Activity.MainActivity;
import razor.nikhil.Listener.RecyclerItemClickListener;
import razor.nikhil.R;
import razor.nikhil.adapter.CoursesAdapter;
import razor.nikhil.model.AttendBrief;
import razor.nikhil.model.DetailAtten;
import razor.nikhil.model.Marks_Model;
import razor.nikhil.model.Model_Slots;
import razor.nikhil.model.PBL_Model;


/**
 * Created by Nikhil Verma on 7/26/2015.
 */
public class Slots extends Fragment {
    private static Context context;
    private List<Model_Slots> lists;
    private RecyclerView recyclerView;
    private List<AttendBrief> attendBriefs = null;
    private List<Marks_Model> marks_det;
    private List<PBL_Model> lpbl;
    private HashMap<String, List<DetailAtten>> hash = new HashMap<>();

    public Slots() {
        lists = MainActivity.list;
        attendBriefs = MainActivity.attendBriefs;
        marks_det = MainActivity.marks_det;
        lpbl = MainActivity.lpbl;
        hash = MainActivity.hash;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.recycler, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.card_list);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        context = getActivity();
        CoursesAdapter ca = new CoursesAdapter(lists, attendBriefs, context);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        boolean isPBL = true;
                        CharSequence classnbr = ((TextView) view.findViewById(R.id.class_number_slots)).getText();
                        try {
                            for (PBL_Model pe : lpbl) {
                                if (pe.getClas_nbr().trim().equals(classnbr.toString().trim())) {
                                    List<DetailAtten> laDetailAttens;
                                    laDetailAttens = hash.get(pe.getClas_nbr().trim());
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                            .replace(R.id.recylerview_, new MarksView(pe, laDetailAttens)).addToBackStack(null).commit();
                                    isPBL = false;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (isPBL) {
                            String d = "";
                            try {
                                d = marks_det.get(position).getClassnbr().trim();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (classnbr.equals(d) && (position != marks_det.size())) {
                                List<DetailAtten> laDetailAttens;
                                laDetailAttens = hash.get(classnbr);

                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.recylerview_, new MarksView(marks_det.get(position), laDetailAttens)).addToBackStack(null).commit();
                            } else {
                                for (int er = 0; er < marks_det.size(); er++) {
                                    if (marks_det.get(er).getClassnbr().trim().equals(classnbr)) {
                                        List<DetailAtten> laDetailAttens;
                                        laDetailAttens = hash.get(classnbr);
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.recylerview_, new MarksView(marks_det.get(er), laDetailAttens)).addToBackStack(null).commit();
                                    }
                                }

                            }
                        }
                    }
                })
        );
        recyclerView.setAdapter(ca);
        return v;
    }
}
