package razor.nikhil.Fragments.Leave;

import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFloat;
import com.gc.materialdesign.widgets.SnackBar;

import org.apache.http.client.HttpClient;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.List;

import razor.nikhil.Http.Http;
import razor.nikhil.R;
import razor.nikhil.model.PendLeave;

/**
 * Created by Nikhil Verma on 10/9/2015.
 */
public class LeavePending extends Fragment {
    private RecyclerView recyclerView;
    private static List<PendLeave> lis;

    public static LeavePending newInstance(List<PendLeave> list) {
        LeavePending fragment = new LeavePending();
        lis = list;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.rv_mult_teacher, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView_multi_teach);
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        LeavePend adap = new LeavePend();
        recyclerView.setAdapter(adap);

    }

    private class LeavePend extends RecyclerView.Adapter<LeavePending.LeavePend.ViewHolder> {
        private Typeface thin, bold, roboto_light, condensedLightItalic, regular;

        public LeavePend() {
            thin = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Thin.ttf");
            bold = Typeface.createFromAsset(getActivity().getAssets(), "RobotoCondensed-Bold.ttf");
            roboto_light = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
            condensedLightItalic = Typeface.createFromAsset(getActivity().getAssets(), "RobotoCondensed-LightItalic.ttf");
            regular = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.leave_pend_item, parent, false);
            return (new ViewHolder(itemView));
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.name.setText(lis.get(position).getName());
            holder.status.setText(lis.get(position).getStatus());
            holder.to.setText(lis.get(position).getTo());
            holder.from.setText(lis.get(position).getFrom());
            holder.type.setText(lis.get(position).getType());
            holder.buts.setOnClickListener(new View.OnClickListener() {
                                               @Override
                                               public void onClick(View v) {
                                                   ProgressDialog dialog = ProgressDialog.show(getActivity(), "Cancelling",
                                                           "Wait", true);
                                                   // if (!Logins.isStudentLoggedIn())
                                                   cancelRequest(lis.get(position).getIdL(), dialog);
                                               }
                                           }
            );
        }

        @Override
        public int getItemCount() {
            return lis.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView name, to, from, status, type;
            ButtonFloat buts;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.leave_pend_sname);
                from = (TextView) itemView.findViewById(R.id.leave_pend_from);
                to = (TextView) itemView.findViewById(R.id.leave_pend_to);
                type = (TextView) itemView.findViewById(R.id.leave_pend_type);
                status = (TextView) itemView.findViewById(R.id.leave_pend_status);
                buts = (ButtonFloat) itemView.findViewById(R.id.fac_pend_cancelbutfloat);
                //na
            }
        }
    }

    private void cancelRequest(final String ID, final ProgressDialog dialog) {
        final int trSize = LeaveMainPost.List_tr_Size;
        Log.d("Id", ID);
        // StudentLoggerDialog.newInstance().show(getActivity().getSupportFragmentManager(), "");
        final HttpClient client = LeaveMainPost.getHttpClient();
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                HashMap<String, String> map = new HashMap<>();
                map.put("leave_id", ID);
                map.put("requestcmd", "Cancel");
                String data = "";
                try {
                    data = Http.postMethod("https://academics.vit.ac.in/student/leave_cancel_submit.asp", map, client);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Elements table = null;
                try {
                    table = Jsoup.parse(data).getElementsByTag("table").get(2).getElementsByTag("tr");
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
                table.remove(0);
                Log.d("data", table.size() + "");
                if ((table.size() + 1) == trSize) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            SnackBar bar = new SnackBar(getActivity(), "Leave Cancelled Successfully", null, null);
                            bar.show();
                        }
                    });
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                dialog.dismiss();
                SnackBar bar = new SnackBar(getActivity(), "Leave Cancelled Successfully", null, null);
                bar.show();
            }
        }.execute();
    }
}
