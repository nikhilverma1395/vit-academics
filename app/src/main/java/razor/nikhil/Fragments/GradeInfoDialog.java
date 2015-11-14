package razor.nikhil.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import razor.nikhil.R;
import razor.nikhil.model.GradeModel;

/**
 * Created by Nikhil Verma on 9/19/2015.
 */
public class GradeInfoDialog extends DialogFragment {
    private TextView subname, subcode, grade, type, examdate, resdate, credit, option;
    private static GradeModel model;
    private Typeface regular;
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.facinfodialog;
    }

    public GradeInfoDialog() {
    }

    public static GradeInfoDialog newInstance(GradeModel mod) {
        GradeInfoDialog frag = new GradeInfoDialog();
        model = mod;
        return frag;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.gradeinfodialog, container);
        regular = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        init(view);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        subname.setText(model.getSubname());
        subname.setTypeface(regular);
        subcode.setText(model.getSubcode());
        subcode.setTypeface(regular);
        grade.setText(model.getGrade());
        grade.setTypeface(regular);
        examdate.setText("Exam Held\t\t-\t\t" + model.getExamheld());
        grade.setTypeface(regular);
        resdate.setText("Result Date\t\t-\t\t" + model.getExamresult());
        examdate.setTypeface(regular);
        credit.setText("Credit\t\t-\t\t" + model.getCredit());
        resdate.setTypeface(regular);
        type.setText("Course Type\t\t-\t\t" + model.getSubtype());
        type.setTypeface(regular);
        option.setText(model.getCourseoption());
        option.setTypeface(regular);
        if (model.getCourseoption().trim().equals("NIL")) option.setVisibility(View.GONE);
    }

    private void init(View view) {
        subname = (TextView) view.findViewById(R.id.name_gradeinfo);
        subcode = (TextView) view.findViewById(R.id.code_gradeinfo);
        grade = (TextView) view.findViewById(R.id.grade_gradeinfo);
        type = (TextView) view.findViewById(R.id.ctype_ginfodialog);
        credit = (TextView) view.findViewById(R.id.credit_ginfodialog);
        examdate = (TextView) view.findViewById(R.id.examheld_ginfodialog);
        resdate = (TextView) view.findViewById(R.id.examres_ginfodialog);
        option = (TextView) view.findViewById(R.id.copt_ginfodialog);
    }
}
