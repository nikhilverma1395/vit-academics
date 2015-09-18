package razor.nikhil.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import razor.nikhil.R;
import razor.nikhil.model.Model_Daywise;


/**
 * Created by Nikhil Verma on 7/27/2015.
 */

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.CardViewHolderDaywise> {
    private List<Model_Daywise> cardList;
    private Context context = null;
    private Typeface light, bold, roboto_light, regular,thinno;

    public TimeTableAdapter(List<Model_Daywise> list, Context context) {
        this.cardList = list;
        this.context = context;

        light = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
        bold = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-Bold.ttf");
        roboto_light = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
        thinno = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
        regular = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
    }


    @Override
    public CardViewHolderDaywise onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_layout_daywise, viewGroup, false);
        return (new CardViewHolderDaywise(itemView));

    }

    @Override
    public void onBindViewHolder(final TimeTableAdapter.CardViewHolderDaywise cardViewHolder, int i) {
        final Model_Daywise cl = cardList.get(i);
        cardViewHolder.subname.setText(cl.getSubname());
        cardViewHolder.subcode.setText(cl.getSubcode());
        cardViewHolder.slot.setText(cl.getSubslot());
        cardViewHolder.timings.setText(cl.getSubtimings());
        cardViewHolder.venue.setText(cl.getVenue());
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }


    public class CardViewHolderDaywise extends RecyclerView.ViewHolder {
        TextView subname, subcode, slot, venue, timings;

        public CardViewHolderDaywise(View v) {
            super(v);
            subname = (TextView) v.findViewById(R.id.subject_name_daywise);
            subcode = (TextView) v.findViewById(R.id.sub_code_daywise);
            slot = (TextView) v.findViewById(R.id.slot_daywise);
            venue = (TextView) v.findViewById(R.id.venue_daywise);
            timings = (TextView) v.findViewById(R.id.timings_daywise);
            subname.setTypeface(light);
            subcode.setTypeface(light);
            venue.setTypeface(roboto_light);
            slot.setTypeface(bold);
            timings.setTypeface(regular);
        }


    }
}



