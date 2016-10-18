package com.github.namuan;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class YoutubeService {

  @Value("${download.audio:false}")
  private boolean downloadAudio;

  @Autowired
  CommandRunnerService commandRunnerService;

  private Downloader downloader = new Downloader();

  @Autowired
  VideoFileService videoFileService;

  String processVideo(String url) {
    String filename = downloadAndConvertVideo(url);
    return videoFileService.moveFileToTargetDirectory(filename);
  }

  String downloadAndConvertVideo(String url) {
    String downloadCommand = generateDownloadCommand(url);
    String commandOutput = commandRunnerService.runCommand(downloadCommand);
    return getFilenameFromOutput(commandOutput);
  }

  String getFilenameFromOutput(String commandOutput) {
    final String[] lines = commandOutput.split("\n");
    final Pattern downloadPattern = getRegexPattern("download.*Destination:\\s(.*)$");
    final Pattern ffmpegPattern = getRegexPattern("ffmpeg.*Destination:\\s(.*)$");

    String filename = "";

    for (String line : lines) {

      final Optional<String> downloadedFilename = getFilenameIfMatchFound(downloadPattern, line);
      final Optional<String> ffmpegFilename = getFilenameIfMatchFound(ffmpegPattern, line);

      filename = ffmpegFilename.orElse(downloadedFilename.orElse(filename));
    }

    return filename;
  }

  private Pattern getRegexPattern(String regex) {
    return Pattern.compile(regex);
  }

  private Optional<String> getFilenameIfMatchFound(Pattern pattern, String line) {
    final Matcher matcher = pattern.matcher(line);
    if (matcher.find()) {
      return Optional.of(matcher.group(1));
    }

    return Optional.empty();
  }

  private String generateDownloadCommand(String url) {
    return downloader.generateDownloadCommand(url, downloadAudio);
  }
}
