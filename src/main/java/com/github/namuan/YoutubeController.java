package com.github.namuan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.WebAsyncTask;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

@RestController
public class YoutubeController {

  @Autowired
  YoutubeService youtubeService;

  @Autowired
  VideoFileService videoFileService;

  @Value("${request.wait.time.in.mins:5}")
  private long requestWaitTimeInMins;

  @RequestMapping("/yt")
  public WebAsyncTask<String> yt(@ModelAttribute Video video) {
    Callable<String> callable = () -> youtubeService.processVideo(video.getVideoUrl());
    return new WebAsyncTask<>(requestWaitTimeInMins * 60 * 1000, callable);
  }

  @RequestMapping("/files/{filename:.+}")
  public void downloadFile(@PathVariable("filename") String filename, HttpServletResponse response) throws IOException {
    try (InputStream is = videoFileService.getFileFromTargetDirectory(filename)) {
      FileCopyUtils.copy(is, response.getOutputStream());
      response.flushBuffer();
    }
  }

}
