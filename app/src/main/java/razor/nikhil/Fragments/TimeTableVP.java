package razor.nikhil.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.florent37.materialviewpager.MaterialViewPager;
import com.github.florent37.materialviewpager.header.HeaderDesign;

import razor.nikhil.Activity.MainActivity;
import razor.nikhil.R;

/**
 * Created by Nikhil Verma on 9/5/2015.
 */
public class TimeTableVP extends Fragment {
    private MaterialViewPager mViewPager;

    public static TimeTableVP newInstance() {
        return new TimeTableVP();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mvp_layout, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mViewPager = (MaterialViewPager) view.findViewById(R.id.materialViewPager);
        mViewPager.getViewPager().setAdapter(new FragmentStatePagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new TimeTableRV(MainActivity.todayslist_m);
                    case 1:
                        return new TimeTableRV(MainActivity.todayslist_t);
                    case 2:
                        return new TimeTableRV(MainActivity.todayslist_w);
                    case 3:
                        return new TimeTableRV(MainActivity.todayslist_th);
                    case 4:
                        return new TimeTableRV(MainActivity.todayslist_fr);
                    default:
                        return new TimeTableRV(MainActivity.todayslist_m);
                }
            }

            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "Monday";
                    case 1:
                        return "Tuesday";
                    case 2:
                        return "Wednesday";
                    case 3:
                        return "Thursday";
                    case 4:
                        return "Friday";
                }
                return "";
            }
        });
        mViewPager.setMaterialViewPagerListener(new MaterialViewPager.Listener() {
            @Override
            public HeaderDesign getHeaderDesign(int page) {
                switch (page) {
                    case 0:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.green,
                                "https://fs01.androidpit.info/a/63/0e/android-l-wallpapers-630ea6-h900.jpg");
                    case 1:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.blue,
                                "http://cdn1.tnwcdn.com/wp-content/blogs.dir/1/files/2014/06/wallpaper_51.jpg");
                    case 2:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.cyan,
                                "http://www.droid-life.com/wp-content/uploads/2014/10/lollipop-wallpapers10.jpg");
                    case 3:
                        return HeaderDesign.fromColorResAndUrl(
                                R.color.red,
                                "http://www.tothemobile.com/wp-content/uploads/2014/07/original.jpg");
                }

                //execute others actions if needed (ex : modify your header logo)

                return null;
            }
        });


        mViewPager.getViewPager().setOffscreenPageLimit(mViewPager.getViewPager().getAdapter().getCount());
        mViewPager.getPagerTitleStrip().setViewPager(mViewPager.getViewPager());
        mViewPager.getToolbar().setVisibility(View.INVISIBLE);
        mViewPager.getPagerTitleStrip().setTextColor(Color.BLACK);
    }
}