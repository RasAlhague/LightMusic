package rasalhague.lightmusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.*;
import android.os.Handler;

public class ScreenOffReceiver extends BroadcastReceiver
{
    private SensorManager       mSensorManager;
    private Sensor              mLight;
    private SensorEventListener sensorEventListener;
    private final long SCREEN_OFF_RECEIVER_DELAY = 500;

    public ScreenOffReceiver(SensorManager mSensorManager, Sensor mLight, SensorEventListener sensorEventListener)
    {
        this.mSensorManager = mSensorManager;
        this.mLight = mLight;
        this.sensorEventListener = sensorEventListener;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (!intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
        {
            return;
        }

        Runnable runnable = new Runnable()
        {
            public void run()
            {
                mSensorManager.unregisterListener(sensorEventListener);
                mSensorManager.registerListener(sensorEventListener, mLight, SensorManager.SENSOR_DELAY_NORMAL);
            }
        };

        new Handler().postDelayed(runnable, SCREEN_OFF_RECEIVER_DELAY);
    }
}
