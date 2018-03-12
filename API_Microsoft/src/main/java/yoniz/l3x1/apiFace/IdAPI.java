package yoniz.l3x1.apiFace;

public interface IdAPI {

    //Clé pour accéder à l'Api Face
    String subscriptionKey = "7c5a15ca818b48d6b13d8ec78bcc8cbe";

    // 2
    //String subscriptionKey = "081ada40e80e4a5c9be2ceb2f9f06941";

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
