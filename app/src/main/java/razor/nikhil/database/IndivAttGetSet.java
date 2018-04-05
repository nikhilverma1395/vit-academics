package razor.nikhil.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.Http.FullAttendParseStore;
import razor.nikhil.model.DetailAtten;
import razor.nikhil.model.DetailAttenColumnNames;

/**
 * Created by Nikhil Verma on 9/4/2015.
 */
public class IndivAttGetSet {
    private static final String LOG_CAT = "Academics.IndivAtt";
    private static IndivAttHelper sqLiteOpenHelper;
    private final Context context;
    public SQLiteDatabase sqLiteDatabase;
    private String TABLE = "";

    public IndivAttGetSet(Context context, String tname) {
        this.context = context;
        sqLiteOpenHelper = new IndivAttHelper(context);
        this.TABLE = tname.trim();
    }

    public void Delete(String code) {
        open();
        sqLiteDatabase.execSQL(" DROP TABLE " + TABLE);
        sqLiteDatabase.execSQL(FullAttendParseStore.getFullAttTableName(code));
        close();
    }

    public void open() {
        Log.i(LOG_CAT, "Database opened");
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public int getEntriesCount() {
        String countQuery = "SELECT  * FROM " + TABLE;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        int d = cursor.getCount();
        cursor.close();
        close();
        return d;
    }

    public List<DetailAtten> getAllCredentials() {
        List<DetailAtten> list = new ArrayList<>();
        String Query = "SELECT  * FROM " + TABLE;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                DetailAtten m = new DetailAtten();
                m.setId(Integer.parseInt(cursor.getString(0)));
                m.setNo(cursor.getString(1));
                m.setDay(cursor.getString(2));
                m.setAttend_unit(cursor.getString(3));
                list.add(m);
            } while (cursor.moveToNext());
        }
        close();
        try {
            if (list.size() == 0) {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
        return list;
    }


    public void create(List<DetailAtten> LIST) {
        open();
        for (DetailAtten model : LIST) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(DetailAttenColumnNames.sno, model.getNo());
            contentValues.put(DetailAttenColumnNames.date, model.getDay());
            contentValues.put(DetailAttenColumnNames.unit, model.getAttend_unit());
            long id = sqLiteDatabase.insert(TABLE, null, contentValues);
            model.setId(id);
        }
        close();
    }

    public void close() {
        Log.i(LOG_CAT, "Database closed");
        sqLiteOpenHelper.close();
    }

}
