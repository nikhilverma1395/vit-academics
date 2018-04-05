package razor.nikhil.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikhil Verma on 7/30/2015.
 */
public class PBL_Helper extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "_Subjects_PBL_";
    public static final String _ID = "_id";
    public static final String course_code = "course_code";
    public static final String clas_nbr = "clas_nbr";
    public static final String Option1_Title = "op1_t";
    public static final String Option2_Title = "op2_t";
    public static final String Option3_Title = "op3_t";
    public static final String Option4_Title = "op4_t";
    public static final String Option5_Title = "op5_t";
    public static final String Option1_Max_Mark = "op1_mm";
    public static final String Option2_Max_Mark = "op2_mm";
    public static final String Option3_Max_Mark = "op3_mm";
    public static final String Option4_Max_Mark = "op4_mm";
    public static final String Option5__Max_Mark = "op5_mm";
    public static final String Option1_Weightage = "op1_wi";
    public static final String Option2_Weightage = "op2_wi";
    public static final String Option3_Weightage = "op3_wi";
    public static final String Option4_Weightage = "op4_wi";
    public static final String Option5_Weightage = "op5_wi";
    public static final String Option1_Date = "op1d";
    public static final String Option2_Date = "op2d";
    public static final String Option3_Date = "op3d";
    public static final String Option4_Date = "op4d";
    public static final String Option5_Date = "op5d";

    public static final String Option1_Attend = "op1att";
    public static final String Option2_Attend = "op2att";
    public static final String Option3_Attend = "op3att";
    public static final String Option4_Attend = "op4att";
    public static final String Option5_Attend = "op5att";
    public static final String Option1_Scored = "op1sc";
    public static final String Option2_Scored = "op2sc";
    public static final String Option3_Scored = "op3sc";
    public static final String Option4_Scored = "op4sc";
    public static final String Option5_Scored = "op5sc";

    public static final String Option1_Scored_Percent = "op1scp";
    public static final String Option2_Scored_Percent = "op2scp";
    public static final String Option3_Scored_Percent = "op3scp";
    public static final String Option4_Scored_Percent = "op4scp";
    public static final String Option5_Scored_Percent = "op5scp";

    public String getTABLE_CREATE() {
        return TABLE_CREATE;
    }

    private final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            course_code + " TEXT, " +
            clas_nbr + " TEXT, " +
            Option1_Title + " TEXT, " +
            Option2_Title + " TEXT, " +
            Option3_Title + " TEXT, " +
            Option4_Title + " TEXT, " +
            Option5_Title + " TEXT, " +
            Option1_Max_Mark + " TEXT, " +
            Option2_Max_Mark + " TEXT, " +
            Option3_Max_Mark + " TEXT, " +
            Option4_Max_Mark + " TEXT, " +
            Option5__Max_Mark + " TEXT, " +
            Option1_Weightage + " TEXT, " +
            Option2_Weightage + " TEXT, " +
            Option3_Weightage + " TEXT, " +
            Option4_Weightage + " TEXT, " +
            Option5_Weightage + " TEXT, " +
            Option1_Date + " TEXT, " +
            Option2_Date + " TEXT, " +
            Option3_Date + " TEXT, " +
            Option4_Date + " TEXT, " +
            Option5_Date + " TEXT, " +
            Option1_Attend + " TEXT, " +
            Option2_Attend + " TEXT, " +
            Option3_Attend + " TEXT, " +
            Option4_Attend + " TEXT, " +
            Option5_Attend + " TEXT, " +
            Option1_Scored + " TEXT, " +
            Option2_Scored + " TEXT, " +
            Option3_Scored + " TEXT, " +
            Option4_Scored + " TEXT, " +
            Option5_Scored + " TEXT, " +
            Option1_Scored_Percent + " TEXT, " +
            Option2_Scored_Percent + " TEXT, " +
            Option3_Scored_Percent + " TEXT, " +
            Option4_Scored_Percent + " TEXT, " +
            Option5_Scored_Percent + " TEXT " + ")";
    //Logcat Tag
    private static final String LOG_CAT = "Sqlite.VitAcademics";
    //Database Name
    private static final String DATABASE_NAME = "Sqlite.VitAcademics.pbl";
    //Database Version
    private static final int DATABASE_VERSION = 1;
    private static Context context = null;


    public PBL_Helper(Context context) {
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
