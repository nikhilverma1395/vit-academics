/**
 * Created by Nikhil Verma on 16-12-2014.
 */

package razor.nikhil.Http;

import android.util.Log;

import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Http {
    public static String CRED_ERR = "credErr";
    public static String NET_ERR = "NETErr";

    public static String getData(String url, HttpClient client) {
        HttpGet request = new HttpGet(url);
        HttpResponse response = null;
        try {
            response = client.execute(request);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String html = "";
        InputStream in = null;
        try {
            in = response.getEntity().getContent();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder str = new StringBuilder();
        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                str.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        html = str.toString();
        return html;
    }

    public static String postMethod(String url, HashMap<String, String> headers, HttpClient client) throws HttpException, IOException {
        HttpPost httppost = null;
        try {
            httppost = new HttpPost(url);
            List<NameValuePair> nameValuePair = new ArrayList<>(headers.size());
            for (Map.Entry entry : headers.entrySet()) {
                nameValuePair.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
            }
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return NET_ERR;
            }
            String op = "";
            try {
                HttpResponse response = client.execute(httppost);
                op = EntityUtils.toString(response.getEntity());
                return op;
            } catch (Exception e) {
                e.printStackTrace();
                Log.d("Data", op);
                return NET_ERR;
            }

        } catch (Exception e) {
            e.printStackTrace();
            return NET_ERR;
        }
    }

    public static InputStream postMethodStream(String url, HashMap<String, String> headers, HttpClient client) throws HttpException, IOException {
        HttpPost httppost = null;
        try {
            httppost = new HttpPost(url);
            List<NameValuePair> nameValuePair = new ArrayList<>(headers.size());
            for (Map.Entry entry : headers.entrySet()) {
                nameValuePair.add(new BasicNameValuePair(entry.getKey().toString(), entry.getValue().toString()));
            }
            try {
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePair));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                HttpResponse response = client.execute(httppost);
                final InputStream op = response.getEntity().getContent();
                return op;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("POST ERROR", "Error in Http.java Stream");
        return null;
    }
}
