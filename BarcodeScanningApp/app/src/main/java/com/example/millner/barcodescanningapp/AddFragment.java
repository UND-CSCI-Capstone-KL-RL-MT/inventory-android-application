package com.example.millner.barcodescanningapp;


import android.content.Context;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


/**
 * The controller class for the HomeFragment which displays on the
 * homepage of the application.
 */
public class AddFragment extends Fragment {

    public static AddFragment newInstance(String pageDesc) {
        Bundle args = new Bundle();
        AddFragment fragment = new AddFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        Spinner buildingSpinner = (Spinner) view.findViewById(R.id.building_spinner);
        ArrayAdapter<CharSequence> buildingAdapter = ArrayAdapter.createFromResource(this.getContext(), R.array.building_array, R.layout.spinner_item);
        buildingAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buildingSpinner.setAdapter(buildingAdapter);

        Button clearButton = (Button) view.findViewById(R.id.clear_button);
        clearButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                View rootView = v.getRootView();
                clearAllFields(rootView);
            }
        });

        Button saveButton = (Button) view.findViewById(R.id.save_button);
        saveButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                View rootView = getView().getRootView();
                ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    Snackbar.make(rootView.findViewById(R.id.snackbarPosition), "Item saved successfully.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    clearAllFields(rootView);
                } else {
                    Snackbar.make(rootView.findViewById(R.id.snackbarPosition), "Save failed: No internet connection.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        return view;
    }

    public Boolean clearAllFields(View mainView) {
        EditText itemName = (EditText) mainView.findViewById(R.id.input_name);
        EditText itemBarcode = (EditText) mainView.findViewById(R.id.input_barcode);
        EditText itemRoom = (EditText) mainView.findViewById(R.id.input_room_num);
        itemName.setText("");
        itemBarcode.setText("");
        itemRoom.setText("");
        return true;
    }

}