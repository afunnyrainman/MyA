package com.example.user.mya.Utils;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.mya.R;


public class SimpleHUDDialog extends Dialog {

	public SimpleHUDDialog(Context context, int theme) {
		super(context, theme);
	}

	public OnBackListener onBackListener;

	public static SimpleHUDDialog createDialog(Context context) {
		SimpleHUDDialog dialog = new SimpleHUDDialog(context, R.style.SimpleHUD);
		dialog.setContentView(R.layout.simplehud);
		dialog.getWindow().getAttributes().gravity = Gravity.CENTER;
		return dialog;
	}

	public void setMessage(String message) {
		TextView msgView = (TextView) findViewById(R.id.simplehud_message);
		msgView.setText(message);
	}

	public void setImage(Context ctx, int resId) {
		ImageView image = (ImageView) findViewById(R.id.simplehud_image);
		image.setImageResource(resId);

		if (resId == R.mipmap.loading_00) {
			Animation anim = AnimationUtils.loadAnimation(ctx,
					R.anim.progressbar);
			anim.start();
			image.startAnimation(anim);
		}
	}

	@Override
	public void show() {
		super.show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			if (onBackListener != null) {
				onBackListener.onBack();
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	public interface OnBackListener {
		void onBack();
	}

	public SimpleHUDDialog setOnBackListener(OnBackListener onBackListener) {
		this.onBackListener = onBackListener;
		return this;
	}

}
