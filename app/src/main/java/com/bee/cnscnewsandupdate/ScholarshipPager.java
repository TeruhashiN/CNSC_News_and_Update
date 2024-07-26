package com.bee.cnscnewsandupdate;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by Chirag on 30-Jul-17.
 */

public class ScholarshipPager extends FragmentStatePagerAdapter {

    private RequirementsFragment requirementsFragment;
    public ScholarshipPager(FragmentManager fm, RequirementsFragment requirementsFragment){
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.requirementsFragment = requirementsFragment;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AcademicFragment();
            case 1:
                return new IncomeFragment();
            default:
                return requirementsFragment;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
