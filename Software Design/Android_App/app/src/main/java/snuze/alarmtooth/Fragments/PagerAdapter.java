package snuze.alarmtooth.Fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


public class PagerAdapter extends FragmentStatePagerAdapter {


    int mNumOfTabs;


    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                Fragment alarmFragment =  AlarmFragment.newInstance();
                return alarmFragment;

            case 1:
                Fragment colorFragment = ColorFragment.newInstance("Color Fragment");
                return  colorFragment;

            case 2:
                Fragment soundFragment = SoundFragment.newInstance("Sound Fragment");
                return soundFragment;
            case 3:
                Fragment settingsFragment = SettingsFragment.newInstance("Settings Fragment");
                return settingsFragment;
            case 4:
                Fragment logFragment = LogFragment.newInstance(1);
                return logFragment;
            default:
                return null;
        }

    }

    public void updateLog(String log){


    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
