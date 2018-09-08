package com.example.smartlife;

import java.security.MessageDigest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.sdzz.tools.R;

import com.alibaba.fastjson.JSONObject;
import com.example.smartlife_api.Login_Api;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;



public class Reg extends Activity implements OnTouchListener {

	private Button btn_ok, btn_c;
	private EditText name, pass01, pass02;

    private String uid;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.reg);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.login_titlebar);

		btn_ok = (Button) findViewById(R.id.reg_btn_ok);
		btn_c = (Button) findViewById(R.id.reg_btn_c);
		name = (EditText) findViewById(R.id.reg_edit_01);
		pass01 = (EditText) findViewById(R.id.reg_edit_02);
		pass02 = (EditText) findViewById(R.id.reg_edit_03);
		btn_c.setOnTouchListener(this);
		btn_ok.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.reg_btn_c:
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				btn_c.setBackgroundResource(R.drawable.right_pen_max_clk);
			}
			if (arg1.getAction() == MotionEvent.ACTION_UP) {
				btn_c.setBackgroundResource(R.drawable.right_pen_max);
				// ��������
				Intent intent = new Intent();
				intent.putExtra("name", name.getText().toString());
				intent.putExtra("pass", pass01.getText().toString());
				this.setResult(RESULT_OK, intent);
				this.finish();
			}
			break;
		case R.id.reg_btn_ok:
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				btn_ok.setBackgroundResource(R.drawable.right_pen_max_clk);
			}
			if (arg1.getAction() == MotionEvent.ACTION_UP) {
				if (name.getText().toString().equals("")) {
					Toast.makeText(getBaseContext(), "�˺Ų���Ϊ��",Toast.LENGTH_SHORT).show();
				}else if (TextUtils.isDigitsOnly(name.getText().toString())) {
					Toast.makeText(getBaseContext(), "�˺Ų���Ϊ������",Toast.LENGTH_SHORT).show();
				}else if (pass01.getText().toString().equals("")|| pass02.getText().toString().equals("")) {
					Toast.makeText(getBaseContext(), "���벻��Ϊ��",Toast.LENGTH_SHORT).show();
				} else if (pass01.getText().length() < 6) {
					Toast.makeText(getBaseContext(), "����������λ��",Toast.LENGTH_SHORT).show();
				} else if (!pass01.getText().toString().equals(pass02.getText().toString())) {
					Toast.makeText(getBaseContext(), "�������벻һ��",Toast.LENGTH_SHORT).show();
				} else {
					btn_ok.setText("ע����...");
					btn_ok.setEnabled(false);
					regst();
				}
				btn_ok.setBackgroundResource(R.drawable.right_pen_max);
			}
			break;
		default:
			break;
		}
		return false;
	}

	// ��д���ؼ� ��������
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub

		Intent intent = new Intent();
		intent.putExtra("name", "");
		intent.putExtra("pass", "");
		this.setResult(RESULT_OK, intent);
		this.finish();

		super.onBackPressed();
	}
//�û�ע��
	public void regst() {
		String path = App.address + "reg.php?name=" + name.getText().toString()+ "&pass=" + MD5(pass01.getText().toString());
		HttpUtils http = new HttpUtils(10000);//10s��ʱ
		http.configCurrentHttpCacheExpiry(5000); // ���û���5��,5����ֱ�ӷ����ϴγɹ�����Ľ����

		http.send(HttpMethod.GET, path, new RequestCallBack<String>() {

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				// TODO Auto-generated method stub
				Toast.makeText(getBaseContext(), "��������ʧ��", Toast.LENGTH_SHORT).show();
				btn_ok.setText("ע��");
				btn_ok.setEnabled(true);
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0) {
				// TODO Auto-generated method stub
				// �������� ת��ΪJSON��ʽ
				Login_Api content = (Login_Api) JSONObject.parseObject(arg0.result, Login_Api.class);
				if (content.getStatus().equals("202")) {
					Toast.makeText(getBaseContext(), "�˺��ѱ�ע��",Toast.LENGTH_SHORT).show();
					btn_ok.setText("ע��");
					btn_ok.setEnabled(true);
				} else if (content.getStatus().equals("203")) {
					Toast.makeText(getBaseContext(), "ע��ɹ�", Toast.LENGTH_SHORT).show();
					SharedPreferences sharedPreferences = getSharedPreferences("LOGIN", 0);
					Editor editor = sharedPreferences.edit();
					editor.putString("Name", name.getText().toString());
					editor.commit();
					// ��������
					Intent intent = new Intent();
					intent.putExtra("name", name.getText().toString());
					intent.putExtra("pass", pass01.getText().toString());
					Reg.this.setResult(RESULT_OK, intent);
					Reg.this.finish();
					
				}
			}
		});
	}
	// MD5����
	public static String MD5(String str) {
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		char[] charArray = str.toCharArray();
		byte[] byteArray = new byte[charArray.length];
		for (int i = 0; i < charArray.length; i++) {
			byteArray[i] = (byte) charArray[i];
		}
		byte[] md5Bytes = md5.digest(byteArray);
		StringBuffer hexValue = new StringBuffer();
		for (int i = 0; i < md5Bytes.length; i++) {
			int val = (md5Bytes[i]) & 0xff;
			if (val < 16) {
				hexValue.append("0");
			}
			hexValue.append(Integer.toHexString(val));
		}
		return hexValue.toString();
	}

}
