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
import razor.nikhil.model.MyTeacherDet;

/**
 * Created by Nik1hil Verma on 9/27/2015.
 */
public class MyTeacherAdap extends RecyclerView.Adapter<MyTeacherAdap.ViewHolder> {
    private final Context context;
    private Typeface regular;
    private final List<MyTeacherDet> lists;

    public MyTeacherAdap(Context context, List<MyTeacherDet> list) {
        this.context = context;
        this.lists = list;
        regular = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
    }

    @Override
    public MyTeacherAdap.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.myteachrow, parent, false);
        return (new ViewHolder(itemView));
    }

    @Override
    public void onBindViewHolder(MyTeacherAdap.ViewHolder holder, int position) {
        try {
            holder.tname.setText(lists.get(position).getNAME());
            holder.myt_sub.setText(lists.get(position).getSCHOOL());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tname, myt_sub;

        public ViewHolder(View v) {
            super(v);
            tname = (TextView) v.findViewById(R.id.myt_name);
            myt_sub = (TextView) v.findViewById(R.id.myt_sub);
            tname.setTypeface(regular);
        }


    }
}
