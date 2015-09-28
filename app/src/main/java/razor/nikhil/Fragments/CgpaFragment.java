package razor.nikhil.Fragments;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonRectangle;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import razor.nikhil.Config;
import razor.nikhil.R;
import razor.nikhil.adapter.GPA_Adapter;
import razor.nikhil.database.SharedPrefs;

/**
 * Created by Nikhil Verma on 9/26/2015.
 */
public class CgpaFragment extends Fragment {
    private static List<String> subnames;
    private TextView currentCgpa, calcCgpa, calcGpa;
    private RecyclerView recyclerView;
    private ButtonRectangle calculate;
    private static List<Integer> credits;
    private float sumofcalcred = 0;
    private float prevCredAll;
    private float currentCGPA = 0;

    public static CgpaFragment newInstance(List<String> subname, List<Integer> sumofred) {
        subnames = subname;
        credits = sumofred;
        return new CgpaFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.cgpa_recview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        for (int i = 0; i < credits.size(); i++) {
            sumofcalcred += credits.get(i);
        }
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        GPA_Adapter adap = new GPA_Adapter(getActivity(), subnames);
        recyclerView.setAdapter(adap);
        SharedPrefs sf = new SharedPrefs(getActivity());
        try {
            String curt = sf.getMsg(Config.GPA);//already gives trimmed value
            currentCGPA = Float.parseFloat(curt);
            if (!curt.equals(""))
                currentCgpa.setText("Current CGPA : \n" + curt);
            else currentCgpa.setText("N/A");
            prevCredAll = Integer.parseInt(sf.getMsg(Config.CREDITSEARNED));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Resources r = getResources();
        float pxq = 0;
        boolean tabletSize = getResources().getBoolean(R.bool.isfullhd_tab);
        if (!tabletSize) {
            pxq = 60;
        } else {
            pxq = 85;
        }
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pxq, r.getDisplayMetrics());
        int viewHeight = (int) px * adap.getItemCount();
        recyclerView.getLayoutParams().height = viewHeight;
    }

    private void init(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.rview_cgpa);
        calcCgpa = (TextView) view.findViewById(R.id.calculated_cgpa_calc);
        calcGpa = (TextView) view.findViewById(R.id.calculated_gpa_calc);
        currentCgpa = (TextView) view.findViewById(R.id.cur_cgpa_calc);
        calculate = (ButtonRectangle) view.findViewById(R.id.calc_cgpa_button);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float now = 0;
                HashMap<Integer, Integer> hmap = GPA_Adapter.sel;
                for (Map.Entry<Integer, Integer> item : hmap.entrySet()) {
                    now += item.getValue() * credits.get(item.getKey());
                    Log.d(item.getValue() + "", credits.get(item.getKey()) + "");
                }
                float gpa = now / sumofcalcred;
                calcGpa.setText("Calculated GPA :\n" + gpa + "");
                float cgpa = (now + (prevCredAll * currentCGPA)) / (prevCredAll + sumofcalcred);
                cgpa = (float) round(cgpa, 2, BigDecimal.ROUND_HALF_UP);
                Log.d("CGPA", now + "" + prevCredAll + "" + currentCGPA + "" + sumofcalcred);
                calcCgpa.setText("Calculated CGPA :\n" + cgpa + "");
            }
        });
    }

    public static double round(double unrounded, int precision, int roundingMode) {
        BigDecimal bd = new BigDecimal(unrounded);
        BigDecimal rounded = bd.setScale(precision, roundingMode);
        return rounded.doubleValue();
    }
}
