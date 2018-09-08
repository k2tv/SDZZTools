package com.example.smartlife;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import cn.sdzz.tools.R;

public class Setting_Mod_Pass extends Activity implements OnTouchListener{
	
	private Button ok;
	private ImageView back;
	private EditText edtTxt1,edtTxt2;
	private CheckBox chB1,chB2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.left_mod_pass);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.left_mod_pass_titlebar);
		
		ok = (Button) findViewById(R.id.left_mod_pass_btn);
		ok.setOnTouchListener(this);
		
		back = (ImageView) findViewById(R.id.left_mod_pass_title_btn);
		back.setOnTouchListener(this);
		
		edtTxt1 = (EditText) findViewById(R.id.left_mod_pass_start);
		edtTxt2 = (EditText) findViewById(R.id.left_mod_pass_end);
		chB1 = (CheckBox) findViewById(R.id.left_mod_pass_switch_start);
		chB2 = (CheckBox) findViewById(R.id.left_mod_pass_switch_end);
		chB1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					edtTxt1.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				}
				else{
					edtTxt1.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
		});
		chB2.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				if(arg1){
					edtTxt2.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				}
				else{
					edtTxt2.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}
			}
		});
	}
	
	@Override
	public boolean onTouch(View arg0, MotionEvent arg1) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.left_mod_pass_btn:
			if (arg1.getAction() == MotionEvent.ACTION_DOWN) {
				ok.setBackgroundResource(R.drawable.right_pen_max_clk);
			}
			if(arg1.getAction() == MotionEvent.ACTION_UP){
				ok.setBackgroundResource(R.drawable.right_pen_max);
				if(edtTxt1.getText().toString().equals("")){
					Toast.makeText(getBaseContext(), "请输入原密码", Toast.LENGTH_SHORT).show();
				}
				else if(edtTxt2.getText().toString().equals("")){
					Toast.makeText(getBaseContext(), "请输入新密码", Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(getBaseContext(), "修改成功", Toast.LENGTH_SHORT).show();
				}
			}
			break;
		case R.id.left_mod_pass_title_btn:
			if(arg1.getAction() == MotionEvent.ACTION_DOWN){
				this.finish();
			}
			break;

		default:
			break;
		}
		return false;
	}
	


}
