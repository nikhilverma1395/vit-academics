package razor.nikhil.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import razor.nikhil.R;
import razor.nikhil.model.SyllabusCourseItem;

/**
 * Created by Nikhil Verma on 11/6/2015.
 */
public class AllSubjectsAdapter extends RecyclerView.Adapter<AllSubjectsAdapter.Sub> {
    private List<SyllabusCourseItem> list = null;

    public List<SyllabusCourseItem> getList() {
        return list;
    }

    public AllSubjectsAdapter(List<SyllabusCourseItem> arrayList) {
        list = arrayList;
    }

    @Override
    public Sub onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tv_list_row_syllabus_courses, parent, false);
        return (new Sub(itemView));
    }

    @Override
    public void onBindViewHolder(Sub holder, int position) {
        holder.sname.setText(list.get(position).getSUBNAME());
    }


    public void animateTo(List<SyllabusCourseItem> models) {
        List<SyllabusCourseItem> rope = models;
        applyAndAnimateRemovals(rope);
        applyAndAnimateAdditions(rope);
        applyAndAnimateMovedItems(rope);
    }

    private void applyAndAnimateRemovals(List<SyllabusCourseItem> newModels) {
        for (int i = list.size() - 1; i >= 0; i--) {
            final SyllabusCourseItem model = list.get(i);
            if (!newModels.contains(model)) {
                removeItem(i);
            }
        }
    }

    private void applyAndAnimateAdditions(List<SyllabusCourseItem> newModels) {
        for (int i = 0, count = newModels.size(); i < count; i++) {
            final SyllabusCourseItem model = newModels.get(i);
            if (!list.contains(model)) {
                addItem(i, model);
            }
        }
    }

    private void applyAndAnimateMovedItems(List<SyllabusCourseItem> newModels) {
        for (int toPosition = newModels.size() - 1; toPosition >= 0; toPosition--) {
            final SyllabusCourseItem model = newModels.get(toPosition);
            final int fromPosition = list.indexOf(model);
            if (fromPosition >= 0 && fromPosition != toPosition) {
                moveItem(fromPosition, toPosition);
            }
        }
    }

    public SyllabusCourseItem removeItem(int position) {
        final SyllabusCourseItem model = list.remove(position);
        notifyItemRemoved(position);
        return model;
    }

    public void addItem(int position, SyllabusCourseItem model) {
        list.add(position, model);
        notifyItemInserted(position);
    }

    public void moveItem(int fromPosition, int toPosition) {
        SyllabusCourseItem model = list.remove(fromPosition);
        list.add(toPosition, model);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class Sub extends RecyclerView.ViewHolder {
        TextView sname;

        public Sub(View itemView) {
            super(itemView);
            sname = (TextView) itemView.findViewById(R.id.tv_cou_row_cgpa);
        }
    }
}

