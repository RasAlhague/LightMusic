package rasalhague.lightmusic;

import android.widget.SeekBar;
import android.widget.TextView;

public class ThresholdSlider
{
    private SeekBar thresholdBar;
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

    public void initialize(SeekBar thresholdBar, TextView thresholdValueTextView)
    {
        this.thresholdBar = thresholdBar;
        this.thresholdValueTextView = thresholdValueTextView;

        currentThreshold = thresholdBar.getProgress();

        configureThresholdValueTextView();
    }

    private void configureThresholdValueTextView()
    {
        thresholdBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                thresholdValueTextView.setText(String.valueOf(progress));
                currentThreshold = progress;
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
