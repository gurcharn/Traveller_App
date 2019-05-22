package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;

public class EditProfileFragment extends Fragment {

    private View editProfileView;

    private ProgressDialog progressDialog;

    private ProfileLocalDAO profileLocalDAO;
    private ProfileRemoteDAO profileRemoteDAO;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        editProfileView = inflater.inflate(R.layout.edit_profile_layout,container,false);

        return editProfileView;
    }


}
