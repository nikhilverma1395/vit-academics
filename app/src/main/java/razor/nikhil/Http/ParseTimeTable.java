package razor.nikhil.Http;

import android.content.Context;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.database.Slots_GetSet;
import razor.nikhil.model.Model_Daywise;
import razor.nikhil.model.Model_Slots;
import razor.nikhil.model.Slots_Timings;

/**
 * Created by Nikhil Verma on 9/12/2015.
 */
public class ParseTimeTable {
    private final HttpClient client;
    private final Context context;
    private List<Model_Slots> list_slots;

    private Thread th[] = new Thread[2];

    public ParseTimeTable(Context ctxt, HttpClient client) {
        this.client = client;
        this.context = ctxt;
    }


    public static String FirstCharCap(String source) {
        if (source.trim().equals("") || source == "" || source == null || source.equalsIgnoreCase("N/A")) {
            return "N/A";
        }
        StringBuffer res = new StringBuffer();
        String[] strArr = source.toLowerCase().split(" ");
        for (String str : strArr) {
            char[] stringArray = str.trim().toCharArray();
            stringArray[0] = Character.toUpperCase(stringArray[0]);
            str = new String(stringArray);
            res.append(str).append(" ");
        }
        return res.toString().trim();
    }

    public void parser(String tt) {
        Document document = Jsoup.parse(tt);
        Element el = document.getElementsByTag("table").get(1);
        Elements dd = el.getElementsByTag("tr");
        dd.remove(0);
        dd.remove(dd.size() - 1);
        dd.remove(dd.size() - 1);
        list_slots = new ArrayList<>();
        for (int t = 0; t < dd.size(); t++) {
            Element one = dd.get(t);
            Elements ui = one.getElementsByTag("td");
            Model_Slots mmodel = new Model_Slots();
            if (ui.size() != 10) {
                for (int i = 0; i < ui.size(); i++) {
                    if (i == 0) continue;
                    String d = ui.get(i).getElementsByTag("font").get(0).html().trim();
                    if (i == 1) mmodel.setNumber(d);
                    if (i == 2) mmodel.setClass_number(d);
                    if (i == 3) mmodel.setCode(d);
                    if (i == 4) mmodel.setSubject_name(d);
                    if (i == 5) mmodel.setCourse_type(d);
                    if (i == 6) mmodel.setLTPJC(d);
                    if (i == 7) mmodel.setCourse_mode(d);
                    if (i == 8) mmodel.setCourse_option(d);
                    if (i == 9) mmodel.setSlot(d);
                    if (i == 10) mmodel.setVenue(d);
                    if (i == 11) mmodel.setTeacher(FirstCharCap(d));
                    if (i == 12) mmodel.setStatus(d);
                }
                list_slots.add(mmodel);
            } else {
                for (int i = 0; i < ui.size(); i++) {
                    String d = ui.get(i).getElementsByTag("font").get(0).html().trim();
                    if (i == 0) mmodel.setClass_number(d);
                    if (i == 1) mmodel.setCode(d);
                    if (i == 2) mmodel.setSubject_name(d);
                    if (i == 3) mmodel.setCourse_type(d);
                    if (i == 4) mmodel.setLTPJC(d);
                    if (i == 5) mmodel.setCourse_mode(d);
                    if (i == 6) mmodel.setCourse_option(d);
                    if (i == 7) mmodel.setSlot(d);
                    if (i == 8) mmodel.setVenue(d);
                    if (i == 9) mmodel.setTeacher(FirstCharCap(d));
                    mmodel.setNumber("Lab");
                    mmodel.setStatus("Lab");
                }
                list_slots.add(mmodel);
            }
        }

        try {
            Slots_GetSet mes = new Slots_GetSet(context);
            if (mes.getEntriesCount() == 0) {
                mes.Delete();
                for (Model_Slots ms : list_slots) {
                    mes.create(ms);
                }
                context.getSharedPreferences("VitAcademics_sp", Context.MODE_PRIVATE).edit().putString("slots_down", "yes").commit();
            } else {
                mes.Delete();
                for (Model_Slots ms : list_slots) {
                    mes.create(ms);
                }
                context.getSharedPreferences("VitAcademics_sp", Context.MODE_PRIVATE).edit().putString("slots_down", "already").commit();
            }
            List<Model_Slots> ms = list_slots;
            th[0] = new Thread(new Runnable() {
                public void run() {
                    new PostStudFragPre(context, client).MYT(tnames(list_slots));
                }
            });
            th[0].start();
        } catch (Exception e) {
            e.printStackTrace();
            context.getSharedPreferences("VitAcademics_sp", Context.MODE_PRIVATE).edit().putString("slots_down", "no").commit();
        }

        String[] days = {"mon", "tue", "wed", "thur", "fri"};
        final List<List<Model_Daywise>> alldayslist = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            List<Model_Daywise> daywiseList = new ArrayList<>();
            List<Slots_Timings.Time_Code> list = (new Slots_Timings()).getTimings(days[i]);
            for (Model_Slots fd : list_slots) {
                for (Slots_Timings.Time_Code ter : list) {
                    try {
                        if (fd.getSlot().contains(ter.getSlot())) {
                            daywiseList.add(new Model_Daywise(fd.getSubject_name(), fd.getCode(), fd.getSlot(), ter.getTime(), fd.getTeacher(), fd.getVenue(), fd.getCourse_type()));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        if (fd.getSlot().contains(ter.getLab_slot())) {
                            daywiseList.add(new Model_Daywise(fd.getSubject_name() + "( Embedded Lab )", fd.getCode(), ter.getLab_slot(), ter.getLab_time(), fd.getTeacher(), fd.getVenue(), fd.getCourse_type()));
                        }
                    } catch (Exception e) {
                        //       e.printStackTrace();
                    }
                }

            }
            alldayslist.add(daywiseList);
        }
        th[1] = new Thread(new Runnable() {
            public void run() {
                new StoreTimeTable(context).insertMTWTFinDB(alldayslist);
            }
        });
        th[1].start();
        for (Thread thw : th) {
            try {
                if (thw != null)
                    thw.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    public String[] tnames(List<Model_Slots> mo) {
        String[] tnames = new String[mo.size()];
        int y = 0;
        for (Model_Slots ms : mo) {
            tnames[y++] = ms.getTeacher();
        }
        return tnames;
    }

}
