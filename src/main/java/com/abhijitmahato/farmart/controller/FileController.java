package com.abhijitmahato.farmart.controller;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.abhijitmahato.farmart.models.FileDto;
import com.abhijitmahato.farmart.models.UserFile;
import com.abhijitmahato.farmart.service.AmazonS3Service;
import com.abhijitmahato.farmart.service.FileService;

@CrossOrigin(origins = "https://elaborate-marzipan-b473a7.netlify.app/")
@RestController
@RequestMapping("/api/files")
public class FileController {
  
  @Autowired
  AmazonS3Service amazonS3Service;

  @Autowired
  FileService fileService;

  @PostMapping("/upload")
  public ResponseEntity<Object> uploadFile(@RequestBody MultipartFile file) {
    String fileUrl = amazonS3Service.uploadFile(file);
    return new ResponseEntity<Object>(fileUrl, HttpStatus.OK);
  }

  @GetMapping
  public ResponseEntity<Object> getAllFiles() {
    List<UserFile> foundFiles = fileService.getAllFiles();
    List<FileDto> userFiles = new ArrayList<>();

    for (UserFile file : foundFiles) {
      FileDto fileDetail = new FileDto();
      fileDetail.setId(file.getFileId());
      fileDetail.setFileName(file.getFileName());
      fileDetail.setShortUrl(file.getShortUrl());
      userFiles.add(fileDetail);
    }

    if (foundFiles != null) {
      return new ResponseEntity<Object>(userFiles, HttpStatus.OK);
    }
    return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
  }

  @GetMapping("/download/{key}")
  public ResponseEntity<Object> downloadFile(@PathVariable("key") String key) {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-type", MediaType.ALL_VALUE);

    String fileOriginalName = fileService.getFileOriginalName(key);
    headers.add("Content-Disposition", "attachment; filename=" + fileOriginalName);

    byte[] file = amazonS3Service.getFile(key);
    if (file != null) {
      return new ResponseEntity<Object>(file, headers, HttpStatus.OK);
    }
    return new ResponseEntity<Object>(HttpStatus.NOT_FOUND);
  }

  @DeleteMapping("/delete/{id}")
  public ResponseEntity<Object> deleteFile(@PathVariable("id") Long id) {
    UserFile foundFile = fileService.getFileById(id);
    if (foundFile != null) {
      if (amazonS3Service.deleteFile(foundFile.getS3Key())) {
        fileService.deleteFileById(id);
        return new ResponseEntity<Object>(HttpStatus.OK);
      }
    }
    return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
  }

}
