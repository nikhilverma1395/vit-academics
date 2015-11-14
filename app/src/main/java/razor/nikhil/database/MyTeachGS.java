package razor.nikhil.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.model.MyTeacherDet;

/**
 * Created by Nikhil Verma on 9/27/2015.
 */
public class MyTeachGS {
    private static final String LOG_CAT = "Sqlite.myt";
    private static MyTeacherHelper sqLiteOpenHelper;
    public SQLiteDatabase sqLiteDatabase;

    public MyTeachGS(Context context) {
        sqLiteOpenHelper = new MyTeacherHelper(context);
    }

    public void Delete() {
        open();
        sqLiteDatabase.execSQL(" DROP TABLE " + MyTeacherHelper.TABLE_NAME);
        sqLiteDatabase.execSQL(sqLiteOpenHelper.getTABLE_CREATE());
        close();
    }

    public void open() {
        Log.i(LOG_CAT, "Database opened");
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public int getEntriesCount() {
        String countQuery = "SELECT  * FROM " + MyTeacherHelper.TABLE_NAME;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        int d = cursor.getCount();
        cursor.close();
        close();
        return d;
    }


    public List<MyTeacherDet> getAllCredentials() {
        List<MyTeacherDet> list = new ArrayList<>();
        String Query = "SELECT  * FROM " + MyTeacherHelper.TABLE_NAME;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                MyTeacherDet m = new MyTeacherDet();
                m.setId(Integer.parseInt(cursor.getString(0)));
                m.setNAME(cursor.getString(1));
                m.setSCHOOL(cursor.getString(2));
                m.setDESIGNATION(cursor.getString(3));
                m.setROOM(cursor.getString(4));
                m.setDIVISION(cursor.getString(5));
                m.setADDROLE(cursor.getString(6));
                m.setEMAIL(cursor.getString(7));
                m.setOPENHRS(cursor.getString(8));
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


    public void createList(List<MyTeacherDet> list) {
        open();
        for (MyTeacherDet model : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(MyTeacherHelper.COLUMN_TNAME, model.getNAME());
            contentValues.put(MyTeacherHelper.COLUMN_SCHOOL, model.getSCHOOL());
            contentValues.put(MyTeacherHelper.COLUMN_DESIG, model.getDESIGNATION());
            contentValues.put(MyTeacherHelper.COLUMN_ROOM, model.getROOM());
            contentValues.put(MyTeacherHelper.COLUMN_DIV, model.getDIVISION());
            contentValues.put(MyTeacherHelper.COLUMN_ADDROLE, model.getADDROLE());
            contentValues.put(MyTeacherHelper.COLUMN_EMAIL, model.getEMAIL());
            contentValues.put(MyTeacherHelper.COLUMN_OPENHRS, model.getOPENHRS());
            long id = sqLiteDatabase.insert(MyTeacherHelper.TABLE_NAME, null, contentValues);
            model.setId(id);
        }
        close();
        Log.d("Inserted Data ", "MYT");
    }


    public void close() {
        Log.i(LOG_CAT, "Database closed");
        sqLiteOpenHelper.close();
    }

}
