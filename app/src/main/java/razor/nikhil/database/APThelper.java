package razor.nikhil.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikhil Verma on 10/1/2015.
 */
public class APThelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "apt";
    public static final String ID = "_id";
    public static final String DATE = "date";
    public static final String SESSION = "session";
    public static final String UNIT = "unit";
    private static final String DATABASE_NAME = "Sqlite.VitAcademics.APT";
    private static final int DATABASE_VERSION = 1;
    private final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            DATE + " TEXT, " +
            SESSION + " TEXT, " +
            UNIT + " TEXT " + ")";


    public APThelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public String getTABLE_CREATE() {
        return TABLE_CREATE;
    }
}

