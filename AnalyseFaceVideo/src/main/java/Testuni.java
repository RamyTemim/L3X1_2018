

import amazon.CreatCollectionFaces;
import amazon.S3operation;
import microsoft.DetectFace;
import microsoft.JsonUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Testuni {


    @Test
    public void testreadFile()
    {
        List<String> list = Arrays.asList("src/main/resources/akram_blouza.jpg",
                "src/main/resources/arnaud_Brachetti.jpeg",
                "src/main/resources/julien-smadja.png",
                "src/main/resources/mael.png",
                "src/main/resources/renaud_chevalier.jpg",
                "src/main/resources/sameh.png");
        Assert.assertEquals(list, JsonUtil.readFile(new File("src/main/resources/listePhoto.txt")));

     }



    @Test
    public void testpathToName ()
    {
        Assert.assertEquals("hocine", JsonUtil.pathToName("/home/hocine/L3X1_2018/AnalyseFaceVideo/src/main/resources/hocine.jpg"));
    }

    @Test
    public void testListFilesInBucket()
    {
    List<String> ListFile= new ArrayList<>();
    ListFile.add("a.jpg");
    ListFile.add("film1.mov");
    ListFile.add("yanis.jpg");

       Assert.assertEquals(ListFile,S3operation.ListFilesInBucket("yanisaws"));
    }





    @Test
    public void testsupprimeGuillemet()
    {
    Assert.assertEquals("yanis",JsonUtil.supprimeGuillemet("\"yanis\""));
    }

    @Test
    public void testCreateCollection ()
    {
        Assert.assertEquals("hocine", CreatCollectionFaces.CreatCollectionFace(CreatCollectionFaces.connexionIdexFace(),"hocine"));
    }

    @Test
    public void testCreateBucket ()
    {
        Assert.assertEquals("testbucket",S3operation.CreatBucket("testbucket"));
    }


}
