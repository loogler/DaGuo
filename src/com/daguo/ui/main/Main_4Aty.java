package com.daguo.ui.main;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.daguo.R;
import com.daguo.ui.before.MainLoginAty;
import com.daguo.ui.settings.Setting_AboutAty;
import com.daguo.ui.user.UserInfo_ModifyAty;

public class Main_4Aty extends Activity implements OnClickListener {
	private RelativeLayout tuichu, zhuxiao, modify, guanyu;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_main4);
		tuichu = (RelativeLayout) findViewById(R.id.rl_tuichu);
		zhuxiao = (RelativeLayout) findViewById(R.id.rl_zhuxiao);
		modify = (RelativeLayout) findViewById(R.id.rl_modify);
		guanyu = (RelativeLayout) findViewById(R.id.rl_guanyu);

		tuichu.setOnClickListener(this);
		zhuxiao.setOnClickListener(this);
		modify.setOnClickListener(this);
		guanyu.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.rl_tuichu:
			new AlertDialog.Builder(Main_4Aty.this)
					.setMessage("确定要退出吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									android.os.Process
											.killProcess(android.os.Process
													.myPid());
									System.exit(0);
								}
							}).setNegativeButton("取消", null).create().show();

			break;
		case R.id.rl_zhuxiao:
			new AlertDialog.Builder(Main_4Aty.this)
					.setMessage("确定要注销吗？")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface arg0,
										int arg1) {
									SharedPreferences sp = getSharedPreferences(
											"userinfo",
											Context.MODE_WORLD_READABLE);
									Editor edt = sp.edit();
									edt.putString("tel", "");
									edt.putString("school_id", "");
									edt.putString("id", "");
									edt.commit();
									// System.exit(0);
									Intent intent = new Intent(Main_4Aty.this,
											MainLoginAty.class);
									startActivity(intent);
									finish();
								}
							}).setNegativeButton("取消", null).create().show();

			break;
		case R.id.rl_modify:
			Intent intent = new Intent(Main_4Aty.this, UserInfo_ModifyAty.class);
			startActivity(intent);
			break;
		case R.id.rl_guanyu:
			Intent intent1 = new Intent(Main_4Aty.this, Setting_AboutAty.class);
			startActivity(intent1);
			break;

		default:
			break;
		}

	}
}
