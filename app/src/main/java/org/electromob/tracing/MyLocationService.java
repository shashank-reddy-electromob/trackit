package org.electromob.tracing;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;

import com.google.android.gms.location.LocationResult;

public class MyLocationService extends BroadcastReceiver {

    public static final String ACTION_PROCESS_UPDATE = "googlelocationbackground.UPDATE_LOCATION";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null)
        {
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATE.equals(action))
            {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null)
                {
                    Location location = result.getLastLocation();
                    /*String location_string = new StringBuilder("" + location.getLatitude())
                            .append("/")
                            .append(location.getLongitude())
                            .toString();*/
                    double lnt1,lng1;
                    lnt1 = location.getLatitude();
                    lng1 = location.getLongitude();
                    try {
                        userlocation.getInstance().updatelocation(lnt1);
                        userlocation.getInstance().updatelocation1(lng1);
                    }catch (Exception e){

                    }
                }
            }
        }

    }
}
