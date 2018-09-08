package com.example.smartlife;

import cn.sdzz.tools.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class Setting_Setting extends Activity implements OnTouchListener {

	private ImageView back;
	private RelativeLayout socket, quit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.left_setting);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.left_setting_titlebar);

		back = (ImageView) findViewById(R.id.left_setting_btn_back);
		back.setOnTouchListener(this);

		quit = (RelativeLayout) findViewById(R.id.left_setting_quit);
		quit.setOnTouchListener(this);
	}

	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.left_setting_btn_back:
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				this.finish();
			}
			break;
		case R.id.left_setting_quit:
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				SharedPreferences sharedPreferences = getSharedPreferences("LOGIN", 0);
				Editor editor = sharedPreferences.edit();
				editor.putString("name", "");
				editor.putString("pass", "");
				editor.putString("mark", "0");
				editor.commit();
				
				// 先结束前两个activity
				Setting setting = new Setting();
				SmartLife smartlife = new SmartLife();
				Setting.setting.finish();
				SmartLife.smartlife.finish();

				Intent intent_quit = new Intent(Setting_Setting.this,Login.class);
				Setting_Setting.this.startActivity(intent_quit);
				this.finish();
			}
			break;
		default:
			break;
		}

		return false;
	}
}
