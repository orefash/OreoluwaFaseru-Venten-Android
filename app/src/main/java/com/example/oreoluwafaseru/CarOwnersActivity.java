package com.example.oreoluwafaseru;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.oreoluwafaseru.adapters.CarOwnerAdapter;
import com.example.oreoluwafaseru.adapters.FilterAdapter;
import com.example.oreoluwafaseru.db_helpers.DBManager;
import com.example.oreoluwafaseru.models.CarOwner;
import com.example.oreoluwafaseru.models.DataFilter;

import java.util.ArrayList;
import java.util.List;

public class CarOwnersActivity extends AppCompatActivity {

    AppCompatActivity mActivity = CarOwnersActivity.this;
    ProgressBar progressBar;

    private RecyclerView recyclerView;

    List<CarOwner> carOwners;

    CarOwnerAdapter carOwnerAdapter;

    private DBManager dbManager;

    DataFilter dataFilter;

    private boolean loading = true;
    int page = 1;
    final int ITEM_LIMIT = 20;

    private ConstraintLayout messageView;
    private TextView msgHead, msgBody;
    private Button actionBn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_owners);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Car Owners");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new ViewPager.OnClickListener()
                                             {
                                                 @Override
                                                 public void onClick(View v){
                                                     finish();
                                                 }


                                             }
        );


        progressBar = findViewById(R.id.progress_overlay);

        initMsgView();
        initOwnerList();

        try{
            Intent intent = getIntent();
            dataFilter = (DataFilter) intent.getSerializableExtra("filter");

            fetchOwners();
        } catch (Exception ex) {
            Log.e("Fetching owners", ex.getMessage());
        }

    }

    private void initMsgView() {
        messageView = findViewById(R.id.messageView);
        msgHead = findViewById(R.id.headerTV);
        msgBody = findViewById(R.id.messageTxt);
        actionBn = findViewById(R.id.actionBtn);

        msgHead.setText(getString(R.string.no_owner_head));
        msgBody.setText(getString(R.string.no_owner_msg));
        actionBn.setText(getString(R.string.no_owner_btn));

        actionBn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /// button click event
                finish();
            }
        });
    }

    private void initOwnerList() {
        carOwners = new ArrayList<>();
        recyclerView = findViewById(R.id.ownerRecycler);

        carOwnerAdapter = new CarOwnerAdapter(carOwners, mActivity);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(carOwnerAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(!recyclerView.canScrollVertically(1)) {
                    onScrolledToBottom();
                }
            }

            public void onScrolledToBottom(){
                fetchOwners();
            }
        });
    }

    private void fetchOwners() {
        dbManager = new DBManager(mActivity);
        dbManager.open();

        new CarOwnersFetcher().execute();
    }


    private final class CarOwnersFetcher extends AsyncTask<Void, Void, String> {

        List<CarOwner> newOwners;

        public CarOwnersFetcher() {

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... voids) {

            newOwners = dbManager.getFilteredCarOwners(page, ITEM_LIMIT, dataFilter);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if(newOwners.size()>0){
                carOwners.addAll(newOwners);
                page+=1;
            }else{
                if(page==1){
                    recyclerView.setVisibility(View.GONE);
                    messageView.setVisibility(View.VISIBLE);
                }
            }


            if(dbManager!=null)
                dbManager.close();
            carOwnerAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
//            Toast.makeText(activity, "DOne Store", Toast.LENGTH_SHORT);
        }
    }
}
