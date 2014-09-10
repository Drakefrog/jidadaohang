package com.example.jidadaohang;

import java.nio.channels.FileChannel.MapMode;
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
import org.json.JSONException;
import org.json.JSONObject;

//百度定位所需相关类
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

//百度地图所需相关类
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.widget.FrameLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.map.TextOverlay;
import com.baidu.mapapi.map.Symbol.Color;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.example.jidadaohang.R;

public class MainActivity extends Activity {
	ContentResolver resolver;
	MyLocationOverlay myLocationOverlay;
	public LocationData mlocationData = new LocationData();
	// 创建ActionBar
	private ActionBar actionBar;
	private BMapManager mapManager;
	private MapView bMapView;
	public LocationClient mLocationClient = null;
	BDLocation bdLocation = new BDLocation();
	public MyLocationListenner myListener = new MyLocationListenner();
	public static String TAG = "msg";
	public String massage = null;
	public JSONArray jsonArray = null;
	public Double latitude = 43.830714;
	public Double longitude = 125.285099;
	boolean isFirstLocate = true;
	private String myKey = "NG55N7Mxcz3hu1KHLlyiY5hT";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		resolver = getContentResolver();
		resolver.query(Contacts.CONTENT_URI, new String[] { Contacts.NAME },
				null, null, null);
		String str;
		System.out.println("!!!!!!!!!!!!!!!console");
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
		myLocationOverlay = new MyLocationOverlay(bMapView);
		
		bMapView.getOverlays().add(myLocationOverlay);
		// 获得actionBar
		actionBar = getActionBar();
		// MyLocationOverlay myLocationOverlay = new
		// MyLocationOverlay(bMapView);
		// LocationData mlocationData = new LocationData();
		// if (massage != null) {
		new Thread() {
			public void run() {
				getJsonContent(Double.toString(125.289344),
						Double.toString(43.827733), "",
						"http://1.jludaohang.sinaapp.com/");
			}

		}.start();
		// }
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
		// clientOption.setLocationMode(LocationMode.Device_Sensors);// 设置为高精度
		// clientOption.setAddrType("all");// 返回的定位结果包含地址信息
		clientOption.setOpenGps(true);
		clientOption.setProdName("jidadaohang");
		clientOption.setCoorType("bd90ll");// 设置返回定位的结果为百度经纬度
		clientOption.setScanSpan(2000);// 设置发起定位请求的时间间隔为5000
		// clientOption.setPriority(LocationClientOption.GpsFirst); // 设置定位优先级
		// clientOption.disableCache(true);// 禁止启用缓存定位
		// clientOption.setIsNeedAddress(true);// 设置是否返回地址信息
		// clientOption.setPoiNumber(5); // 最多返回POI个数
		// clientOption.setPoiDistance(1000); // poi查询距离
		// clientOption.setPoiExtraInfo(true); // 是否需要POI的电话和地址等详细信息
		// clientOption.setNeedDeviceDirect(true);// 设置返回结果包含设备（手机机头）方向
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
			ContentValues values = new ContentValues();
			// 发出HTTP请求
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// 取得HTTP response
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);

			// 若状态码为200则请求成功，取到返回数据
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 取出字符串
				result = EntityUtils.toString(httpResponse.getEntity());
				System.out.println("result= " + result);
				JSONArray arr = new JSONArray(result.toString());
				StringBuffer sb = new StringBuffer(256);
				JSONObject obj = (JSONObject) arr.get(0);
				double re_longitude = obj.getDouble("X");
				switch ((int) re_longitude) {
				case 0:
					Looper.prepare();
					new AlertDialog.Builder(this)
							.setMessage("您超出了服务范围！请在吉大校园内使用本应用！")
							.setPositiveButton("好", null).show();
					Looper.loop();
					break;
				case 1:
					for (int i = 0; i < arr.length(); i++) {
						JSONObject jsonObject = (JSONObject) arr.get(i);
						String re_username = jsonObject.getString("X");
						sb.append(re_username);
						String re_password = jsonObject.getString("Y");
						sb.append(re_password);
						values.put(Contacts.NAME, obj.getString("Y"));
						resolver.insert(Contacts.CONTENT_URI, values);
						values.clear();
						Log.v("asdsada3", obj.getString("Y"));
					}
					break;
				case 2:
					Looper.prepare();
					new AlertDialog.Builder(this)
							.setMessage("您输入的内容未找到匹配项！请查证后重新输入")
							.setPositiveButton("好", null).show();
					Looper.loop();
					break;
				default:
					bMapView.getOverlays().clear();
			    	bMapView.refresh();
			    	bMapView.getOverlays().add(myLocationOverlay);
					jsonArray = arr;
					addCustomElementsDemo(arr);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// 判断返回的数据是否为php中成功登入是输出的

	}

	/**
	 * 添加点、线、多边形、圆、文字
	 * 
	 * @throws JSONException
	 */
	public void addCustomElementsDemo(JSONArray arr) throws JSONException {
		GraphicsOverlay graphicsOverlay = new GraphicsOverlay(bMapView);
		bMapView.getOverlays().add(graphicsOverlay);
		// 添加点
		// graphicsOverlay.setData(drawPoint());
		// 添加折线
		graphicsOverlay.setData(drawLine(arr));
		// //添加弧线
		// graphicsOverlay.setData(drawArc());
		// //添加多边形
		// graphicsOverlay.setData(drawPolygon());
		// //添加圆
		// graphicsOverlay.setData(drawCircle());
		// //绘制文字
		// TextOverlay textOverlay = new TextOverlay(mMapView);
		// mMapView.getOverlays().add(textOverlay);
		// textOverlay.addText(drawText());
		// 执行地图刷新使生效
		bMapView.refresh();
	}

	/**
	 * 绘制折线，该折线状态随地图状态变化
	 * 
	 * @return 折线对象
	 * @throws JSONException
	 */
	public Graphic drawLine(JSONArray arr) throws JSONException {
		double mLat = 39.97923;
		double mLon = 116.357428;

		int lat = (int) (mLat * 1E6);
		int lon = (int) (mLon * 1E6);
		// GeoPoint pt1 = new GeoPoint(lat, lon);
		//
		// mLat = 39.94923;
		// mLon = 116.397428;
		// lat = (int) (mLat * 1E6);
		// lon = (int) (mLon * 1E6);
		// GeoPoint pt2 = new GeoPoint(lat, lon);
		// mLat = 39.97923;
		// mLon = 116.437428;
		// lat = (int) (mLat * 1E6);
		// lon = (int) (mLon * 1E6);
		// GeoPoint pt3 = new GeoPoint(lat, lon);
		//
		// mLat = 40.07923;
		// mLon = 116.457428;
		// lat = (int) (mLat * 1E6);
		// lon = (int) (mLon * 1E6);
		// GeoPoint pt4 = new GeoPoint(lat, lon);

		// 构建线
		Geometry lineGeometry = new Geometry();
		// 设定折线点坐标
		GeoPoint[] linePoints = new GeoPoint[arr.length()];
		for (int i = 0; i < arr.length(); i++) {
			JSONObject jsonObject = (JSONObject) arr.get(i);

			mLat = jsonObject.getDouble("X");
			mLon = jsonObject.getDouble("Y");
			lat = (int) (mLat * 1E6);
			lon = (int) (mLon * 1E6);
			System.out.println(mLat + " " + " " + mLon);
			linePoints[i] = new GeoPoint(lon, lat);

		}
		// linePoints[0] = pt1;
		// linePoints[1] = pt2;
		// linePoints[2] = pt3;
		// linePoints[3] = pt4;
		lineGeometry.setPolyLine(linePoints);
		// 设定样式
		Symbol lineSymbol = new Symbol();
		Symbol.Color lineColor = lineSymbol.new Color();
		lineColor.red = 255;
		lineColor.green = 0;
		lineColor.blue = 0;
		lineColor.alpha = 255;
		lineSymbol.setLineSymbol(lineColor, 10);
		// 生成Graphic对象
		Graphic lineGraphic = new Graphic(lineGeometry, lineSymbol);
		return lineGraphic;
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
			sb.append("time : ");
			sb.append(location.getTime());
			sb.append("\nerror code : ");
			sb.append(location.getLocType());
			
			// sb.append(location.getLatitude());
			
			// sb.append(location.getLongitude());
			sb.append("\nmassage");
			sb.append(massage);
			sb.append("\nradius : ");
			sb.append(location.getRadius());
			sb.append("\nlatitude : ");
			double lati = location.getLatitude() + 0.0064718;
			latitude = lati ;
			sb.append(lati);
			sb.append("\nlontitude : ");
			double longit = location.getLongitude() + 0.0064412;
			longitude = longit ;
			sb.append(longit);

			// sb.append(location.isCellChangeFlag());125.277576,43.826139
			// 小的是latitude 大的是longitude
			// 小的在前
			mlocationData.latitude = lati;
			mlocationData.longitude = longit;
			// mlocationData.direction = location.getDirection();
			mlocationData.accuracy = location.getRadius();
			myLocationOverlay.setData(mlocationData);
			GeoPoint geoPoint = new GeoPoint((int) (lati * 1E6),
					(int) (longit * 1E6));
			if (isFirstLocate) {
				MapController mapController = bMapView.getController();
				mapController.setCenter(geoPoint);
			}
			bMapView.refresh();
			// mTv.setText(sb.toString());
			Log.v("ABCDEFG", sb.toString());
			isFirstLocate = false;
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
		SearchView searchView = (SearchView) menu.findItem(R.id.action_search)
				.getActionView();
		SearchableInfo info = searchManager
				.getSearchableInfo(getComponentName());
		searchView.setSearchableInfo(info);
		searchView.setSubmitButtonEnabled(true);
		searchView.setIconifiedByDefault(true);
		searchView.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				// TODO Auto-generated method stub
				massage = query;
				mLocationClient.requestLocation();
				Log.v("latitude", latitude.toString());
				Log.v("longitude", longitude.toString());
				if (massage != null) {
					new Thread() {
						public void run() {
							getJsonContent(longitude.toString(),
									latitude.toString(), massage,
									"http://1.jludaohang.sinaapp.com/");
						}

					}.start();
				}
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

}
