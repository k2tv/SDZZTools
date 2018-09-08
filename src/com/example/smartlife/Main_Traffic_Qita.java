package com.example.smartlife;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.example.smartlife_api.Login_Api;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import cn.sdzz.tools.R;

public class Main_Traffic_Qita extends Activity implements OnClickListener,OnTouchListener,SensorEventListener{ 
	  
	  private Button button_1,button_2,hqdw; 
	  private String sdPath;//SD����·�� 
	  private String picPath,picPathTemp;//ͼƬ�洢·�� 
	  private File destDir; 
	  private EditText name,hqdw_et,bz;
	  private String picname,dianweiname,dianweiId,b,sql,qyname;
	  private ImageView btn;
	  private long sql_int;
	  private Double long_t = 0.0, lat_t = 0.0; // Ĭ��ֵ
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
	 	setContentView(R.layout.main_traffic_qita); 		
	 	getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.main_traffic_qita_titlebar);
	  
	 	loct();
	    button_1 = (Button) findViewById(R.id.qita_button); 
	    button_2 = (Button) findViewById(R.id.qita_button_sj); 
	    name = (EditText) findViewById(R.id.qita_edittext);
	    hqdw = (Button) findViewById(R.id.main_traffic_qita_hqdw_btn);
	    hqdw_et = (EditText) findViewById(R.id.main_traffic_qita_hqdw_et);
	    bz =(EditText) findViewById(R.id.main_traffic_qita_bz);
	    
	    btn = (ImageView) findViewById(R.id.main_traffic_qita_title_left_btn);
	    
	    button_1.setOnTouchListener(this); 
	    button_2.setOnTouchListener(this);
	    btn.setOnClickListener(this);
	    hqdw.setOnTouchListener(this);
	    //��ȡSD����·�� 
	    sdPath = Environment.getExternalStorageDirectory().getPath();  
	    
	  //����
	  	sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	  	gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	  	fangxiang = (TextView) findViewById(R.id.qita_fangxiang);
	  	
	  //����ѡ��
	  Spinener();
	  	
	  } 
	  
	  @Override
	  public void onClick(View view) { 
	    switch (view.getId()) {   
	    case R.id.main_traffic_qita_title_left_btn:
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
		case R.id.qita_button:
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				button_1.setBackgroundResource(R.drawable.right_pen_max_clk);
			} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
				button_1.setBackgroundResource(R.drawable.right_pen_max);
				dianweiname = name.getText().toString();
				if (dianweiname.equals("")) {
					Toast.makeText(getBaseContext(), "�������λ����",Toast.LENGTH_SHORT).show();
				} else {
					update(dianweiId,sql, long_t+"", lat_t+"");
					TakePic();
				}
			}
			break;
		case R.id.qita_button_sj:
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				button_2.setBackgroundResource(R.drawable.right_pen_max_clk);
			} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
				button_2.setBackgroundResource(R.drawable.right_pen_max);
				dianweiname = name.getText().toString();
				if (dianweiname.equals("")) {
					Toast.makeText(getBaseContext(), "�������λ����",Toast.LENGTH_SHORT).show();
				} else {
					file();
					String fileName = dianweiname + "_������Ϣ_����.txt";
					b=bz.getText().toString();
					if(!b.equals("")){
						writeTxtToFile("������Ϣ��\n" + "��ע��"+b, picPathTemp,fileName);
					}else{
						Toast.makeText(getBaseContext(), "�����뱸ע��Ϣ",Toast.LENGTH_SHORT).show();
					}
					

					dianweiId = hqdw_et.getText().toString();	
				}
			}
			break;
		case R.id.main_traffic_qita_hqdw_btn:
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				hqdw.setBackgroundResource(R.drawable.right_pen_max_clk);
			} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
				hqdw.setBackgroundResource(R.drawable.right_pen_max);
				dianweiId = hqdw_et.getText().toString();
				if (dianweiId.equals("")) {
					Toast.makeText(getBaseContext(), "�������λ���",Toast.LENGTH_SHORT).show();
				} else {
					hqdw(dianweiId,sql);
				}
			}
		default:
			break;
		}
		return false;
	} 

	
	
	//����
	public Main_Traffic_Qita() {
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
			button_1.setText("��������");
			button_2.setText("����ע");
		}else if(event.values[0]>=225&&event.values[0]<315){
			fangxiangPath = "����";
			fangxiang.setText("����");
			button_1.setText("��������");
			button_2.setText("����ע");
		}else if(event.values[0]>=135&&event.values[0]<225){
			fangxiangPath = "����";
			fangxiang.setText("����");
			button_1.setText("��������");
			button_2.setText("����ע");
		}else if(event.values[0]>=45&&event.values[0]<135){
			fangxiangPath = "����";
			fangxiang.setText("����");
			button_1.setText("��������");
			button_2.setText("����ע");
		}
	}
	
	public void file() {
		// �ļ������
		picPathTemp = sdPath + "/0_PIC_SDZZ/����/" + qyname + "/" + dianweiname+ "/"+fangxiangPath+"/";
			destDir = new File(picPathTemp);
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
	}
	
	public void TakePic() {
		file();

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
	
	public void hqdw(String id,String qy) {
		String path = App.address + "getname.php?id="+id+"&qy="+qy;
		// Toast.makeText(getApplicationContext(), ""+path, Toast.LENGTH_LONG).show();
		HttpUtils http = new HttpUtils(10000);//10s��ʱ
		http.configCurrentHttpCacheExpiry(5000); // ���û���5��,5����ֱ�ӷ����ϴγɹ�����Ľ����
		http.send(HttpMethod.GET, path, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), "��������ʧ��", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				// �������� ת��ΪJSON��ʽ
				Login_Api content = (Login_Api) JSONObject.parseObject(arg0.result, Login_Api.class);
				if (content.getStatus().equals("102")|| content.getStatus().equals("101")) {
					Toast.makeText(getBaseContext(), "��λ�������",Toast.LENGTH_SHORT).show();
				} else if (content.getStatus().equals("103")) {
					name.setText(""+content.getName());
				} else {
					Toast.makeText(getBaseContext(), "δ֪����", Toast.LENGTH_SHORT).show();
					
				}
			}
		});
	}

	// ���ַ���д�뵽�ı��ļ���
	public void writeTxtToFile(String strcontent, String filePath,String fileName) {
		// �����ļ���֮���������ļ�����Ȼ�����

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
			Toast.makeText(getBaseContext(), "���ؼ�¼���",Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Log.e("TestFile", "Error on write File:" + e);
		}

	}
	
	public void Spinener() {
		 //����id��ȡ����  
		Spinner spinner; 
        spinner=(Spinner) findViewById(R.id.qita_spinner);  
         
      //����
        List<String> arr;
        arr = new ArrayList<String>();
        arr.add("�ŵ�");
        arr.add("��̨");
        arr.add("����");
        arr.add("����");
        arr.add("�ܴ�");
        arr.add("�Ĳ���");
        arr.add("����");
        arr.add("�ʹ�");
        
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arr);  
      //������ʽ
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       //������ʾ������  
        spinner.setAdapter(arrayAdapter);  
       // Toast.makeText(getApplicationContext(), "main Thread"+spinner.getItemIdAtPosition(spinner.getSelectedItemPosition()), Toast.LENGTH_LONG).show();  
        sql_int = spinner.getItemIdAtPosition(spinner.getSelectedItemPosition())+1;
        sql = sql_int+"";
        qyname = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
        //ע���¼�  
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {  
  
            @Override  
            public void onItemSelected(AdapterView<?> parent, View view,  
                    int position, long id) {  
                Spinner spinner=(Spinner) parent;  
               // Toast.makeText(getApplicationContext(), "xxxx"+spinner.getItemAtPosition(position), Toast.LENGTH_LONG).show();               
                sql_int = spinner.getItemIdAtPosition(spinner.getSelectedItemPosition())+1;
                sql = sql_int+"";
                qyname = spinner.getItemAtPosition(position).toString();
            }  
  
            @Override  
            public void onNothingSelected(AdapterView<?> parent) {  
                //Toast.makeText(getApplicationContext(), "û�иı�Ĵ���", Toast.LENGTH_LONG).show();  
            }  
  
        });  
       
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
		}
	}
	
	public void update(String id,String qy,String jd,String wd) {
		String path = App.address + "gps_sj.php?id="+id+"&qy="+qy+"+&jd="+jd+"&wd="+wd;
		//Toast.makeText(getBaseContext(), path, Toast.LENGTH_SHORT).show();
		HttpUtils http = new HttpUtils(10000);//10s��ʱ
		http.configCurrentHttpCacheExpiry(5000); // ���û���5��,5����ֱ�ӷ����ϴγɹ�����Ľ����
		http.send(HttpMethod.GET, path, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), "��������ʧ��", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				// �������� ת��ΪJSON��ʽ
				Login_Api content = (Login_Api) JSONObject.parseObject(arg0.result, Login_Api.class);
				if (content.getStatus().equals("102")|| content.getStatus().equals("101")) {
					Toast.makeText(getBaseContext(), "��λ��������",Toast.LENGTH_SHORT).show();
				} else if (content.getStatus().equals("103")) {
					Toast.makeText(getBaseContext(), "�ɹ��ϴ���λ����",Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getBaseContext(), "δ֪����", Toast.LENGTH_SHORT).show();
					
				}
			}
		});
	}
} 
