package group12.tcss450.uw.edu.appproject.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import group12.tcss450.uw.edu.appproject.activities.MainActivity;
import group12.tcss450.uw.edu.appproject.R;

public class BootUpReceiver extends BroadcastReceiver {
    private static final String TAG = "BootUpReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");

        SharedPreferences sharedPreferences =
                context.getSharedPreferences(context.getString(R.string.SHARED_PREFS),
                        Context.MODE_PRIVATE);
        String user = sharedPreferences.getString(context.getString(R.string.username_pref), null);
        if (user != null) {
            Log.d(TAG, "auto login " + user);
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("user", user);
            context.startActivity(i);
        }

    }

    public BootUpReceiver() {
    }
}
