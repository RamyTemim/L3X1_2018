import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.ibm.watson.developer_cloud.http.RequestBuilder;
import com.ibm.watson.developer_cloud.http.ServiceCall;
import com.ibm.watson.developer_cloud.service.WatsonService;
import com.ibm.watson.developer_cloud.util.RequestUtils;
import com.ibm.watson.developer_cloud.util.ResponseConverterUtils;
import com.ibm.watson.developer_cloud.util.Validator;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifiedImages;
import com.ibm.watson.developer_cloud.visual_recognition.v3.model.ClassifyOptions;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


public class ComparateurApi  extends WatsonService{
     // name of the project in IBM cloud
    private static final String SERVICE_NAME = "apirest-watson-vis-1519555011205";
    // URL to poste request HTTP
    private static final String URL = "https://gateway-a.watsonplatform.net/visual-recognition/api";
    // version avec api
    private String versionDate;
     // the consatant version
    public static final String VERSION_DATE_2016_05_20 = "2016-05-20";
    private boolean endPointChanged;

    /**
     * Instantiates a new `VisualRecognition`.
     *
     * @param versionDate The version date (yyyy-MM-dd) of the REST API to use. Specifying this value will keep your API
     *          calls from failing when the service introduces breaking changes.
     */
    public ComparateurApi(String versionDate) {
        super(SERVICE_NAME);
        if ((getEndPoint() == null) || getEndPoint().isEmpty()) {
            setEndPoint(URL);
        }

        Validator.isTrue((versionDate != null) && !versionDate.isEmpty(),
                "'version cannot be null. Use " + VERSION_DATE_2016_05_20);

        this.versionDate = versionDate;
    }// END ComparateurApi Date

    /**
     * Instantiates a new `VisualRecognition` with API Key.
     *
     * @param versionDate The version date (yyyy-MM-dd) of the REST API to use. Specifying this value will keep your API
     *          calls from failing when the service introduces breaking changes.
     * @param apiKey the API Key
     */
    public ComparateurApi(String versionDate, String apiKey) {
        this(versionDate);
        setApiKey(apiKey);
    }// END ComparateurApi apiKey

    /**
     * Classify images.
     *
     * @param classifyOptions the {@link ClassifyOptions} containing the options for the call
     * @return the {@link ClassifiedImages} with the response
     */
    public ServiceCall<ClassifiedImages> classify(ClassifyOptions classifyOptions) {
        Validator.notNull(classifyOptions, "classifyOptions cannot be null");
        Validator.isTrue((classifyOptions.imagesFile() != null) || (classifyOptions.parameters() != null),
                "At least one of imagesFile or parameters must be supplied.");
        RequestBuilder builder = RequestBuilder.post("/v3/classify");
        builder.query(VERSION, versionDate);
        if (classifyOptions.acceptLanguage() != null) {
            builder.header("Accept-Language", classifyOptions.acceptLanguage());
        }
        MultipartBody.Builder multipartBuilder = new MultipartBody.Builder();
        multipartBuilder.setType(MultipartBody.FORM);
        if (classifyOptions.imagesFile() != null) {
            RequestBody imagesFileBody = RequestUtils.inputStreamBody(classifyOptions.imagesFile(), classifyOptions
                    .imagesFileContentType());
            multipartBuilder.addFormDataPart("images_file", classifyOptions.imagesFilename(), imagesFileBody);
        }
        if (classifyOptions.parameters() != null) {
            multipartBuilder.addFormDataPart("parameters", classifyOptions.parameters());
        }
        builder.body(multipartBuilder.build());
        return createServiceCall(builder.build(), ResponseConverterUtils.getObject(ClassifiedImages.class));
    }

}//end class
