package com.example.oreoluwafaseru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;

import com.example.oreoluwafaseru.utils.FileConfig;

import java.io.File;

import android.os.Environment;
import android.util.Log;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        String PATH = FileConfig.dirName;
        String DATA_FILE = FileConfig.fileName;

        FileConfig.verifyStoragePermission(this);

//        File f = new File(this.getExternalFilesDir(null).getAbsolutePath(), PATH);
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), PATH);
        if(!f.exists()){
            if(!f.mkdir()){
                Log.e("Error Mk file", f.getAbsolutePath());
            }else{
                Log.e("Mk file", f.getAbsolutePath());
                startActivity(new Intent(this, FileSelectActivity.class));
                finish();
            }

        }else{
            File dataFile = new File(f, DATA_FILE);
            if(!dataFile.exists()){
                Log.e("not exist data file", dataFile.getAbsolutePath());
                startActivity(new Intent(this, FileSelectActivity.class));
                finish();
            }
            else{
                Log.e("exist data file", dataFile.getAbsolutePath());
                startActivity(new Intent(this, MainActivity.class));
                finish();
            }


        }

    }
}
