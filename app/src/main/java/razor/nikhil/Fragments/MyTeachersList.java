package razor.nikhil.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.Listener.RecyclerItemClickListener;
import razor.nikhil.R;
import razor.nikhil.adapter.MyTeacherAdap;
import razor.nikhil.model.MyTeacherDet;

/**
 * Created by Nikhil Verma on 9/27/2015.
 */
public class MyTeachersList extends Fragment {
    private RecyclerView recyclerView;
    private List<MyTeacherDet> list = new ArrayList<>();

    public MyTeachersList(List<MyTeacherDet> list) {
        this.list = list;
    }

    public MyTeachersList() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_marks_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_minfo);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        Log.d("list", list.size() + "");
        MyTeacherAdap adap = new MyTeacherAdap(getActivity(), list);
        recyclerView.setAdapter(adap);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .setCustomAnimations(R.anim.abc_popup_enter, R.anim.abc_popup_exit
                                , R.anim.abc_popup_enter, R.anim.abc_popup_exit)
                        .add(R.id.container_main, new FacInfoFrag(list.get(position)))
                        .addToBackStack(null).commit();
            }
        }));
    }
}
