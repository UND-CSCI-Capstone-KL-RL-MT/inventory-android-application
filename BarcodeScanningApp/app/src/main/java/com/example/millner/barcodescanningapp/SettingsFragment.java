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
 * The controller class for the SettingsFragment which displays on the
 * settings page of the application.
 */
public class SettingsFragment extends Fragment {

    public static SettingsFragment newInstance(String pageDesc) { // allow for creation of new instance
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false); // reference fragment_settings layout
        TextView homeText = (TextView) view.findViewById(R.id.settings_text); // retrieve text view from layout
        homeText.setText("Settings fragment."); // set text of view
        return view;
    }

}