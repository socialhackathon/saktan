package com.example.omurbek.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by omurbek on 10/21/2017.
 */

public class SOSBroadcastReceiver extends BroadcastReceiver {

    private static long lastTriggerTime = 0;
    private static final int ONE_MILLI = 1000;
    protected static final long ONE_SEC = 1 * ONE_MILLI;
    protected static final long TWO_SEC = 2 * ONE_MILLI;
    protected static final long THREE_SEC = 3 * ONE_MILLI;
    protected static final long FOUR_SEC = 4 * ONE_MILLI;
    protected static final long FIVE_SEC = 5 * ONE_MILLI;
    protected static final int TRIGGER_THRESHOLD = 3;
    protected static boolean triggerInProgress = false;

    public static final String APP_PREFERENCE_KEY = "SAKTAN_TEAM_3";


    protected static int triggerCounter = 0;

    // TODO Locations
    protected Location mLastLocation;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().contains(Intent.ACTION_SCREEN_ON)) {
            if (!triggerInProgress) {
                checkAndCreateAlert(context);
            }

        } else if (intent.getAction().contains(Intent.ACTION_SCREEN_OFF)) {
            if (!triggerInProgress) {
                checkAndCreateAlert(context);
            }
        }
    }

    private void checkAndCreateAlert(Context context) {
        Log.i("checkAndCreateAlert", triggerCounter + "");
        /*---- If the gap between power button press is less than 5 seconds ----*/
        if ((System.currentTimeMillis() - lastTriggerTime) <= FIVE_SEC
                || (triggerCounter == 0)) {
            triggerCounter++;
            lastTriggerTime = System.currentTimeMillis();
        } else {
            triggerCounter = 0;
        }

        if (triggerCounter >= TRIGGER_THRESHOLD) {
            triggerInProgress = true;
            Log.i("triggerInProgress", triggerCounter + "");

            Toast toast = Toast.makeText(context.getApplicationContext(), "Emergency!!!",
                    Toast.LENGTH_SHORT);
            TextView toastMessage = (TextView) toast.getView().findViewById(android.R.id.message);
            toastMessage.setTextColor(Color.RED);
            toast.show();

            getLastLocation(context);
            triggerInProgress = false;
            triggerCounter = 0;
        }
    }

    @SuppressWarnings("MissingPermission")
    private void getLastLocation(final Context context) {
        FusedLocationProviderClient mFusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                            String latitude = String.valueOf(mLastLocation.getLatitude());
                            String longitude = String.valueOf(mLastLocation.getLongitude());
//                            TODO check if sms is working with the location
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
                            String contacts = preferences.getString(APP_PREFERENCE_KEY, null);
                            Log.i("my_key_key", APP_PREFERENCE_KEY);

                            if (contacts.length() > 0) {
                                String[] contactList = contacts.split(",");
                                for (int i = 0; i < contactList.length; i++) {
                                    String[] contact = contactList[i].split("#");
                                    String contactName = contact[0];
                                    if (contact.length < 2) continue;
                                    String contactNumber = contact[1];
                                    sendSMS(context, contactNumber, latitude, longitude);
                                }
                            } else {
                                Log.i("phoneSharedPreferences", "can not read phone number");
                            }
                        } else {
                            Log.w("LOCATION_SOS", "getLastLocation:exception", task.getException());
                        }
                    }
                });
    }

    public void sendSMS(Context context, String phoneNo, String latitude, String longitude) {
        String msg = "SOS!!!\t" + "http://maps.google.com/?q=" + latitude + "," + longitude;
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(context.getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
            ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(2000);
        } catch (Exception ex) {
            Toast.makeText(context.getApplicationContext(), ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

}