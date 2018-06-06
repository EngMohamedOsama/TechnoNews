package com.example.engmohamed.technonews;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public final class QueryUtils {

    // Log Tag for exceptions
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * private constructor as QueryUtils should never be called with constructor
     */
    private QueryUtils() {

    }

    /**
     * Return a list of {@link TechnoNews} objects that has been built up from
     * parsing the given JSON response.
     */
    private static ArrayList<TechnoNews> extractFeatureFromJson(String technoNewsJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(technoNewsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding TechnoNews to
        ArrayList<TechnoNews> technoNews = new ArrayList<>();

        // Try to parse the JSON response string. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Create a JSONObject from the JSON response string
            JSONObject jsonData = new JSONObject(technoNewsJSON);

            // Extract the root JSON object with key called "response"
            JSONObject root = jsonData.getJSONObject("response");

            // Extract the root JSON array with key called "results"
            JSONArray newsArray = root.getJSONArray("results");

            // For each NewsPost in the newsArray, create an {@link TechnoNews} object
            for (int i = 0; i < newsArray.length(); i++) {

                // Get a single NewsPost at position i within the list of News
                JSONObject c = newsArray.getJSONObject(i);

                // Extract the value for the key called "webUrl"
                String newsURL = c.getString("webUrl");

                // Extract the value for the key called "webPublicationDate"
                String newsDate = c.getString("webPublicationDate");

                // Extract the value for the key called "sectionName"
                String newsSection = c.getString("sectionName");

                // Extract JSON object with key called "fields"
                JSONObject cFields = c.getJSONObject("fields");

                // Extract the value for the key called "headline"
                String newsTitle = cFields.getString("headline");

                // Sometimes there is no byline key
                // Set Default Author
                String newsAuthor = "Pass notes";

                // Check weather there is a byline key or not
                if (!cFields.isNull("byline")) {
                    newsAuthor = cFields.getString("byline");
                }

                // Extract the value for the key called "thumbnail"
                String newsImageUrl = cFields.getString("thumbnail");
                Drawable newsImage = loadImageFromWebOperations(newsImageUrl);

                // Extract the value for the key called "bodyText"
                String newsDescription = cFields.getString("bodyText");

                // Create a new {@link TechnologyNews} object with the image, title, description, author, date, section, url
                // Add the new {@link TechnologyNews} to the list of technoNews.
                technoNews.add(new TechnoNews(newsImage, newsTitle, newsDescription, newsAuthor, newsDate, newsSection, newsURL));

            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the TechnoNews JSON results", e);

        }

        // Return the list of earthquakes
        return technoNews;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * load image from a given URL
     *
     * @param url image link
     * @return Drawable image resource
     */
    @Nullable
    private static Drawable loadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            return Drawable.createFromStream(is, "src name");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Method the combine all connections & JSON method to return needed data
     *
     * @param url image link
     * @return ArrayList of Data
     * @throws IOException in case of url fail to create
     */
    public static ArrayList<TechnoNews> fetchTechnoNewsData(String url) throws IOException {
        // Create a url object for https connection
        URL urlObject = createUrl(url);

        // Get json response
        String jsonResponse = makeHttpRequest(urlObject);

        // Return ArrayList of all news data needed
        return extractFeatureFromJson(jsonResponse);
    }
}
