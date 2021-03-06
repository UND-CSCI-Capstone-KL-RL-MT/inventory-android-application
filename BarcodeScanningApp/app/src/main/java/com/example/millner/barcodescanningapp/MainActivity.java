/**
 * Barcode Scanning Application - Test Application
 * ------------------------------------------------------
 * The MainActivity class specifies our main activity. It
 * contains the methods that specify the actions on the
 * creation of the activity, as well as the method which
 * creates an intent on the click of the scan button. In
 * addition, the test application will show the result of
 * the scan on return to the application after the intent
 * is complete.
 * -------------------------------------------------------
 * Last updated: 11/30/2015
 * -------------------------------------------------------
 * Current to-do list:
 * 1. Modify user interface to meet specifications and add
 *    an aspect of user friendliness.
 *
 * 2. Create a navigation activity to allow ease of access
 *    throughout application and provide structure.
 *
 * 3. Utilize the Android SDK HTTP class to create a class
 *    that can be used to connect with our inventory API
 *    and thus utilize data from it.
 *
 * 4. Utilize data retrieved from barcode scan to perform a
 *    lookup in the database.
 *
 * 5. Add a login/logout system so that only authorized
 *    users will have access to the application.
 *
 * 6. Add in error handling to determine what happens if
 *    the item does not exist.
 */

// Adam Millner testing ListView branch commit.

package com.example.millner.barcodescanningapp;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private Button scanBtn;
    private TextView formatTxt, contentTxt;
    ArrayList<String> results;

    public void onClick(View v){
        //respond to clicks
    }

    // On retrieval of our scan result, what we should do.
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
        SwitchCompat batchSwitch = (SwitchCompat) findViewById(R.id.batch_switch);
        Log.v("Batch_Mode", "Batch scanner switch set to: " + batchSwitch.isChecked());
        if (batchSwitch.isChecked()) {
            Log.v("Batch_Mode", "Request code is 0: " + (requestCode == 0) + " (request code = " + requestCode + "), Result code OK: " + (resultCode == RESULT_OK));
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                results.add(contents);

                Log.d("Batch_Mode", "We're in batch mode.");
                // create new intent integrator
                IntentIntegrator scanIntegrator = new IntentIntegrator((Activity) findViewById(R.id.snackbarPosition).getContext());
                // start the scan
                scanIntegrator.initiateScan();
            } else if (resultCode == RESULT_CANCELED) {
                Log.v("Batch_Mode_Result", results.toString());
                Snackbar.make(findViewById(R.id.snackbarPosition), "Batch scan completed.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        } else {
            IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
            if (scanningResult != null) {
                //we have a result
                String scanContent = scanningResult.getContents();
                String scanFormat = scanningResult.getFormatName();
                if (scanContent != null) {
                    viewPager.setCurrentItem(1);
                    EditText barcodeField = (EditText) findViewById(R.id.input_barcode);
                    barcodeField.setText(scanContent);
                } else {
                    Snackbar.make(findViewById(R.id.snackbarPosition), "Scan failed. Please try again.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            } else {
                Snackbar.make(findViewById(R.id.snackbarPosition), "Scan failed. Please try again.", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        results = new ArrayList<String>();

        int[] navDrawables = {
                R.drawable.ic_home,
                R.drawable.ic_create_new,
                R.drawable.ic_search,
                R.drawable.ic_main_settings
        };

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert getSupportActionBar() != null;
        getSupportActionBar().setTitle("Inventory Manager");
        getSupportActionBar().setElevation(0);

        // Get ViewPager and set the PagerAdapter
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(new PrimaryFragmentPagerAdapter(getSupportFragmentManager(), MainActivity.this));

        // Add page change listener to listen for page change
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Must invoke method, unused
            }

            // Update the page title dependent on selected page
            @Override
            public void onPageSelected(int position) {
                switch(position) {
                    case 0:
                        getSupportActionBar().setTitle("Inventory Manager");
                        break;
                    case 1:
                        getSupportActionBar().setTitle("Add Item");
                        break;
                    case 2:
                        getSupportActionBar().setTitle("Search Inventory");
                        break;
                    case 3:
                        getSupportActionBar().setTitle("Settings");
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // Must invoke method, unused
            }
        });

        // Give TabLayout to the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.nav_tabs);
        tabLayout.setupWithViewPager(viewPager);
        for (int i=0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(navDrawables[i]);
        }

        // Bring FAB to front
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.bringToFront();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // use snackbar to let user know the barcode scan request was successful
                Snackbar.make(view, "Opening barcode scanner...", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // create new intent integrator
                IntentIntegrator scanIntegrator = new IntentIntegrator((Activity) view.getContext());
                // start the scan
                scanIntegrator.initiateScan();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

}
