package razor.nikhil.database;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Nikhil Verma on 9/12/2015.
 */
public class SharedPrefs {

    public static final String PREFIX_REGNO = "regno_sf";
    public static final String PREFIX_PASS = "pass_sf";
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SharedPrefs(Context context) {
        preferences = context.getSharedPreferences("USER_DETAILS", Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public void storeMsg(String key, String value) {
        editor.putString(key, value.trim()).commit();
    }

    public String getMsg(String prefix) {
        return preferences.getString(prefix, "").trim();
    }

}
