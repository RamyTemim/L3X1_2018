

import amazon.S3operation;
import microsoft.DetectFace;
import microsoft.JsonUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
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
    public void testreadFile() throws IOException {
       List<String> listfile = new ArrayList<>();
       listfile.add("src/main/resources/yoni.jpg");
        listfile.add("src/main/resources/jordan.jpg");
        listfile.add("src/main/resources/56007.HR.jpg");
        listfile.add("src/main/resources/hocine.jpg");
       Assert.assertEquals(listfile,JsonUtil.readFile("/home/hocine/L3X1_2018/AnalyseFaceVideo/src/main/resources/listePhoto.txt") );
    }



    @Test
    public void testsupprimeGuillemet()
    {
    Assert.assertEquals("yanis",JsonUtil.supprimeGuillemet("\"yanis\""));
    }



}
