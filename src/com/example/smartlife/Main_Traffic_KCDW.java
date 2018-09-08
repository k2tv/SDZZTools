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
	private String sdPath;// SD����·��
	private String picPath, picPathTemp;// ͼƬ�洢·��
	private File destDir;
	private EditText name;
	private String picname, dianweiname;
	private ImageView btn;
	private Double long_t = 0.0, lat_t = 0.0; // Ĭ��ֵ
	private String placename;
	//����
	private SensorManager sensorManager = null;
	private Sensor gyroSensor = null;
	private float[] angle = new float[3];
	private TextView fangxiang;
	private String fangxiangPath = "δ֪����";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.activity_main);

		// �Զ���TitleBar
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.main_traffic);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.main_traffic_dwkc_titlebar);

		button = (Button) findViewById(R.id.kandian_button);
		name = (EditText) findViewById(R.id.kandian_edittext);
		fangxiang = (TextView) findViewById(R.id.kandian_fangxiang);

		btn = (ImageView) findViewById(R.id.main_traffic_dwkc_title_left_btn);

		button.setOnTouchListener(this);

		btn.setOnClickListener(this);
		// ��ȡSD����·��
		sdPath = Environment.getExternalStorageDirectory().getPath();
		
		//����
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
					Toast.makeText(getBaseContext(), "�������λ����",Toast.LENGTH_SHORT).show();
				} else {
					loct();
					picPathTemp = sdPath + "/0_PIC_SDZZ/��λ����/" + dianweiname+ "/"+fangxiangPath+"/";
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
		// �ļ������
		destDir = new File(picPathTemp);
		if (!destDir.exists()) {
			destDir.mkdirs();
		}

		String fileName = dianweiname + "_λ����Ϣ.txt";
		writeTxtToFile("λ����Ϣ\n���ȣ�" + lat_t + "γ�ȣ�" + long_t+"\n��ַ��"+placename+"\n\n", picPathTemp,fileName);

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
		// Ϊ�����ͼƬָ��һ���洢��·��
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		// startActivityForResult(intent, REQUEST_ORIGINAL);
		startActivity(intent);
	}

	public void loct() {
		// λ����Ϣ

		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		LocationListener locationListener = new LocationListener() {

			// Provider��״̬�ڿ��á���ʱ�����ú��޷�������״ֱ̬���л�ʱ�����˺���
			@Override
			public void onStatusChanged(String provider, int status,
					Bundle extras) {
				Log.e("Map", "onStatusChanged");
			}

			// Provider��enableʱ�����˺���������GPS����
			@Override
			public void onProviderEnabled(String provider) {
				Log.e("Map", "onProviderEnabled");
			}

			// Provider��disableʱ�����˺���������GPS���ر�

			@Override
			public void onProviderDisabled(String provider) {
				Log.e("Map", "onProviderDisabled");
			}

			// ������ı�ʱ�����˺��������Provider������ͬ�����꣬���Ͳ��ᱻ����
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
			lat_t = location.getLatitude(); // ����
			long_t = location.getLongitude(); // γ��

			// ��ȡ��ȷλ��///////////////////////////////////////////////////////////////////////////
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
				// һ�µ���Ϣ������嵽ĳ����
				// ����getAddressLine(0)��ʾ���ң�getAddressLine(1)��ʾ��ȷ��ĳ������getAddressLine(2)��ʾ��ȷ������Ľ�
				placename = ((Address) places.get(0)).getAddressLine(0) + ", "
						//+ System.getProperty("line.separator")
						+ ((Address) places.get(0)).getAddressLine(1) + ", "
						+ ((Address) places.get(0)).getAddressLine(2);
			}
			// latLongString = "γ��:" + lat + "/n����:" + lng;
			//Toast.makeText(getBaseContext(), placename, Toast.LENGTH_LONG).show();
		} else {
			// latLongString = "�޷���ȡ������Ϣ";
			placename =  "�޷���ȡ������Ϣ";
		}
		// myLocationText.setText("����ǰ��λ����:/n" + latLongString);

	}

	// ���ַ���д�뵽�ı��ļ���
	public void writeTxtToFile(String strcontent, String filePath,
			String fileName) {
		// �����ļ���֮���������ļ�����Ȼ�����
		// makeFilePath(filePath, fileName);

		String strFilePath = filePath + fileName;
		// ÿ��д��ʱ��������д
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
	
	//����
	public Main_Traffic_KCDW() {
		// TODO Auto-generated constructor stub
		angle[0] = 0;
	}


	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		sensorManager.unregisterListener(this); // ���������ע��
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		sensorManager.registerListener(this, gyroSensor,SensorManager.SENSOR_DELAY_NORMAL); // Ϊ������ע�������
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSensorChanged(SensorEvent event) {

		// ���򴫸����ṩ�������ݣ��ֱ�Ϊazimuth��pitch��roll��
		//
		// azimuth����λ������ˮƽʱ�ű�����Y��ļнǣ���ΧΪ0����360�㡣
		// 0��=����90��=����180��=�ϣ�270��=����
		//
		// pitch��x���ˮƽ��ļнǣ���ΧΪ-180����180�㡣
		// ��z����y��ת��ʱ���Ƕ�Ϊ��ֵ��
		//
		// roll��y���ˮƽ��ļнǣ�������ʷԭ�򣬷�ΧΪ-90����90�㡣
		// ��x����z���ƶ�ʱ���Ƕ�Ϊ��ֵ��
		if(event.values[0]<=45||event.values[0]>=315){
			fangxiangPath = "����";
			fangxiang.setText("����");
			button.setText("��������");
		}else if(event.values[0]>=225&&event.values[0]<315){
			fangxiangPath = "����";
			fangxiang.setText("����");
			button.setText("��������");
		}else if(event.values[0]>=135&&event.values[0]<225){
			fangxiangPath = "����";
			fangxiang.setText("����");
			button.setText("��������");
		}else if(event.values[0]>=45&&event.values[0]<135){
			fangxiangPath = "����";
			fangxiang.setText("����");
			button.setText("��������");
		}
		
	}

}
