package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;

public class TripsFragment extends Fragment {

    private View tripsFragment;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    private SectionsPageAdapter sectionsPageAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        tripsFragment = inflater.inflate(R.layout.activity_home,container,false);

        init(tripsFragment);

        return tripsFragment;
    }

    private void init(View view){
        viewPager = (ViewPager) view.findViewById(R.id.container);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        sectionsPageAdapter = new SectionsPageAdapter(getChildFragmentManager());
        sectionsPageAdapter.addFragment(new NewTripsFragment(), "New");
        sectionsPageAdapter.addFragment(new SavedTripFragment(), "Saved");
        viewPager.setAdapter(sectionsPageAdapter);
    }
}
