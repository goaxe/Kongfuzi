package com.kongfuzi.teacher.events;

public class DianmingModifyEvent {
  public String callnameDetailId;
  public String statue = null;
  public DianmingModifyEvent(String callnameDetailId, String statue) {
    this.callnameDetailId = callnameDetailId;
    this.statue = statue;
  }
}
