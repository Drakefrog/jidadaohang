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
    	String result = null; //����ȡ�÷��ص�String��
		boolean isLoginSucceed = false;
		//����post����
		HttpPost httpRequest = new HttpPost(connectUrl);
		//Post�������ͱ���������NameValuePair[]���д���
		List params = new ArrayList();
		params.add(new BasicNameValuePair("name",userName));
		params.add(new BasicNameValuePair("pwd",password));

		try{
		//����HTTP����
		httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
		//ȡ��HTTP response
		HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
		
		//��״̬��Ϊ200������ɹ���ȡ����������
		if(httpResponse.getStatusLine().getStatusCode()==200){
		//ȡ���ַ���
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
		//�жϷ��ص������Ƿ�Ϊphp�гɹ������������
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
            connection.setConnectTimeout(3000); // ����ʱʱ��3s  
            connection.setRequestMethod("GET");  
            connection.setDoInput(true);
            System.out.println("url_path : " + url_path);
            int code = connection.getResponseCode(); // ����״̬��  
            if (code == 200) {  
            	System.out.println("code : 200 ");
                // ��õ�����������ʱ�������Ѿ������˷���˷��ػ�����JSON������,��ʱ��Ҫ�������ת�����ַ���  
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
            // inputStream�������õ�����д��ByteArrayOutputStream����,  
            // Ȼ��ͨ��outputStream.toByteArrayת���ֽ����飬��ͨ��new String()����һ���µ��ַ�����  
            jsonString = new String(outputStream.toByteArray());  
        } catch (Exception e) {  
            // TODO: handle exception  
        }  
        return jsonString;  
    }

}
