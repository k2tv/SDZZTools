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
	  private String sdPath;//SD卡的路径 
	  private String picPath,picPathTemp;//图片存储路径 
	  private File destDir; 
	  private EditText name,hqdw_et,bz;
	  private String picname,dianweiname,dianweiId,b,sql,qyname;
	  private ImageView btn;
	  private long sql_int;
	  private Double long_t = 0.0, lat_t = 0.0; // 默认值
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
	    //获取SD卡的路径 
	    sdPath = Environment.getExternalStorageDirectory().getPath();  
	    
	  //方向
	  	sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
	  	gyroSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
	  	fangxiang = (TextView) findViewById(R.id.qita_fangxiang);
	  	
	  //下拉选项
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
					Toast.makeText(getBaseContext(), "请输入点位名称",Toast.LENGTH_SHORT).show();
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
					Toast.makeText(getBaseContext(), "请输入点位名称",Toast.LENGTH_SHORT).show();
				} else {
					file();
					String fileName = dianweiname + "_数据信息_浇筑.txt";
					b=bz.getText().toString();
					if(!b.equals("")){
						writeTxtToFile("数据信息：\n" + "备注："+b, picPathTemp,fileName);
					}else{
						Toast.makeText(getBaseContext(), "请输入备注信息",Toast.LENGTH_SHORT).show();
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
					Toast.makeText(getBaseContext(), "请输入点位编号",Toast.LENGTH_SHORT).show();
				} else {
					hqdw(dianweiId,sql);
				}
			}
		default:
			break;
		}
		return false;
	} 

	
	
	//方向
	public Main_Traffic_Qita() {
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
			button_1.setText("北向拍照");
			button_2.setText("北向备注");
		}else if(event.values[0]>=225&&event.values[0]<315){
			fangxiangPath = "西向";
			fangxiang.setText("西向");
			button_1.setText("西向拍照");
			button_2.setText("西向备注");
		}else if(event.values[0]>=135&&event.values[0]<225){
			fangxiangPath = "南向";
			fangxiang.setText("南向");
			button_1.setText("南向拍照");
			button_2.setText("南向备注");
		}else if(event.values[0]>=45&&event.values[0]<135){
			fangxiangPath = "东向";
			fangxiang.setText("东向");
			button_1.setText("东向拍照");
			button_2.setText("东向备注");
		}
	}
	
	public void file() {
		// 文件夹相关
		picPathTemp = sdPath + "/0_PIC_SDZZ/其他/" + qyname + "/" + dianweiname+ "/"+fangxiangPath+"/";
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
		// 为拍摄的图片指定一个存储的路径
		intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
		// startActivityForResult(intent, REQUEST_ORIGINAL);
		startActivity(intent);
	}
	
	public void hqdw(String id,String qy) {
		String path = App.address + "getname.php?id="+id+"&qy="+qy;
		// Toast.makeText(getApplicationContext(), ""+path, Toast.LENGTH_LONG).show();
		HttpUtils http = new HttpUtils(10000);//10s超时
		http.configCurrentHttpCacheExpiry(5000); // 设置缓存5秒,5秒内直接返回上次成功请求的结果。
		http.send(HttpMethod.GET, path, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				// 加载数据 转化为JSON格式
				Login_Api content = (Login_Api) JSONObject.parseObject(arg0.result, Login_Api.class);
				if (content.getStatus().equals("102")|| content.getStatus().equals("101")) {
					Toast.makeText(getBaseContext(), "点位编号有误",Toast.LENGTH_SHORT).show();
				} else if (content.getStatus().equals("103")) {
					name.setText(""+content.getName());
				} else {
					Toast.makeText(getBaseContext(), "未知错误", Toast.LENGTH_SHORT).show();
					
				}
			}
		});
	}

	// 将字符串写入到文本文件中
	public void writeTxtToFile(String strcontent, String filePath,String fileName) {
		// 生成文件夹之后，再生成文件，不然会出错

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
			Toast.makeText(getBaseContext(), "本地记录完成",Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Log.e("TestFile", "Error on write File:" + e);
		}

	}
	
	public void Spinener() {
		 //根据id获取对象  
		Spinner spinner; 
        spinner=(Spinner) findViewById(R.id.qita_spinner);  
         
      //数据
        List<String> arr;
        arr = new ArrayList<String>();
        arr.add("张店");
        arr.add("桓台");
        arr.add("高新");
        arr.add("高青");
        arr.add("周村");
        arr.add("文昌湖");
        arr.add("临淄");
        arr.add("淄川");
        
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, arr);  
      //设置样式
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       //设置显示的数据  
        spinner.setAdapter(arrayAdapter);  
       // Toast.makeText(getApplicationContext(), "main Thread"+spinner.getItemIdAtPosition(spinner.getSelectedItemPosition()), Toast.LENGTH_LONG).show();  
        sql_int = spinner.getItemIdAtPosition(spinner.getSelectedItemPosition())+1;
        sql = sql_int+"";
        qyname = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
        //注册事件  
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
                //Toast.makeText(getApplicationContext(), "没有改变的处理", Toast.LENGTH_LONG).show();  
            }  
  
        });  
       
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
		}
	}
	
	public void update(String id,String qy,String jd,String wd) {
		String path = App.address + "gps_sj.php?id="+id+"&qy="+qy+"+&jd="+jd+"&wd="+wd;
		//Toast.makeText(getBaseContext(), path, Toast.LENGTH_SHORT).show();
		HttpUtils http = new HttpUtils(10000);//10s超时
		http.configCurrentHttpCacheExpiry(5000); // 设置缓存5秒,5秒内直接返回上次成功请求的结果。
		http.send(HttpMethod.GET, path, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), "网络连接失败", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				// 加载数据 转化为JSON格式
				Login_Api content = (Login_Api) JSONObject.parseObject(arg0.result, Login_Api.class);
				if (content.getStatus().equals("102")|| content.getStatus().equals("101")) {
					Toast.makeText(getBaseContext(), "定位数据有误",Toast.LENGTH_SHORT).show();
				} else if (content.getStatus().equals("103")) {
					Toast.makeText(getBaseContext(), "成功上传定位数据",Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getBaseContext(), "未知错误", Toast.LENGTH_SHORT).show();
					
				}
			}
		});
	}
} 
