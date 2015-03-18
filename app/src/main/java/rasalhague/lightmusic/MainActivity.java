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
import android.view.Menu;
import android.view.MenuItem;
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

        final Context context = this;

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
                System.out.println(newToken);
                LightIntent.startActionLightListening(context);
            }

            @Override
            public void onAcceptUserToken(VKAccessToken token)
            {
                System.out.println();
            }
        }, "4831978");

        VKSdk.authorize(sMyScope);

        configureSensorValueTextView();
        configureThresholdSlider();
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
        TextView thresholdValueTextView = (TextView) findViewById(R.id.threshold_value);

        ThresholdSlider.getInstance().initialize(thresholdSlider, thresholdValueTextView);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
