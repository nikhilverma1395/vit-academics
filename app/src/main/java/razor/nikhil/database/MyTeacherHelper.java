package razor.nikhil.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikhil Verma on 9/27/2015.
 */
public class MyTeacherHelper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "myteachers";
    public static final String ID = "_id";
    public static final String COLUMN_TNAME = "tname";
    public static final String COLUMN_SCHOOL = "tschool";
    public static final String COLUMN_DESIG = "designation";
    public static final String COLUMN_ROOM = "room";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_OPENHRS = "openhrs";
    public static final String COLUMN_DIV = "division";
    public static final String COLUMN_ADDROLE = "addrole";
    private final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TNAME + " TEXT, " +
            COLUMN_SCHOOL + " TEXT, " +
            COLUMN_DESIG + " TEXT, " +
            COLUMN_ROOM + " TEXT, " +
            COLUMN_DIV + " TEXT, " +
            COLUMN_ADDROLE + " TEXT, " +
            COLUMN_EMAIL + " TEXT, " +
            COLUMN_OPENHRS + " TEXT " + ")";
    //Database Name
    private static final String DATABASE_NAME = "Sqlite.VitAcademics.myt";
    //Database Version
    private static final int DATABASE_VERSION = 1;


    public MyTeacherHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
