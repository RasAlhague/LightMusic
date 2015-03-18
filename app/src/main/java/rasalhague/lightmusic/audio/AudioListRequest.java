package rasalhague.lightmusic.audio;

import com.vk.sdk.api.*;

public class AudioListRequest
{
    private RequestCompleteHandler requestCompleteHandler;
    private       String audioCount        = "20";
    private final String AUDIO_GET_VERSION = "5.29";

    public AudioListRequest(RequestCompleteHandler requestCompleteHandler)
    {
        this.requestCompleteHandler = requestCompleteHandler;
    }

    public AudioListRequest(RequestCompleteHandler requestCompleteHandler, String AUDIO_COUNT)
    {
        this.requestCompleteHandler = requestCompleteHandler;
        this.audioCount = AUDIO_COUNT;
    }

    public String getAudioCount()
    {
        return audioCount;
    }

    public void setAudioCount(String audioCount)
    {
        this.audioCount = audioCount;
    }

    public void execute()
    {
        VKParameters vkParameters = VKParameters.from(VKApiConst.USER_ID,
                                                      "192383960",
                                                      VKApiConst.COUNT,
                                                      audioCount,
                                                      VKApiConst.VERSION,
                                                      AUDIO_GET_VERSION);

//        VKRequest request = new VKRequest("audio.get", vkParameters);
        VKRequest request = new VKRequest("audio.getRecommendations", vkParameters);
        request.executeWithListener(new VKRequest.VKRequestListener()
        {
            @Override
            public void onComplete(VKResponse response)
            {
                super.onComplete(response);
                requestCompleteHandler.handleResponse(response);
            }

            @Override
            public void onError(VKError error)
            {
                super.onError(error);
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts)
            {
                super.attemptFailed(request, attemptNumber, totalAttempts);
            }

            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal)
            {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
            }
        });
    }
}
