package rasalhague.lightmusic;

import android.content.SharedPreferences;

public class SharedPreferencesHolder
{
    private SharedPreferences sharedPreferences;
    public static final String PREFS_NAME = "Preferences";
    public static final String CURRENT_THRESHOLD = "CURRENT_THRESHOLD";

    private static SharedPreferencesHolder ourInstance = new SharedPreferencesHolder();

    public static SharedPreferencesHolder getInstance()
    {
        return ourInstance;
    }

    public SharedPreferences getSharedPreferences()
    {
        return sharedPreferences;
    }

    private SharedPreferencesHolder()
    {  }

    public void initialize(SharedPreferences sharedPreferences)
    {
        this.sharedPreferences = sharedPreferences;
    }
}
