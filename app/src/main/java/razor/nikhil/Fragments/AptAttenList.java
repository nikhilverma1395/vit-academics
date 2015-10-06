package razor.nikhil.Fragments;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.Config;
import razor.nikhil.R;
import razor.nikhil.database.SharedPrefs;
import razor.nikhil.model.AptModel;

/**
 * Created by Nikhil Verma on 10/1/2015.
 */
public class AptAttenList extends Fragment {
    private RecyclerView recyclerView;
    private static List<AptModel> list = new ArrayList<>();
    private TextView nomsg_facmsg;

    public static AptAttenList newInstance(List<AptModel> lis) {
        list = lis;
        AptAttenList fragment = new AptAttenList();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recycler_marks_info, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        nomsg_facmsg = (TextView) view.findViewById(R.id.nomsg_facmsg);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_minfo);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        AptAdap adap = new AptAdap();
        if (list.size() != 0)
            recyclerView.setAdapter(adap);
        else {
            recyclerView.setVisibility(View.GONE);
            nomsg_facmsg.setVisibility(View.VISIBLE);
        }

    }

    private class AptAdap extends RecyclerView.Adapter<AptAdap.ViewHolder> {
        private Typeface thin, bold, roboto_light, condensedLightItalic, regular;
        private Context context;
        private int lastPosition = -1;

        public AptAdap() {
            context = getActivity();
            thin = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
            bold = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-Bold.ttf");
            roboto_light = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
            condensedLightItalic = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-LightItalic.ttf");
            regular = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.apt_item, parent, false);
            return (new ViewHolder(itemView));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            if (position > 0) {
                AptModel model = list.get(position - 1);
                int val = Integer.parseInt(model.getUnits());
                if (val > 0) {
                    holder.unit.setText("Present");
                    holder.unit.setTextColor(Color.parseColor("#1AFF3B"));
                } else if (val == 0) {
                    holder.unit.setText("Absent");
                    holder.unit.setTextColor(Color.parseColor("#FF613C"));
                }
                holder.session.setText(model.getSession());
                holder.date.setText(model.getDate());
                holder.date.setTypeface(roboto_light);
                holder.session.setTypeface(regular);
                holder.unit.setTypeface(regular);
            } else if (position == 0) {
                SharedPrefs spref = new SharedPrefs(context);
                holder.date.setText("Attendance\t:\n\t" + spref.getMsg(Config.APT_PERCENT).replaceAll("&nbsp;", ""));
                holder.session.setText("Attended Classes\t:\n\t" + spref.getMsg(Config.APT_ATTENDED));
                holder.unit.setText("Total Classes\t:\n\t" + spref.getMsg(Config.APT_TOTAL_CLASSES));
                holder.date.setTypeface(bold);
                holder.session.setTypeface(bold);
                holder.unit.setTypeface(bold);
                holder.unit.setTextColor(holder.session.getCurrentTextColor());
            }
        }

        @Override
        public int getItemCount() {
            return list.size() + 1;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView date, session, unit;
            private CardView cardView;

            public ViewHolder(View itemView) {
                super(itemView);
                cardView = (CardView) itemView.findViewById(R.id.card_view_daywise_);
                date = (TextView) itemView.findViewById(R.id.date_apt);
                session = (TextView) itemView.findViewById(R.id.session_apt);
                unit = (TextView) itemView.findViewById(R.id.present_apt);
                date.setTypeface(roboto_light);
                session.setTypeface(regular);
                unit.setTypeface(regular);
            }
        }
    }
}


