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

    private static final String parent_captcha_url = "https://academics.vit.ac.in/parent/captcha.asp";
    private static final String PARENT_POST_URL = "https://academics.vit.ac.in/parent/parent_login_submit.asp";
    private static HttpClient globalClient;
    //Views
    private ImageView captchaIV;
    private boolean enableit = false;
    private Button proceed;
    private String regno_str = "", dob_str = "", parentcell_str = "", captcha_str = "";
    private EditText mtf_reg, mtf_parcell, mtf_dob, mtf_cap;
    private GetDetails gd = null;

    public GetDetails() {
    }

    public GetDetails(GetDetails frag) {
        gd = frag;
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
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.login_layout, container, false);
        return vg;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mtf_reg = (EditText) view.findViewById(R.id.mainlogin_regno);
        mtf_parcell = (EditText) view.findViewById(R.id.mainlogin_mobile);
        mtf_dob = (EditText) view.findViewById(R.id.mainlogin_dob);
        mtf_cap = (EditText) view.findViewById(R.id.mainlogin_captcha_text);
        proceed = (Button) view.findViewById(R.id.mainlogin_button);
        proceed.setEnabled(false);
        captchaIV = (ImageView) view.findViewById(R.id.mainlogin_captcha_image);
        //Setting up HttpClient
        globalClient = getThreadSafeClient();
        //Get Bitmap for captcha
        try {
            new SetCaptcha().execute();//Setting up Captcha is new AsyncTask()
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPrefs pref = new SharedPrefs(getActivity());
        setText(mtf_reg, pref.getMsg(SharedPrefs.PREFIX_REGNO));
        setText(mtf_dob, pref.getMsg(SharedPrefs.PREFIX_DOB));
        setText(mtf_parcell, pref.getMsg(SharedPrefs.PREFIX_PARENTCELL));
        setText(mtf_cap, "");
        mtf_reg.addTextChangedListener(new TextWatcher() {
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
                    mtf_reg.setError("Invalid Reg.No. Format(must be 9 letters)!");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mtf_dob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                if (str != "" && str.length() == 8)
                    enableit = true;
                else {
                    enableit = false;
                    mtf_dob.setError("Invalid DOB Format(ex: ddmmyyyy - 16111994)!");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mtf_parcell.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                if (str != "")
                    enableit = true;
                else {
                    enableit = false;
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mtf_cap.addTextChangedListener(new TextWatcher() {
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
                    mtf_cap.setError("Invalid Captcha Format(must be 6 letters)!");
                }
                if (enableit) {
                    proceed.setEnabled(true);
                } else {
                    proceed.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProgressDialog dialog = ProgressDialog.show(getActivity(), "Getting Data",
                        "Wait", true);
                storeAndPOST();
                new PostParent(regno_str, dob_str, parentcell_str, captcha_str, globalClient, getActivity(), gd, dialog);
            }
        });
    }

    private void storeAndPOST() {
        regno_str = mtf_reg.getText().toString().trim();
        dob_str = mtf_dob.getText().toString().trim();
        parentcell_str = mtf_parcell.getText().toString().trim();
        captcha_str = mtf_cap.getText().toString().trim();
        SharedPrefs spref = new SharedPrefs(getActivity());
        spref.storeMsg(SharedPrefs.PREFIX_REGNO, regno_str);
        spref.storeMsg(SharedPrefs.PREFIX_DOB, dob_str);
        spref.storeMsg(SharedPrefs.PREFIX_PARENTCELL, parentcell_str);

    }

    private void setText(EditText et, String value) {
        et.setText(value);
    }

    //Setting Up Captcha
    private class SetCaptcha extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            Bitmap myBitmap = null;
            try {
                myBitmap = new BitmapUrlClient().getBitmapFromURL(parent_captcha_url, globalClient);
                final Bitmap rma = myBitmap;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            captchaIV.setImageBitmap(rma);
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