

import amazon.S3operation;
import microsoft.DetectFace;
import microsoft.JsonUtil;
import org.junit.Assert;
import org.junit.Test;


public class Testuni {
    @Test
    public void testpathToName ()
    {
        Assert.assertEquals("hocine", JsonUtil.pathToName("/home/hocine/L3X1_2018/AnalyseFaceVideo/src/main/resources/hocine.jpg"));
    }

    @Test
    public void testListFilesInBucket()
    {
       Assert.assertEquals("yanis" +"yoni",S3operation.ListFilesInBucket("yanisaws"));
    }

    @Test
    public void testdetect()
    {
        Assert.assertEquals(" {\n" +
                "\t\"age\":100,\n" +
                "\t\"name\":\"mkyong.com\",\n" +
                "} ", DetectFace.detect("/Users/yanis/Desktop/ProjetL3X1/L3X1_2018/AnalyseFaceVideo/src/main/resources/yanis.jpg","name, age",true));
    }

    @Test
    public void testindSimilar()
    {
    Assert.assertEquals("[{ \"A\" : \"IN\", \"B\" : \"DL\"},{ \"A\" : \"US\", \"B\" : \"KA\"}]",DetectFace.findSimilar("yanislist","yan01200"));
    }

    @Test
    public void testJSONObject()
    {

    }
    @Test
    public void testreadFile()
    {

    }

    @Test
    public void testhttpToJsonObject()
    {
    /*Assert.assertEquals("{\n" +
            "    \"names\": [\n" +
            "        {\n" +
            "            \"name\": \"Zachary\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"Wyatt\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"name\": \"William\"\n" +
            "        }\n" +
            "    ]\n" +
            "}",JsonUtil.httpToJsonObject(""));*/
    }

    @Test
    public void teststringToJson()
    {

    }

    @Test
    public void testsupprimeGuillemet()
    {

    }

    @Test
    public void testgetListLienVideo()
    {

    }

    @Test
    public void testuploadVideo()
    {

    }

    @Test
    public void testdetectFaceWithVideoAndPhoto()
    {

    }
    @Test
    public void testgetFaceList()
    {

    }

    @Test
    public void testgetFaceOflist()
    {

    }

    @Test
    public void testgetProcessingState()
    {

    }


}
