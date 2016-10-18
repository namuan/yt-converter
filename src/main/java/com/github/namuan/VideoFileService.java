package com.github.namuan;

import javaslang.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Service
class VideoFileService {

  private static Logger logger = LoggerFactory.getLogger(VideoFileService.class);

  @Value("${video.directory}")
  String videoDirectory;

  String moveFileToTargetDirectory(String filename) {
    final File sourceFile = new File(filename);
    final File targetFile = new File(videoDirectory + filename);

    if (!sourceFile.renameTo(targetFile)) {
      throw new IllegalStateException(
              String.format("Unable to rename file from %s to %s",
                      sourceFile.getAbsolutePath(),
                      targetFile.getAbsolutePath()
              ));
    }

    logger.info("Completed downloading file to " + targetFile.getAbsolutePath());

    return encodeFilename(filename);
  }

  InputStream getFileFromTargetDirectory(String filename) {
    final Try<FileInputStream> inputStream =
            Try.of(() -> new FileInputStream(new File(videoDirectory + filename)));

    return inputStream.getOrElseThrow(e -> {
      throw new RuntimeException("Unable to open file: " + filename, e);
    });
  }

  private String encodeFilename(String filename) {
    final Try<String> maybeEncodedString = Try.of(() -> UriUtils.encodePath(filename, "UTF-8"));
    return maybeEncodedString.getOrElse(filename);
  }
}
