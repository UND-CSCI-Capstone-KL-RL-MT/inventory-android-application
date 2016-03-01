package com.example.millner.barcodescanningapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
 * Fragment for creating the listvew
 */
public class InventoryListFragment extends Fragment {

    private ArrayAdapter<String> mInventoryListAdapter;


    // Empty constructor
    public InventoryListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){

        // Creating dummy data for fragment_home
        String[] data = {
                "Dell Optiplex - M123123 - Streibel 108",
                "Apple iMac - M987987 - Streibel 106",
                "Samsung Galaxy Note 5 - M112233 - Streibel 230",
                "Apple iPhone 10 - M999999 - Streibel 10",
                "Samsung LED Monitor - M555666 - Campus Place 304"
        };
        List<String> dummyInventory = new ArrayList<String>(Arrays.asList(data));

        mInventoryListAdapter =
                new ArrayAdapter<String>(
                        getActivity(),
                        R.layout.list_item_inventory,
                        R.id.list_item_inventory_textview,
                        dummyInventory);

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        ListView listView = (ListView) rootView.findViewById(R.id.listview_inventory);
        listView.setAdapter(mInventoryListAdapter);

        return rootView;
    }
}
