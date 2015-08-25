package com.daguo.ui.before;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.ui.main.MainActivity;
import com.daguo.util.message.xioo;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.TelNumberCheckUtil;
import com.daguo.view.dialog.CustomProgressDialog;

public class MainRegisterAty extends Activity implements
		android.view.View.OnClickListener {
	private Button btn_get, btn_regist;
	private EditText lineedit_tel, lineedit_yanzhengma;
	private AutoCompleteTextView lineedit_school;
	private String school, tel, yanzhengma;
	private String school_id;
	private CustomProgressDialog dialog;

	/**
	 * 倒计时
	 */
	private int state = 75;
	/**
	 * 倒计时线程
	 */
	private Thread tt;
	private boolean threadstop1 = true;
	// private boolean threadstop = true;
	/**
	 * yanzhengma
	 */
	int haoma;

	/**
	 * 学校信息
	 */
	private String[] schoolName;
	Map<String, String> maps = new HashMap<String, String>();
	private List<String> schooList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_mainregister);
		btn_get = (Button) findViewById(R.id.btn_get);
		btn_regist = (Button) findViewById(R.id.btn_regist);
		btn_get.setOnClickListener(this);
		btn_regist.setOnClickListener(this);
		lineedit_school = (AutoCompleteTextView) findViewById(R.id.lineedit_school);
		lineedit_tel = (EditText) findViewById(R.id.lineedit_tel);
		lineedit_yanzhengma = (EditText) findViewById(R.id.lineedit_yanzhengma);
		// 获取学校信息
		new Thread(new Runnable() {
			public void run() {

				try {
					String urlString = HttpUtil.DICT_SCHOOLNAME;
					String reString = HttpUtil.getRequest(urlString);
					JSONObject jsObject = new JSONObject(reString);
					JSONArray array = jsObject.getJSONArray("rows");
					for (int i = 0; i < array.length(); i++) {
						String name = array.optJSONObject(i).getString("name");
						String id = array.optJSONObject(i).getString("id");

						schooList.add(name);
						maps.put(name, id);

					}

				} catch (Exception e) {
				}
				// list转成数组

				schoolName = schooList.toArray(new String[schooList.size()]);
				Log.i("学校名称", schoolName + "");

				runOnUiThread(new Runnable() {
					public void run() {
						lineedit_school.setAdapter(new ArrayAdapter<String>(
								MainRegisterAty.this,
								android.R.layout.simple_dropdown_item_1line,
								schoolName));
					}
				});

			}
		}).start();

	}

	class GetNum implements Runnable {

		@Override
		public void run() {
			tel = lineedit_tel.getText().toString().trim();
			if (tel != null && !tel.equals("")) {
				if (TelNumberCheckUtil.isMobileNO(tel)) {
					// 首先查询该号码是否被注册
					try {
						String url = HttpUtil.QUERY_USERINFO;
						Map<String, String> map = new HashMap<String, String>();
						map.put("tel", tel);
						String res = HttpUtil.postRequest(url, map);
						int total = new JSONObject(res).getInt("total");
						if (total == 0) {
							// 未被注册
							haoma = (int) ((Math.random() * 9 + 1) * 100000);
							String duanxin = "短信验证码为： " + haoma
									+ " ，请勿将验证码提供给他人!";
							try {
								xioo.main(duanxin, tel);
							} catch (IOException e1) {
								e1.printStackTrace();
							}

						} else {
							// 被注册
							runOnUiThread(new Runnable() {
								public void run() {

									new AlertDialog.Builder(
											MainRegisterAty.this)
											.setMessage("号码已被注册，请直接登录！")
											.setPositiveButton("确定",
													new OnClickListener() {

														@Override
														public void onClick(
																DialogInterface arg0,
																int arg1) {
															Intent intent = new Intent(
																	MainRegisterAty.this,
																	MainLoginAty.class);
															startActivity(intent);
															MainRegisterAty.this
																	.finish();
														}
													})
											.setNegativeButton("取消", null)
											.create().show();
								}
							});

						}

					} catch (Exception e) {
					}
					//
					while (threadstop1 && state > 0) {
						Message msg = new Message();
						msg.what = state;
						handler.sendMessage(msg);
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						state = state - 1;
					}
					if (state == 0) {
						Message msg = new Message();
						msg.what = 0;
						handler.sendMessage(msg);
					}
					threadstop1 = false;

				} else {
					// 号码错误
					btn_get.setClickable(true);
					runOnUiThread(new Runnable() {
						public void run() {

							Toast.makeText(MainRegisterAty.this, "请输入正确的手机号码！",
									Toast.LENGTH_SHORT).show();
						}
					});
				}

			} else {
				// 没填写手机号
				btn_get.setClickable(true);
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(MainRegisterAty.this, "手机号码未填写",
								Toast.LENGTH_SHORT).show();

					}
				});
			}

		}
	};
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case -1:
				btn_get.setText("点此重发");
				btn_get.setClickable(true);
				break;
			case 0:
				btn_get.setText("点此重发");
				btn_get.setClickable(true);
				state = 75;
				break;
			default:
				btn_get.setText("请等待" + msg.what + "s");
				btn_get.setClickable(false);
				break;
			}
		}
	};

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.btn_get:
			// 获取验证码 1.服务器判断是否已注册2.生成本地号码发送3.填写
			btn_get.setClickable(false);
			threadstop1=true;
			tt = new Thread(new GetNum());
			tt.start();

			break;
		case R.id.btn_regist:

			// 注册线程 1.验证注册码2.服务器提交
			yanzhengma = lineedit_yanzhengma.getText().toString().trim();
			if (yanzhengma != null && !yanzhengma.equals("")) {

				if (Integer.parseInt(yanzhengma) == haoma) {
					// 验证成功
					school = lineedit_school.getText().toString().trim();
					if (school != null && !school.equals("")) {

						if (maps.get(school) != null
								&& !maps.get(school).equals("")) {
							new RegistThread().start();
						} else {
							Toast.makeText(MainRegisterAty.this, "没有找到该学校！",
									Toast.LENGTH_SHORT).show();
						}

					} else {
						// 有没搞定的
						Toast.makeText(MainRegisterAty.this, "尚未选择学校！",
								Toast.LENGTH_SHORT).show();

					}
				} else {
					// 验证失败
					Toast.makeText(MainRegisterAty.this, "验证码错误！",
							Toast.LENGTH_SHORT).show();

				}
			} else {
				Toast.makeText(MainRegisterAty.this, "验证码为空！",
						Toast.LENGTH_SHORT).show();

			}
			break;
		default:
			break;
		}

	}

	class RegistThread extends Thread {
		@Override
		public void run() {
			super.run();
			runOnUiThread(new Runnable() {
				public void run() {

					dialog = CustomProgressDialog.createDialog(
							MainRegisterAty.this, "正在提交个人资料");
					dialog.show();
				}
			});

			school_id = maps.get(school);

			String url = HttpUtil.SUBMIT_USERINFO;
			Map<String, String> map = new HashMap<String, String>();
			map.put("tel", tel);
			map.put("school_id", school_id);

			try {
				String res = HttpUtil.postRequest(url, map);
				JSONObject jsonObject = new JSONObject(res);

				if (jsonObject.getInt("result") == 1) {

					// 提交成功
					JSONObject js = jsonObject.getJSONObject("obj");
					String id = js.getString("id");

					SharedPreferences sharedPreferences = getSharedPreferences(
							"userinfo", Context.MODE_WORLD_READABLE);
					Editor editor = sharedPreferences.edit();
					editor.putString("tel", tel);
					editor.putString("school_id", school_id);
					editor.putString("id", id);
					editor.commit();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 如果注册成功，则执行如下代码，不成继续注册。

			Intent intent = new Intent(MainRegisterAty.this, MainActivity.class);
			startActivity(intent);
			dialog.dismiss();
			MainRegisterAty.this.finish();

		}
	}

}
