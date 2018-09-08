package com.email;
import cn.sdzz.tools.R;

import com.email.app.MyApplication;
import com.email.utils.EmailFormatUtil;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.Toast;

public class HomeActivity extends Activity implements android.view.View.OnClickListener{
	private ExpandableListView expendView;
	private int []group_click=new int[5];
	private long mExitTime=0;
	private ImageView btn;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_main);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.left_mail_titlebar);
		 final MyExpendAdapter adapter=new MyExpendAdapter();
		
		expendView=(ExpandableListView) findViewById(R.id.list);
		expendView.setGroupIndicator(null);  //设置默认图标不显示
		expendView.setAdapter(adapter);
		btn = (ImageView) findViewById(R.id.left_mail_btn_back);
		btn.setOnClickListener(this);
		//一级点击事件
		expendView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {
			
				group_click[groupPosition]+=1;
				adapter.notifyDataSetChanged();
				return false;
			}
		});
		
		//二级点击事件
		expendView.setOnChildClickListener(new OnChildClickListener() {	
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				//可在这里做点击事件
				if(groupPosition==0&&childPosition==0){
					Intent intent=new Intent(HomeActivity.this, MailEditActivity.class);
					startActivity(intent);
				}else if(groupPosition==0&&childPosition==1){
					Intent intent=new Intent(HomeActivity.this, MailCaogaoxiangActivity.class);
					startActivity(intent);
				}else if(groupPosition==1&&childPosition==0){
					Intent intent=new Intent(HomeActivity.this, MailBoxActivity.class);
					intent.putExtra("TYPE", "INBOX");
					intent.putExtra("status", 0);//全部
					startActivity(intent);
				}else if(groupPosition==1&&childPosition==1){
					Intent intent=new Intent(HomeActivity.this, MailBoxActivity.class);
					intent.putExtra("TYPE", "INBOX");
					intent.putExtra("status", 1);//未读
					startActivity(intent);
				}else if(groupPosition==1&&childPosition==2){
					Intent intent=new Intent(HomeActivity.this, MailBoxActivity.class);
					intent.putExtra("TYPE", "INBOX");
					intent.putExtra("status", 2);//已读
					startActivity(intent);
				}
				adapter.notifyDataSetChanged();
				return false;
			}
		});
		
	}
		
	/**
	 * 适配器
	 * @author Administrator
	 *
	 */
	private class MyExpendAdapter extends BaseExpandableListAdapter{
		
		/**
		 * pic state
		 */
		//int []group_state=new int[]{R.drawable.group_right,R.drawable.group_down};
		
		/**
		 * group title
		 */
		String []group_title=new String[]{"写邮件","收件箱"};
		
		/**
		 * child text
		 */
		String [][] child_text=new String [][]{
				{"新邮件","草稿箱"},
				{"全部邮件","未读邮件","已读邮件"},};
		int [][] child_icons=new int[][]{
				{R.drawable.user_info_icon1,R.drawable.user_info_icon1},
				{R.drawable.user_info_icon1,R.drawable.user_info_icon1,R.drawable.user_info_icon1},
		};
        
		/**
		 * 获取一级标签中二级标签的内容
		 */
		@Override
		public Object getChild(int groupPosition, int childPosition) {
			return child_text[groupPosition][childPosition];
		}
        
		/**
		 * 获取二级标签ID
		 */
		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return childPosition;
		}
		/**
		 * 对一级标签下的二级标签进行设置
		 */
		@SuppressLint("SimpleDateFormat")
		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			convertView=getLayoutInflater().inflate(R.layout.email_child, null);
			TextView tv=(TextView) convertView.findViewById(R.id.tv);
			tv.setText(child_text[groupPosition][childPosition]);
			
			ImageView iv=(ImageView) convertView.findViewById(R.id.child_icon);
			iv.setImageResource(child_icons[groupPosition][childPosition]);
			return convertView;
		}
        
		/**
		 * 一级标签下二级标签的数量
		 */
		@Override
		public int getChildrenCount(int groupPosition) {
			return child_text[groupPosition].length;
		}
        
		/**
		 * 获取一级标签内容
		 */
		@Override
		public Object getGroup(int groupPosition) {
			return group_title[groupPosition];
		}
        
		/**
		 * 一级标签总数
		 */
		@Override
		public int getGroupCount() {
			return group_title.length;
		}
        
		/**
		 * 一级标签ID
		 */
		@Override
		public long getGroupId(int groupPosition) {
			return groupPosition;
		}
		/**
		 * 对一级标签进行设置
		 */
		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			convertView=getLayoutInflater().inflate(R.layout.email_group, null);
			
			ImageView icon=(ImageView) convertView.findViewById(R.id.icon);
			ImageView iv=(ImageView) convertView.findViewById(R.id.iv);
			TextView tv=(TextView) convertView.findViewById(R.id.iv_title);
			
			iv.setImageResource(R.drawable.group_right);
			tv.setText(group_title[groupPosition]);
			
			if(groupPosition==0){
				icon.setImageResource(R.drawable.user_info_icon5);
			}else if(groupPosition==1){
				icon.setImageResource(R.drawable.user_info_icon5);
			}
			
			if(group_click[groupPosition]%2==0){
				iv.setImageResource(R.drawable.group_right);
			}else{
				iv.setImageResource(R.drawable.group_down);
			}
			
			
			return convertView;
		}
		/**
		 * 指定位置相应的组视图
		 */
		@Override
		public boolean hasStableIds() {
			return true;
		}
        
		/**
		 *  当选择子节点的时候，调用该方法
		 */
		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
		
	}
	
	/**
	 * 返回退出系统
	 */
	/*@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode==KeyEvent.KEYCODE_BACK){
			if((System.currentTimeMillis()-mExitTime)<2000){
				android.os.Process.killProcess(android.os.Process.myPid());
			}else{
				Toast.makeText(HomeActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime=System.currentTimeMillis();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}*/

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.left_mail_btn_back:
			finish();
			break;

		default:
			break;
		}
	}
}
