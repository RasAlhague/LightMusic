package rasalhague.lightmusic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.SeekBar;
import android.widget.TextView;
import com.vk.sdk.*;
import com.vk.sdk.api.VKError;
import com.vk.sdk.dialogs.VKCaptchaDialog;

public class MainActivity extends ActionBarActivity
{
    private static final String[] sMyScope = new String[]{VKScope.STATS, VKScope.AUDIO};

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        VKUIHelper.onCreate(this);
        VKSdk.initialize(new VKSdkListener()
        {
            @Override
            public void onCaptchaError(VKError captchaError)
            {
                new VKCaptchaDialog(captchaError).show();
            }

            @Override
            public void onTokenExpired(VKAccessToken expiredToken)
            {
                VKSdk.authorize(sMyScope);
            }

            @Override
            public void onAccessDenied(final VKError authorizationError)
            {
                new AlertDialog.Builder(VKUIHelper.getTopActivity()).setMessage(authorizationError.toString()).show();
            }

            @Override
            public void onReceiveNewToken(VKAccessToken newToken)
            {
                LightIntent.startActionLightListening(MainActivity.this);
                //                LightManager.getInstance().initialize(MainActivity.this);
                //                startService(new Intent(MainActivity.this, LightMusicService.class));
            }

            @Override
            public void onAcceptUserToken(VKAccessToken token)
            {
                System.out.println();
            }
        }, "4831978");

        VKSdk.authorize(sMyScope);

        configureSharedPreferencesManager(); //first

        configureSensorValueTextView();
        configureThresholdSlider();
        fillCurrentThresholdValueTextView();
    }

    private void configureSharedPreferencesManager()
    {
        SharedPreferencesHolder.getInstance()
                                .initialize(getSharedPreferences(SharedPreferencesHolder.PREFS_NAME, MODE_PRIVATE));
    }

    private void configureSensorValueTextView()
    {
        final TextView textView = (TextView) findViewById(R.id.sensor_value);
        SensorManager mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        Sensor mLight = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        mSensorManager.registerListener(new SensorEventListener()
        {
            @Override
            public void onSensorChanged(SensorEvent event)
            {
                textView.setText(String.valueOf(event.values[0]));
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy)
            {

            }
        }, mLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void configureThresholdSlider()
    {
        SeekBar thresholdSlider = (SeekBar) findViewById(R.id.threshold_bar);
        TextView thresholdValueTextView = (TextView) findViewById(R.id.slider_value);

        int threshold = SharedPreferencesHolder.getInstance()
                                               .getSharedPreferences()
                                               .getInt(SharedPreferencesHolder.CURRENT_THRESHOLD, 100);
        ThresholdSlider.getInstance().initialize(thresholdSlider, thresholdValueTextView, threshold);
    }

    private void fillCurrentThresholdValueTextView()
    {
        TextView currentThresholdValueTextView = (TextView) findViewById(R.id.current_threshold_value);
        int threshold = SharedPreferencesHolder.getInstance()
                                               .getSharedPreferences()
                                               .getInt(SharedPreferencesHolder.CURRENT_THRESHOLD, 100);
        currentThresholdValueTextView.setText("Current threshold: " + String.valueOf(threshold));
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        VKUIHelper.onResume(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        VKUIHelper.onDestroy(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        VKUIHelper.onActivityResult(this, requestCode, resultCode, data);
    }
}
