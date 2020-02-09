package com.example.oreoluwafaseru.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oreoluwafaseru.CarOwnersActivity;
import com.example.oreoluwafaseru.R;
import com.example.oreoluwafaseru.models.DataFilter;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder>  {

    private List<DataFilter> dataFilters;
    private Context context;

    private String TAG = "In recycler adapter: ";

    public FilterAdapter(List<DataFilter> dataFilters, Context context) {
        this.dataFilters = dataFilters;
        this.context = context;
    }

    @Override
    public FilterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.filter_item
                        , parent, false);

        return new FilterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FilterViewHolder holder, int position) {


        final int mPosition = position;
        final DataFilter dataFilter = dataFilters.get(mPosition);
        String dateRange = dataFilter.getStartYr()+ " - " + dataFilter.getEndYr();
        holder.rangeTV.setText(dateRange);
        if(dataFilter.getGender()!=null)
            holder.genderTV.setText(dataFilter.getGender());
        else
            holder.genderTV.setText("ALL");
        if(dataFilter.getCountries().size()>0){
            String countries = "";
            for (int i=0; i<dataFilter.getCountries().size(); i++){
                countries += dataFilter.getCountries().get(i);
                if(i!=dataFilter.getCountries().size()){
                    countries += " | ";
                }
            }
            holder.countryTV.setText(countries);
        }else{
            holder.countryTV.setText("ALL");
        }
        if(dataFilter.getColors().size()>0){
            String colors = "";
            for (int i=0; i<dataFilter.getColors().size(); i++){
                colors += dataFilter.getColors().get(i);
                if(i!=dataFilter.getColors().size()){
                    colors += " | ";
                }
            }
            holder.colorTV.setText(colors);
        }else{
            holder.colorTV.setText("ALL");
        }


        holder.filterView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /// button click event
                Intent intent = new Intent(context, CarOwnersActivity.class)
                        .putExtra("filter", dataFilter);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
//            Log.v(MainNewsAdapter.class.getSimpleName(),""+dataFilters.size());
        return dataFilters.size();
    }

    /**
     * ViewHolder class
     */
    public class FilterViewHolder extends RecyclerView.ViewHolder {

        public TextView rangeTV, countryTV, colorTV, genderTV;
        public ConstraintLayout filterView;

        public FilterViewHolder(View view) {
            super(view);
            rangeTV =  view.findViewById(R.id.dateRangeTxt);
            countryTV = view.findViewById(R.id.countryTxt);
            colorTV = view.findViewById(R.id.colorTxt);
            genderTV = view.findViewById(R.id.genderTxt);
            filterView = view.findViewById(R.id.filterView);

        }
    }





}

