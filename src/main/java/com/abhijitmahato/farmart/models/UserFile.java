package com.abhijitmahato.farmart.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_files")
public class UserFile {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "file_id")
  private Long fileId;

  @Column(name = "long_url")
  private String longUrl;

  @Column(name = "short_url")
  private String shortUrl;

  @Column(name = "original_file_name")
  private String fileName;

  @Column(name = "s3_key")
  private String s3Key;

  public UserFile() {
  }

  public UserFile(String longUrl, String shortUrl, String fileName, String s3Key) {
    this.longUrl = longUrl;
    this.shortUrl = shortUrl;
    this.fileName = fileName;
    this.s3Key = s3Key;
  }

  public Long getFileId() {
    return fileId;
  }

  public void setFileId(Long fileId) {
    this.fileId = fileId;
  }

  public String getLongUrl() {
    return longUrl;
  }

  public void setLongUrl(String longUrl) {
    this.longUrl = longUrl;
  }

  public String getShortUrl() {
    return shortUrl;
  }

  public void setShortUrl(String shortUrl) {
    this.shortUrl = shortUrl;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  public String getS3Key() {
    return s3Key;
  }

  public void setS3Key(String s3Key) {
    this.s3Key = s3Key;
  }

  @Override
  public String toString() {
    return "UserFile [fileId=" + fileId + ", longUrl=" + longUrl + ", shortUrl=" + shortUrl + ", fileName="
        + fileName + ", s3Key=" + s3Key + "]";
  }

}
