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
import android.widget.TextView;

import org.apache.http.HttpException;
import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.util.HashMap;

import razor.nikhil.Http.BitmapUrlClient;
import razor.nikhil.Http.Http;
import razor.nikhil.Http.MySSLSocketFactory;
import razor.nikhil.R;

/**
 * Created by Nikhil Verma on 9/18/2015.
 */
public class LeaveRequest extends Fragment {
    private String POST_LINK_PRE = "https://academics.vit.ac.in/student/leave_request.asp";
    private String MAIN_POST_LINK = "https://academics.vit.ac.in/student/leave_request_submit.asp";
    private ImageView cap;
    private EditText et;
    private Button button;
    private TextView log;
    private static HttpClient httpClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.leave, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
        httpClient = MySSLSocketFactory.getNewHttpClient();
        try {
            new SetCaptcha().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void init(View view) {
        cap = (ImageView) view.findViewById(R.id.captcha_leave_iv);
        et = (EditText) view.findViewById(R.id.captcha_leave_et);
        button = (Button) view.findViewById(R.id.button_leave);
        log = (TextView) view.findViewById(R.id.log_leave);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String captext = et.getText().toString();
                new Post().execute(captext);
            }
        });
    }

    private class SetCaptcha extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Bitmap myBitmap = null;
            try {
                myBitmap = new BitmapUrlClient().getBitmapFromURL(StudentLogin.student_Login_Captcha_Link, httpClient);
                final Bitmap rma = myBitmap;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            cap.setImageBitmap(rma);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class Post extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            HashMap<String, String> headers = new HashMap<>();
            headers.put("regno", "13BCE0037");
            headers.put("passwd", "supermariogotze");
            headers.put("vrfcd", strings[0].trim());
            try {
                Http.postMethod(StudentLogin.student_Login_Link, headers, httpClient);
            } catch (HttpException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            HashMap<String, String> map = new HashMap<>();
            map.put("apply", "11934\\FA");
            map.put("lvtype", "EY");
            //ot
            map.put("exitdate", "24-Sep-2015");
//            map.put("exitdate", "21-Sep-2015");
            map.put("sttime_hh", "2");//After 7am defore 6pm , ,max diff 6 hrs
            map.put("sttime_mm", "30");
            map.put("frm_timetype", "PM");
            map.put("reentry_date", "26-Sep-2015");
            map.put("endtime_hh", "4");
            map.put("endtime_mm", "30");
            map.put("to_timetype", "PM");

            map.put("place", "Vellore ");
            map.put("reason", "Birthay ");
            String data = "";

            String huy = "";
            try {
                Http.getData("https://academics.vit.ac.in/student/stud_home.asp", httpClient);//need this as a pre-req
                Http.getData("https://academics.vit.ac.in/student/student_leave_report.asp", httpClient);//3nd pre-reqr
                final String weq = Http.getData("https://academics.vit.ac.in/student/leave_apply_dt.asp?x=%20&lvtyp=EY", httpClient);//2nd pre-req
                final String we = Http.getData(POST_LINK_PRE, httpClient);//3nd pre-reqr
                Http.getData("https://academics.vit.ac.in/student/leave_apply_dt.asp?x=%20&lvtyp=EY", httpClient);//2nd pre-req
                Http.getData(POST_LINK_PRE, httpClient);
                data = Http.postMethod(MAIN_POST_LINK, map, httpClient);
                final String finalData = data;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        log.setText(we + "\n\n" + weq + "\n\n\n" + finalData);
                    }
                });
            } catch (HttpException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Log.d("Data", data);
            return null;
        }
    }
}
