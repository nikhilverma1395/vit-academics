package razor.nikhil.Http;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import org.apache.http.HttpException;
import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;

import razor.nikhil.Config;
import razor.nikhil.Fragments.GetDetails;
import razor.nikhil.database.GradeGetSet;
import razor.nikhil.database.SharedPrefs;
import razor.nikhil.database.StoreAndGetImage;

/**
 * Created by Nikhil Verma on 9/12/2015.
 */
public class PostParent {
    private final String ATTENDANCE_URL = "https://academics.vit.ac.in/parent/attn_report.asp?sem=";
    public static final String PARENT_LOGIN_POST_URL = "https://academics.vit.ac.in/parent/parent_login_submit.asp";
    private final String TIMETABLE_URL = "https://academics.vit.ac.in/parent/timetable.asp?sem=";
    private final String MARKS_URL = "https://academics.vit.ac.in/parent/marks.asp?sem=";
    private final String GRADE_URL = "https://academics.vit.ac.in/parent/student_history.asp";
    private final String FAC_PHOTO = "https://academics.vit.ac.in/parent/emp_photo.asp";
    private final String FACULTY_DETAILS = "https://academics.vit.ac.in/parent/fa_view.asp";
    private final String winter_sem = "WS";//Jan-May
    private final String fall_sem = "FS";//July-Nov
    private final String summer_sem = "SS";//June
    private final String tri_sem = "TS";//Dec
    private final ProgressDialog dialog;
    private final GetDetails frag;
    private String whichsem = "";
    private String uname, dob, ward, captchatxt;
    private HttpClient httpClient;
    private FragmentActivity context;
    Thread[] threads;

    public PostParent(String reg, String dob, String cell, String capt, HttpClient htp, FragmentActivity ctxt, GetDetails frag, ProgressDialog dialog) {
        threads = new Thread[5];
        this.dialog = dialog;
        this.frag = frag;
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
            HashMap<String, String> headers = new HashMap<>();
            headers.put("wdregno", uname);
            headers.put("wdpswd", dob);
            headers.put("wdmobno", ward);
            headers.put("vrfcd", captchatxt);
            try {
                Http.postMethod(voids[0], headers, httpClient);
            } catch (HttpException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }//FacAdv Thread
            try {
                Runnable r1 = new Runnable() {
                    public void run() {
                        final String fadv = Http.getData(FACULTY_DETAILS, httpClient);
                        Bitmap facpic = null;
                        try {
                            facpic = BitmapUrlClient.getBitmapFromURL(FAC_PHOTO, httpClient);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (facpic == null) {
                            Log.d("Null", "Faculty Adv Image is Null");
                        }
                        String storedpath = null;
                        try {
                            storedpath = new StoreAndGetImage(context).saveToInternalSorage(facpic, Config.FACULTY_IMAGE_NAME);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        new SharedPrefs(context).storeMsg(Config.FADV_PIcKEY, storedpath);
                        try {
                            parseFaculty(fadv);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                threads[0] = new Thread(r1);
                threads[0].start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Grades Thread
            try {
                Runnable r2 = new Runnable() {
                    public void run() {
                        if (!(new GradeGetSet(context).getEntriesCount() == 0)) {
                            Log.d("Grades Data Already", "Done");
                            return;
                        }
                        final String grades = Http.getData(GRADE_URL, httpClient);
                        new GradeParser(context).parser(grades);
                    }
                };
                threads[1] = new Thread(r2);
                threads[1].start();
            } catch (Exception e) {
                e.printStackTrace();

            }
            //TT Thread
            try {
                threads[2] = new Thread(new Runnable() {
                    public void run() {
                        final String timetableHTML = Http.getData(TIMETABLE_URL + whichsem, httpClient);
                        new ParseTimeTable(context).parser(timetableHTML);
                    }
                });
                threads[2].start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Attendance Thread
            try {
                threads[3] = new Thread(new Runnable() {
                    public void run() {
                        final String attendanceHTML = Http.getData(ATTENDANCE_URL + whichsem, httpClient);
                        new AttendanceParser(context, httpClient).parse(attendanceHTML);
                    }
                });
                threads[3].start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Marks Thread
            try {
                threads[4] = new Thread(new Runnable() {
                    public void run() {
                        final String marksHTML = Http.getData(MARKS_URL + whichsem, httpClient);
                        new MarksParser(context, marksHTML);
                    }
                });
                threads[4].start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (Thread th : threads) {
                try {
                    th.join();//waits fot thread to exit
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("Done Now", "Done");
            dialog.setMessage("Finished");
            dialog.dismiss();
            try {
                context.getSupportFragmentManager().beginTransaction().remove(frag);
            } catch (Exception e) {
                e.printStackTrace();
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
