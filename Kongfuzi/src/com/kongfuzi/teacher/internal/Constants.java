package com.kongfuzi.teacher.internal;

public class Constants {
    public static final String POS = "POS";
    public static final String VERSION = "VERSION";
    public static final String NETWORK_ERROR = "NETWORK_ERROR";
    public static final String EXAM_SUBMIT_ACTIVITY = "EXAM_SUBMIT_ACTIVITY";
    
    public static final String AttendanceDayActivity = "AttendanceDayActivity";
    public static final String AttendanceFragment = "AttendanceFragment";
    public static final String DianpingFragment = "DianpingFragment";
    public static final String HomeworkFragment_TODAY = "HomeworkFragment_TODAY";
    public static final String HomeworkFragment_HISTORY = "HomeworkFragment_HISTORY";
    public static final String MarkFragment = "MarkFragment";
    public static final String AttendanceSummaryActivity = "AttendanceSummaryActivity";
    public static final String ChengjiLuRuActivity = "ChengjiLuRuActivity";
    public static final String SYSTEM_NOTIFICATION = "SYSTEM_NOTIFICATION";
    public static final String MESSAGE = "MESSAGE";
    /*    public static final String  = "";
    public static final String  = "";
    public static final String  = "";
    public static final String  = "";
    public static final String  = "";
    public static final String  = "";
    public static final String  = "";
    public static final String  = "";*/
    
    
    
    
    //url
    public static final String login_url = "http://182.92.158.34:3378/apps/teacher/account/login";
    public static final String logout_url = "http://182.92.158.34:3378/apps/teacher/account/logout";
    public static final String query_classes_list = "http://182.92.158.34:3378/apps/teacher/class/queryall";
    public static final String query_students_list = "http://182.92.158.34:3378/apps/teacher/student/queryall";
    
    /*点名-读取历史点名列表（按照创建时间倒序排列）
     * @params
     * 请求 secretkey 登录后的密钥  String类型
     * classid  班级ID  String类型
     * start  分页（从第几个开始）  Int类型
     * limit  分页（取多少） Int类型
     */
    public static final String dianming_init = "http://182.92.158.34:3378/apps/teacher/callname/init";
    public static final String dianming_summary = "http://182.92.158.34:3378/apps/teacher/callname/allday";
    public static final String dianming_oneday = "http://182.92.158.34:3378/apps/teacher/callname/oneday";
    public static final String dianming_student_list = "http://182.92.158.34:3378/apps/teacher/callname/detail2";
    public static final String dianming_modify = "http://182.92.158.34:3378/apps/teacher/callname/modify";
    public static final String query_dianming_list = "http://182.92.158.34:3378/apps/teacher/callname/list";
    public static final String query_dianming_detail = "http://182.92.158.34:3378/apps/teacher/callname/detail";
    public static final String dianming_detail_list = "http://182.92.158.34:3378/apps/teacher/callname/list2";
    public static final String submit_dianming = "http://182.92.158.34:3378/apps/teacher/callname/submit";
    public static final String image_url = "http://182.92.158.34:3378/apps/file/download?fileID=";
    public static final String image_upload = "http://182.92.158.34:3378/apps/file/upload";
    public static final String multiple_image_upload = "http://182.92.158.34:3378/apps/file/uploads";
    
    public static final String query_exam_list = "http://182.92.158.34:3378/apps/teacher/exam/list";
    public static final String exam_detail_list = "http://182.92.158.34:3378/apps/teacher/exam/detail";
    public static final String submit_exam_chengji = "http://182.92.158.34:3378/apps/teacher/exam/submit";
    public static final String exam_score_modify = "http://182.92.158.34:3378/apps/teacher/exam/modify";
    public static final String exam_student_list = "http://182.92.158.34:3378/apps/teacher/exam/namelist";
    public static final String exam_detail = "http://182.92.158.34:3378/apps/teacher/exam/querystudent";
    
    public static final String muban_list = "http://182.92.158.34:3378/apps/teacher/comment/list";
    public static final String muban_modify = "http://182.92.158.34:3378/apps/teacher/comment/modify";
    public static final String muban_delete = "http://182.92.158.34:3378/apps/teacher/comment/delete";
    public static final String muban_add = "http://182.92.158.34:3378/apps/teacher/comment/submit";
    
    public static final String dianping_list = "http://182.92.158.34:3378/apps/teacher/chat/list";
    public static final String dianping_detail = "http://182.92.158.34:3378/apps/teacher/chat/detail";
    public static final String dianping_send = "http://182.92.158.34:3378/apps/teacher/chat/send";
    
    public static final String zuoye_list = "http://182.92.158.34:3378/apps/teacher/homework/list";
    public static final String zuoye_detail_list = "http://182.92.158.34:3378/apps/teacher/homework/detail";
    public static final String zuoye_modify = "http://182.92.158.34:3378/apps/teacher/homework/modify";
    public static final String zuoye_submit = "http://182.92.158.34:3378/apps/teacher/homework/submit";
    public static final String zuoye_upload = "http://182.92.158.34:3378/apps/teacher/homework/studentupload";
    public static final String zuoye_today = "http://182.92.158.34:3378/apps/teacher/homework/summarytoday";
    public static final String zuoye_history = "http://182.92.158.34:3378/apps/teacher/homework/summaryhistory";
    public static final String zuoye_list_by_subject = "http://182.92.158.34:3378/apps/teacher/homework/summarytoday1";
    public static final String zuoye_list_by_class = "http://182.92.158.34:3378/apps/teacher/homework/summarytoday2";
    public static final String zuoye_list_by_title = "http://182.92.158.34:3378/apps/teacher/homework/summarytoday3";
    public static final String zuoye_student_list = "http://182.92.158.34:3378/apps/teacher/homework/namelist";
    public static final String zuoye_detail = "http://182.92.158.34:3378/apps/teacher/homework/querystudent";
    
    //个人资料设置
    public static final String profile_modify = "http://182.92.158.34:3378/apps/teacher/account/modify";
    
    //成绩曲线
    public static final String chengji_quxian = "http://182.92.158.34:3378/apps/teacher/exam/line";
    
    //科目列表
    public static final String subject_list = "http://182.92.158.34:3378/apps/teacher/orgext/subjects";

    //for Intent
    public static final String UPLOAD_IMAGE_PROGRESS = "正在上传图片";
    public static final String LOAD_PROGRESS = "正在加载";
    public static final String MESSAGE_RECEIVED_ACTION = "MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "KEY_TITLE";
    public static final String KEY_MESSAGE = "KEY_MESSAGE";
    public static final String KEY_EXTRAS = "KEY_EXTRAS";
    public static final String TAG = "TAG";
    public static final String COMMENT_STR_LIST = "COMMENT_STR_LIST";
    public static final String COMMENT_STR = "COMMENT_STR";
    public static final String MESSAGE_MAP = "MESSAGE_MAP";
    public static final String DATE = "DATE";
    public static final String POST_TIME = "POST_TIME";
    public static final String TITLE = "TITLE";
    public static final String CALLNAME_LIST_ID = "CALLNAME_LIST_ID";
    public static final String CALLNAME_DETAIL_ID = "CALLNAME_DETAIL_ID";
    public static final String STATUE = "STATUE";
    public static final String ID = "ID";
    public static final String SCORE = "SCORE";
    public static final String PIC_ID = "PIC_ID";
    public static final String NAME = "NAME";
    public static final String PHONE = "PHONE";
    public static final String EMAIL = "EMAIL";
    public static final String QINGJIA_COUNT = "QINGJIA_COUNT";
    public static final String KUANGKE_COUNT = "KUANGKE_COUNT";
    public static final String ZAOTUI_COUNT = "ZAOTUI_COUNT";
    public static final String QITA_COUNT = "QITA_COUNT";
    public static final String SUBJECT = "SUBJECT";
    public static final String ADD_VIEW_TAG = "ADD_VIEW_TAG";
    public static final String NO_ADD_VIEW_TAG = "NO_ADD_VIEW_TAG";
    public static final String HW_LIST_ID = "HW_LIST_ID";
    public static final String STUDENT_ID = "STUDENT_ID";
    public static final String STUDENT_NAME = "STUDENT_NAME";
    public static final String CLASS_BUS = "CLASS_BUS";
    public static final String ZUOYE_MODIFY_ACTIVITY = "ZUOYE_MODIFY_ACTIVITY";
    public static final String ZUOYE_SUBMIT_ACTIVITY = "ZUOYE_SUBMIT_ACTIVITY";
    public static final Integer ALBUM_PHOTO_CODE = 0;
    public static final Integer CAPTUER_IMAGE_CODE = 1;
    
    
    
    //for api
    public static final String STATUE_DAO = "STATUE_DAO";
    public static final String STATUE_KUANGKE = "STATUE_KUANGKE";
    public static final String STATUE_BINGJIA = "STATUE_BINGJIA";
    public static final String STATUE_SHIJIA = "STATUE_SHIJIA";
    public static final String STATUE_CHIDAO = "STATUE_CHIDAO";
    public static final String STATUE_ZAOTUI = "STATUE_ZAOTUI";
    public static final String STATUE_ALL = "STATUE_ALL";
    public static final String STATUE_OTHER = "STATUE_OTHER";
    public static final String TYPE_SEND = "TYPE_SEND";
    public static final String TYPE_RECV = "TYPE_RECV";
    //for prefs
    public static final String SECRET_KEY = "SECRET_KEY";
    public static final String AVATAR_ID = "AVATAR_ID";
    public static final String TEACHER_NAME = "TEACHER_NAME";
    public static final String TEACHER_PHONE = "TEACHER_PHONE";
    public static final String TEACHER_EMAIL = "TEACHER_EMAIL";
    
    
}
