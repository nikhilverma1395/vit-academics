package razor.nikhil.Fragments.Leave;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gc.materialdesign.views.ButtonRectangle;

import razor.nikhil.R;

/**
 * Created by Nikhil Verma on 10/3/2015.
 */
public class LeavePre extends Fragment {
    ButtonRectangle pending, apply;

    public static LeavePre newInstance() {
        LeavePre fragment = new LeavePre();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.leave_pre, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);

    }

    private void init(View view) {
        apply = (ButtonRectangle) view.findViewById(R.id.apply_leave);
        pending = (ButtonRectangle) view.findViewById(R.id.view_leave_req);
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFrag(R.id.container_main, null, LeaveMainPost.newInstance(false));
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addFrag(R.id.container_main, LeaveApply.newInstance(), null);
            }
        });
    }

    private void addFrag(int container_main, LeaveApply leaveApply, LeaveMainPost leavePending) {
        FragmentTransaction tr = getActivity().getSupportFragmentManager().beginTransaction();
        if (leavePending == null)
            tr.add(container_main, leaveApply).addToBackStack(null).commit();
        else tr.add(container_main, leavePending).addToBackStack(null).commit();
    }
}
