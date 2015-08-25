package com.daguo.ui.before;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.ui.main.MainActivity;
import com.daguo.util.message.xioo;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.ImgHandlerUtil;
import com.daguo.utils.TelNumberCheckUtil;
import com.daguo.view.dialog.CustomProgressDialog;

/**
 * 登录界面
 * 
 * @author Bugs_Rabbit 時間： 2015-8-13 下午9:16:09
 */
public class MainLoginAty extends Activity {

	private ImageView loginImage;
	private TextView topText;
	private TextPaint tp;
	private Button loginbtn, yanzhengmabtn, registeTextView;

	private EditText username;
	private EditText password;

	private Drawable mIconPerson;
	private Drawable mIconLock;

	Thread tt;
	private boolean threadstop1 = false;
	private int state = 75;

	private String tel;
	private String yanzhengmaString;
	private int haoma;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);// 不随屏幕旋转
		setContentView(R.layout.aty_mainlogin);
		SharedPreferences sp = getSharedPreferences("userinfo",
				Context.MODE_WORLD_READABLE);
		String tel = sp.getString("tel", "");
		if (tel != null && !tel.equals("")) {
			CustomProgressDialog
					.createDialog(MainLoginAty.this, "加载中。。。", 1000).show();
			// 已登录
			Intent intent = new Intent(MainLoginAty.this, MainActivity.class);
			startActivity(intent);
			finish();
		}

		// mIconPerson = getResources().getDrawable(R.drawable.txt_person_icon);
		// mIconPerson.setBounds(5, 1, 60, 50);
		// mIconLock = getResources().getDrawable(R.drawable.txt_lock_icon);
		// mIconLock.setBounds(5, 1, 60, 50);

		username = (EditText) findViewById(R.id.username);
		// username.setCompoundDrawables(mIconPerson, null, null, null);
		password = (EditText) findViewById(R.id.password);
		// password.setCompoundDrawables(mIconLock, null, null, null);
		registeTextView = (Button) findViewById(R.id.registtv);
		registeTextView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// Toast.makeText(getBaseContext(), "注册界面", Toast.LENGTH_SHORT)
				// .show();
				Intent intent = new Intent(MainLoginAty.this,
						MainRegisterAty.class);
				startActivity(intent);
				// 不用注销 也许还回来登录

			}
		});

		init();

		// new Thread(new Runnable() {
		//
		// @SuppressWarnings("static-access")
		// @Override
		// public void run() {
		//
		// while (threadstop1 && state > 0) {
		// Message msg = new Message();
		// msg.what = state;
		// handler.sendMessage(msg);
		// try {
		// tt.sleep(1000);
		// } catch (InterruptedException e) {
		// e.printStackTrace();
		// }
		// state = state - 1;
		// }
		// if (state == 0) {
		// Message msg = new Message();
		// msg.what = 0;
		// handler.sendMessage(msg);
		// }
		// threadstop1 = false;
		// }
		//
		// });

	}

	// /oncreat ^^^^
	/**********************************************************************/
	@SuppressWarnings("deprecation")
	public void init() {

		// topText.setTextColor(Color.MAGENTA);
		// topText.setTextSize(24.0f);
		// topText.setTypeface(Typeface.MONOSPACE, Typeface.BOLD_ITALIC);
		// // 使用TextPaint的仿“粗体”设置setFakeBoldText为true。目前还无法支持仿“斜体”方法
		// tp = topText.getPaint();
		//
		// tp.setFakeBoldText(true);
		// loginImage.setBackgroundDrawable(new BitmapDrawable(ImgHandlerUtil
		// .toRoundBitmap(this, "ic_luncher.png")));
		// loginImage.getBackground().setAlpha(0);
		// loginImage.setImageBitmap(ImgHandlerUtil.toRoundBitmap(this,
		// "ic_luncher.png"));

		// loginbtn.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		//
		// if (event.getAction() == MotionEvent.ACTION_DOWN) {
		// v.getBackground().setAlpha(20);
		// } else if (event.getAction() == MotionEvent.ACTION_UP) {
		// v.getBackground().setAlpha(255);// 设置的透明度
		//
		// }
		// return true;
		// }
		//
		// });
		topText = (TextView) findViewById(R.id.topname);
		loginbtn = (Button) this.findViewById(R.id.loginbtn);
		loginbtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 登录 -成功则保存到本地-结束activity
				yanzhengmaString = password.getText().toString().trim();

				if (yanzhengmaString != null && !yanzhengmaString.equals("")) {

					if (Integer.parseInt(yanzhengmaString) == haoma) {// 验证码与输入的一样
						{

							try {
								CustomProgressDialog dialog = CustomProgressDialog
										.createDialog(MainLoginAty.this,
												"资料提交中");
								dialog.show();
								String url = HttpUtil.LOGIN;
								Map<String, String> map = new HashMap<String, String>();
								map.put("tel", tel);
								String reString = HttpUtil
										.postRequest(url, map);
								JSONObject jsonObject = new JSONObject(reString);
								JSONArray array = jsonObject
										.getJSONArray("rows");
								int l = array.length();
								if (l != 0) {
									// 成功

									String tel = array.optJSONObject(0)
											.getString("tel");
									String school_idString = array
											.optJSONObject(0).getString(
													"school_id");
									String id = array.optJSONObject(0)
											.getString("id");
									String pro_name = array.optJSONObject(0)
											.getString("pro_name");
									String birthday = array.optJSONObject(0)
											.getString("birthday");
									String sex = array.optJSONObject(0)
											.getString("sex");
									String head_info = array.optJSONObject(0)
											.getString("head_info");
									String school_name = array.optJSONObject(0)
											.getString("school_name");
									String start_year = array.optJSONObject(0)
											.getString("start_year");
									String score = array.optJSONObject(0)
											.getString("score");
									String id_card = array.optJSONObject(0)
											.getString("id_card");
									String id_card_copy = array
											.optJSONObject(0).getString(
													"id_card_copy");
									String address = array.optJSONObject(0)
											.getString("address");
									String name = array.optJSONObject(0)
											.getString("name");
									String stu_card_copy = array.optJSONObject(
											0).getString("stu_card_copy");

									SharedPreferences sPreferences = getSharedPreferences(
											"userinfo",
											Context.MODE_WORLD_READABLE);
									Editor editor = sPreferences.edit();
									editor.putString("tel", tel);
									editor.putString("school_id",
											school_idString);
									editor.putString("pro_name", pro_name);
									editor.putString("birthday", birthday);
									editor.putString("sex", sex);
									editor.putString("head_info", head_info);
									editor.putString("school_name", school_name);
									editor.putString("start_year", start_year);
									editor.putString("score", score);
									editor.putString("id_card", id_card);
									editor.putString("id_card_copy",
											id_card_copy);
									editor.putString("address", address);
									editor.putString("name", name);
									editor.putString("stu_card_copy",
											stu_card_copy);
									editor.putString("id", id);

									editor.commit();

									dialog.dismiss();
									Intent intent = new Intent(
											MainLoginAty.this,
											MainActivity.class);
									startActivity(intent);
									MainLoginAty.this.finish();

								} else {
									// 失败
									Toast.makeText(MainLoginAty.this,
											"账号有误，请检查", Toast.LENGTH_LONG)
											.show();
								}
							} catch (Exception e) {
							}

						}

					} else {
						// 验证码错误
						Toast.makeText(MainLoginAty.this, "验证码输入错误，请检查后输入",
								Toast.LENGTH_SHORT).show();

					}
				} else {
					Toast.makeText(MainLoginAty.this, "请输入验证码",
							Toast.LENGTH_SHORT).show();
				}

			}

		});

		yanzhengmabtn = (Button) findViewById(R.id.btn_yanzhengma);
		yanzhengmabtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				yanzhengmabtn.setClickable(false);
				threadstop1 = true;
				tt = new Thread(new Check());
				tt.start();

			}
		});

	}

	class Check implements Runnable {

		@Override
		public void run() {
			tel = username.getText().toString().trim();

			if (tel != null && !tel.equals("")) {// 判断号码输入没有
				if (TelNumberCheckUtil.isMobileNO(tel)) {// 检查号码是不是正确
					String url = HttpUtil.QUERY_USERINFO;
					Map<String, String> map = new HashMap<String, String>();
					map.put("tel", tel);
					String res;
					try {
						res = HttpUtil.postRequest(url, map);
						int total = new JSONObject(res).getInt("total");
						if (total != 0) {

							// 取到值 说明存在账号 获取验证码
							haoma = (int) ((Math.random() * 9 + 1) * 100000);
							String duanxin = "短信验证码为： " + haoma
									+ " ，请勿将验证码提供给他人";
							try {
								xioo.main(duanxin, tel);
							} catch (IOException e1) {
								e1.printStackTrace();
							}

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
							// 账号不存在
							yanzhengmabtn.setClickable(true);
							runOnUiThread(new Runnable() {
								public void run() {
									Toast.makeText(MainLoginAty.this,
											"该账号还未注册，请先注册！", Toast.LENGTH_LONG)
											.show();
								}
							});
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {
					yanzhengmabtn.setClickable(true);
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(MainLoginAty.this, "手机号码有误！",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			} else {
				yanzhengmabtn.setClickable(true);
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(MainLoginAty.this, "请输入手机号码！",
								Toast.LENGTH_SHORT).show();
					}
				});
			}

		}

	}

	// class CheckThread extends Thread {
	// @Override
	// public void run() {
	// super.run();
	// tel = username.getText().toString().trim();
	//
	// if (tel != null && !tel.equals("")) {// 判断号码输入没有
	// if (TelNumberCheckUtil.isMobileNO(tel)) {// 检查号码是不是正确
	// String url = HttpUtil.QUERY_ALLUSERS;
	// Map<String, String> map = new HashMap<String, String>();
	// map.put("tel", tel);
	// String res;
	// try {
	// res = HttpUtil.postRequest(url, map);
	// int total = new JSONObject(res).getInt("total");
	// if (total != 0) {
	// // 取到值 说明存在账号 获取验证码
	// haoma = (int) ((Math.random() * 9 + 1) * 100000);
	// String duanxin = "短信验证码为： " + haoma
	// + " ，请勿将验证码提供给他人";
	// try {
	// xioo.main(duanxin, tel);
	// } catch (IOException e1) {
	// e1.printStackTrace();
	// }
	//
	// } else {
	// // 账号不存在
	// runOnUiThread(new Runnable() {
	// public void run() {
	// Toast.makeText(MainLoginAty.this,
	// "该账号还未注册，请先注册！", Toast.LENGTH_LONG)
	// .show();
	// }
	// });
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// } else {
	// runOnUiThread(new Runnable() {
	// public void run() {
	// Toast.makeText(MainLoginAty.this, "手机号码有误！",
	// Toast.LENGTH_SHORT).show();
	// }
	// });
	// }
	// } else {
	// runOnUiThread(new Runnable() {
	// public void run() {
	// Toast.makeText(MainLoginAty.this, "请输入手机号码！",
	// Toast.LENGTH_SHORT).show();
	// }
	// });
	// }
	// }
	// }

	/**
	 * 登录线程
	 * 
	 * @author Bugs_Rabbit 時間： 2015-8-12 上午10:40:47
	 */

	// class LoginThread implements Runnable {};

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case -1:
				yanzhengmabtn.setText("点此重发");
				yanzhengmabtn.setClickable(true);
				break;
			case 0:
				yanzhengmabtn.setText("点此重发");
				yanzhengmabtn.setClickable(true);
				state = 75;
				break;
			default:
				yanzhengmabtn.setText("请等待" + msg.what + "s");
				yanzhengmabtn.setClickable(false);
				break;
			}
		}
	};

}
