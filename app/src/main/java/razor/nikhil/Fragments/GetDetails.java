package razor.nikhil.Fragments;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.github.florent37.materialtextfield.MaterialTextField;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;

import razor.nikhil.Http.BitmapUrlClient;
import razor.nikhil.Http.MySSLSocketFactory;
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
    private EditText regno, dob, parentcell, captcha;
    private ImageView captchaIV;
    private boolean enableit = false;
    private ButtonRectangle proceed;
    private ProgressBarCircularIndeterminate progress_bar;
    private String regno_str = "", dob_str = "", parentcell_str = "", captcha_str = "";
    private MaterialTextField mtf_reg, mtf_parcell, mtf_dob, mtf_cap;
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
        ViewGroup vg = (ViewGroup) inflater.inflate(R.layout.get_details, container, false);
        return vg;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mtf_reg = (MaterialTextField) view.findViewById(R.id.regno_parent);
        mtf_parcell = (MaterialTextField) view.findViewById(R.id.parentcell_parent);
        mtf_dob = (MaterialTextField) view.findViewById(R.id.dob_parent);
        mtf_cap = (MaterialTextField) view.findViewById(R.id.captcha_parent);
        regno = (EditText) view.findViewById(R.id.regno_getdet);
        dob = (EditText) view.findViewById(R.id.dob_getdet);
        parentcell = (EditText) view.findViewById(R.id.parentcellno_getdet);
        captcha = (EditText) view.findViewById(R.id.captcha_getdet);
        proceed = (ButtonRectangle) view.findViewById(R.id.proceed_getdet);
        proceed.setEnabled(false);
        captchaIV = (ImageView) view.findViewById(R.id.get_details_captcha_iv);
        //Setting up HttpClient
        globalClient = MySSLSocketFactory.getNewHttpClient();
        //Get Bitmap for captcha
        try {
            new SetCaptcha().execute();//Setting up Captcha is new AsyncTask()
        } catch (Exception e) {
            e.printStackTrace();
        }
        progress_bar = (ProgressBarCircularIndeterminate) view.findViewById(R.id.progress_getdet);
        SharedPrefs pref = new SharedPrefs(getActivity());
        setText(regno, pref.getMsg(SharedPrefs.PREFIX_REGNO));
        setText(dob, pref.getMsg(SharedPrefs.PREFIX_DOB));
        setText(parentcell, pref.getMsg(SharedPrefs.PREFIX_PARENTCELL));
        setText(captcha, "");
        regno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                if (str != "")
                    enableit = true;
                else
                    enableit = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        dob.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                if (str != "")
                    enableit = true;
                else
                    enableit = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        parentcell.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                if (str != "")
                    enableit = true;
                else
                    enableit = false;
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        captcha.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String str = charSequence.toString();
                if (str != "")
                    enableit = true;
                else
                    enableit = false;
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
                storeAndPOST();
                new PostParent(regno_str, dob_str, parentcell_str, captcha_str, globalClient, getActivity());
                new GetReturn().execute();
            }
        });
    }

    private void storeAndPOST() {
        regno_str = regno.getText().toString().trim();
        dob_str = dob.getText().toString().trim();
        parentcell_str = parentcell.getText().toString().trim();
        captcha_str = captcha.getText().toString().trim();
        mtf_dob.setVisibility(View.GONE);
        mtf_parcell.setVisibility(View.GONE);
        mtf_reg.setVisibility(View.GONE);
        mtf_cap.setVisibility(View.GONE);
        captchaIV.setVisibility(View.GONE);
        proceed.setVisibility(View.GONE);
        progress_bar.setVisibility(View.VISIBLE);
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

    private class GetReturn extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            while (true) {
                String mark = new SharedPrefs(getActivity()).getMsg("marksdone");
                String att = new SharedPrefs(getActivity()).getMsg("ttdone");
                String tt = new SharedPrefs(getActivity()).getMsg("attendone");
                if ((mark == "y" && att == "y" && tt == "y") || (mark.equals("y") && att.equals("y") && tt.equals("y"))) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            progress_bar.setVisibility(View.GONE);
                            if (gd.isAdded() && gd != null)
                                getActivity().getSupportFragmentManager()
                                        .beginTransaction().remove(gd).commit();
                        }
                    });
                }
                return null;
            }
        }

    }
}