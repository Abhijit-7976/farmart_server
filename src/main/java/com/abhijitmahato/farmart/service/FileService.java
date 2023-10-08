package com.abhijitmahato.farmart.service;

import java.security.MessageDigest;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.abhijitmahato.farmart.models.UserFile;
import com.abhijitmahato.farmart.repository.FileDao;

@Service
public class FileService {
  @Value("${serverUrl}")
  private String serverUrl;

  @Autowired
  FileDao fileDao;

  public UserFile getFileByShortUrl(String shortUrl) {
    return fileDao.findByShortUrl(shortUrl);
  }

  public List<UserFile> getAllFiles() {
    return fileDao.findAll();
  }

  public void saveOrUpdateFile(UserFile myFile) {
    fileDao.save(myFile);
  }

  public void deleteFileByShortUrl(String shortUrl) {
    UserFile foundFile = fileDao.findByShortUrl(shortUrl);
    if (foundFile != null) {
      fileDao.deleteById(foundFile.getFileId());
    }
  }

  public UserFile getFileById(Long id) {
    Optional<UserFile> foundFile = fileDao.findById(id);
    if (foundFile.isPresent()) {
      return foundFile.get();
    }
    return null;
  }

  public UserFile getFileByFileName(String fileName) {
    return fileDao.findByFileName(fileName);
  }

  public String generateShortUrl(String url) {
    MessageDigest md;
    try {
      md = MessageDigest.getInstance("MD5");
      md.update(url.getBytes());
      byte[] bytes = md.digest();
      String hex = Hex.encodeHexString(bytes).substring(0, 7);
      return serverUrl + "/s/" + hex;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public String getFileOriginalName(String fileName) {
    String[] fileNameSplitArray = fileName.split("_");
    String originalFileName = "";

    for (int i = 1; i < fileNameSplitArray.length; i++) {
      originalFileName = originalFileName + fileNameSplitArray[i];
    }
    return originalFileName;
  }

  public void deleteFileById(Long id) {
    fileDao.deleteById(id);
  }

}
