package ru.netology.cloudstorage.service.impl;

import ru.netology.cloudstorage.entities.CloudFile;
import ru.netology.cloudstorage.entities.User;
import ru.netology.cloudstorage.repositories.CloudFilesRepository;
import ru.netology.cloudstorage.repositories.UserRepository;
import ru.netology.cloudstorage.util.exceptions.CloudFileException;
import ru.netology.cloudstorage.util.exceptions.UsernameNotFoundException;
import ru.netology.cloudstorage.security.jwt.JwtUtils;
import ru.netology.cloudstorage.service.CloudFilesService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


@Transactional(readOnly = true)
@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j

public class CloudFilesServiceImpl implements CloudFilesService {

    CloudFilesRepository cloudFilesRepository;
    UserRepository userRepository;
    JwtUtils jwtUtils;
    String tokenPrefix = "Bearer ";

    @Autowired
    public CloudFilesServiceImpl(CloudFilesRepository cloudFilesRepository, UserRepository userRepository,
                                 JwtUtils jwtUtils) {
        this.cloudFilesRepository = cloudFilesRepository;
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CloudFile upload(String authToken, String filename, MultipartFile resource) throws IOException {
        User user = getUserByToken(authToken);
        if (resource.isEmpty()) {
            log.info("the file not found");
            throw new CloudFileException("Error input data");
        }
        CloudFile file = CloudFile.builder()
                .name(filename)
                .fileType(resource.getContentType())
                .size(resource.getSize())
                .bytes(resource.getBytes())
                .owner(user)
                .build();
        Optional<CloudFile> checkFilename = cloudFilesRepository.findCloudFileByName(file.getName());
        if (checkFilename.isPresent()) {
            log.error("the file with a such name already exist");
            throw new CloudFileException("the file with a such name already exist");

        }
        cloudFilesRepository.save(file);
        log.info("user successfully uploaded a file {}", filename);
        return file;

    }

    @Override
    public CloudFile download(String filename, String authToken) {
        User user = getUserByToken(authToken);
        CloudFile file = cloudFilesRepository.findCloudFileByNameAndOwner(filename, user)
                .orElseThrow(() -> new CloudFileException("The file not found"));
        return file;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(String filename, String authToken) {

        CloudFile cloudFile = getCloudFileByName(filename, authToken);
        cloudFilesRepository.deleteById(cloudFile.getId());
        log.info("File {} was deleted successfully", filename);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void renameFile(String authToken, String currentFileName,
                           String newFileName) {
        CloudFile file = getCloudFileByName(currentFileName, authToken);
        file.setName(newFileName);
        log.info("File's name was successfully changed");
        cloudFilesRepository.saveAndFlush(file);
    }


    private User getUserByToken(String authToken) {
        String username = jwtUtils.validateTokenRetrieveClaim(authToken.replace
                (tokenPrefix, ""));
        return userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username
                        + " not found"));
    }

    @Override
    public List<CloudFile> getAllCloudFileForUser(String authToken) {
        User user = getUserByToken(authToken);
        return cloudFilesRepository.findAllByOwnerOrderByCreatedDesc(user);
    }


    private CloudFile getCloudFileByName(String filename, String authToken) {
        User user = getUserByToken(authToken);
        CloudFile cloudFile = cloudFilesRepository.findCloudFileByNameAndOwner(filename, user)
                .orElseThrow(() -> new CloudFileException("File can't be found for user:  "
                        + user.getLogin()
                ));
        return cloudFile;
    }

}
