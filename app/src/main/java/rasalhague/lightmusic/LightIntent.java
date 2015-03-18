package rasalhague.lightmusic;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import rasalhague.lightmusic.audio.VkAudioManager;

public class LightIntent extends IntentService implements SensorEventListener
{
    private SensorManager     mSensorManager;
    private Sensor            mLight;
    private ScreenOffReceiver screenOffReceiver;
    private VkAudioManager vkAudioManager = new VkAudioManager(this);

    private static final String ACTION_LightListening = "rasalhague.lightsensormusictrigger.action.LightListening";

    public static void startActionLightListening(Context context)
    {
        Intent intent = new Intent(context, LightIntent.class);
        intent.setAction(ACTION_LightListening);
        context.startService(intent);
    }

    public LightIntent()
    {
        super("LightIntent");
    }

    @Override
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
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
        System.out.println(accuracy);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        if (intent != null)
        {
            final String action = intent.getAction();
            if (ACTION_LightListening.equals(action))
            {
                handleActionLightListening();
            }
        }
    }

    private void handleActionLightListening()
    {
        initSensors();
        registerScreenOffReceiver();
    }

    private void initSensors()
    {
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);

        mSensorManager.registerListener(this, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void registerScreenOffReceiver()
    {
        screenOffReceiver = new ScreenOffReceiver(mSensorManager, mLight, this);
        getApplicationContext().registerReceiver(screenOffReceiver, new IntentFilter(Intent.ACTION_SCREEN_OFF));
    }
}
