package com.github.namuan;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.io.InputStream;

public class YoutubeControllerTest {

  @Mock
  YoutubeService youtubeService;

  @Mock
  VideoFileService videoFileService;

  @InjectMocks
  YoutubeController youtubeController = new YoutubeController();

  MockMvc mockMvc;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(youtubeController).build();

    ReflectionTestUtils.setField(youtubeController, "requestWaitTimeInMins", 5);
  }

  @Test
  public void testPostRequestToDownloadVideo() throws Exception {
    // given
    String url = "http://example.com/video";

    Mockito.when(youtubeService.processVideo(Mockito.anyString())).thenReturn(url);

    // when
    final MvcResult mvcResult = mockMvc.perform
            (post("/yt")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .param("videoUrl", url)
            )
            .andExpect(status().isOk())
            .andExpect(request().asyncStarted())
            .andReturn();

    mockMvc.perform(asyncDispatch(mvcResult))
            .andExpect(status().isOk())
            .andExpect(content().string(url));

    // then
    Mockito.verify(youtubeService, Mockito.times(1)).processVideo(url);
  }

  @Test
  public void testGetRequestToDownloadVideo() throws Exception {
    // given
    String filename = "videofile";
    InputStream is = IOUtils.toInputStream("Terminator", "UTF-8");
    Mockito.when(videoFileService.getFileFromTargetDirectory(Mockito.anyString())).thenReturn(is);

    // when
    final MvcResult result = mockMvc.perform
            (
                    get("/files/" + filename)
            )
            .andExpect(status().isOk())
            .andReturn();


    // then
    String response = result.getResponse().getContentAsString();
    Assert.assertEquals("Terminator", response);

    Mockito.verify(videoFileService, Mockito.times(1)).getFileFromTargetDirectory(filename);

  }

}