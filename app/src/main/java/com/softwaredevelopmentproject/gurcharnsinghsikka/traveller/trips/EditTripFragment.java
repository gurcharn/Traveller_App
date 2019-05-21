package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EditTripFragment extends Fragment {

    private View editTripView;
    private TextView errorText;
    private AutoCompleteTextView placeAutoCompleteText;
    private EditText fromDate;
    private EditText toDate;
    private Button updateButton;
    private Button deleteButton;
    private Button goBackButton;

    private ProgressDialog progressDialog;

    private Geocoder geocoder;
    private ArrayList<String> placesSuggestions;
    private TripRemoteDAO tripRemoteDAO;
    private TripLocalDAO tripLocalDAO;
    private Trip trip;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        editTripView = inflater.inflate(R.layout.edit_trip_layout, container, false);

        init(editTripView);
        fetchArguments();
        placeTextEditHandler();
        fromDateHandler();
        toDateHandler();
        updateTripButtonHandler();
        deleteTripButtonHandler();
        goBackButtonHandler();

        return editTripView;
    }

    private void init(View view){
        errorText = (TextView) view.findViewById(R.id.error_text);
        placeAutoCompleteText = (AutoCompleteTextView) view.findViewById(R.id.place);
        fromDate = (EditText) view.findViewById(R.id.fromDate);
        toDate = (EditText) view.findViewById(R.id.toDate);
        updateButton = (Button) view.findViewById(R.id.updateTrip);
        deleteButton = (Button) view.findViewById(R.id.deleteTrip);
        goBackButton = (Button) view.findViewById(R.id.goBack);

        progressDialog = new ProgressDialog(this.getContext());
        tripRemoteDAO = new TripRemoteDAO(this.getContext(), this);
        tripLocalDAO = new TripLocalDAO(this.getContext());
        trip = null;

        placesSuggestions = new ArrayList<String>();
        geocoder = new Geocoder(this.getContext());
    }

    private void fetchArguments(){
        Bundle bundle = getArguments();
        trip = tripLocalDAO.getTrip(bundle.getString("tripId"));

        placeAutoCompleteText.setText(trip.getPlace());
        fromDate.setText(trip.getArrival());
        toDate.setText(trip.getDeparture());
    }

    private void placeTextEditHandler(){
        placeAutoCompleteText.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() > 0) {
                    placesSuggestions = new ArrayList<String>();
                    System.out.println(s.toString());
                    searchPlace(s.toString());
                }
            }
        });
    }

    private void fromDateHandler(){
        fromDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatePickerDialog(fromDate);
            }
        });
    }

    private void toDateHandler(){
        toDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDatePickerDialog(toDate);
            }
        });
    }

    private void updateTripButtonHandler(){
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNewTripFormFilled()){
                    setErrorText("", Color.RED);

                    tripRemoteDAO.updateTripRequestHandler(trip);
                } else {
                    setErrorText(editTripView.getResources().getString(R.string.new_trip_form_error), Color.RED);
                }
            }
        });
    }

    private void deleteTripButtonHandler(){
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setErrorText("", Color.RED);

                tripRemoteDAO.deleteTripRequestHandler(trip);
            }
        });
    }

    private void goBackButtonHandler(){
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popBackStack();
            }
        });
    }

    public void popBackStack(){
        getFragmentManager().popBackStack();
    }

    private void getDatePickerDialog(final EditText editText){
        final Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int month = calendar.get(Calendar.MONTH);
        final int year = calendar.get(Calendar.YEAR);

        DatePickerDialog.OnDateSetListener dateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String dateText = year + "-" + month + "-" + dayOfMonth;
                editText.setText(dateText.toString());
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(),
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateListener, year, month, day);

        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    private void updatePlaceSuggestion(){
        placeAutoCompleteText.setAdapter(new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_dropdown_item_1line, placesSuggestions));
    }

    private void searchPlace(String place){
        try{
            List<Address> addresses = geocoder.getFromLocationName(place, 5);
            if(!addresses.isEmpty()){
                placesSuggestions = new ArrayList<String>();

                for(Address address : addresses){
                    placesSuggestions.add(address.getFeatureName() +", " + address.getCountryName());
                }

                updatePlaceSuggestion();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private boolean isNewTripFormFilled(){
        boolean formFilled = true;

        if(!isEditTextFilled(toDate))
            formFilled = false;
        if(!isEditTextFilled(fromDate))
            formFilled = false;
        if(!isEditTextFilled(placeAutoCompleteText))
            formFilled = false;

        return formFilled;
    }

    private boolean isEditTextFilled(EditText editText){
        String string = editText.getText().toString().trim();

        if(editText == null){
            editText.setError("Required");
            editText.requestFocus();
            return false;
        } else if(string.isEmpty()){
            editText.setError("Required");
            editText.requestFocus();
            return false;
        } else if(string.length() <= 0){
            editText.setError("Required");
            editText.requestFocus();
            return false;
        } else if(string.equals("")){
            editText.setError("Required");
            editText.requestFocus();
            return false;
        } else{
            editText.clearFocus();
            return true;
        }
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
