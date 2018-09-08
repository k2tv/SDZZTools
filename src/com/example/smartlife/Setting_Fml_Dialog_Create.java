package com.example.smartlife;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import cn.sdzz.tools.R;

public class Setting_Fml_Dialog_Create extends Dialog implements OnClickListener{

	private Context context;
	private Button btn_cancle,btn_ok;
	private EditText text;
	private Setting_Fml_Dialog_Create_DialogListener listener;
	
	public Setting_Fml_Dialog_Create(Context context){
		super(context);
		this.context = context;
	}
	
	public interface Setting_Fml_Dialog_Create_DialogListener{
		public void onClick(View view);
	}
	
	public Setting_Fml_Dialog_Create(Context context, int theme, Setting_Fml_Dialog_Create_DialogListener listener) {
		super(context, theme);
		this.context = context;
		this.listener = listener;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.setContentView(R.layout.dialog_fml_create);
		initViews();
	}
	
	private void initViews(){
		btn_cancle = (Button) findViewById(R.id.dialog_fml_create_but_cancle);
		btn_ok = (Button) findViewById(R.id.dialog_fml_create_but_ok);
		text = (EditText) findViewById(R.id.dialog_fml_create_edit);//��ʱû���õ�
		btn_cancle.setOnClickListener(this);
		btn_ok.setOnClickListener(this);
	}
	
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		listener.onClick(view);
		switch (view.getId()) {
		case R.id.dialog_fml_create_but_cancle:
			this.dismiss();
			break;
		case R.id.dialog_fml_create_but_ok:
			this.dismiss();
			break;
		default:
			break;
		}
	}

}
