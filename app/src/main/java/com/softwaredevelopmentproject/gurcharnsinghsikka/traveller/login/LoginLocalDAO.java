package com.softwaredevelopmentproject.gurcharnsinghsikka.traveller.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LoginLocalDAO extends SQLiteOpenHelper {

    private static final String TRAVELLER_DATABASE_NAME = "Traveller.db";
    private final String TRAVELLER_TABLE_NAME = "Login";
    private final String LOGIN_COLUMN_ID = "id";
    private final String LOGIN_COLUMN_USERNAME = "username";
    private final String LOGIN_COLUMN_TOKEN = "token";
    private final String LOGIN_COLUMN_DATE = "date";

    private final String LOGIN_CREATE_TABLE = "create table " + TRAVELLER_TABLE_NAME +"(" +
                                                LOGIN_COLUMN_ID + " text primary key, " +
                                                LOGIN_COLUMN_USERNAME + " text," +
                                                LOGIN_COLUMN_TOKEN + " text, " +
                                                LOGIN_COLUMN_DATE + " text" +
                                                ")";
    private final String LOGIN_DROP_TABLE = "drop table " + TRAVELLER_TABLE_NAME;

    private final SQLiteDatabase getWritableDB = this.getWritableDatabase();
    private final SQLiteDatabase getReadableDB = this.getReadableDatabase();
    private ContentValues contentValues = new ContentValues();

    public LoginLocalDAO(Context context){
        super(context, TRAVELLER_DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LOGIN_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(LOGIN_DROP_TABLE);
        db.execSQL(LOGIN_CREATE_TABLE);
    }

    public boolean insertLogin(Login login) {
        try {
            resetDb();
            contentValues.put(LOGIN_COLUMN_ID, login.getId());
            contentValues.put(LOGIN_COLUMN_USERNAME, login.getUsername());
            contentValues.put(LOGIN_COLUMN_TOKEN, login.getToken());
            getWritableDB.insert(TRAVELLER_TABLE_NAME, null, contentValues);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public Login getLogin() {
        Login login = null;
        Cursor cursor = getReadableDB.rawQuery("SELECT * FROM " + TRAVELLER_TABLE_NAME, null);

        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            login = new Login(cursor.getString(0), cursor.getString(1), cursor.getString(2));
        }

        return login;
    }

    public boolean updateLogin(Login login) {
        contentValues.put(LOGIN_COLUMN_ID, login.getId());
        contentValues.put(LOGIN_COLUMN_USERNAME, login.getUsername());
        contentValues.put(LOGIN_COLUMN_TOKEN, login.getToken());
        getWritableDB.update(TRAVELLER_TABLE_NAME, contentValues, "id = ? ", new String[] { login.getId() } );
        return true;
    }

    public boolean deleteLogin(Login login) {
        getWritableDB.delete(TRAVELLER_TABLE_NAME, "id = ? ", new String[] { login.getId() });
        return true;
    }

    public boolean resetDb(){
        onUpgrade(getReadableDB, 0, 0);
        return true;
    }

}
