package com.example.millner.barcodescanningapp;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;


/**
 * A simple {@link Fragment} subclass.
 */
public class Main extends Fragment implements OnClickListener {

    private TextView formatTxt, contentTxt;

    public Main() {
        // Required empty public constructor
    }

    public void onClick(View view) {
        // respond to clicks
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        formatTxt = (TextView) rootView.findViewById(R.id.scan_format);
        contentTxt = (TextView) rootView.findViewById(R.id.scan_content);
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // use snackbar to let user know the barcode scan request was successful
                Snackbar.make(view, "Opening barcode scanner...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // create new intent integrator
                IntentIntegrator scanIntegrator = new IntentIntegrator((Activity)view.getContext());
                // start the scan
                scanIntegrator.initiateScan();
            }
        });

        return rootView;
    }

}
