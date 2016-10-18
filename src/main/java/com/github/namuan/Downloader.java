package com.github.namuan;

class Downloader {
  String generateDownloadCommand(String url, boolean downloadAudio) {
    String downloaderApplication = "youtube-dl";

    if (downloadAudio) {
      return String.format("%s -x --extract-audio --audio-format=mp3 --audio-quality=0 '%s'", downloaderApplication, url);
    }

    return String.format("%s '%s'", downloaderApplication, url);
  }
}
