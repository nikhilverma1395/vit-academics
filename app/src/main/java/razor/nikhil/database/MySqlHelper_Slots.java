package razor.nikhil.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikhil Verma on 7/28/2015.
 */
public class MySqlHelper_Slots extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "_Subjects_Registered_";
    public static final String _ID = "_id";
    public static final String COLUMN_SUBJECT_NAME = "_subname_";
    public static final String COLUMN_SUBJECT_CODE = "_subcode_";
    public static final String COLUMN_SUBJECT_CLASS_NUMBER = "_subclassnbr_";
    public static final String COLUMN_SUBJECT_TEACHER = "_subteacher_";
    public static final String COLUMN_SUBJECT_TYPE = "_subtype_";
    public static final String COLUMN_SUBJECT_OPTION = "_suboption_";
    public static final String COLUMN_SUBJECT_MODE = "_submode_";
    public static final String COLUMN_SUBJECT_LTJPC = "_subltjpc_";
    public static final String COLUMN_SUBJECT_SLOT = "_subslot_";
    public static final String COLUMN_SUBJECT_VENUE = "_subvenue_";
    public static final String COLUMN_SUBJECT_REG_DATE = "_subregdate_";
    private final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SUBJECT_NAME + " TEXT, " +
            COLUMN_SUBJECT_CODE + " TEXT, " +
            COLUMN_SUBJECT_CLASS_NUMBER + " TEXT, " +
            COLUMN_SUBJECT_TEACHER + " TEXT, " +
            COLUMN_SUBJECT_TYPE + " TEXT, " +
            COLUMN_SUBJECT_OPTION + " TEXT, " +
            COLUMN_SUBJECT_MODE + " TEXT, " +
            COLUMN_SUBJECT_LTJPC + " TEXT, " +
            COLUMN_SUBJECT_SLOT + " TEXT, " +
            COLUMN_SUBJECT_VENUE + " TEXT, " +
            COLUMN_SUBJECT_REG_DATE + " TEXT " + ")";
    //Database Name
    private static final String DATABASE_NAME = "Sqlite.VitAcademics";
    //Database Version
    private static final int DATABASE_VERSION = 1;

    public String getTABLE_CREATE() {
        return TABLE_CREATE;
    }

    public MySqlHelper_Slots(Context context) {
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

