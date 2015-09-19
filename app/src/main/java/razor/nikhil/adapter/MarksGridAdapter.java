package razor.nikhil.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import razor.nikhil.R;
import razor.nikhil.model.Marks_Model;

/**
 * Created by Nikhil Verma on 9/19/2015.
 */
public class MarksGridAdapter extends RecyclerView.Adapter<MarksGridAdapter.Holder> {
    private Context context = null;
    private String Colors[] = {"#AB47BC", "#7986CB", "#00BCD4", "#FF8A65", "#66BB6A", "#FFD54F", "#FF8A65", "#78909C", "#EF5350"};
    private Typeface roboto_light, regular;
    private Marks_Model marks_cbl;

    public MarksGridAdapter(Context ct, Marks_Model marks_cbl) {
        this.context = ct;
        this.marks_cbl = marks_cbl;
        roboto_light = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        regular = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
    }


    @Override
    public MarksGridAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.marks_grid_item_cbl, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.total.setBackgroundColor(Color.parseColor(Colors[8 % (position+1)]));
        switch (position) {
            case 0:
                holder.scored.setText(marks_cbl.getQUIZ1());
                holder.total.setText("/ 5");
                holder.marktype.setText("Quiz 1");
                break;
            case 1:
                holder.scored.setText(marks_cbl.getQUIZ2());
                holder.total.setText("/ 5");
                holder.marktype.setText("Quiz 2");
                break;
            case 2:
                holder.scored.setText(marks_cbl.getQUIZ3());
                holder.total.setText("/ 5");
                holder.marktype.setText("Quiz 3");
                break;
            case 3:
                holder.scored.setText(marks_cbl.getCAT1());
                holder.total.setText("/ 50");
                holder.marktype.setText("Cat 1");
                break;
            case 4:
                holder.scored.setText(marks_cbl.getCAT2());
                holder.total.setText("/ 50");
                holder.marktype.setText("Cat 2");
                break;
            case 5:
                holder.scored.setText(marks_cbl.getASIIGN());
                holder.total.setText("/ 5");
                holder.marktype.setText("Assignment 1");
                break;
        }
    }


    @Override
    public int getItemCount() {
        return 6;
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView total, scored, marktype;

        public Holder(View v) {
            super(v);
            total = (TextView) v.findViewById(R.id.tv_total_marks);
            scored = (TextView) v.findViewById(R.id.tv_obt_marks);
            marktype = (TextView) v.findViewById(R.id.tv_mark_type);
            total.setTypeface(regular);
            scored.setTypeface(roboto_light);
            marktype.setTypeface(regular);
        }
    }

}

