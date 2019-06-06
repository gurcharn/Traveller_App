package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile;

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
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips.EditTripFragment;

public class ProfileFragment extends Fragment {

    private View profileView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        profileView = inflater.inflate(R.layout.profile_layout,container,false);

        startViewProfileFragment();

        return profileView;
    }

    /**
     *  Method to start profile view fragment
     */
    private void startViewProfileFragment(){
        ViewProfileFragment viewProfileFragment = new ViewProfileFragment();

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.profile_container, viewProfileFragment);
        fragmentTransaction.addToBackStack(null).addToBackStack(null).commit();
    }
}
