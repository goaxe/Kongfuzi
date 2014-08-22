package com.kongfuzi.teacher.events;

public class RefreshDianmingInitEvent {
  public String rate;
  public String callnameListId;
  public String className;
  public String latestDate;
  public Integer latestBj;
  public Integer latestCd;
  public Integer latestD;
  public Integer latestKk;
  public Integer latestSj;
  public Integer latestZt;
  public Integer latestAll;

  public RefreshDianmingInitEvent(String rate, String callnameListId, String className, String latestDate, Integer latestBj, Integer latestCd,
      Integer latestD, Integer latestKk, Integer latestSj, Integer latestZt, Integer latestAll) {
    this.rate = rate;
    this.callnameListId = callnameListId;
    this.className = className;
    this.latestDate = latestDate;
    this.latestBj = latestBj;
    this.latestCd = latestCd;
    this.latestD = latestD;
    this.latestKk = latestKk;
    this.latestSj = latestSj;
    this.latestZt = latestZt;
    this.latestAll = latestAll;
  }

}
