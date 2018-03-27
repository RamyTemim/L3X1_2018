package microsoft;

/**
 * Interface contenant toutes les clés et les url permettant de s'authentifier et d'intéragire avec les différentes APIs de Microsoft
 */
public interface IdAPI {

    // A modifier avec sa propre clé crée en ouvrant un compte sur azure.microsoft.com
    //Clé pour accéder à l'Api Face
    String subscriptionKey = "7c5a15ca818b48d6b13d8ec78bcc8cbe";

    //Url pour accéder à la fonction de detection des visages dans l'API face
    String uriBaseDetect = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/detect";

    //Url pour accéder à la fonction de comparaison des visages dans l'API face
    String uriBaseFindSimilar = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/findsimilars";

    //Url pour accéder à la fonction de création de list de visage dans l'API face
    String uriBaseFaceList = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/facelists/";

    // A modifier avec sa propre clé crée en ouvrant un compte sur videoindexer.ai
    //Clé pour acceder à l'API vidéo Indexer
    String videoKey = "19b9d647b7e649b38ec9dbb472b6d668";

    //Url pour accéder à la fonction permettant de récupérer les éléments détéctés par l'API Vidéo Indexer dans une vidéo
    String videoIndexerUpload = "https://videobreakdown.azure-api.net/Breakdowns/Api/Partner/Breakdowns/";

    //Url pour accéder à une photo trouvée dans une vidéo
    String thumbnail = "https://www.videoindexer.ai/api/Thumbnail/";

}
