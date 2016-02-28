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
 * The controller class for the SearchFragment which displays on the
 * search page of the application.
 */
public class SearchFragment extends Fragment {

    public static SearchFragment newInstance(String pageDesc) { // allow for creation of new instance
        Bundle args = new Bundle();
        SearchFragment fragment = new SearchFragment();
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
        View view = inflater.inflate(R.layout.fragment_search, container, false); // reference fragment_search layout
        TextView searchText = (TextView) view.findViewById(R.id.search_text); // retrieve text view from layout
        searchText.setText("Search fragment."); // set text of view
        return view;
    }

}