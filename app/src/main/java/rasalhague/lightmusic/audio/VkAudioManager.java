package rasalhague.lightmusic.audio;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiAudio;
import com.vk.sdk.api.model.VKList;

import java.io.IOException;

public class VkAudioManager
{
    private Context            context;
    private MediaPlayer        mediaPlayer;
    private VKList<VKApiAudio> audioList;
    private int lastAudioNumber = 0;
    private boolean isPlaying = false;

    public VkAudioManager(Context context)
    {
        this.context = context;
        audioList = new VKList<>();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener()
        {
            @Override
            public void onCompletion(MediaPlayer mp)
            {
                if (audioList != null) playNextFromList();
            }
        });
    }

    public void play()
    {
        if (!isPlaying)
        {
            updateAudioList();
            isPlaying = true;
        }
    }

    public void stop()
    {
        if (isPlaying)
        {
            mediaPlayer.stop();
            mediaPlayer.reset();
            isPlaying = false;
        }
    }

    private void updateAudioList()
    {
        AudioListRequest audioListRequest = new AudioListRequest(new RequestCompleteHandler()
        {
            @Override
            public void handleResponse(VKResponse response)
            {
                VKList<VKApiAudio> tempAudioList = new VKList<>(response.json, VKApiAudio.class);
                if (! isAudioListsSame(tempAudioList, audioList))
                {
                    audioList = tempAudioList;
                    lastAudioNumber = 0;
                }
                playNextFromList();
            }
        });
        audioListRequest.execute();
    }

    private boolean isAudioListsSame(VKList<VKApiAudio> first, VKList<VKApiAudio> second)
    {
        if (first.size() != second.size()) return false;

        for (int i = 0; i < first.size(); i++)
        {
            VKApiAudio audioFromFirst = first.get(i);
            VKApiAudio audioFromSecond = second.get(i);

            if (audioFromFirst.id != audioFromSecond.id)
            {
                return false;
            }
        }

        return true;
    }

    private void playNextFromList()
    {
        Uri audioUri = Uri.parse(audioList.get(lastAudioNumber).url);

        mediaPlayer.stop();
        mediaPlayer.reset();

        try
        {
            mediaPlayer.setDataSource(context, audioUri);
            mediaPlayer.prepare();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
            {
                @Override
                public void onPrepared(MediaPlayer mp)
                {
                    mediaPlayer.start();
                }
            });
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        lastAudioNumber++;
        if (audioList.size() == lastAudioNumber)
        {
            lastAudioNumber = 0;
        }
    }
}
