package razor.nikhil.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import fr.ganfra.materialspinner.MaterialSpinner;
import razor.nikhil.R;

/**
 * Created by Nikhil Verma on 9/26/2015.
 */
public class GPA_Adapter extends RecyclerView.Adapter<GPA_Adapter.ViewHolder> {
    private Context ctxt;
    private List<String> names;
    private Typeface regular;
    private HashMap<String, Integer> grades;
    public static HashMap<Integer, Integer> sel;
    private double cursum = 0;
    String[] ITEMS = {"S", "A", "B", "C", "D", "E", "F", "N"};

    //private
    public GPA_Adapter(Context activity, List<String> subnames) {
        this.ctxt = activity;
        this.names = subnames;
        regular = Typeface.createFromAsset(activity.getAssets(), "Roboto-Regular.ttf");
        grades = new HashMap<>();
        sel = new HashMap<>();
        grades.put("s", 10);
        grades.put("a", 9);
        grades.put("b", 8);
        grades.put("c", 7);
        grades.put("d", 6);
        grades.put("e", 5);
        grades.put("f", 0);
        grades.put("n", 0);
    }

    @Override
    public GPA_Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cgpa_calculator_item, parent, false);
        return (new ViewHolder(itemView));
    }

    @Override
    public void onBindViewHolder(GPA_Adapter.ViewHolder holder, final int position) {
        holder.subname.setText(names.get(position));
        holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                sel.put(position, grades.get(ITEMS[i].toLowerCase()));
                Log.d(position + "", grades.get(ITEMS[i].toLowerCase()) + "");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    @Override
    public int getItemCount() {
        return names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView subname;
        MaterialSpinner spinner;
        CardView cardView;

        public ViewHolder(View v) {
            super(v);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(ctxt, R.layout.spinner_row, ITEMS);
            cardView = (CardView) v.findViewById(R.id.card_view_gpa);
            subname = (TextView) v.findViewById(R.id.subname_cgpa_cal);
            spinner = (MaterialSpinner) v.findViewById(R.id.spinner_cgpa);
            spinner.setAdapter(adapter);
            subname.setTypeface(regular);
        }


    }
}
