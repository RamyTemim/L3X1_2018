package yoniz.l3x1.apiFace;

public interface IdAPI {

    //Clé pour accéder à l'Api Face
    String subscriptionKey = "818c50f19c974fbc8601820ef5aaa75a";

    // 2
    // String subscriptionKey = "d320e411a69b4982900eec166e8be4ed";

    String uriBaseDetect = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/detect";

    String uriBaseFindSimilar = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/findsimilars";

    String uriBaseFaceList = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/facelists/";

    String videoIndexerUpload = "https://videobreakdown.azure-api.net/Breakdowns/Api/Partner/Breakdowns/";

    String thumbnail = "https://www.videoindexer.ai/api/Thumbnail/";

    String videoKey = "19b9d647b7e649b38ec9dbb472b6d668";


}
