package razor.nikhil.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import razor.nikhil.R;
import razor.nikhil.View.SlidingTabLayout;

/**
 * Created by Nikhil Verma on 9/27/2015.
 */
public class TabMainDet extends Fragment {
    private Context context;

    public TabMainDet() {
    }

    private SlidingTabLayout mSlidingTabLayout;
    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.slidingtab, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager_custom);
        String array[] = new String[4];
        array[0] = "Subject";
        array[1] = "Marks";
        array[2] = "Attendance";
        context = getActivity();
        mViewPager.setAdapter(new MyPagerAdapter(getChildFragmentManager(), array));
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs_custom);
        mSlidingTabLayout.setDistributeEvenly(true);
        mSlidingTabLayout.setViewPager(mViewPager);

    }

    public static TabMainDet newInstance() {
        return new TabMainDet();
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        String[] arr;

        public MyPagerAdapter(FragmentManager fm, String[] a) {
            super(fm);
            this.arr = a;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return arr[position];
        }

        @Override
        public Fragment getItem(int pos) {
            switch (pos) {

                case 0:
                    return SubDetailTab.newInstance();
                case 1:
                    return MarksTab.newInstance();
                case 2:
                    return AttendanceTab.newInstance();
                default:
                    return SubDetailTab.newInstance();
            }
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

}
