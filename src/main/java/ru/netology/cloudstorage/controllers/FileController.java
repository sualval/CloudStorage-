package ru.netology.cloudstorage.controllers;


import ru.netology.cloudstorage.entities.CloudFile;
import ru.netology.cloudstorage.dto.entity.CloudFileDto;
import ru.netology.cloudstorage.service.CloudFilesService;
import ru.netology.cloudstorage.util.mapper.CloudFileMapper;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@Slf4j
@RequestMapping("/")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FileController {

    CloudFilesService cloudFilesService;
    CloudFileMapper cloudFileMapper;


    @PostMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> upload(@RequestHeader("auth-token") String authToken,
                                         @RequestParam("filename") String filename, MultipartFile file) throws IOException {

        CloudFile cloudFile = cloudFilesService.upload(authToken, filename, file);
        CloudFileDto cloudFileDto = cloudFileMapper.fromCloudFileToDto(cloudFile);

        return new ResponseEntity<>(cloudFileDto, HttpStatus.OK);


    }

    @PutMapping("/file")
    public ResponseEntity<String> editFileName(@RequestHeader("auth-token") String authToken,
                                               @RequestParam("filename") String oldFileName,
                                               @RequestBody String newFileName) {
        cloudFilesService.renameFile(authToken, oldFileName, newFileName);
        return new ResponseEntity<>("Success upload", HttpStatus.OK);
    }


    @GetMapping("/file")
    public ResponseEntity<?> download(@RequestParam("filename") String filename,
                                      @RequestHeader("auth-token") String authToken) {

        CloudFile cloudFile = cloudFilesService.download(filename, authToken);
        if (cloudFile != null) {
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + cloudFile.getName())
                    .body(cloudFile);
        } else {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<?> getAllCloudFiles(@RequestHeader("auth-token") String authToken,
                                              @RequestParam("limit") int limit) {

        List<CloudFileDto> cloudFileDtos = cloudFilesService.getAllCloudFileForUser(authToken)
                .stream()
                .limit(limit)
                .map(cloudFileMapper::fromCloudFileToDto)
                .collect(Collectors.toList());
        if (limit <= 0) {
            return ResponseEntity.badRequest().body("Error input data");
        }

        if (cloudFileDtos.size() > 3) {
            return ResponseEntity.internalServerError().body("Error getting file list");
        }

        return ResponseEntity.ok(cloudFileDtos);

    }

    @DeleteMapping("/file")
    public ResponseEntity<?> deleteCloudFile(@RequestParam("filename") String filename,
                                             @RequestHeader("auth-token") String authToken) {
        cloudFilesService.deleteFile(filename, authToken);

        return ResponseEntity.ok("Success deleted");

    }

}
