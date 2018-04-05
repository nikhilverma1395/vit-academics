package razor.nikhil.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikhil Verma on 9/30/2015.
 */
public class FacMessagesHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "facmessage";
    public static final String ID = "id";
    public static final String SUBNAME = "subname";
    public static final String MESSAGE = "message";
    public static final String TIME = "time";
    public static final String TEACHER = "teacher";
    private static final String DATABASE_NAME = "Sqlite.VitAcademics.messages";
    private static final int DATABASE_VERSION = 1;
    private final String TABLE_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
            ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            TEACHER + " TEXT, " +
            SUBNAME + " TEXT, " +
            MESSAGE + " TEXT, " +
            TIME + " TEXT " + ")";

    public  String getTABLE_CREATE() {
        return TABLE_CREATE;
    }

    public FacMessagesHelper(Context context) {
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

