package com.example.oreoluwafaseru.utils;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.example.oreoluwafaseru.db_helpers.DBManager;
import com.example.oreoluwafaseru.models.CarOwner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class OwnerDataManager {

    public static void csvDataToDB (Uri filePath, Context context) {

        DBManager dbManager = new DBManager(context);


        try{
//            FileReader file = new FileReader(filePath);
            InputStream is = context.getContentResolver().openInputStream(filePath);

            BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
            String line = "";

            dbManager.open();

            dbManager.deleteAll();

            while ((line = buffer.readLine()) != null) {

                String[] str = line.split(",", 11);

//                Log.e("In file read", line+"");
                CarOwner ownerInfo = new CarOwner();

                String fname = str[1]+" "+str[2];
                String email = str[3];
                String country = str[4];
                String cmake = str[5];
                String year = str[6];
                String color = str[7];
                String gender = str[8];
                String title = str[9];
                String bio = str[10];

                ownerInfo.setBio(bio);
                ownerInfo.setJobTitle(title);
                ownerInfo.setGender(gender);
                ownerInfo.setColor(color);
                ownerInfo.setYear(year);
                ownerInfo.setCarMake(cmake);
                ownerInfo.setCountry(country);
                ownerInfo.setEmail(email);
                ownerInfo.setFullName(fname);

                dbManager.insert(ownerInfo);

            }

            dbManager.close();

        }catch (IOException ex){
            Log.e("file reader io" , ex.getLocalizedMessage());
        } catch (Exception ex){

            Log.e("file reader ex" , ex.getLocalizedMessage());
        }


    }

}
