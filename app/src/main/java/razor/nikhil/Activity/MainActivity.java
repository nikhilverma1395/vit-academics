package razor.nikhil.Activity;

/**
 * Created by Nikhil Verma on 9/15/2015.
 */

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import razor.nikhil.Fragments.FacultyAdvFrag;
import razor.nikhil.Fragments.GradeFragment;
import razor.nikhil.Fragments.LeaveRequest;
import razor.nikhil.Fragments.Slots;
import razor.nikhil.Fragments.StudentLogin;
import razor.nikhil.Fragments.TimeTableVP;
import razor.nikhil.Listener.RecyclerItemClickListener;
import razor.nikhil.R;
import razor.nikhil.database.Attend_GetSet;
import razor.nikhil.database.CBL_Get_Set;
import razor.nikhil.database.GradeGetSet;
import razor.nikhil.database.IndivAttGetSet;
import razor.nikhil.database.MTWTHgetset;
import razor.nikhil.database.PBL_Get_Set;
import razor.nikhil.database.SharedPrefs;
import razor.nikhil.database.Slots_GetSet;
import razor.nikhil.model.AttendBrief;
import razor.nikhil.model.DetailAtten;
import razor.nikhil.model.GradeModel;
import razor.nikhil.model.Marks_Model;
import razor.nikhil.model.Model_Daywise;
import razor.nikhil.model.Model_Slots;
import razor.nikhil.model.PBL_Model;
import razor.nikhil.model.detailattlist_subcode;

public class MainActivity extends ActionBarActivity {


    String TITLES[] = {"Courses", "Photo Login", "Details", "TimeTable", "Grades", "Faculty Adviser", "Leave", "Free Play", "Open Source"};
    int ICONS[] = {R.mipmap.user_icon,
            R.mipmap.assignment, -
            R.mipmap.tick,
            R.mipmap.book,
            R.mipmap.ic_dns,
            R.mipmap.bug_report,
            R.mipmap.explore,
            R.mipmap.ic_event,
            R.mipmap.ic_language};
    public static List<Model_Slots> list;
    private static GetDetails gd;
    public static List<Model_Daywise> todayslist_m;
    public static List<Model_Daywise> todayslist_t;
    public static List<Model_Daywise> todayslist_w;
    public static List<Model_Daywise> todayslist_th;
    public static List<Model_Daywise> todayslist_fr;
    public static List<detailattlist_subcode> detail_att_all = new ArrayList<>();
    public static HashMap<String, List<DetailAtten>> hash = new HashMap<>();
    public static List<AttendBrief> attendBriefs = null;
    public static List<Marks_Model> marks_det;
    public static List<PBL_Model> lpbl;

    String NAME = "Mike Ross";
    String EMAIL = "mike@gmail.com";
    int PROFILE = R.drawable.head;
    public static Context context;
    private Toolbar toolbar;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;

    ActionBarDrawerToggle mDrawerToggle;
    public static List<GradeModel> grades;


    @Override
    public void onBackPressed() {
        if (gd.isAdded())
            getSupportFragmentManager()
                    .beginTransaction().remove(gd).commit();
        else super.onBackPressed();

    }

    public void setMTWTFLists(final Context ctxt) {
        String mark = new SharedPrefs(ctxt).getMsg("marksdone").trim();
        String att = new SharedPrefs(ctxt).getMsg("ttdone").trim();
        String tt = new SharedPrefs(ctxt).getMsg("attendone").trim();
        if (mark.equals("y") && att.equals("y") && tt.equals("y")) {
            try {
                new Thread(new Runnable() {
                    public void run() {
                        todayslist_m = new MTWTHgetset(ctxt, "monday").getAllCredentials();
                        todayslist_t = new MTWTHgetset(ctxt, "tuesday").getAllCredentials();
                        todayslist_w = new MTWTHgetset(ctxt, "wednesday").getAllCredentials();
                        todayslist_th = new MTWTHgetset(ctxt, "thursday").getAllCredentials();
                        todayslist_fr = new MTWTHgetset(ctxt, "friday").getAllCredentials();
                    }
                }).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mai);
        context = getApplicationContext();
        gd = new GetDetails(gd);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container_main, gd).commit();
        }
        try {
            //Getting the list of slots
            list = new Slots_GetSet(this).getAllCredentials();
            String classnbrs[] = new String[list.size()];
            int r = 0;
            //getting the name of individual attendance tables
            for (Model_Slots ms : list) {
                classnbrs[r++] = ms.getNumber().trim();
            }
            //getting lists of detailed att of individual subjects
            for (int t = 0; t < classnbrs.length; t++) {
                detailattlist_subcode dr = new detailattlist_subcode();
                hash.put(classnbrs[t], new IndivAttGetSet(this, " table_of_" + classnbrs[t]).getAllCredentials());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Runnable runnable = new Runnable() {
                public void run() {
                    grades = new GradeGetSet(context).getAllCredentials();
                }
            };
            new Thread(runnable).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            attendBriefs = new Attend_GetSet(this).getAllCredentials();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            marks_det = new CBL_Get_Set(this).getAllCredentials();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            lpbl = new PBL_Get_Set(this).getAllCredentials();
        } catch (Exception e) {
            e.printStackTrace();
            lpbl = new ArrayList<>();
        }
        //Set the MTWTF lists here, so that they aren't read from database every-time on changes fragment
        try {
            setMTWTFLists(getApplication());
        } catch (Exception e) {
            e.printStackTrace();
        }

    toolbar = (Toolbar) findViewById(R.id.toolbar_layout_main);
     setSupportActionBar(toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new MyAdapter(TITLES, ICONS, NAME, EMAIL, PROFILE);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(recycleritemlict);
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }


        };
        Drawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

    }

    RecyclerItemClickListener recycleritemlict = new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            displayView(position);
            if (position == 4)
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Drawer.closeDrawers();
                    }
                }, 120);
            else Drawer.closeDrawers();

        }
    });

    private void displayView(int position) {
        Fragment fragment = null;
        Log.d("pos", position + "");
        switch (position) {
            case 1:
                String s = getSharedPreferences("VitAcademics_sp", Context.MODE_PRIVATE).getString("slots_down", "no");
                if (s.equals("already")) {
                    fragment = new Slots();
                } else if (s.equals("yes")) {
                    list = new Slots_GetSet(getApplicationContext()).getAllCredentials();
                    fragment = new Slots();
                } else if (s.equals("no")) {
                    fragment = new GetDetails();
                } else {
                    return;
                }
                break;
            case 2:
                fragment = new StudentLogin();
                break;
            case 3:
                fragment = new GetDetails();
                break;
            case 4:
                fragment = TimeTableVP.newInstance();
                break;
            case 5:
                fragment = GradeFragment.newInstance(grades);
                break;
            case 6:
                fragment = FacultyAdvFrag.newInstance();
                break;
            case 7:
                fragment = new LeaveRequest();
                break;
            default:
                displayView(1);
                break;
        }
        if (fragment != null) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.container_main, fragment).commit();
        } else {
            Log.e("DrawerActivity", "Error creating fragment");
        }
    }

}