package com.example.smartlife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import cn.sdzz.tools.R;

public class SmartLife extends Activity implements OnClickListener, OnTouchListener{

	static Activity smartlife;
	private ImageView img_title_btn_left;
	private ImageView img_main;
	private int mBackPressedTimes = 0;
	private LinearLayout linear_11, linear_12, linear_21, linear_22;
	private PopupWindow popupwindow;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 自定义TitleBar
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_smart_life);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.main_titlebar);
		img_main = (ImageView) findViewById(R.id.img_main);
		
		smartlife = this;
		// titlebar上的两个按钮
		img_title_btn_left = (ImageView) findViewById(R.id.header_left_btn);
		
		// 四个选项块
		linear_11 = (LinearLayout) findViewById(R.id.linear_main_switch_0101);
		linear_12 = (LinearLayout) findViewById(R.id.linear_main_switch_0102);
		linear_21 = (LinearLayout) findViewById(R.id.linear_main_switch_0201);
		linear_22 = (LinearLayout) findViewById(R.id.linear_main_switch_0202);
		linear_11.setOnTouchListener(this);
		linear_12.setOnTouchListener(this);
		linear_21.setOnTouchListener(this);
		linear_22.setOnTouchListener(this);
		// 按钮监听
		img_title_btn_left.setOnClickListener(this);
	}

	// 重写BACK键
	@Override
	public void onBackPressed() {
		if (mBackPressedTimes == 0) {
			Toast.makeText(getBaseContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
			mBackPressedTimes = 1;
			new Thread() {
				@Override
				public void run() {
					try {
						Thread.sleep(2000);
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					} finally {
						mBackPressedTimes = 0;
					}
				};
			}.start();
			return;
		} else {
			this.finish();
		}
		super.onBackPressed();
	}

	// 监听点击事件处理
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		// titlebar
		case R.id.header_left_btn:
			Intent intent_left = new Intent(SmartLife.this, Setting.class);
			SmartLife.this.startActivity(intent_left);
			break;
		
		default:
			break;
		}

	}
		//触摸监听
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.linear_main_switch_0101:
			if(arg1.getAction() == MotionEvent.ACTION_DOWN){
				linear_11.setBackgroundResource(R.drawable.main_background_touch);
			}
			if(arg1.getAction() == MotionEvent.ACTION_UP){
				linear_11.setBackgroundResource(R.drawable.main_background);
				Intent intent11 = new Intent(SmartLife.this, Main_Traffic.class);
				SmartLife.this.startActivity(intent11);
			}
			break;
		case R.id.linear_main_switch_0102:
			if(arg1.getAction() == MotionEvent.ACTION_DOWN){
				linear_12.setBackgroundResource(R.drawable.main_background_touch);
			}
			if(arg1.getAction() == MotionEvent.ACTION_UP){
				linear_12.setBackgroundResource(R.drawable.main_background);
			}
			break;
		case R.id.linear_main_switch_0201:
			if(arg1.getAction() == MotionEvent.ACTION_DOWN){
				linear_21.setBackgroundResource(R.drawable.main_background_touch);
			}
			if(arg1.getAction() == MotionEvent.ACTION_UP){
				linear_21.setBackgroundResource(R.drawable.main_background);
			}
			break;
		case R.id.linear_main_switch_0202:
			if(arg1.getAction() == MotionEvent.ACTION_DOWN){
				linear_22.setBackgroundResource(R.drawable.main_background_touch);
			}
			if(arg1.getAction() == MotionEvent.ACTION_UP){
				linear_22.setBackgroundResource(R.drawable.main_background);
			}
			break;
		default:
			break;
		}
		return false;
	}
	

}
