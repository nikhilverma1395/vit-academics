package razor.myapplication.imagecach;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

import razor.myapplication.R;
import razor.myapplication.SharedPrefs;

/**
 * Created by Nikhil Verma on 9/12/2015.
 */
public class StoreImage extends AppCompatActivity {
    ImageView ivp;
    private static final String Url = "http://www.thehardtackle.com/wp-content/uploads/2014/11/Bayern-M%C3%BCnchen-old-logo.png";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ivpp);
        ivp = (ImageView) findViewById(R.id.imagebauern);

        try {
            ivp.setImageBitmap(loadImageFromStorage(new SharedPrefs(getApplicationContext()).getMsg("pathim")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loaderup(final View view) {
        try {
            new Storer().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Storer extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Bitmap image = null;
            try {
                image = BitmapFactory.decodeStream((new URL(Url)).openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            final Bitmap rma = image;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ivp.setImageBitmap(rma);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            new SharedPrefs(getApplicationContext()).storeMsg("pathim", saveToInternalSorage(rma));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(getApplicationContext(), "Done", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap loadImageFromStorage(String path) {

        try {
            File f = new File(path, "bayern1.jpg");
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String saveToInternalSorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "bayern1.jpg");

        FileOutputStream fos = null;
        try {

            fos = new FileOutputStream(mypath);

            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return directory.getAbsolutePath();
    }
}
