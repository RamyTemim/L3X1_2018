package identification;

/**
 * L3X1 FACIAL RECOGNITION COMPARATOR
 * <p>
 * IA as a service (Facial recognition on video)
 * <p>
 * PACKAGE identification
 * <p>
 * Cette classe est une classe qui a pour but de stocker les constantes
 * <p>
 * Elle contient toutes les clés d'accès à l'API de Microsoft
 */
public final class KeyMicrosoftApi {
    private KeyMicrosoftApi() {
    }

    // A modifier avec sa propre clé crée en ouvrant un compte sur azure.microsoft.com
    //Clé pour accéder à l'Api Face
    public static final String SUBSCRIPTION_KEY = "c04fe078bbc24062bbf4e19e2872aeb8";

    //Url pour accéder à la fonction de detection des visages dans l'API face
    public static final String URI_BASE_DETECT = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/detect";

    //Url pour accéder à la fonction de comparaison des visages dans l'API face
    public static final String URI_BASE_FIND_SIMILAR = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/findsimilars";

    //Url pour accéder à la fonction de création de list de visage dans l'API face
    public static final String URI_BASE_FACE_LIST = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/facelists/";

    // A modifier avec sa propre clé crée en ouvrant un compte sur videoindexer.ai
    //Clé pour acceder à l'API vidéo Indexer
    public static final String VIDEO_KEY = "19b9d647b7e649b38ec9dbb472b6d668";

    //Url pour accéder à la fonction permettant de récupérer les éléments détéctés par l'API Vidéo Indexer dans une vidéo
    public static final String VIDEO_INDEXER_UPLOAD = "https://videobreakdown.azure-api.net/Breakdowns/Api/Partner/Breakdowns/";

    //Url pour accéder à une photo trouvée dans une vidéo
    public static final String THUMBNAIL = "https://www.videoindexer.ai/api/Thumbnail/";

}
