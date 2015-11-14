package razor.nikhil.Fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

import razor.nikhil.Http.BitmapUrlClient;
import razor.nikhil.Http.PostParent;
import razor.nikhil.R;
import razor.nikhil.database.SharedPrefs;

/**
 * Created by Nikhil Verma on 9/12/2015.
 */

public class GetDetails extends Fragment {

    private static HttpClient globalClient;
    //Views
    private boolean enableit = false;
    private EditText REGNO, PASS, CAPTXT;
    private ImageView CAPIMAG;
    private Button button;
    private ProgressBarCircularIndeterminate progress_ll_;

    public GetDetails() {
    }

    public static DefaultHttpClient getThreadSafeClient() {

        DefaultHttpClient client = new DefaultHttpClient();
        ClientConnectionManager mgr = client.getConnectionManager();
        HttpParams params = client.getParams();
        client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,

                mgr.getSchemeRegistry()), params);
        return client;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.login_layout_stud, container, false);
        return vg;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        init(view);
        //Setting up HttpClient
        globalClient = getThreadSafeClient();
        //Get Bitmap for captcha
        try {
            new SetCaptcha().execute();//Setting up Captcha is new AsyncTask()
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPrefs pref = new SharedPrefs(getActivity());
        setText(REGNO, pref.getMsg(SharedPrefs.PREFIX_REGNO));
        setText(PASS, pref.getMsg(SharedPrefs.PREFIX_PASS));
        REGNO.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                if (str != "" && str.length() == 9)
                    enableit = true;
                else {
                    enableit = false;
                    REGNO.setError("Invalid Reg.No. Format(must be 9 letters)!");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        CAPTXT.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                if (str != "" && str.length() == 6)
                    enableit = true;
                else {
                    enableit = false;
                    CAPTXT.setError("Invalid Captcha Format(must be 6 letters)!");
                }
                if (enableit) {
                    button.setEnabled(true);
                } else {
                    button.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog dialog = ProgressDialog.show(getActivity(), "Getting Data",
                        "Wait", true);
                storeAndPOST();
                new PostParent(REGNO.getText().toString(), PASS.getText().toString(), CAPTXT.getText().toString(), globalClient, getActivity(), dialog);
            }
        });
    }

    private void init(View view) {
        progress_ll_ = (ProgressBarCircularIndeterminate) view.findViewById(R.id.progress_ll_log_dialog_);
        REGNO = (EditText) view.findViewById(R.id.regno_studentlog_myt);
        PASS = (EditText) view.findViewById(R.id.pass_studentlog_myt);
        CAPIMAG = (ImageView) view.findViewById(R.id.capImg_studentlog_myt);
        CAPTXT = (EditText) view.findViewById(R.id.capTxt_studentlog_myt);
        button = (Button) view.findViewById(R.id.proceedBut_studentlog_myt);
    }


    private void storeAndPOST() {
        SharedPrefs spref = new SharedPrefs(getActivity());
        spref.storeMsg(SharedPrefs.PREFIX_REGNO, REGNO.getText().toString().trim());
        spref.storeMsg(SharedPrefs.PREFIX_PASS, PASS.getText().toString().trim());
    }

    private void setText(EditText et, String value) {
        et.setText(value);
    }

    public  void reloadCaptcha() {
    new SetCaptcha().execute();
    }

    //Setting Up Captcha
    public class SetCaptcha extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Bitmap myBitmap = null;
            try {
                myBitmap = new BitmapUrlClient().getBitmapFromURL(GetFacDataStudLogin.student_Login_Captcha_Link, globalClient);
                final Bitmap rma = myBitmap;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            CAPIMAG.setImageBitmap(rma);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}