package ru.netology.cloudstorage.service;


import ru.netology.cloudstorage.entities.CloudFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface CloudFilesService {

    CloudFile upload(String authToken, String filename, MultipartFile resource) throws IOException;

    List<CloudFile> getAllCloudFileForUser(String authToken);

    CloudFile download(String filename, String authToken);

    void deleteFile(String filename, String authToken);


    void renameFile(String authToken, String currentFileName,
                    String newFileName);


}
