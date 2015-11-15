package razor.nikhil.Fragments;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.Activity.MainActivity;
import razor.nikhil.Http.Http;
import razor.nikhil.Http.PostParent;
import razor.nikhil.Listener.RecyclerItemClickListener;
import razor.nikhil.R;
import razor.nikhil.model.CoursePageModel;
import razor.nikhil.model.FacMsgModel;
import razor.nikhil.model.Model_Slots;

/**
 * Created by Nikhil Verma on 10/2/2015.
 */
public class CoursePage extends Fragment implements StudentLoggerDialog.WhenLoggedIn {
    private RecyclerView recyclerView;
    private static HttpClient httpClient;
    private static String coursepage = "https://academics.vit.ac.in/student/coursepage_view.asp?sem=";
    private static String CoursePagePost = "https://academics.vit.ac.in/student/coursepage_view3.asp";
    private String SEM;
    private List<Model_Slots> subs;
    ProgressDialog dialog;

    public static CoursePage newInstance() {
        CoursePage fragment = new CoursePage();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!((MainActivity) getActivity()).toolbarIsShown())
            ((MainActivity) getActivity()).showToolbar();
        try {
            httpClient = GetDetails.getThreadSafeClient();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SEM = PostParent.getSem();
        subs = MainActivity.list;
        for (int y = 0; y < subs.size(); y++) {
            if (subs.get(y).getCourse_type().toLowerCase().contains("lab"))
                subs.remove(y);
        }
        coursepage += SEM + "&crs="; //+ "&slt=&fac="
        return inflater.inflate(R.layout.rv_mult_teacher, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (subs == null) {
            Toast.makeText(getActivity(), "Subject Data Not Available", Toast.LENGTH_SHORT).show();
            return;
        }
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
            public void onItemClick(View view, int pos) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                StudentLoggerDialog editNameDialog = StudentLoggerDialog.newInstance(CoursePage.this);
                editNameDialog.show(fm, "B");
                coursepage += subs.get(pos).getCode() + "&slt=&fac=";
            }
        }));
    }

    private void mysync() {
        dialog = ProgressDialog.show(getActivity(), "Wait", "Getting Subject Data..", true);
        new AsyncTask<Void, Void, List<CoursePageModel>>() {
            @Override
            protected List<CoursePageModel> doInBackground(Void... params) {
                Http.getData("https://academics.vit.ac.in/student/stud_home.asp", httpClient);//pre
                final String data = Http.getData(coursepage, httpClient);
                Elements trs = Jsoup.parse(data).getElementsByTag("table")
                        .get(2).getElementsByTag("tr");
                trs.remove(0);//headers
                List<CoursePageModel> list = new ArrayList<>();
                for (Element el : trs)
                    try {
                        CoursePageModel model = new CoursePageModel();
                        model.setType(el.getElementsByTag("td").get(2).html().trim());
                        model.setTeacher(el.getElementsByTag("td").get(3).html().trim());
                        model.setSlot(el.getElementsByTag("td").get(5).html().trim());
                        Element td = el.getElementsByTag("td").last();
                        Elements inputs = td.getElementsByTag("input");
                        String crsplancode = inputs.get(1).attr("value").trim();
                        model.setCrsplancode(crsplancode);
                        list.add(model);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                return list;
            }

            @Override
            protected void onPostExecute(List<CoursePageModel> coursePageModels) {
                super.onPostExecute(coursePageModels);
                dialog.dismiss();
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container_main, SyllabusVersionFragment.newInstance(coursePageModels)).addToBackStack(null).commit();
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

    @Override
    public void Success(HttpClient client) {
        httpClient = client;
        mysync();
    }


    @Override
    public void Error(String message) {

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
