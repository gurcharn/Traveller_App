package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.profile;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class ProfileLocalDAO extends SQLiteOpenHelper {

    private static final String TRAVELLER_DATABASE_NAME = "Traveller.db";
    private final String TRAVELLER_TABLE_NAME = "Profile";
    private final String PROFILE_COLUMN_ID = "userId";
    private final String PROFILE_COLUMN_FIRSTNAME = "firstName";
    private final String PROFILE_COLUMN_LASTNAME = "lastName";
    private final String PROFILE_COLUMN_AGE = "age";
    private final String PROFILE_COLUMN_GENDER = "gender";
    private final String PROFILE_COLUMN_BIO = "bio";
    private final String PROFILE_COLUMN_EMAIL = "email";
    private final String PROFILE_COLUMN_PHONE = "phone";
    private final String PROFILE_COLUMN_FACEBOOK = "facebook";
    private final String PROFILE_COLUMN_LIKES = "likes";

    private final String PROFILE_CREATE_TABLE = "create table " + TRAVELLER_TABLE_NAME +"(" +
                                                PROFILE_COLUMN_ID + " text primary key, " +
                                                PROFILE_COLUMN_FIRSTNAME + " text, " +
                                                PROFILE_COLUMN_LASTNAME + " text, " +
                                                PROFILE_COLUMN_AGE + " text, " +
                                                PROFILE_COLUMN_GENDER + " text," +
                                                PROFILE_COLUMN_BIO + " text," +
                                                PROFILE_COLUMN_EMAIL + " text," +
                                                PROFILE_COLUMN_PHONE + " text," +
                                                PROFILE_COLUMN_FACEBOOK + " text," +
                                                PROFILE_COLUMN_LIKES + " text" +
                                                ")";
    private final String PROFILE_DROP_TABLE = "drop table " + TRAVELLER_TABLE_NAME;

    private final SQLiteDatabase getWritableDB = this.getWritableDatabase();
    private final SQLiteDatabase getReadableDB = this.getReadableDatabase();
    private ContentValues contentValues = new ContentValues();

    public ProfileLocalDAO(Context context){
        super(context, TRAVELLER_DATABASE_NAME, null, 1);
        createTableIfNotExist();
    }

    /**
     * Method to create login table on databse creation
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(PROFILE_CREATE_TABLE);
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
            db.execSQL(PROFILE_DROP_TABLE);
            db.execSQL(PROFILE_CREATE_TABLE);
        }
    }

    public void insertProfile(Profile profile) {
        createTableIfNotExist();
        Profile profileExist = getProfile(profile.getUserId());

        if (profileExist == null) {
            contentValues.put(PROFILE_COLUMN_ID, profile.getUserId());
            contentValues.put(PROFILE_COLUMN_FIRSTNAME, profile.getFirstName());
            contentValues.put(PROFILE_COLUMN_LASTNAME, profile.getLastName());
            contentValues.put(PROFILE_COLUMN_AGE, profile.getAge());
            contentValues.put(PROFILE_COLUMN_GENDER, profile.getGender());
            contentValues.put(PROFILE_COLUMN_BIO, profile.getBio());
            contentValues.put(PROFILE_COLUMN_EMAIL, profile.getEmail());
            contentValues.put(PROFILE_COLUMN_PHONE, profile.getPhone());
            contentValues.put(PROFILE_COLUMN_FACEBOOK, profile.getFacebook());
            contentValues.put(PROFILE_COLUMN_LIKES, profile.getLikesString());
            try {
                getWritableDB.insert(TRAVELLER_TABLE_NAME, null, contentValues);
            } catch (SQLiteException e){
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            updateProfile(profile);
        }
    }

    public Profile getProfile(String userId) {
        Profile profile = null;
        Cursor cursor = null;

        try{
            cursor = getReadableDB.rawQuery("SELECT * FROM " + TRAVELLER_TABLE_NAME + " WHERE " + PROFILE_COLUMN_ID + "=\'" + userId + "\'" , null);

            if(cursor.getCount() > 0){
                cursor.moveToFirst();
                profile = new Profile(cursor.getString(0),
                                    cursor.getString(1),
                                    cursor.getString(2),
                                    cursor.getString(3),
                                    cursor.getString(4),
                                    cursor.getString(5),
                                    cursor.getString(6),
                                    cursor.getString(7),
                                    cursor.getString(8),
                                    cursor.getString(9));
            }
            cursor.close();
        } catch(SQLException e){
            cursor.close();
            e.printStackTrace();
        } catch (Exception e){
            cursor.close();
            e.printStackTrace();
        }

        return profile;
    }

    public boolean deleteProfile(Profile profile) {
        getWritableDB.delete(TRAVELLER_TABLE_NAME, "id = ? ", new String[] { profile.getUserId() });
        return true;
    }

    public boolean updateProfile(Profile profile) {
        contentValues.put(PROFILE_COLUMN_ID, profile.getUserId());
        contentValues.put(PROFILE_COLUMN_FIRSTNAME, profile.getFirstName());
        contentValues.put(PROFILE_COLUMN_LASTNAME, profile.getLastName());
        contentValues.put(PROFILE_COLUMN_AGE, profile.getAge());
        contentValues.put(PROFILE_COLUMN_GENDER, profile.getGender());
        contentValues.put(PROFILE_COLUMN_BIO, profile.getBio());
        contentValues.put(PROFILE_COLUMN_EMAIL, profile.getEmail());
        contentValues.put(PROFILE_COLUMN_PHONE, profile.getPhone());
        contentValues.put(PROFILE_COLUMN_FACEBOOK, profile.getFacebook());
        contentValues.put(PROFILE_COLUMN_LIKES, profile.getLikesString());
        getWritableDB.update(TRAVELLER_TABLE_NAME, contentValues, PROFILE_COLUMN_ID + " = ? ", new String[] { profile.getUserId() } );
        return true;
    }

    private void createTableIfNotExist(){
        if(!isTableExist()){
            getWritableDB.execSQL(PROFILE_CREATE_TABLE);
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

    public void resetTable(){
        getWritableDB.execSQL("delete from "+TRAVELLER_TABLE_NAME);
    }

}
