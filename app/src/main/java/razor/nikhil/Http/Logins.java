package razor.nikhil.Http;

import android.content.Context;
import android.util.Log;

import org.apache.http.HttpException;
import org.apache.http.client.HttpClient;

import java.io.IOException;
import java.util.HashMap;

import razor.nikhil.Fragments.GetFacDataStudLogin;
import razor.nikhil.database.SharedPrefs;

/**
 * Created by Nikhil Verma on 9/30/2015.
 */

public class Logins {
    public static String isStudLogin = null;
    public static String isParLogin = null;

    public static HttpClient ParentLogin( HttpClient httpClient, String uname, String dob, String ward, String captcha) {
        HashMap<String, String> headers = new HashMap<>();
        headers.put("wdregno", uname);
        headers.put("wdpswd", dob);
        headers.put("wdmobno", ward);
        headers.put("vrfcd", captcha);
        String dat = "";
        try {
            dat = Http.postMethod(PostParent.PARENT_LOGIN_POST_URL, headers, httpClient);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (dat.contains("Logout")) {
            isParLogin = "y";
            Log.d("Parent Login", "Success");
        } else Log.d("ParentLogErr-PostData", dat);
        return httpClient;
    }

    public static HttpClient StudentLogin(String reg, String pass, String captchaText, HttpClient httpClient) {
        HashMap<String, String> headers = new HashMap<>();
        Log.d("Cred", reg + "\t" + pass + "\t" + captchaText);
        headers.put("regno", reg);
        headers.put("passwd", pass);
        headers.put("vrfcd", captchaText);
        String dat = "";
        try {
            dat = Http.postMethod(GetFacDataStudLogin.student_Login_Link, headers, httpClient);
        } catch (HttpException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (dat.contains("Welcome")) {
            isStudLogin = "y";
            Log.d("Student Login", "Success");
        } else {
            Log.d("StudentLogErr-PostData", dat);
            isStudLogin = "n";
        }
        return httpClient;
    }

    public static boolean isStudentLoggedIn(HttpClient client, Context context) {
        String data = Http.getData("https://academics.vit.ac.in/student/home.asp", client);
        String reg = new SharedPrefs(context).getMsg(SharedPrefs.PREFIX_REGNO).toUpperCase();
        return data.contains(reg);
    }

    public static boolean isParentLoggedIn(HttpClient client, Context context) {
        String data = Http.getData("https://academics.vit.ac.in/parent/home.asp", client);
        String reg = new SharedPrefs(context).getMsg(SharedPrefs.PREFIX_REGNO).toUpperCase();
        return data.contains(reg);
    }

}
