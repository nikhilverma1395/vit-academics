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
    private final String DETAIL_ATTENDANCE_POST_URL = "https://academics.vit.ac.in/parent/attn_report_details.asp";
    private List<detailattlist_subcode> all_sub_detail;
    public static List<Query_TableName> table_queries = new ArrayList<>();
    private static List<AttenDetail> dates_sem_codes;

    public FullAttendParseStore(Context context, HttpClient clien, List<AttenDetail> dates_sem_codes) {
        this.context = context;
        this.httpClient = clien;
        this.dates_sem_codes = dates_sem_codes;
        doInBackground(dates_sem_codes);

    }

    protected Void doInBackground(List<AttenDetail>... lists) {
        HttpPost httppost = null;

        all_sub_detail = new ArrayList<>();
        for (int rt = 0; rt < lists[0].size(); rt++) {
            try {
                httppost = new HttpPost(DETAIL_ATTENDANCE_POST_URL);
                List<NameValuePair> nameValuePair = new ArrayList<>(4);
                nameValuePair.add(new BasicNameValuePair("semcode", lists[0].get(rt).getSemcode()));
                nameValuePair.add(new BasicNameValuePair("classnbr", lists[0].get(rt).getClass_number()));
                nameValuePair.add(new BasicNameValuePair("from_date", lists[0].get(rt).getFrom_date()));
                nameValuePair.add(new BasicNameValuePair("to_date", lists[0].get(rt).getTo_date()));
                try {
                    httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                try {
                    HttpResponse response = httpClient.execute(httppost);
                    final String op = EntityUtils.toString(response.getEntity(), "UTF-8");
                    final List<DetailAtten> jack = parseAtt(op);
                    detailattlist_subcode x = new detailattlist_subcode();
                    x.setList(jack);
                    x.setSubcode(lists[0].get(rt).getClass_number());
                    all_sub_detail.add(x);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        insertIndivAttIntoDBS(all_sub_detail);
        return null;
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


    private void insertIndivAttIntoDBS(final List<detailattlist_subcode> all_sub_detail) {
        for (int i = 0; i < all_sub_detail.size(); i++) {
            final String TABLE_Create = "CREATE TABLE " + " table_of_" + all_sub_detail.get(i).getSubcode().trim() + " (" +
                    "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "sno" + " TEXT, " +
                    "date" + " TEXT, " +
                    "unit" + " TEXT " + ");";
            Query_TableName qt = new Query_TableName();
            qt.setQuery(TABLE_Create);
            qt.setTname(all_sub_detail.get(i).getSubcode().trim());
            table_queries.add(qt);
        }
        for (int i = 0; i < all_sub_detail.size(); i++) {
            IndivAttGetSet wif = new IndivAttGetSet(context, "table_of_" + all_sub_detail.get(i).getSubcode());//subcode-is classnbr
            for (int j = 0; j < all_sub_detail.get(i).getList().size(); j++) {
                wif.create(all_sub_detail.get(i).getList().get(j));
            }
        }
    }


}
