package rasalhague.lightmusic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class ScreenOffReceiver extends BroadcastReceiver
{
    private SensorManager       mSensorManager;
    private Sensor              mLight;
    private SensorEventListener sensorEventListener;

    public ScreenOffReceiver(SensorManager mSensorManager, Sensor mLight, SensorEventListener sensorEventListener)
    {
        this.mSensorManager = mSensorManager;
        this.mLight = mLight;
        this.sensorEventListener = sensorEventListener;
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
        {
            mSensorManager.unregisterListener(sensorEventListener);
            mSensorManager.registerListener(sensorEventListener, mLight, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
}
