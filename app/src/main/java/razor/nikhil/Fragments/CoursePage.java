package razor.nikhil.Fragments;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;

import razor.nikhil.Activity.MainActivity;
import razor.nikhil.Http.Http;
import razor.nikhil.Http.PostParent;
import razor.nikhil.Listener.RecyclerItemClickListener;
import razor.nikhil.R;
import razor.nikhil.model.FacMsgModel;
import razor.nikhil.model.Model_Slots;

/**
 * Created by Nikhil Verma on 10/2/2015.
 */
public class CoursePage extends Fragment {
    private RecyclerView recyclerView;
    private static HttpClient httpClient;
    private static String CoursePage = "https://academics.vit.ac.in/student/coursepage_view.asp?sem=";
    private static String CoursePagePost = "https://academics.vit.ac.in/student/coursepage_view3.asp";
    private String SEM;
    private List<Model_Slots> subs;

    public static CoursePage newInstance() {
        CoursePage fragment = new CoursePage();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        try {
            httpClient = GetDetails.getThreadSafeClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SEM = PostParent.getSem();
        subs = MainActivity.list;
        CoursePage += (SEM + "&crs=CSE327&slt=B1+TB1&fac=");
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
        Subjects adap = new Subjects();
        recyclerView.setAdapter(adap);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
            }
        }));
    }

    private void mysync() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Http.getData("https://academics.vit.ac.in/student/stud_home.asp", httpClient);//pre
                final String DATA = Http.getData(CoursePage, httpClient);
                Elements tRs = Jsoup.parse(DATA).getElementsByTag("table")
                        .get(2).getElementsByTag("tr");
                tRs.remove(0);//headers
                Log.d("Data", DATA);
                for (Element el : tRs)
                    try {
                        Element td = el.getElementsByTag("td").last();
                        Elements inputs = td.getElementsByTag("input");
                        String sem = inputs.get(0).attr("value").trim();
                        String plancode = inputs.get(1).attr("value").trim();
                        HashMap<String, String> map = new HashMap<>();
                        map.put("sem", sem);
                        map.put("crsplancode", plancode);
                        map.put("crpnvwcmd", "View");
                        Log.d("SEM+Cplan", sem + "\t" + plancode);
                        String DAT = Http.postMethod(CoursePagePost, map, httpClient);//correct Data
                        parseCP(DAT);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                return null;
            }

        }.execute();
    }

    private void parseCP(String source) {
        Element TextRefMat = Jsoup.parse(source).getElementsByTag("table").get(2);//Text/Reference Material
        //      Element syllabus
    }


    private void calLFrag(List<FacMsgModel> list) {
        //    button.setEnabled(true);
        //    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container_main, ).commit();
    }

    private class Subjects extends RecyclerView.Adapter<Subjects.ViewHolder> {
        private Typeface regular;

        public Subjects() {
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
                holder.tname.setText(subs.get(position).getSubject_name());
                holder.myt_sub.setText(subs.get(position).getCode());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return subs.size();
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
