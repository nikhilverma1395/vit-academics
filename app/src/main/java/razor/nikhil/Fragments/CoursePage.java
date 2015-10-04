package razor.nikhil.Fragments;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;

import razor.nikhil.Http.BitmapUrlClient;
import razor.nikhil.Http.Http;
import razor.nikhil.Http.Logins;
import razor.nikhil.Http.PostParent;
import razor.nikhil.R;
import razor.nikhil.model.FacMsgModel;

/**
 * Created by Nikhil Verma on 10/2/2015.
 */
public class CoursePage extends Fragment {
    private static HttpClient httpClient;
    private static String CoursePage = "https://academics.vit.ac.in/student/coursepage_view.asp?sem=";
    private static String CoursePagePost = "https://academics.vit.ac.in/student/coursepage_view3.asp";
    private EditText REGNO, PASS, CAPTXT;
    private ImageView CAPIMAG;
    private Button button;
    private ProgressBarCircularIndeterminate progress_ll_;
    private String SEM;

    public static CoursePage newInstance() {
        CoursePage fragment = new CoursePage();
        return fragment;
    }

    private class LoadCaptcha extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final Bitmap bmp = BitmapUrlClient.getBitmapFromURL(GetFacDataStudLogin.student_Login_Captcha_Link, httpClient);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CAPIMAG.setImageBitmap(bmp);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        SEM = PostParent.getSem();
        CoursePage += (SEM + "&crs=CSE327&slt=B1+TB1&fac=");
        return inflater.inflate(R.layout.login_layout_stud, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        try {
            httpClient = GetDetails.getThreadSafeClient();
            new LoadCaptcha().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(View view) {
        progress_ll_ = (ProgressBarCircularIndeterminate) view.findViewById(R.id.progress_ll_);
        REGNO = (EditText) view.findViewById(R.id.regno_studentlog_myt);
        PASS = (EditText) view.findViewById(R.id.pass_studentlog_myt);
        CAPIMAG = (ImageView) view.findViewById(R.id.capImg_studentlog_myt);
        CAPTXT = (EditText) view.findViewById(R.id.capTxt_studentlog_myt);
        button = (Button) view.findViewById(R.id.proceedBut_studentlog_myt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mysync();
            }
        });
    }

    private void mysync() {
        new AsyncTask<Void, Void, Void>() {
            String cap = "";
            String reg, pass;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                button.setEnabled(false);
                progress_ll_.setVisibility(View.VISIBLE);
                cap = CAPTXT.getText().toString();
                reg = REGNO.getText().toString();
                pass = PASS.getText().toString();
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    httpClient = Logins.StudentLogin(reg, pass, cap, httpClient);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progress_ll_.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }

    private void parseCP(String source) {
        Element TextRefMat = Jsoup.parse(source).getElementsByTag("table").get(2);//Text/Reference Material
        //      Element syllabus
    }


    private void calLFrag(List<FacMsgModel> list) {
        button.setEnabled(true);
        //    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container_main, ).commit();
    }

}
