package razor.nikhil.Fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFloat;

import razor.nikhil.Config;
import razor.nikhil.R;
import razor.nikhil.database.SharedPrefs;
import razor.nikhil.database.StoreAndGetImage;

/**
 * Created by Nikhil Verma on 9/17/2015.
 */
public class FacultyAdvFrag extends Fragment {
    public static FacultyAdvFrag newInstance() {
        return new FacultyAdvFrag();
    }

    private ButtonFloat call;
    private Typeface roboto_light, regular;
    private ImageView dp;
    private TextView name, desig, room, school, mobile, email;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        roboto_light = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Light.ttf");
        regular = Typeface.createFromAsset(getActivity().getAssets(), "Roboto-Regular.ttf");
        return inflater.inflate(R.layout.faculty_frag, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        call = (ButtonFloat) view.findViewById(R.id.call_fac_butfloat);
        dp = (ImageView) view.findViewById(R.id.fac_img);
        name = (TextView) view.findViewById(R.id.fac_name);
        desig = (TextView) view.findViewById(R.id.fac_desig);
        room = (TextView) view.findViewById(R.id.fac_room);
        mobile = (TextView) view.findViewById(R.id.fac_mobile);
        email = (TextView) view.findViewById(R.id.fac_email);
        school = (TextView) view.findViewById(R.id.fac_school);
        SharedPrefs pref = new SharedPrefs(getActivity());
        try {
            dp.setImageBitmap(new StoreAndGetImage(getActivity())
                    .loadImageFromStorage(pref.getMsg(Config.FADV_PIcKEY), Config.FACULTY_IMAGE_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
        name.setTypeface(regular);
        school.setTypeface(regular);
        desig.setTypeface(regular);
        email.setTypeface(roboto_light);
        mobile.setTypeface(roboto_light);
        room.setTypeface(roboto_light);
        name.setText(pref.getMsg(Config.FADV_NAME));
        school.setText(pref.getMsg(Config.FADV_SCHOOL));
        desig.setText(pref.getMsg(Config.FADV_DESIGNATION));
        email.setText(pref.getMsg(Config.FADV_EMAIL));
        room.setText(pref.getMsg(Config.FADV_ROOM));
        mobile.setText(pref.getMsg(Config.FADV_MOBILE));
        final Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile.getText().toString().trim()));
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

    }

}
