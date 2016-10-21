package com.almootassem.android.lab4;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver mBatteryBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String notificationText = "";
            int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING;
            boolean isFull = status == BatteryManager.BATTERY_STATUS_FULL;
            boolean isHealthy = status == BatteryManager.BATTERY_HEALTH_GOOD;
            boolean isPluggedAC = status == BatteryManager.BATTERY_PLUGGED_AC;
            boolean isPluggedUSB = status == BatteryManager.BATTERY_PLUGGED_USB;
            int temp = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, 0)/10;
            int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 1);
            String healthString = "";
            if (!isHealthy){
                switch (health) {
                    case BatteryManager.BATTERY_HEALTH_DEAD:
                        healthString = getString(R.string.dead);
                        break;
                    case BatteryManager.BATTERY_HEALTH_GOOD:
                        healthString = getString(R.string.good);
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                        healthString = getString(R.string.over_voltage);
                        break;
                    case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                        healthString = getString(R.string.over_heat);
                        break;
                    case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                        healthString = getString(R.string.failure);
                        break;
                }
            }
            Log.e("BATTERY", "Charging: " + isCharging + " Full: " + isFull + " Healthy: " + isHealthy + " AC: " + isPluggedAC + " USB: " + isPluggedUSB + " Temp: " + temp);

            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(MainActivity.this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(getString(R.string.battery_status));
            if (isCharging) notificationText += getString(R.string.battery_charging); else notificationText += getString(R.string.battery_discharging);
            if (isFull) notificationText += getString(R.string.battery_full);
            if (isHealthy) notificationText += getString(R.string.battery_healthy); else notificationText += getString(R.string.battery) + healthString + "\n";
            if (isPluggedAC) notificationText += getString(R.string.battery_ac); else if (isPluggedUSB) notificationText += getString(R.string.battery_usb); else notificationText += getString(R.string.phone_unplugged);
            notificationText += getString(R.string.temperature) + temp + "°C";
            mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(notificationText));

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(context.NOTIFICATION_SERVICE);
            mNotificationManager.notify(0, mBuilder.build());
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        registerReceiver(mBatteryBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public void onPause(){
        super.onPause();
        unregisterReceiver(mBatteryBroadcastReceiver);
    }

    @Override
    public void onResume(){
        super.onResume();
        registerReceiver(mBatteryBroadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
}
