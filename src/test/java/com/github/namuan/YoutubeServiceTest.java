package com.github.namuan;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

public class YoutubeServiceTest {

  @Mock
  CommandRunnerService commandRunnerService;

  @Mock
  VideoFileService videoFileService;

  @InjectMocks
  YoutubeService youtubeService = new YoutubeService();

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testDownloadAndConvertFile() throws IOException {
    // given
    String commandOutput = "[download] Destination: video file.webm\n";

    String url = "http://example.com/video";
    Mockito.when(commandRunnerService.runCommand(Mockito.anyString())).thenReturn(commandOutput);

    // when
    String convertedFilename = youtubeService.downloadAndConvertVideo(url);

    // then
    Assert.assertEquals("video file.webm", convertedFilename);
  }

  @Test
  public void testFilenameFromOutput() {
    // given
    String commandOutput = "[youtube] abcd: Downloading webpage\n" +
            "[youtube] abcd: Downloading video info webpage\n" +
            "[youtube] abcd: Extracting video information\n" +
            "[youtube] abcd: Downloading MPD manifest\n" +
            "[download] abcd: saujaskjda 23423 --- iusaidsadija.webm\n" +
            "[download] 100% of 3.79MiB in 00:00\n" +
            "[ffmpeg] Destination: saujaskjda 23423 --- iusaidsadija.mp3";

    // when
    String filename = youtubeService.getFilenameFromOutput(commandOutput);

    // then
    Assert.assertEquals("saujaskjda 23423 --- iusaidsadija.mp3", filename);
  }

  @Test
  public void testFilenameForDownloadedFileIfAudioIsNotExtracted() {
    // given
    String commandOutput = "[youtube] abcd: Downloading webpage\n" +
            "[youtube] abcd: Downloading video info webpage\n" +
            "[youtube] abcd: Extracting video information\n" +
            "[youtube] abcd: Downloading MPD manifest\n" +
            "[download] Destination: asdas-dwnload 23423 --- iusaidsadija.webm\n" +
            "[download] 100% of 3.79MiB in 00:00\n";

    // when
    String filename = youtubeService.getFilenameFromOutput(commandOutput);

    // then
    Assert.assertEquals("asdas-dwnload 23423 --- iusaidsadija.webm", filename);
  }

  @Test
  public void testProcessVideo() {
    // given
    String url = "http://example.com/video";
    String expectedCommand = String.format("youtube-dl '%s'", url);

    String commandOutput = "[download] Destination: video file.webm\n";

    Mockito.when(commandRunnerService.runCommand(Mockito.anyString())).thenReturn(commandOutput);

    // when
    youtubeService.processVideo(url);

    // then
    Mockito.verify(commandRunnerService, Mockito.times(1)).runCommand(expectedCommand);
    Mockito.verify(videoFileService, Mockito.times(1)).moveFileToTargetDirectory("video file.webm");
  }

}