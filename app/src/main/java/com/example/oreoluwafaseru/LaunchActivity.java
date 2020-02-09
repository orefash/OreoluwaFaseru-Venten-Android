package com.example.oreoluwafaseru;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.oreoluwafaseru.utils.FileConfig;

import java.io.File;
import android.util.Log;

public class LaunchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        String PATH = FileConfig.dirName;

        File dir = getFilesDir();
        File f = new File(dir, PATH);
        if(!f.exists()){
            f.mkdir();
            Log.e("Mk file", dir.getAbsolutePath());
        }else{

            Log.e("exist file", dir.getAbsolutePath());
        }

    }
}
