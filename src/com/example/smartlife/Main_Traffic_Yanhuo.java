package com.example.smartlife;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;

import cn.sdzz.tools.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class Main_Traffic_Yanhuo extends Activity implements OnTouchListener{
	
	private EditText CP,SJ,BZ;
	private ImageView back;
	private Button PZ,JL,KX;
	private String picname,name,time,qy;
	private String sdPath;//SD����·�� 
	private String picPath,picPathTemp;//ͼƬ�洢·�� 
	private File destDir; 
	private final static int SCANNIN_GREQUEST_CODE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
 		setContentView(R.layout.main_traffic_yanhuo); 		
 		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.main_traffic_titlebar_yanhuo);
 		
 		back = (ImageView) findViewById(R.id.main_traffic_yh_title_left_btn);
 		back.setOnTouchListener(this);
 		CP = (EditText) findViewById(R.id.main_traffic_yh_cpmc);
 		CP.setOnTouchListener(this);
 		SJ = (EditText) findViewById(R.id.main_traffic_yh_sjqk);
 		SJ.setOnTouchListener(this);
 		BZ = (EditText) findViewById(R.id.main_traffic_yh_bz);
 		BZ.setOnTouchListener(this);
 		PZ =(Button) findViewById(R.id.main_traffic_yh_pz);
 		PZ.setOnTouchListener(this);
 		JL =(Button) findViewById(R.id.main_traffic_yh_jl);
 		JL.setOnTouchListener(this);
 		KX =(Button) findViewById(R.id.main_traffic_yh_kx);
 		KX.setOnTouchListener(this);
 		
 		 //��ȡSD����·�� 
	    sdPath = Environment.getExternalStorageDirectory().getPath();  
	    //ʱ��
	    Calendar c = Calendar.getInstance();
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH) + 1;
		int day = c.get(Calendar.DAY_OF_MONTH);
		int hour = c.get(Calendar.HOUR_OF_DAY);
		time  = String.format("%02d", year) + "��" +String.format("%02d", month)+"��"
				+ String.format("%02d", day) +"��";
	    //����
	    Spinener();
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		name = CP.getText().toString();
		switch (arg0.getId()) {
		case R.id.main_traffic_yh_title_left_btn:
			this.finish();
			break;
		case R.id.main_traffic_yh_jl:
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				JL.setBackgroundResource(R.drawable.right_pen_max_clk);
			} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
				JL.setBackgroundResource(R.drawable.right_pen_max);
				String fileName =  "������Ϣ_���.txt";
				String b=SJ.getText().toString();
				if(!name.equals("")){
					if((!b.equals(""))&(BZ.getText().equals(""))){
						writeTxtToFile("������Ϣ��\n" + "ʵ�������"+b, picPathTemp,fileName);
					}else if((!b.equals(""))&(!BZ.getText().equals(""))){
						writeTxtToFile("������Ϣ��\n" + "ʵ�������"+b+"\n��ע��"+BZ.getText().toString(), picPathTemp,fileName);
					}else{
						Toast.makeText(getBaseContext(), "������������Ϣ",Toast.LENGTH_SHORT).show();
					}
				}else{
					Toast.makeText(getBaseContext(), "�������Ʒ����", Toast.LENGTH_SHORT).show();
				}
				
			}
			break;
		case R.id.main_traffic_yh_pz:	
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				PZ.setBackgroundResource(R.drawable.right_pen_max_clk);
			} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
				PZ.setBackgroundResource(R.drawable.right_pen_max);
				if(!name.equals("")){
					TakePic();
				}else{
					Toast.makeText(getBaseContext(), "�������Ʒ����", Toast.LENGTH_SHORT).show();
				}
				
			}
			break;
		case R.id.main_traffic_yh_kx:
			
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				KX.setBackgroundResource(R.drawable.right_pen_max_clk);
			} else if (arg1.getAction() == MotionEvent.ACTION_UP) {
				KX.setBackgroundResource(R.drawable.right_pen_max);
				
				if(!name.equals("")){
					Intent intent = new Intent();
					intent.setClass(Main_Traffic_Yanhuo.this, MipcaActivityCapture.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					startActivityForResult(intent, SCANNIN_GREQUEST_CODE);
				}else{
					Toast.makeText(getBaseContext(), "�������Ʒ����", Toast.LENGTH_SHORT).show();
				}
				
			}		
			break;
		default:
			break;
		}
		
		return false;
	}
	
	public void file() {
		// �ļ������
		picPathTemp = sdPath + "/0_PIC_SDZZ/��������/"+ qy + "/" + name +"/"+time+"/";
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
	
	public void Spinener() {
		 //����id��ȡ����  
	   Spinner spinner; 
       spinner=(Spinner) findViewById(R.id.yanhuo_spinner);  
        
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
       qy = spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
       //ע���¼�  
       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {  
 
           @Override  
           public void onItemSelected(AdapterView<?> parent, View view,  
                   int position, long id) {  
               Spinner spinner=(Spinner) parent;  
              // Toast.makeText(getApplicationContext(), "xxxx"+spinner.getItemAtPosition(position), Toast.LENGTH_LONG).show();  
               qy = spinner.getItemAtPosition(position).toString();
           }  
 
           @Override  
           public void onNothingSelected(AdapterView<?> parent) {  
              // Toast.makeText(getApplicationContext(), "û�иı�Ĵ���", Toast.LENGTH_LONG).show();  
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
	
	
	// ���ַ���д�뵽�ı��ļ���
	public void writeTxtToFile_New(String strcontent, String filePath,String fileName) {
		// �����ļ���֮���������ļ�����Ȼ�����

		String strFilePath = filePath + fileName;
		// ÿ��д��ʱ��������д
		String strContent = strcontent + "/";
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
			Toast.makeText(getBaseContext(), "ɨ���¼�ɹ�",Toast.LENGTH_SHORT).show();
		} catch (Exception e) {
			Log.e("TestFile", "Error on write File:" + e);
		}

	}
	
	
	//ɨ�践�صĽ������
	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
		case SCANNIN_GREQUEST_CODE:
			if(resultCode == RESULT_OK){
				Bundle bundle = data.getExtras();
				//��ʾɨ�赽������
				file();
				String fileName =  "SN����Ϣ_����.txt";	
				//id.setText(bundle.getString("Type")+"-"+bundle.getString("Did"));
				String sn =  bundle.getString("SN");
				//Bitmap bm = bundle.getParcelable("bitmap");
				writeTxtToFile_New(sn+"", picPathTemp,fileName);
				
				
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
				saveBitmapTofile(picPath);
			}
			break;
		}
    }	
	
	/**
	 * ����ͼƬ��ָ���ļ���
	 * 
	 * @param bmp
	 * @param filename
	 * @return
	 */
	private boolean saveBitmapTofile(String filename) {
		Bitmap bmp = getDiskBitmap(sdPath+"/0_PIC_SDZZ/sm.png");
		if (bmp == null || filename == null)
			return false;
		CompressFormat format = Bitmap.CompressFormat.JPEG;
		int quality = 100;
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(filename);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		delFile(sdPath+"/0_PIC_SDZZ/sm.png");
		return bmp.compress(format, quality, stream);
		
	}
	
	//ɾ���ļ�
		public static void delFile(String fileName){
			File file = new File(fileName);
			if(file.isFile()){
				file.delete();
	        }
			file.exists();
		}
	private Bitmap getDiskBitmap(String pathString)
	{
	  Bitmap bitmap = null;
	  try
	  {
	    File file = new File(pathString);
	    if(file.exists())
	    {
	      bitmap = BitmapFactory.decodeFile(pathString);
	    }
	  } catch (Exception e)
	  {
	    // TODO: handle exception
	  }
	  return bitmap;
	}

}
