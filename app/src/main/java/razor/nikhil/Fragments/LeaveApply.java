package razor.nikhil.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import fr.ganfra.materialspinner.MaterialSpinner;
import razor.nikhil.R;

/**
 * Created by Nikhil Verma on 10/3/2015.
 */

public class LeaveApply extends Fragment implements DatePickerDialog.OnDateSetListener {
    MaterialSpinner approvAuth, LeaveType, exit_hr, exit_min, exit_am_pm, entry_hr, entry_min, entry_am_pm;
    TextView exitDate, entryDate;
    DatePickerDialog dpd1;
    DatePickerDialog dpd2;
    String date = null;
    boolean one = false, two = false;

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
    }

    private void init(View view) {
        String[] ltypes = {"Emergency Leave",};
        //   ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_row, {});
        approvAuth = (MaterialSpinner) view.findViewById(R.id.approv_auth);
        LeaveType = (MaterialSpinner) view.findViewById(R.id.leave_type);
        exit_hr = (MaterialSpinner) view.findViewById(R.id.exit_hr);
        exit_min = (MaterialSpinner) view.findViewById(R.id.exit_min);
        exit_am_pm = (MaterialSpinner) view.findViewById(R.id.Am_pm_exit);
        entry_hr = (MaterialSpinner) view.findViewById(R.id.entry_hr);
        entry_min = (MaterialSpinner) view.findViewById(R.id.entry_min);
        entry_am_pm = (MaterialSpinner) view.findViewById(R.id.Am_pm_entry);
        exitDate = (TextView) view.findViewById(R.id.exit_date);
        exitDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one = true;
                two = false;
                Calendar now = Calendar.getInstance();
                dpd2 = DatePickerDialog.newInstance(
                        LeaveApply.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd2.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });
        entryDate = (TextView) view.findViewById(R.id.entry_date);
        entryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                one = false;
                two = true;
                Calendar now = Calendar.getInstance();
                dpd2 = DatePickerDialog.newInstance(
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
        date = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
        if (one && !two)
            exitDate.setText(date);
        else if (!one && two)
            entryDate.setText(date);
    }
}
