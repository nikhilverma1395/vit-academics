package razor.nikhil.Http;

import android.content.Context;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import razor.nikhil.database.IndivAttGetSet;
import razor.nikhil.model.AttenDetail;
import razor.nikhil.model.DetailAtten;
import razor.nikhil.model.Query_TableName;
import razor.nikhil.model.detailattlist_subcode;

/**
 * Created by Nikhil Verma on 9/12/2015.
 */
public class FullAttendParseStore {
    private Context context;
    private HttpClient httpClient;
    private final String DETAIL_ATTENDANCE_POST_URL = "https://academics.vit.ac.in/student/attn_report_details.asp";
    private List<detailattlist_subcode> all_sub_detail;
    public static List<Query_TableName> table_queries = new ArrayList<>();
    private static List<AttenDetail> dates_sem_codes;
    IndivAttGetSet indivDB;
    List<List<DetailAtten>> mylist = new ArrayList<>();
    long in, out;

    public FullAttendParseStore(Context context, HttpClient clien, List<AttenDetail> dates_sem_codes) {
        this.context = context;
        this.httpClient = clien;
        this.dates_sem_codes = dates_sem_codes;
        doInBackground(dates_sem_codes);

    }

    protected Void doInBackground(final List<AttenDetail> lists) {
        createTables(lists);
        final HttpPost httppost = null;
        all_sub_detail = new ArrayList<>();
        Thread thread[] = new Thread[lists.size()];
        for (int t = 0; t < lists.size(); t++) {
            final int ft = t;
            Runnable runnable = new Runnable() {
                public void run() {
                    getIndivAttinDiffThreads(lists.get(ft), httppost);
                }
            };
            thread[ft] = new Thread(runnable);
            thread[ft].start();
        }
        for (Thread rr : thread) {
            if (rr != null)
                try {
                    rr.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
        for (int y = 0; y < mylist.size(); y++) {
            insertIndivAttIntoDBS(mylist.get(y), lists.get(y).getClass_number().trim());
        }
        return null;
    }

    private void createTables(List<AttenDetail> lists) {
        for (int i = 0; i < lists.size(); i++) {
            String cnum = lists.get(i).getClass_number().trim();
            Query_TableName qt = new Query_TableName();
            qt.setQuery(getFullAttTableName(cnum));
            qt.setTname(cnum);
            table_queries.add(qt);
        }
    }

    public static String getFullAttTableName(String code) {
        String TABLE_Create = "CREATE TABLE " + " table_of_" + code + " (" +
                "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "sno" + " TEXT, " +
                "date" + " TEXT, " +
                "unit" + " TEXT " + ");";
        return TABLE_Create;
    }

    private void getIndivAttinDiffThreads(AttenDetail lists, HttpPost httppost) {
        try {
            httppost = new HttpPost(DETAIL_ATTENDANCE_POST_URL);
            List<NameValuePair> nameValuePair = new ArrayList<>(4);
            nameValuePair.add(new BasicNameValuePair("semcode", lists.getSemcode()));
            nameValuePair.add(new BasicNameValuePair("classnbr", lists.getClass_number()));
            nameValuePair.add(new BasicNameValuePair("from_date", lists.getFrom_date()));
            nameValuePair.add(new BasicNameValuePair("to_date", lists.getTo_date()));
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                HttpResponse response = httpClient.execute(httppost);
                final String op = EntityUtils.toString(response.getEntity(), "UTF-8");
                final List<DetailAtten> jack = parseAtt(op);
                mylist.add(jack);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private List<DetailAtten> parseAtt(String op) {
        Document dr = Jsoup.parse(op);
        Elements ele = dr.getElementsByTag("table");
        Elements main = ele.get(2).getElementsByTag("tr");
        List<Elements> lis = new ArrayList<>();
        List<DetailAtten> daysAttended = new ArrayList<>();
        for (int r = 2; r < main.size(); r++) {
            DetailAtten da = new DetailAtten();
            Elements sds = main.get(r).getElementsByTag("td");
            Elements nep = main.get(r).getElementsByTag("font");
            lis.add(sds);
            for (int rr = 0; rr < sds.size(); rr++) {
                if (rr == 0)
                    da.setNo(sds.get(rr).html().trim());
                if (rr == 1)
                    da.setDay(sds.get(rr).html().trim());
                if (rr == 3)
                    da.setAttend(nep.get(0).html());
                if (rr == 4)
                    da.setAttend_unit(sds.get(rr).html().trim());
            }
            daysAttended.add(da);
        }
        return daysAttended;
    }


    private void insertIndivAttIntoDBS(final List<DetailAtten> all_sub_detail, String cnum) {
        indivDB = new IndivAttGetSet(context, "table_of_" + cnum);
        indivDB.Delete(cnum);
        indivDB.create(all_sub_detail);
    }
}
