
import microsoft.JsonUtil;
import org.junit.Assert;
import org.junit.Test;
public class Testuni {
    @Test
    public void testpathToName ()
    {
        Assert.assertEquals("hocine", JsonUtil.pathToName("/home/hocine/L3X1_2018/AnalyseFaceVideo/src/main/resources/hocine.jpg"));
    }



}
