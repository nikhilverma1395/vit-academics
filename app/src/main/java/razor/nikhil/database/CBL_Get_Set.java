package razor.nikhil.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.model.Marks_Model;

/**
 * Created by Nikhil Verma on 7/29/2015.
 */
public class CBL_Get_Set {
    private static final String LOG_CAT = "Academics.Marks";
    private static SQLiteOpenHelper sqLiteOpenHelper;
    public SQLiteDatabase sqLiteDatabase;

    public CBL_Get_Set(Context context) {
        sqLiteOpenHelper = new CBL_MarksHelper(context);
    }

    public void open() {
        Log.i(LOG_CAT, "Database opened");
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();

    }

    public int getEntriesCount() {
        String countQuery = "SELECT  * FROM " + CBL_MarksHelper.TABLE_NAME;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        int d = cursor.getCount();
        cursor.close();
        close();
        return d;
    }

    public List<Marks_Model> getAllCredentials() {
        List<Marks_Model> list = new ArrayList<>();
        String Query = "SELECT  * FROM " + CBL_MarksHelper.TABLE_NAME;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                Marks_Model m = new Marks_Model();
                m.setId(Integer.parseInt(cursor.getString(0)));
                m.setClassnbr(cursor.getString(1));
                m.setSubtype(cursor.getString(2));
                m.setCAT1_Attended(cursor.getString(3));
                m.setCAT1(cursor.getString(4));
                m.setCAT2_Attended(cursor.getString(5));
                m.setCAT2(cursor.getString(6));
                m.setQUIZ1_Attended(cursor.getString(7));
                m.setQUIZ1(cursor.getString(8));
                m.setQUIZ2_Attended(cursor.getString(9));
                m.setQUIZ2(cursor.getString(10));
                m.setQUIZ3_Attended(cursor.getString(11));
                m.setQUIZ3(cursor.getString(12));
                m.setASSIGN_Attended(cursor.getString(13));
                m.setASIIGN(cursor.getString(14));
                m.setLABCAM_Attended(cursor.getString(15));
                m.setLABCAM(cursor.getString(16));
                m.setSubname(cursor.getString(17));
                m.setSubcode(cursor.getString(18));
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


    public void create(Marks_Model model) {
        open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CBL_MarksHelper.COLUMN_SUBJECT_CLASS_NUMBER, model.getClassnbr());
        contentValues.put(CBL_MarksHelper.COLUMN_SUBJECT_TYPE, model.getSubtype());
        contentValues.put(CBL_MarksHelper.COLUMN_SUBJECT_CAT1_ATTEND, model.getCAT1_Attended());
        contentValues.put(CBL_MarksHelper.COLUMN_SUBJECT_CAT1_MARKS, model.getCAT1());
        contentValues.put(CBL_MarksHelper.COLUMN_SUBJECT_CAT2_ATTEND, model.getCAT2_Attended());
        contentValues.put(CBL_MarksHelper.COLUMN_SUBJECT_CAT2_MARKS, model.getCAT2());
        contentValues.put(CBL_MarksHelper.COLUMN_SUBJECT_QUIZ1_ATTEND, model.getQUIZ1_Attended());
        contentValues.put(CBL_MarksHelper.COLUMN_SUBJECT_QUIZ1_MARKS, model.getQUIZ1());
        contentValues.put(CBL_MarksHelper.COLUMN_SUBJECT_QUIZ2_ATTEND, model.getQUIZ2_Attended());
        contentValues.put(CBL_MarksHelper.COLUMN_SUBJECT_QUIZ2_MARKS, model.getQUIZ2());
        contentValues.put(CBL_MarksHelper.COLUMN_SUBJECT_QUIZ3_ATTEND, model.getQUIZ3_Attended());
        contentValues.put(CBL_MarksHelper.COLUMN_SUBJECT_QUIZ3_MARKS, model.getQUIZ3());
        contentValues.put(CBL_MarksHelper.COLUMN_SUBJECT_ASSIGN_ATTEND, model.getASSIGN_Attended());
        contentValues.put(CBL_MarksHelper.COLUMN_SUBJECT_ASSIGN_MARKS, model.getASIIGN());
        contentValues.put(CBL_MarksHelper.COLUMN_SUBJECT_LABCAM_ATTEND, model.getLABCAM_Attended());
        contentValues.put(CBL_MarksHelper.COLUMN_SUBJECT_LABCAM_MARKS, model.getLABCAM());
        contentValues.put(CBL_MarksHelper.COLUMN_SUB_NAME, model.getSubname());
        contentValues.put(CBL_MarksHelper.COLUMN_SUB_CODE, model.getSubcode());
        long id = sqLiteDatabase.insert(CBL_MarksHelper.TABLE_NAME, null, contentValues);
        model.setId(id);
        close();
    }

    public void close() {
        Log.i(LOG_CAT, "Database closed");
        sqLiteOpenHelper.close();
    }

}
