package rasalhague.lightmusic;

import android.app.Notification;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.Process;
import android.util.Log;
import rasalhague.lightmusic.audio.VkAudioManager;

public class LightMusicService extends Service implements SensorEventListener
{
    public LightMusicService()
    {
    }

    public static final String         TAG                       = LightMusicService.class.getName();
    public static final int            SCREEN_OFF_RECEIVER_DELAY = 500;
    private             VkAudioManager vkAudioManager            = new VkAudioManager(this);

    private SensorManager         mSensorManager = null;
    private PowerManager.WakeLock mWakeLock      = null;

    /*
     * Register this as a sensor event listener.
     */
    private void registerListener()
    {
        mSensorManager.registerListener(this,
                                        mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                                        SensorManager.SENSOR_DELAY_NORMAL);
    }

    /*
     * Un-register this as a sensor event listener.
     */
    private void unregisterListener()
    {
        mSensorManager.unregisterListener(this);
    }

    public BroadcastReceiver mReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            Log.i(TAG, "onReceive(" + intent + ")");

            if (!intent.getAction().equals(Intent.ACTION_SCREEN_OFF))
            {
                return;
            }

            Runnable runnable = new Runnable()
            {
                public void run()
                {
                    Log.i(TAG, "Runnable executing.");
                    unregisterListener();
                    registerListener();
                }
            };

            new Handler().postDelayed(runnable, SCREEN_OFF_RECEIVER_DELAY);
        }
    };

    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        Log.i(TAG, "onAccuracyChanged().");
    }

    public void onSensorChanged(SensorEvent event)
    {
        float lightLevel = event.values[0];

        if (lightLevel > ThresholdSlider.getInstance().getCurrentThreshold())
        {
            vkAudioManager.play();
        }
        else
        {
            vkAudioManager.stop();
        }

        System.out.println(lightLevel);
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        PowerManager manager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        mWakeLock = manager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, TAG);

        registerReceiver(mReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }

    @Override
    public void onDestroy()
    {
        unregisterReceiver(mReceiver);
        unregisterListener();
        mWakeLock.release();
        stopForeground(true);
    }

    @Override
    public IBinder onBind(Intent intent)
    {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        super.onStartCommand(intent, flags, startId);

        startForeground(Process.myPid(), new Notification());
        registerListener();
        mWakeLock.acquire();

        return START_STICKY;
    }
}
