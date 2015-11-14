package razor.nikhil.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
        rectangle = (ButtonRectangle) view.findViewById(R.id.leave_reasonfrag_button);
        rectangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valid())
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container_main, LeaveMainPost.newInstance(true)).addToBackStack(null).commit();
            }
        });
    }

    private boolean valid() {
        String o = place.getText().toString();
        String m = reason.getText().toString();
        if (o.equals("")
                || o.equals(null) || o.equals("Place")) {//|| m.equals("") || m.equals(null)) {
            place.setError("Place cannot be blank!");
            return false;
        }
        if (o.length() <= 5) {
            place.setError("Place should be greater than 5 chars!");
            return false;
        }
        if (m.equals("")
                || m.equals(null) || m.equals("Reason")) {//|| m.equals("") || m.equals(null)) {
            reason.setError("Reason cannot be blank!");
            return false;
        }
        if (m.length() <= 5) {
            reason.setError("Reason should be greater than 5 chars!");
            return false;
        }
        return true;
    }
}

