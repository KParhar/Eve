package com.dankov.eve;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class SMSReciever extends BroadcastReceiver{
    //https://android.jlelse.eu/detecting-sending-sms-on-android-8a154562597f
    private static final String TAG = "SMSReciever";

    private final String serviceProviderNumber;
    private final String serviceProviderSmsCondition;

    private Listener listener;

    public SMSReciever(String serviceProviderNumber, String serviceProviderSmsCondition) {
        this.serviceProviderNumber = serviceProviderNumber;
        this.serviceProviderSmsCondition = serviceProviderSmsCondition;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            //SmsSender is who sent it
            String smsSender = "";
            //smsBody is what was sent
            String smsBody = "";
            //Check version of Android, if Kitkat use better method if not available use legacy
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                    smsSender = smsMessage.getDisplayOriginatingAddress();
                    smsBody += smsMessage.getMessageBody();
                }
            } else {
                Bundle smsBundle = intent.getExtras();
                if (smsBundle != null) {
                    Object[] pdus = (Object[]) smsBundle.get("pdus");
                    if (pdus == null) {
                        // Display some error to the user
                        Log.e(TAG, "SmsBundle had no pdus key");
                        return;
                    }
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    for (int i = 0; i < messages.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        smsBody += messages[i].getMessageBody();
                    }
                    smsSender = messages[0].getOriginatingAddress();
                }
            }
            //Right now this is commented out for testing
            //What this does is restricts what message is put in to differentiate the recieved message from some random text
            if (/*smsSender.equals(serviceProviderNumber) && smsBody.startsWith(serviceProviderSmsCondition)*/true) {
                if (listener != null) {
                    listener.onTextReceived(smsBody);
                }
            }
        }
    }

    void setListener(Listener listener) {
        this.listener = listener;
    }

    interface Listener {
        void onTextReceived(String text);
    }
}
