package com.example.millner.barcodescanningapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;


/**
 * The controller class for the HomeFragment which displays on the
 * homepage of the application.
 */
public class HomeFragment extends Fragment {

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
        return view;
    }

}