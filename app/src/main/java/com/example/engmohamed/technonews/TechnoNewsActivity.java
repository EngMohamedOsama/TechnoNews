package com.example.engmohamed.technonews;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;

public class TechnoNewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<TechnoNews>> {

    // URL for News data from Guardian APIs
    private static final String REQUEST_URL = "http://content.guardianapis.com/search";

    // XML References
    @BindView(R.id.empty_view) TextView emptyText;
    @BindView(R.id.load_bar) ProgressBar loadBar;
    @BindView(R.id.list) ListView newsList;

    // Initialized as global Adaptor to reuse it in onLoadFinished
    private TechnoNewsAdaptor listAdaptor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_techno_news);

        // Initialize Network State Service to check for internet connection
        ConnectivityManager internetService = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

        // Get the network Info
        NetworkInfo internetCheck = internetService.getActiveNetworkInfo();

        // Check for internet connection availability
        if (!(internetCheck != null && internetCheck.isConnectedOrConnecting())) {
            emptyText.setText(R.string.no_internet);
        }

        // Create a new adapter that takes an empty list of News as input
        listAdaptor = new TechnoNewsAdaptor(this, new ArrayList<TechnoNews>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsList.setAdapter(listAdaptor);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected Technology News.
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Find the current item that was clicked on
                TechnoNews currentItem = listAdaptor.getItem(position);

                // Create a new intent to view the Item URL
                Intent itemWebPage = new Intent(Intent.ACTION_VIEW, Uri.parse(currentItem.getUrl()));

                // Send the intent to launch a new activity
                startActivity(itemWebPage);

            }
        });

        // Force to run the AsyncTaskLoader on the background
        getLoaderManager().initLoader(1, null, this).forceLoad();
    }

    @Override
    public Loader<ArrayList<TechnoNews>> onCreateLoader(int id, Bundle args) {
        // Instantiate new preference constructor
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String pageSize = sharedPrefs.getString(getString(R.string.news_number_key), getString(R.string.news_number_default));
        String newsType = sharedPrefs.getString(getString(R.string.news_type_key), getString(R.string.news_type_default_value));

        // Parse the api link to Uri
        Uri baseUri = Uri.parse(REQUEST_URL);

        // Instantiate news uriBuilder Constructor
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query and it's values to api link
        uriBuilder.appendQueryParameter("section", newsType);
        uriBuilder.appendQueryParameter("order-by", "newest");
        uriBuilder.appendQueryParameter("show-fields", "all");
        uriBuilder.appendQueryParameter("page-size", pageSize);
        uriBuilder.appendQueryParameter("api-key", "9369626e-5ce2-4c1c-87c7-7511701d23fc");

        // Return Customized AsyncTaskLoader with API URL
        return new TechnoNewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<TechnoNews>> loader, ArrayList<TechnoNews> data) {
        // Clear previews data
        listAdaptor.clear();

        // Check the availability of data
        if (data != null && !data.isEmpty()) {
            // Add data to the adaptor
            listAdaptor.addAll(data);
        }
        // Set the Empty Text in case it needed
        newsList.setEmptyView(emptyText);

        // Hide the load progress bar
        loadBar.setVisibility(View.GONE);

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<TechnoNews>> loader) {
        // Clear data when the app is reset
        listAdaptor.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu xml
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Get the id of menu item that got clicked
        int id = item.getItemId();

        // Check for the idea
        if (id == R.id.setting) {
            // Start Setting Activity using intent
            Intent settingIntent = new Intent(this, SettingActivity.class);
            startActivity(settingIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
