package com.example.millner.barcodescanningapp;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * The controller class for the HomeFragment which displays on the
 * homepage of the application.
 */
public class HomeFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    public static HomeFragment newInstance(String pageDesc) { // allows for creation of new instance
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { // required override
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false); // reference fragment_home layout
        TextView homeText = (TextView) view.findViewById(R.id.home_text); // retrieve text view from layout
        // homeText.setText("Home fragment."); // set text of view


        // RecyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.home_fragment_recycler_view);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(llm);

        // Dummy Data
        // Creating dummy data for fragment_home
        String[] data = {
                "Dell Optiplex - M123123 - Streibel 108",
                "Apple iMac - M987987 - Streibel 106",
                "Samsung Galaxy Note 5 - M112233 - Streibel 230",
                "Apple iPhone 10 - M999999 - Streibel 10",
                "Samsung LED Monitor - M555666 - Campus Place 304",
                "Dell Optiplex - M123123 - Streibel 108",
                "Apple iMac - M987987 - Streibel 106",
                "Samsung Galaxy Note 5 - M112233 - Streibel 230",
                "Apple iPhone 10 - M999999 - Streibel 10",
                "Samsung LED Monitor - M555666 - Campus Place 304",
                "Dell Optiplex - M123123 - Streibel 108",
                "Apple iMac - M987987 - Streibel 106",
                "Samsung Galaxy Note 5 - M112233 - Streibel 230",
                "Apple iPhone 10 - M999999 - Streibel 10",
                "Samsung LED Monitor - M555666 - Campus Place 304"
        };
        //List<String> dummyInventory = new ArrayList<String>(Arrays.asList(data));
        FetchInventoryTask inventoryTask = new FetchInventoryTask();
        inventoryTask.execute();

        //mRecyclerView.setHasFixedSize(true);
        //mAdapter = new RecyclerViewAdapter(data);
        //mRecyclerView.setAdapter(mAdapter);

        return view;
    } // End of OnCreateView

    // Used for calling API for inventory items
    // To be displayed in the RecyclerView
    public class FetchInventoryTask extends AsyncTask<String, Void, String[]> {

        private final String LOG_TAG = FetchInventoryTask.class.getSimpleName();

        private String[] getInventoryDataFromJson(String inventoryJsonStr) throws JSONException {

            // These are the names of JSON objects that need to be extracted.
            final String ITEM_ID_INV_API = "item_id";
            final String ITEM_DESCRIPTION_INV_API = "item_description";
            final String ITEM_BUILDING_INV_API = "item_building";
            final String ITEM_LOCATION_INV_API = "item_location";

            JSONArray inventoryArray = new JSONArray(inventoryJsonStr);

            String id;
            String description;
            String building;
            int location;
            String[] resultStrs = new String[inventoryArray.length()];
            //List<String> resultStrs = new ArrayList<String>(Arrays.asList(data));

            for (int i = 0; i < inventoryArray.length(); i++) {


                // Get the JSON object representing the day
                JSONObject inventoryObject = inventoryArray.getJSONObject(i);

                id = inventoryObject.getString(ITEM_ID_INV_API);
                description = inventoryObject.getString(ITEM_DESCRIPTION_INV_API);
                building = inventoryObject.getString(ITEM_BUILDING_INV_API);
                location = inventoryObject.getInt(ITEM_LOCATION_INV_API);

                resultStrs[i] = id + " - " + description + " - " + building + " - " + location;
            }

            for (String s : resultStrs) {
                Log.v(LOG_TAG, "Inventory entry: " + s);
            }
            return resultStrs;
        }

        protected String[] doInBackground(String... params) {
            // Declaring these outside of try catch block so that they may be closed
            // in the finally block
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string
            String inventoryJsonStr = null;

            try {
                // Construct the URL for the inventory-api
                URL url = new URL("http://people.cs.und.edu/~balman/inventory-api/?inventory");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a string
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    // Comment from Udacity Project Sunshine
                    // Since it's JSON, adding a new line isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty. No point in parsing
                    return null;
                }

                inventoryJsonStr = buffer.toString();
                Log.v(LOG_TAG, "Inventory String: " + inventoryJsonStr);

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getInventoryDataFromJson(inventoryJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }
            // This will only happen if there was an error getting or parsing the inventory.
            return null;
        } // End of doInBackground

        @Override
        protected void onPostExecute(String[] result) {
            if (result != null) {
                mAdapter = new RecyclerViewAdapter(result);
                mRecyclerView.setAdapter(mAdapter);
            }
        }

    } // End of FetchInventoryTask
} // End of HomeFragment