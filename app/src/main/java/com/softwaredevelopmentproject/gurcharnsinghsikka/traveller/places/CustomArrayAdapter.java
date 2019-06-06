package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.places;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.R;
import com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips.Trip;

import java.io.InputStream;
import java.util.ArrayList;

public class CustomArrayAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Place> placeArrayList;

    public CustomArrayAdapter(Context context, ArrayList<Place> placeArrayList) {
        this.context = context;
        this.placeArrayList = placeArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            viewHolder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.place_list_item, parent, false);
            viewHolder.iconImage = (ImageView) convertView.findViewById(R.id.iconImage);
            viewHolder.name = (TextView)convertView.findViewById(R.id.name);
            viewHolder.rating = (TextView)convertView.findViewById(R.id.rating);
            viewHolder.address = (TextView)convertView.findViewById(R.id.address);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        new DownloadImageTask(viewHolder.iconImage).execute(placeArrayList.get(position).getIcon());
        viewHolder.name.setText(placeArrayList.get(position).getName());
        viewHolder.rating.setText(placeArrayList.get(position).getRating()+"");
        viewHolder.address.setText(placeArrayList.get(position).getAddress());

        return convertView;
    }

    @Override
    public int getCount(){
        return placeArrayList.size();
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public Object getItem(int position) {
        return placeArrayList.get(position);
    }

    public String getPlaceId(int position){
        return placeArrayList.get(position).getPlaceId();
    }

    static class ViewHolder {
        public ImageView iconImage;
        public TextView name;
        public TextView rating;
        public TextView address;
    }

    /**
     *  Method to load icons of places
     */
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bmp;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}
