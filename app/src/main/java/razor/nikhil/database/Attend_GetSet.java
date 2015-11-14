package razor.nikhil.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.model.AttendBrief;

/**
 * Created by Nikhil Verma on 9/4/2015.
 */
public class Attend_GetSet {
    private static final String LOG_CAT = "Sqlite.Attend_getset";
    private static Attendance_Helper sqLiteOpenHelper;
    public SQLiteDatabase sqLiteDatabase;

    public Attend_GetSet(Context context) {
        sqLiteOpenHelper = new Attendance_Helper(context);
    }

    public void Delete() {
        open();
        sqLiteDatabase.execSQL(" DROP TABLE " + Attendance_Helper.TABLE_NAME);
        sqLiteDatabase.execSQL(sqLiteOpenHelper.getTABLE_CREATE());
        close();
    }

    public void open() {
        Log.i(LOG_CAT, "Database opened");
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

    }

    public int getEntriesCount() {
        String countQuery = "SELECT  * FROM " + Attendance_Helper.TABLE_NAME;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        int d = cursor.getCount();
        cursor.close();
        close();
        return d;
    }

    public List<AttendBrief> getAllCredentials() {
        List<AttendBrief> list = new ArrayList<>();
        String Query = "SELECT  * FROM " + Attendance_Helper.TABLE_NAME;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                AttendBrief m = new AttendBrief();
                m.setId(Integer.parseInt(cursor.getString(0)));
                m.setSubcode(cursor.getString(1));
                m.setAttended(cursor.getString(2));
                m.setTotal(cursor.getString(3));
                m.setPercent(cursor.getString(4));
                m.setSubtype(cursor.getString(5));
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


    public void create(AttendBrief model) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Attendance_Helper.COLUMN_SUBJECT_CODE, model.getSubcode());
        contentValues.put(Attendance_Helper.COLUMN_ATTENDED, model.getAttended());
        contentValues.put(Attendance_Helper.COLUMN_TOTAL, model.getTotal());
        contentValues.put(Attendance_Helper.COLUMN_ATTEND_PERCENT, model.getPercent());
        contentValues.put(Attendance_Helper.COLUMN_subtype, model.getSubtype());
        long id = sqLiteDatabase.insert(Attendance_Helper.TABLE_NAME, null, contentValues);
        model.setId(id);
        close();
    }

    public void close() {
        Log.i(LOG_CAT, "Database closed");
        sqLiteOpenHelper.close();
    }

}
