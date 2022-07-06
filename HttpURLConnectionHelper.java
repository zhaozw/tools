package test;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
 
public class HttpURLConnectionHelper {
 
 public static String sendRequest(String urlParam,String requestType) {
 
  HttpURLConnection con = null; 
 
  BufferedReader buffer = null; 
  StringBuffer resultBuffer = null; 
 
  try {
   URL url = new URL(urlParam); 
   //得到连接对象
   con = (HttpURLConnection) url.openConnection(); 
   //设置请求类型
   con.setRequestMethod(requestType); 
   //设置请求需要返回的数据类型和字符集类型
   con.setRequestProperty("Content-Type", "application/json;charset=utf-8"); 
   //允许写出
   con.setDoOutput(true);
   //允许读入
   con.setDoInput(true);
   //不使用缓存
   con.setUseCaches(false);
   //得到响应码
   int responseCode = con.getResponseCode();
 
   if(responseCode == HttpURLConnection.HTTP_OK){
    //得到响应流
    InputStream inputStream = con.getInputStream();
    //将响应流转换成字符串
    resultBuffer = new StringBuffer();
    String line;
    buffer = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
    while ((line = buffer.readLine()) != null) {
     resultBuffer.append(line);
    }
    return resultBuffer.toString();
   }
 
  }catch(Exception e) {
   e.printStackTrace();
  }
  return "";
 }
 static int v1=0;
 static int v2=0;
 public static void main(String[] args) {
 

	 String[] args2= {""};
	 if (args!=null&&args.length>0&&args[0]!=null) {
		 args2[0]=args[0];
	}
	 System.out.println("request id is: "+args2[0]);
  List<Thread> threadSet = new ArrayList<>();
  for (int i = 0; i < 10; i++) {
      Thread thread = new Thread(() -> {
          for (int j = 0; j < 10; j++) {

        	  String url ="http://36.134.94.72:31904/canary?id="+args2[0];
        	  String result = sendRequest(url,"GET");
        	  if(result.contains("beta")) {
        		  v2++;
        	  }else {
        		  v1++;
        	  }
		}
      });
      thread.start();
      threadSet.add(thread);
  }
  for (Thread thread : threadSet) {
      try {
		thread.join();
	} catch (InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
  }
  System.out.println("v1: "+v1+" times");
  System.out.println("v2: "+v2+" times");
 }
}
