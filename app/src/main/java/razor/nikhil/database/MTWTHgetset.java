package razor.nikhil.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.model.Model_Daywise;

/**
 * Created by Nikhil Verma on 9/5/2015.
 */
public class MTWTHgetset {
    private static final String LOG_CAT = "Sqlite.MTWTF";
    private static SQLiteOpenHelper sqLiteOpenHelper;
    public SQLiteDatabase sqLiteDatabase;
    private String TABLE = "";

    public MTWTHgetset(Context context, String tname) {
        this.TABLE = tname;
        sqLiteOpenHelper = new MTWTFhelper(context);
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

    public List<Model_Daywise> getAllCredentials() {
        List<Model_Daywise> list = new ArrayList<>();
        String Query = "SELECT  * FROM " + TABLE;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                Model_Daywise m = new Model_Daywise();
                m.setId(Integer.parseInt(cursor.getString(0)));
                m.setSubname(cursor.getString(1));
                m.setSubcode(cursor.getString(2));
                m.setSubtype(cursor.getString(3));
                m.setVenue(cursor.getString(4));
                m.setSubtimings(cursor.getString(5));
                m.setSubslot(cursor.getString(6));
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

    public void create(Model_Daywise model) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MTWTFhelper.COLUMN_SUBJECT_NAME, model.getSubname());
        contentValues.put(MTWTFhelper.COLUMN_SUBJECT_CODE, model.getSubcode());
        contentValues.put(MTWTFhelper.COLUMN_SUBJECT_TYPE, model.getSubtype());
        contentValues.put(MTWTFhelper.COLUMN_SUBJECT_VENUE, model.getVenue());
        contentValues.put(MTWTFhelper.COLUMN_TIMINGS, model.getSubtimings());
        contentValues.put(MTWTFhelper.COLUMN_SLOT, model.getSubslot());
        long id = sqLiteDatabase.insert(TABLE, null, contentValues);
        model.setId(id);
        close();
    }

    public void close() {
        Log.i(LOG_CAT, "Database closed");
        sqLiteOpenHelper.close();
    }


}
