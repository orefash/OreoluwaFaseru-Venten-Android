package com.example.oreoluwafaseru.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oreoluwafaseru.R;
import com.example.oreoluwafaseru.models.CarOwner;

import java.util.List;

public class CarOwnerAdapter extends RecyclerView.Adapter<CarOwnerAdapter.CarOwnerHolder>  {

    private List<CarOwner> carOwners;
    private Context context;

    private String TAG = "In recycler adapter: ";

    public CarOwnerAdapter(List<CarOwner> carOwners, Context context) {
        this.carOwners = carOwners;
        this.context = context;
    }

    @Override
    public CarOwnerHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflating recycler item view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.car_owner_item
                        , parent, false);

        return new CarOwnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CarOwnerHolder holder, final int position) {


        final CarOwner carOwner = carOwners.get(position);
        holder.fnameTV.setText(carOwner.getFullName());
        holder.genderTV.setText(carOwner.getGender());
        holder.titleTV.setText(carOwner.getJobTitle());
        holder.emailTV.setText(carOwner.getEmail());
        holder.countryTV.setText(carOwner.getCountry());
        holder.cmakeTV.setText(carOwner.getCarMake());
        holder.colorTV.setText(carOwner.getColor());
        holder.yearTV.setText(carOwner.getYear());
        holder.bioTV.setText(carOwner.getBio());



    }

    @Override
    public int getItemCount() {
//            Log.v(MainNewsAdapter.class.getSimpleName(),""+carOwners.size());
        return carOwners.size();
    }

    /**
     * ViewHolder class
     */
    public class CarOwnerHolder extends RecyclerView.ViewHolder {

        public TextView fnameTV, countryTV, colorTV, genderTV, titleTV, emailTV, cmakeTV, yearTV, bioTV;

        public CarOwnerHolder(View view) {
            super(view);
            fnameTV =  view.findViewById(R.id.fnameText);
            countryTV = view.findViewById(R.id.countryTxt);
            colorTV = view.findViewById(R.id.colorTxt);
            genderTV = view.findViewById(R.id.genderTxt);
            titleTV = view.findViewById(R.id.jtitleTxt);
            emailTV = view.findViewById(R.id.emailTxt);
            cmakeTV = view.findViewById(R.id.cmakeTxt);
            yearTV = view.findViewById(R.id.yearTxt);
            bioTV = view.findViewById(R.id.bioTxt);

        }
    }





}

