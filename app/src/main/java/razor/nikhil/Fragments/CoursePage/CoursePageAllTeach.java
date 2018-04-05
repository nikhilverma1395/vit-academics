package razor.nikhil.Fragments.CoursePage;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import razor.nikhil.Listener.RecyclerItemClickListener;
import razor.nikhil.R;

/**
 * Created by Nikhil Verma on 10/11/2015.
 */
public class CoursePageAllTeach extends Fragment {
    private RecyclerView recyclerView;

    public static CoursePageAllTeach newInstance() {
        CoursePageAllTeach fragment = new CoursePageAllTeach();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rv_mult_teacher, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

    }

    private void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_multi_teach);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        CourseTall adap = new CourseTall();
        recyclerView.setAdapter(adap);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        }));
    }

    private class CourseTall extends RecyclerView.Adapter<CourseTall.ViewHolder> {
        Typeface regular;

        public CourseTall() {
            regular = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myteachrow, parent, false);
            return (new ViewHolder(itemView));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            try {
                //      holder.tname.setText(subs.get(position).getSubject_name());
                //    holder.myt_sub.setText(subs.get(position).getCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return 0;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView tname, myt_sub;

            public ViewHolder(View v) {
                super(v);
                tname = (TextView) v.findViewById(R.id.myt_name);
                myt_sub = (TextView) v.findViewById(R.id.myt_sub);
                tname.setTypeface(regular);
            }
        }
    }
}
