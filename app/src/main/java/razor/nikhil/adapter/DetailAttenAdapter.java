package razor.nikhil.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.R;
import razor.nikhil.model.DetailAtten;

/**
 * Created by Nikhil Verma on 9/4/2015.
 */
public class DetailAttenAdapter extends RecyclerView.Adapter<DetailAttenAdapter.ItemsAtt> {
    List<DetailAtten> list = new ArrayList<>();

    public DetailAttenAdapter(List<DetailAtten> lis) {
        this.list = lis;
    }

    @Override
    public ItemsAtt onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_att_item, parent, false);
        return (new ItemsAtt(itemView));
    }

    @Override
    public void onBindViewHolder(ItemsAtt holder, int position) {
        DetailAtten detailAtten = list.get(position);
        holder.sno.setText(detailAtten.getNo());
        holder.day.setText(detailAtten.getDay());
        if (Integer.parseInt(detailAtten.getAttend_unit()) == 0) {
            holder.status.setText("Absent");
            holder.status.setTextColor(Color.RED);
        } else {
            holder.status.setText("Present");
            holder.status.setTextColor(Color.BLUE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ItemsAtt extends RecyclerView.ViewHolder {
        TextView sno, day, status;

        public ItemsAtt(View v) {
            super(v);
            status = (TextView) v.findViewById(R.id.status_);
            day = (TextView) v.findViewById(R.id.day_);
            sno = (TextView) v.findViewById(R.id.sno_);
        }
    }
}
