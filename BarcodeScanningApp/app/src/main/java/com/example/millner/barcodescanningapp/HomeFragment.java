package com.example.millner.barcodescanningapp;


import android.content.Context;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    private List<InventoryItem> inventoryItemList = new ArrayList<>();

    public static HomeFragment newInstance(String pageDesc) { // allows for creation of new instance
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private HomeFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final HomeFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) { // required override
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false); // reference fragment_home layout

        // RecyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.home_fragment_recycler_view);
        mAdapter = new RecyclerViewAdapter(inventoryItemList);
        RecyclerView.LayoutManager mLayoutmanager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutmanager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(this.getContext(),LinearLayoutManager.VERTICAL));

        // Click Listener
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity().getApplicationContext(), mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                InventoryItem inventoryItem = inventoryItemList.get(position);
                Toast.makeText(getActivity().getApplicationContext(), inventoryItem.getDescription() + " is selected!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));


        // Clear list initially
        inventoryItemList.clear();

        // Populate the RecyclerView with items
        FetchInventoryTask inventoryTask = new FetchInventoryTask();
        inventoryTask.execute();

        return view;
    } // End of OnCreateView

    // Used for calling API for inventory items
    // To be displayed in the RecyclerView
    public class FetchInventoryTask extends AsyncTask<String, Void, List<InventoryItem>> {

        private final String LOG_TAG = FetchInventoryTask.class.getSimpleName();

        private List<InventoryItem> getInventoryDataFromJson(String inventoryJsonStr) throws JSONException {

            // These are the names of JSON objects that need to be extracted.
            final String ITEM_ID_INV_API = "item_id";
            final String ITEM_DESCRIPTION_INV_API = "item_description";
            final String ITEM_BUILDING_INV_API = "item_building";
            final String ITEM_LOCATION_INV_API = "item_location";

            JSONArray inventoryArray = new JSONArray(inventoryJsonStr);

            for (int i = 0; i < inventoryArray.length(); i++) {
                // Get the JSON object representing the day
                JSONObject inventoryObject = inventoryArray.getJSONObject(i);
                InventoryItem inventoryItem = new InventoryItem();


                inventoryItem.setDescription(inventoryObject.getString(ITEM_DESCRIPTION_INV_API));
                inventoryItem.setBuilding(inventoryObject.getString(ITEM_BUILDING_INV_API));
                inventoryItem.setRoomNumber(inventoryObject.getInt(ITEM_LOCATION_INV_API));
                inventoryItem.setTag(inventoryObject.getString(ITEM_ID_INV_API));

                inventoryItemList.add(inventoryItem);

            }

            return inventoryItemList;
        }

        protected List<InventoryItem> doInBackground(String... params) {
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
        protected void onPostExecute(List<InventoryItem> inventoryItemList) {
            if (inventoryItemList != null) {
                mAdapter = new RecyclerViewAdapter(inventoryItemList);
                mRecyclerView.setAdapter(mAdapter);
            }
        }

    } // End of FetchInventoryTask
} // End of HomeFragment