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

package com.example.millner.barcodescanningapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    private Button scanBtn;
    private TextView formatTxt, contentTxt;

    public void onClick(View v){
        //respond to clicks
    }

    // On retrieval of our scan result, what we should do.
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //retrieve scan result
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int[] navDrawables = {
                R.drawable.ic_home,
                R.drawable.ic_create_new,
                R.drawable.ic_search,
                R.drawable.ic_main_settings
        };

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
}
