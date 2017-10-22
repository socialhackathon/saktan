package newfusedlocationclient;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.StringDef;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Date;

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

        if (triggerCounter > TRIGGER_THRESHOLD) {
            triggerInProgress = true;
            Log.i("triggerInProgress", triggerCounter + "");

            Toast.makeText(context.getApplicationContext(), "EMERGENCYYYYYYY", Toast.LENGTH_LONG).show();
//            sendSMS(context, "+996771333076", triggerCounter+" # "+new Date().toString());

            getLastLocation(context);
            triggerInProgress = false;

//            new android.os.Handler().postDelayed(
//                    new Runnable() {
//                        public void run() {
//                            triggerInProgress = false;
//                            Log.i("tag", "This'll run 300 milliseconds later");
//                        }
//                    },
//                    5000);
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
//                            SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                            String key = "omurbek-android-app", value = "+996771333076";


                            if (preferences.getString(key, null) == null) {
                                preferences.edit().putString(key, value).commit();
                                preferences.edit().putString(key+"2", "+996772137791").commit();
                            }
                            String phoneNumber = preferences.getString(key, null);
                            String mederPhone = preferences.getString(key+"2", null);
                            Log.i("phoneSharedPreferences", phoneNumber);
                            if (phoneNumber != null) {
                                sendSMS(context, phoneNumber, latitude, longitude);
                                sendSMS(context, mederPhone, latitude, longitude);
                            }else{
                                Log.i("phoneSharedPreferences", "can not read phone number");
                            }
                        } else {
                            Log.w("LOCATION_SOS", "getLastLocation:exception", task.getException());
                        }
                    }
                });
    }
//     http://maps.google.com/?q=42.8111227,74.6273123
    public void sendSMS(Context context, String phoneNo, String latitude, String longitude) {
      String msg = "SOS!!!\t" + "http://maps.google.com/?q=" + latitude +","+longitude;
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, msg, null, null);
            Toast.makeText(context.getApplicationContext(), "Message Sent",
                    Toast.LENGTH_LONG).show();
            ((Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE)).vibrate(5000);
        } catch (Exception ex) {
            Toast.makeText(context.getApplicationContext(), ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }

}