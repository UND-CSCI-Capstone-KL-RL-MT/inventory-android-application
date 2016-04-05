package com.example.millner.barcodescanningapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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
    private RecyclerView.LayoutManager mLayoutManager;

    public static HomeFragment newInstance(String pageDesc) { // allow for creation of new instance
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
        homeText.setText("Home fragment."); // set text of view

        /*
         * Old listView implementation
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new InventoryListFragment())
                    .commit();
        }
        */

        // RecyclerView
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
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
        List<String> dummyInventory = new ArrayList<String>(Arrays.asList(data));

        mRecyclerView.setHasFixedSize(true);

        mAdapter = new RecyclerViewAdapter(data);
        mRecyclerView.setAdapter(mAdapter);


        return view;
    }

}