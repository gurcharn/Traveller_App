package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;

public class SavedTripsFragment extends Fragment {

    private View savedTripsView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        savedTripsView = inflater.inflate(R.layout.saved_trips,container,false);

        return savedTripsView;
    }
}
