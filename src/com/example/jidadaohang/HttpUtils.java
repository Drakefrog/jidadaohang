package com.example.jidadaohang;

import java.io.ByteArrayOutputStream;  
import java.io.InputStream;  
import java.net.HttpURLConnection;  
import java.net.URL; 
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class HttpUtils {
	
	public HttpUtils() {  
        // TODO Auto-generated constructor stub  
    }  
  
    static void getJsonContent(String userName, String password,String connectUrl) {  
    	String result = null; //用来取得返回的String；
		boolean isLoginSucceed = false;
		//发送post请求
		HttpPost httpRequest = new HttpPost(connectUrl);
		//Post运作传送变数必须用NameValuePair[]阵列储存
		List params = new ArrayList();
		params.add(new BasicNameValuePair("name",userName));
		params.add(new BasicNameValuePair("pwd",password));

		try{
		//发出HTTP请求
		httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
		//取得HTTP response
		HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
		
		//若状态码为200则请求成功，取到返回数据
		if(httpResponse.getStatusLine().getStatusCode()==200){
		//取出字符串
		result=EntityUtils.toString(httpResponse.getEntity());
		System.out.println("result= "+result);
		
		JSONArray arr = new JSONArray (result.toString());
		for(int i=0;i<arr.length(); i++)
		{
			JSONObject jsonObject = (JSONObject) arr.get(i);
			
			String re_username = jsonObject.getString("username");
			System.out.println("username = "+re_username);
			String re_password = jsonObject.getString("password");
			System.out.println("password = "+re_password);
			int re_user_id = jsonObject.getInt("user_id");
			System.out.println("user_id = "+re_user_id);
		}

		}
		}catch(Exception e){
		e.printStackTrace();
		}
		//判断返回的数据是否为php中成功登入是输出的
		if(result.equals("login succeed")){
			isLoginSucceed = true;
		}
		else
		{
			System.out.println(result);
		}
		
		System.out.println(result);
		
    	/**
    	 * try {  
    	 
            URL url = new URL(url_path);  
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();  
            connection.setConnectTimeout(3000); // 请求超时时间3s  
            connection.setRequestMethod("GET");  
            connection.setDoInput(true);
            System.out.println("url_path : " + url_path);
            int code = connection.getResponseCode(); // 返回状态码  
            if (code == 200) {  
            	System.out.println("code : 200 ");
                // 或得到输入流，此时流里面已经包含了服务端返回回来的JSON数据了,此时需要将这个流转换成字符串  
                //return changeInputStream(connection.getInputStream());
            	return connection.getInputStream().toString();
            }  
        } catch (Exception e) {  
            // TODO: handle exception  
        }  
        return "JLU error ! ";
       */
    }

    	  
  
    private static String changeInputStream(InputStream inputStream) {  
        // TODO Auto-generated method stub  
        String jsonString = "";  
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();  
        int length = 0;  
        byte[] data = new byte[1024];  
        try {  
            while (-1 != (length = inputStream.read(data))) {  
                outputStream.write(data, 0, length);  
            }  
            // inputStream流里面拿到数据写到ByteArrayOutputStream里面,  
            // 然后通过outputStream.toByteArray转换字节数组，再通过new String()构建一个新的字符串。  
            jsonString = new String(outputStream.toByteArray());  
        } catch (Exception e) {  
            // TODO: handle exception  
        }  
        return jsonString;  
    }

}
