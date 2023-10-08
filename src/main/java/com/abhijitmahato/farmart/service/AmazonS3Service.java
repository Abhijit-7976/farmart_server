package com.abhijitmahato.farmart.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.abhijitmahato.farmart.models.UserFile;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.util.IOUtils;

import jakarta.annotation.PostConstruct;

@Service
public class AmazonS3Service {
  @Autowired
  FileService fileService;

  @Value("${accessKey}")
  private String accessKey;

  @Value("${secretKey}")
  private String secretKey;

  @Value("${bucketName}")
  private String bucketName;

  @Value("${region}")
  private String region;

  @Value("${serverUrl}")
  private String serverUrl;

  private AmazonS3 amazonS3Client;

  public AmazonS3 getAmazonS3Client() {
    return amazonS3Client;
  }

  @PostConstruct
  private void initializeAmazon() {
    AWSCredentials awsCredentials = new BasicAWSCredentials(accessKey, secretKey);

    this.amazonS3Client = AmazonS3ClientBuilder.standard()
        .withRegion(region)
        .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
        .build();
  }
  
  private String generateFileName(MultipartFile multipartFile) {
    String fileName = multipartFile.getOriginalFilename();
    long currentTime = System.currentTimeMillis();
    if (fileName != null)
      return currentTime + "_" + fileName.replace(" ", "_");
    return "";
  }
  
  private File convertMultipartToFile(MultipartFile file) throws IOException {
    File convFile = new File(file.getOriginalFilename());
    FileOutputStream fos = new FileOutputStream(convFile);
    fos.write(file.getBytes());
    fos.close();
    return convFile;
  }

  public String uploadFile(MultipartFile multipartFile) {
    String key = "";
    try {
      File file = convertMultipartToFile(multipartFile);
      key = generateFileName(multipartFile);
      amazonS3Client.putObject(bucketName, key, file);
      String url = serverUrl + "/api/files/download/" + key;
      String shortUrl = fileService.generateShortUrl(url);

      UserFile newFile = new UserFile(url, shortUrl, multipartFile.getOriginalFilename(), key);
      fileService.saveOrUpdateFile(newFile);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return key;
  }

  public byte[] getFile(String key) {
    try {
      S3Object object = amazonS3Client.getObject(bucketName, key);
      S3ObjectInputStream objectContent = object.getObjectContent();
      return IOUtils.toByteArray(objectContent);
    } catch (Exception e) {
      e.getStackTrace();
      return null;
    }
  }

  public Boolean deleteFile(String key) {
    try {
      amazonS3Client.deleteObject(bucketName, key);
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }
}