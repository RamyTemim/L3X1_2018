package useful;

import org.junit.Test;
import useful.JsonUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class JsonUtilTest {

    @Test
    public void readFile(){
        File file = new File("fileTest");
        try {
            assertEquals(JsonUtil.readFile(file), Arrays.asList(new File ("file1").getAbsolutePath(),new File ("file2").getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void pathToName() {
        assertEquals(JsonUtil.pathToName("src/main/java/yoni.jpg"),"yoni");
    }

    @Test
    public void supprimeGuillemet() {
        assertEquals(JsonUtil.supprimeGuillemet("\"Bonjour\""),"Bonjour" );
    }

    @Test
    public void getListLienVideo() {
        try {
            List<List<String>> list = new ArrayList<List<String>>();
            list.add(Arrays.asList("https://www.videoindexer.ai/api/Thumbnail/6e1d3b7703/81e9bf4b-d308-442d-b7e9-07a8c7de79b2"));
            list.add(Arrays.asList("https://www.videoindexer.ai/api/Thumbnail/35147c0c73/03596b0a-6c40-40b5-83fc-40140c48b64e", "https://www.videoindexer.ai/api/Thumbnail/35147c0c73/05bf238a-a2a1-409d-b612-443399b3f900"));
            assertEquals(JsonUtil.getListLienVideo(Arrays.asList("6e1d3b7703", "35147c0c73")),list );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}