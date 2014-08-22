package com.kongfuzi.teacher.events;

public class DianmingEvent {
  public String orgStudentId;
  public String status;
  
  public DianmingEvent(String orgStudentId, String status) {
    this.orgStudentId = orgStudentId;
    this.status = status;
  }

}
