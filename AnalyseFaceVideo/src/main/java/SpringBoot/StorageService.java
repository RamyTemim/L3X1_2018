package SpringBoot;

import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class StorageService {



    private final Path path = Paths.get("src/main/resources");
    public  void storFile(MultipartFile multipartFile)
    {
       try{
        Files.copy(multipartFile.getInputStream(), path.resolve(multipartFile.getOriginalFilename()));
       }catch (Exception e){
           throw new RuntimeException("Fail");
       }
    }


    public void deletFile()
    {
        FileSystemUtils.deleteRecursively(path.toFile());
    }


}
