package com.kongfuzi.teacher.events;

public class RefreshPingyuEvent {
  public Integer tag;
  public String content;
  public RefreshPingyuEvent(Integer tag, String content) {
    this.tag = tag;
    this.content = content;
  }

}
