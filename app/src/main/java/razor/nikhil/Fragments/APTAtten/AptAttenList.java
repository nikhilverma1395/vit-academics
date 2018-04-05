package razor.nikhil.Fragments.APTAtten;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFloat;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import razor.nikhil.Config;
import razor.nikhil.Fragments.CGPA.CgpaFragment;
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

    private class AptAdap extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        double att = 0, total = 0, newatt = 0, countmiss = 0;
        double newattperc = 0;
        private Typeface thin, bold, roboto_light, condensedLightItalic, regular;
        private Context context;
        private static final int TYPE_HEADER = 0;
        private static final int TYPE_CELL = 1;

        @Override
        public int getItemViewType(int position) {
            if (position == 0)
                return TYPE_HEADER;

            return TYPE_CELL;
        }

        public AptAdap() {
            context = getActivity();
            thin = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
            bold = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-Bold.ttf");
            roboto_light = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
            condensedLightItalic = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-LightItalic.ttf");
            regular = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = null;
            switch (viewType) {
                case TYPE_HEADER: {
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.header_apt_atten, parent, false);
                    return new HeaderHolder(view);
                }
                case TYPE_CELL: {
                    view = LayoutInflater.from(parent.getContext())
                            .inflate(R.layout.apt_item, parent, false);
                    return new ViewHolder(view);
                }
            }
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ViewHolder) {
                ViewHolder hold = (ViewHolder) holder;
                AptModel model = list.get(position - 1);
                int val = Integer.parseInt(model.getUnits());
                if (val > 0) {
                    hold.unit.setText("Present");
                    hold.unit.setTextColor(Color.parseColor("#1AFF3B"));
                } else if (val == 0) {
                    hold.unit.setText("Absent");
                    hold.unit.setTextColor(Color.parseColor("#FF613C"));
                }
                hold.session.setText(model.getSession());
                hold.date.setText(model.getDate());
            } else if (holder instanceof HeaderHolder) {

                final HeaderHolder hold = (HeaderHolder) holder;
                hold.result.setText("If you attend\t0\tsessions");
                SharedPrefs spref = new SharedPrefs(context);
                newatt = att = Integer.parseInt(spref.getMsg(Config.APT_ATTENDED));
                total = Integer.parseInt(spref.getMsg(Config.APT_TOTAL_CLASSES));
                hold.curr_atten.setText("Attendance\t:\n\t" + spref.getMsg(Config.APT_PERCENT).replaceAll("&nbsp;", ""));
                hold.attend.setText("Attended Classes\t:\n\t" + spref.getMsg(Config.APT_ATTENDED));
                hold.total.setText("Total Classes\t:\n\t" + spref.getMsg(Config.APT_TOTAL_CLASSES));
                hold.plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!((newatt + 2) > total)) {
                            ++countmiss;
                            newatt += 2;
                        }
                        hold.result_total.setText("attended\t" + newatt + "\tout of\t" + total);
                        if (countmiss == 1)
                            hold.result.setText("If you attend\t" + countmiss + "\tsession");
                        else hold.result.setText("If you attend\t" + countmiss + "\tsessions");
                        Log.d("sdsds", newatt + "\t" + total);
                        newattperc = newatt / total;
                        Log.d("sdsd", "" + (float)newattperc);
                        newattperc *= 100;
                        newattperc = CgpaFragment.round(newattperc, 2, BigDecimal.ROUND_HALF_UP);
                        hold.calc_perc.setText((float)newattperc + "\t%");
                    }
                });
                hold.minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ((countmiss - 1 < 0)) {
                            return;
                        }
                        --countmiss;
                        newatt -= 2;
                        hold.result_total.setText("attended\t" + newatt + "\tout of\t" + total);
                        if (countmiss == 1)
                            hold.result.setText("If you attend\t" + countmiss + "\tsession");
                        else hold.result.setText("If you attend\t" + countmiss + "\tsessions");
                        Log.d("sdsds", newatt + "\t" + total);
                        newattperc = newatt / total;
                        newattperc *= 100;
                        newattperc = CgpaFragment.round(newattperc, 2, BigDecimal.ROUND_HALF_UP);
                        hold.calc_perc.setText((float)newattperc + "\t%");
                        Log.d("sdsd", "" + newattperc);
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return list.size() + 1;
        }

        class HeaderHolder extends RecyclerView.ViewHolder {
            private TextView attend, total, curr_atten, result, result_total, calc_perc;
            private ButtonFloat plus, minus;

            public HeaderHolder(View itemView) {
                super(itemView);
                attend = (TextView) itemView.findViewById(R.id.apt_head_atten);
                total = (TextView) itemView.findViewById(R.id.apt_head_total);
                curr_atten = (TextView) itemView.findViewById(R.id.atten_head_main_perc);
                result = (TextView) itemView.findViewById(R.id.result_tv_apt_head);
                result_total = (TextView) itemView.findViewById(R.id.bw_line_apt_head);
                calc_perc = (TextView) itemView.findViewById(R.id.bw_line_apt_head_percent_calc);
                plus = (ButtonFloat) itemView.findViewById(R.id.plus_float_apt);
                minus = (ButtonFloat) itemView.findViewById(R.id.minus_float_apt);
                attend.setTypeface(regular);
                total.setTypeface(regular);
                curr_atten.setTypeface(regular);
                result.setTypeface(regular);
                result_total.setTypeface(regular);
                calc_perc.setTypeface(regular);
            }
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


