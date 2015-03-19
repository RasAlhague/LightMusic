package rasalhague.lightmusic;

import android.content.SharedPreferences;
import android.widget.SeekBar;
import android.widget.TextView;

public class ThresholdSlider
{
    private SeekBar  thresholdBar;
    private TextView thresholdValueTextView;
    private int currentThreshold = 100;

    private static class ThresholdSliderHolder
    {
        static ThresholdSlider instance = new ThresholdSlider();
    }

    public static ThresholdSlider getInstance()
    {
        return ThresholdSliderHolder.instance;
    }

    private ThresholdSlider() { }

    public int getCurrentThreshold()
    {
        return currentThreshold;
    }

    public void initialize(SeekBar thresholdBar, TextView thresholdValueTextView, int threshold)
    {
        this.thresholdBar = thresholdBar;
        this.thresholdValueTextView = thresholdValueTextView;

        currentThreshold = threshold;
        thresholdValueTextView.setText("Slider value (app restart is required): " + String.valueOf(currentThreshold));

        thresholdBar.setProgress(threshold);

        configureThresholdValueTextView();
    }

    private void configureThresholdValueTextView()
    {
        thresholdBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                thresholdValueTextView.setText("Slider value (app restart is required): " + String.valueOf(progress));
                currentThreshold = progress;

                SharedPreferences settings = SharedPreferencesHolder.getInstance().getSharedPreferences();
                SharedPreferences.Editor editor = settings.edit();
                editor.putInt(SharedPreferencesHolder.CURRENT_THRESHOLD, progress);
                editor.apply();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {

            }
        });
    }
}
