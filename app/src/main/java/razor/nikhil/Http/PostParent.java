package razor.nikhil.Http;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import razor.nikhil.Config;
import razor.nikhil.Fragments.GetDetails;
import razor.nikhil.database.GradeGetSet;
import razor.nikhil.database.SharedPrefs;
import razor.nikhil.database.StoreAndGetImage;
import razor.nikhil.model.AptModel;

/**
 * Created by Nikhil Verma on 9/12/2015.
 */
public class PostParent {
    public static final String ATTENDANCE_URL = "https://academics.vit.ac.in/student/attn_report.asp?sem=";
    public static final String TIMETABLE_URL = "https://academics.vit.ac.in/student/timetable_";
    public static final String MARKS_URL = "https://academics.vit.ac.in/student/marks.asp?sem=";
    public final String GRADE_URL = "https://academics.vit.ac.in/student/student_history.asp";
    public final String FAC_PHOTO = "https://academics.vit.ac.in/student/emp_photo.asp";
    public final String FACULTY_DETAILS = "https://academics.vit.ac.in/student/faculty_advisor_view.asp";
    public static final String winter_sem = "WS";//Jan-May
    public static final String fall_sem = "FS";//July-Nov
    public static final String summer_sem = "SS";//June
    public static final String tri_sem = "TS";//Dec
    public final ProgressDialog dialog;
    public static String whichsem = "";
    public String uname, pass, captchatxt;
    public HttpClient httpClient;
    public FragmentActivity context;
    Thread[] threads;
    public String[] TNAMES;
    String todayDate = "";
    public long in, out;

    public static String getSem() {
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
        return whichsem.trim();
    }

    public PostParent(String reg, String pass, String capt, HttpClient htp, FragmentActivity ctxt, ProgressDialog dialog) {
        threads = new Thread[8];
        this.dialog = dialog;
        this.captchatxt = capt;
        this.pass = pass;
        this.httpClient = htp;
        this.uname = reg;
        context = ctxt;
        getSem();
        todayDate = new SimpleDateFormat("dd-MMM-yyyy", Locale.UK).format(new Date());
        in = System.nanoTime();
        new ParentLoginPost().execute();
    }

    public static String getTodayDateInFormat() {
        String texas = new SimpleDateFormat("dd-MMM-yyyy", Locale.UK).format(new Date());
        return texas;
    }

    public class ParentLoginPost extends AsyncTask<Void, String[], Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            final String credMes = "Something went wrong, Check your Credentials and Connection!";
            httpClient = Logins.StudentLogin(uname, pass, captchatxt, httpClient);
            if (Logins.isStudLogin.equals("n")) {
                context.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Toast.makeText(context, credMes, Toast.LENGTH_SHORT).show();
                        new GetDetails().reloadCaptcha();
                    }
                });
                return null;
            }

            String facdata = "";
            //FacAdv Thread
            try {
                facdata = Http.getData("https://academics.vit.ac.in/student/stud_home.asp", httpClient);//pre for most
            } catch (Exception e) {
                e.printStackTrace();
            }
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
                        final String timetableHTML = Http.getData(TIMETABLE_URL + whichsem.toLowerCase() + ".asp", httpClient);
                        Log.d("data", timetableHTML);
                        new ParseTimeTable(context, httpClient).parser(timetableHTML);
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
                        String dataq = Http.getData("https://academics.vit.ac.in/student/attn_report.asp?sem=" + whichsem, httpClient);
                        String from_date = Jsoup.parse(dataq).getElementsByTag("table").get(1)
                                .getElementsByTag("tr").get(2).getElementsByTag("input").get(0).attr("value");
                        final String attendanceHTML = Http.getData(ATTENDANCE_URL + whichsem + "&fmdt=" + from_date + "&todt=" + todayDate, httpClient);
                        try {
                            new AttendanceParser(context, httpClient).parse(attendanceHTML);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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
                        try {
                            new MarksParser(context, marksHTML);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                threads[4].start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                threads[5] = new Thread(new Runnable() {
                    public void run() {
                        String data = Http.getData("https://academics.vit.ac.in/student/apt_attendance.asp", httpClient);
                        final List<AptModel> listFac = new PostStudFragPre(context, httpClient).parse(data);
                    }
                });
                threads[5].start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Apt Data up
            //MyTeach down
            try {
                final String fda = facdata;
                threads[6] = new Thread(new Runnable() {
                    public void run() {
                        new PostStudFragPre(context, httpClient).parseFacMsg(fda);
                    }
                });
                threads[6].start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                final SharedPrefs prefs = new SharedPrefs(context);
                threads[7] = new Thread(new Runnable() {
                    public void run() {
                        String DATA = Http.getData("https://academics.vit.ac.in/student/leave_request.asp", httpClient);
                        Elements names = Jsoup.parse(DATA).getElementsByTag("table").get(1)
                                .getElementsByTag("tr").get(1).getElementsByTag("td").get(1).getElementsByTag("option");
                        String facadv = names.get(1).html();
                        String pm = names.get(2).html();
                        Log.d("facadv", facadv);
                        Log.d("ProgramM", pm);
                        prefs.storeMsg(Config.PM_NAME_CODE_LEAVE_VALUE, names.get(2).attr("value"));
                        prefs.storeMsg(Config.FADV_NAME_CODE_LEAVE_VALUE, names.get(1).attr("value"));
                        prefs.storeMsg(Config.PM_NAME_CODE_LEAVE, pm);
                        prefs.storeMsg(Config.FADV_NAME_CODE_LEAVE, facadv);
                    }
                });
                threads[7].start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (Thread th : threads) {
                try {
                    if (th != null)
                        th.join();//waits fot thread to exit
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            out = System.nanoTime();
            Log.d("TimeTaken", (out - in) / 1000000 + "\tmillisec");
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d("Done Now", "Done");
            dialog.setMessage("Finished");
            dialog.dismiss();
        }

    }

    public void parseFaculty(String fadv) {
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
        pref.storeMsg(Config.FADV_NAME, new ParseTimeTable(null, null).FirstCharCap(name));
        pref.storeMsg(Config.FADV_DESIGNATION, designation);
        pref.storeMsg(Config.FADV_SCHOOL, school);
        pref.storeMsg(Config.FADV_MOBILE, mobile);
        pref.storeMsg(Config.FADV_EMAIL, email);
        pref.storeMsg(Config.FADV_ROOM, room);
    }
}
