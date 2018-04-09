package springboot.model;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class InputResources {
    private List<String> listpathTophoto;
    private List<String> listpathToVideo;



    public List<String> getListpathTophoto() {
        return listpathTophoto;
    }

    public void setListpathTophoto(List<String> listpathTophoto) {
        this.listpathTophoto = listpathTophoto;
    }

    public List<String> getListpathToVideo() {
        return listpathToVideo;
    }

    public void setListpathToVideo(List<String> listpathToVideo) {
        this.listpathToVideo = listpathToVideo;
    }
}
