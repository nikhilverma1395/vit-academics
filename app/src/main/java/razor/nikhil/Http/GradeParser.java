package razor.nikhil.Http;

import android.content.Context;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.Activity.Config;
import razor.nikhil.database.GradeGetSet;
import razor.nikhil.database.SharedPrefs;
import razor.nikhil.model.GradeModel;

/**
 * Created by Nikhil Verma on 9/16/2015.
 */
public class GradeParser {
    private Context context;
    private SharedPrefs pref;

    public GradeParser(Context context) {
        this.context = context;
        pref = new SharedPrefs(context);
    }

    public void parser(String source) {
        Document jsoup = Jsoup.parse(source);
        Elements tables = jsoup.getElementsByTag("table");
        String reg = null;
        String name = null;
        String branch = null;
        String school = null;
        try {
            Element name_reg = tables.get(1);
            Elements tr = name_reg.getElementsByTag("tr");
            reg = tr.get(0).getElementsByTag("td").get(1).html().trim();
            name = tr.get(0).getElementsByTag("td").get(3).html().trim();
            branch = tr.get(1).getElementsByTag("td").get(1).html().trim();
            school = tr.get(1).getElementsByTag("td").get(3).html().trim();
            pref.storeMsg(Config.NAME, name);
            pref.storeMsg(Config.REGISTRATION_NUMBER, reg);
            pref.storeMsg(Config.BRANCH, branch);
            pref.storeMsg(Config.SCHOOL, school);
            parseGrade(tables.get(2));
            //GPA-CREDITS Earned
            Element gpatab = tables.get(3);
            Elements gpatr = gpatab.getElementsByTag("tr").get(1).getElementsByTag("td");
            String registered = gpatr.get(0).html().trim();
            String earned = gpatr.get(1).html().trim();
            String gpa = gpatr.get(2).html().trim();
            pref.storeMsg(Config.CREDITSEARNED, earned);
            pref.storeMsg(Config.CREDITSREGISTERED, registered);
            pref.storeMsg(Config.GPA, gpa);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parseGrade(Element element) {
        List<GradeModel> list = new ArrayList<>();
        Elements courses = element.getElementsByTag("tr");
        courses.remove(0);
        for (int k = 0; k < courses.size(); k++) {
            GradeModel gm = new GradeModel();
            Elements course = courses.get(k).getElementsByTag("td");
            gm.setSubcode(course.get(1).html());
            gm.setSubname(course.get(2).html());
            gm.setSubtype(course.get(3).html());
            gm.setCredit(course.get(4).html());
            gm.setGrade(course.get(5).html());
            gm.setExamheld(course.get(6).html());
            gm.setExamresult(course.get(7).html());
            gm.setCourseoption(course.get(8).html());
            list.add(gm);
        }
        try {
            new GradeGetSet(context).createList(list);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
