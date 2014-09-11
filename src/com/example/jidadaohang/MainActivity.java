package com.example.jidadaohang;

import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;

import android.R.string;
import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.SearchView;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
//百度定位所需相关类
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
//百度地图所需相关类
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.SearchView.OnSuggestionListener;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.jidadaohang.R;
import com.example.jidadaohang.Contacts;

public class MainActivity extends Activity {

	ContentResolver resolver;
	  Button button;
	// 创建ActionBar
//	private ActionBar actionBar;
	private BMapManager mapManager;
	private MapView bMapView;
	public LocationClient mLocationClient = null;
	BDLocation bdLocation = new BDLocation();
	public MyLocationListenner myListener = new MyLocationListenner();
	public static String TAG = "msg";
	public String massage = null;
	// 获取位置管理工具
	// LocationManager locManager = null;
	// 经纬度
	// double longitudeNow = 125.316732;
	// double latitudeNow = 43.864758;
	// double height = 0;
	// float speed = 0;// 速度
	// float destination = 0;// 方向
	// GeoPoint geoPoint = null;
	private String myKey = "4NRzbDZNxpp59c3x3lv0VqrV";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		resolver=getContentResolver();
        resolver.query(Contacts.CONTENT_URI, new String[]{Contacts.NAME}, null, null, null);
		String str;
		System.out.println("!!!!!!!!!!!!!!!console");
		// 登陸
		 new Thread() {
		 public void run() {
		 HttpUtils.getJsonContent("chan", "3264",
		 "http://1.jludaohang.sinaapp.com/");
		 }
		
		 }.start();
		// 启动百度地图

		mapManager = new BMapManager(getApplication());
		mapManager.init(myKey, new MKGeneralListener() {
			// 检查AK是否正确
			@Override
			public void onGetPermissionState(int arg0) {
				// TODO Auto-generated method stub
				if (arg0 == 300) {
					Toast.makeText(MainActivity.this, "输入的key不正确！", 1).show();
					System.out.println("Wrong key!!!!");
				}
			}

			@Override
			public void onGetNetworkState(int arg0) {
				// TODO Auto-generated method stub

			}
		});
		setContentView(R.layout.activity_main);
		mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener(myListener);

		setLocationOption();// 设定定位参数

		mLocationClient.start();// 开始定位
		bMapView = (MapView) findViewById(R.id.bmapView);
		// 获得actionBar
//		actionBar = getActionBar();
		StringBuffer sb = new StringBuffer(256);
		sb.append("\nlatitude : ");
		sb.append(bdLocation.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(bdLocation.getLongitude());
		Log.i(TAG, sb.toString());
		// 缩放控制
		bMapView.setBuiltInZoomControls(true);
		MapController mapController = bMapView.getController();
		mapController.setZoom(16);

		/**
		 * 获取当前位置 125.269737,43.824363大学城的经纬度 116.403875,39.915168天安门
		 * 116.417863,39.914411王府井 116.403875,39.915168天安门
		 * 125.269737,43.824363大学城的经纬度 125.316732,43.864758南湖公园
		 * 125.316732,43.864758 百度长春 125.330315,43.888775长春体育馆
		 * 117.215987,39.141244天津
		 */
		// GeoPoint geoPoint = new GeoPoint((int) (location.getLatitude() *
		// 1E6),
		// (int) (location.getLongitude() * 1E6));
		// mapController.setCenter(geoPoint);

	}

	// 设置定位选项
	private void setLocationOption() {
		LocationClientOption clientOption = new LocationClientOption();
		clientOption.setLocationMode(LocationMode.Hight_Accuracy);// 设置为高精度
		clientOption.setCoorType("bd90ll");// 设置返回定位的结果为百度经纬度
		clientOption.setScanSpan(1500);// 设置发起定位请求的时间间隔为5000
		clientOption.setIsNeedAddress(true);// 设置是否返回地址信息
		clientOption.setNeedDeviceDirect(true);// 设置返回结果包含设备（手机机头）方向
		mLocationClient.setLocOption(clientOption);
	}

	/**
	 * 向服务器端发送并接收数据
	 * 
	 * @param userName
	 * @param password
	 * @param connectUrl
	 */
	 void getJsonContent(String userName, String password, String destination,
			String connectUrl) {
	
		String result = null; // 用来取得返回的String；
		boolean isLoginSucceed = false;
		// 发送post请求
		HttpPost httpRequest = new HttpPost(connectUrl);
		// Post运作传送变数必须用NameValuePair[]阵列储存
		List params = new ArrayList();
		params.add(new BasicNameValuePair("X", userName));
		params.add(new BasicNameValuePair("Y", password));
		params.add(new BasicNameValuePair("destination", destination));

		try {
			ContentValues values=new ContentValues();
			 Log.v("wocao", "我运行了");
			// 发出HTTP请求
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// 取得HTTP response
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);

			// 若状态码为200则请求成功，取到返回数据
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				 Log.v("wocao", "我运行了1");
				// 取出字符串
				result = EntityUtils.toString(httpResponse.getEntity());
				System.out.println("result= " + result);
				JSONArray arr = new JSONArray(result.toString());
				StringBuffer sb = new StringBuffer(256);
				for (int i = 0; i < arr.length(); i++) {
					JSONObject jsonObject = (JSONObject) arr.get(i);

					String re_username = jsonObject.getString("X");
					sb.append(re_username);
					String re_password = jsonObject.getString("Y");
					sb.append(re_password);
					Log.v("ttttttt", sb.toString());
					double re_longitude = jsonObject.getDouble("X");
					switch ((int) re_longitude) {
					case 0:
						new AlertDialog.Builder(this)
								.setMessage("您超出了服务范围！请在吉大校园内使用本应用！")
								.setPositiveButton("好", null).show();
						break;
					case 1:
						values.put(Contacts.NAME, re_password);
					   	resolver.insert(Contacts.CONTENT_URI, values);
					   	values.clear();
					   	Log.v("asdsada3", re_password);
						break;
					case 2:
						new AlertDialog.Builder(this)
								.setMessage("您输入的内容未找到匹配项！请查证后重新输入")
								.setPositiveButton("好", null).show();
						break;
					default:
						new AlertDialog.Builder(this).setMessage("马上可以获得路线！")
								.setPositiveButton("好", null).show();
					}
					Log.v("ttttttt", sb.toString());

				}
				// mTv.setText(sb.toString());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 判断返回的数据是否为php中成功登入是输出的

	}

	/**
	 * 实现BDlocationListener
	 * 
	 * @author Drakefrog
	 * 
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		// 接收位置信息
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			StringBuffer sb = new StringBuffer(256);
			// sb.append("time : ");
			// sb.append(location.getTime());
			// sb.append("\nerror code : ");
			// sb.append(location.getLocType());
			sb.append("\nlatitude : ");
			sb.append(location.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(location.getLongitude());
			sb.append("\nmassage");
			sb.append(massage);
			// sb.append("\nradius : ");
			// sb.append(location.getRadius());
			// if (location.getLocType() == BDLocation.TypeGpsLocation){
			// sb.append("\nspeed : ");
			// sb.append(location.getSpeed());
			// sb.append("\nsatellite : ");
			// sb.append(location.getSatelliteNumber());
			// } else if (location.getLocType() ==
			// BDLocation.TypeNetWorkLocation){
			// /**
			// * 格式化显示地址信息
			// */
			// sb.append("\naddr : ");
			// sb.append(location.getAddrStr());
			// }
			// sb.append("\nsdk version : ");
			// sb.append(mLocationClient.getVersion());
			// sb.append("\nisCellChangeFlag : ");
			// sb.append(location.isCellChangeFlag());
			final Double Y = 43.827784;
			final Double X = 125.289492;
			GeoPoint geoPoint = new GeoPoint((int) (Y * 1E6), (int) (X * 1E6));
			MapController mapController = bMapView.getController();
			mapController.setCenter(geoPoint);
			Log.v("ABCDEFG", "111111111111");
			final String destination = "经";
			// if (massage != null) {
			new Thread() {
				public void run() {
					getJsonContent(X.toString(), Y.toString(), destination,
							"http://1.jludaohang.sinaapp.com/");
				}

			}.start();
			// }

			// mTv.setText(sb.toString());
			Log.v("ABCDEFG", sb.toString());
		}

		// 接收POI信息函数，我不需要POI，所以我没有做处理
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		bMapView.destroy();
		mLocationClient.stop();// 停止定位
		if (mapManager != null) {
			mapManager.destroy();
			mapManager = null;
		}
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		bMapView.onPause();
		mLocationClient.stop();
		if (mapManager != null) {
			mapManager.stop();
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		bMapView.onResume();
		mLocationClient.start();
		if (mapManager != null) {
			mapManager.start();

		}
		super.onResume();
	}
	

	// 设置menu选项
	// 用于隐藏显示actionbar
	// public void showActionBar(View source) {
	// actionBar.show();
	// }
	//
	// public void hideActionBar(View source) {
	// actionBar.hide();
	// }

	// 获取actionBar中的按钮
	@Override
	
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
		SearchableInfo info=searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(info);
		searchView.setSubmitButtonEnabled(true);
		searchView.setIconifiedByDefault(true); 
		searchView.setOnSuggestionListener(new OnSuggestionListener(){

			@Override
			public boolean onSuggestionClick(int arg0) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean onSuggestionSelect(int arg0) {
				// TODO Auto-generated method stub
				return false;
			}
			
		});
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				massage = query;
				mLocationClient.requestLocation();
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		return super.onCreateOptionsMenu(menu);
	}
//	private void insertContacts(String re_password){
//   	 ContentValues values=new ContentValues();
//   	 values.put(Contacts.NAME, re_password);
//   	 resolver.insert(Contacts.CONTENT_URI, values);
//    }
}
