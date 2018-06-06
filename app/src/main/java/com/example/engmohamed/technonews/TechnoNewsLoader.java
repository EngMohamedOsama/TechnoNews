package com.example.engmohamed.technonews;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Customized AsyncTaskLoader to preform specific operation
 */
public class TechnoNewsLoader extends AsyncTaskLoader<ArrayList<TechnoNews>> {
    // API URL that we will get data from
    private String apiUrl;

    /**
     * Customized constructor the receive API URL
     *
     * @param context for super
     * @param apiUrl  to use it on background
     */
    TechnoNewsLoader(Context context, String apiUrl) {
        super(context);
        this.apiUrl = apiUrl;
    }

    /**
     * Preform the fetch of data on the background
     *
     * @return Arraylist of data or null
     */
    @Override
    public ArrayList<TechnoNews> loadInBackground() {
        try {
            return QueryUtils.fetchTechnoNewsData(apiUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
