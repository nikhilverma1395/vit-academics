package razor.nikhil.Fragments;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import java.util.List;

import razor.nikhil.Fragments.FacInfo.FacInfoFrag;
import razor.nikhil.Fragments.FacInfo.GetFacDataStudLogin;
import razor.nikhil.Listener.RecyclerItemClickListener;
import razor.nikhil.R;

/**
 * Created by Nikhil Verma on 9/29/2015.
 */
public class MultipleTeacherListFrag extends Fragment {
    private List<GetFacDataStudLogin.MutlipleResultFac> lists;
    private RecyclerView recyclerView;
    private Typeface regular, light;
    private ProgressBarCircularIndeterminate progress;

    public MultipleTeacherListFrag() {
    }

    public MultipleTeacherListFrag(List<GetFacDataStudLogin.MutlipleResultFac> list) {
        this.lists = list;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rv_mult_teacher, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_multi_teach);
        progress = (ProgressBarCircularIndeterminate) view.findViewById(R.id.progress_multi_teach);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        MultipleTeachListAdap adap = new MultipleTeachListAdap();
        recyclerView.setAdapter(adap);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, final int position) {
                new AsyncTask<Void, GetFacDataStudLogin.bmparr, GetFacDataStudLogin.bmparr>() {
                    @Override
                    protected void onPreExecute() {
                        super.onPreExecute();
                        progress.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    protected GetFacDataStudLogin.bmparr doInBackground(Void... params) {
                        return GetFacDataStudLogin.parsefacidlink(lists.get(position).href);
                    }

                    @Override
                    protected void onPostExecute(GetFacDataStudLogin.bmparr bmp) {
                        super.onPostExecute(bmp);
                        try {

                            progress.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_left_in, R.anim.push_left_out)
                                    .add(R.id.fac_info_getdata_frame, new FacInfoFrag(bmp))
                                    .addToBackStack(null)
                                    .commit();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.execute();

            }
        }));
    }

    private class MultipleTeachListAdap extends RecyclerView.Adapter<MultipleTeacherListFrag.MultipleTeachListAdap.ViewHolder> {

        public MultipleTeachListAdap() {
            light = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
            regular = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        }

        @Override
        public MultipleTeacherListFrag.MultipleTeachListAdap.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.multiple_teach_item, parent, false);
            return (new ViewHolder(itemView));
        }

        @Override
        public void onBindViewHolder(MultipleTeacherListFrag.MultipleTeachListAdap.ViewHolder holder, int position) {
            holder.school.setText(lists.get(position).school);
            holder.name.setText(lists.get(position).name);
        }

        @Override
        public int getItemCount() {
            return lists.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, school, href;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.name_mult_fac);
                school = (TextView) itemView.findViewById(R.id.school_mult_fac);
                name.setTypeface(regular);
                school.setTypeface(light);
            }
        }

    }
}
