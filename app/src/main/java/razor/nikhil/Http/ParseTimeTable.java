package razor.nikhil.Http;

import android.content.Context;

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
    private Context context;

    public ParseTimeTable(Context ctxt) {
        this.context = ctxt;
    }

    public ParseTimeTable() {
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
        Elements el = document.getElementsByTag("table");

        Element eel = el.get(0).getElementsByTag("table").get(1);//.getElementsByTag("table").get(0).getElementsByTag("tr");
        Elements dd = eel.getElementsByTag("tr");
        dd.remove(0);
        dd.remove(dd.size() - 1);
        List<Model_Slots> list_slots = new ArrayList<>();
        for (int t = 0; t < dd.size(); t++) {
            Element one = dd.get(t);
            Elements ui = one.getElementsByTag("td");
            Model_Slots mmodel = new Model_Slots();
            if (ui.size() != 8) {
                for (int i = 0; i < ui.size(); i++) {
                    String d = ui.get(i).getElementsByTag("font").get(0).html().toString();
                    if (i == 0) mmodel.setNumber(d);
                    if (i == 1) mmodel.setCode(d);
                    if (i == 2) mmodel.setSubject_name(d);
                    if (i == 3) mmodel.setClass_number(d);
                    if (i == 4) mmodel.setCourse_type(d);
                    if (i == 5) mmodel.setLTPJC(d);
                    if (i == 6) mmodel.setCourse_mode(d);
                    if (i == 7) mmodel.setCourse_option(d);
                    if (i == 8) mmodel.setSlot(d);
                    if (i == 9) mmodel.setVenue(d);
                    if (i == 10) mmodel.setTeacher(FirstCharCap(d));
                    if (i == 11) mmodel.setStatus(d);
                }
                list_slots.add(mmodel);
            } else {

                for (int i = 0; i < ui.size(); i++) {
                    String d = ui.get(i).getElementsByTag("font").get(0).html().toString();
                    if (i == 0) mmodel.setClass_number(d);
                    if (i == 1) mmodel.setCourse_type(d);
                    if (i == 2) mmodel.setLTPJC(d);
                    if (i == 3) mmodel.setCourse_mode(d);
                    if (i == 4) mmodel.setCourse_option(d);
                    if (i == 5) mmodel.setSlot(d);
                    if (i == 6) mmodel.setVenue(d);
                    if (i == 7) mmodel.setTeacher(FirstCharCap(d));
                    if (t != 0)
                        mmodel.setSubject_name(list_slots.get(t - 1).getSubject_name());
                    else mmodel.setSubject_name("Lab");
                    mmodel.setNumber("Lab");
                    mmodel.setCode("Lab");
                    mmodel.setStatus("Lab");
                }
                list_slots.add(mmodel);
            }
        }
        try {
            Slots_GetSet mes = new Slots_GetSet(context);
            if (mes.getEntriesCount() == 0) {

                for (Model_Slots ms : list_slots) {
                    mes.create(ms);
                }
                context.getSharedPreferences("VitAcademics_sp", Context.MODE_PRIVATE).edit().putString("slots_down", "yes").commit();
            } else {
                context.getSharedPreferences("VitAcademics_sp", Context.MODE_PRIVATE).edit().putString("slots_down", "already").commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
            context.getSharedPreferences("VitAcademics_sp", Context.MODE_PRIVATE).edit().putString("slots_down", "no").commit();
        }

        String[] days = {"mon", "tue", "wed", "thur", "fri"};
        List<List<Model_Daywise>> alldayslist = new ArrayList<>();

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
        new StoreTimeTable(context).insertMTWTFinDB(alldayslist);

    }
}
