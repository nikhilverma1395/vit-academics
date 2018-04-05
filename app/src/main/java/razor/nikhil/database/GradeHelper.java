package razor.nikhil.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikhil Verma on 9/16/2015.
 */
public class GradeHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "grades";
    public static final String ID = "_id";
    public static final String SUBCODE = "subcode";
    public static final String NAME = "subname";
    public static final String TYPE = "subtype";
    public static final String CREDITS = "credits";
    public static final String GRADE = "grade";
    public static final String EXAMHELD = "examheld";
    public static final String RESULTDATE = "resultdate";
    public static final String COURSEOPTION = "courseoption";
    private static final String DATABASE_NAME = "Sqlite.VitAcademics.Grades";
    private static final int DATABASE_VERSION = 1;
    private final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SUBCODE + " TEXT, " +
            NAME + " TEXT, " +
            TYPE + " TEXT, " +
            CREDITS + " TEXT, " +
            GRADE + " TEXT, " +
            EXAMHELD + " TEXT, " +
            RESULTDATE + " TEXT, " +
            COURSEOPTION + " TEXT " + ")";


    public GradeHelper(Context context) {
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
}
