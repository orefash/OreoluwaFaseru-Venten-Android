package com.example.oreoluwafaseru;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.oreoluwafaseru.adapters.FilterAdapter;
import com.example.oreoluwafaseru.models.DataFilter;
import com.example.oreoluwafaseru.utils.AppRequestController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppCompatActivity mActivity = MainActivity.this;

    private static String TAG = MainActivity.class.getName();

    private static String FILTER_URL = "https://ven10.co/assessment/filter.json";

    private RecyclerView recyclerView;

    List<DataFilter> dataFilters;

    private ProgressDialog pDialog;

    FilterAdapter filterAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Filters");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        toolbar.setNavigationOnClickListener(new ViewPager.OnClickListener()
//                                             {
//                                                 @Override
//                                                 public void onClick(View v){
//                                                     finish();
//                                                 }
//
//
//                                             }
//        );

        recyclerView = findViewById(R.id.filterRecycler);

        dataFilters = new ArrayList<>();

        filterAdapter = new FilterAdapter(dataFilters, mActivity);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this );
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(filterAdapter);

        fetchFilters();
    }

    private void loadFilters(){

    }

    private void parseResponse(JSONArray response){
//        List<DataFilter> filters = new ArrayList<>();

        try {
            for(int i = 0; i < response.length(); i++) {

                JSONObject obj = response.getJSONObject(i);

                DataFilter filter = new DataFilter();

                filter.setStartYr(obj.getString("start_year"));
                filter.setEndYr(obj.getString("end_year"));
                if(obj.getString("gender").length()==0){
                    filter.setGender(null);
                } else {
                    filter.setGender(obj.getString("gender"));
                }

                ArrayList<String> countries = new ArrayList<>();
                JSONArray countryArr = obj.getJSONArray("countries");
                if(countryArr.length()>0){
                    for(int j=0; j < countryArr.length(); j++) {
                        countries.add(countryArr.getString(j));
                    }
                }

                ArrayList<String> colors = new ArrayList<>();
                JSONArray colorArr = obj.getJSONArray("colors");
                if(colorArr.length()>0){
                    for(int j=0; j < colorArr.length(); j++) {
                        colors.add(colorArr.getString(j));
                    }
                }

                filter.setColors(colors);
                filter.setCountries(countries);

                dataFilters.add(filter);

            }
        } catch (JSONException e) {
            Log.e("Parse Error", "Error: " + e.getMessage());
        }

    }

    private void fetchFilters() {

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading Filters..");
        pDialog.show();

        JsonArrayRequest request = new JsonArrayRequest
                (Request.Method.GET, FILTER_URL, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Resp", "Response: " + response.toString());
//                        dataFilters = parseResponse(response);
                        parseResponse(response);

                        filterAdapter.notifyDataSetChanged();
                        hidePDialog();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error

                        Log.e("Error", "Error: " + error.toString());
                        hidePDialog();
                    }
                });

        AppRequestController.getInstance(this).addToRequestQueue(request);

    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }




}
