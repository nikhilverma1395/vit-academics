package razor.nikhil.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
import com.gc.materialdesign.widgets.SnackBar;

import org.apache.http.client.HttpClient;

import razor.nikhil.Http.BitmapUrlClient;
import razor.nikhil.Http.Logins;
import razor.nikhil.R;

/**
 * Created by Nikhil Verma on 10/10/2015.
 */
public class StudentLoggerDialog extends DialogFragment {
    private static HttpClient httpClient;
    private EditText REGNO, PASS, CAPTXT;
    private ImageView CAPIMAG;
    private Button button;
    private String POST_LINK_PRE = "https://academics.vit.ac.in/student/leave_request.asp";
    private String MAIN_POST_LINK = "https://academics.vit.ac.in/student/leave_request_submit.asp";
    private static boolean isPending;
    private static boolean isLoggedIn = false;
    private ProgressBarCircularIndeterminate progressBarCircularIndeterminate;
    boolean noyhing = false;

    public static boolean getIsLoggedIn() {
        return isLoggedIn;
    }

    public static HttpClient getHttpClient() {
        return httpClient;
    }

    public static WhenLoggedIn listenLooged;

    public abstract interface WhenLoggedIn {
        void Success(HttpClient client);

        void Error(String message);
    }


    public static StudentLoggerDialog newInstance(WhenLoggedIn context) {
        listenLooged = context;
        StudentLoggerDialog fragment = new StudentLoggerDialog();
        return fragment;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.facinfodialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (!noyhing)
            Slots.swipeContainer.setRefreshing(false);
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }

    public StudentLoggerDialog() {
    }

    ProgressDialog dialog;

    private void init(View view) {
        REGNO = (EditText) view.findViewById(R.id.regno_studentlog_myt);
        PASS = (EditText) view.findViewById(R.id.pass_studentlog_myt);
        CAPIMAG = (ImageView) view.findViewById(R.id.capImg_studentlog_myt);
        CAPTXT = (EditText) view.findViewById(R.id.capTxt_studentlog_myt);
        button = (Button) view.findViewById(R.id.proceedBut_studentlog_myt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = ProgressDialog.show(getActivity(), "Wait", "Logging In..", true);
                new mysync().execute();
            }
        });
        progressBarCircularIndeterminate = (ProgressBarCircularIndeterminate) view.findViewById(R.id.progress_ll_log_dialog);
    }

    class mysync extends AsyncTask<Void, Void, Void> {
        String uname = "", pass = "", cap = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            cap = CAPTXT.getText().toString();
            uname = REGNO.getText().toString();
            pass = PASS.getText().toString();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                httpClient = Logins.StudentLogin(uname, pass, cap, httpClient);
                if (Logins.isStudLogin.equals("y")) {
                    isLoggedIn = true;
                } else {
                    isLoggedIn = false;
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            dialog.dismiss();
            if (isLoggedIn) {
                dismiss();
                noyhing = true;
                listenLooged.Success(httpClient);
            } else {
                new LoadCaptcha().execute();
                SnackBar bar = new SnackBar(getActivity(), "Incorrect Credentials or Network Error !", null, null);
                bar.show();
                listenLooged.Error("Incorrect Credentials or Network Error !");
            }
        }
    }

    private class LoadCaptcha extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBarCircularIndeterminate.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final Bitmap bmp = BitmapUrlClient.getBitmapFromURL(GetFacDataStudLogin.student_Login_Captcha_Link, httpClient);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CAPIMAG.setImageBitmap(bmp);
                        progressBarCircularIndeterminate.setVisibility(View.INVISIBLE);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.student_logger_dialog, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        try {
            httpClient = GetDetails.getThreadSafeClient();
            new LoadCaptcha().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
