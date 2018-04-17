package microsoft;

import org.json.JSONObject;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

/**
 * Test Unitaire de la classe FaceList
 */
public class FaceListTest {

    @Test
    public void getFaceOflist() {
        FaceList.createFaceList("test", "12");
        assertEquals(FaceList.getFaceOflist("12"), new JSONObject().put("userData", "yoni").put("name", "test").put("persistedFaces", new ArrayList<String>()).put("faceListId", "12"));
        FaceList.deleteFaceList("12");
    }
}