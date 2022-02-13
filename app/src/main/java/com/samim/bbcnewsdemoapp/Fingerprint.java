package com.samim.bbcnewsdemoapp;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;

public class Fingerprint {
    private LinearLayout homeLy;
    private LinearLayout fingerprintLy;
    private static Boolean isSuccessLogin;
    Context context;

    public Fingerprint(Context context){
        this.context = context;
    }

    public boolean fingerprintFun(){
        // Initialising msgtext and loginbutton
        TextView msgtex = ((Activity)context).findViewById(R.id.msgtext);
        Button loginbutton = ((Activity)context).findViewById(R.id.login_btn);
        homeLy = (LinearLayout) ((Activity)context).findViewById(R.id.main);
        fingerprintLy = (LinearLayout) ((Activity)context).findViewById(R.id.fingerprint);

        // creating a variable for our BiometricManager
        // and lets check if our user can use biometric sensor or not
        BiometricManager biometricManager = androidx.biometric.BiometricManager.from(context);
        switch (biometricManager.canAuthenticate()) {

            // this means we can use biometric sensor
            case BiometricManager.BIOMETRIC_SUCCESS:
                msgtex.setText("You can use the fingerprint sensor to login");
                msgtex.setTextColor(Color.parseColor("#fafafa"));
                if (isSuccessLogin != null && isSuccessLogin){
                    fingerprintLy.setVisibility(View.GONE);
                    homeLy.setVisibility(View.VISIBLE);
                }
                break;

            // this means that the device doesn't have fingerprint sensor
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                msgtex.setText("This device doesnot have a fingerprint sensor");
                loginbutton.setVisibility(View.GONE);
                fingerprintLy.setVisibility(View.GONE);
                homeLy.setVisibility(View.VISIBLE);
                break;

            // this means that biometric sensor is not available
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                msgtex.setText("The biometric sensor is currently unavailable");
                loginbutton.setVisibility(View.GONE);
                fingerprintLy.setVisibility(View.GONE);
                homeLy.setVisibility(View.VISIBLE);
                break;

            // this means that the device doesn't contain your fingerprint
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                msgtex.setText("Your device doesn't have fingerprint saved, \n please check your security settings");
                loginbutton.setVisibility(View.GONE);
                fingerprintLy.setVisibility(View.GONE);
                homeLy.setVisibility(View.VISIBLE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                fingerprintLy.setVisibility(View.GONE);
                homeLy.setVisibility(View.VISIBLE);
                break;
        }
        // creating a variable for our Executor
        Executor executor = ContextCompat.getMainExecutor(context);
        // this will give us result of AUTHENTICATION
        final BiometricPrompt biometricPrompt = new BiometricPrompt((FragmentActivity) context, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
            }

            // THIS METHOD IS CALLED WHEN AUTHENTICATION IS SUCCESS
            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(context, "Login Success", Toast.LENGTH_SHORT).show();
                loginbutton.setText("Login Successful");
                isSuccessLogin = true;
                fingerprintLy.setVisibility(View.GONE);
                homeLy.setVisibility(View.VISIBLE);
            }
            @Override
            public void onAuthenticationFailed()
            {
                super.onAuthenticationFailed();
                isSuccessLogin = false;
            }
        });
        // creating a variable for our promptInfo
        // BIOMETRIC DIALOG
        final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder().setTitle("GFG")
                .setDescription("Use your fingerprint to login ").setNegativeButtonText("Cancel").build();
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                biometricPrompt.authenticate(promptInfo);

            }
        });

        return true;
    }
}
