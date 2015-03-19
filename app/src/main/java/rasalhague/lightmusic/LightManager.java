package rasalhague.lightmusic;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

public class LightManager implements Runnable, SensorEventListener
{
    private SensorManager mSensorManager;
    private Sensor        mLight;
    private Context       context;
    private static LightManager ourInstance = new LightManager();

    public static LightManager getInstance()
    {
        return ourInstance;
    }

    private LightManager()
    {
        new Thread(this).start();
    }

    public void initialize(Context context)
    {
        this.context = context;
        initSensors();
        registerScreenOffReceiver();
    }

    @Override
    public void run()
    {

        //        new Timer().scheduleAtFixedRate(new TimerTask()
        //        {
        //            @Override
        //            public void run()
        //            {
        //
        //            }
        //        }, 0, 500);
    }

    private void initSensors()
    {
        mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void registerScreenOffReceiver()
    {
        ScreenOffReceiver screenOffReceiver;
        screenOffReceiver = new ScreenOffReceiver(mSensorManager, mLight, this);
        context.registerReceiver(screenOffReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        float lightLevel = event.values[0];
        System.out.println(lightLevel);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }
}
