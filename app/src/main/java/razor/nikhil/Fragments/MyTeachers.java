package razor.nikhil.Fragments;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.apache.http.HttpException;
import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import razor.nikhil.Http.BitmapUrlClient;
import razor.nikhil.Http.Http;
import razor.nikhil.Http.ParseTimeTable;
import razor.nikhil.R;
import razor.nikhil.database.MyTeachGS;
import razor.nikhil.model.MyTeacherDet;

/**
 * Created by Nikhil Verma on 9/27/2015.
 */
public class MyTeachers extends Fragment {
    private EditText REGNO, PASS, CAPTXT;
    private ImageView CAPIMAG;
    private List<MyTeacherDet> list = new ArrayList<>();
    private Button button;
    private static HttpClient httpClient;
    public static String student_Login_Link = "https://academics.vit.ac.in/student/stud_login_submit.asp";
    public static String student_Login_Captcha_Link = "https://academics.vit.ac.in/student/captcha.asp";
    private String student_Login_Photo_Link = "https://academics.vit.ac.in/student/view_photo.asp";
    private String per = "https://academics.vit.ac.in/student/profile_personal_view.asp";
    private static String captchaText = "";
    private String FACULTY_INFO_pre = "https://academics.vit.ac.in/student/fac_profile.asp";
    private String FACULTY_INFO_LINK = "https://academics.vit.ac.in/student/getfacdet.asp?fac=";
    private String fac_det_pre = "https://academics.vit.ac.in/student/";
    private String employee_fac_pic = "https://academics.vit.ac.in/student/emp_photo.asp";
    private static String[] NAMES;

    public static MyTeachers newInstance(String[] names) {
        NAMES = names;
        MyTeachers met = new MyTeachers();
        return new MyTeachers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        httpClient = GetDetails.getThreadSafeClient();
        return inflater.inflate(R.layout.login_layout_stud, container, false);
    }

    public static String getwithPERCname(String name) {
        char cr[] = name.toCharArray();
        name = "";
        for (char a : cr) {
            if (a == ' ')
                name += "%20";
            else
                name += a;

        }
        return name;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                Bitmap bmp = null;
                try {
                    bmp = BitmapUrlClient.getBitmapFromURL(student_Login_Captcha_Link, httpClient);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final Bitmap bmpq = bmp;
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CAPIMAG.setImageBitmap(bmpq);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;

            }
        }.execute();
        CAPTXT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                captchaText = charSequence.toString();
                if (charSequence.length() != 6)
                    button.setEnabled(false);
                else button.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    private class GetFacData extends AsyncTask<String[], Void, Void> {

        @Override
        protected Void doInBackground(String[]... voids) {
            String names[] = voids[0];
            HashMap<String, String> headers = new HashMap<>();
            headers.put("regno", "13BCE0037");
            headers.put("passwd", "supermariogotze");
            headers.put("vrfcd", captchaText);
            try {
                Http.postMethod(student_Login_Link, headers, httpClient);
            } catch (HttpException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Http.getData(FACULTY_INFO_pre, httpClient);//need to do this
            for (String name : names) {
                char[] kack = name.toCharArray();
                name = "";
                for (char cr : kack) {
                    if (cr != '-') {
                        name += cr;
                    } else break;
                }
                name = name.toUpperCase().trim();
                Log.d("name", name);
                try {
                    final String facresp = Http.getData(FACULTY_INFO_LINK + getwithPERCname(name), httpClient);
                    if (!facresp.contains(name)) {
                        //names are in upper case
                        int x = 1 % 0;//invalid statement to invoke catch block
                    }
                    MyTeacherDet mmod = parsefacidlink(facresp);
                    list.add(mmod);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("Faculty Info Not av", name);
                }

            }
            inserindb(list);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            getActivity()
                    .getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container_main, new MyTeachersList(list)).commit();
        }
    }

    private void inserindb(List<MyTeacherDet> list) {
        MyTeachGS sql = new MyTeachGS(getActivity());
        try {
            if (sql.getEntriesCount() == 0)
                sql.createList(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private MyTeacherDet parsefacidlink(String source) {
        MyTeacherDet mod = new MyTeacherDet();
        //Checked working for 1 returned query, gives the href
        String href = Jsoup.parse(source).getElementsByTag("table").get(0)//table 1
                .getElementsByTag("tr").get(1)//tr - 2
                .getElementsByTag("td").get(3)//td- 4
                .getElementsByTag("a").get(0).attr("href");//a - 1
        String content = Http.getData(fac_det_pre + href, httpClient);
        Elements data = Jsoup.parse(content).getElementsByTag("table").get(1).getElementsByTag("tr");
        data.remove(0);
        String name = new ParseTimeTable(null,null).FirstCharCap(data.get(0).getElementsByTag("td").get(1).html().trim());
        Log.d("Name in Parse", name);
        mod.setNAME(name);
        String school = data.get(1).getElementsByTag("td").get(1).html().trim().replaceAll("amp;", "");
        mod.setSCHOOL(school);
        String designation = data.get(2).getElementsByTag("td").get(1).html().trim();
        mod.setDESIGNATION(designation);
        String room = data.get(3).getElementsByTag("td").get(1).html().trim();
        mod.setROOM(room);
        String email = data.get(5).getElementsByTag("td").get(1).html().trim();//Skipping 4th as its intercom
        mod.setEMAIL(email);
        String divi = "";
        String openhs = "";
        String addrole = "";
        List<OpenHours> open = new ArrayList<>();

        try {
            //in the html, they were blank , so a precaution
            divi = data.get(6).getElementsByTag("td").get(1).html().trim();
            mod.setDIVISION(divi);
            addrole = data.get(7).getElementsByTag("td").get(1).html().trim();
            mod.setADDROLE(addrole);
            try {
                Elements openhrs = null;//it gets big, if available
                try {
                    openhrs = data.get(8).getElementsByTag("td").get(1).getElementsByTag("table").get(0).getElementsByTag("tr");
                } catch (Exception e) {
                    throw new Exception("Open Hours Data Not Available for " + name);
                }
                openhrs.remove(0);
                for (Element ele : openhrs) {
                    OpenHours or = new OpenHours();
                    or.day = ele.getElementsByTag("td").get(0).html();
                    or.from = ele.getElementsByTag("td").get(1).html();
                    or.to = ele.getElementsByTag("td").get(2).html();
                    open.add(or);
                }
                StringBuilder build = new StringBuilder();
                for (OpenHours op : open) {
                    build.append(Html.fromHtml("<b>" + op.day.trim() + "</b>") + "\t\t\t" + op.from.trim() + "\t\t to \t\t" + op.to.trim() + "\t\t\n\n");
                }
                mod.setOPENHRS(build.toString());
            } catch (Exception e) {
                mod.setOPENHRS("N/A");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mod;
    }

    class OpenHours {
        String day;
        String from;
        String to;
    }

    private void init(View view) {
        REGNO = (EditText) view.findViewById(R.id.regno_studentlog_myt);
        PASS = (EditText) view.findViewById(R.id.pass_studentlog_myt);
        CAPIMAG = (ImageView) view.findViewById(R.id.capImg_studentlog_myt);
        CAPTXT = (EditText) view.findViewById(R.id.capTxt_studentlog_myt);
        button = (Button) view.findViewById(R.id.proceedBut_studentlog_myt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetFacData().execute(NAMES);
            }
        });
    }
}
