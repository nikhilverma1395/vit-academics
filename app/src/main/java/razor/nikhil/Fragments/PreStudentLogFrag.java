package razor.nikhil.Fragments;

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
import android.widget.Toast;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
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

/**
 * Created by Nikhil Verma on 10/2/2015.
 */
public class PreStudentLogFrag extends Fragment {
    private static HttpClient httpClient;
    private EditText REGNO, PASS, CAPTXT;
    private ImageView CAPIMAG;
    private Button button;
    private String POST_LINK_PRE = "https://academics.vit.ac.in/student/leave_request.asp";
    private String MAIN_POST_LINK = "https://academics.vit.ac.in/student/leave_request_submit.asp";

    public static PreStudentLogFrag newInstance() {
        PreStudentLogFrag fragment = new PreStudentLogFrag();
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
        new AsyncTask<Void, Void, Void>() {
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
                Log.d("dskjdbsd", entryD + "\t" + exitD);
            }

            private String changeDateFormat(String ip) throws ParseException {
                SimpleDateFormat sdf =
                        new SimpleDateFormat("dd-MM-yyyy");
                Date date = sdf.parse(ip);
                sdf.applyPattern("dd-MMM-yyyy");
                return sdf.format(date);
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                button.setEnabled(false);
                cap = CAPTXT.getText().toString();
                uname = REGNO.getText().toString();
                pass = PASS.getText().toString();
                setParams();
            }


            @Override
            protected Void doInBackground(Void... params) {
                try {
                    httpClient = Logins.StudentLogin(uname, pass, cap, httpClient);
                } catch (Exception e) {
                    e.printStackTrace();
                }
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
                    final String data = Http.postMethod(MAIN_POST_LINK, map, httpClient);
                    Log.d("Data", data);
                    getActivity().runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (data.contains("Sucessfully")) {
                                                            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }
                    );
                    //final String finalData = Http.getData(GET_SYLL, httpClient);
//                mapLTYPE = new HashMap<>();//cleared
//                mapLTYPE.put("crscd", "BIF304");
//                mapLTYPE.put("crstp", "TH");
//                mapLTYPE.put("version", "1");
//                mapLTYPE.put("sybcmd", "Download");
//                final InputStream stream = Http.postMethodStream(POST_SYLL, mapLTYPE, httpClient);
                    // saveFile(stream);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //          Toast.makeText(getActivity(), "File Saved in Root Directory as\t" + "BIF304_Syllabus.pdf", Toast.LENGTH_SHORT).show();
                        }
                    });
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //          log.setText(we + "\n\n" + weq + "\n\n\n" + data);
                        }
                    });
                } catch (Exception e) {
                    Log.d("Exception", e.toString());
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
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

    private void calLFrag(List<FacMsgModel> list) {
        button.setEnabled(true);
        getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container_main, GetFacMsgList.newInstance(list)).commit();
    }

}
