package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;

import java.util.ArrayList;

public class SavedTripsFragment extends Fragment {

    private View savedTripsView;
    private TextView errorText;
    private ListView listView;

    private ProgressDialog progressDialog;
    private TripRemoteDAO tripRemoteDAO;
    private TripLocalDAO tripLocalDAO;

    private ArrayList<Trip> tripArrayList;
    private CustomArrayAdapter customArrayAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        savedTripsView = inflater.inflate(R.layout.saved_trips,container,false);

        init(savedTripsView);
        retrieveTripList();

        return savedTripsView;
    }

    private void init(View view){
        errorText = (TextView) view.findViewById(R.id.error_text);
        listView = (ListView) view.findViewById(R.id.savedTripsList);

        progressDialog = new ProgressDialog(this.getContext());

        tripRemoteDAO = new TripRemoteDAO(this.getContext(), this);
        tripLocalDAO = new TripLocalDAO(this.getContext());

        tripArrayList = new ArrayList<Trip>();
        customArrayAdapter = new CustomArrayAdapter(this.getContext(), tripArrayList);
        listView.setAdapter(customArrayAdapter);
    }

    private void toDoListItemClickHandler(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Intent editActivity = new Intent(MainActivity.this, EditActivity.class);
//                editActivity.putExtra("taskId", customArrayAdapter.getTaskItemId(position));
//                startActivityForResult(editActivity, 16);
            }
        });
    }

    private void retrieveTripList(){
        ArrayList<Trip> newTripsList;

        tripRemoteDAO.fetchTripsRequestHandler();
        newTripsList  = tripLocalDAO.getAllTrips();

        if(newTripsList == null || newTripsList.isEmpty()){
            setErrorText(getString(R.string.no_results_error), Color.RED);
        } else{
            setErrorText("", Color.RED);
            tripArrayList.clear();
            tripArrayList.addAll(newTripsList);
        }

        customArrayAdapter.notifyDataSetChanged();
    }

    /**
     * Method to set error text and its color in view
     * @param error
     * @param color
     */
    public void setErrorText(String error, int color){
        errorText.setTextColor(color);
        errorText.setText(error);
    }

    /**
     * Method to show progress dialog box
     * @param message
     */
    public void showProgressDialog(String message){
        if(progressDialog.isShowing())
            progressDialog.setMessage(message);
        else{
            progressDialog.setTitle("Trips");
            progressDialog.setMessage(message);
            progressDialog.show();
        }
    }

    /**
     * Method to dismiss progress dialog
     */
    public void dismissProgressDialog(){
        progressDialog.dismiss();
    }
}
