package razor.nikhil.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

import razor.nikhil.Fragments.Slots;
import razor.nikhil.Http.FullAttendParseStore;
import razor.nikhil.model.Query_TableName;

/**
 * Created by Nikhil Verma on 9/4/2015.
 */
public class IndivAttHelper extends SQLiteOpenHelper {
    private List<Query_TableName> queries;


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        for (int i = 0; i < queries.size(); i++) {
            sqLiteDatabase.execSQL(queries.get(i).getQuery().trim());
        }
    }

    public IndivAttHelper(Context context) {
        super(context, "Indiv_att", null, 1);
        if (Slots.sqltn.size() <= FullAttendParseStore.table_queries.size())
            queries = FullAttendParseStore.table_queries;
        else queries = Slots.sqltn;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i2, int i1) {
        for (int i = 0; i < queries.size(); i++) {
            db.execSQL("DROP TABLE IF EXISTS " + queries.get(i).getQuery().trim());
        }
        onCreate(db);
    }
}
