package com.example.oreoluwafaseru;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.oreoluwafaseru.utils.FileConfig;
import com.example.oreoluwafaseru.utils.OwnerDataManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileSelectActivity extends AppCompatActivity {

    private static final int REQUEST_CODE = 101;
    private static final String OWNER_FILE = "car_ownsers_data.csv";
    private AppCompatActivity activity = FileSelectActivity.this;
    AlphaAnimation inAnimation, outAnimation;
    FrameLayout progressHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_select);

        Button browseBtn = findViewById(R.id.browseBtn);

        progressHolder = findViewById(R.id.progress_overlay);

        FileConfig.verifyStoragePermission(this);

        browseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent fileintent = new Intent(Intent.ACTION_GET_CONTENT);
                fileintent.setType("*/*");
                try {
                    startActivityForResult(fileintent, REQUEST_CODE);
                } catch (ActivityNotFoundException e) {
                    Log.e("file eror" , "No app found for importing the file.");
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null)
            return;
        if (requestCode == REQUEST_CODE) {
            Uri filePath = data.getData();


            Log.e("file select" , filePath.getPath());
            Log.e("file select" , Environment.getExternalStorageDirectory().getAbsolutePath());
            if(FileConfig.fileName.equals(new File(filePath.getPath()).getName())){
                Log.e("file select" , new File(filePath.getPath()).getName());

                if (resultCode == RESULT_OK) {
                    Log.e("file select" , "RES ok");

                    performDataLoad(filePath);
//                    new DataStoreOperation(filePath).execute();
                } else {


                }
            }else{
                Toast.makeText(getApplicationContext(), "You selected the wrong file", Toast.LENGTH_SHORT).show();
                Log.e("wrong file select" , new File(filePath.getPath()).getName());
            }


        }

    }

    private void beforeLoad(){
        inAnimation = new AlphaAnimation(0f, 1f);
        inAnimation.setDuration(200);
        progressHolder.setAnimation(inAnimation);
        progressHolder.setVisibility(View.VISIBLE);
    }
    private void afterLoad(){
        outAnimation = new AlphaAnimation(1f, 0f);
        outAnimation.setDuration(200);
        progressHolder.setAnimation(outAnimation);
        progressHolder.setVisibility(View.GONE);
        Toast.makeText(activity, "DOne Store", Toast.LENGTH_SHORT);


    }
    private void performDataLoad(Uri filePath){

        beforeLoad();

        File dst = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), FileConfig.dirName);
//            File dst = new File(getApplicationContext().getExternalFilesDir(null).getAbsolutePath(), FileConfig.dirName);
        File dstFile = new File(dst, FileConfig.fileName);
        File srcFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filePath.getPath().split(":")[1]);
        if(srcFile.exists()){
            Log.e("file select" , "src exists");
        }

        try{
            FileChannel source = new FileInputStream(srcFile).getChannel();
            FileChannel destination = new FileOutputStream(dstFile).getChannel();
            destination.transferFrom(source, 0, source.size());

            Log.e("In copy", "completed");
        } catch (FileNotFoundException e) {
            Log.e("In copy", e.getMessage());
        } catch (IOException e) {
            Log.e("In copy", e.getMessage());
        }


        boolean notEmpty = OwnerDataManager.csvDataToDB(filePath, activity);

        afterLoad();

        if(notEmpty)
            startActivity(new Intent(FileSelectActivity.this, MainActivity.class));
    }

    private final class DataStoreOperation extends AsyncTask<Void, Void, Boolean> {

        Uri filePath;

        public DataStoreOperation(Uri filePath) {
            this.filePath = filePath;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            inAnimation = new AlphaAnimation(0f, 1f);
            inAnimation.setDuration(200);
            progressHolder.setAnimation(inAnimation);
            progressHolder.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            File dst = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), FileConfig.dirName);
//            File dst = new File(getApplicationContext().getExternalFilesDir(null).getAbsolutePath(), FileConfig.dirName);
            File dstFile = new File(dst, FileConfig.fileName);
            File srcFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), filePath.getPath().split(":")[1]);
            if(srcFile.exists()){
                Log.e("file select" , "src exists");
            }

            try{
                FileChannel source = new FileInputStream(srcFile).getChannel();
                FileChannel destination = new FileOutputStream(dstFile).getChannel();
                destination.transferFrom(source, 0, source.size());

                Log.e("In copy", "completed");
            } catch (FileNotFoundException e) {
                Log.e("In copy", e.getMessage());
            } catch (IOException e) {
                Log.e("In copy", e.getMessage());
            }


            boolean notEmpty = OwnerDataManager.csvDataToDB(filePath, activity);
            return notEmpty;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressHolder.setAnimation(outAnimation);
            progressHolder.setVisibility(View.GONE);
            Toast.makeText(activity, "DOne Store", Toast.LENGTH_SHORT);

            if(result)
                startActivity(new Intent(FileSelectActivity.this, MainActivity.class));
        }
    }

}
