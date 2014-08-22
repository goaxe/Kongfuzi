package com.kongfuzi.teacher.internal;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;

public class KFZClient {
  public static final String DEBUG_TAG = "KFZClient";

  private String secretKey = null;

  public KFZClient() {
  }

  public JSONObject getNewestVersion() throws KFZNetworkError {
    String apiPath = "http://www.kongfuzi.com/interface.php";
    return doRequest(apiPath, null, null);
  }

  public JSONObject login(String username, String password) throws JSONException, KFZNetworkError {
    String apiPath = Constants.login_url;
    String[] params = { "username", username, "password",
        new MD5().getMD5ofStr("nxxjmpf_nxxjmpf_nxxjmpf_nxxjmpf_nxxjmpf_nxxjmpf_nxxjmpf" + password) };
    return doRequest(apiPath, params, null);
  }

  public JSONObject logout() throws KFZNetworkError {
    String apiPath = Constants.logout_url;
    return doRequest(apiPath, null, null);
  }

  // false缺少旧密码 或 新密码 参数！
  public JSONObject profileModify(String name, String phone, String email, String oldPassword, String newPassword,
      String fileId) throws KFZNetworkError {
    String apiPath = Constants.profile_modify;
    List<String> paramList = new ArrayList<String>();
    if (name != null) {
      paramList.add("name");
      paramList.add(name);
    }
    if (phone != null) {
      paramList.add("phone");
      paramList.add(phone);
    }
    if (email != null) {
      paramList.add("email");
      paramList.add(email);
    }
    if (oldPassword != null) {
      paramList.add("oldpassword");
      paramList.add(oldPassword);
    }
    if (newPassword != null) {
      paramList.add("newpassword");
      paramList.add(newPassword);
    }
    if (fileId != null) {
      paramList.add("file");
      paramList.add(fileId);
    }
    String[] params = paramList.toArray(new String[0]);
    return doRequest(apiPath, params, null);
  }

  public JSONObject subjectList() throws KFZNetworkError {
    String apiPath = Constants.subject_list;
    return doRequest(apiPath, null, null);

  }

  public JSONObject queryClassesList() throws KFZNetworkError {
    String apiPath = Constants.query_classes_list;
    return doRequest(apiPath, null, null);
  }

  // TODO:
  public JSONObject queryStudentsList(String classId, String appCallnameDays) throws KFZNetworkError {
    String apiPath = Constants.query_students_list;
    List<String> paramList = new ArrayList<String>();

    paramList.add("classid");
    paramList.add(classId);
    if (appCallnameDays != null) {
      paramList.add("app_callname_days");
      paramList.add(appCallnameDays);
    }
    String[] params = paramList.toArray(new String[0]);
    return doRequest(apiPath, params, null);
  }

  public JSONObject dianmingInit() throws KFZNetworkError {
    String apiPath = Constants.dianming_init;
    return doRequest(apiPath, null, null);
  }

  public JSONObject dianmingOneDay(String date) throws KFZNetworkError {
    String apiPath = Constants.dianming_oneday;
    List<String> paramList = new ArrayList<String>();
    if (date != null) {
      paramList.add("date");
      paramList.add(date);
    }
    String[] params = paramList.toArray(new String[0]);
    return doRequest(apiPath, params, null);
  }

  public JSONObject dianmingSummary(Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.dianming_summary;
    String[] params = { "start", "" + start, "limit", "" + limit };
    return doRequest(apiPath, params, null);
  }

  /*// TODO:
  public JSONObject dianmingDetail(String classId, String orgStudentId, String uType) {
    String apiPath = Constants.dianming_detail;
    List<String> paramList = new ArrayList<String>();
    paramList.add("callnamelistid");
    paramList.add(callnameListId);
    if (status != null) {
      paramList.add("status");
      paramList.add(status);
    }
    String[] params = paramList.toArray(new String[0]);
    return doRequest(apiPath, params, null);
  }*/

  // TODO:
  public JSONObject submitDianming(String classId, JSONArray jsonArray) throws KFZNetworkError {
    String apiPath = Constants.submit_dianming;
    String[] params = { "classid", classId, "jsonarray", jsonArray.toString() };
    return doRequest(apiPath, params, null);
  }

  // TODO:
  public JSONObject dianmingModify(JSONArray jsonArray) throws KFZNetworkError {
    String apiPath = Constants.dianming_modify;
    String[] params = { "jsonarray", jsonArray.toString() };
    return doRequest(apiPath, params, null);
  }

  // TODO:
  public JSONObject queryDianmingList(String classId, Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.query_dianming_list;
    String[] params = { "classid", classId, "start", "" + start, "limit", "" + limit };
    return doRequest(apiPath, params, null);

  }

  // TODO:
  public JSONObject queryDianmingDetail(String callnameListId, String days) throws KFZNetworkError {
    String apiPath = Constants.query_dianming_detail;
    String[] params = { "callnamelistid", callnameListId, "days", days };
    return doRequest(apiPath, params, null);
  }

  public JSONObject dianmingDetailList(String classId, String orgtudentId, String days, Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.dianming_detail_list;
    String[] params = { "classid", classId, "orgtudentid", "days", days, "start", "" + start, "limit", "" + limit };
    return doRequest(apiPath, params, null);
  }

  public JSONObject dianmingStudentList(String callnameListId, String status) throws KFZNetworkError {
    String apiPath = Constants.dianming_student_list;
    String[] params = { "callnamelistid", callnameListId, "status", status };
    return doRequest(apiPath, params, null);
  }

  /******** exam 
   * @throws KFZNetworkError *********/

  public JSONObject exampDetail(String orgstudentId, String examSubjectId) throws KFZNetworkError {
    String apiPath = Constants.exam_detail;
    String[] params = { "orgstudentid", orgstudentId, "examsubjectid", examSubjectId };
    return doRequest(apiPath, params, null);
  }

  public JSONObject examStudentList(String classId, String examDate, String examTitle, String examSubject) throws KFZNetworkError {
    String apiPath = Constants.exam_student_list;
    String[] params = { "classid", classId, "examdate", examDate, "examtitle", examTitle, "examsubject", examSubject };
    return doRequest(apiPath, params, null);
  }

  public JSONObject submitExamChengji(String classId, String examDate, String examTitle, String examSubject,
      JSONArray jsonArray) throws KFZNetworkError {
    String apiPath = Constants.submit_exam_chengji;
    String[] params = { "classid", classId, "examdate", examDate, "examtitle", examTitle, "examsubject", examSubject,
        "jsonarray", jsonArray.toString() };
    return doRequest(apiPath, params, null);
  }

  public JSONObject examScoreModify(JSONArray jsonArray) throws KFZNetworkError {
    String apiPath = Constants.exam_score_modify;
    String[] params = { "jsonarray", jsonArray.toString() };
    return doRequest(apiPath, params, null);
  }

  public JSONObject queryExamList(String classId, Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.query_exam_list;
    String[] params = { "classid", classId, "start", "" + start, "limit", "" + limit };
    return doRequest(apiPath, params, null);
  }

  public JSONObject examDetailList(String examTitleId, Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.exam_detail_list;
    String[] params = { "examtitleid", examTitleId, "start", "" + start, "limit", "" + limit };
    return doRequest(apiPath, params, null);
  }

  /***** muban 
   * @throws KFZNetworkError ***/
  public JSONObject mubanAdd(String type, String content) throws KFZNetworkError {
    String apiPath = Constants.muban_add;
    String[] params = { "type", type, "content", content };
    return doRequest(apiPath, params, null);
  }

  public JSONObject mubanModify(String commentId, String content) throws KFZNetworkError {
    String apiPath = Constants.muban_modify;
    String[] params = { "commentid", commentId, "content", content };
    return doRequest(apiPath, params, null);
  }

  public JSONObject mubanDelete(JSONArray jsonArray) throws KFZNetworkError {
    String apiPath = Constants.muban_delete;
    String[] params = { "jsonarray", jsonArray.toString() };
    return doRequest(apiPath, params, null);
  }

  public JSONObject mubanList(String type, Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.muban_list;
    String[] params = { "type", type, "start", "" + start, "limit", "" + limit };
    return doRequest(apiPath, params, null);
  }

  public JSONObject zuoyeListBySubject(String date, Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.zuoye_list_by_subject;
    String[] params = { "date", date, "start", "" + start, "limit", "" + limit };
    return doRequest(apiPath, params, null);
  }

  public JSONObject zuoyeListByCalss(String date, String subject, Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.zuoye_list_by_class;
    String[] params = { "date", date, "subject", subject, "start", "" + start, "limit", "" + limit };
    return doRequest(apiPath, params, null);
  }

  public JSONObject zuoyeListByTitle(String date, String subject, String classId, Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.zuoye_list_by_title;
    String[] params = { "date", date, "subject", subject, "classid", classId, "start", "" + start, "limit", "" + limit };
    return doRequest(apiPath, params, null);
  }

  public JSONObject zuoyeDetail(String hwListId, String orgStudentId) throws KFZNetworkError {
    String apiPath = Constants.zuoye_detail;
    String[] params = { "hwlistid", hwListId, "orgstudentid", orgStudentId };
    return doRequest(apiPath, params, null);
  }

  public JSONObject zuoyeToday(String date, Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.zuoye_today;
    String[] params = { "date", date, "start", "" + start, "limit", "" + limit };
    return doRequest(apiPath, params, null);
  }

  public JSONObject zuoyeHistory(Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.zuoye_history;
    String[] params = { "start", "" + start, "limit", "" + limit };
    return doRequest(apiPath, params, null);
  }

  public JSONObject zuoyeStudentList(String classId, String hwDate, String hwTitle, String hwSubject) throws KFZNetworkError {
    String apiPath = Constants.zuoye_student_list;
    List<String> paramList = new ArrayList<String>();
    paramList.add("classid");
    paramList.add(classId);
    if (hwDate != null) {
      paramList.add("hwdate");
      paramList.add(hwDate);
    }
    paramList.add("hwtitle");
    paramList.add(hwTitle);
    paramList.add("hwsubject");
    paramList.add(hwSubject);
    String[] params = paramList.toArray(new String[0]);
    return doRequest(apiPath, params, null);
  }

  public JSONObject zuoyeSubmit(String classId, String hwDate, String hwTitle, String hwSubject, JSONArray jsonArray) throws KFZNetworkError {
    String apiPath = Constants.zuoye_submit;
    List<String> paramList = new ArrayList<String>();
    paramList.add("classid");
    paramList.add(classId);
    if (hwDate != null) {
      paramList.add("hwdate");
      paramList.add(hwDate);
    }
    paramList.add("hwtitle");
    paramList.add(hwTitle);
    paramList.add("hwsubject");
    paramList.add(hwSubject);
    paramList.add("jsonarray");
    paramList.add(jsonArray.toString());
    String[] params = paramList.toArray(new String[0]);
    return doRequest(apiPath, params, null);
  }

  public JSONObject zuoyeModify(JSONArray jsonArray) throws KFZNetworkError {
    String apiPath = Constants.zuoye_modify;
    String[] params = { "jsonarray", jsonArray.toString() };
    return doRequest(apiPath, params, null);
  }

  public JSONObject zuoyeList(String classId, Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.zuoye_list;
    String[] params = { "classid", classId, "start", "" + start, "limit", "" + limit };
    return doRequest(apiPath, params, null);
  }

  public JSONObject zuoyeDetailList(String hwListId, Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.zuoye_detail_list;
    String[] params = { "hwlistid", hwListId, "start", "" + start, "limit", "" + limit };
    return doRequest(apiPath, params, null);
  }

  public JSONObject zuoyeUpload(String classId, String orgStudentId, String subject, String hwDate) throws KFZNetworkError {
    String apiPath = Constants.zuoye_upload;
    String[] params = { "classid", classId, "orgstudentid", orgStudentId, "subject", subject, "hwdate", hwDate };
    return doRequest(apiPath, params, null);
  }

  public JSONObject dianpingList(String classId, Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.dianping_list;
    String[] params = { "classid", classId, "start", "" + start, "limit", "" + limit };
    return doRequest(apiPath, params, null);
  }

  public JSONObject dianpingDetail(String classId, String orgStudentId, String uType, Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.dianping_detail;
    String[] params = { "classid", classId, "orgstudentid", orgStudentId, "utype", uType, "start", "" + start, "limit",
        "" + limit };
    return doRequest(apiPath, params, null);
  }

  public JSONObject dianpingSend(String classId, String message, JSONArray jsonArray) throws KFZNetworkError {
    String apiPath = Constants.dianping_send;
    String[] params = { "classid", classId, "message", message, "jsonarray", jsonArray.toString() };
    return doRequest(apiPath, params, null);
  }

  public JSONObject chengjiQuxian(String classId, String subject, String days) throws KFZNetworkError {
    String apiPath = Constants.chengji_quxian;
    String[] params = { "classid", classId, "subject", subject, "days", days };
    return doRequest(apiPath, params, null);
  }

  public void setSecretKey(String secretKey) {
    this.secretKey = secretKey;
  }

  public static String encodeUrlParam(String s) {
    return Uri.encode(s, "UTF-8");
  }

  public String buildUrlWithParams(String url, String[] params) {
    StringBuilder buf = new StringBuilder();
    String sep = "";
    if (secretKey != null) {
      buf.append(sep);
      sep = "&";
      buf.append("secretkey=").append(secretKey);
    }
    if (params != null) {
      if (params.length % 2 != 0) {
        throw new IllegalArgumentException("'params.length' is " + params.length + "; expecting a multiple of two");
      }
      for (int i = 0; i < params.length;) {
        String key = params[i++];
        String value = params[i++];
        if (value != null) {
          buf.append(sep);
          sep = "&";
          buf.append(encodeUrlParam(key));
          buf.append("=");
          buf.append(encodeUrlParam(value));
        }
      }
    }
    return url + "?" + buf.toString();
  }

  public JSONObject doRequest(String apiPath, String[] params, JSONObject body) throws KFZNetworkError {

    try {
      URL url = new URL(buildUrlWithParams(apiPath, params));
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("GET");
      conn.setConnectTimeout(5000);
      conn.setReadTimeout(5000);
      if (conn.getResponseCode() == 200) {
        InputStream is = conn.getInputStream();
        // 将输入流转换成字符串
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = is.read(buffer)) != -1) {
          baos.write(buffer, 0, len);
        }
        String json = baos.toString();
        baos.close();
        is.close();
        //
        return new JSONObject(json);
      }
    } catch (Exception e) {
      Log.e(DEBUG_TAG, "network error");
      throw new KFZNetworkError("网络异常");
    }

    return null;
  }
  
  public class KFZNetworkError extends Exception{
    String message;
    public KFZNetworkError(String message) {
      this.message = message;
    }
    
  }
  
  private static final int TIME_OUT = 10 * 1000; // 超时时间
  private static final String CHARSET = "utf-8"; // 设置编码

  /**
   * android上传文件到服务器
   * 
   * @param file
   *          需要上传的文件
   * @param RequestURL
   *          请求的rul
   * @return 返回响应的内容
   */
  public JSONObject uploadFile(File file, String RequestURL) {
    String result = null;
    String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
    String PREFIX = "--", LINE_END = "\r\n";
    String CONTENT_TYPE = "multipart/form-data"; // 内容类型

    try {
      URL url = new URL(RequestURL + "?" + "secretkey=" + this.secretKey);
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setReadTimeout(TIME_OUT);
      conn.setConnectTimeout(TIME_OUT);
      conn.setDoInput(true); // 允许输入流
      conn.setDoOutput(true); // 允许输出流
      conn.setUseCaches(false); // 不允许使用缓存
      conn.setRequestMethod("POST"); // 请求方式
      conn.setRequestProperty("Charset", CHARSET); // 设置编码
      conn.setRequestProperty("connection", "keep-alive");
      conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);
      conn.setRequestProperty("name", "file");

      if (file != null) {
        /**
         * 当文件不为空，把文件包装并且上传
         */
        DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
        StringBuffer sb = new StringBuffer();
        sb.append(PREFIX);
        sb.append(BOUNDARY);
        sb.append(LINE_END);
        /**
         * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件 filename是文件的名字，包含后缀名的
         * 比如:abc.png
         */

        sb.append("Content-Disposition: form-data; name=\"file\";" + "filename=\"" + file.getName() + "\"" + LINE_END);
        sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
        sb.append(LINE_END);
        dos.write(sb.toString().getBytes());
        InputStream is = new FileInputStream(file);
        byte[] bytes = new byte[1024];
        int len = 0;
        while ((len = is.read(bytes)) != -1) {
          dos.write(bytes, 0, len);
        }
        is.close();
        dos.write(LINE_END.getBytes());
        byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
        dos.write(end_data);
        dos.flush();
        /**
         * 获取响应码 200=成功 当响应成功，获取响应的流
         */
        int res = conn.getResponseCode();
        Log.e(DEBUG_TAG, "response code:" + res);
        // if(res==200)
        // {
        Log.e(DEBUG_TAG, "request success");
        InputStream input = conn.getInputStream();
        StringBuffer sb1 = new StringBuffer();
        int ss;
        while ((ss = input.read()) != -1) {
          sb1.append((char) ss);
        }
        result = sb1.toString();
        Log.e(DEBUG_TAG, "result : " + result);
        return new JSONObject(result);
        // }
        // else{
        // Log.e(TAG, "request error");
        // }
      }
    } catch (MalformedURLException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      // TODO: handle exception
    }
    return null;
  }
}
