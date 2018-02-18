package yoniz.l3x1.apiFace;

public interface IdAPI {
    // **********************************************
    // *** Update or verify the following values. ***
    // **********************************************

    // Replace the subscriptionKey string value with your valid subscription key.
    String subscriptionKey = "818c50f19c974fbc8601820ef5aaa75a";

    // Replace or verify the region.
    //
    // You must use the same region in your REST API call as you used to obtain your subscription keys.
    // For example, if you obtained your subscription keys from the westus region, replace
    // "westcentralus" in the URI below with "westus".
    //
    // NOTE: Free trial subscription keys are generated in the westcentralus region, so if you are using
    // a free trial subscription key, you should not need to change this region.
    String uriBaseDetect = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/detect";

    String uriBaseFaceList = "https://northeurope.api.cognitive.microsoft.com/face/v1.0/facelists/";


}
