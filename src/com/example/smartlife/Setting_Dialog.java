package com.example.smartlife;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import cn.sdzz.tools.R;

public class Setting_Dialog extends Dialog {

	public Setting_Dialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public Setting_Dialog(Context context, int theme) {
		super(context, theme);
	}

	public static class Builder {
		private Context context;
		private Bitmap image;

		public Builder(Context context) {
			this.context = context;
		}

		public Bitmap getImage() {
			return image;
		}

		public void setImage(Bitmap image) {
			this.image = image;
		}

		public Setting_Dialog create() {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			final Setting_Dialog dialog = new Setting_Dialog(context,
					R.style.Dialog);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			View layout = inflater.inflate(R.layout.dialog_2code, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			dialog.setContentView(layout);
			ImageView img = (ImageView) layout
					.findViewById(R.id.dialog_2code_img);
			img.setImageBitmap(getImage());
			return dialog;
		}
	}

}
