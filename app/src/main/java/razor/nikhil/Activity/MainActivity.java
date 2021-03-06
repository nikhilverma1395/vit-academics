package razor.nikhil.Activity;

/**
 * Created by Nikhil Verma on 9/15/2015.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import razor.nikhil.Fragments.APTAtten.AptAtten;
import razor.nikhil.Fragments.APTAtten.AptAttenList;
import razor.nikhil.Fragments.CGPA.CgpaFragment;
import razor.nikhil.Fragments.CoursePage.CoursePage;
import razor.nikhil.Fragments.FacInfo.FacultyAdvFrag;
import razor.nikhil.Fragments.FacInfo.GetFacDataStudLogin;
import razor.nikhil.Fragments.FacMsg.GetFacMessage;
import razor.nikhil.Fragments.FacMsg.GetFacMsgList;
import razor.nikhil.Fragments.GetDetails;
import razor.nikhil.Fragments.Grade.GradeFragment;
import razor.nikhil.Fragments.Leave.LeavePre;
import razor.nikhil.Fragments.MyTeachers;
import razor.nikhil.Fragments.MyTeachersList;
import razor.nikhil.Fragments.Slots;
import razor.nikhil.Fragments.Syllabus.Syllabus;
import razor.nikhil.Fragments.TeeQBank;
import razor.nikhil.Fragments.TimeTableVP;
import razor.nikhil.Listener.RecyclerItemClickListener;
import razor.nikhil.R;
import razor.nikhil.adapter.NavBarRVAdapter;
import razor.nikhil.database.APT_GS;
import razor.nikhil.database.Attend_GetSet;
import razor.nikhil.database.CBL_Get_Set;
import razor.nikhil.database.FacMsgGS;
import razor.nikhil.database.GradeGetSet;
import razor.nikhil.database.IndivAttGetSet;
import razor.nikhil.database.MTWTHgetset;
import razor.nikhil.database.MyTeachGS;
import razor.nikhil.database.PBL_Get_Set;
import razor.nikhil.database.Slots_GetSet;
import razor.nikhil.gcm.QuickstartPreferences;
import razor.nikhil.gcm.RegistrationIntentService;
import razor.nikhil.model.AttendBrief;
import razor.nikhil.model.DetailAtten;
import razor.nikhil.model.GradeModel;
import razor.nikhil.model.Marks_Model;
import razor.nikhil.model.Model_Daywise;
import razor.nikhil.model.Model_Slots;
import razor.nikhil.model.PBL_Model;
import razor.nikhil.model.detailattlist_subcode;

public class MainActivity extends ActionBarActivity implements NavBarRVAdapter.HeaderItemClicked {

    private String TITLES[] = {"Courses", "Faculty Info", "Enter Details", "TimeTable", "Grades", "Faculty Adviser",
            "Syllabus", "CGPA Calculator", "My Teachers", "Messages", "APT Attendance", "Course Page", "Leave Request Pre", "TEE Papers"};
    private int ICONS[] = {R.mipmap.user_icon,
            R.mipmap.assignment,
            R.mipmap.tick_icon,
            R.mipmap.book,
            R.mipmap.ic_dns,
            R.mipmap.bug_report,
            R.mipmap.explore,
            R.mipmap.ic_event,
            R.mipmap.ic_language,
            R.mipmap.book,
            R.mipmap.ic_dns,
            R.mipmap.bug_report,
            R.mipmap.ic_language,
            R.mipmap.book,
            R.mipmap.ic_dns,
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private BroadcastReceiver mRegistrationBroadcastReceiver;


    public static List<Model_Slots> list;
    public static List<Model_Daywise> todayslist_m = new ArrayList<>();
    public static List<Model_Daywise> todayslist_t = new ArrayList<>();
    public static List<Model_Daywise> todayslist_w = new ArrayList<>();
    public static List<Model_Daywise> todayslist_th = new ArrayList<>();
    public static List<Model_Daywise> todayslist_fr = new ArrayList<>();
    public static HashMap<String, List<DetailAtten>> hash = new HashMap<>();
    public static List<AttendBrief> attendBriefs = null;
    public static List<Marks_Model> marks_det;
    public static List<PBL_Model> lpbl;
    String NAME = "Mike Ross";
    String EMAIL = "mike@gmail.com";
    private List<String> subnames;
    private List<Integer> cur_credits_sum = new ArrayList<>();
    int PROFILE = R.drawable.head;
    private HashMap<String, Integer> mapsubcred = new HashMap<>();
    public static Context context;
    private Toolbar mToolbar;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;

    ActionBarDrawerToggle mDrawerToggle;
    public static List<GradeModel> grades;
    private String teacherNames[];
    private int mToolbarHeight;

    public void setMTWTFLists(final Context ctxt) {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mai);
        context = getApplicationContext();
        setUPGcm();
        try {
            //Getting the list of slots
            list = new Slots_GetSet(this).getAllCredentials();
            String classnbrs[] = new String[list.size()];
            subnames = new ArrayList<>();

            int r = 0;
            //getting the name of individual attendance tables
            //teachernames
            teacherNames = new String[list.size()];
            //
            for (int y = 0; y < list.size(); y++) {
                Model_Slots ms = list.get(y);
                //teachernames
                teacherNames[y] = ms.getTeacher();
                classnbrs[r++] = ms.getNumber().trim();
                String n = ms.getSubject_name().trim();
                if (!(ms.getSlot().trim().toLowerCase().contains("l")))//subnames for gpa calc, ignoring labs
                {
                    subnames.add(n);

                }
                String credstr = ms.getLTPJC().trim();
                int cred = Integer.parseInt(credstr.charAt(credstr.length() - 1) + "");
                if (mapsubcred.get(n) == null) {
                    mapsubcred.put(n, cred);
                } else {
                    mapsubcred.put(n, mapsubcred.get(n) + cred);
                }//adding lab credits
            }
            //getting lists of detailed att of individual subjects
            for (int t = 0; t < classnbrs.length; t++) {
                detailattlist_subcode dr = new detailattlist_subcode();
                hash.put(classnbrs[t], new IndivAttGetSet(this, " table_of_" + classnbrs[t]).getAllCredentials());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (Map.Entry<String, Integer> map : mapsubcred.entrySet()) {
            cur_credits_sum.add(map.getValue());
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

        mToolbar = (Toolbar) findViewById(R.id.toolbar_layout_main);
        setSupportActionBar(mToolbar);
        mToolbarHeight = mToolbar.getHeight();
        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new NavBarRVAdapter(TITLES, ICONS, NAME, EMAIL, PROFILE, this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(recycleritemlict);
        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, mToolbar, R.string.open_drawer, R.string.close_drawer) {

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

    private void setUPGcm() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    Toast.makeText(getApplicationContext(), getString(R.string.gcm_send_message), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.token_error_message), Toast.LENGTH_SHORT).show();
                }
            }
        };
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
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
                fragment = new GetFacDataStudLogin();
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
                fragment = new Syllabus();
                break;
            case 8:
                fragment = CgpaFragment.newInstance(subnames, cur_credits_sum);
                break;
            case 9:
                //    fragment = TabMainDet.newInstance();
                MyTeachGS sql = new MyTeachGS(getApplicationContext());
                try {
                    if (sql.getEntriesCount() == 0)
                        fragment = MyTeachers.newInstance(teacherNames);
                    else {
                        fragment = new MyTeachersList(sql.getAllCredentials());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    displayView(1);
                }

                break;
            case 10:
                FacMsgGS msgGS = new FacMsgGS(getApplicationContext());
                if (msgGS.getEntriesCount() == 0)
                    fragment = GetFacMessage.newInstance();
                else fragment = GetFacMsgList.newInstance(msgGS.getAllCredentials());
                break;
            case 11:
                APT_GS msg = new APT_GS(getApplicationContext());
                if (msg.getEntriesCount() == 0)
                    fragment = AptAtten.newInstance();
                else fragment = AptAttenList.newInstance(msg.getAllCredentials());
                break;
            case 12:
                fragment = CoursePage.newInstance();
                break;
            case 13:
                fragment = LeavePre.newInstance();
                break;
            case 14:
                fragment = TeeQBank.newInstance();
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


    public void onMoved(int distance) {
        mToolbar.setTranslationY(-distance);
    }

    @Override
    public void OnImage(View v) {
        Log.d("Image", "Yep");
    }

    @Override
    public void OnProfile(View v) {
        Log.d("Profile", "Yep");
    }

    public boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mToolbar) == 0;
    }

    public boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mToolbar) == -mToolbar.getHeight();
    }

    public void showToolbar() {
        moveToolbar(0);
    }

    public void hideToolbar() {
        moveToolbar(-mToolbar.getHeight());
    }

    public void moveToolbar(float toTranslationY) {
        if (ViewHelper.getTranslationY(mToolbar) == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(mToolbar), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(mToolbar, translationY);
            }
        });
        animator.start();
    }

    public void setStatusBarColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(color);
        }
    }

    public void setToolBarColor(int color) {
        try {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(color));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}