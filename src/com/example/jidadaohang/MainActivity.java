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
//�ٶȶ�λ���������
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
//�ٶȵ�ͼ���������
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
	// ����ActionBar
//	private ActionBar actionBar;
	private BMapManager mapManager;
	private MapView bMapView;
	public LocationClient mLocationClient = null;
	BDLocation bdLocation = new BDLocation();
	public MyLocationListenner myListener = new MyLocationListenner();
	public static String TAG = "msg";
	public String massage = null;
	// ��ȡλ�ù�����
	// LocationManager locManager = null;
	// ��γ��
	// double longitudeNow = 125.316732;
	// double latitudeNow = 43.864758;
	// double height = 0;
	// float speed = 0;// �ٶ�
	// float destination = 0;// ����
	// GeoPoint geoPoint = null;
	private String myKey = "4NRzbDZNxpp59c3x3lv0VqrV";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		resolver=getContentResolver();
        resolver.query(Contacts.CONTENT_URI, new String[]{Contacts.NAME}, null, null, null);
		String str;
		System.out.println("!!!!!!!!!!!!!!!console");
		// ���
		 new Thread() {
		 public void run() {
		 HttpUtils.getJsonContent("chan", "3264",
		 "http://1.jludaohang.sinaapp.com/");
		 }
		
		 }.start();
		// �����ٶȵ�ͼ

		mapManager = new BMapManager(getApplication());
		mapManager.init(myKey, new MKGeneralListener() {
			// ���AK�Ƿ���ȷ
			@Override
			public void onGetPermissionState(int arg0) {
				// TODO Auto-generated method stub
				if (arg0 == 300) {
					Toast.makeText(MainActivity.this, "�����key����ȷ��", 1).show();
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

		setLocationOption();// �趨��λ����

		mLocationClient.start();// ��ʼ��λ
		bMapView = (MapView) findViewById(R.id.bmapView);
		// ���actionBar
//		actionBar = getActionBar();
		StringBuffer sb = new StringBuffer(256);
		sb.append("\nlatitude : ");
		sb.append(bdLocation.getLatitude());
		sb.append("\nlontitude : ");
		sb.append(bdLocation.getLongitude());
		Log.i(TAG, sb.toString());
		// ���ſ���
		bMapView.setBuiltInZoomControls(true);
		MapController mapController = bMapView.getController();
		mapController.setZoom(16);

		/**
		 * ��ȡ��ǰλ�� 125.269737,43.824363��ѧ�ǵľ�γ�� 116.403875,39.915168�찲��
		 * 116.417863,39.914411������ 116.403875,39.915168�찲��
		 * 125.269737,43.824363��ѧ�ǵľ�γ�� 125.316732,43.864758�Ϻ���԰
		 * 125.316732,43.864758 �ٶȳ��� 125.330315,43.888775����������
		 * 117.215987,39.141244���
		 */
		// GeoPoint geoPoint = new GeoPoint((int) (location.getLatitude() *
		// 1E6),
		// (int) (location.getLongitude() * 1E6));
		// mapController.setCenter(geoPoint);

	}

	// ���ö�λѡ��
	private void setLocationOption() {
		LocationClientOption clientOption = new LocationClientOption();
		clientOption.setLocationMode(LocationMode.Hight_Accuracy);// ����Ϊ�߾���
		clientOption.setCoorType("bd90ll");// ���÷��ض�λ�Ľ��Ϊ�ٶȾ�γ��
		clientOption.setScanSpan(1500);// ���÷���λ�����ʱ����Ϊ5000
		clientOption.setIsNeedAddress(true);// �����Ƿ񷵻ص�ַ��Ϣ
		clientOption.setNeedDeviceDirect(true);// ���÷��ؽ�������豸���ֻ���ͷ������
		mLocationClient.setLocOption(clientOption);
	}

	/**
	 * ��������˷��Ͳ���������
	 * 
	 * @param userName
	 * @param password
	 * @param connectUrl
	 */
	 void getJsonContent(String userName, String password, String destination,
			String connectUrl) {
	
		String result = null; // ����ȡ�÷��ص�String��
		boolean isLoginSucceed = false;
		// ����post����
		HttpPost httpRequest = new HttpPost(connectUrl);
		// Post�������ͱ���������NameValuePair[]���д���
		List params = new ArrayList();
		params.add(new BasicNameValuePair("X", userName));
		params.add(new BasicNameValuePair("Y", password));
		params.add(new BasicNameValuePair("destination", destination));

		try {
			ContentValues values=new ContentValues();
			 Log.v("wocao", "��������");
			// ����HTTP����
			httpRequest.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			// ȡ��HTTP response
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);

			// ��״̬��Ϊ200������ɹ���ȡ����������
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				 Log.v("wocao", "��������1");
				// ȡ���ַ���
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
								.setMessage("�������˷���Χ�����ڼ���У԰��ʹ�ñ�Ӧ�ã�")
								.setPositiveButton("��", null).show();
						break;
					case 1:
						values.put(Contacts.NAME, re_password);
					   	resolver.insert(Contacts.CONTENT_URI, values);
					   	values.clear();
					   	Log.v("asdsada3", re_password);
						break;
					case 2:
						new AlertDialog.Builder(this)
								.setMessage("�����������δ�ҵ�ƥ������֤����������")
								.setPositiveButton("��", null).show();
						break;
					default:
						new AlertDialog.Builder(this).setMessage("���Ͽ��Ի��·�ߣ�")
								.setPositiveButton("��", null).show();
					}
					Log.v("ttttttt", sb.toString());

				}
				// mTv.setText(sb.toString());

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		// �жϷ��ص������Ƿ�Ϊphp�гɹ������������

	}

	/**
	 * ʵ��BDlocationListener
	 * 
	 * @author Drakefrog
	 * 
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		// ����λ����Ϣ
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
			// * ��ʽ����ʾ��ַ��Ϣ
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
			final String destination = "��";
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

		// ����POI��Ϣ�������Ҳ���ҪPOI��������û��������
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
		mLocationClient.stop();// ֹͣ��λ
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
	

	// ����menuѡ��
	// ����������ʾactionbar
	// public void showActionBar(View source) {
	// actionBar.show();
	// }
	//
	// public void hideActionBar(View source) {
	// actionBar.hide();
	// }

	// ��ȡactionBar�еİ�ť
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
