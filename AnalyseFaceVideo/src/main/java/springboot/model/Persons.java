package springboot.model;


import java.util.List;

public class Persons {
    private  String name;
    private List<String> videos;

   public Persons() {
  //instanci une personne
   }

    public void setName(String name) {
        this.name = name;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
    }

    public String getName() {
        return name;
    }

    public List<String> getVideos() {
        return videos;
    }
}
