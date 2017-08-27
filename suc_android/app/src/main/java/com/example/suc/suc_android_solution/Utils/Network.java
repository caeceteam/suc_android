package com.example.suc.suc_android_solution.Utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by efridman on 27/8/17.
 */

public class Network {
    private static final String TAG = Network.class.getSimpleName();

    private static final String STATIC_API_BASE_URL = "http://10.0.2.2:3000";

    private static final String format = "json";

    /**
     * Builds the URL used to talk to the suc server using a apiName and params.
     *
     * @param apiAddress The api that will be queried for.
     * @param params     The params that will be used to queried the api.
     * @return The URL to use to query the suc server.
     */
    public static URL buildUrl(String apiAddress, Map<String, Object> params) {
        Uri builtUri = Uri.parse(STATIC_API_BASE_URL);
        Uri.Builder uriBuilder = builtUri.buildUpon();
        uriBuilder.path(apiAddress);
        if (params != null && !params.isEmpty()) {
            for (Map.Entry parameterEntry : params.entrySet()) {
                uriBuilder.appendQueryParameter(parameterEntry.getKey().toString(), parameterEntry.getValue().toString());
            }
        }


        URL url = null;
        try {
            url = new URL(uriBuilder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
