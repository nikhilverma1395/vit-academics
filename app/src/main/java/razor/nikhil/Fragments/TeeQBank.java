package razor.nikhil.Fragments;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.Activity.MainActivity;
import razor.nikhil.Fragments.Syllabus.Syllabus;
import razor.nikhil.Fragments.Syllabus.SyllabusVersionFragment;
import razor.nikhil.Http.Http;
import razor.nikhil.Listener.HidingScrollListener;
import razor.nikhil.Listener.RecyclerItemClickListener;
import razor.nikhil.R;
import razor.nikhil.adapter.AllSubjectsAdapter;
import razor.nikhil.model.SyllabusCourseItem;
import razor.nikhil.model.TEEVersionModel;

/**
 * Created by Nikhil Verma on 11/14/2015.
 */
public class TeeQBank extends Fragment implements StudentLoggerDialog.WhenLoggedIn, SearchView.OnQueryTextListener {
    private RecyclerView recyclerView;
    private ImageButton imageButton;
    private ProgressDialog dialog;
    private static HttpClient httpClient;
    private TextView no_data_rv_syll;
    private ArrayList<SyllabusCourseItem> names;
    private AllSubjectsAdapter adapter;

    public TeeQBank() {
    }

    public static HttpClient getHttpClient() {
        return httpClient;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        names = new ArrayList<>();
        httpClient = GetDetails.getThreadSafeClient();
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
                StudentLoggerDialog editNameDialog = StudentLoggerDialog.newInstance(TeeQBank.this);
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

    private void doTheThing(View view, int position) {
        dia = ProgressDialog.show(getActivity(), "Wait", "Getting Paper Versions..", true);
        new GetTEEPaperVersions().execute(adapter.getList().get(position));

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
        searchView.setOnQueryTextListener(TeeQBank.this);
    }

    @Override
    public void Success(HttpClient client) {
        httpClient = client;
        new GetSubjects().execute();
    }

    @Override
    public void Error(String message) {

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }


    @Override
    public boolean onQueryTextChange(String query) {
        List<SyllabusCourseItem> liy = Syllabus.filter(names, query);
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

    public static Fragment newInstance() {
        return new TeeQBank();
    }

    public class GetSubjects extends AsyncTask<Void, Void, List<SyllabusCourseItem>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(getActivity(), "Wait", "Loading Subjects", true);
        }

        @Override
        protected List<SyllabusCourseItem> doInBackground(Void... params) {
            Http.getData("https://academics.vit.ac.in/student/stud_home.asp", httpClient);//need this as a pre-req
            String data = Http.getData("https://academics.vit.ac.in/student/tee_questionbank_view.asp", httpClient);
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

    private List<SyllabusCourseItem> parseData(String data) {
        List<SyllabusCourseItem> list = new ArrayList<>();
        Elements tdS = Jsoup.parse(data).getElementsByTag("table").get(1).getElementsByTag("tr").get(0).getElementsByTag("td");
        tdS.remove(0);
        Elements options = tdS.get(0).getElementsByTag("option");
        options.remove(0);
        for (Element mod : options) {
            SyllabusCourseItem model = new SyllabusCourseItem();
            model.setSUBCODE(mod.attr("value").trim());
            model.setSUBNAME(mod.html().trim());
            list.add(model);
        }
        return list;
    }

    ProgressDialog dia;

    private class GetTEEPaperVersions extends AsyncTask<SyllabusCourseItem, Void, List<TEEVersionModel>> {
        SyllabusCourseItem item;

        @Override
        protected List<TEEVersionModel> doInBackground(SyllabusCourseItem... params) {
            String dot = Http.getData("https://academics.vit.ac.in/student/tee_questionbank_view.asp?crcd=" + params[0].getSUBCODE(), httpClient);
            Log.d("LINK", "https://academics.vit.ac.in/student/tee_questionbank_view.asp?crcd=" + params[0].getSUBCODE());
            Elements versions = null;
            try {
                versions = Jsoup.parse(dot).getElementsByTag("table").get(2).getElementsByTag("tr");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            item = params[0];
            versions.remove(0);
            //Name - QPID.pdf
            List<TEEVersionModel> list = getData(versions);
            return list;
        }

        @Override
        protected void onPostExecute(List<TEEVersionModel> syllabusSubjectVersions) {
            super.onPostExecute(syllabusSubjectVersions);
            dia.dismiss();
            if (syllabusSubjectVersions == null) {
                Toast.makeText(getActivity(), "Not Available ,Try Again", Toast.LENGTH_SHORT).show();
                return;
            }
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container_main, SyllabusVersionFragment.newInstant(syllabusSubjectVersions, item)).addToBackStack(null).commit();
        }
    }

    private List<TEEVersionModel> getData(Elements versions) {
        List<TEEVersionModel> data = new ArrayList<>();
        for (Element op : versions) {
            TEEVersionModel model = new TEEVersionModel();
            Elements tdS = op.getElementsByTag("td");
            model.setName(tdS.get(2).getElementsByTag("font").get(0).html().trim());
            model.setSem(tdS.get(3).getElementsByTag("font").get(0).html().trim());
            tdS = op.getElementsByTag("input");
            model.setCrscd(tdS.get(0).attr("value"));
            model.setQpid(tdS.get(1).attr("value"));
            data.add(model);
        }
        return data;
    }

}
