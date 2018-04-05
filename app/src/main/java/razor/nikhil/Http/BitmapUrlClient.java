package razor.nikhil.Http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by Nikhil Verma on 9/12/2015.
 */
public class BitmapUrlClient {

    public static Bitmap getBitmapFromURL(String url, HttpClient httpClient) {
        HttpUriRequest request = new HttpGet(url.toString());
        HttpResponse response = null;
        byte[] bytes;
        try {
            try {
                response = httpClient.execute(request);
            } catch (IOException e) {
                e.printStackTrace();
            }
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                bytes = new byte[0];
                bytes = EntityUtils.toByteArray(entity);

                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0,
                        bytes.length);
                return bitmap;
            } else {
                try {
                    throw new IOException("Download failed bitch , HTTP response  "
                            + statusCode + " - " + statusLine.getReasonPhrase());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }
}
