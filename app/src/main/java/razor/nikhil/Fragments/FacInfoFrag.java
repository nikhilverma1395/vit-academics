package razor.nikhil.Fragments;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import razor.nikhil.R;
import razor.nikhil.model.MyTeacherDet;

/**
 * Created by Nikhil Verma on 9/18/2015.
 */
public class FacInfoFrag extends Fragment {
    private MyTeacherDet myt = null;
    private String[] data;
    private Bitmap image;
    private View head;
    private Typeface roboto_light, regular;
    private ImageView dp;
    private TextView name, desig, room, school, email, addrole, divi, openhrs;
    private List<GetFacDataStudLogin.OpenHours> openhours;

    public FacInfoFrag() {
    }

    public FacInfoFrag(GetFacDataStudLogin.bmparr bmp) {
        this.data = bmp.data;
        this.image = bmp.bmp;
        this.openhours = bmp.open;
    }

    public FacInfoFrag(MyTeacherDet modelmyt) {
        this.myt = modelmyt;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        roboto_light = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
        regular = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        return inflater.inflate(R.layout.sample, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);

        if (myt == null) {
            name.setText(data[0]);
            school.setText(data[1]);
            desig.setText(data[2]);
            if (data[2].trim() == "" || data[2].equals(""))
                desig.setVisibility(View.GONE);
            room.setText(data[3]);
            if (data[3].trim() == "" || data[3].equals(""))
                room.setVisibility(View.GONE);
            email.setText(data[4]);
            if (data[4].trim() == "" || data[4].equals(""))
                email.setVisibility(View.GONE);
            divi.setText(data[5]);
            if (data[5].trim() == "" || data[5].equals(""))
                divi.setVisibility(View.GONE);
            addrole.setText(data[6]);
            if (data[6].trim() == "" || data[6].equals(""))
                addrole.setVisibility(View.GONE);
            if (openhours == null)
                openhrs.setText(data[7]);
            else {
                StringBuilder build = new StringBuilder();
                for (GetFacDataStudLogin.OpenHours op : openhours) {
                    build.append(Html.fromHtml("<b>" + op.day.trim() + "</b>") + "\t\t\t" + op.from.trim() + "\t\t to \t\t" + op.to.trim() + "\t\t\n\n");
                }
                openhrs.setText(build.toString());
            }
            try {
                if (image != null)
                    dp.setImageBitmap(image);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (data[7].trim() == "" || data[7].equals(""))
                openhrs.setVisibility(View.GONE);
        } else {
            name.setText(myt.getNAME());
            school.setText(myt.getSCHOOL());
            desig.setText(myt.getDESIGNATION());
            if (myt.getDESIGNATION() == "" || myt.getDESIGNATION().equals(""))
                desig.setVisibility(View.GONE);
            room.setText(myt.getROOM());
            if (myt.getDESIGNATION() == "" || myt.getDESIGNATION().equals(""))
                room.setVisibility(View.GONE);
            email.setText(myt.getEMAIL());
            if (myt.getEMAIL() == "" || myt.getEMAIL().equals(""))
                email.setVisibility(View.GONE);
            divi.setText(myt.getDIVISION());
            if (myt.getDIVISION() == "" || myt.getDIVISION().equals(""))
                divi.setVisibility(View.GONE);
            addrole.setText(myt.getADDROLE());
            if (myt.getADDROLE() == "" || myt.getADDROLE().equals(""))
                addrole.setVisibility(View.GONE);
            openhrs.setText(myt.getOPENHRS());
            if (myt.getOPENHRS() == "" || myt.getOPENHRS().equals(""))
                openhrs.setVisibility(View.GONE);
            dp.setVisibility(View.GONE);
        }
    }

    private void init(View view) {
        head = (View) view.findViewById(R.id.head_f);
        dp = (ImageView) view.findViewById(R.id.fac_img);
        name = (TextView) view.findViewById(R.id.fac_name_info);
        desig = (TextView) view.findViewById(R.id.fac_desig_info);
        name.setTypeface(regular);
        room = (TextView) view.findViewById(R.id.fac_room_info);
        desig.setTypeface(roboto_light);
        email = (TextView) view.findViewById(R.id.fac_email_info);
        room.setTypeface(regular);
        school = (TextView) view.findViewById(R.id.fac_school_info);
        email.setTypeface(roboto_light);
        openhrs = (TextView) view.findViewById(R.id.fac_openhrs);
        school.setTypeface(regular);
        addrole = (TextView) view.findViewById(R.id.fac_addrole);
        openhrs.setTypeface(regular);
        divi = (TextView) view.findViewById(R.id.fac_division);
        divi.setTypeface(roboto_light);
//        if (image != null) {
//            Palette palette = Palette.generate(image);
//            int vibrant = palette.getVibrantColor(0x000000);
//            int vibrantLight = palette.getLightVibrantColor(0x000000);
//            int vibrantDark = palette.getDarkVibrantColor(0x000000);
//            view.setBackgroundColor(vibrant);
//            toolbar.setBackgroundColor(vibrantLight);
//            int currentapiVersion = android.os.Build.VERSION.SDK_INT;
//             if (currentapiVersion >= android.os.Build.VERSION_CODES.LOLLIPOP) {
//                Window window = getActivity().getWindow();
//                // clear FLAG_TRANSLUCENT_STATUS flag:
//                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//
//                // add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
//                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//                window.setStatusBarColor(vibrantDark);
//                toolbar.setBackgroundColor(vibrantLight);
//            } else {
//                toolbar.setBackgroundColor(vibrantDark);
//            }
//
//        }
    }
}

