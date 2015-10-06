package razor.nikhil.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.gc.materialdesign.views.ButtonRectangle;

import razor.nikhil.R;

/**
 * Created by Nikhil Verma on 10/6/2015.
 */
public class LeaveReason extends android.support.v4.app.Fragment {
    public static EditText place, reason;
    ButtonRectangle rectangle;

    public static LeaveReason newInstance() {
        LeaveReason fragment = new LeaveReason();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.leave_address_reason, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        place = (EditText) view.findViewById(R.id.leave_place);
        reason = (EditText) view.findViewById(R.id.leave_reason);
        watchers();
        rectangle = (ButtonRectangle) view.findViewById(R.id.leave_reasonfrag_button);
        rectangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container_main, PreStudentLogFrag.newInstance()).addToBackStack(null).commit();
            }
        });
    }

    private void watchers() {
        place.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sw = s.toString();
                if (sw.length() <= 5) {
                    place.setError("Length should be greater than 5!");
                    rectangle.setEnabled(false);
                } else rectangle.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        reason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String sw = s.toString();
                if (sw.length() <= 5) {
                    reason.setError("Length should be greater than 5!");
                    rectangle.setEnabled(false);
                } else rectangle.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
