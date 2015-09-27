package razor.nikhil.Http;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import razor.nikhil.Config;
import razor.nikhil.Activity.MainActivity;
import razor.nikhil.database.GradeGetSet;
import razor.nikhil.database.SharedPrefs;
import razor.nikhil.database.StoreAndGetImage;

/**
 * Created by Nikhil Verma on 9/12/2015.
 */
public class PostParent {
    private final String ATTENDANCE_URL = "https://academics.vit.ac.in/parent/attn_report.asp?sem=";
    private final String PARENT_LOGIN_POST_URL = "https://academics.vit.ac.in/parent/parent_login_submit.asp";
    private final String TIMETABLE_URL = "https://academics.vit.ac.in/parent/timetable.asp?sem=";
    private final String MARKS_URL = "https://academics.vit.ac.in/parent/marks.asp?sem=";
    private final String GRADE_URL = "https://academics.vit.ac.in/parent/student_history.asp";
    private final String FAC_PHOTO = "https://academics.vit.ac.in/parent/emp_photo.asp";
    private final String FACULTY_DETAILS = "https://academics.vit.ac.in/parent/fa_view.asp";
    private final String winter_sem = "WS";//Jan-May
    private final String fall_sem = "FS";//July-Nov
    private final String summer_sem = "SS";//June
    private final String tri_sem = "TS";//Dec
    private String whichsem = "";
    private String uname, dob, ward, captchatxt;
    private HttpClient httpClient;
    private Context context;

    public PostParent(String reg, String dob, String cell, String capt, HttpClient htp, Context ctxt) {
        this.captchatxt = capt;
        this.dob = dob;
        this.httpClient = htp;
        this.uname = reg;
        this.ward = cell;
        context = ctxt;
        //To get Sem-code
        Calendar c = Calendar.getInstance();
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DAY_OF_MONTH);

        if (month >= 0 && month <= 4) {
            whichsem = winter_sem;
        }
        if (month == 5) {
            whichsem = summer_sem;
        }
        if (month == 11) {
            whichsem = tri_sem;
        }
        if (month >= 6 && month <= 10) {
            whichsem = fall_sem;
        }

        new ParentLoginPost().execute(PARENT_LOGIN_POST_URL);
    }


    private class ParentLoginPost extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... voids) {
            // Create a new HttpClient and Post Header
            HttpPost httppost = null;
            try {
                httppost = new HttpPost(voids[0]);
                List<NameValuePair> nameValuePair = new ArrayList<>(4);

                nameValuePair.add(new BasicNameValuePair("wdregno", uname));
                nameValuePair.add(new BasicNameValuePair("wdpswd", dob));
                nameValuePair.add(new BasicNameValuePair("wdmobno", ward));
                nameValuePair.add(new BasicNameValuePair("vrfcd", captchatxt));
                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    HttpResponse response = httpClient.execute(httppost);
                    final String op = EntityUtils.toString(response.getEntity(), "UTF-8");
                    Log.d("Post in Parent", "Done");
                    Log.d("PostData", op);
                    try {
                        final String fadv = Http.getData(FACULTY_DETAILS, httpClient);
                        Bitmap facpic = null;
                        try {
                            facpic = BitmapUrlClient.getBitmapFromURL(FAC_PHOTO, httpClient);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (facpic == null) {
                            Log.d("Null", "yep");
                        }
                        String storedpath = null;
                        try {
                            storedpath = new StoreAndGetImage(context).saveToInternalSorage(facpic, Config.FACULTY_IMAGE_NAME);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        Log.d("Path", storedpath);
                        new SharedPrefs(context).storeMsg(Config.FADV_PIcKEY, storedpath);
                        parseFaculty(fadv);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Runnable runnable = new Runnable() {
                            public void run() {
                                if (!(new GradeGetSet(context).getEntriesCount() == 0)) {
                                    Log.d("Grades Data Already", "Done");
                                    return;
                                }
                                final String grades = Http.getData(GRADE_URL, httpClient);
                                new GradeParser(context).parser(grades);
                            }
                        };
                        new Thread(runnable).start();
                    } catch (Exception e) {
                        e.printStackTrace();

                    }


                    try {
                        new Thread(new Runnable() {
                            public void run() {
                                final String timetableHTML = Http.getData(TIMETABLE_URL + whichsem, httpClient);
                                new ParseTimeTable(context).parser(timetableHTML);
                                Log.d("parsett", "Done");
                            }
                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        new Thread(new Runnable() {
                            public void run() {
                                final String attendanceHTML = Http.getData(ATTENDANCE_URL + whichsem, httpClient);
                                new AttendanceParser(context, httpClient).parse(attendanceHTML);
                                Log.d("atten", "Done");
                            }
                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        new Thread(new Runnable() {
                            public void run() {
                                final String marksHTML = Http.getData(MARKS_URL + whichsem, httpClient);
                                new MarksParser(context, marksHTML);
                                Log.d("marks", "Done");
                            }
                        }).start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            while (true) {
                String mark = new SharedPrefs(context).getMsg("marksdone");
                String att = new SharedPrefs(context).getMsg("ttdone");
                String tt = new SharedPrefs(context).getMsg("attendone");
                if (mark == "y" && att == "y" && tt == "y") {
                    Log.d("Yepp", "Done");
                    MainActivity ob = new MainActivity();
                    ob.setMTWTFLists(context);
                    //    ob.removeFrag(context);
                    return;
                }
            }
        }
    }

    private void parseFaculty(String fadv) {
        SharedPrefs pref = new SharedPrefs(context);
        Document doc = Jsoup.parse(fadv);
        Elements tab = doc.getElementsByTag("table");
        Element fac = tab.get(1);
        Elements ele = fac.getElementsByTag("tr");
        ele.remove(0);
        String name = Jsoup.parse(ele.get(0).getElementsByTag("td").get(1).html()).getElementsByTag("font").first().html();
        String designation = Jsoup.parse(ele.get(1).getElementsByTag("td").get(1).html()).getElementsByTag("font").first().html();
        String school = Jsoup.parse(ele.get(2).getElementsByTag("td").get(1).html()).getElementsByTag("font").first().html();
        String mobile = Jsoup.parse(ele.get(4).getElementsByTag("td").get(1).html()).getElementsByTag("font").first().html();
        String email = Jsoup.parse(ele.get(5).getElementsByTag("td").get(1).html()).getElementsByTag("font").first().html();
        String room = Jsoup.parse(ele.get(6).getElementsByTag("td").get(1).html()).getElementsByTag("font").first().html();
        pref.storeMsg(Config.FADV_NAME, new ParseTimeTable().FirstCharCap(name));
        pref.storeMsg(Config.FADV_DESIGNATION, designation);
        pref.storeMsg(Config.FADV_SCHOOL, school);
        pref.storeMsg(Config.FADV_MOBILE, mobile);
        pref.storeMsg(Config.FADV_EMAIL, email);
        pref.storeMsg(Config.FADV_ROOM, room);
    }
}
