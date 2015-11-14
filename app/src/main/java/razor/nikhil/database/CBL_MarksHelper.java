package razor.nikhil.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikhil Verma on 7/29/2015.
 */
public class CBL_MarksHelper extends SQLiteOpenHelper {
    public String getTABLE_CREATE() {
        return TABLE_CREATE;
    }

    public static final String TABLE_NAME = "my_cbl_marks";
    public static final String _ID = "_id";
    public static final String COLUMN_SUB_NAME = "_sub__num_";
    public static final String COLUMN_SUB_CODE = "_sub__code_";
    public static final String COLUMN_SUBJECT_CLASS_NUMBER = "_subclassnbr_";
    public static final String COLUMN_SUBJECT_TYPE = "_subtype_";
    public static final String COLUMN_SUBJECT_CAT1_ATTEND = "_cat1attend_";
    public static final String COLUMN_SUBJECT_CAT1_MARKS = "_cat1m_";

    public static final String COLUMN_SUBJECT_CAT2_ATTEND = "_cat2attend_";
    public static final String COLUMN_SUBJECT_CAT2_MARKS = "_cat2m_";

    public static final String COLUMN_SUBJECT_QUIZ1_ATTEND = "_quiz1attend_";
    public static final String COLUMN_SUBJECT_QUIZ1_MARKS = "_quiz1m_";


    public static final String COLUMN_SUBJECT_QUIZ2_ATTEND = "_quiz2attend_";
    public static final String COLUMN_SUBJECT_QUIZ2_MARKS = "_quiz2m_";


    public static final String COLUMN_SUBJECT_QUIZ3_ATTEND = "_quiz3attend_";
    public static final String COLUMN_SUBJECT_QUIZ3_MARKS = "_quiz3m_";

    public static final String COLUMN_SUBJECT_ASSIGN_ATTEND = "_assign_attend_";
    public static final String COLUMN_SUBJECT_ASSIGN_MARKS = "_assignm_";

    public static final String COLUMN_SUBJECT_LABCAM_ATTEND = "_labcam_att_";
    public static final String COLUMN_SUBJECT_LABCAM_MARKS = "_labcam_mar_";
    private final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_SUBJECT_CLASS_NUMBER + " TEXT, " +
            COLUMN_SUBJECT_TYPE + " TEXT, " +
            COLUMN_SUBJECT_CAT1_ATTEND + " TEXT, " +
            COLUMN_SUBJECT_CAT1_MARKS + " TEXT, " +
            COLUMN_SUBJECT_CAT2_ATTEND + " TEXT, " +
            COLUMN_SUBJECT_CAT2_MARKS + " TEXT, " +
            COLUMN_SUBJECT_QUIZ1_ATTEND + " TEXT, " +
            COLUMN_SUBJECT_QUIZ1_MARKS + " TEXT, " +
            COLUMN_SUBJECT_QUIZ2_ATTEND + " TEXT, " +
            COLUMN_SUBJECT_QUIZ2_MARKS + " TEXT, " +
            COLUMN_SUBJECT_QUIZ3_ATTEND + " TEXT, " +
            COLUMN_SUBJECT_QUIZ3_MARKS + " TEXT, " +
            COLUMN_SUBJECT_ASSIGN_ATTEND + " TEXT, " +
            COLUMN_SUBJECT_ASSIGN_MARKS + " TEXT, " +
            COLUMN_SUBJECT_LABCAM_ATTEND + " TEXT, " +
            COLUMN_SUBJECT_LABCAM_MARKS + " TEXT, " +
            COLUMN_SUB_NAME + " TEXT, " +
            COLUMN_SUB_CODE + " TEXT " + ")";
    //Logcat Tag
    private static final String LOG_CAT = "Sqlite.VitAcademics.Cbl.Marks";
    //Database Name
    private static final String DATABASE_NAME = "Sqlite.VitAcademics.Marks";
    //Database Version
    private static final int DATABASE_VERSION = 1;
    private static Context context = null;


    public CBL_MarksHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(TABLE_CREATE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
