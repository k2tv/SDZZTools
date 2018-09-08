package com.example.smartlife;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import cn.sdzz.tools.R;

public class Setting_Auto_Setting extends Activity implements OnClickListener{
	
	private ImageView back;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.left_auto_setting);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.left_auto_setting_titlebar);
		
		back = (ImageView) findViewById(R.id.left_auto_setting_back);
		back.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.left_auto_setting_back:
			this.finish();
			break;

		default:
			break;
		}
		
	}

}
