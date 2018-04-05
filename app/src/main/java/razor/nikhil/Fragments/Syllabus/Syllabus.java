package razor.nikhil.Fragments.Syllabus;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.Activity.MainActivity;
import razor.nikhil.Fragments.GetDetails;
import razor.nikhil.Fragments.StudentLoggerDialog;
import razor.nikhil.Http.Http;
import razor.nikhil.Listener.HidingScrollListener;
import razor.nikhil.Listener.RecyclerItemClickListener;
import razor.nikhil.R;
import razor.nikhil.adapter.AllSubjectsAdapter;
import razor.nikhil.model.SyllabusCourseItem;
import razor.nikhil.model.SyllabusSubjectVersion;


/*
 * Created by Nikhil Verma on 10/13/2015.
 */
public class Syllabus extends Fragment implements SearchView.OnQueryTextListener, StudentLoggerDialog.WhenLoggedIn {
    private RecyclerView recyclerView;
    private ImageButton imageButton;
    private ProgressDialog dialog;
    private static HttpClient httpClient;
    private boolean isLoggedIn = false;
    private AllSubjectsAdapter adapter;
    public static ArrayList<SyllabusCourseItem> names;
    private TextView no_data_rv_syll;

    public static HttpClient getHttpClient() {
        return httpClient;
    }

    public static void setHttpClient(HttpClient httpClient) {
        Syllabus.httpClient = httpClient;
    }

    public Syllabus() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        httpClient = GetDetails.getThreadSafeClient();
        names = new ArrayList<>();
        return inflater.inflate(R.layout.rv_mult_teacher, container, false);
    }

    @Override
    public void onViewCreated(View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(v, savedInstanceState);
        setHasOptionsMenu(false);
        imageButton = (ImageButton) v.findViewById(R.id.refresh_ib_);
        no_data_rv_syll = (TextView) v.findViewById(R.id.no_data_rv_syll);
        recyclerView = (RecyclerView) v.findViewById(R.id.recyclerView_multi_teach);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setVisibility(View.GONE);
        imageButton.setVisibility(View.VISIBLE);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                StudentLoggerDialog editNameDialog = StudentLoggerDialog.newInstance(Syllabus.this);
                editNameDialog.show(fm, "B");
                animateImage(imageButton);
            }
        });
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                doTheThing(view, position);
            }
        }));
    }

    ProgressDialog dia;

    private void doTheThing(View view, int position) {
        dia = ProgressDialog.show(getActivity(), "Wait", "Getting Syllabus Versions..", true);
        new GetSubjectSyllabusVersions().execute(adapter.getList().get(position));
    }

    private void animateImage(ImageButton imageButton) {
        imageButton.clearAnimation();
        RotateAnimation anim = new RotateAnimation(0, 360, imageButton.getWidth() / 2, imageButton.getHeight() / 2);
        anim.setFillAfter(true);
        anim.setRepeatCount(10);
        anim.setDuration(2300);
        imageButton.startAnimation(anim);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mymenusearch, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);
    }


    public static List<SyllabusCourseItem> filter(List<SyllabusCourseItem> models, String query) {
        query = query.toLowerCase();
        List<SyllabusCourseItem> filteredModelList = new ArrayList<>();
        for (SyllabusCourseItem model : models) {
            String text = model.getSUBNAME().toLowerCase();
            String text1 = model.getSUBCODE().toLowerCase();
            if (text.contains(query) || text1.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        List<SyllabusCourseItem> liy = filter(names, query);
        if (liy.size() != 0) {
            if (recyclerView.getVisibility() == View.INVISIBLE) {
                recyclerView.setVisibility(View.VISIBLE);
                no_data_rv_syll.setVisibility(View.INVISIBLE);
            }
            adapter.animateTo(liy);
            recyclerView.scrollToPosition(0);
        } else {
            recyclerView.setVisibility(View.INVISIBLE);
            no_data_rv_syll.setVisibility(View.VISIBLE);
        }
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public void Success(HttpClient client) {
        httpClient = client;
        new GetSubjects().execute();
    }

    @Override
    public void Error(String message) {

    }

    public class GetSubjects extends AsyncTask<Void, Void, List<SyllabusCourseItem>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(getActivity(), "Wait", "Loading Subjects", true);
        }

        @Override
        protected ArrayList<SyllabusCourseItem> doInBackground(Void... params) {
            Http.getData("https://academics.vit.ac.in/student/stud_home.asp", httpClient);//need this as a pre-req
            String data = Http.getData("https://academics.vit.ac.in/student/syllabus_view.asp", httpClient);
            return parseData(data);
        }

        @Override
        protected void onPostExecute(List<SyllabusCourseItem> DATA) {
            super.onPostExecute(DATA);
            names.addAll(DATA);
            adapter = new AllSubjectsAdapter(DATA);
            dialog.dismiss();
            imageButton.clearAnimation();
            imageButton.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
            setHasOptionsMenu(true);
            recyclerView.setAdapter(adapter);
            recyclerView.setOnScrollListener(new HidingScrollListener(getActivity()) {
                MainActivity activity = ((MainActivity) getActivity());

                @Override
                public void onMoved(int distance) {
                    activity.onMoved(distance);
                }

                @Override
                public void onShow() {
                    if (activity.toolbarIsHidden())
                        activity.showToolbar();
                }

                @Override
                public void onHide() {
                    if (activity.toolbarIsShown())
                        activity.hideToolbar();
                }

            });
        }
    }

    public static ArrayList<SyllabusCourseItem> parseData(String data) {
        Element doc = Jsoup.parse(data).getElementsByTag("table").get(1);
        Elements trS = doc.getElementsByTag("tr");
        trS.remove(0);
        Elements tdS = trS.get(0).getElementsByTag("td");
        tdS.remove(0);
        Elements optionS = tdS.get(0).getElementsByTag("option");
        optionS.remove(0);
        ArrayList<SyllabusCourseItem> list = new ArrayList<>();
        for (Element dot : optionS) {
            SyllabusCourseItem model = new SyllabusCourseItem();
            model.setSUBCODE(dot.attr("value").trim());
            model.setSUBNAME(dot.html().trim());
            list.add(model);
        }
        return list;
    }


    private class GetSubjectSyllabusVersions extends AsyncTask<SyllabusCourseItem, Void, List<SyllabusSubjectVersion>> {
        SyllabusCourseItem item;

        @Override
        protected List<SyllabusSubjectVersion> doInBackground(SyllabusCourseItem... params) {
            String dot = Http.getData("https://academics.vit.ac.in/student/syllabus_view.asp?shby=0&crcd=" + params[0].getSUBCODE(), httpClient);
            Elements versions = Jsoup.parse(dot).getElementsByTag("table").get(2).getElementsByTag("tr");
            item = params[0];
            versions.remove(0);
            List<SyllabusSubjectVersion> list = getData(versions);
            return list;
        }

        @Override
        protected void onPostExecute(List<SyllabusSubjectVersion> syllabusSubjectVersions) {
            super.onPostExecute(syllabusSubjectVersions);
            dia.dismiss();
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container_main, SyllabusVersionFragment.newInstance(syllabusSubjectVersions, item)).addToBackStack(null).commit();
        }
    }

    private List<SyllabusSubjectVersion> getData(Elements versions) {
        List<SyllabusSubjectVersion> list = new ArrayList<>();
        for (Element mod : versions) {
            SyllabusSubjectVersion miz = new SyllabusSubjectVersion();
            Elements INPUT = mod.getElementsByTag("input");
            miz.setAcademicCounsel(mod.getElementsByTag("td").get(5).getElementsByTag("font").get(0).html());
            miz.setType_POST(INPUT.get(1).attr("value"));
            miz.setVersion_POST(INPUT.get(2).attr("value"));
            list.add(miz);
        }
        return list;
    }
}
