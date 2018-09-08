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
import android.widget.RelativeLayout;
import cn.sdzz.tools.R;

public class Main_Traffic  extends Activity implements OnClickListener,OnTouchListener { 
	  
	private RelativeLayout DWKC,JCKW,JCJZ,QITA,YanH;
	private ImageView btn;
	  @Override
	  protected void onCreate(Bundle savedInstanceState) { 
	    super.onCreate(savedInstanceState); 
	   // setContentView(R.layout.activity_main); 
	    
	 // 自定义TitleBar
	 		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
	 		setContentView(R.layout.main_traffic_new); 		
	 		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.main_traffic_titlebar);
	  
	 		DWKC = (RelativeLayout) findViewById(R.id.traffic_main_dwkc);
	 		DWKC.setOnTouchListener(this);
	 		JCKW = (RelativeLayout) findViewById(R.id.traffic_main_jckw);
	 		JCKW.setOnTouchListener(this);
	 		JCJZ = (RelativeLayout) findViewById(R.id.traffic_main_jcjz);
	 		JCJZ.setOnTouchListener(this);
	 		QITA = (RelativeLayout) findViewById(R.id.traffic_main_qita);
	 		QITA.setOnTouchListener(this);
	 		YanH = (RelativeLayout) findViewById(R.id.traffic_main_dhys);
	 		YanH.setOnTouchListener(this);
	 		btn =(ImageView) findViewById(R.id.main_traffic_title_left_btn);
	 		btn.setOnClickListener(this);
	  } 
	  
	  @Override
	  public void onClick(View view) { 
	    switch (view.getId()) {   
	    case R.id.main_traffic_title_left_btn://获取原图 
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
		case R.id.traffic_main_dwkc:
			if(arg1.getAction() == MotionEvent.ACTION_DOWN){
				DWKC.setBackgroundResource(R.color.backgroundColor);
				
			}
			else if(arg1.getAction() == MotionEvent.ACTION_UP){
				DWKC.setBackgroundResource(R.color.backgroundColor_low);
				Intent intent = new Intent(Main_Traffic.this, Main_Traffic_KCDW.class);
				Main_Traffic.this.startActivity(intent);
			}
			break;
		case R.id.traffic_main_jckw:
			if(arg1.getAction() == MotionEvent.ACTION_DOWN){
				JCKW.setBackgroundResource(R.color.backgroundColor);
				
			}
			else if(arg1.getAction() == MotionEvent.ACTION_UP){
				JCKW.setBackgroundResource(R.color.backgroundColor_low);
				Intent intent = new Intent(Main_Traffic.this, Main_Traffic_JCKW.class);
				Main_Traffic.this.startActivity(intent);
			}
		
			break;
		case R.id.traffic_main_jcjz:
			if(arg1.getAction() == MotionEvent.ACTION_DOWN){
				JCJZ.setBackgroundResource(R.color.backgroundColor);
				
			}
			else if(arg1.getAction() == MotionEvent.ACTION_UP){
				JCJZ.setBackgroundResource(R.color.backgroundColor_low);
				Intent intent = new Intent(Main_Traffic.this, Main_Traffic_JCJZ.class);
				Main_Traffic.this.startActivity(intent);
			}
			break;
		case R.id.traffic_main_qita:
			if(arg1.getAction() == MotionEvent.ACTION_DOWN){
				QITA.setBackgroundResource(R.color.backgroundColor);
				
			}
			else if(arg1.getAction() == MotionEvent.ACTION_UP){
				QITA.setBackgroundResource(R.color.backgroundColor_low);
				Intent intent = new Intent(Main_Traffic.this, Main_Traffic_Qita.class);
				Main_Traffic.this.startActivity(intent);
			}
			break;
		case R.id.traffic_main_dhys:
			if(arg1.getAction() == MotionEvent.ACTION_DOWN){
				YanH.setBackgroundResource(R.color.backgroundColor);
				
			}
			else if(arg1.getAction() == MotionEvent.ACTION_UP){
				YanH.setBackgroundResource(R.color.backgroundColor_low);
				Intent intent = new Intent(Main_Traffic.this, Main_Traffic_Yanhuo.class);
				Main_Traffic.this.startActivity(intent);
			}
			break;
		default:
			break;
		}
		return false;
	} 
	    
	} 