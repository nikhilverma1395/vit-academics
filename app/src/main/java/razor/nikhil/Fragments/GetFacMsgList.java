package razor.nikhil.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import razor.nikhil.R;
import razor.nikhil.database.FacMsgGS;
import razor.nikhil.model.FacMsgModel;

/**
 * Created by Nikhil Verma on 10/1/2015.
 */
public class GetFacMsgList extends Fragment {
    private RecyclerView recyclerView;
    private static List<FacMsgModel> list = new ArrayList<>();
    private TextView nomsg_facmsg;

    public static GetFacMsgList newInstance(List<FacMsgModel> lis) {
        list = lis;
        GetFacMsgList fragment = new GetFacMsgList();
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
        FacMsgAdap adap = new FacMsgAdap();
        if (list.size() != 0)
            recyclerView.setAdapter(adap);
        else {
            recyclerView.setVisibility(View.GONE);
            nomsg_facmsg.setVisibility(View.VISIBLE);
        }

    }

    private class FacMsgAdap extends RecyclerView.Adapter<FacMsgAdap.ViewHolder> {
        private Typeface thin, bold, roboto_light, condensedLightItalic, regular;
        private Context context;

        public FacMsgAdap() {
            context = getActivity();
            thin = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
            bold = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-Bold.ttf");
            roboto_light = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
            condensedLightItalic = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-LightItalic.ttf");
            regular = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fac_msgs_item_rv, parent, false);
            return (new ViewHolder(itemView));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            FacMsgModel model = list.get(position);
            holder.msg.setText(model.getMsg());
            holder.subname.setText(model.getSubject());
            holder.teacher.setText(model.getFacname());
            holder.time.setText(model.getSentTime());
            holder.facmsg_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle("Delete Message")
                            .setMessage("Are you sure you want to delete this message?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    delete(position);
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(R.drawable.delete)
                            .show();

                }
            });
        }

        private void delete(int position) {
            FacMsgGS gs = new FacMsgGS(getActivity());
            try {
                boolean beep = gs.deleteRowById((int) list.get(position).getId());
                list.remove(position);
                notifyDataSetChanged();
                if (list.size() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    nomsg_facmsg.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            private TextView subname, teacher, msg, time;
            private ImageButton facmsg_delete;

            public ViewHolder(View itemView) {
                super(itemView);
                facmsg_delete = (ImageButton) itemView.findViewById(R.id.fac_msg_delete);
                subname = (TextView) itemView.findViewById(R.id.facmsg_subname);
                teacher = (TextView) itemView.findViewById(R.id.facmsg_tname);
                msg = (TextView) itemView.findViewById(R.id.facmsg_msg);
                time = (TextView) itemView.findViewById(R.id.facmsg_time);
                subname.setTypeface(bold);
                teacher.setTypeface(roboto_light);
                msg.setTypeface(regular);
                time.setTypeface(condensedLightItalic);
            }
        }
    }
}

