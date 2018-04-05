package razor.nikhil.Fragments.Syllabus;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.widgets.SnackBar;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import razor.nikhil.Activity.MainActivity;
import razor.nikhil.Activity.WebViewAct;
import razor.nikhil.Download.NLService;
import razor.nikhil.Fragments.CoursePage.CoursePage;
import razor.nikhil.Fragments.TeeQBank;
import razor.nikhil.Http.Http;
import razor.nikhil.Listener.HidingScrollListener;
import razor.nikhil.Listener.RecyclerItemClickListener;
import razor.nikhil.R;
import razor.nikhil.model.CoursePageModel;
import razor.nikhil.model.SyllabusCourseItem;
import razor.nikhil.model.SyllabusSubjectVersion;
import razor.nikhil.model.TEEVersionModel;

/**
 * Created by Nikhil Verma on 11/7/2015.
 */
public class SyllabusVersionFragment extends Fragment {
    private RecyclerView recyclerView;
    public static final String WEB_VIEW_DATA = "web_View_HO";
    //CoursePage
    static List<CoursePageModel> coursePage = null;
    //
    private static List<SyllabusSubjectVersion> VER = null;
    private static List<TEEVersionModel> teepapers = null;
    private static SyllabusCourseItem ITEM = null;
    public static String POST_SYLL = "https://academics.vit.ac.in/student/syllabus_file.asp";
    private String POST_TEE = "https://academics.vit.ac.in/student/tee_questionbank_download.asp";

    NotificationCompat.Builder mBuilder;
    NotificationManager mNotifyManager;
    private static int id = 1;
    int counter = 0;
    NotificationReceiver nReceiver;

    public static SyllabusVersionFragment newInstance(List<SyllabusSubjectVersion> sub, SyllabusCourseItem item) {
        SyllabusVersionFragment fragment = new SyllabusVersionFragment();
        VER = sub;
        ITEM = item;
        return fragment;
    }

    public static SyllabusVersionFragment newInstant(List<TEEVersionModel> sub, SyllabusCourseItem item) {
        SyllabusVersionFragment fragment = new SyllabusVersionFragment();
        teepapers = sub;
        ITEM = item;
        return fragment;
    }

    public static Fragment newInstance(List<CoursePageModel> coursePageModels) {
        coursePage = coursePageModels;
        return new SyllabusVersionFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!((MainActivity) getActivity()).toolbarIsShown())
            ((MainActivity) getActivity()).showToolbar();
        setHasOptionsMenu(false);
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
        recyclerView.setAdapter(new SyllabusDownload());
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (VER != null)
                    new DownloadFile().execute(ITEM.getSUBCODE(), VER.get(position - 1).getType_POST(), VER.get(position - 1).getVersion_POST(), position - 1 + "");
                else if (teepapers != null) new DownloadPaper().execute(position);
                else if (coursePage != null) {
                    new ParseCoursePage().execute(coursePage.get(position));
                }
            }
        }));
        if (coursePage != null) {
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
        setUpNotification();
    }


    private class SyllabusDownload extends RecyclerView.Adapter<SyllabusVersionFragment.SyllabusDownload.ViewHolder> {

        private Typeface bold, regular;

        public SyllabusDownload() {
            bold = Typeface.createFromAsset(getActivity().getAssets(), "RobotoCondensed-Bold.ttf");
            regular = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myteachrow, parent, false);
            return (new ViewHolder(itemView));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (position == 0) {
                if (VER != null) {
                    holder.TITLE.setText("Course Title");
                    holder.TYPE_ver.setText("Course Title" + " -\t" + "Version");
                } else if (teepapers != null) {
                    holder.TITLE.setText("Course Title");
                    holder.TYPE_ver.setText("Semester");
                } else if (coursePage != null) {
                    holder.TITLE.setText("Faculty");
                    holder.TYPE_ver.setText("Slot");
                }
                holder.TITLE.setTypeface(bold);
                holder.TYPE_ver.setTypeface(bold);
                return;
            }
            holder.TITLE.setTypeface(regular);
            holder.TYPE_ver.setTypeface(regular);
            if (coursePage == null)
                holder.TITLE.setText(ITEM.getSUBNAME());
            else
                holder.TITLE.setText(coursePage.get(position - 1).getTeacher());

            if (VER != null)
                holder.TYPE_ver.setText(VER.get(position - 1).getType_POST() + "\1t\t\t\t" + VER.get(position - 1).getVersion_POST());
            else if (teepapers != null)
                holder.TYPE_ver.setText(teepapers.get(position - 1).getSem());
            else if (coursePage != null) {
                holder.TYPE_ver.setText(coursePage.get(position - 1).getSlot());
            }
        }

        @Override
        public int getItemCount() {
            if (VER != null)
                return VER.size() + 1;
            else if (teepapers != null)
                return teepapers.size() + 1;
            else
                return coursePage.size() + 1;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView TITLE, TYPE_ver;

            public ViewHolder(View v) {
                super(v);
                TITLE = (TextView) v.findViewById(R.id.myt_name);
                TYPE_ver = (TextView) v.findViewById(R.id.myt_sub);
            }
        }

    }

    private class DownloadFile extends AsyncTask<String, Integer, Integer> {
        long TOTAL_SIZE = 0;
        String FILENAME = "";

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (values[0] >= 0 && values[0] <= 100) {
                mBuilder.setContentTitle(FILENAME);
                mBuilder.setContentText(values[0] + "\t%");
                mBuilder.setProgress(100, values[0], false);
                mNotifyManager.notify(id, mBuilder.build());
                return;
            }
        }

        @Override
        protected Integer doInBackground(String... params) {
            HashMap<String, String> mapLTYPE = new HashMap<>();//cleared
            mapLTYPE.put("crscd", params[0]);
            mapLTYPE.put("crstp", params[1]);
            mapLTYPE.put("version", params[2]);
            mapLTYPE.put("sybcmd", "Download");
            InputStream inputStream = null;
            Http HTTP = new Http();
            try {
                inputStream = HTTP.postMethodStream(POST_SYLL, mapLTYPE, Syllabus.getHttpClient());
            } catch (Exception e) {
                e.printStackTrace();
            }
            FileOutputStream fos;

            try {
            /* making a directory in sdcard */
                String sdCard = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(sdCard, "Vit Syllabus");

            /* if specified not exist create new */
                if (!myDir.exists()) {
                    myDir.mkdir();
                    Log.v("", "inside mkdir");
                }
                //Name - BIF205_PERL-FOR-BIOINFORMATICS_ETH_2.00_SC04.pdf
            /* checks the file and if it already exist delete */
                String fname = ITEM.getSUBNAME() + "_" + VER.get(Integer.parseInt(params[3])).getType_POST() + "_" + params[2] + ".00" + "_" +
                        VER.get(Integer.parseInt(params[3])).getAcademicCounsel() +
                        ".pdf";
                FILENAME = fname;
                File file = new File(myDir, fname);
                Log.d("file===========path", "" + file);
                if (file.exists())
                    file.delete();
                fos = new FileOutputStream(file);
                long totalSize = HTTP.INPUTSTREAM_SIZE;
                TOTAL_SIZE = totalSize;
                int downloadedSize = 0;
                byte[] buffer = new byte[1024];
                int bufferLength = 0;
                int latestPercentDone;
                int percentDone = -1;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, bufferLength);
                    downloadedSize += bufferLength;
                    latestPercentDone = (int) ((downloadedSize * 100) / totalSize);
                    if (percentDone != latestPercentDone) {//No Lag in Progress Update
                        percentDone = latestPercentDone;
                        publishProgress(percentDone);
                    }
                }
                inputStream.close();
                fos.close();
                Log.d("test", "File Saved in sdcard..");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBuilder.setContentTitle(FILENAME);
                        mBuilder.setContentText("Completed");
                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Vit Syllabus/" + FILENAME);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        mBuilder.setContentIntent(PendingIntent.getActivity(getActivity(), 0, intent,
                                0));
                        mNotifyManager.notify(id, mBuilder.build());
                        id = new Random().nextInt();
                        Toast.makeText(getActivity(), FILENAME + "\t Saved in \\emulated\\Vit Syllabus", Toast.LENGTH_SHORT).show();
                        return;
                    }
                });
                return 91;
            } catch (IOException io) {
                inputStream = null;
                fos = null;
                io.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 78;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            try {
                if (integer == 91) {
                } else if (integer == 78) {
                    Toast.makeText(getActivity(), FILENAME + "\t Error In Downloading.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void setUpNotification() {
        mNotifyManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(getActivity());
        mBuilder.setSmallIcon(R.mipmap.download);
        mBuilder.setAutoCancel(false);
        ContentResolver contentResolver = getActivity().getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getActivity().getPackageName();

        if (enabledNotificationListeners == null || !enabledNotificationListeners.contains(packageName)) {
            Log.i("ACC", "Dont Have Notification access");
            Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        } else {
            Log.i("ACC", "Have Notification access");
        }
        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(NLService.NOT_TAG);
        getActivity().registerReceiver(nReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(nReceiver);
    }


    class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String event = intent.getExtras().getString(NLService.NOT_EVENT_KEY);
            Log.i("NotificationReceiver", "NotificationReceiver onReceive : " + event);
            if (event.trim().contentEquals(NLService.NOT_REMOVED)) {
//                killTasks();
            }
        }
    }

    private class DownloadPaper extends AsyncTask<Integer, Integer, Integer> {
        long TOTAL_SIZE = 0;
        String FILENAME = "";

        @Override
        protected Integer doInBackground(final Integer... params) {
            --params[0];
            HashMap<String, String> mapLTYPE = new HashMap<>();//cleared
            FILENAME = ITEM.getSUBCODE() + "_" + teepapers.get(params[0]).getName() + "_" + teepapers.get(params[0]).getSem();
            mapLTYPE.put("crscd", ITEM.getSUBCODE());
            Log.d("crscd", ITEM.getSUBCODE());
            Log.d("qpid", teepapers.get(params[0]).getQpid());
            mapLTYPE.put("qpid", teepapers.get(params[0]).getQpid());
            mapLTYPE.put("sybcmd", "Download");
            InputStream inputStream = null;
            try {
                inputStream = Http.postMethodStream(POST_TEE, mapLTYPE, TeeQBank.getHttpClient());
            } catch (Exception e) {
                e.printStackTrace();
            }
            FileOutputStream fos;

            try {
            /* making a directory in sdcard */
                String sdCard = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(sdCard, "Vit_TEE_Papers");

            /* if specified not exist create new */
                if (!myDir.exists())
                    myDir.mkdir();


            /* checks the file and if it already exist delete */
                final String fname = FILENAME + ".pdf";
                File file = new File(myDir, fname);
                Log.d("file===========path", "" + file);
                if (file.exists())
                    file.delete();
                fos = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int bufferLength = 0;
                while ((bufferLength = inputStream.read(buffer)) > 0) {
                    fos.write(buffer, 0, bufferLength);
                }
                inputStream.close();
                fos.close();
                Log.d("test", "File Saved in sdcard..");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBuilder.setContentTitle(FILENAME);
                        mBuilder.setContentText(teepapers.get(params[0]).getSem());
                        mBuilder.setProgress(100, 100, false);
                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Vit TEE Papers/" + fname);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        mBuilder.setContentIntent(PendingIntent.getActivity(getActivity(), 0, intent,
                                0));
                        mNotifyManager.notify(new Random().nextInt(), mBuilder.build());
                    }
                });
                return 91;
            } catch (IOException io) {
                inputStream = null;
                fos = null;
                io.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 78;
        }

    }

    private class ParseCoursePage extends AsyncTask<CoursePageModel, Void, String> {
        @Override
        protected String doInBackground(CoursePageModel... params) {
            HashMap<String, String> map = new HashMap<>();
            map.put("sem", "fs");
            map.put("crsplancode", params[0].getCrsplancode());
            map.put("crpnvwcmd", "View");
            String repo = "";
            try {
                repo = Http.postMethod("https://academics.vit.ac.in/student/coursepage_view3.asp", map, CoursePage.getHttpClient());
                String toR = "value='Download'";
                String with = "value='Download' onClick='Android.performClick();'";
                repo = repo.replace(toR, with);
                toR = "value='Download Course Page'";
                with = "value='Download Course Page' onClick='Android.downCPage();'";
                repo = repo.replace(toR, with);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return repo;
        }

        @Override
        protected void onPostExecute(String coursePageAll) {
            super.onPostExecute(coursePageAll);
            if (coursePageAll == null) {
                new SnackBar(getActivity(), "No Data Available", null, null).show();
                return;
            }
            Intent intent = new Intent(getActivity(), WebViewAct.class);
            intent.putExtra(WEB_VIEW_DATA, coursePageAll);
            startActivity(intent);
        }
    }
}