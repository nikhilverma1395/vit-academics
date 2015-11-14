package razor.nikhil.Fragments;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.Config;
import razor.nikhil.Http.BitmapUrlClient;
import razor.nikhil.Http.Http;
import razor.nikhil.Http.Logins;
import razor.nikhil.R;
import razor.nikhil.database.APT_GS;
import razor.nikhil.database.SharedPrefs;
import razor.nikhil.model.AptModel;

/**
 * Created by Nikhil Verma on 10/1/2015.
 */
public class AptAtten extends Fragment {
    private HttpClient httpClient;
    private RecyclerView recyclerView;
    private EditText REGNO, PASS, CAPTXT;
    private ImageView CAPIMAG;
    private Button button;
    private ProgressBarCircularIndeterminate progress_ll_;

    public static AptAtten newInstance() {
        AptAtten fragment = new AptAtten();
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
        progress_ll_ = (ProgressBarCircularIndeterminate) view.findViewById(R.id.progress_ll_log_dialog);
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
                httpClient = Logins.StudentLogin(reg, pass, cap, httpClient);
                Http.getData("https://academics.vit.ac.in/student/stud_home.asp", httpClient);//does the job
                String data = Http.getData("https://academics.vit.ac.in/student/apt_attendance.asp", httpClient);
                final List<AptModel> listFac = parse(data);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (listFac != null) {
                            progress_ll_.setVisibility(View.INVISIBLE);
                            calLFrag(listFac);
                        } else {
                            Toast.makeText(getActivity(), "Username or Password or Captcha is Incorrect , Try Again", Toast.LENGTH_SHORT).show();
                            new LoadCaptcha().execute();
                        }
                    }
                });
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                progress_ll_.setVisibility(View.INVISIBLE);
            }
        }.execute();
    }

    private List<AptModel> parse(String data) {
        List<AptModel> list = new ArrayList<>();
        try {
            Element table = Jsoup.parse(data).getElementsByTag("table").get(3);
            Elements trS = table.getElementsByTag("tr");
            trS.remove(0);//Titles
            for (int r = 0; r < trS.size(); r++) {
                AptModel model = new AptModel();
                Element ment = trS.get(r);
                Elements tdS = ment.getElementsByTag("td");
                if (tdS.size() == 4) {
                    model.setDate(tdS.get(0).getElementsByTag("font").get(0).html().trim());
                    model.setSession(tdS.get(1).getElementsByTag("font").get(0).html().trim());
                    model.setUnits(tdS.get(3).getElementsByTag("font").get(0).html().trim());
                    list.add(model);
                } else if (tdS.size() == 2) {
                    SharedPrefs pref = new SharedPrefs(getActivity());
                    if (r == trS.size() - 3)
                        pref.storeMsg(Config.APT_TOTAL_CLASSES, tdS.get(1).html());
                    if (r == trS.size() - 2)
                        pref.storeMsg(Config.APT_ATTENDED, tdS.get(1).html());
                    if (r == trS.size() - 1)
                        pref.storeMsg(Config.APT_PERCENT, tdS.get(1).html());
                }

            }
            try {
                new APT_GS(getActivity()).createList(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void calLFrag(List<AptModel> list) {
        button.setEnabled(true);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container_main, AptAttenList.newInstance(list)).commit();
    }

}
