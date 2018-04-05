package razor.nikhil.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.model.GradeModel;

/**
 * Created by Nikhil Verma on 9/16/2015.
 */
public class GradeGetSet {
    private static final String LOG_CAT = "Sqlite.Grades";
    private static SQLiteOpenHelper sqLiteOpenHelper;
    public SQLiteDatabase sqLiteDatabase;

    public GradeGetSet(Context context) {
        sqLiteOpenHelper = new GradeHelper(context);
    }

    public void open() {
        Log.i(LOG_CAT, "Database opened");
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public int getEntriesCount() {
        String countQuery = "SELECT  * FROM " + GradeHelper.TABLE_NAME;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        int d = cursor.getCount();
        cursor.close();
        close();
        return d;
    }


    public List<GradeModel> getAllCredentials() {
        List<GradeModel> list = new ArrayList<>();
        String Query = "SELECT  * FROM " + GradeHelper.TABLE_NAME;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                GradeModel m = new GradeModel();
                m.setId(Integer.parseInt(cursor.getString(0)));
                m.setSubcode(cursor.getString(1));
                m.setSubname(cursor.getString(2));
                m.setSubtype(cursor.getString(3));
                m.setCredit(cursor.getString(4));
                m.setGrade(cursor.getString(5));
                m.setExamheld(cursor.getString(6));
                m.setExamresult(cursor.getString(7));
                m.setCourseoption(cursor.getString(8));
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


    public void createList(List<GradeModel> list) {
        open();
        for (GradeModel model : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(GradeHelper.SUBCODE, model.getSubcode());
            contentValues.put(GradeHelper.NAME, model.getSubname());
            contentValues.put(GradeHelper.TYPE, model.getSubtype());
            contentValues.put(GradeHelper.CREDITS, model.getCredit());
            contentValues.put(GradeHelper.GRADE, model.getGrade());
            contentValues.put(GradeHelper.EXAMHELD, model.getExamheld());
            contentValues.put(GradeHelper.RESULTDATE, model.getExamresult());
            contentValues.put(GradeHelper.COURSEOPTION, model.getCourseoption());
            long id = sqLiteDatabase.insert(GradeHelper.TABLE_NAME, null, contentValues);
            model.setId(id);
        }
        close();
        Log.d("Inserted Data ", "Grades");
    }


    public void close() {
        Log.i(LOG_CAT, "Database closed");
        sqLiteOpenHelper.close();
    }

}
