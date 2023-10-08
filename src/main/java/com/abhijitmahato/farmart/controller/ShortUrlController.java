package com.abhijitmahato.farmart.controller;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.abhijitmahato.farmart.models.UserFile;
import com.abhijitmahato.farmart.service.FileService;

@CrossOrigin(origins = "http://localhost:5173/")
@RestController
public class ShortUrlController {
  @Autowired
  FileService fileService;

  @Value("${serverUrl}")
  private String serverUrl;

  @GetMapping("/s/{hex}")
  public ResponseEntity<Object> getLongUrl(@PathVariable("hex") String hex) {
    String shortUrl = serverUrl + "/s/" + hex;
    UserFile foundFile = fileService.getFileByShortUrl(shortUrl);

    if (foundFile != null) {
      String longUrl = foundFile.getLongUrl();
      System.out.println(longUrl);
      
      try {
        URI longUri = new URI(longUrl);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(longUri);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return new ResponseEntity<Object>(HttpStatus.BAD_REQUEST);
  }
  
}