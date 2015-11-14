package razor.nikhil.Http;

import android.content.Context;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.database.Attend_GetSet;
import razor.nikhil.model.AttenDetail;
import razor.nikhil.model.AttendBrief;

/**
 * Created by Nikhil Verma on 9/12/2015.
 */
public class AttendanceParser {
    private Context context;
    private static List<AttenDetail> dates_sem_codes;
    private static HttpClient httpClient;

    public AttendanceParser(Context ctxt, HttpClient http) {
        this.context = ctxt;
        this.httpClient = http;
    }

    public void parse(String rawData) {
        Document doc = Jsoup.parse(rawData);
        Elements ele = doc.getElementsByTag("table");
        Elements subs = ele.get(3).getElementsByTag("tr");
        dates_sem_codes = new ArrayList<>();
        Attend_GetSet set = new Attend_GetSet(context);
        set.Delete();
        for (int t = 1; t < subs.size(); t++) {
            final Elements al = subs.get(t).getElementsByTag("td");
            AttendBrief ab = new AttendBrief();
            ab.setSubcode(al.get(1).html().trim());
            ab.setAttended(al.get(6).html().trim());
            ab.setTotal(al.get(7).html().trim());
            ab.setPercent(al.get(8).html().trim());
            ab.setSubtype(al.get(3).html().trim());
            set.create(ab);
            Elements dates = subs.get(t).getElementsByTag("input");
            AttenDetail dd = new AttenDetail();
            dd.setSemcode(dates.get(0).attr("value"));
            dd.setClass_number(dates.get(1).attr("value"));
            dd.setFrom_date(dates.get(2).attr("value"));
            dd.setTo_date(dates.get(3).attr("value"));
            dates_sem_codes.add(dd);
        }
        new FullAttendParseStore(context, httpClient, dates_sem_codes);
    }
}