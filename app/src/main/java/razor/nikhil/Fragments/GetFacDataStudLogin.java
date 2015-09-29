package razor.nikhil.Fragments;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import org.apache.http.HttpException;
import org.apache.http.client.HttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import razor.nikhil.Http.BitmapUrlClient;
import razor.nikhil.Http.Http;
import razor.nikhil.Http.MySSLSocketFactory;
import razor.nikhil.Http.ParseTimeTable;
import razor.nikhil.R;

/**
 * Created by Nikhil Verma on 7/29/2015.
 */
public class GetFacDataStudLogin extends Fragment {
    private EditText captcha_edit_student, facname;
    private ProgressBarCircularIndeterminate pbar;
    private static HttpClient httpClient;
    private String facnamestr = "";
    private ButtonRectangle assign_stud_pic;
    private ImageView captcha_student;
    private String SPOTLIGHT = "https://academics.vit.ac.in/student/include_spotlight.asp";
    public static String student_Login_Link = "https://academics.vit.ac.in/student/stud_login_submit.asp";
    public static String student_Login_Captcha_Link = "https://academics.vit.ac.in/student/captcha.asp";
    private String student_Login_Photo_Link = "https://academics.vit.ac.in/student/view_photo.asp";
    private String per = "https://academics.vit.ac.in/student/profile_personal_view.asp";
    private static String captchaText = "";
    private String FACULTY_INFO_pre = "https://academics.vit.ac.in/student/fac_profile.asp";
    private String FACULTY_INFO_LINK = "https://academics.vit.ac.in/student/getfacdet.asp?fac=";
    private static String fac_det_pre = "https://academics.vit.ac.in/student/";
    public static String employee_fac_pic = "https://academics.vit.ac.in/student/emp_photo.asp";
    private boolean isSingleEntry = false;

    public GetFacDataStudLogin() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vs = inflater.inflate(R.layout.fac_details_new, container, false);
        captcha_edit_student = (EditText) vs.findViewById(R.id.captcha_fac_info_et);
        pbar = (ProgressBarCircularIndeterminate) vs.findViewById(R.id.progress_facinfo);
        facname = (EditText) vs.findViewById(R.id.fac_info_name);
        captcha_edit_student.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                captchaText = charSequence.toString();
                if (charSequence.length() != 6)
                    assign_stud_pic.setEnabled(false);
                else assign_stud_pic.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //     tv_stud = (TextView) vs.findViewById(R.id.tv_stud);
        assign_stud_pic = (ButtonRectangle) vs.findViewById(R.id.fac_info_submit);
        captcha_student = (ImageView) vs.findViewById(R.id.captcha_fac_info_iv);
        try {
            new LoadCaptcha().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        assign_stud_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String back;
                back = facnamestr = facname.getText().toString().trim();
                char cr[] = facnamestr.toCharArray();
                facnamestr = "";
                for (char a : cr) {
                    if (a == ' ')
                        facnamestr += "%20";
                    else
                        facnamestr += a;

                }
                FACULTY_INFO_LINK += facnamestr;
                facnamestr = back;//as we check for contains() later
                new GetFacData().execute();
            }
        });
        return vs;
    }


    private class LoadCaptcha extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
            HttpConnectionParams.setSoTimeout(httpParameters, 15000);
            httpClient = MySSLSocketFactory.getNewHttpClient();
            try {
                final Bitmap bmp = BitmapUrlClient.getBitmapFromURL(student_Login_Captcha_Link, httpClient);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        captcha_student.setImageBitmap(bmp);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static class bmparr {
        String[] data;
        Bitmap bmp;
        List<OpenHours> open;
    }

    private class GetFacData extends AsyncTask<Void, bmparr, bmparr> {
        @Override
        protected void onPreExecute() {
            pbar.setVisibility(View.VISIBLE);
            assign_stud_pic.setEnabled(false);
        }

        @Override
        protected bmparr doInBackground(Void... voids) {
            String response = "";
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
            try {
                final String facresp = Http.getData(FACULTY_INFO_LINK, httpClient);
                Log.d("fACE", facresp);
                if (!facresp.contains(facnamestr.toUpperCase())) {//names are in upper caseint x = 1 % 0;//invalid statement to invoke catch block
                    int x = 1 % 0;
                }
                Elements faculties = Jsoup.parse(facresp).getElementsByTag("table").get(0)//table 1
                        .getElementsByTag("tr");
                String hrefTR1 = faculties.get(1).getElementsByTag("td").get(3).getElementsByTag("a").get(0).attr("href").trim();
                faculties.remove(0);
                if (faculties.size() > 1) {
                    final List<MutlipleResultFac> mult = new ArrayList<>();
                    for (Element r : faculties) {
                        Log.d("namew", r.toString());
                        MutlipleResultFac mod = new MutlipleResultFac();
                        String name = r.getElementsByTag("td").get(0).getElementsByTag("font").get(0).html().trim();
                        Log.d("name3", name);
                        String school = r.getElementsByTag("td").get(2).getElementsByTag("font").get(0).html().trim();
                        Log.d("name2", school);
                        String href = r.getElementsByTag("td").get(3).getElementsByTag("a").get(0).attr("href").trim();
                        Log.d("name1", href);
                        mod.name = name;
                        mod.school = school;
                        mod.href = href;
                        mult.add(mod);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        public void run() {
                            getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_left_in, R.anim.push_left_out)
                                    .add(R.id.fac_info_getdata_frame, new MultipleTeacherListFrag(mult))
                                    .addToBackStack(null)
                                    .commit();
                        }
                    });
                    return null;
                } else {
                    bmparr data = parsefacidlink(hrefTR1);
                    return data;
                }
            } catch (Exception e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            new LoadCaptcha().execute();//reload captcha
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "Invalid Name", Toast.LENGTH_SHORT).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(bmparr strings) {
            pbar.setVisibility(View.GONE);
            if (strings != null) {
                try {
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.push_left_in, R.anim.push_left_out, R.anim.push_left_in, R.anim.push_left_out)
                            .add(R.id.fac_info_getdata_frame, new FacInfoFrag(strings))
                            .addToBackStack(null)
                            .commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static bmparr parsefacidlink(String href) {

        String content = Http.getData(fac_det_pre + href, httpClient);
        Elements data = Jsoup.parse(content).getElementsByTag("table").get(1).getElementsByTag("tr");
        data.remove(0);
        String pass[] = new String[8];
        String name = pass[0] = new ParseTimeTable().FirstCharCap(data.get(0).getElementsByTag("td").get(1).html().trim());
        String school = data.get(1).getElementsByTag("td").get(1).html().trim();
        school = pass[1] = school.replaceAll("amp;", "");
        String designation = pass[2] = data.get(2).getElementsByTag("td").get(1).html().trim();
        String room = pass[3] = data.get(3).getElementsByTag("td").get(1).html().trim();
        String email = pass[4] = data.get(5).getElementsByTag("td").get(1).html().trim();//Skipping 4th as its intercom
        String divi = "";
        String openhs = "";
        String addrole = "";
        List<OpenHours> open = new ArrayList<>();

        try {
            //in the html, they were blank , so a precaution
            divi = pass[5] = data.get(6).getElementsByTag("td").get(1).html().trim();
            addrole = pass[6] = data.get(7).getElementsByTag("td").get(1).html().trim();
            openhs = pass[7] = data.get(8).getElementsByTag("td").get(1).html().trim();
            try {
                Elements openhrs = data.get(8).getElementsByTag("td").get(1).getElementsByTag("table").get(0).getElementsByTag("tr");//it gets big, if available
                openhrs.remove(0);
                for (Element ele : openhrs) {
                    OpenHours or = new OpenHours();
                    or.day = ele.getElementsByTag("td").get(0).html();
                    or.from = ele.getElementsByTag("td").get(1).html();
                    or.to = ele.getElementsByTag("td").get(2).html();
                    open.add(or);
                }
            } catch (Exception e) {
                e.printStackTrace();
                open = null;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        bmparr beep = new bmparr();
        beep.open = open;
        beep.data = pass;
        try {
            final Bitmap fac_pic = BitmapUrlClient.getBitmapFromURL(employee_fac_pic, httpClient);
            if (fac_pic != null) {
                beep.bmp = fac_pic;
            } else {
                beep.bmp = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return beep;
    }

    static class OpenHours {
        String day;
        String from;
        String to;
    }

    class MutlipleResultFac {
        String name;
        String href;
        String school;
    }
}
//Excluded Code
// String str = Http.getData(per, httpClient);//need to do this as it get's some ccokie ,otherwise image is null
//final Bitmap b = getBitmapFromURL(student_Login_Photo_Link, httpClient);
//final String spot = Http.getData(SPOTLIGHT, httpClient);
//final Bitmap bmp = getBitmapFromURL(student_Login_Photo_Link, httpClient);
//final String d = Http.getData("https://academics.vit.ac.in/student/profile_personal_view.asp", httpClient);
// if (bmp == null) {
//}
//            getActivity().runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
////                        Fragment frag = new Spotlight_WebView();
////                        Bundle da = new Bundle();
////                        da.putString("url_jack", spot);
////                        frag.setArguments(da);
////                        getActivity().getSupportFragmentManager().beginTransaction()
////                                .replace(R.id.stud_log_frag, frag).addToBackStack(null).commit();
////                    if (b != null)
////                        captcha_student.setImageBitmap(b);
////                    else
////                        Toast.makeText(getActivity(), "Its null", Toast.LENGTH_SHORT).show();
//                }
//            });