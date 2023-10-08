package com.abhijitmahato.farmart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.abhijitmahato.farmart.models.UserFile;


@Repository
public interface FileDao extends JpaRepository<UserFile, Long> {
  @Query("SELECT f FROM UserFile f WHERE f.shortUrl = :shortUrl")
  public UserFile findByShortUrl(String shortUrl);

  public UserFile findByFileName(String fileName);
}
