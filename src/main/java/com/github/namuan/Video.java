package com.github.namuan;

public class Video {
  private String videoUrl;

  public Video() {
    this.videoUrl = "";
  }

  public Video(String videoUrl) {
    this.videoUrl = videoUrl;
  }

  public String getVideoUrl() {
    return videoUrl;
  }

  public void setVideoUrl(String videoUrl) {
    this.videoUrl = videoUrl;
  }

}
