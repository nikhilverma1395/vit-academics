package razor.nikhil.Fragments;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private String GET_SYLL = "https://academics.vit.ac.in/student/syllabus_view.asp?shby=0&crcd=BIF304";
    private String POST_SYLL = "https://academics.vit.ac.in/student/syllabus_file.asp";
    private String MAIN_POST_LINK = "https://academics.vit.ac.in/student/leave_request_submit.asp";
    private ImageView cap;
    private EditText et;
    private Button button;
    private TextView log;
    private static HttpClient httpClient;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.login_layout_stud, container, false);
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

    public static LeaveRequest newInstance() {
        return new LeaveRequest();
    }

    private class SetCaptcha extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Bitmap myBitmap = null;
            try {
                myBitmap = new BitmapUrlClient().getBitmapFromURL(GetFacDataStudLogin.student_Login_Captcha_Link, httpClient);
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
                Http.postMethod(GetFacDataStudLogin.student_Login_Link, headers, httpClient);
            } catch (HttpException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            HashMap<String, String> map = new HashMap<>();
            map.put("apply", "11934\\FA");
            map.put("lvtype", "EY");
            //ot
            map.put("exitdate", "24-Oct-2015");
//            mapLTYPE.put("exitdate", "21-Sep-2015");
            map.put("sttime_hh", "2");//After 7am defore 6pm , ,max diff 6 hrs
            map.put("sttime_mm", "30");
            map.put("frm_timetype", "PM");
            map.put("reentry_date", "26-Oct-2015");
            map.put("endtime_hh", "4");
            map.put("endtime_mm", "30");
            map.put("to_timetype", "PM");

            map.put("place", "Vellore ");
            map.put("reason", "Birthay ");
            //Corrected the error
            map.put("requestcmd", "Apply");//requires this Also , as the name for submit is changed

            try {
                Http.getData("https://academics.vit.ac.in/student/stud_home.asp", httpClient);//need this as a pre-req
                final String weq = Http.getData("https://academics.vit.ac.in/student/leave_apply_dt.asp?x=%20&lvtyp=EY", httpClient);//2nd pre-req
                final String we = Http.getData(POST_LINK_PRE, httpClient);//3nd pre-reqr
                Http.getData("https://academics.vit.ac.in/student/leave_apply_dt.asp?x=%20&lvtyp=EY", httpClient);//2nd pre-req
                Http.getData(POST_LINK_PRE, httpClient);
                final String data = Http.postMethod(MAIN_POST_LINK, map, httpClient);
                //final String finalData = Http.getData(GET_SYLL, httpClient);
//                mapLTYPE = new HashMap<>();//cleared
//                mapLTYPE.put("crscd", "BIF304");
//                mapLTYPE.put("crstp", "TH");
//                mapLTYPE.put("version", "1");
//                mapLTYPE.put("sybcmd", "Download");
//                final InputStream stream = Http.postMethodStream(POST_SYLL, mapLTYPE, httpClient);
                // saveFile(stream);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //          Toast.makeText(getActivity(), "File Saved in Root Directory as\t" + "BIF304_Syllabus.pdf", Toast.LENGTH_SHORT).show();
                    }
                });
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        log.setText(we + "\n\n" + weq + "\n\n\n" + data);
                    }
                });
            } catch (Exception e) {
                Log.d("Exception", e.toString());
                e.printStackTrace();
            }
            return null;
        }
    }

    private void saveFile(InputStream is) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "BIF304_Syllabus.pdf"));
            int read = 0;
            byte[] buffer = new byte[32768];
            while ((read = is.read(buffer)) > 0) {
                fos.write(buffer, 0, read);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            fos.close();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
