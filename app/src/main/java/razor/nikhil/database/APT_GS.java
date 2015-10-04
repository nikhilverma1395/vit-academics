package razor.nikhil.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.model.AptModel;

/**
 * Created by Nikhil Verma on 10/1/2015.
 */
public class APT_GS {
    public void Delete() {
        open();
        sqLiteDatabase.execSQL(" DROP TABLE " + APThelper.TABLE_NAME);
        sqLiteDatabase.execSQL(sqLiteOpenHelper.getTABLE_CREATE());
        close();
    }


    private static final String LOG_CAT = "Sqlite.Grades";
    private static APThelper sqLiteOpenHelper;
    public SQLiteDatabase sqLiteDatabase;

    public APT_GS(Context context) {
        sqLiteOpenHelper = new APThelper(context);
    }

    public void open() {
        Log.i(LOG_CAT, "Database opened");
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public int getEntriesCount() {
        String countQuery = "SELECT  * FROM " + APThelper.TABLE_NAME;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        int d = cursor.getCount();
        cursor.close();
        close();
        return d;
    }


    public List<AptModel> getAllCredentials() {
        List<AptModel> list = new ArrayList<>();
        String Query = "SELECT  * FROM " + APThelper.TABLE_NAME;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                AptModel m = new AptModel();
                m.setId(Integer.parseInt(cursor.getString(0)));
                m.setDate(cursor.getString(1));
                m.setSession(cursor.getString(2));
                m.setUnits(cursor.getString(3));
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


    public void createList(List<AptModel> list) {
        open();
        for (AptModel model : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(APThelper.DATE, model.getDate());
            contentValues.put(APThelper.SESSION, model.getSession());
            contentValues.put(APThelper.UNIT, model.getUnits());
            long id = sqLiteDatabase.insert(APThelper.TABLE_NAME, null, contentValues);
            model.setId(id);
        }
        close();
        Log.d("Inserted Data ", "APT");
    }


    public void close() {
        Log.i(LOG_CAT, "Database closed");
        sqLiteOpenHelper.close();
    }

}
