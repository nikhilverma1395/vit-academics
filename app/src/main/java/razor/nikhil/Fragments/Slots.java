package razor.nikhil.Fragments;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.materialdesign.widgets.SnackBar;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import razor.nikhil.Activity.MainActivity;
import razor.nikhil.Http.AttendanceParser;
import razor.nikhil.Http.FullAttendParseStore;
import razor.nikhil.Http.Http;
import razor.nikhil.Http.MarksParser;
import razor.nikhil.Http.ParseTimeTable;
import razor.nikhil.Http.PostParent;
import razor.nikhil.Listener.RecyclerItemClickListener;
import razor.nikhil.R;
import razor.nikhil.adapter.CoursesAdapter;
import razor.nikhil.database.Attend_GetSet;
import razor.nikhil.database.CBL_Get_Set;
import razor.nikhil.database.IndivAttGetSet;
import razor.nikhil.database.PBL_Get_Set;
import razor.nikhil.database.Slots_GetSet;
import razor.nikhil.model.AttendBrief;
import razor.nikhil.model.DetailAtten;
import razor.nikhil.model.Marks_Model;
import razor.nikhil.model.Model_Slots;
import razor.nikhil.model.PBL_Model;
import razor.nikhil.model.Query_TableName;
import razor.nikhil.model.detailattlist_subcode;


/**
 * Created by Nikhil Verma on 7/26/2015.
 */
public class Slots extends Fragment implements ObservableScrollViewCallbacks, StudentLoggerDialog.WhenLoggedIn {
    private static Context context;
    private List<Model_Slots> lists;
    private ObservableRecyclerView recyclerView;
    private List<AttendBrief> attendBriefs = null;
    private List<Marks_Model> marks_det;
    private List<PBL_Model> lpbl;
    private HashMap<String, List<DetailAtten>> hash = new HashMap<>();
    public static SwipeRefreshLayout swipeContainer;
    private static HttpClient httpClient;
    private boolean isLoggedIn = false;
    private Thread[] threads = new Thread[3];
    private CoursesAdapter adapter;
    private String[] classnbrs;
    public static List<Query_TableName> sqltn = new ArrayList<>();

    public Slots() {
        lists = MainActivity.list;
        attendBriefs = MainActivity.attendBriefs;
        marks_det = MainActivity.marks_det;
        lpbl = MainActivity.lpbl;
        hash = MainActivity.hash;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MainActivity activity = ((MainActivity) getActivity());
        if (activity.toolbarIsHidden()) {
            activity.showToolbar();
        }
        int Color[] = getActivity().getResources().getIntArray(R.array.reds);
        activity.setStatusBarColor(Color[0]);
        activity.setToolBarColor(Color[7]);

        httpClient = GetDetails.getThreadSafeClient();
        context = getActivity();
        final View v = inflater.inflate(R.layout.recycler, container, false);
        setRv(v);
        adapter = new CoursesAdapter(lists, attendBriefs, context);
        onClickListenLogic();
        recyclerView.setScrollViewCallbacks(this);
        recyclerView.setAdapter(adapter);
        return v;
    }

    private void onClickListenLogic() {
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
                                    getActivity()
                                            .getSupportFragmentManager().beginTransaction()
                                            .setCustomAnimations(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom,
                                                    R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom)
                                            .replace(R.id.recylerview_, new MarksView(pe, laDetailAttens))
                                            .addToBackStack(null)
                                            .commit();
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

                                getActivity()
                                        .getSupportFragmentManager()
                                        .beginTransaction()
                                        .setCustomAnimations(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom,
                                                R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom)

                                        .replace(R.id.recylerview_, new MarksView(marks_det.get(position), laDetailAttens))
                                        .addToBackStack(null).commit();
                            } else {
                                for (int er = 0; er < marks_det.size(); er++) {
                                    if (marks_det.get(er).getClassnbr().trim().equals(classnbr)) {
                                        List<DetailAtten> laDetailAttens;
                                        laDetailAttens = hash.get(classnbr);
                                        getActivity()
                                                .getSupportFragmentManager()
                                                .beginTransaction()
                                                .setCustomAnimations(R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom,
                                                        R.anim.abc_grow_fade_in_from_bottom, R.anim.abc_shrink_fade_out_from_bottom)
                                                .replace(R.id.recylerview_, new MarksView(marks_det.get(er), laDetailAttens))
                                                .addToBackStack(null).commit();
                                    }
                                }

                            }
                        }
                    }
                })
        );

    }

    private void setRv(View v) {
        recyclerView = (ObservableRecyclerView) v.findViewById(R.id.card_list);
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        swipeContainer.setProgressViewOffset(false, 0, 320);//works :)
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                doTheShit();
            }
        });
        swipeContainer.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

    }

    StudentLoggerDialog editNameDialog;

    private void doTheShit() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        editNameDialog = StudentLoggerDialog.newInstance(Slots.this);
        editNameDialog.show(fm, "heck");
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {

    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        MainActivity act = (MainActivity) getActivity();
        if (scrollState == ScrollState.UP) {
            if (act.toolbarIsShown()) {
                act.hideToolbar();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (act.toolbarIsHidden()) {
                act.showToolbar();
            }
        }
    }

    @Override
    public void Success(HttpClient client) {
        httpClient = client;
        new Sync().execute();
    }

    private void callTheUpdateMethods(final HttpClient client) {
        try {
            Runnable runnable = new Runnable() {
                public void run() {
                    String timetableHTML = Http.getData(PostParent.TIMETABLE_URL + (PostParent.getSem().toLowerCase()).toLowerCase() + ".asp", client);
                    new ParseTimeTable(context, client).parser(timetableHTML);
                }
            };
            threads[0] = new Thread(runnable);
            threads[0].start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //
        try {
            threads[1] = new Thread(new Runnable() {
                public void run() {
                    String dataq = Http.getData("https://academics.vit.ac.in/student/attn_report.asp?sem=" + PostParent.getSem().toUpperCase(), client);
                    String from_date = Jsoup.parse(dataq).getElementsByTag("table").get(1)
                            .getElementsByTag("tr").get(2).getElementsByTag("input").get(0).attr("value");
                    final String attendanceHTML = Http.getData(PostParent.ATTENDANCE_URL + PostParent.getSem() + "&fmdt=" + from_date + "&todt=" + PostParent.getTodayDateInFormat(), client);
                    try {
                        new AttendanceParser(context, client).parse(attendanceHTML);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            threads[1].start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            threads[2] = new Thread(new Runnable() {
                public void run() {
                    final String marksHTML = Http.getData(PostParent.MARKS_URL + PostParent.getSem(), client);
                    try {
                        new MarksParser(context, marksHTML);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            threads[2].start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Thread th : threads) {
            try {
                th.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        threads = null;
        threads = new Thread[2];

        lists = null;
        Runnable r1 = new Runnable() {
            public void run() {
                lists = new Slots_GetSet(context).getAllCredentials();
                classnbrs = new String[lists.size()];
                for (int y = 0; y < lists.size(); y++) {
                    Model_Slots ms = lists.get(y);
                    classnbrs[y] = ms.getNumber().trim();
                    sqltn.add(new Query_TableName(FullAttendParseStore.getFullAttTableName(classnbrs[y]), classnbrs[y]));
                }
                hash = new HashMap<>();
                for (int t = 0; t < classnbrs.length; t++) {
                    detailattlist_subcode dr = new detailattlist_subcode();
                    hash.put(classnbrs[t], new IndivAttGetSet(getActivity(), " table_of_" + classnbrs[t]).getAllCredentials());
                }
            }
        };
        threads[0] = new Thread(r1);
        threads[0].start();
        Runnable r2 = new Runnable() {
            public void run() {
                marks_det = new CBL_Get_Set(context).getAllCredentials();
                lpbl = new PBL_Get_Set(context).getAllCredentials();
            }
        };
        threads[1] = new Thread(r2);
        threads[1].start();
        for (Thread read : threads) {
            try {
                read.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void Error(String message) {
    }

    private class Sync extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            Http.getData("https://academics.vit.ac.in/student/stud_home.asp", httpClient);//pre for most
            callTheUpdateMethods(httpClient);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.clear();
            adapter.addAll(lists, new Attend_GetSet(context).getAllCredentials());
            swipeContainer.setRefreshing(false);
            SnackBar snackBar = new SnackBar(getActivity(), "Data Updated Successfully!");
            snackBar.show();
        }
    }
}
