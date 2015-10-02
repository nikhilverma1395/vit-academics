package razor.nikhil.Http;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import razor.nikhil.database.MTWTHgetset;
import razor.nikhil.model.Model_Daywise;

/**
 * Created by Nikhil Verma on 9/12/2015.
 */
public class StoreTimeTable {
    private Context context;

    public StoreTimeTable(Context ctxt) {
        context = ctxt;
    }

    public void insertMTWTFinDB(List<List<Model_Daywise>> alldayslist) {
        List<List<Model_Daywise>> alldayssortedtt = new ArrayList<>();
        for (int r = 0; r < 5; r++) {
            TreeMap<Integer, Model_Daywise> hk = new TreeMap<>();
            for (int u = 0; u < alldayslist.get(r).size(); u++) {
                Model_Daywise day = alldayslist.get(r).get(u);
                String timef = day.getSubtimings().trim().toLowerCase();
                String time = timef.substring(0, 2);
                if (time.contains(":"))
                    time = time.substring(0, 1);
                int tim = Integer.parseInt(time);
                if (timef.contains("am")) {
                    hk.put(tim, day);
                } else if (timef.contains("pm")) {
                    if (tim > 6)
                        hk.put(tim, day);//as if its 12:00 pm
                    else hk.put(tim + 20, day);// just a basic counter 20 for pm
                } else {
                }
            }
            List<Model_Daywise> val = new ArrayList<>(hk.values());
            alldayssortedtt.add(val);
        }

        if (alldayssortedtt.size() == 5) {
            for (int i = 0; i < 5; i++) {

                if (i == 0) {
                    MTWTHgetset mod = new MTWTHgetset(context, "monday");
                    for (Model_Daywise md : alldayssortedtt.get(0)) {
                        mod.create(md);
                    }
                }
                if (i == 1) {
                    MTWTHgetset mod = new MTWTHgetset(context, "tuesday");
                    for (Model_Daywise md : alldayssortedtt.get(1)) {
                        mod.create(md);
                    }

                }
                if (i == 2) {
                    MTWTHgetset mod = new MTWTHgetset(context, "wednesday");
                    for (Model_Daywise md : alldayssortedtt.get(2)) {
                        mod.create(md);
                    }

                }
                if (i == 3) {
                    MTWTHgetset mod = new MTWTHgetset(context, "thursday");
                    for (Model_Daywise md : alldayssortedtt.get(3)) {
                        mod.create(md);
                    }

                }
                if (i == 4) {
                    MTWTHgetset mod = new MTWTHgetset(context, "friday");
                    for (Model_Daywise md : alldayssortedtt.get(4)) {
                        mod.create(md);
                    }

                }
                Log.d("TR","sdddddddddd");
            }
        }
    }


}
