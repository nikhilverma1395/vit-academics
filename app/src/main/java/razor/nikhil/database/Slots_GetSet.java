package razor.nikhil.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.model.Model_Slots;

/**
 * Created by Nikhil Verma on 7/28/2015.
 */
public class Slots_GetSet {
    private static final String LOG_CAT = "Sqlite.Slots";
    private static SQLiteOpenHelper sqLiteOpenHelper;
    public SQLiteDatabase sqLiteDatabase;

    public Slots_GetSet(Context context) {
        sqLiteOpenHelper = new MySqlHelper_Slots(context);
    }

    public void open() {
        Log.i(LOG_CAT, "Database opened");
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

    }

    public int getEntriesCount() {
        String countQuery = "SELECT  * FROM " + MySqlHelper_Slots.TABLE_NAME;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        int d = cursor.getCount();
        cursor.close();
        close();
        return d;
    }

    public List<Model_Slots> getAllCredentials() {
        List<Model_Slots> list = new ArrayList<>();
        String Query = "SELECT  * FROM " + MySqlHelper_Slots.TABLE_NAME;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                Model_Slots m = new Model_Slots();
                m.setId(Integer.parseInt(cursor.getString(0)));
                m.setSubject_name(cursor.getString(1));
                m.setCode(cursor.getString(2));
                m.setNumber(cursor.getString(3));
                Log.d("Class Number", cursor.getString(3));
                m.setTeacher(cursor.getString(4));
                m.setCourse_type(cursor.getString(5));
                m.setCourse_option(cursor.getString(6));
                m.setCourse_mode(cursor.getString(7));
                m.setLTPJC(cursor.getString(8));
                m.setSlot(cursor.getString(9));
                m.setVenue(cursor.getString(10));
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


    public void create(Model_Slots model) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MySqlHelper_Slots.COLUMN_SUBJECT_NAME, model.getSubject_name());
        contentValues.put(MySqlHelper_Slots.COLUMN_SUBJECT_CODE, model.getCode());
        contentValues.put(MySqlHelper_Slots.COLUMN_SUBJECT_CLASS_NUMBER, model.getClass_number());
        contentValues.put(MySqlHelper_Slots.COLUMN_SUBJECT_TEACHER, model.getTeacher());
        contentValues.put(MySqlHelper_Slots.COLUMN_SUBJECT_TYPE, model.getCourse_type());
        contentValues.put(MySqlHelper_Slots.COLUMN_SUBJECT_OPTION, model.getCourse_option());
        contentValues.put(MySqlHelper_Slots.COLUMN_SUBJECT_MODE, model.getCourse_mode());
        contentValues.put(MySqlHelper_Slots.COLUMN_SUBJECT_LTJPC, model.getLTPJC());
        contentValues.put(MySqlHelper_Slots.COLUMN_SUBJECT_SLOT, model.getSlot());
        contentValues.put(MySqlHelper_Slots.COLUMN_SUBJECT_VENUE, model.getVenue());
        //contentValues.put(MySqlHelper_Slots.COLUMN_SUBJECT_REG_DATE, model.r);
        long id = sqLiteDatabase.insert(MySqlHelper_Slots.TABLE_NAME, null, contentValues);
        model.setId(id);
        close();
    }

    public void close() {
        Log.i(LOG_CAT, "Database closed");
        sqLiteOpenHelper.close();
    }

}
