package razor.nikhil.Fragments;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.gc.materialdesign.widgets.SnackBar;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import razor.nikhil.Http.BitmapUrlClient;
import razor.nikhil.Http.Http;
import razor.nikhil.Http.Logins;
import razor.nikhil.Http.ParseTimeTable;
import razor.nikhil.R;
import razor.nikhil.database.FacMsgGS;
import razor.nikhil.model.FacMsgModel;
import razor.nikhil.model.PendLeave;

/**
 * Created by Nikhil Verma on 10/2/2015.
 */
public class LeaveMainPost extends Fragment {
    private static HttpClient httpClient;
    public static int List_tr_Size;
    static List<PendLeave> list = new ArrayList<>();
    private EditText REGNO, PASS, CAPTXT;
    private ImageView CAPIMAG;
    private Button button;
    private String POST_LINK_PRE = "https://academics.vit.ac.in/student/leave_request.asp";
    private String MAIN_POST_LINK = "https://academics.vit.ac.in/student/leave_request_submit.asp";
    private static boolean isPending;

    public static HttpClient getHttpClient() {
        return httpClient;
    }

    public static LeaveMainPost newInstance(boolean isqPending) {
        LeaveMainPost fragment = new LeaveMainPost();
        isPending = isqPending;
        return fragment;
    }

    private class LoadCaptcha extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                final Bitmap bmp = BitmapUrlClient.getBitmapFromURL(GetFacDataStudLogin.student_Login_Captcha_Link, httpClient);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        CAPIMAG.setImageBitmap(bmp);
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
        return inflater.inflate(R.layout.login_layout_stud, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        try {
            httpClient = GetDetails.getThreadSafeClient();
            new LoadCaptcha().execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void init(View view) {
        REGNO = (EditText) view.findViewById(R.id.regno_studentlog_myt);
        PASS = (EditText) view.findViewById(R.id.pass_studentlog_myt);
        CAPIMAG = (ImageView) view.findViewById(R.id.capImg_studentlog_myt);
        CAPTXT = (EditText) view.findViewById(R.id.capTxt_studentlog_myt);
        button = (Button) view.findViewById(R.id.proceedBut_studentlog_myt);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mysync();
            }
        });
    }

    private void mysync() {
        new AsyncTask<Void, String, String>() {
            String uname = "", pass = "", cap = "";
            String appprovAuth = "", ltype = "", exitD = "", exitHr = "", exitMin = "", exitPeriod = "", entryD = "", entryHr = "", entryMin = "", entryPeriod = "", place = "", reason = "";

            private void setParams() {
                appprovAuth = LeaveApply.approvAuth.getSelectedItem().toString();
                appprovAuth = LeaveApply.mapApprov.get(appprovAuth);
                ltype = LeaveApply.LeaveType.getSelectedItem().toString();
                ltype = LeaveApply.mapLTYPE.get(ltype);
                exitD = LeaveApply.exitDate.getText().toString();
                try {
                    exitD = changeDateFormat(exitD);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                exitHr = LeaveApply.exit_hr.getSelectedItem().toString();
                exitMin = LeaveApply.exit_min.getSelectedItem().toString();
                exitPeriod = LeaveApply.exit_am_pm.getSelectedItem().toString();
                entryD = LeaveApply.entryDate.getText().toString();
                try {
                    entryD = changeDateFormat(entryD);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                entryHr = LeaveApply.entry_hr.getSelectedItem().toString();
                entryMin = LeaveApply.entry_min.getSelectedItem().toString();
                entryPeriod = LeaveApply.entry_am_pm.getSelectedItem().toString();
                place = LeaveReason.place.getText().toString();
                reason = LeaveReason.reason.getText().toString();
            }

            private String changeDateFormat(String ip) throws ParseException {
                SimpleDateFormat sdf =
                        new SimpleDateFormat("dd-MM-yyyy");
                Date date = sdf.parse(ip);
                sdf.applyPattern("dd-MMM-yyyy");
                return sdf.format(date);
            }

            ProgressDialog dialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                String msg = "Fetching Data";
                String msg1 = "Applying for Leave";
                String y = msg;
                if (isPending)
                    y = msg1;
                dialog = ProgressDialog.show(getActivity(), y,
                        "Wait", true);
                button.setEnabled(false);
                cap = CAPTXT.getText().toString();
                uname = REGNO.getText().toString();
                pass = PASS.getText().toString();
                if (isPending)
                    setParams();
            }

            @Override
            protected void onProgressUpdate(String... values) {
                super.onProgressUpdate(values);
                dialog.setMessage(values[0]);
            }

            @Override
            protected String doInBackground(Void... params) {
                try {
                    httpClient = Logins.StudentLogin(uname, pass, cap, httpClient);
                    if (Logins.isStudLogin.equals("y"))
                        publishProgress("Logged In");
                    else {
                        publishProgress("Error in Logging In!");
                        return null;
                    }
                } catch (Exception e) {
                    publishProgress("Error in Logging In!");
                    e.printStackTrace();
                    return null;
                }
                Http.getData("https://academics.vit.ac.in/student/stud_home.asp", httpClient);//need this as a pre-req
                String data = Http.getData(POST_LINK_PRE, httpClient);//3nd pre-reqr

                if (isPending)
                    return applyLeave();
                else {
                    boolean but = getPendData(data);
                    if (!but)
                        return "npr";
                    return "pend";
                }
            }

            private boolean getPendData(String data) {

                Element ele = null;
                try {
                    ele = Jsoup.parse(data).getElementsByTag("table").get(2);
                } catch (Exception e) {
                    e.printStackTrace();
                    return false;
                }
                Elements trS = ele.getElementsByTag("tr");
                trS.remove(0);
                LeaveMainPost.List_tr_Size = trS.size();
                List<PendLeave> list = new ArrayList<>();
                for (Element ob : trS) {
                    PendLeave pl = new PendLeave();
                    Elements tdS = ob.getElementsByTag("td");
                    pl.setIdL(tdS.get(1).getElementsByTag("font").get(0).html().trim());
                    pl.setName(tdS.get(3).getElementsByTag("font").get(0).html().trim());
                    pl.setFrom(tdS.get(4).getElementsByTag("font").get(0).html().trim());
                    pl.setTo(tdS.get(5).getElementsByTag("font").get(0).html().trim());
                    pl.setType(tdS.get(6).getElementsByTag("font").get(0).html().trim());
                    pl.setStatus(tdS.get(7).getElementsByTag("font").get(0).html().trim());
                    list.add(pl);
                }
                LeaveMainPost.list = list;
                return true;
            }

            private String applyLeave() {
                HashMap<String, String> map = new HashMap<>();
                map.put("apply", appprovAuth);
                map.put("lvtype", ltype);
                //ot
                map.put("exitdate", exitD);
//            mapLTYPE.put("exitdate", "21-Sep-2015");
                map.put("sttime_hh", exitHr);//After 7am defore 6pm , ,max diff 6 hrs
                map.put("sttime_mm", exitMin);
                map.put("frm_timetype", exitPeriod);
                map.put("reentry_date", entryD);
                map.put("endtime_hh", entryHr);
                map.put("endtime_mm", entryMin);
                map.put("to_timetype", entryPeriod);

                map.put("place", place);
                map.put("reason", reason);
                //Corrected the error
                map.put("requestcmd", "Apply");//requires this Also , as the name for submit is changed

                try {
                    Http.getData("https://academics.vit.ac.in/student/stud_home.asp", httpClient);//need this as a pre-req
                    Http.getData(POST_LINK_PRE, httpClient);//3nd pre-reqr
                    Http.getData("https://academics.vit.ac.in/student/leave_apply_dt.asp?x=%20&lvtyp=" + ltype, httpClient);//2nd pre-req
                    publishProgress("Applying For Leave..");
                    final String data = Http.postMethod(MAIN_POST_LINK, map, httpClient);
                    publishProgress("Success");
                    Log.d("Data", data);
                    return data;
                    //final String finalData = Http.getData(GET_SYLL, httpClient);
//                mapLTYPE = new HashMap<>();//cleared
//                mapLTYPE.put("crscd", "BIF304");
//                mapLTYPE.put("crstp", "TH");
//                mapLTYPE.put("version", "1");
//                mapLTYPE.put("sybcmd", "Download");
//                final InputStream stream = Http.postMethodStream(POST_SYLL, mapLTYPE, httpClient);
                    // saveFile(stream);
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //          Toast.makeText(getActivity(), "File Saved in Root Directory as\t" + "BIF304_Syllabus.pdf", Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                    getActivity().runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            //          log.setText(we + "\n\n" + weq + "\n\n\n" + data);
//                        }
//                    });
                } catch (Exception e) {
                    Log.d("Exception", e.toString());
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String aVoid) {
                super.onPostExecute(aVoid);
                if (isPending) {
                    dialog.dismiss();
                    String no = "Error in Applying Leave!";
                    String yes = "Leave Applied Successfully";
                    String param = no;
                    if (aVoid.contains("Apply Id"))
                        param = yes;
                    SnackBar snackbar = new SnackBar(getActivity(), param, null, null);
                    snackbar.show();
                } else {
                    dialog.dismiss();
                    if (aVoid.equals("npr")) {
                        SnackBar snackbar = new SnackBar(getActivity(), "No Pending Leave Requests!", null, null);
                        snackbar.show();
                    } else
                        calLFrag(LeaveMainPost.list);
                }
            }
        }.execute();
    }

    private List<FacMsgModel> parse(String dataw) {
        int gap = 0;
        List<FacMsgModel> list = new ArrayList<>();
        Elements data = null;
        try {
            data = Jsoup.parse(dataw).getElementsByTag("table");

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        Elements datas = data.get(data.size() - 1).getElementsByTag("tr");
        String facname = null, time = null, subject, message = null;
        try {
            datas.remove(0);//title
            datas.remove(0);//sem
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        for (int i = 0; i < 15; i++)//10 msgs
            try {//Msgs
                FacMsgModel model = new FacMsgModel();
                facname = datas.get(0 + gap).getElementsByTag("td").get(2).html();
                subject = datas.get(1 + gap).getElementsByTag("td").get(2).html();
                message = datas.get(2 + gap).getElementsByTag("td").get(2).html();
                time = datas.get(3 + gap).getElementsByTag("td").get(2).html();
                model.setFacname(ParseTimeTable.FirstCharCap(facname));
                model.setSubject(subject);
                model.setMsg(message);
                model.setSentTime(time);
                list.add(model);
                gap += 5;
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        if (list.size() > 0) {
            FacMsgGS gs = new FacMsgGS(getActivity());
            if (gs.getEntriesCount() == 0) {
                gs.createList(list);
            } else {
                gs.Delete();
                gs.createList(list);
            }
        }
        return list;
    }

    private void calLFrag(List<PendLeave> list) {
        button.setEnabled(true);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container_main, LeavePending.newInstance(list)).commit();
    }

}
