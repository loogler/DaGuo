package com.daguo.ui.settings;

import java.sql.Date;
import java.text.SimpleDateFormat;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.ui.main.MainActivity;
import com.daguo.utils.HttpUtil;

public class Setting_AboutAty extends Activity implements OnClickListener {
	private RelativeLayout gengxin, tuandui, jianjie;
	private TextView versionTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_setting_about);
		init();
	}

	private void init() {
		gengxin = (RelativeLayout) this.findViewById(R.id.rl_gengxin);
		tuandui = (RelativeLayout) findViewById(R.id.rl_kaifa);
		jianjie = (RelativeLayout) findViewById(R.id.rl_jianjie);
		versionTextView = (TextView) findViewById(R.id.version);
		PackageManager packageManager = this.getPackageManager();
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(
					this.getPackageName(), 0);

			versionTextView.setText("Version: "+packageInfo.versionName);
		} catch (Exception e) {

		}

		gengxin.setOnClickListener(this);
		tuandui.setOnClickListener(this);
		jianjie.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.rl_gengxin:

			break;
		case R.id.rl_kaifa:
			break;
		case R.id.rl_jianjie:
			break;

		default:
			break;
		}
	}

}
