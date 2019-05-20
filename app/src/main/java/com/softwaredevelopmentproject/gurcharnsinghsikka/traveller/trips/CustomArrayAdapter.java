package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;

import java.util.ArrayList;

public class CustomArrayAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Trip> tripArrayList;

    public CustomArrayAdapter(Context context, ArrayList<Trip> tripArrayList) {
        this.context = context;
        this.tripArrayList = tripArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.triplist_item_layout, parent, false);
            viewHolder.place = (TextView)convertView.findViewById(R.id.place);
            viewHolder.arrivalDate = (TextView)convertView.findViewById(R.id.arrival_date);
            viewHolder.departureDate = (TextView)convertView.findViewById(R.id.departure_date);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.place.setText(tripArrayList.get(position).getPlace());
        viewHolder.arrivalDate.setText(tripArrayList.get(position).getArrival());
        viewHolder.departureDate.setText(tripArrayList.get(position).getDeparture());

        return convertView;
    }

    @Override
    public int getCount(){
        return tripArrayList.size();
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position) {
        return tripArrayList.get(position);
    }

    public String getTripId(int position){
        return tripArrayList.get(position).getTripId();
    }

    static class ViewHolder {
        public TextView place;
        public TextView arrivalDate;
        public TextView departureDate;
    }
}
