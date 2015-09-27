package razor.nikhil.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import razor.nikhil.R;
import razor.nikhil.model.Marks_Model;
import razor.nikhil.model.PBL_Model;

/**
 * Created by Nikhil Verma on 9/19/2015.
 */
public class MarksGridAdapter extends RecyclerView.Adapter<MarksGridAdapter.Holder> {
    private Context context = null;
    private String Colors[] = {"#AB47BC", "#7986CB", "#00BCD4", "#FF8A65", "#66BB6A", "#FFD54F", "#FF8A65", "#78909C", "#EF5350"};
    private Typeface roboto_light, regular;
    private Marks_Model marks_cbl = null;
    private boolean isLab;
    private PBL_Model pmod = null;

    public MarksGridAdapter(Context ct, Marks_Model marks_cbl, boolean isLab) {
        this.isLab = isLab;
        this.context = ct;
        this.marks_cbl = marks_cbl;
        roboto_light = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        regular = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
    }

    public MarksGridAdapter(Context ct, PBL_Model pmod) {
        this.pmod = pmod;
        this.context = ct;
        roboto_light = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        regular = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
    }

    @Override
    public MarksGridAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.marks_grid_item, parent, false);
        return new Holder(view);
    }

    public void AbsentBack(Holder holder, String str) {
        if (str.toLowerCase().trim().equals("absent")) {
            holder.present.setText("A");
            holder.present.setBackgroundColor(Color.parseColor(Colors[3]));
        } else if (str.toLowerCase().trim().equals("present")) {
            holder.present.setText("P");
            holder.present.setBackgroundColor(Color.parseColor(Colors[4]));
        } else {
            holder.present.setVisibility(View.GONE);
        }

    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.total.setBackgroundColor(Color.parseColor(Colors[position]));
        if (marks_cbl != null)
            if (isLab) {
                switch (position) {
                    case 0:
                        AbsentBack(holder, marks_cbl.getQUIZ1_Attended());
                        holder.scored.setText(marks_cbl.getQUIZ1());
                        holder.total.setText("/ 5");
                        holder.marktype.setText("Quiz 1");
                        break;
                    case 1:
                        AbsentBack(holder, marks_cbl.getQUIZ2_Attended());
                        holder.scored.setText(marks_cbl.getQUIZ2());
                        holder.total.setText("/ 5");
                        holder.marktype.setText("Quiz 2");
                        break;
                    case 2:
                        AbsentBack(holder, marks_cbl.getQUIZ3_Attended());
                        holder.scored.setText(marks_cbl.getQUIZ3());
                        holder.total.setText("/ 5");
                        holder.marktype.setText("Quiz 3");
                        break;
                    case 3:
                        AbsentBack(holder, marks_cbl.getCAT1_Attended());
                        holder.scored.setText(marks_cbl.getCAT1());
                        holder.total.setText("/ 50");
                        holder.marktype.setText("Cat 1");
                        break;
                    case 4:
                        AbsentBack(holder, marks_cbl.getCAT2_Attended());
                        holder.scored.setText(marks_cbl.getCAT2());
                        holder.total.setText("/ 50");
                        holder.marktype.setText("Cat 2");
                        break;
                    case 5:
                        AbsentBack(holder, marks_cbl.getASSIGN_Attended());
                        holder.scored.setText(marks_cbl.getASIIGN());
                        holder.total.setText("/ 5");
                        holder.marktype.setText("Assignment");
                        break;
                }
            } else {

                if (position == 0) {
                    AbsentBack(holder, marks_cbl.getLABCAM_Attended());
                    holder.total.setText("/ 50");
                    holder.scored.setText(marks_cbl.getLABCAM());
                    holder.marktype.setText("Lab CAM");
                } else if (position >= 1 && position <= 5) {
                    holder.mark_frame.setVisibility(View.INVISIBLE);
                }
            }
        if (pmod != null) {
            switch (position) {
                case 0:
                    AbsentBack(holder, pmod.getOption1_Attend());
                    holder.scored.setText(pmod.getOption1_Scored());
                    holder.total.setText(pmod.getOption1_Max_Mark());
                    holder.marktype.setText(pmod.getOption1_Title());
                    break;
                case 1:
                    AbsentBack(holder, pmod.getOption2_Attend());
                    holder.scored.setText(pmod.getOption2_Scored());
                    holder.total.setText(pmod.getOption2_Max_Mark());
                    holder.marktype.setText(pmod.getOption2_Title());
                    break;
                case 2:
                    AbsentBack(holder, pmod.getOption3_Attend());
                    holder.scored.setText(pmod.getOption3_Scored());
                    holder.total.setText(pmod.getOption3_Max_Mark());
                    holder.marktype.setText(pmod.getOption3_Title());
                    break;
                case 3:
                    AbsentBack(holder, pmod.getOption4_Attend());
                    holder.scored.setText(pmod.getOption4_Scored());
                    holder.total.setText(pmod.getOption4_Max_Mark());
                    holder.marktype.setText(pmod.getOption4_Title());
                    break;
                case 4:
                    AbsentBack(holder, pmod.getOption5_Attend());
                    holder.scored.setText(pmod.getOption5_Scored());
                    holder.total.setText(pmod.getOption5__Max_Mark());
                    holder.marktype.setText(pmod.getOption5_Title());
                    break;
            }
            if (position == 5) holder.mark_frame.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return 6;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView total, scored, marktype, present;
        CardView mark_frame;

        public Holder(View v) {
            super(v);
            present = (TextView) v.findViewById(R.id.markinfo_present);
            total = (TextView) v.findViewById(R.id.tv_total_marks);
            scored = (TextView) v.findViewById(R.id.tv_obt_marks);
            marktype = (TextView) v.findViewById(R.id.tv_mark_type);
            mark_frame = (CardView) v.findViewById(R.id.card_mark_det);
            total.setTypeface(regular);
            scored.setTypeface(roboto_light);
            marktype.setTypeface(regular);
        }
    }

}

