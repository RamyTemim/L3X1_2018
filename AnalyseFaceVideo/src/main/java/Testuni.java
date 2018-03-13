

import amazon.S3operation;
import microsoft.DetectFace;
import microsoft.JsonUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class Testuni {

    @Test
    public void testpathToName ()
    {
        Assert.assertEquals("hocine", JsonUtil.pathToName("/home/hocine/L3X1_2018/AnalyseFaceVideo/src/main/resources/hocine.jpg"));
    }

    @Test
    public void testListFilesInBucket()
    {
    List<String> ListFile= new ArrayList<String>();
    ListFile.add("a.jpg");
    ListFile.add("film1.mov");
    ListFile.add("yanis.jpg");

       Assert.assertEquals(ListFile,S3operation.ListFilesInBucket("yanisaws"));
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
    Assert.assertEquals("yanis",JsonUtil.supprimeGuillemet("\"yanis\""));
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
