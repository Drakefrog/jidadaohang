package com.example.jidadaohang;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Intent;

public class LoginHandler implements Runnable {

	@Override
	public void run() {
		//get username and password;
		/*
		userName = user_name.getText().toString().trim();
		password = pass_word.getText().toString().trim();
		//���ӵ��������ĵ�ַ���Ҽ�������8080�˿�
		String connectURL="http://192.168.1.100:8080/text0/com.light.text/login.php/";
		//�����û�����������ӵ�ַ
		boolean isLoginSucceed = gotoLogin(userName, password,connectURL);
		//�жϷ���ֵ�Ƿ�Ϊtrue�����ǵĻ���������ҳ��
		if(isLoginSucceed){
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), HomeActivity.class);
		startActivity(intent);
		proDialog.dismiss();
		}else{
		proDialog.dismiss();
		// Toast.makeText(ClientActivity.this, "�������", Toast.LENGTH_LONG).show();
		System.out.println("�������");
		}
		*/
		}
		

		//����ķ����������û� ���� �����ӵ�ַ
		private boolean gotoLogin(String userName, String password,String connectUrl) {
		String result = null; //����ȡ�÷��ص�String��
		boolean isLoginSucceed = false;
		//test
		System.out.println("username:"+userName);
		System.out.println("password:"+password);
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
		}
		}catch(Exception e){
		e.printStackTrace();
		}
		//�жϷ��ص������Ƿ�Ϊphp�гɹ������������
		if(result.equals("login succeed")){
		isLoginSucceed = true;
		}
		return isLoginSucceed;
		}
}