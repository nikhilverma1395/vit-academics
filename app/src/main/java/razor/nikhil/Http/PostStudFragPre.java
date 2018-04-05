package razor.nikhil.Http;

import android.content.Context;
import android.text.Html;
import android.util.Log;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.Config;
import razor.nikhil.Fragments.MyTeachers;
import razor.nikhil.database.APT_GS;
import razor.nikhil.database.FacMsgGS;
import razor.nikhil.database.MyTeachGS;
import razor.nikhil.database.SharedPrefs;
import razor.nikhil.model.AptModel;
import razor.nikhil.model.FacMsgModel;
import razor.nikhil.model.MyTeacherDet;

/**
 * Created by Nikhil Verma on 10/4/2015.
 */
public class PostStudFragPre {
    private final Context context;
    private final HttpClient client;
    private String FACULTY_INFO_pre = "https://academics.vit.ac.in/student/fac_profile.asp";
    private String FACULTY_INFO_LINK = "https://academics.vit.ac.in/student/getfacdet.asp?fac=";
    private String fac_det_pre = "https://academics.vit.ac.in/student/";
    private String employee_fac_pic = "https://academics.vit.ac.in/student/emp_photo.asp";
    List<MyTeacherDet> list = new ArrayList<>();

    public PostStudFragPre(Context context, HttpClient client) {
        this.client = client;
        this.context = context;
    }

    public List<AptModel> parse(String data) {
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
                    SharedPrefs pref = new SharedPrefs(context);
                    if (r == trS.size() - 3)
                        pref.storeMsg(Config.APT_TOTAL_CLASSES, tdS.get(1).html());
                    if (r == trS.size() - 2)
                        pref.storeMsg(Config.APT_ATTENDED, tdS.get(1).html());
                    if (r == trS.size() - 1)
                        pref.storeMsg(Config.APT_PERCENT, tdS.get(1).html());
                }

            }
            try {
                APT_GS gs = new APT_GS(context);
                if (gs.getEntriesCount() == 0)
                    gs.createList(list);
                else {
                    gs.Delete();
                    gs.createList(list);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void MYT(String[] tn) {
        final Thread thread[] = new Thread[tn.length];
        for (int y = 0; y < tn.length; y++) {
            String name = tn[y];
            char[] kack = name.toCharArray();
            name = "";
            for (char cr : kack) {
                if (cr != '-') {
                    name += cr;
                } else break;
            }
            name = name.toUpperCase().trim();
            final String fname = name;
            thread[y] = new Thread(new Runnable() {
                @Override
                public void run() {
                    getThisTeacher(fname);
                }

                private void getThisTeacher(String fname) {
                    try {
                        final String facresp = Http.getData(FACULTY_INFO_LINK + MyTeachers.getwithPERCname(fname), client);
                        MyTeacherDet mmod = parsefacidlink(facresp);
                        Log.d("Done for", fname);
                        list.add(mmod);
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.d("Faculty Info Not av", fname);
                    }
                }
            });
            thread[y].start();
        }
        for (Thread th : thread) {
            if (th != null) try {
                th.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d("Teachers Done", "");
        inserindb(list);
    }

    private void inserindb(List<MyTeacherDet> list) {
        MyTeachGS sql = new MyTeachGS(context);
        try {
            sql.Delete();
            sql.createList(list);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<FacMsgModel> parseFacMsg(String dataw) {
        int gap = 0;
        List<FacMsgModel> list = new ArrayList<>();
        Elements data = null;
        try {
            data = Jsoup.parse(dataw).getElementsByTag("table");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Elements datas = null;
        try {
            datas = data.get(data.size() - 1).getElementsByTag("tr");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
            FacMsgGS gs = new FacMsgGS(context);
            if (gs.getEntriesCount() == 0) {
                gs.createList(list);
            } else {
                gs.Delete();
                gs.createList(list);
            }
        }
        return list;
    }

    class OpenHours {
        String day;
        String from;
        String to;
    }

    public MyTeacherDet parsefacidlink(String source) {
        MyTeacherDet mod = new MyTeacherDet();
        String href = Jsoup.parse(source).getElementsByTag("table").get(0)//table 1
                .getElementsByTag("tr").get(1)//tr - 2
                .getElementsByTag("td").get(3)//td- 4
                .getElementsByTag("a").get(0).attr("href");//a - 1
        String content = Http.getData(fac_det_pre + href, client);
        Elements data = Jsoup.parse(content).getElementsByTag("table").get(1).getElementsByTag("tr");
        data.remove(0);
        String name = new ParseTimeTable(null, null).FirstCharCap(data.get(0).getElementsByTag("td").get(1).html().trim());
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

}
