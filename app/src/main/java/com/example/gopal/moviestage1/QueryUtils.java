package com.example.gopal.moviestage1;

/**
 * Created by Gopal on 1/13/2019.
 */


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
import java.util.List;

/**
 * Utility class with methods to help perform the HTTP request and
 * parse the response.
 */
public final class QueryUtils {

    /**
     * Tag for the log messages
     */
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();


    /**
     * Query TMDb database and return a list of  {@link Movie} .
     */
    public static List<Movie> fetchTrainData(String requestUrl) {
        Log.e(LOG_TAG, "Fetching is called: ");
      /*  try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create a list of movie
        List<Movie> trains = extractFeatureFromJson(jsonResponse);

        // Return the movie list
        return trains;
    }

    /**
     * Query TMDb database and return a list of  {@link Movie} .
     */
    public static List<List<String>> fetchMovieDetails(String requestUrl) {
        Log.e(LOG_TAG, "Fetching Movie Details: ");
       /* try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/

        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create a list of movie
        List<List<String>> details = extractMovieDetailsFromJson(jsonResponse);

        // Return the movie list
        return details;
    }


    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
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
                // Toast.makeText(,"Http Networking Problem " + urlConnection.getResponseCode(),Toast.LENGTH_SHORT).show();
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
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
     * Return a list of  {@link Movie}  by parsing out json.
     */
    private static List<Movie> extractFeatureFromJson(String jsonString) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        // Create an empty ArrayList that we use to add movie
        List<Movie> movies = new ArrayList<>();

        try {
            JSONObject root = new JSONObject(jsonString);
            JSONArray resultsArray = root.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++) {

                JSONObject firstObject = resultsArray.getJSONObject(i);
                String movieId = firstObject.optString("id");
                String movieTitle = firstObject.optString("title");
                String movieRating = firstObject.optString("vote_average");
                String movieOverview = firstObject.optString("overview");
                String movieReleaseDate = firstObject.optString("release_date");
                String moviePoster = firstObject.optString("poster_path");
                movies.add(new Movie(movieId, movieTitle, moviePoster, movieOverview, movieRating, movieReleaseDate));
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        return movies;
    }

    /**
     * Return a list of  {@link Movie}  by parsing out json.
     */
    private static List<List<String>> extractMovieDetailsFromJson(String jsonString) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(jsonString)) {
            return null;
        }
        // Create an empty ArrayList that we use to add movie
        List<List<String>> details = new ArrayList<>();

        List<String> trailers = new ArrayList<String>();
        List<String> reviews = new ArrayList<String>();

        try {
            JSONObject root = new JSONObject(jsonString);
            JSONObject videos = root.getJSONObject("videos");
            JSONArray trailersArray = videos.getJSONArray("results");

            for (int i = 0; i < trailersArray.length(); i++) {
                JSONObject trailer = trailersArray.getJSONObject(i);
                String videoType = trailer.optString("type");
                if (videoType.contains("Trailer")) {
                    String key = trailer.optString("key");
                    //  Log.v(LOG_TAG, "Trailer " + i + key);
                    trailers.add(key);
                }
            }
            details.add(0, trailers);
            JSONObject reviewObject = root.getJSONObject("reviews");
            JSONArray reviewsArray = reviewObject.getJSONArray("results");
            for (int i = 0; i < reviewsArray.length(); i++) {
                JSONObject review = reviewsArray.getJSONObject(i);
                String author = review.optString("author");
                String content = review.optString("content");
                //Log.v(LOG_TAG,"REVIEW " + i + content);
                reviews.add(content);
            }
            details.add(1, reviews);


        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }

        return details;
    }
}

