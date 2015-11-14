package razor.nikhil.Fragments;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import razor.nikhil.Activity.MainActivity;
import razor.nikhil.Download.NLService;
import razor.nikhil.Http.Http;
import razor.nikhil.Listener.RecyclerItemClickListener;
import razor.nikhil.R;
import razor.nikhil.model.SyllabusCourseItem;
import razor.nikhil.model.SyllabusSubjectVersion;

/**
 * Created by Nikhil Verma on 11/7/2015.
 */
public class SyllabusVersionFragment extends Fragment {
    private RecyclerView recyclerView;
    private static List<SyllabusSubjectVersion> VER;
    private static SyllabusCourseItem ITEM;
    private String POST_SYLL = "https://academics.vit.ac.in/student/syllabus_file.asp";

    NotificationCompat.Builder mBuilder;
    NotificationManager mNotifyManager;
    int id = 1;
    int counter = 0;
    NotificationReceiver nReceiver;

    public static SyllabusVersionFragment newInstance(List<SyllabusSubjectVersion> sub, SyllabusCourseItem item) {
        SyllabusVersionFragment fragment = new SyllabusVersionFragment();
        VER = sub;
        ITEM = item;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (!((MainActivity) getActivity()).toolbarIsShown())
            ((MainActivity) getActivity()).showToolbar();
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
                new DownloadFile().execute(ITEM.getSUBCODE(), VER.get(position - 1).getType_POST(), VER.get(position - 1).getVersion_POST());
            }
        }));
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
                holder.TITLE.setText("Course Title");
                holder.TYPE_ver.setText("Course Title" + " -\t" + "Version");
                holder.TITLE.setTypeface(bold);
                holder.TYPE_ver.setTypeface(bold);
                return;
            }
            holder.TITLE.setTypeface(regular);
            holder.TYPE_ver.setTypeface(regular);
            holder.TITLE.setText(ITEM.getSUBNAME());
            holder.TYPE_ver.setText(VER.get(position - 1).getType_POST() + "\1t\t\t\t" + VER.get(position - 1).getVersion_POST());
        }

        @Override
        public int getItemCount() {
            return VER.size() + 1;
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
            if (values[0] == 100) {
                mBuilder.setContentTitle(FILENAME + ".pdf\t Completed.");
                mNotifyManager.notify(id, mBuilder.build());
                id += 2;
                Toast.makeText(getActivity(), FILENAME + "\t Saved in \\emulated\\Vit Syllabus", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        @Override
        protected Integer doInBackground(String... params) {
            HashMap<String, String> mapLTYPE = new HashMap<>();//cleared
            FILENAME = params[0];
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

            /* checks the file and if it already exist delete */
                String fname = params[0] + ".pdf";
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
                    Log.d("VALUE", latestPercentDone + "");
                    if (percentDone != latestPercentDone) {//No Lag in Progress Update
                        percentDone = latestPercentDone;
                        publishProgress(percentDone);
                    }
                    Log.i("Progress:", "downloadedSize:" + downloadedSize +
                            "totalSize:" + totalSize);
                }
                inputStream.close();
                fos.close();
                Log.d("test", "File Saved in sdcard..");
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
        mBuilder.setContentTitle("Downloading images...").setContentText("Download in progress").setSmallIcon(R.mipmap.download);
        // Start a lengthy operation in a background thread
        mBuilder.setProgress(0, 0, true);
        //mNotifyManager.notify(id, mBuilder.build());
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

}
