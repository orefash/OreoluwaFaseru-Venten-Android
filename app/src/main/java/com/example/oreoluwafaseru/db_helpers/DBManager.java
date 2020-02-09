package com.example.oreoluwafaseru.db_helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.oreoluwafaseru.models.CarOwner;
import com.example.oreoluwafaseru.models.DataFilter;

import java.util.ArrayList;

import static com.example.oreoluwafaseru.db_helpers.DBController.BIO;
import static com.example.oreoluwafaseru.db_helpers.DBController.CMAKE;
import static com.example.oreoluwafaseru.db_helpers.DBController.COLOR;
import static com.example.oreoluwafaseru.db_helpers.DBController.COUNTRY;
import static com.example.oreoluwafaseru.db_helpers.DBController.EMAIL;
import static com.example.oreoluwafaseru.db_helpers.DBController.FNAME;
import static com.example.oreoluwafaseru.db_helpers.DBController.GENDER;
import static com.example.oreoluwafaseru.db_helpers.DBController.J_TITLE;
import static com.example.oreoluwafaseru.db_helpers.DBController.TABLE_NAME;
import static com.example.oreoluwafaseru.db_helpers.DBController.YEAR;

public class DBManager {
    private DBController dbController;
    private Context context;
    private SQLiteDatabase database;

    public DBManager(Context c){
        context = c;
    }

    public DBManager open() throws SQLException {
        dbController = new DBController(context);

        database = dbController.getWritableDatabase();
        return this;
    }

    public void close() {
        dbController.close();
    }

    public void insert(CarOwner owner){
        ContentValues contentValues = new ContentValues();

        contentValues.put(FNAME, owner.getFullName());
        contentValues.put(EMAIL, owner.getEmail());
        contentValues.put(BIO, owner.getBio());
        contentValues.put(CMAKE, owner.getCarMake());
        contentValues.put(COLOR, owner.getColor());
        contentValues.put(COUNTRY, owner.getCountry());
        contentValues.put(J_TITLE, owner.getJobTitle());
        contentValues.put(YEAR, owner.getYear());
        contentValues.put(GENDER, owner.getGender());

        database.insert(TABLE_NAME, null, contentValues);
    }

    public ArrayList<CarOwner> getCarOwners() {
        ArrayList<CarOwner> carOwners;
        carOwners = new ArrayList<>();

//        SQLiteDatabase database = this.getWritableDatabase();

        String[] columns = new String[] {
                FNAME, EMAIL, COUNTRY, CMAKE, COLOR, YEAR, GENDER, J_TITLE, BIO
        };

        Cursor cursor = database.query(TABLE_NAME, columns, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                //Id, Company,Name,Price
                CarOwner owner = new CarOwner();
                owner.setFullName(cursor.getString(1));
                owner.setEmail(cursor.getString(2));
                owner.setCountry(cursor.getString(3));
                owner.setCarMake(cursor.getString(4));
                owner.setColor(cursor.getString(5));
                owner.setYear(cursor.getString(6));
                owner.setGender(cursor.getString(7));
                owner.setJobTitle(cursor.getString(8));
                owner.setBio(cursor.getString(9));

                carOwners.add(owner);
            } while (cursor.moveToNext());
        }

        return carOwners;
    }

    private String getLimitString(int page, int limit) {
        String limitStr = "";
        int offset = page * limit;
        limitStr += offset+","+limit;

        return limitStr;
    }

    private boolean checkString( String value){
        boolean check =  false;

        if(value != null){
            if(value.trim().length()>0){
                check=true;
            }
        }

        return check;
    }

    public String getQueryFilter(DataFilter filter){
        String queryFilter = "";
        boolean hasDate = false, hasColor = false, hasGender = false, hasCountry = false;

        if(filter!=null){
            if(checkString(filter.getEndYr()) && checkString(filter.getStartYr())){
                queryFilter += "(";
                queryFilter += YEAR + " >= " + filter.getStartYr() + " AND " + YEAR + " <= " + filter.getEndYr();
                queryFilter += ")";
                hasDate = true;
            }
            if(checkString(filter.getGender())){
                if(hasDate)
                    queryFilter += " AND ";
                queryFilter += GENDER + " = '" +filter.getGender()+ "' ";
                hasGender=true;
            }
            if(filter.getColors().size()>0){
                if(hasDate||hasGender)
                    queryFilter += " AND ";
                queryFilter += COLOR + " IN (";
                for (int i=0; i< filter.getColors().size(); i++){
                    queryFilter += "'"+ filter.getColors().get(i) + "'";
                    if(i==filter.getColors().size()-1){
                        queryFilter += ")";
                    }else{
                        queryFilter += ", ";
                    }
                }
                hasColor = true;

            }
            if(filter.getCountries().size()>0){
                if(hasDate||hasGender||hasColor)
                    queryFilter += " AND ";
                queryFilter += COUNTRY + " IN (";
                for (int i=0; i< filter.getCountries().size(); i++){
                    queryFilter += "'"+ filter.getCountries().get(i) + "'";
                    if(i==filter.getCountries().size()-1){
                        queryFilter += ")";
                    }else{
                        queryFilter += ", ";
                    }
                }
                hasCountry = true;

            }


        }

        return queryFilter;
    }

    public ArrayList<CarOwner> getFilteredCarOwners(int page, int limit, DataFilter filter) {
        ArrayList<CarOwner> carOwners;
        carOwners = new ArrayList<>();

//        SQLiteDatabase database = this.getWritableDatabase();

        String[] columns = new String[] {
                FNAME, EMAIL, COUNTRY, CMAKE, COLOR, YEAR, GENDER, J_TITLE, BIO
        };

        Log.e("QUERY FILTER", getQueryFilter(filter));

        Cursor cursor = database.query(TABLE_NAME, columns, getQueryFilter(filter), null, null, null, null, getLimitString(page, limit));

        if (cursor.moveToFirst()) {
            do {
                //Id, Company,Name,Price
                CarOwner owner = new CarOwner();
                owner.setFullName(cursor.getString(0));
                owner.setEmail(cursor.getString(1));
                owner.setCountry(cursor.getString(2));
                owner.setCarMake(cursor.getString(3));
                owner.setColor(cursor.getString(4));
                owner.setYear(cursor.getString(5));
                owner.setGender(cursor.getString(6));
                owner.setJobTitle(cursor.getString(7));
                owner.setBio(cursor.getString(8));

                carOwners.add(owner);
            } while (cursor.moveToNext());
        }

        return carOwners;
    }

    public void deleteAll(){
        database.delete(TABLE_NAME, null, null);
    }

}
