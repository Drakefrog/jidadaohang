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
		//连接到服务器的地址，我监听的是8080端口
		String connectURL="http://192.168.1.100:8080/text0/com.light.text/login.php/";
		//填入用户名密码和连接地址
		boolean isLoginSucceed = gotoLogin(userName, password,connectURL);
		//判断返回值是否为true，若是的话就跳到主页。
		if(isLoginSucceed){
		Intent intent = new Intent();
		intent.setClass(getApplicationContext(), HomeActivity.class);
		startActivity(intent);
		proDialog.dismiss();
		}else{
		proDialog.dismiss();
		// Toast.makeText(ClientActivity.this, "登入错误", Toast.LENGTH_LONG).show();
		System.out.println("登入错误");
		}
		*/
		}
		

		//登入的方法，传入用户 密码 和连接地址
		private boolean gotoLogin(String userName, String password,String connectUrl) {
		String result = null; //用来取得返回的String；
		boolean isLoginSucceed = false;
		//test
		System.out.println("username:"+userName);
		System.out.println("password:"+password);
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
		}
		}catch(Exception e){
		e.printStackTrace();
		}
		//判断返回的数据是否为php中成功登入是输出的
		if(result.equals("login succeed")){
		isLoginSucceed = true;
		}
		return isLoginSucceed;
		}
}