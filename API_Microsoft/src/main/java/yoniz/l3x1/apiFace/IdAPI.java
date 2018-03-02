package yoniz.l3x1.apiFace;

public interface IdAPI {

    //Clé pour accéder à l'Api Face
    String subscriptionKey = "818c50f19c974fbc8601820ef5aaa75a";

    // 2
    //String subscriptionKey = "d320e411a69b4982900eec166e8be4ed";

    //Url pour accéder à la fonction de detection des visages dans l'API face
    String uriBaseDetect = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/detect";

    //Url pour accéder à la fonction de comparaison des visages dans l'API face
    String uriBaseFindSimilar = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/findsimilars";

    //Url pour accéder à la fonction de création de list de visage dans l'API face
    String uriBaseFaceList = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/facelists/";

    //Clé pour acceder à l'API vidéo Indexer
    String videoKey = "19b9d647b7e649b38ec9dbb472b6d668";

    //Url pour accéder à la fonction permettant de récupérer les éléments détéctés par l'API Vidéo Indexer dans une vidéo
    String videoIndexerUpload = "https://videobreakdown.azure-api.net/Breakdowns/Api/Partner/Breakdowns/";

    //URL pour accéder à une photo trouvée dans une vidéo
    String thumbnail = "https://www.videoindexer.ai/api/Thumbnail/";

}
