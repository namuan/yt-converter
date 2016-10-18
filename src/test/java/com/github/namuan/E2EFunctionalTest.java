package com.github.namuan;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class E2EFunctionalTest {

  @Autowired
  YoutubeService youtubeService;

  @Mock
  CommandRunnerService commandRunnerService;

  @Test
  public void testService() throws IOException {
    // given
    String commandOutput = "[download] Destination: video file.webm\n";
    Mockito.when(commandRunnerService.runCommand(Mockito.anyString())).thenReturn(commandOutput);

    youtubeService.commandRunnerService = commandRunnerService;

    // when
    final String filename = youtubeService.downloadAndConvertVideo("http://www.youtube.com/video");

    // then
    Assert.assertEquals("video file.webm", filename);
    Mockito.verify(commandRunnerService, Mockito.times(1)).runCommand("youtube-dl 'http://www.youtube.com/video'");
  }
}
