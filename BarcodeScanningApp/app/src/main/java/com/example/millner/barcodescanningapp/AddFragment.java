package com.example.millner.barcodescanningapp;


import android.content.Context;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


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
                View rootView = v.getRootView();
                EditText itemName = (EditText) v.getRootView().findViewById(R.id.input_name);
                EditText itemBarcode = (EditText) v.getRootView().findViewById(R.id.input_barcode);
                EditText itemRoom = (EditText) v.getRootView().findViewById(R.id.input_room_num);
                Spinner itemBuilding = (Spinner) v.getRootView().findViewById(R.id.building_spinner);
                ArrayList<String> params = new ArrayList<String>();
                params.add(itemBarcode.getText().toString());
                params.add(itemName.getText().toString());
                params.add(itemBuilding.getSelectedItem().toString());
                params.add(itemRoom.getText().toString());
                ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                if (networkInfo != null && networkInfo.isConnected()) {
                    new SaveItemTask().execute(params);
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

    private class SaveItemTask extends AsyncTask<ArrayList<String>, Void, String> {

        @Override
        protected String doInBackground(ArrayList<String>... params) {

            StringBuilder sb = new StringBuilder();

            try {
                URL url = new URL("http://people.cs.und.edu/~balman/inventory-api/");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.connect();

                JSONObject jsonParams = new JSONObject();
                jsonParams.put("itemID", params[0].get(0));
                jsonParams.put("itemDescription", params[0].get(1));
                jsonParams.put("itemBuilding", params[0].get(2));
                jsonParams.put("itemLocation", params[0].get(3));
                jsonParams.put("type", "add_item");

                Log.v("InventoryManager", jsonParams.toString());

                OutputStream os = conn.getOutputStream();
                os.write(jsonParams.toString().getBytes("UTF-8"));
                os.close();

                int HttpResult = conn.getResponseCode();
                if(HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        String nl = line + "\n";
                        sb.append(nl);
                    }
                    br.close();
                } else {
                    Log.e("InventoryManager", conn.getResponseMessage());
                }

                conn.disconnect();

                return "Success";
            } catch (JSONException e) {
                return "Failure";
            } catch (IOException e) {
                return "Failure";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            Snackbar.make(getActivity().findViewById(R.id.snackbarPosition), "Item saved successfully.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }

}