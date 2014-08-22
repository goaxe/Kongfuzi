package com.kongfuzi.student.internal;

import java.io.ByteArrayOutputStream;
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

import javax.crypto.spec.PSource;

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

  // http://182.92.158.34:3378/apps/student
  /*
   * public JSONObject zuoyeStudentList(String classId, String hwDate, String
   * hwTitle, String hwSubject) throws KFZNetworkError { String apiPath =
   * Constants.zuoye_student_list; List<String> paramList = new
   * ArrayList<String>(); paramList.add("classid"); paramList.add(classId); if
   * (hwDate != null) { paramList.add("hwdate"); paramList.add(hwDate); }
   * paramList.add("hwtitle"); paramList.add(hwTitle);
   * paramList.add("hwsubject"); paramList.add(hwSubject); String[] params =
   * paramList.toArray(new String[0]); return doRequest(apiPath, params, null);
   * }
   */

  public JSONObject subjectList(String classId) throws KFZNetworkError {
    String apiPath = Constants.subject_list;
    String[] params = { "classid", classId };
    return doRequest(apiPath, params);
  }

  public JSONObject login(String phone, String password) throws KFZNetworkError {
    String apiPath = Constants.login_url;
    String[] params = { "phone", phone, "password", password };
    return doRequest(apiPath, params);

  }

  public JSONObject logout() throws KFZNetworkError {
    String apiPath = Constants.logout_url;
    return doRequest(apiPath, null);
  }

  public JSONObject profileModify(String studentName, String email, String oldPassword, String newPassword, String file) throws KFZNetworkError {
    String apiPath = Constants.profile_modify;
    List<String> paramList = new ArrayList<String>();
    if (studentName != null) {
      paramList.add("studentname");
      paramList.add(studentName);
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
    if (file != null) {
      paramList.add("file");
      paramList.add(file);
    }
    String[] params = paramList.toArray(new String[0]);
    return doRequest(apiPath, params);
  }

  public JSONObject register(String phone, String email, String password, String vcode) throws KFZNetworkError {
    String apiPath = Constants.register_url;
    String[] params = { "phone", phone, "email", email, "password", password, "vcode", vcode };
    return doRequest(apiPath, params);
  }

  public JSONObject resetPassword(String phone, String password, String vcode) throws KFZNetworkError {
    String apiPath = Constants.resetpwd_url;
    String[] params = { "phone", phone, "password", password, "vcode", vcode };
    return doRequest(apiPath, params);
  }

  public JSONObject queryClassesList() throws KFZNetworkError {
    String apiPath = Constants.query_classes_list;
    return doRequest(apiPath, null);
  }

  public JSONObject queryExamList(String classId, Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.query_exam_list;
    String[] params = { "classid", classId, "start", "" + start, "limit", "" + limit };
    return doRequest(apiPath, params);
  }

  public JSONObject examDetail(String examTitleId) throws KFZNetworkError {
    String apiPath = Constants.exam_detail;
    String[] params = { "examtitleid", examTitleId };
    return doRequest(apiPath, params);
  }

  public JSONObject chengjiQuxian(String classId, String subject, String days) throws KFZNetworkError {
    String apiPath = Constants.chengji_quxian;
    String[] params = { "classid", classId, "subject", subject, "days", days };
    return doRequest(apiPath, params);
  }

  public JSONObject zuoyeList(String classId, Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.zuoye_list;
    String[] params = { "classid", classId, "start", "" + start, "", "limit" + limit };
    return doRequest(apiPath, params);
  }

  public JSONObject zuoyeSubmit(String classId, String subject, String hwDate, JSONArray jsonArray) throws KFZNetworkError {
    String apiPath = Constants.zuoye_submit;
    String[] params = { "classid", classId, "subject", subject, "hwdate", hwDate, "jsonarray", jsonArray.toString() };
    return doRequest(apiPath, params);
  }
  
  public JSONObject queryDianmingList(String classId, String days, Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.query_dianming_list;
    String[] params = {"classid", classId, "days", days, "start", "" + start, "limit", "" + limit};
    return doRequest(apiPath, params);
  }
  
  public JSONObject dianpingList(String classId, Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.dianping_list;
    String[] params = {"classid", classId,  "start", "" + start, "limit", "" + limit};
    return doRequest(apiPath, params);
  }
  
  public JSONObject dianpingDetail(String classId, String teacherId, Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.dianping_detail;
    String[] params = {"classid", classId, "teacherid", teacherId, "start", "" + start, "limit", "" + limit};
    return doRequest(apiPath, params);
  }
  
  public JSONObject dianpingSend(String classId, String message, String teacherId) throws KFZNetworkError {
    String apiPath = Constants.dianping_send;
    String[] params = {"classid", classId, "message", message, "teacherid", teacherId};
    return doRequest(apiPath, params);
  }
  
  public JSONObject pkList(String pkId, String hotornot) throws KFZNetworkError {
    String apiPath = Constants.pk_list;
    List<String> paramList = new ArrayList<String>();
    if (pkId != null) {
      paramList.add("pkid");
      paramList.add(pkId);
    }
    if (hotornot != null) {
      paramList.add("hotornot");
      paramList.add(hotornot);
    }
    String[] params = paramList.toArray(new String[0]);
    return doRequest(apiPath, params);
  }
  
  public JSONObject pkMine(Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.pk_mine;
    String[] params = {"start", "" + start, "limit", "" + limit};
    return doRequest(apiPath, params);
  }
  
  public JSONObject pkHoted(Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.pk_hoted;
    String[] params = {"start", "" + start, "limit", "" + limit};
    return doRequest(apiPath, params);
  }
  
  public JSONObject pkRank(Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.pk_rank;
    String[] params = {"start", "" + start, "limit", "" + limit};
    return doRequest(apiPath, params);
  }
  
  public JSONObject pkSubmit(String fileId) throws KFZNetworkError {
    String apiPath = Constants.pk_submit;
    String[] params = {"fileid", fileId};
    return doRequest(apiPath, params);
  }
  
  public JSONObject todayAll(Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.today_all;
    String[] params = {"start", "" + start, "limit", "" + limit};
    return doRequest(apiPath, params);
  }
  
  public JSONObject todaySchools(Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.today_schools;
    String[] params = {"start", "" + start, "limit", "" + limit};
    return doRequest(apiPath, params);
  }
  
  public JSONObject todayList(Integer start, Integer limit) throws KFZNetworkError {
    String apiPath = Constants.today_list;
    String[] params = {"start", "" + start, "limit", "" + limit};
    return doRequest(apiPath, params);
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

  public JSONObject doRequest(String apiPath, String[] params) throws KFZNetworkError {

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

  public class KFZNetworkError extends Exception {
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
