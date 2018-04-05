package razor.nikhil.Activity;

import android.animation.ValueAnimator;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gc.materialdesign.widgets.SnackBar;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ObservableWebView;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.nineoldandroids.view.ViewHelper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Random;

import razor.nikhil.Download.NLService;
import razor.nikhil.Fragments.CoursePage.CoursePage;
import razor.nikhil.Fragments.Syllabus.SyllabusVersionFragment;
import razor.nikhil.Http.Http;
import razor.nikhil.R;

/**
 * Created by Nikhil Verma on 12/11/2015.
 */
public class WebViewAct extends AppCompatActivity implements ObservableScrollViewCallbacks {
    private String data = "";
    private ObservableWebView webView;
    private Toolbar mToolbar;
    private SwipeRefreshLayout swipeContainer;
    TextView toolbarTitle;
    NotificationCompat.Builder mBuilder;
    NotificationManager mNotifyManager;
    private static int id = 1;
    int counter = 0;
    NotificationReceiver nReceiver;
    private static final String CoursePageDownPost = "https://academics.vit.ac.in/student/coursepage_export.asp";

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
    }

    @Override
    public void onDownMotionEvent() {
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        if (scrollState == ScrollState.UP) {
            if (toolbarIsShown()) {
                hideToolbar();
            }
        } else if (scrollState == ScrollState.DOWN) {
            if (toolbarIsHidden()) {
                showToolbar();
            }
        }
    }

    private boolean toolbarIsShown() {
        return ViewHelper.getTranslationY(mToolbar) == 0;
    }

    private boolean toolbarIsHidden() {
        return ViewHelper.getTranslationY(mToolbar) == -mToolbar.getHeight();
    }

    private void showToolbar() {
        moveToolbar(0);
    }

    private void hideToolbar() {
        moveToolbar(-mToolbar.getHeight());
    }

    private void moveToolbar(float toTranslationY) {
        if (ViewHelper.getTranslationY(mToolbar) == toTranslationY) {
            return;
        }
        ValueAnimator animator = ValueAnimator.ofFloat(ViewHelper.getTranslationY(mToolbar), toTranslationY).setDuration(200);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float translationY = (float) animation.getAnimatedValue();
                ViewHelper.setTranslationY(mToolbar, translationY);
                ViewHelper.setTranslationY(webView, translationY);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) (webView).getLayoutParams();
                lp.height = (int) -translationY + getScreenHeight() - lp.topMargin;
                webView.requestLayout();
            }
        });
        animator.start();
    }

    private int getScreenHeight() {
        return findViewById(android.R.id.content).getHeight();
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

    private void setUpNotification() {
        mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.mipmap.download);
        mBuilder.setAutoCancel(false);
        ContentResolver contentResolver = getContentResolver();
        String enabledNotificationListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");
        String packageName = getPackageName();

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
        registerReceiver(nReceiver, filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nReceiver);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webviewact);
        data = getIntent().getExtras().getString(SyllabusVersionFragment.WEB_VIEW_DATA);
        if (data == null || data.equals("")) {
            SnackBar bar = new SnackBar(this, "Data Not Available", null, null);
            bar.show();
            return;
        }
        setUpNotification();
        init();
        webView.loadData(data, "text/html", null);

    }

    private void init() {
        //  swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipecont_webview);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_webview);
        setSupportActionBar(mToolbar);
        toolbarTitle = (TextView) mToolbar.findViewById(R.id.toolbar_text_wv);
        toolbarTitle.setText(CoursePage.Slot_Web.getSubject_name());
//        swipeContainer.setProgressViewOffset(false, 0, 320);//works :)
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        swipeContainer.setRefreshing(false);
//                    }
//                }, 300);
//
//            }
//        });

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        webView = (ObservableWebView) findViewById(R.id.webview_act);
        webView.setWebViewClient(new MyWebViewClient());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");
        webView.setScrollViewCallbacks(this);
    }

    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void performClick() {//works :)
            new DownloadFile().execute(SyllabusVersionFragment.POST_SYLL, "s");
        }

        @JavascriptInterface
        public void downCPage() {//works :)
            Log.d("Here", "d");
            new DownloadFile().execute(CoursePageDownPost, "c");
        }

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            //        view.loadUrl(url);
            //    if (Uri.parse(url).getHost().equals("www.example.com")) {
            //      return false;
            //  }
            // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    private class DownloadFile extends AsyncTask<String, Integer, Integer> {
        long TOTAL_SIZE = 0;
        String FILENAME = "";
        boolean isSyll = false;

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

            if (params[1].equals("s"))
                isSyll = true;

            Elements syll = null;
            if (isSyll) {
                Elements syllabus = Jsoup.parse(data).getElementsByTag("table");//
                Elements syllTR = syllabus.get(2).getElementsByTag("tr");
                syll = syllTR.get(0).getElementsByTag("input");
                if (syllabus.size() == 0 || syllTR.size() == 0)
                    return null;
            }
            Element Form = Jsoup.parse(data).getElementsByTag("table").get(0).getElementsByTag("form").last();
            Log.d("Data", Form.toString());
            if (1 == 1)
                return null;
            HashMap<String, String> mapLTYPE = new HashMap<>();//cleared
            if (isSyll) {
                mapLTYPE.put("crscd", CoursePage.Slot_Web.getCode().trim());
                mapLTYPE.put("crstp", syll.get(1).attr("value").trim());
                mapLTYPE.put("version", syll.get(2).attr("value").trim());
                mapLTYPE.put("sybcmd", "Download");
            } else {
            }
            InputStream inputStream = null;
            Http HTTP = new Http();
            try {
                inputStream = HTTP.postMethodStream(SyllabusVersionFragment.POST_SYLL, mapLTYPE, CoursePage.getHttpClient());
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
                final String fname = CoursePage.Slot_Web.getSubject_name() + "_" + CoursePage.Slot_Web.getCourse_type() + "_" + syll.get(2).attr("value").trim() + ".00" + "" + ".pdf";
                FILENAME = CoursePage.Slot_Web.getSubject_name();
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mBuilder.setContentTitle(FILENAME);
                        mBuilder.setContentText("Completed");
                        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Vit Syllabus/" + fname);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mBuilder.setContentIntent(PendingIntent.getActivity(WebViewAct.this, 0, intent,
                                0));
                        mNotifyManager.notify(id, mBuilder.build());
                        id = new Random().nextInt();
                        Toast.makeText(WebViewAct.this, FILENAME + "\t Saved in \\emulated\\Vit Syllabus", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(WebViewAct.this, FILENAME + "\t Error In Downloading.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
