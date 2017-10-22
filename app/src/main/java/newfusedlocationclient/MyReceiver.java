package newfusedlocationclient;

//import BroadcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by omurbek on 10/21/2017.
 */

public class MyReceiver extends BroadcastReceiver {
    private static int countPowerOff = 0;

    public MyReceiver() {
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Counter: " + countPowerOff, Toast.LENGTH_SHORT).show();
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.e("In on receive", "In Method:  ACTION_SCREEN_OFF");
            countPowerOff++;
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            countPowerOff++;
            Log.e("In on receive", "In Method:  ACTION_SCREEN_ON");
        } else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
            Toast.makeText(context, "Counter: " + countPowerOff, Toast.LENGTH_SHORT).show();
            Log.e("In on receive", "In Method:  ACTION_USER_PRESENT");
            if (countPowerOff > 2) {
                countPowerOff = 0;
                Toast.makeText(context, "MAIN ACTIVITY IS BEING CALLED ", Toast.LENGTH_SHORT).show();
                Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                if (Build.VERSION.SDK_INT >= 16) {
                    v.vibrate(VibrationEffect.createOneShot(150, 10));
                } else {
                    v.vibrate(3000);
                }
                Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(i);
            }
        }

        if (countPowerOff > 3) {
            Toast.makeText(context, "Mobile will vibrate: " + countPowerOff, Toast.LENGTH_SHORT).show();
            Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
            if (Build.VERSION.SDK_INT >= 16) {
                v.vibrate(VibrationEffect.createOneShot(150, 10));
            } else {
                v.vibrate(3000);
            }
        }
    }
}
