package com.example.smartlife;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sdzz.tools.R;

public class Main_Traffic_KCDW extends Activity implements OnClickListener,OnTouchListener,SensorEventListener {

	private Button button;
	private String sdPath;// SD卡的路径
	private String picPath, picPathTemp;// 图片存储路径
	private File destDir;
	private EditText name;
	private String picname, dianweiname;
	private ImageView btn;
	private Double long_t = 0.0, lat_t = 0.0; // 默认值
	private String placename;
	//方向
	private SensorManager sensorManager = null;
	private Sensor gyroSensor = null;
	private float[] angle = new float[3];
	private TextView fangxiang;
	private String fangxiangPath = "未知方向";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);

		// 自定义TitleBar
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main_traffic);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.main_traffic_dwkc_titlebar);

		button = (Button) findViewById(R.id.kandian_button);
		name = (EditText) findViewById(R.id.kandian_edittext);
		fangxiang = (TextView) findViewById(R.id.kandian_fangxiang);

		btn = (ImageView) findViewById(R.id.main_traffic_dwkc_title_left_btn);

		button.setOnTouchListener(this);

		btn.setOnClickListener(this);
		// 获取SD卡的路径
		sdPath = Environment.getExternalStorageDirectory().getPath();
		
		//方向
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.main_traffic_dwkc_title_left_btn:
			this.finish();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.kandian_button:
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				button.setBackgroundResource(R.drawable.right_pen_max_clk);
			} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
				button.setBackgroundResource(R.drawable.right_pen_max);
				dianweiname = name.getText().toString();
				if (dianweiname.equals("")) {
					Toast.makeText(getBaseContext(), "请输入点位名称",Toast.LENGTH_SHORT).show();
				} else {
					loct();
					picPathTemp = sdPath + "/0_PIC_SDZZ/点位勘察/" + dianweiname+ "/"+fangxiangPath+"/";
					TakePic();
				}
			}
			break;
		default:
			break;
		}
		return false;
	}

	public void TakePic() {
		// 文件夹相关
		destDir = new File(picPathTemp);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		String fileName = dianweiname + "_位置信息.txt";
		writeTxtToFile("位置信息\n经度：" + lat_t + "纬度：" + long_t+"\n地址："+placename+"\n\n", picPathTemp,fileName);

		Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);
		int sec = c.get(Calendar.SECOND);
		picname = String.format("%02d", year) + String.format("%02d", month)
				+ String.format("%02d", day) + String.format("%02d", hour)
				+ String.format("%02d", minute) + String.format("%02d", sec);

		picPath = picPathTemp + "/" + picname + ".png";
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri uri = Uri.fromFile(new File(picPath));
		// 为拍摄的图片指定一个存储的路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		// startActivityForResult(intent, REQUEST_ORIGINAL);
		startActivity(intent);
	}

	public void loct() {
		// 位置信息

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		LocationListener locationListener = new LocationListener() {

			// Provider的状态在可用、暂时不可用和无服务三个状态直接切换时触发此函数
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				Log.e("Map", "onStatusChanged");
			}

			// Provider被enable时触发此函数，比如GPS被打开
			@Override
			public void onProviderEnabled(String provider) {
				Log.e("Map", "onProviderEnabled");
			}

			// Provider被disable时触发此函数，比如GPS被关闭

			@Override
			public void onProviderDisabled(String provider) {
				Log.e("Map", "onProviderDisabled");
			}

			// 当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
			@Override
			public void onLocationChanged(Location location) {
				if (location != null) {
					Log.e("Map","Location changed : Lat: " + location.getLatitude()+ " Lng: " + location.getLongitude());
				}
			}
		};
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 1000, 0, locationListener);
		Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		if (location != null) {
			lat_t = location.getLatitude(); // 经度
			long_t = location.getLongitude(); // 纬度

			// 获取精确位置///////////////////////////////////////////////////////////////////////////
			Geocoder geocoder = new Geocoder(this);
			// Geocoder geocoder = new Geocoder(this, Locale.CHINA);
			List places = null;

			try {
				// Thread.sleep(2000);
				places = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(), 5);
				// Thread.sleep(2000);
				//Toast.makeText(getBaseContext(), places.size() + "",Toast.LENGTH_LONG).show();
				System.out.println(places.size() + "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			placename = "";
			if (places != null && places.size() > 0) {
				// placename=((Address)places.get(0)).getLocality();
				// 一下的信息将会具体到某条街
				// 其中getAddressLine(0)表示国家，getAddressLine(1)表示精确到某个区，getAddressLine(2)表示精确到具体的街
				placename = ((Address) places.get(0)).getAddressLine(0) + ", "
						//+ System.getProperty("line.separator")
						+ ((Address) places.get(0)).getAddressLine(1) + ", "
						+ ((Address) places.get(0)).getAddressLine(2);
			}
			// latLongString = "纬度:" + lat + "/n经度:" + lng;
			//Toast.makeText(getBaseContext(), placename, Toast.LENGTH_LONG).show();
		} else {
			// latLongString = "无法获取地理信息";
			placename =  "无法获取地理信息";
		}
		// myLocationText.setText("您当前的位置是:/n" + latLongString);

	}

	// 将字符串写入到文本文件中
	public void writeTxtToFile(String strcontent, String filePath,
			String fileName) {
		// 生成文件夹之后，再生成文件，不然会出错
		// makeFilePath(filePath, fileName);

		String strFilePath = filePath + fileName;
		// 每次写入时，都换行写
		String strContent = strcontent + "\r\n";
		try {
			File file = new File(strFilePath);
			if (!file.exists()) {
				Log.d("TestFile", "Create the file:" + strFilePath);
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			RandomAccessFile raf = new RandomAccessFile(file, "rwd");
			raf.seek(file.length());
			raf.write(strContent.getBytes());
			raf.close();
		} catch (Exception e) {
			Log.e("TestFile", "Error on write File:" + e);
		}

	}
	
	//方向
	public Main_Traffic_KCDW() {
		// TODO Auto-generated constructor stub
		angle[0] = 0;
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sensorManager.unregisterListener(this); // 解除监听器注册
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		sensorManager.registerListener(this, gyroSensor,SensorManager.SENSOR_DELAY_NORMAL); // 为传感器注册监听器
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		// 方向传感器提供三个数据，分别为azimuth、pitch和roll。
		//
		// azimuth：方位，返回水平时磁北极和Y轴的夹角，范围为0°至360°。
		// 0°=北，90°=东，180°=南，270°=西。
		//
		// pitch：x轴和水平面的夹角，范围为-180°至180°。
		// 当z轴向y轴转动时，角度为正值。
		//
		// roll：y轴和水平面的夹角，由于历史原因，范围为-90°至90°。
		// 当x轴向z轴移动时，角度为正值。
		if(event.values[0]<=45||event.values[0]>=315){
			fangxiangPath = "北向";
			fangxiang.setText("北向");
			button.setText("北向拍照");
		}else if(event.values[0]>=225&&event.values[0]<315){
			fangxiangPath = "西向";
			fangxiang.setText("西向");
			button.setText("西向拍照");
		}else if(event.values[0]>=135&&event.values[0]<225){
			fangxiangPath = "南向";
			fangxiang.setText("南向");
			button.setText("南向拍照");
		}else if(event.values[0]>=45&&event.values[0]<135){
			fangxiangPath = "东向";
			fangxiang.setText("东向");
			button.setText("东向拍照");
		}
		
	}

}
