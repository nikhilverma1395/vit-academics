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

import java.util.List;

import razor.nikhil.R;
import razor.nikhil.model.AttendBrief;
import razor.nikhil.model.Model_Slots;

/**
 * Created by Nikhil Verma on 7/26/2015.
 */
public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.CardViewHolder> {
    private List<Model_Slots> cardList;
    private List<AttendBrief> atteds;
    private Context context = null;
    private String Colors[] = {"#AB47BC", "#7986CB", "#00BCD4", "#FF8A65", "#66BB6A", "#FFD54F", "#FF8A65", "#78909C", "#EF5350"};
    private Typeface light, bold, roboto_light, thinno, regular;

    public CoursesAdapter(List<Model_Slots> list, List<AttendBrief> ll, Context context) {
        atteds = ll;
        this.cardList = list;
        this.context = context;
        light = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
        bold = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-Bold.ttf");
        roboto_light = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        thinno = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
        regular = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");

    }


    @Override
    public CoursesAdapter.CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.row_courses, viewGroup, false);
        return (new CardViewHolder(itemView));

    }


    @Override
    public void onBindViewHolder(final CoursesAdapter.CardViewHolder holder, int i) {
        final Model_Slots cl = cardList.get(i);
        try {
            if (!cl.getSubject_name().equals("Lab")) {
                holder.subname.setText(cl.getSubject_name());
            } else {
                if (i > 0)
                    holder.subname.setText(cardList.get(i - 1).getSubject_name() + " (Embedded Lab)");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (!cl.getCode().equals("Lab")) {
                holder.branch_circle.setText(cl.getCode().trim().substring(0, 3));
            } else {
                holder.branch_circle.setText(cardList.get(i - 1).getCode().trim().substring(0, 3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.class_number_slots.setText(cl.getNumber());
        holder.teacher.setText(cl.getTeacher());
        holder.slot.setText(cl.getSlot());
        String att = atteds.get(i).getPercent();
        holder.attend.setText(att);
        try {
            if (att.length() == 4) {
                String at = att.substring(0, 3);
                int per = Integer.parseInt(at);
                colorIt(holder.attend, per);
            }
            if (att.length() == 3) {
                String at = att.substring(0, 2);
                int per = Integer.parseInt(at);
                colorIt(holder.attend, per);
                //FF5C4B
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        holder.cardView.setCardBackgroundColor(Color.parseColor(Colors[9 % (i + 1)]));
    }

    private void colorIt(TextView attend, int per) {
        if (per < 75) {
            attend.setTextColor(Color.parseColor("#FF5C4B"));
        } else if (per >= 75 && per <= 80) {
            attend.setTextColor(Color.parseColor("#73FF7A"));
        } else {
            attend.setTextColor(Color.parseColor("#30FF35"));
        }
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }


    public class CardViewHolder extends RecyclerView.ViewHolder {
        TextView subname, attend, subcode, slot, venue, teacher, class_number_slots, branch_circle;
        CardView cardView;

        public CardViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_view_slot);
            subname = (TextView) v.findViewById(R.id.subject_name);
            branch_circle = (TextView) v.findViewById(R.id.branch_circle);
            // subcode = (TextView) v.findViewById(R.id.sub_code);
            slot = (TextView) v.findViewById(R.id.slot);
            // venue = (TextView) v.findViewById(R.id.venue);
            teacher = (TextView) v.findViewById(R.id.teacher);
            attend = (TextView) v.findViewById(R.id.attendance);
            class_number_slots = (TextView) v.findViewById(R.id.class_number_slots);
            subname.setTypeface(regular);
            teacher.setTypeface(roboto_light);
            attend.setTypeface(regular);
            slot.setTypeface(roboto_light);
        }


    }
}

