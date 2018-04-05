package razor.nikhil.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.model.FacMsgModel;

/**
 * Created by Nikhil Verma on 9/30/2015.
 */
public class FacMsgGS {
    private static final String LOG_CAT = "Sqlite.Facmsg";
    private static FacMessagesHelper sqLiteOpenHelper;
    public SQLiteDatabase sqLiteDatabase;

    public void Delete() {
        open();
        sqLiteDatabase.execSQL(" DROP TABLE " + FacMessagesHelper.TABLE_NAME);
        sqLiteDatabase.execSQL(sqLiteOpenHelper.getTABLE_CREATE());
        close();
    }

    public FacMsgGS(Context context) {
        sqLiteOpenHelper = new FacMessagesHelper(context);
    }

    public boolean deleteRowById(int ide) {
        open();
        boolean b = sqLiteDatabase.delete(FacMessagesHelper.TABLE_NAME, FacMessagesHelper.ID + "=" + ide, null) > 0;
        close();
        return b;

    }

    public void open() {
        Log.i(LOG_CAT, "Database opened");
        sqLiteDatabase = sqLiteOpenHelper.getWritableDatabase();
    }

    public int getEntriesCount() {
        String countQuery = "SELECT  * FROM " + FacMessagesHelper.TABLE_NAME;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(countQuery, null);
        int d = cursor.getCount();
        cursor.close();
        close();
        return d;
    }


    public List<FacMsgModel> getAllCredentials() {
        List<FacMsgModel> list = new ArrayList<>();
        String Query = "SELECT  * FROM " + FacMessagesHelper.TABLE_NAME;
        open();
        Cursor cursor = sqLiteDatabase.rawQuery(Query, null);
        if (cursor.moveToFirst()) {
            do {
                FacMsgModel m = new FacMsgModel();
                m.setId(Integer.parseInt(cursor.getString(0)));
                m.setFacname(cursor.getString(1));
                m.setSubject(cursor.getString(2));
                m.setMsg(cursor.getString(3));
                m.setSentTime(cursor.getString(4));
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


    public void createList(List<FacMsgModel> list) {
        open();
        for (FacMsgModel model : list) {
            ContentValues contentValues = new ContentValues();
            contentValues.put(FacMessagesHelper.TEACHER, model.getFacname());
            contentValues.put(FacMessagesHelper.SUBNAME, model.getSubject());
            contentValues.put(FacMessagesHelper.MESSAGE, model.getMsg());
            contentValues.put(FacMessagesHelper.TIME, model.getSentTime());
            long id = sqLiteDatabase.insert(FacMessagesHelper.TABLE_NAME, null, contentValues);
            model.setId(id);
        }
        close();
        Log.d("Inserted Data ", "Messages");
    }

    public void close() {
        Log.i(LOG_CAT, "Database closed");
        sqLiteOpenHelper.close();
    }

}
