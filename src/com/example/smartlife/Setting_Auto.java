package com.example.smartlife;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.sdzz.tools.R;

import com.example.smartlife.Setting_Auto_Dialog.Setting_Auto_DialogListener;

public class Setting_Auto extends Activity implements OnTouchListener,
		OnCheckedChangeListener {

	private ImageView back;
	private RelativeLayout re01, re02, re03;
	private CheckBox ch_re01, ch_re02, ch_re03;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.left_auto);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,
				R.layout.left_auto_titlebar);

		// 标题栏
		back = (ImageView) findViewById(R.id.left_auto_btn_back);
		back.setOnTouchListener(this);
		// 场景
		re01 = (RelativeLayout) findViewById(R.id.left_auto_re01);
		re02 = (RelativeLayout) findViewById(R.id.left_auto_re02);
		re03 = (RelativeLayout) findViewById(R.id.left_auto_re03);
		re01.setOnTouchListener(this);
		re02.setOnTouchListener(this);
		re03.setOnTouchListener(this);
		// CheckBox
		ch_re01 = (CheckBox) findViewById(R.id.left_auto_re01_chek);
		ch_re02 = (CheckBox) findViewById(R.id.left_auto_re02_chek);
		ch_re03 = (CheckBox) findViewById(R.id.left_auto_re03_chek);
		ch_re01.setOnCheckedChangeListener(this);
		ch_re02.setOnCheckedChangeListener(this);
		ch_re03.setOnCheckedChangeListener(this);
	}

	// 触摸事件处理
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
		// 场景
		case R.id.left_auto_re01:
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				re01.setBackgroundResource(R.color.backgroundColor);
			}
			if (arg1.getAction() == MotionEvent.ACTION_UP) {
				re01.setBackgroundResource(R.color.backgroundColor_low);
				Intent intent = new Intent(Setting_Auto.this,Setting_Auto_Setting.class);
				Setting_Auto.this.startActivity(intent);
			}
			break;
		case R.id.left_auto_re02:
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				re02.setBackgroundResource(R.color.backgroundColor);
			}
			if (arg1.getAction() == MotionEvent.ACTION_UP) {
				re02.setBackgroundResource(R.color.backgroundColor_low);
				Intent intent = new Intent(Setting_Auto.this,Setting_Auto_Setting.class);
				Setting_Auto.this.startActivity(intent);
			}
			break;
		case R.id.left_auto_re03:
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				re03.setBackgroundResource(R.color.backgroundColor);
			}
			if (arg1.getAction() == MotionEvent.ACTION_UP) {
				re03.setBackgroundResource(R.color.backgroundColor_low);
				Intent intent = new Intent(Setting_Auto.this,Setting_Auto_Setting.class);
				Setting_Auto.this.startActivity(intent);
			}
			break;
		default:
			break;
		}

		return false;
	}

	// 监听选择事件
	@Override
	public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.left_auto_re01_chek:
			if (arg1) {
				ch_re02.setChecked(false);
				ch_re03.setChecked(false);
				Toast.makeText(getBaseContext(), "设置成功", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.left_auto_re02_chek:
			if (arg1) {
				ch_re01.setChecked(false);
				ch_re03.setChecked(false);
				Toast.makeText(getBaseContext(), "设置成功", Toast.LENGTH_SHORT)
						.show();
			}
			break;
		case R.id.left_auto_re03_chek:
			if (arg1) {
				ch_re02.setChecked(false);
				ch_re01.setChecked(false);
				Toast.makeText(getBaseContext(), "设置成功", Toast.LENGTH_SHORT)
						.show();
			}
			break;

		default:
			break;
		}

	}
	
}
