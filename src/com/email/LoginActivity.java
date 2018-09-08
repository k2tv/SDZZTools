package com.email;
import cn.sdzz.tools.R;

import com.email.app.MyApplication;
import com.email.utils.EmailFormatUtil;
import com.email.utils.HttpUtil;
import com.example.smartlife.Setting_Auto;
import com.example.smartlife.Setting_Auto_Setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.Toast;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
public class LoginActivity extends Activity implements TextWatcher, OnClickListener,OnTouchListener{
	private EditText emailAddress;
	private EditText password;
	private Button clearAddress;
	private Button emailLogin;
	private SharedPreferences sp;
	private CheckBox cb_remenber;
	private CheckBox cb_autologin;
	private ImageView back;
	private Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(MyApplication.session==null){
				//dialog.dismiss();
				Toast.makeText(LoginActivity.this, "账号或密码错误", Toast.LENGTH_SHORT).show();
			}else{
				//dialog.dismiss();
				Intent intent=new Intent(LoginActivity.this, HomeActivity.class);
				startActivity(intent);
				finish();
				Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
			}
			super.handleMessage(msg);
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.email_login);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.left_auto_titlebar);
		
		// 标题栏
		back = (ImageView) findViewById(R.id.left_auto_btn_back);
		back.setOnTouchListener(this);
		sp=getSharedPreferences("config", Context.MODE_APPEND);

		initView();
		isRemenberPwd();
		boolean auto=sp.getBoolean("isRbAuto", false);
		if(auto){
			cb_autologin.setChecked(true);
			loginEmail();
		}
	}
	
	/**
	 * 初始化数据
	 */
	private void initView(){
		emailAddress=(EditText) findViewById(R.id.emailAddress);
		password=(EditText) findViewById(R.id.password);
		clearAddress=(Button) findViewById(R.id.clear_address);
		emailLogin=(Button) findViewById(R.id.login_btn);
		cb_remenber=(CheckBox) findViewById(R.id.remenberPassword);
		cb_autologin=(CheckBox) findViewById(R.id.autoLogin);
		
		clearAddress.setOnClickListener(this);
		emailAddress.addTextChangedListener(this);
		emailLogin.setOnTouchListener(this);
		
		cb_remenber.setOnClickListener(this);
		cb_autologin.setOnClickListener(this);
		
	}
    
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clear_address:
			emailAddress.setText("");
			break;
		//case R.id.login_btn:
			//loginEmail();
			//break;
		case R.id.remenberPassword:
			remenberPwd();
			break;
		case R.id.autoLogin:
			remenberAuTo();
			break;
		}
	}
	
	/**
	 * 是否记住密码
	 */
	private void isRemenberPwd(){
		boolean isRbPwd=sp.getBoolean("isRbPwd", false);
		if(isRbPwd){
			String addr=sp.getString("emailaddress", "");
			String pwd=sp.getString("emailpassword", "");
			emailAddress.setText(addr);
			password.setText(pwd);
			cb_remenber.setChecked(true);
		}
	}
	
	/**
	 * 记住密码
	 */
	private void remenberPwd(){
		boolean isRbPwd=sp.getBoolean("isRbPwd", false);
		if(isRbPwd){
			sp.edit().putBoolean("isRbPwd", false).commit();
			cb_remenber.setChecked(false);
		}else{
			sp.edit().putBoolean("isRbPwd", true).commit();
			sp.edit().putString("emailaddress", emailAddress.getText().toString().trim()).commit();
			sp.edit().putString("emailpassword", password.getText().toString().trim()).commit();
			cb_remenber.setChecked(true);
			
		}
	}
	
	/**
	 * 记住密码
	 */
	private void remenberAuTo(){
		boolean isRbAuto=sp.getBoolean("isRbAuto", false);
		if(isRbAuto){
			sp.edit().putBoolean("isRbAuto", false).commit();
			cb_autologin.setChecked(false);
		}else{
			sp.edit().putBoolean("isRbAuto", true).commit();
			//sp.edit().putString("emailaddress", emailAddress.getText().toString().trim()).commit();
			//sp.edit().putString("emailpassword", password.getText().toString().trim()).commit();
			cb_autologin.setChecked(true);
			
		}
	}
	
	/**
	 * 登入邮箱
	 */
	private void loginEmail(){
		String address=emailAddress.getText().toString().trim();
		String pwd=password.getText().toString().trim();
		if(TextUtils.isEmpty(address)){
			Toast.makeText(LoginActivity.this, "地址不能为空", Toast.LENGTH_SHORT).show();
			return;
		}else{
			if(TextUtils.isEmpty(pwd)){
				Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
				return;
			}
		}
		/**
		 * 校验邮箱格式
		 */
		if(!EmailFormatUtil.emailFormat(address)){
			Toast.makeText(LoginActivity.this, "邮箱格式不正确", Toast.LENGTH_SHORT).show();
		}else{
			String host="mail."+address.substring(address.lastIndexOf("@")+1);
			MyApplication.info.setMailServerHost(host);
			MyApplication.info.setMailServerPort("25");
			MyApplication.info.setUserName(address);
			MyApplication.info.setPassword(pwd);
			MyApplication.info.setValidate(true);
			
			/**
			 * 进度条
			 */
			/*dialog=new ProgressDialog(LoginActivity.this);
			dialog.setMessage("正在登入，请稍后");
			dialog.show();*/
			
			/**
			 * 访问网络
			 */
			new Thread(){
				@Override
				public void run() {		
					//登入操作
					HttpUtil util=new HttpUtil();
					MyApplication.session=util.login();
					Message message=handler.obtainMessage();
					message.sendToTarget();
				}
				
			}.start();
		}
	}
	
	 
		/**
		 * 文本监听事件
		 * 
		 */
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
				if(!TextUtils.isEmpty(s)){
					clearAddress.setVisibility(View.VISIBLE);
				}else{
					clearAddress.setVisibility(View.INVISIBLE);
				}
			
		}
	
	@Override
	public void afterTextChanged(Editable s) {

	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub	
		switch (arg0.getId()) {
		// 标题栏
		
		case R.id.left_auto_btn_back:
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				this.finish();
			}
			break;
			
		case R.id.login_btn:
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				emailLogin.setBackgroundResource(R.color.backgroundColor);
			}else if(arg1.getAction() == MotionEvent.ACTION_UP){
				emailLogin.setBackgroundResource(R.color.backgroundColor_low);
				loginEmail();
			}
			break;
		default:
			break;
		}

		return false;
		
	}
    

}
