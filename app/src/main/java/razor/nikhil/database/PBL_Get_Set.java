package razor.nikhil.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.model.PBL_Model;

/**
 * Created by Nikhil Verma on 7/30/2015.
 */
public class PBL_Get_Set {
    private static final String LOG_CAT = "Academics.Marks.Pbl";
    private static SQLiteOpenHelper sqLiteOpenHelper;
    public SQLiteDatabase sqLiteDatabase;

    public PBL_Get_Set(Context context) {
        sqLiteOpenHelper = new PBL_Helper(context);
    }

    public void open() {
        Log.i(LOG_CAT, "Database opened");
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

    }

    public int getEntriesCount() {
        String countQuery = "SELECT  * FROM " + PBL_Helper.TABLE_NAME;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        int d = cursor.getCount();
        cursor.close();
        close();
        return d;
    }

    public List<PBL_Model> getAllCredentials() {
        List<PBL_Model> list = new ArrayList<>();
        String Query = "SELECT  * FROM " +PBL_Helper.TABLE_NAME;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                PBL_Model m = new PBL_Model();
                m.set_id(Integer.parseInt(cursor.getString(0)));

                m.setCourse_code(cursor.getString(1));
                m.setClas_nbr(cursor.getString(2));

                m.setOption1_Title(cursor.getString(3));
                m.setOption2_Title(cursor.getString(4));
                m.setOption3_Title(cursor.getString(5));
                m.setOption4_Title(cursor.getString(6));
                m.setOption5_Title(cursor.getString(7));

                m.setOption1_Max_Mark(cursor.getString(8));
                m.setOption2_Max_Mark(cursor.getString(9));
                m.setOption3_Max_Mark(cursor.getString(10));
                m.setOption4_Max_Mark(cursor.getString(11));
                m.setOption5__Max_Mark(cursor.getString(12));

                m.setOption1_Weightage(cursor.getString(13));
                m.setOption2_Weightage(cursor.getString(14));
                m.setOption3_Weightage(cursor.getString(15));
                m.setOption4_Weightage(cursor.getString(16));
                m.setOption5_Weightage(cursor.getString(17));

                m.setOption1_Date(cursor.getString(18));
                m.setOption2_Date(cursor.getString(19));
                m.setOption3_Date(cursor.getString(20));
                m.setOption4_Date(cursor.getString(21));
                m.setOption5_Date(cursor.getString(22));

                m.setOption1_Attend(cursor.getString(23));
                m.setOption2_Attend(cursor.getString(24));
                m.setOption3_Attend(cursor.getString(25));
                m.setOption4_Attend(cursor.getString(26));
                m.setOption5_Attend(cursor.getString(27));

                m.setOption1_Scored(cursor.getString(28));
                m.setOption2_Scored(cursor.getString(29));
                m.setOption3_Scored(cursor.getString(30));
                m.setOption4_Scored(cursor.getString(31));
                m.setOption5_Scored(cursor.getString(32));

                m.setOption1_Scored_Percent(cursor.getString(33));
                m.setOption2_Scored_Percent(cursor.getString(34));
                m.setOption3_Scored_Percent(cursor.getString(35));
                m.setOption4_Scored_Percent(cursor.getString(36));
                m.setOption5_Scored_Percent(cursor.getString(37));
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


    public void create(PBL_Model model) {
        open();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PBL_Helper.course_code, model.getCourse_code());
        contentValues.put(PBL_Helper.clas_nbr, model.getClas_nbr());

        contentValues.put(PBL_Helper.Option1_Title, model.getOption1_Title());
        contentValues.put(PBL_Helper.Option2_Title, model.getOption2_Title());
        contentValues.put(PBL_Helper.Option3_Title, model.getOption3_Title());
        contentValues.put(PBL_Helper.Option4_Title, model.getOption4_Title());
        contentValues.put(PBL_Helper.Option5_Title, model.getOption5_Title());

        contentValues.put(PBL_Helper.Option1_Max_Mark, model.getOption1_Max_Mark());
        contentValues.put(PBL_Helper.Option2_Max_Mark, model.getOption2_Max_Mark());
        contentValues.put(PBL_Helper.Option3_Max_Mark, model.getOption3_Max_Mark());
        contentValues.put(PBL_Helper.Option4_Max_Mark, model.getOption4_Max_Mark());
        contentValues.put(PBL_Helper.Option5__Max_Mark, model.getOption5__Max_Mark());

        contentValues.put(PBL_Helper.Option1_Weightage, model.getOption1_Weightage());
        contentValues.put(PBL_Helper.Option2_Weightage, model.getOption2_Weightage());
        contentValues.put(PBL_Helper.Option3_Weightage, model.getOption3_Weightage());
        contentValues.put(PBL_Helper.Option4_Weightage, model.getOption4_Weightage());
        contentValues.put(PBL_Helper.Option5_Weightage, model.getOption5_Weightage());

        contentValues.put(PBL_Helper.Option1_Date, model.getOption1_Date());
        contentValues.put(PBL_Helper.Option2_Date, model.getOption2_Date());
        contentValues.put(PBL_Helper.Option3_Date, model.getOption3_Date());
        contentValues.put(PBL_Helper.Option4_Date, model.getOption4_Date());
        contentValues.put(PBL_Helper.Option5_Date, model.getOption5_Date());

        contentValues.put(PBL_Helper.Option1_Attend, model.getOption1_Attend());
        contentValues.put(PBL_Helper.Option2_Attend, model.getOption2_Attend());
        contentValues.put(PBL_Helper.Option3_Attend, model.getOption3_Attend());
        contentValues.put(PBL_Helper.Option4_Attend, model.getOption4_Attend());
        contentValues.put(PBL_Helper.Option5_Attend, model.getOption5_Attend());

        contentValues.put(PBL_Helper.Option1_Scored, model.getOption1_Scored());
        contentValues.put(PBL_Helper.Option2_Scored, model.getOption2_Scored());
        contentValues.put(PBL_Helper.Option3_Scored, model.getOption3_Scored());
        contentValues.put(PBL_Helper.Option4_Scored, model.getOption4_Scored());
        contentValues.put(PBL_Helper.Option5_Scored, model.getOption5_Scored());

        contentValues.put(PBL_Helper.Option1_Scored_Percent, model.getOption1_Scored_Percent());
        contentValues.put(PBL_Helper.Option2_Scored_Percent, model.getOption2_Scored_Percent());
        contentValues.put(PBL_Helper.Option3_Scored_Percent, model.getOption3_Scored_Percent());
        contentValues.put(PBL_Helper.Option4_Scored_Percent, model.getOption4_Scored_Percent());
        contentValues.put(PBL_Helper.Option5_Scored_Percent, model.getOption5_Scored_Percent());
        long id = sqLiteDatabase.insert(PBL_Helper.TABLE_NAME, null, contentValues);
        model.set_id(id);
        close();
    }

    public void close() {
        Log.i(LOG_CAT, "Database closed");
        sqLiteOpenHelper.close();
    }

}
