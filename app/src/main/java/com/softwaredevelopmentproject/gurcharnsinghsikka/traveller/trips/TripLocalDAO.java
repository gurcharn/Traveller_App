package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.trips;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.ArrayList;

public class TripLocalDAO extends SQLiteOpenHelper {

    private static final String TRAVELLER_DATABASE_NAME = "Traveller.db";
    private final String TRAVELLER_TABLE_NAME = "Trip";
    private final String TRIP_COLUMN_ID = "tripId";
    private final String TRIP_COLUMN_USERID = "userId";
    private final String TRIP_COLUMN_PLACE = "place";
    private final String TRIP_COLUMN_ARIVAL_DATE = "arrivalDate";
    private final String TRIP_COLUMN_DEPARTURE_DATE = "departureDate";

    private final String TRIP_CREATE_TABLE = "create table " + TRAVELLER_TABLE_NAME +"(" +
                                                                TRIP_COLUMN_ID + " text primary key, " +
                                                                TRIP_COLUMN_USERID + " text, " +
                                                                TRIP_COLUMN_PLACE + " text, " +
                                                                TRIP_COLUMN_ARIVAL_DATE + " text, " +
                                                                TRIP_COLUMN_DEPARTURE_DATE + " text" +
                                                                ")";
    private final String TRIP_DROP_TABLE = "drop table " + TRAVELLER_TABLE_NAME;

    private final SQLiteDatabase getWritableDB = this.getWritableDatabase();
    private final SQLiteDatabase getReadableDB = this.getReadableDatabase();
    private ContentValues contentValues = new ContentValues();

    /**
     * Constructor
     * @param context
     */
    public TripLocalDAO(Context context){
        super(context, TRAVELLER_DATABASE_NAME, null, 1);
        createTableIfNotExist();
    }

    /**
     * Method to create login table on databse creation
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TRIP_CREATE_TABLE);
    }

    /**
     * Method to be called when database upgraded
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(isTableExist()){
            db.execSQL(TRIP_DROP_TABLE);
            db.execSQL(TRIP_CREATE_TABLE);
        }
    }

    public void insertTrip(Trip trip) {
        Trip tripExist = getTrip(trip.getTripId());

        if (tripExist == null) {
            contentValues.put(TRIP_COLUMN_ID, trip.getTripId());
            contentValues.put(TRIP_COLUMN_USERID, trip.getUserId());
            contentValues.put(TRIP_COLUMN_PLACE, trip.getPlace());
            contentValues.put(TRIP_COLUMN_ARIVAL_DATE, trip.getArrival());
            contentValues.put(TRIP_COLUMN_DEPARTURE_DATE, trip.getDeparture());
            try {
                getWritableDB.insert(TRAVELLER_TABLE_NAME, null, contentValues);
            } catch (SQLiteException e){
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            updateTrip(trip);
        }
    }

    public boolean insertTrip(ArrayList<Trip> tripList){
        for(Trip trip : tripList)
            insertTrip(trip);

        return true;
    }

    private void createTableIfNotExist(){
        if(!isTableExist()){
            getWritableDB.execSQL(TRIP_CREATE_TABLE);
        }
    }

    private boolean isTableExist(){
        Cursor cursor = getReadableDB.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+TRAVELLER_TABLE_NAME+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
            cursor.close();
        }
        return false;
    }

    public Trip getTrip(String tripId) {
        Trip trip = null;

        try{
            Cursor cursor = getReadableDB.rawQuery("SELECT * FROM " + TRAVELLER_TABLE_NAME + " WHERE " + TRIP_COLUMN_ID + "=" + tripId , null);

            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                trip = new Trip(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
            }

            cursor.close();
        } catch(SQLException e){
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }

        return trip;
    }

    public ArrayList<Trip> getAllTrips() {
        ArrayList<Trip> tripList = null;
        Cursor cursor = getReadableDB.rawQuery("SELECT * FROM " + TRAVELLER_TABLE_NAME, null);

        if(cursor.getCount() > 0){
            tripList = new ArrayList<Trip>();
            cursor.moveToFirst();
            for(int i=0 ; i<cursor.getCount() ; i++){
                tripList.add(new Trip(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
                cursor.moveToNext();
            }
        }

        cursor.close();
        return tripList;
    }

    public boolean updateTrip(Trip trip) {
        contentValues.put(TRIP_COLUMN_ID, trip.getTripId());
        contentValues.put(TRIP_COLUMN_USERID, trip.getUserId());
        contentValues.put(TRIP_COLUMN_PLACE, trip.getPlace());
        contentValues.put(TRIP_COLUMN_ARIVAL_DATE, trip.getArrival());
        contentValues.put(TRIP_COLUMN_DEPARTURE_DATE, trip.getDeparture());
        getWritableDB.update(TRAVELLER_TABLE_NAME, contentValues, "id = ? ", new String[] { trip.getTripId() } );
        return true;
    }

    public boolean deleteTrip(Trip trip) {
        getWritableDB.delete(TRAVELLER_TABLE_NAME, "id = ? ", new String[] { trip.getTripId() });
        return true;
    }

    /**
     * Method to reset databse
     * @return boolean
     */
    public boolean resetDb(){
        onUpgrade(getReadableDB, 0, 0);
        return true;
    }
}
