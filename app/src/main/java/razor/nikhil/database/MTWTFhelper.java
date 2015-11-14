package razor.nikhil.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Nikhil Verma on 9/5/2015.
 */
public class MTWTFhelper extends SQLiteOpenHelper {
    private String table[] = {"monday", "tuesday", "wednesday", "thursday", "friday"};
    public static final String COLUMN_SUBJECT_NAME = "_subname_";
    public static final String COLUMN_SUBJECT_VENUE = "_subvenue_";
    public static final String COLUMN_SUBJECT_CODE = "_subcode_";
    public static final String COLUMN_SUBJECT_TYPE = "_subtype_";
    public static final String COLUMN_TIMINGS = "_subtime_";
    public static final String COLUMN_SLOT = "slot";
    private String Queries[] = new String[5];

    public MTWTFhelper(Context context) {
        super(context, "MTWTF", null, 1);
        for (int r = 0; r < 5; r++) {
            Queries[r] = getQueryByTable(table[r]);
        }
    }

    public String getQueryByTable(String TABLE) {
        String st = "CREATE TABLE " + TABLE + " (" +
                "_id" + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SUBJECT_NAME + " TEXT, " +
                COLUMN_SUBJECT_CODE + " TEXT, " +
                COLUMN_SUBJECT_TYPE + " TEXT, " +
                COLUMN_SUBJECT_VENUE + " TEXT, " +
                COLUMN_TIMINGS + " TEXT, " +
                COLUMN_SLOT + " TEXT " + ");";
        return st;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (int r = 0; r < 5; r++)
            db.execSQL(Queries[r]);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        for (int r = 0; r < 5; r++)
            db.execSQL("DROP TABLE IF EXISTS " + table[r]);
        onCreate(db);
    }
}
