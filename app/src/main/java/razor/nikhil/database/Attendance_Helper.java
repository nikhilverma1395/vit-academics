package razor.nikhil.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikhil Verma on 9/4/2015.
 */
public class Attendance_Helper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "Attendance_Brief";
    public static final String _ID = "_id";
    public static final String COLUMN_SUBJECT_CODE = "sub_code";
    public static final String COLUMN_ATTENDED = "attended_";
    public static final String COLUMN_TOTAL = "total_";
    public static final String COLUMN_ATTEND_PERCENT = "percent_";
    public static final String COLUMN_subtype = "subtype_";

    public String getTABLE_CREATE() {
        return TABLE_CREATE;
    }

    private final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SUBJECT_CODE + " TEXT, " +
            COLUMN_ATTENDED + " TEXT, " +
            COLUMN_TOTAL + " TEXT, " +
            COLUMN_ATTEND_PERCENT + " TEXT, " +
            COLUMN_subtype + " TEXT " + ");";

    //Logcat Tag
    private static final String LOG_CAT = "Sqlite.VitAcademics";

    private static final String DATABASE_NAME = "Sqlite_attendbrief";
    //Database Version
    private static final int DATABASE_VERSION = 2;


    public Attendance_Helper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
