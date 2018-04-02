package SpringBoot.model;


import java.util.List;

public class Persons {
   private String name;
   private List<String> videos;

   public Persons(String name, List<String> videos) {
      this.name = name;
      this.videos = videos;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public List<String> getVideos() {
      return videos;
   }

   public void setVideos(List<String> videos) {
      this.videos = videos;
   }


}
