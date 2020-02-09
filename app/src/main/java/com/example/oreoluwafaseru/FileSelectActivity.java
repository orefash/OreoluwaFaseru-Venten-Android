package com.example.oreoluwafaseru;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.oreoluwafaseru.utils.OwnerDataManager;

import java.io.File;

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
            Log.e("file select" , new File(filePath.getPath()).getName());

            if (resultCode == RESULT_OK) {
                Log.e("file select" , "RES ok");
                new DataStoreOperation(filePath).execute();
            } else {

            }
        }

    }

    private final class DataStoreOperation extends AsyncTask<Void, Void, String> {

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
        protected String doInBackground(Void... voids) {
            OwnerDataManager.csvDataToDB(filePath, activity);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            outAnimation = new AlphaAnimation(1f, 0f);
            outAnimation.setDuration(200);
            progressHolder.setAnimation(outAnimation);
            progressHolder.setVisibility(View.GONE);
            Toast.makeText(activity, "DOne Store", Toast.LENGTH_SHORT);
        }
    }

}
