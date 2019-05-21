package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;

public class SavedTripFragment extends Fragment {

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.saved_trips_layout,container,false);;

        startSavedTripsFragment();

        return view;
    }

    private void startSavedTripsFragment(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.savedTrip_container, new SavedTripsListFragment());
        fragmentTransaction.addToBackStack(null).commit();
    }
}
