package com.samim.bbcnewsdemoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {
    ListView newsListview;
    Context context = MainActivity.this;
    CheckBox bbcCheckbox;
    CheckBox otherCheckbox;
    Spinner countrySpinner;
    Spinner categorySpinner;
    private static String countryCode = "";
    private static String category = "";
    private static Boolean bbcSelected = true;
    private LinearLayout homeLy;
    private LinearLayout fingerprintLy;
    private static Boolean isSuccessLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_logo_white);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        homeLy = (LinearLayout) findViewById(R.id.main);
        fingerprintLy = (LinearLayout) findViewById(R.id.fingerprint);
        Button getBtn = (Button) findViewById(R.id.get);
        newsListview = (ListView) findViewById(R.id.meg_list_lisview);

        Fingerprint fingerprint = new Fingerprint(context);
        fingerprint.fingerprintFun();

        BBCNews bbcNews = new BBCNews(context);
        bbcNews.getNews(countryCode, category, bbcSelected);

    }

    public static DrawerLayout dl;
    public MenuItem setting;
    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        setting = menu.findItem(R.id.setting_menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            if(dl.isDrawerOpen(Gravity.LEFT)) {
                dl.closeDrawer(Gravity.LEFT);
            }else{
                dl.openDrawer(Gravity.LEFT);
            }
        }

        //setting layout
        if (item.getItemId() == R.id.setting_menu) {

            final Dialog dialog = new Dialog((context));
            dialog.setContentView(R.layout.settings_layout);//report_details
            dialog.setCancelable(false);
            Button closeSettingLayout = (Button) dialog.findViewById(R.id.close_layout);
            Button loadNews = (Button) dialog.findViewById(R.id.load_news_btn);
            bbcCheckbox = (CheckBox) dialog.findViewById(R.id.bbc_checkbox);
            otherCheckbox = (CheckBox) dialog.findViewById(R.id.other_checkbox);
            countrySpinner = (Spinner) dialog.findViewById(R.id.country_spinner);
            categorySpinner = (Spinner) dialog.findViewById(R.id.category_spinner);
            countrySpinner.setEnabled(false);
            categorySpinner.setEnabled(false);



            HashMap<String,String> spinnerMap = new HashMap<String, String>();
            spinnerMap.put("Portugal","pt");
            spinnerMap.put("Turkey","tr");
            spinnerMap.put("England","gb");
            spinnerMap.put("Japan","jp");

            ArrayList<String> values = new ArrayList<>(spinnerMap.keySet());
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item,values);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            countrySpinner.setAdapter(adapter);

            countrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    countryCode = spinnerMap.get(adapterView.getItemAtPosition(i));
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    category = parent.getItemAtPosition(i).toString();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });



            loadNews.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (bbcCheckbox.isChecked())
                        bbcSelected = true;

                    dialog.dismiss();
                    BBCNews bbcNews = new BBCNews(context);
                    bbcNews.getNews(countryCode, category, bbcSelected);
                }
            });

            closeSettingLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);


        }

        return super.onOptionsItemSelected(item);
    }



//    private boolean fingerPrintFun(){
//        // Initialising msgtext and loginbutton
//        TextView msgtex = findViewById(R.id.msgtext);
//        final Button loginbutton = findViewById(R.id.login_btn);
//
//        // creating a variable for our BiometricManager
//        // and lets check if our user can use biometric sensor or not
//        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(this);
//        switch (biometricManager.canAuthenticate()) {
//
//            // this means we can use biometric sensor
//            case BiometricManager.BIOMETRIC_SUCCESS:
//                msgtex.setText("You can use the fingerprint sensor to login");
//                msgtex.setTextColor(Color.parseColor("#fafafa"));
//                if (isSuccessLogin != null && isSuccessLogin){
//                    fingerprintLy.setVisibility(View.GONE);
//                    homeLy.setVisibility(View.VISIBLE);
//                }
//                break;
//
//            // this means that the device doesn't have fingerprint sensor
//            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
//                msgtex.setText("This device doesnot have a fingerprint sensor");
//                loginbutton.setVisibility(View.GONE);
//                fingerprintLy.setVisibility(View.GONE);
//                homeLy.setVisibility(View.VISIBLE);
//                break;
//
//            // this means that biometric sensor is not available
//            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
//                msgtex.setText("The biometric sensor is currently unavailable");
//                loginbutton.setVisibility(View.GONE);
//                fingerprintLy.setVisibility(View.GONE);
//                homeLy.setVisibility(View.VISIBLE);
//                break;
//
//            // this means that the device doesn't contain your fingerprint
//            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
//                msgtex.setText("Your device doesn't have fingerprint saved, \n please check your security settings");
//                loginbutton.setVisibility(View.GONE);
//                fingerprintLy.setVisibility(View.GONE);
//                homeLy.setVisibility(View.VISIBLE);
//                break;
//            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
//            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
//            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
//                fingerprintLy.setVisibility(View.GONE);
//                homeLy.setVisibility(View.VISIBLE);
//                break;
//        }
//        // creating a variable for our Executor
//        Executor executor = ContextCompat.getMainExecutor(this);
//        // this will give us result of AUTHENTICATION
//        final BiometricPrompt biometricPrompt = new BiometricPrompt(MainActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
//            @Override
//            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
//                super.onAuthenticationError(errorCode, errString);
//            }
//
//            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
//            @Override
//            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
//                super.onAuthenticationSucceeded(result);
//                Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();
//                loginbutton.setText("Login Successful");
//                isSuccessLogin = true;
//                fingerprintLy.setVisibility(View.GONE);
//                homeLy.setVisibility(View.VISIBLE);
//            }
//            @Override
//            public void onAuthenticationFailed()
//            {
//                super.onAuthenticationFailed();
//                isSuccessLogin = false;
//            }
//        });
//        // creating a variable for our promptInfo
//        // BIOMETRIC DIALOG
//        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("GFG")
//                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();
//        loginbutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                biometricPrompt.authenticate(promptInfo);
//
//            }
//        });
//
//        return true;
//    }

    //back button to exit
    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getResources().getString(R.string.quit_message), Toast.LENGTH_SHORT).show();


        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }


    public void otherCheckboxClicked(View v) {
        //if this checkbox is checked!
        CheckBox checkBox = (CheckBox)v;
        if(checkBox.isChecked()){
            bbcCheckbox.setChecked(false);
            countrySpinner.setEnabled(true);
            categorySpinner.setEnabled(true);
            bbcSelected = false;
        }else {
            bbcCheckbox.setChecked(true);
            countrySpinner.setEnabled(false);
            categorySpinner.setEnabled(false);
            bbcSelected = true;
            countryCode = "";
            category = "";
        }
    }

    public void bbcCheckboxClicked(View v) {
            //if this checkbox is checked!
            CheckBox checkBox = (CheckBox)v;
            if(checkBox.isChecked()){
                otherCheckbox.setChecked(false);
                countrySpinner.setEnabled(false);
                categorySpinner.setEnabled(false);
                bbcSelected = true;
                countryCode = "";
                category = "";
            }else {
                otherCheckbox.setChecked(true);
                countrySpinner.setEnabled(true);
                categorySpinner.setEnabled(true);
                bbcSelected = false;
            }
    }

}