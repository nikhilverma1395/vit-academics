package razor.nikhil.Fragments.FacMsg;

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
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.Fragments.FacInfo.GetFacDataStudLogin;
import razor.nikhil.Fragments.GetDetails;
import razor.nikhil.Http.BitmapUrlClient;
import razor.nikhil.Http.Http;
import razor.nikhil.Http.Logins;
import razor.nikhil.Http.ParseTimeTable;
import razor.nikhil.R;
import razor.nikhil.database.FacMsgGS;
import razor.nikhil.model.FacMsgModel;

/**
 * Created by Nikhil Verma on 9/30/2015.
 */
public class GetFacMessage extends Fragment {
    private static HttpClient httpClient;
    private RecyclerView recyclerView;
    private EditText REGNO, PASS, CAPTXT;
    private ImageView CAPIMAG;
    private Button button;
    private ProgressBarCircularIndeterminate progress_ll_;

    public static GetFacMessage newInstance() {
        GetFacMessage fragment = new GetFacMessage();
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
        progress_ll_ = (ProgressBarCircularIndeterminate) view.findViewById(R.id.progress_ll_log_dialog_);
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
                String data = Http.getData("https://academics.vit.ac.in/student/stud_home.asp", httpClient);//does the job
                final List<FacMsgModel> listFac = parse(data);
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

    private List<FacMsgModel> parse(String dataw) {
        int gap = 0;
        List<FacMsgModel> list = new ArrayList<>();
        Elements data = null;
        try {
            data = Jsoup.parse(dataw).getElementsByTag("table");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Elements datas = data.get(data.size() - 1).getElementsByTag("tr");
        String facname = null, time = null, subject, message = null;
        try {
            datas.remove(0);//title
            datas.remove(0);//sem
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        for (int i = 0; i < 15; i++)//10 msgs
            try {//Msgs
                FacMsgModel model = new FacMsgModel();
                facname = datas.get(0 + gap).getElementsByTag("td").get(2).html();
                subject = datas.get(1 + gap).getElementsByTag("td").get(2).html();
                message = datas.get(2 + gap).getElementsByTag("td").get(2).html();
                time = datas.get(3 + gap).getElementsByTag("td").get(2).html();
                model.setFacname(ParseTimeTable.FirstCharCap(facname));
                model.setSubject(subject);
                model.setMsg(message);
                model.setSentTime(time);
                list.add(model);
                gap += 5;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        if (list.size() > 0) {
            FacMsgGS gs = new FacMsgGS(getActivity());
            if (gs.getEntriesCount() == 0) {
                gs.createList(list);
            } else {
                gs.Delete();
                gs.createList(list);
            }
        }
        return list;
    }

    private void calLFrag(List<FacMsgModel> list) {
        button.setEnabled(true);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container_main, GetFacMsgList.newInstance(list)).commit();
    }

}
