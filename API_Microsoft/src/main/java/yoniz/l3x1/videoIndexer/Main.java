package yoniz.l3x1.videoIndexer;

import org.json.JSONArray;
import org.json.JSONObject;

public class Main {

    public static void main(String[] args)
    {
        /*String path = "src/main/resources/film1.mov";
        String videoId = VideoIndexer.upload(path);
        //System.out.println(videoId);
        boolean bool = true;
        while(bool)
        {
            JSONObject json = VideoIndexer.getProcessingState(videoId.substring(1,videoId.length()-1));
            System.out.println(json);
            if(json.get("state")=="Processed" && json.get("state")=="Failed")
                bool = false;
        }


        System.out.println(VideoIndexer.getProcessingState(videoId.substring(1,videoId.length()-1)));
        System.out.println(VideoIndexer.getProcessingState("4550037e04"));

        //System.out.println(VideoIndexer.getBreakdown("4550037e04"));
        */

        String videoId = "7c26a90418";

        JSONObject json = VideoIndexer.getBreakdown(videoId);
        //Pour obtenir le nombre de visage extrait de la vidéo
        JSONArray faces = VideoIndexer.getFacesFromVideos(json);
        System.out.println(faces.length());
        System.out.println(faces.getJSONObject(0).get("thumbnailId"));
        System.out.println(faces.getJSONObject(1).get("thumbnailId"));




    }
}



