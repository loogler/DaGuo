package com.daguo.ui.before;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.daguo.R;
import com.daguo.ui.main.MainActivity;

/**
 * 
 * @author Bugs_rabbit
 * @function 进入app时加载页。
 */
@SuppressLint("WorldReadableFiles")
public class MainLoadingAty extends Activity {
	private boolean isFirstLoading;

	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_mainloading);

		// 获取本地数据
		SharedPreferences sPreferences = getSharedPreferences("setting",
				Context.MODE_WORLD_READABLE);
		isFirstLoading = sPreferences.getBoolean("isFirstLoading", false);
		Log.d("isfirstloading1", isFirstLoading+"");
		//isFirstLoading == false后期换这个判断首次加载
		if (true) {
			// 保存到本地
			SharedPreferences sp = getSharedPreferences("setting",
					Context.MODE_WORLD_READABLE);
			Editor editor = sp.edit();
			editor.putBoolean("isFirstLoading", true);
			editor.commit();
			Log.d("isfirstloading2", isFirstLoading+"");
			// 加载效果
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					Intent i = new Intent(MainLoadingAty.this,
							MainWelcomeAty.class);
					startActivity(i);
					MainLoadingAty.this.finish();
				}
			}, 2000);

		} else {
			new LoadingThread().start();
			// 加载效果
			new Handler().postDelayed(new Runnable() {

				@Override
				public void run() {
					Intent intent = new Intent(MainLoadingAty.this,
							MainActivity.class);
					startActivity(intent);
					MainLoadingAty.this.finish();
				}
			}, 2000);

		}

	}

	class LoadingThread extends Thread {
		@Override
		public void run() {
			super.run();
			@SuppressWarnings("deprecation")
			SharedPreferences spPreferences = getSharedPreferences("setting",
					Context.MODE_WORLD_READABLE);
			Editor editor = spPreferences.edit();
			//TODO 处理用户信息
			editor.commit();

		}
	}

}
