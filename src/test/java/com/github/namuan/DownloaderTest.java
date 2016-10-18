package com.github.namuan;

import org.junit.Assert;
import org.junit.Test;

public class DownloaderTest {

  Downloader downloader = new Downloader();

  @Test
  public void testCommandToDownloadVideoAndAudio() {
    // given
    String url = "http://www.youtube.com/somevideo";

    // when
    String downloadCommand = downloader.generateDownloadCommand(url, false);

    // then
    Assert.assertEquals(String.format("youtube-dl '%s'", url), downloadCommand);
  }

  @Test
  public void testCommandToDownloadVideoIfDownloadAudioFlagIsNotSet() {
    // given
    String url = "http://www.youtube.com/somevideo";

    // when
    String downloadCommand = downloader.generateDownloadCommand(url, true);

    // then
    Assert.assertEquals(String.format("youtube-dl -x --extract-audio --audio-format=mp3 --audio-quality=0 '%s'", url), downloadCommand);
  }

}