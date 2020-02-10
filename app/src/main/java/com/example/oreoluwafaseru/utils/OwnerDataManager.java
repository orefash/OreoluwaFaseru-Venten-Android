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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class OwnerDataManager {

    public static boolean csvDataToDB (Uri filePath, Context context) {

        ExecutorService executorService = Executors.newFixedThreadPool(30);

        DBManager dbManager = new DBManager(context);
        boolean notEmpty = false;


        try{
//            FileReader file = new FileReader(filePath);
            InputStream is = context.getContentResolver().openInputStream(filePath);

            BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
            String line = "";

            dbManager.open();
            dbManager.deleteAll();


            List<CarOwner> owners = new ArrayList<>();

            int count = 0;

            while ((line = buffer.readLine()) != null) {

                if(count!=0){
                    String[] str = line.split(",", 11);

//                Log.e("In file read", line+"");
                    CarOwner ownerInfo = new CarOwner();

                    ownerInfo.setBio(str[10]);
                    ownerInfo.setJobTitle(str[9]);
                    ownerInfo.setGender(str[8]);
                    ownerInfo.setColor(str[7]);
                    ownerInfo.setYear(str[6]);
                    ownerInfo.setCarMake(str[5]);
                    ownerInfo.setCountry(str[4]);
                    ownerInfo.setEmail(str[3]);
                    ownerInfo.setFullName(str[1]+" "+str[2]);

                    owners.add(ownerInfo);

                }else{
                    notEmpty = true;
                }
                if (count==6000){
                    count = 0;
                    Runnable worker = new MyRunnable(new ArrayList<CarOwner>(owners), context, dbManager);
                    executorService.execute(worker);
                    owners.clear();
                }
                count++;
            }
            if(count>0 && owners.size()>0){
                Runnable worker = new MyRunnable(owners, context, dbManager);
                executorService.execute(worker);
                owners.clear();
            }

            executorService.shutdown();
            dbManager.close();

            Log.e("After read-write", "All workers done");


        }catch (IOException ex){
            Log.e("file reader io" , ex.toString());
        } catch (Exception ex){

            Log.e("file reader ex" , ex.toString());
        }

        return notEmpty;
    }

    private static void write2DB(List<CarOwner> owners, Context context, DBManager dbManager){
//        DBManager dbManager = new DBManager(context);
//        dbManager.open();

        for (CarOwner owner:
                owners
             ) {
            dbManager.insert(owner);
        }


//        dbManager.close();

    }

    public static class MyRunnable implements Runnable {
        List<CarOwner> owners;
        Context context;
        DBManager dbManager;

        public MyRunnable(List<CarOwner> owners, Context context, DBManager dbManager) {
            this.owners = owners;
            this.context = context;
            this.dbManager = dbManager;
        }

        @Override
        public void run() {
            write2DB(owners, context, dbManager);
        }
    }

}
