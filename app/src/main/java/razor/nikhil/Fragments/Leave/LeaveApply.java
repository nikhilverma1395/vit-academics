package razor.nikhil.Fragments.Leave;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.HashMap;

import fr.ganfra.materialspinner.MaterialSpinner;
import razor.nikhil.Config;
import razor.nikhil.R;
import razor.nikhil.database.SharedPrefs;

/**
 * Created by Nikhil Verma on 10/3/2015.
 */

public class LeaveApply extends Fragment implements DatePickerDialog.OnDateSetListener {
    public static MaterialSpinner approvAuth, LeaveType, exit_hr, exit_min, exit_am_pm, entry_hr, entry_min, entry_am_pm;
    public static TextView exitDate, entryDate;
    String date = null;
    public static HashMap<String, String> mapLTYPE = new HashMap<>();
    public static HashMap<String, String> mapApprov = new HashMap<>();
    ButtonRectangle proceed;
    boolean one = false, two = false;
    String appprovAuth = "", ltype = "", exitD = "", exitHr = "", exitMin = "", exitPeriod = "", entryD = "", entryHr = "", entryMin = "", entryPeriod = "", place = "", reason = "";
    String appprovAuthHint = "Approving Authority", ltypeHint = "Leave Type", exitHrHint = "Exit Hr.", exitMinHint = "Exit Min.", exitPeriodHint = "Period",
            entryHrHint = "Entry Hr.",
            entryMinHint = "Entry Min.";

    public static LeaveApply newInstance() {
        LeaveApply fragment = new LeaveApply();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.leave_apply, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        Setup();
    }

    private void init(View view) {
        LeaveType = (MaterialSpinner) view.findViewById(R.id.leave_type);
        approvAuth = (MaterialSpinner) view.findViewById(R.id.approv_auth);
        exit_hr = (MaterialSpinner) view.findViewById(R.id.exit_hr);
        exit_min = (MaterialSpinner) view.findViewById(R.id.exit_min);
        exit_am_pm = (MaterialSpinner) view.findViewById(R.id.Am_pm_exit);
        entry_hr = (MaterialSpinner) view.findViewById(R.id.entry_hr);
        entry_min = (MaterialSpinner) view.findViewById(R.id.entry_min);
        entry_am_pm = (MaterialSpinner) view.findViewById(R.id.Am_pm_entry);
        exitDate = (TextView) view.findViewById(R.id.exit_date);
        entryDate = (TextView) view.findViewById(R.id.entry_date);
        proceed = (ButtonRectangle) view.findViewById(R.id.leave_apply_button);
        proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkFields())
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .add(R.id.container_main, LeaveReason.newInstance()).addToBackStack(null).commit();
            }
        });
    }

    private boolean checkFields() {
        boolean valid = true;
        appprovAuth = approvAuth.getSelectedItem().toString();
        if (appprovAuth.equals(appprovAuthHint)) {
            valid = false;
            approvAuth.setError("Select one field!");
        }
        appprovAuth = LeaveApply.mapApprov.get(appprovAuth);
        ltype = LeaveType.getSelectedItem().toString();
        if (ltype.equals(ltypeHint)) {
            valid = false;
            LeaveType.setError("Select one field!");
        }
        //   ltype = mapLTYPE.get(ltype);
        exitD = exitDate.getText().toString();
        if (exitD.equals("") || exitD.equals(null)) {
            valid = false;
            exitDate.setError("Select Exit Date");
        }
        exitHr = exit_hr.getSelectedItem().toString();
        if (exitHr.equals(exitHrHint)) {
            valid = false;
            exit_hr.setError("Select exit hour!");
        }
        exitMin = exit_min.getSelectedItem().toString();
        if (exitMin.equals(exitMinHint)) {
            valid = false;
            exit_min.setError("Select exit minutes!");
        }
        exitPeriod = exit_am_pm.getSelectedItem().toString();
        if (exitPeriod.equals(exitPeriodHint)) {
            valid = false;
            exit_am_pm.setError("Select exit Period!");
        }
        entryD = entryDate.getText().toString();
        if (entryD.equals("") || entryD.equals(null)) {
            valid = false;
            entryDate.setError("Select return Date");
        }
        entryHr = entry_hr.getSelectedItem().toString();
        if (entryHr.equals(entryHrHint)) {
            valid = false;
            entry_hr.setError("Select entry Hour");
        }
        entryMin = entry_min.getSelectedItem().toString();
        if (entryMin.equals(entryMinHint)) {
            valid = false;
            entry_min.setError("Select entry minutes!");
        }
        entryPeriod = entry_am_pm.getSelectedItem().toString();
        if (entryPeriod.equals(exitPeriodHint)) {
            valid = false;
            entry_am_pm.setError("Select entry Period!");
        }
        return valid;
    }


    public void setAdapter(String[] type, MaterialSpinner mp) {
        ArrayAdapter<String> LTYPES = new ArrayAdapter<>(getActivity(), R.layout.leave_spinner_ror, type);
        mp.setAdapter(LTYPES);
    }

    private void Setup() {
        String[] ltypes = {"Emergency Leave", "Examinations (Gate)", "Home Town / Local Guardians Place", "Industrial Visit (Through Faculty Coordinators)"
                , "Local Guardian", "Off Campus Interviews (Through Pat Office)", "Official Events", "Outing", "Semester Leave", "With Parent Leave"};
        mapLTYPE.put(ltypes[0], "EY");
        mapLTYPE.put(ltypes[1], "AE");
        mapLTYPE.put(ltypes[2], "HT");
        mapLTYPE.put(ltypes[3], "II");
        mapLTYPE.put(ltypes[4], "LG");
        mapLTYPE.put(ltypes[5], "PJ");
        mapLTYPE.put(ltypes[6], "EP");
        mapLTYPE.put(ltypes[7], "OG");
        mapLTYPE.put(ltypes[8], "SL");
        mapLTYPE.put(ltypes[9], "WP");
        setAdapter(ltypes, LeaveType);
        SharedPrefs prefs = new SharedPrefs(getActivity());
        String[] auth = {prefs.getMsg(Config.PM_NAME_CODE_LEAVE), prefs.getMsg(Config.FADV_NAME_CODE_LEAVE)};
        mapApprov.put(prefs.getMsg(Config.PM_NAME_CODE_LEAVE), prefs.getMsg(Config.PM_NAME_CODE_LEAVE_VALUE));
        mapApprov.put(prefs.getMsg(Config.FADV_NAME_CODE_LEAVE), prefs.getMsg(Config.FADV_NAME_CODE_LEAVE_VALUE));
        setAdapter(auth, approvAuth);
        String[] exitHr = {"01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"};
        String[] exitMin = {"00", "05", "10", "15", "20", "25", "30", "35", "40", "45", "50", "55", "60"};
        String[] AmPm = {"AM", "PM"};
        setAdapter(exitHr, exit_hr);
        setAdapter(exitMin, exit_min);
        setAdapter(AmPm, exit_am_pm);
        setAdapter(exitHr, entry_hr);
        setAdapter(exitMin, entry_min);
        setAdapter(AmPm, entry_am_pm);
        exitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDate.setError(null);
                one = true;
                two = false;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd2 = DatePickerDialog.newInstance(
                        LeaveApply.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd2.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });
        entryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                entryDate.setError(null);
                one = false;
                two = true;
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd2 = DatePickerDialog.newInstance(
                        LeaveApply.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd2.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        date = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
        if (one && !two)
            exitDate.setText(date);
        else if (!one && two)
            entryDate.setText(date);
    }
}
