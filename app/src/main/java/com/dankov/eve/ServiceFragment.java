package com.dankov.eve;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.telephony.SmsManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public abstract class ServiceFragment extends Fragment {

    public static final String PHONE_NUMBER = "6475260677";

    Activity currActivity;
    SmsManager sms;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        currActivity = getActivity();
        initSMS(currActivity);
    }

    public boolean initSMS(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            sms = SmsManager.getDefault();
            return true;
        } else {
            Log.e("SMS Permission: ", "No Permission");
            int requestCode = 0;
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.SEND_SMS}, requestCode);
            return false;
        }
    }

    public boolean sendSMS(String message) {
        if(initSMS(currActivity)) {
            sms.sendTextMessage(PHONE_NUMBER, null, message, null, null);
            return true;
        } else {
            return false;
        }
    }

}
