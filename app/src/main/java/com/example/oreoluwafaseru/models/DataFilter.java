package com.example.oreoluwafaseru.models;

import java.io.Serializable;
import java.util.ArrayList;

public class DataFilter implements Serializable {
    String startYr, endYr, gender;
    ArrayList countries, colors;

    public DataFilter() {
    }

    public String getStartYr() {
        return startYr;
    }

    public void setStartYr(String startYr) {
        this.startYr = startYr;
    }

    public String getEndYr() {
        return endYr;
    }

    public void setEndYr(String endYr) {
        this.endYr = endYr;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public ArrayList<String> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList countries) {
        this.countries = countries;
    }

    public ArrayList<String> getColors() {
        return colors;
    }

    public void setColors(ArrayList colors) {
        this.colors = colors;
    }
}
