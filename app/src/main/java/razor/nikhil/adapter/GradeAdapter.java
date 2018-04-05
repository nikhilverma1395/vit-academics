package razor.nikhil.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import razor.nikhil.Config;
import razor.nikhil.R;
import razor.nikhil.database.SharedPrefs;
import razor.nikhil.model.GradeModel;

/**
 * Created by Nikhil Verma on 9/16/2015.
 */
public class GradeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<GradeModel> list;
    private Context context = null;
    private String Colors[] = {"#AB47BC", "#7986CB", "#00BCD4", "#FF8A65", "#66BB6A", "#FFD54F", "#FF8A65", "#78909C", "#EF5350"};
    private Typeface roboto_light, regular;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_CELL = 1;

    public GradeAdapter(List<GradeModel> list, Context context) {
        this.context = context;
        this.list = list;
        roboto_light = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        regular = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return TYPE_HEADER;

        return TYPE_CELL;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType) {
            case TYPE_HEADER: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.grade_header_rv, parent, false);
                return new HeaderHolder(view);
            }
            case TYPE_CELL: {
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.grade_row, parent, false);
                return new CellHolder(view);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderHolder) {
            SharedPrefs pref = new SharedPrefs(context);
            HeaderHolder hold = (HeaderHolder) holder;
            hold.name.setText(pref.getMsg(Config.NAME));
            hold.registered.setText(pref.getMsg(Config.CREDITSREGISTERED));
            hold.earned.setText(pref.getMsg(Config.CREDITSEARNED));
            hold.program.setText(pref.getMsg(Config.BRANCH));
            hold.gpa.setText(pref.getMsg(Config.GPA));
            hold.regno.setText(pref.getMsg(Config.REGISTRATION_NUMBER));
        }
        if (holder instanceof CellHolder) {
            final GradeModel mod = list.get(position-1);
            ((CellHolder) holder).subname.setText(mod.getSubname());
            ((CellHolder) holder).grade.setText(mod.getGrade());
        }
    }


    @Override
    public int getItemCount() {
        return list.size() + 1;
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {
        TextView name, regno, gpa, program, earned, registered;

        public HeaderHolder(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.header_name);
            regno = (TextView) v.findViewById(R.id.header_regno);
            gpa = (TextView) v.findViewById(R.id.header_gpa);
            program = (TextView) v.findViewById(R.id.header_program);
            earned = (TextView) v.findViewById(R.id.header_earned);
            registered = (TextView) v.findViewById(R.id.header_registr);
        }
    }

    public class CellHolder extends RecyclerView.ViewHolder {
        TextView subname, grade;
        CardView cardView;

        public CellHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_view_slot);
            subname = (TextView) v.findViewById(R.id.subname_grade);
            grade = (TextView) v.findViewById(R.id.grade_tv);
            subname.setTypeface(regular);
            grade.setTypeface(roboto_light);
        }
    }
}
