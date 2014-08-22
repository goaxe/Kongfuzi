package com.kongfuzi.student.internal;

public class Constants {

  public static final String HOME_FRAGMENT_TAG = "HOME_FRAGMENT_TAG";
  public static final String DAXUE_FRAGMENT_TAG = "DAXUE_FRAGMENT_TAG";
  public static final String JIAOWU_FRAGMENT_TAG = "JIAOWU_FRAGMENT_TAG";
  public static final String JIGOU_FRAGMENT_TAG = "JIGOU_FRAGMENT_TAG";
  public static final String MYDAXUE_FRAGMENT_TAG = "MYDAXUE_FRAGMENT_TAG";
  public static final String MYGUANZHU_FRAGMENT_TAG = "MYGUANZHU_FRAGMENT_TAG";
  public static final String MYHOME_FRAGMENT_TAG = "MYHOME_FRAGMENT_TAG";
  public static final String MYJIGOU_FRAGMENT_TAG = "MYJIGOU_FRAGMENT_TAG";
  public static final String MYKECHENG_FRAGMENT_TAG = "MYKECHENG_FRAGMENT_TAG";
  public static final String SETTING_FRAGMENT_TAG = "SETTING_FRAGMENT_TAG";

  // for prefs
  public static final String SECRET_KEY = "SECRET_KEY";

  // for url
  public static final String verify_code = "http://182.92.158.34:3378/apps/vcode/send";

  public static final String register_url = "http://182.92.158.34:3378/apps/student/account/register";
  public static final String login_url = "http://182.92.158.34:3378/apps/student/account/login";
  public static final String logout_url = "http://182.92.158.34:3378/apps/student/account/logout";
  public static final String resetpwd_url = "http://182.92.158.34:3378/apps/student/account/resetpwd";
  public static final String query_classes_list = "http://182.92.158.34:3378/apps/student/class/queryall";
  public static final String query_students_list = "http://182.92.158.34:3378/apps/student/student/queryall";

  /*
   * 点名-读取历史点名列表（按照创建时间倒序排列）
   * 
   * @params 请求 secretkey 登录后的密钥 public static final String 类型 classid 班级ID
   * public static final String 类型 start 分页（从第几个开始） Int类型 limit 分页（取多少） Int类型
   */
  public static final String query_dianming_list = "http://182.92.158.34:3378/apps/student/callname/list";
  public static final String query_dianming_detail = "http://182.92.158.34:3378/apps/student/callname/detail";
  public static final String dianming_detail_list = "http://182.92.158.34:3378/apps/student/callname/list";
  public static final String submit_dianming = "http://182.92.158.34:3378/apps/student/callname/submit";
  public static final String image_url = "http://182.92.158.34:3378/apps/file/download?fileID=";
  public static final String image_upload = "http://182.92.158.34:3378/apps/file/upload";
  public static final String multiple_image_upload = "http://182.92.158.34:3378/apps/file/uploads";

  // 成绩
  public static final String query_exam_list = "http://182.92.158.34:3378/apps/student/exam/list";
  public static final String exam_detail = "http://182.92.158.34:3378/apps/student/exam/detail";
  public static final String submit_exam_chengji = "http://182.92.158.34:3378/apps/student/exam/submit";
  public static final String exam_score_modify = "http://182.92.158.34:3378/apps/student/exam/modify";

  public static final String muban_list = "http://182.92.158.34:3378/apps/student/comment/list";
  public static final String muban_modify = "http://182.92.158.34:3378/apps/student/comment/modify";
  public static final String muban_delete = "http://182.92.158.34:3378/apps/student/comment/delete";
  public static final String muban_add = "http://182.92.158.34:3378/apps/student/comment/submit";

  public static final String dianping_list = "http://182.92.158.34:3378/apps/student/chat/list";
  public static final String dianping_detail = "http://182.92.158.34:3378/apps/student/chat/detail";
  public static final String dianping_send = "http://182.92.158.34:3378/apps/student/chat/send";

  // 作业
  public static final String zuoye_list = "http://182.92.158.34:3378/apps/student/homework/list";
  public static final String zuoye_detail_list = "http://182.92.158.34:3378/apps/student/homework/detail";
  public static final String zuoye_modify = "http://182.92.158.34:3378/apps/student/homework/modify";
  public static final String zuoye_submit = "http://182.92.158.34:3378/apps/student/homework/submit";

  // PK广场
  public static final String pk_list = "http://182.92.158.34:3378/apps/student/pkill/hotornot";
  public static final String pk_mine = "http://182.92.158.34:3378/apps/student/pkill/mine";
  public static final String pk_hoted = "http://182.92.158.34:3378/apps/student/pkill/hoted";
  public static final String pk_rank = "http://182.92.158.34:3378/apps/student/pkill/rank";
  public static final String pk_submit = "http://182.92.158.34:3378/apps/student/pkill/submit";

  // 历史上的今天
  public static final String today_all = "http://182.92.158.34:3378/apps/student/today/list";// 所有作品
  public static final String today_schools = "http://182.92.158.34:3378/apps/student/today/list2";// 按学校分类的
  public static final String today_list = "http://182.92.158.34:3378/apps/student/today/list3";// 获取特定学校的

  // 个人资料设置
  public static final String profile_modify = "http://182.92.158.34:3378/apps/student/account/modify";

  // 成绩曲线
  public static final String chengji_quxian = "http://182.92.158.34:3378/apps/student/exam/line";

  // 科目列表
  public static final String subject_list = "http://182.92.158.34:3378/apps/student/orgext/subjects";

}
