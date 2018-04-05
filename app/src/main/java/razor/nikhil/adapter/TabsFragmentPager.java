package razor.nikhil.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import razor.nikhil.Fragments.AttendanceTab;
import razor.nikhil.Fragments.MarksTab;
import razor.nikhil.Fragments.SubDetailTab;

/**
 * Created by Nikhil Verma on 9/21/2015.
 */
public class TabsFragmentPager extends FragmentStatePagerAdapter {

    CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when ViewPagerAdapter is created
    int NumbOfTabs; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public TabsFragmentPager(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            SubDetailTab tab1 = new SubDetailTab();
            return tab1;
        } else if (position == 1) {
            MarksTab tab2 = new MarksTab();
            return tab2;
        } else if (position == 2) {
            AttendanceTab tab2 = new AttendanceTab();
            return tab2;
        }
        return null;

    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
