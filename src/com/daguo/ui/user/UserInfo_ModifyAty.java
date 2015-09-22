package com.daguo.ui.user;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.util.base.PullScrollView;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.UploadUtil;
import com.daguo.view.dialog.CustomProgressDialog;
import com.daguo.view.dialog.MySelf_Mod_Photo;
import com.nostra13.universalimageloader.core.ImageLoader;

public class UserInfo_ModifyAty extends Activity implements
		PullScrollView.OnTurnListener, android.view.View.OnClickListener {
	private String tag = "UserInfo_ModifyAty";
	Message mMessage;
	boolean isInfoChange = false;
	private CustomProgressDialog dialog;
	private com.daguo.utils.AsyncImageLoader2 asyncImageLoader2 = new com.daguo.utils.AsyncImageLoader2();
	/*
	 * 区别照片的验证是哪个的验证
	 */
	private boolean isSchoolCopy = false, isIdCopy = false, isHeadCopy;

	// 弹出窗
	MySelf_Mod_Photo menuWindow;
	// 拍照参数
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果

	// 创建一个以当前时间为名称的文件，获取相册资源
	private File tempFile;
	private File uploadFile;

	// private String
	/**
	 * 界面功能的实例化
	 */
	private PullScrollView mScrollView;
	private ImageView mHeadImg;

	/**
	 * 用户数据的实例化
	 */
	private TextView topNickTextView, topDepartmentTextView, topSchoolTextView;// 顶部
	private ImageView userAvatorImageView;
	private TextView idCheckTextView, stuCheckTextView;
	/**
	 * 资料修改
	 */
	private RelativeLayout rl_idCheck, rl_stuCheck, rl_nick, rl_sex, rl_school,
			rl_department, rl_schoolyear, rl_birthyear, rl_idcard;
	private Button submitButton;

	/**
	 * 显示文字的textview实例化
	 */
	private TextView tv_nick, tv_sex, tv_school, tv_department, tv_schoolyear,
			tv_birthyear, tv_idcard;

	/**
	 * 变量
	 */
	String p_id;// 用户id
	private static String name, sex, school, department, idcard, address,
			schoolyear, idcardCopy, schoolcardCopy, birthday, imgsrc;
	private String ssss;
	Dialog dia;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_userinfo_modify);
		SharedPreferences sp = getSharedPreferences("userinfo",
				Context.MODE_WORLD_READABLE);
		p_id = sp.getString("id", "");
		schoolcardCopy = sp.getString("stu_card_copy", "");
		initView();

		showTable();

	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				if (infoCheck(name)) {
					tv_nick.setText(name);
					topNickTextView.setText(name);

				} else {
					tv_nick.setText("");
					topNickTextView.setText("");
				}
				if (infoCheck(sex)) {
					if (Integer.parseInt(sex) == 0) {
						tv_sex.setText("男");
					} else if (Integer.parseInt(sex) == 1) {
						tv_sex.setText("女");
					}

				} else {
					tv_sex.setText("");
				}
				if (infoCheck(birthday)) {

					tv_birthyear.setText(birthday);
				} else {
					tv_birthyear.setText("");
				}

				if (infoCheck(department)) {
					tv_department.setText(department);
					topDepartmentTextView.setText(department);

				} else {
					topDepartmentTextView.setText("");
					tv_department.setText("");
				}
				if (infoCheck(idcard)) {

					tv_idcard.setText(idcard);
				} else {
					tv_idcard.setText("");
				}
				if (infoCheck(school)) {
					topSchoolTextView.setText(school);
					tv_school.setText(school);

				} else {
					tv_school.setText("");
					topSchoolTextView.setText("");
				}
				if (infoCheck(schoolyear)) {

					tv_schoolyear.setText(schoolyear);
				} else {

					tv_schoolyear.setText("");
				}
				if (infoCheck(imgsrc)) {
					// loadImage(imgsrc, R.id.user_avatar);
					ImageLoader.getInstance().displayImage(
							HttpUtil.IMG_URL + imgsrc, userAvatorImageView);
				}

				break;
			case 3:

				if (UploadUtil.rescode == 200) {
					try {
						JSONObject jsonObject1 = new JSONObject(
								msg.obj.toString());
						String imgSRC = jsonObject1
								.getString("fileRelativePath");
						if (isIdCopy && infoCheck(imgSRC)) {
							idcardCopy = imgSRC;

						} else if (isSchoolCopy && infoCheck(imgSRC)) {
							schoolcardCopy = imgSRC;
						} else if (isHeadCopy && infoCheck(imgSRC)) {
							imgsrc = imgSRC;
						} else {
							// 都没动
						}

					} catch (Exception e) {
					}

					Toast.makeText(UserInfo_ModifyAty.this, "上传成功，请提交资料",
							Toast.LENGTH_SHORT).show();

				} else {

					Toast.makeText(UserInfo_ModifyAty.this, "上传失败，请重新上传",
							Toast.LENGTH_SHORT).show();

				}
				break;

			default:
				break;
			}

		};
	};
	/**
	 * 得到用户信息
	 */

	Thread getUserInfo = new Thread(new Runnable() {
		public void run() {
			try {
				String url = HttpUtil.QUERY_USERINFO;
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", p_id);
				String res = HttpUtil.postRequest(url, map);
				JSONObject js = new JSONObject(res);
				JSONArray array = js.getJSONArray("rows");
				if (array.length() > 0) {
					// 有值
					String namesss = array.optJSONObject(0).getString("name");
					String sexsss = array.optJSONObject(0).getString("sex");
					String schoolsss = array.optJSONObject(0).getString(
							"school_name");
					String departmentsss = array.optJSONObject(0).getString(
							"pro_name");
					String idcardsss = array.optJSONObject(0).getString(
							"id_card");
					String addresssss = array.optJSONObject(0).getString(
							"address");
					String schoolyearsss = array.optJSONObject(0).getString(
							"start_year");
					String idcardCopysss = array.optJSONObject(0).getString(
							"id_card_copy");
					String schoolcardCopysss = array.optJSONObject(0)
							.getString("stu_card_copy");
					String birthdaysss = array.optJSONObject(0).getString(
							"birthday");
					String img = array.optJSONObject(0).getString("head_info");

					if (infoCheck(namesss)) {
						name = namesss;
					} else {
						name = "";
					}
					if (infoCheck(sexsss)) {
						sex = sexsss;
					} else {
						sex = "";
					}
					if (infoCheck(schoolsss)) {
						school = schoolsss;
					} else {
						school = "";
					}
					if (infoCheck(departmentsss)) {
						department = departmentsss;
					} else {
						department = "";
					}
					if (infoCheck(idcardsss)) {
						idcard = idcardsss;
					} else {
						idcard = "";
					}
					if (infoCheck(addresssss)) {
						address = addresssss;
					} else {
						address = "";
					}
					if (infoCheck(schoolyearsss)) {
						schoolyear = schoolyearsss;

					} else {
						schoolyear = "";
					}
					if (infoCheck(idcardCopysss)) {
						idcardCopy = idcardCopysss;

					} else {
						idcardCopy = "";
					}
					if (infoCheck(schoolcardCopysss)) {
						schoolcardCopy = schoolcardCopysss;

					} else {
						schoolcardCopy = "";
					}
					if (infoCheck(birthdaysss)) {
						birthday = birthdaysss;
					} else {
						birthday = "";
					}
					if (infoCheck(img)) {
						imgsrc = img;
					} else {
						imgsrc = "";
					}

					mMessage = handler.obtainMessage(0);
					mMessage.sendToTarget();

				} else {
					runOnUiThread(new Runnable() {
						public void run() {

							Toast.makeText(UserInfo_ModifyAty.this, "服务器异常！",
									Toast.LENGTH_SHORT).show();
						}
					});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	});
	Thread submit = new Thread(new Runnable() {
		public void run() {
			try {

				String url = HttpUtil.SUBMIT_USERINFO;
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", p_id);
				map.put("name", name);
				map.put("head_info", imgsrc);
				map.put("sex", sex);
				map.put("school", school);
				map.put("pro_name", department);
				map.put("id_card", idcard);
				map.put("address", address);
				map.put("start_year", schoolyear);
				map.put("birthday", birthday);
				map.put("stu_card_copy", schoolcardCopy);
				map.put("id_card_copy", idcardCopy);
				String res = HttpUtil.postRequest(url, map);
				if (res != null) {
					// success

					SharedPreferences sp = getSharedPreferences("userinfo",
							Context.MODE_WORLD_READABLE);
					Editor ed = sp.edit();

					ed.putString("name", name);
					ed.putString("head_info", imgsrc);
					ed.putString("sex", sex);
					ed.putString("school_name", school);
					ed.putString("pro_name", department);
					ed.putString("id_card", idcard);
					ed.putString("address", address);
					ed.putString("start_year", schoolyear);
					ed.putString("birthday", birthday);
					ed.putString("stu_card_copy", schoolcardCopy);
					ed.putString("id_card_copy", idcardCopy);

					ed.commit();
					runOnUiThread(new Runnable() {
						public void run() {
							dialog.dismiss();
							Toast.makeText(UserInfo_ModifyAty.this, "修改成功",
									Toast.LENGTH_SHORT).show();
							finish();
						}
					});
				} else {
					// fail
					dialog.dismiss();
					runOnUiThread(new Runnable() {
						public void run() {

							Toast.makeText(UserInfo_ModifyAty.this, "修改失败了！",
									Toast.LENGTH_SHORT).show();
							finish();
						}
					});

				}

			} catch (Exception e) {
			}

		}
	});

	protected void initView() {
		mScrollView = (PullScrollView) findViewById(R.id.scroll_view);
		mHeadImg = (ImageView) findViewById(R.id.background_img);

		topNickTextView = (TextView) findViewById(R.id.user_nick_top);
		topDepartmentTextView = (TextView) findViewById(R.id.topdepartment);
		topSchoolTextView = (TextView) findViewById(R.id.topschool);
		userAvatorImageView = (ImageView) findViewById(R.id.user_avatar);

		idCheckTextView = (TextView) findViewById(R.id.idcheck);
		stuCheckTextView = (TextView) findViewById(R.id.stucheck);
		tv_nick = (TextView) findViewById(R.id.changeinfo_txt_nickname);
		tv_sex = (TextView) findViewById(R.id.changeinfo_txt_sex);
		tv_school = (TextView) findViewById(R.id.changeinfo_txt_school);
		tv_department = (TextView) findViewById(R.id.changeinfo_txt_department);
		tv_schoolyear = (TextView) findViewById(R.id.changeinfo_txt_schoolyear);

		tv_birthyear = (TextView) findViewById(R.id.changeinfo_txt_birthday);
		tv_idcard = (TextView) findViewById(R.id.changeinfo_txt_idcard);

		rl_idCheck = (RelativeLayout) findViewById(R.id.changeinfo_relayout_idcheck);
		rl_stuCheck = (RelativeLayout) findViewById(R.id.changeinfo_relayout_stucheck);
		rl_nick = (RelativeLayout) findViewById(R.id.changeinfo_relayout_nickname);
		rl_sex = (RelativeLayout) findViewById(R.id.changeinfo_relayout_sex);
		rl_school = (RelativeLayout) findViewById(R.id.changeinfo_relayout_school);
		rl_department = (RelativeLayout) findViewById(R.id.changeinfo_relayout_department);
		rl_schoolyear = (RelativeLayout) findViewById(R.id.changeinfo_relayout_schoolyear);

		rl_birthyear = (RelativeLayout) findViewById(R.id.changeinfo_relayout_idcard);
		rl_idcard = (RelativeLayout) findViewById(R.id.changeinfo_relayout_birthday);

		submitButton = (Button) findViewById(R.id.submit_btn);

		if (infoCheck(idcardCopy)) {
			idCheckTextView.setText("身份已验证");
		}
		if (infoCheck(schoolcardCopy)) {
			stuCheckTextView.setText("学生证已验证");
		}

		userAvatorImageView.setOnClickListener(this);
		rl_department.setOnClickListener(this);
		rl_idCheck.setOnClickListener(this);
		rl_stuCheck.setOnClickListener(this);
		rl_nick.setOnClickListener(this);
		rl_sex.setOnClickListener(this);
		rl_school.setOnClickListener(this);
		rl_schoolyear.setOnClickListener(this);

		rl_birthyear.setOnClickListener(this);
		rl_idcard.setOnClickListener(this);
		submitButton.setOnClickListener(this);

		mScrollView.setHeader(mHeadImg);
		mScrollView.setOnTurnListener(this);

		getUserInfo.start();
	}

	public void showTable() {
		TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
				TableRow.LayoutParams.MATCH_PARENT,
				TableRow.LayoutParams.WRAP_CONTENT);
		layoutParams.gravity = Gravity.CENTER;
		layoutParams.leftMargin = 30;
		layoutParams.bottomMargin = 10;
		layoutParams.topMargin = 10;

	}

	@Override
	public void onTurn() {
		// 实现空方法在华东界面时
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {

		case R.id.user_avatar:
			// 上传头像
			isHeadCopy = true;
			isIdCopy = false;
			isSchoolCopy = false;
			menuWindow = new MySelf_Mod_Photo(UserInfo_ModifyAty.this, this);// shilihua
			menuWindow.showAtLocation(
					UserInfo_ModifyAty.this.findViewById(R.id.rl),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			Log.i(tag, "menuWindow执行完毕");

			break;

		case R.id.changeinfo_relayout_idcheck:
			// 身份证检查
			if (infoCheck(idcardCopy)) {
				// 已存在

				Toast.makeText(UserInfo_ModifyAty.this, "您已验证身份，修改需联系管理员",
						Toast.LENGTH_SHORT).show();
			} else {
				//
				isIdCopy = true;
				isSchoolCopy = false;
				isHeadCopy = false;
				menuWindow = new MySelf_Mod_Photo(UserInfo_ModifyAty.this, this);// shilihua
				menuWindow.showAtLocation(
						UserInfo_ModifyAty.this.findViewById(R.id.rl),
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				Log.i(tag, "menuWindow执行完毕");
			}

			break;
		case R.id.changeinfo_relayout_stucheck:
			// 学生证检查
			if (infoCheck(schoolcardCopy)) {
				// 已存在

				Toast.makeText(UserInfo_ModifyAty.this, "您已验证学生证，修改需联系管理员",
						Toast.LENGTH_SHORT).show();
			} else {
				isSchoolCopy = true;
				isIdCopy = false;
				isHeadCopy = false;
				menuWindow = new MySelf_Mod_Photo(UserInfo_ModifyAty.this, this);// shilihua
				menuWindow.showAtLocation(
						UserInfo_ModifyAty.this.findViewById(R.id.rl),
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
				Log.i(tag, "menuWindow执行完毕");
			}

			break;
		case R.id.changeinfo_relayout_nickname:
		// 昵称
		{
			LayoutInflater inflater = LayoutInflater
					.from(UserInfo_ModifyAty.this);
			final View textEntryView = inflater.inflate(
					R.layout.dialog_infochange, null);
			final EditText edit = (EditText) textEntryView
					.findViewById(R.id.dia2);

			new AlertDialog.Builder(UserInfo_ModifyAty.this)
					.setTitle("姓名")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(textEntryView)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String str = edit.getText().toString();
									name = str;
									tv_nick.setText("" + str);
									isInfoChange = true;

								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();
		}
			// {
			// dia = new Dialog(UserInfo_ModifyAty.this);
			// dia.requestWindowFeature(Window.FEATURE_NO_TITLE);
			// dia.show();
			// Window window = dia.getWindow();
			// window.setContentView(R.layout.dialog_infochange);
			// TextView tv = (TextView) window.findViewById(R.id.dia1);
			// final EditText edt = (EditText) window.findViewById(R.id.dia2);
			// Button bt = (Button) window.findViewById(R.id.dia3);
			// tv.setText("请输入昵称： ");
			//
			// bt.setOnClickListener(new View.OnClickListener() {
			// @Override
			// public void onClick(View arg0) {
			// name = edt.getText().toString();
			// mMessage=handler.obtainMessage(0);
			// mMessage.sendToTarget();
			// dia.dismiss();
			// }
			// });
			// }
			break;
		case R.id.changeinfo_relayout_sex:
			// 性别
			final String[] asd = new String[] { "男", "女" };
			{
				new AlertDialog.Builder(UserInfo_ModifyAty.this)
						.setItems(asd, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								ssss = asd[arg1];

								tv_sex.setText(ssss);
								if (ssss.equals("男")) {
									sex = "0";
								} else if (ssss.equals("女")) {
									sex = "1";
								}
								isInfoChange = true;

							}
						}).create().show();
			}
			break;
		case R.id.changeinfo_relayout_school:
		// 学校
		{
			Toast.makeText(UserInfo_ModifyAty.this, "学校信息修改需要联系管理员",
					Toast.LENGTH_SHORT).show();
		}
			break;
		case R.id.changeinfo_relayout_department:
		// 院系
		{
			LayoutInflater inflater = LayoutInflater
					.from(UserInfo_ModifyAty.this);
			final View textEntryView = inflater.inflate(
					R.layout.dialog_infochange, null);
			final EditText edit = (EditText) textEntryView
					.findViewById(R.id.dia2);

			new AlertDialog.Builder(UserInfo_ModifyAty.this)
					.setTitle("院系")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(textEntryView)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String str = edit.getText().toString();
									department = str;
									tv_department.setText("" + str);
									isInfoChange = true;

								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();
		}
			break;
		case R.id.changeinfo_relayout_schoolyear:
		// 学年级
		{
			LayoutInflater inflater = LayoutInflater
					.from(UserInfo_ModifyAty.this);
			final View textEntryView = inflater.inflate(
					R.layout.dialog_infochange, null);
			final EditText edit = (EditText) textEntryView
					.findViewById(R.id.dia2);

			new AlertDialog.Builder(UserInfo_ModifyAty.this)
					.setTitle("入学年份")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(textEntryView)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String str = edit.getText().toString();
									schoolyear = str;
									tv_schoolyear.setText("" + str);
									isInfoChange = true;

								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();
		}
			break;

		case R.id.changeinfo_relayout_idcard:
		// 身份证
		{
			LayoutInflater inflater = LayoutInflater
					.from(UserInfo_ModifyAty.this);
			final View textEntryView = inflater.inflate(
					R.layout.dialog_infochange, null);
			final EditText edit = (EditText) textEntryView
					.findViewById(R.id.dia2);

			new AlertDialog.Builder(UserInfo_ModifyAty.this)
					.setTitle("身份证号")
					.setIcon(android.R.drawable.ic_dialog_info)
					.setView(textEntryView)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									String str = edit.getText().toString();
									idcard = str;
									tv_idcard.setText("" + str);
									isInfoChange = true;

								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).show();
		}
			break;
		case R.id.changeinfo_relayout_birthday:
		// 生日
		{
			new DatePickerDialog(UserInfo_ModifyAty.this,
					new DatePickerDialog.OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker arg0, int year,
								int mon, int day) {
							tv_birthyear.setText(year + "-" + (mon + 1) + "-"
									+ day);

							birthday = year + "-" + (mon + 1) + "-" + day;
							isInfoChange = true;

						}
					}, 2000, 01, 01).show();
		}

			break;
		case R.id.submit_btn:
			// 提交修改
			if (isInfoChange) {
				// 提交修改

				dialog = CustomProgressDialog.createDialog(
						UserInfo_ModifyAty.this, "资料提交中。。");
				dialog.show();
				submit.start();

			} else {
				// 不修改
				Toast.makeText(UserInfo_ModifyAty.this, "资料无需修改",
						Toast.LENGTH_SHORT).show();
			}

			break;

		case R.id.btn_take_photo:
			menuWindow.dismiss();
			// 调用系统的拍照功能
			Log.i(tag, "拍照========");
			Intent takeintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// 指定调用相机拍照后照片的储存路径
			tempFile = new File(Environment.getExternalStorageDirectory(),
					getPhotoFileName(2));
			takeintent
					.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
			startActivityForResult(takeintent, PHOTO_REQUEST_TAKEPHOTO);
			break;
		case R.id.btn_pick_photo:
			menuWindow.dismiss();
			Log.i(tag, "选择照片========");
			Intent pickintent = new Intent(Intent.ACTION_PICK, null);
			pickintent.setDataAndType(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(pickintent, PHOTO_REQUEST_GALLERY);
			break;

		default:
			break;
		}
	}

	/**
	 * 获取照片，名称 使用系统当前日期加以调整作为照片的名称
	 */
	@SuppressLint("SimpleDateFormat")
	private String getPhotoFileName(int i) {
		Log.i(tag, "getPhotoFileName获得照片名称");
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"'IMG'_yyyyMMdd_HHmmss");
		if (i == 1) {
			return dateFormat.format(date) + "_crop.JPEG";
		} else {

			return dateFormat.format(date) + ".JPEG";
		}
	}

	/*** 拍照 **/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i(tag, "void onActivityResult拍照/选照片");
		switch (requestCode) {
		case PHOTO_REQUEST_TAKEPHOTO:// 当选择拍照时调用
			// if (data != null)
			startPhotoZoom(Uri.fromFile(tempFile), 300);
			break;

		case PHOTO_REQUEST_GALLERY:// 当选择从本地获取图片时
			// 做非空判断，当我们觉得不满意想重新剪裁的时候便不会报异常，下同
			if (data != null)
				startPhotoZoom(data.getData(), 300);
			break;

		case PHOTO_REQUEST_CUT:// 返回的结果
			if (data != null) {
				Bitmap bitmap = data.getParcelableExtra("data");
				uploadFile = saveToLocal(bitmap);
				setPicToView(bitmap);

				new Thread(new Runnable() {
					public void run() {

						Log.i(tag, "UploadPhoto上传至网站");

						String request = null;
						String url = HttpUtil.IMG_URL_SUB;
						System.out.println("uploadFile--->"
								+ uploadFile.getName());
						// UploadPhotoUtil up = new UploadPhotoUtil();

						request = UploadUtil.uploadFile(uploadFile, url);
						// progressDialog.dismiss();
						mMessage = handler.obtainMessage(3);

						mMessage.obj = request;
						mMessage.sendToTarget();

					}
				}).start();
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);

	}

	/***
	 * void startActivityForResult
	 * */
	private void startPhotoZoom(Uri uri, int size) {
		Log.i(tag, "void startPhotoZoom还是拍照/选照片，裁剪");

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");

		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);

		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", size);
		intent.putExtra("outputY", size);
		intent.putExtra("return-data", true);

		startActivityForResult(intent, PHOTO_REQUEST_CUT);
	}

	/**** 将进行剪裁后的图片显示到UI界面上 */
	private void setPicToView(Bitmap bitmap) {
		if (isHeadCopy) {
			 Drawable drawable = new BitmapDrawable(bitmap);
			 userAvatorImageView.setImageDrawable(drawable);
			
		}
		// picChange = 1;
		isInfoChange = true;
		Log.i(tag, "setPicToView显示至ui");
		Toast.makeText(getApplicationContext(), "照片已提交！", Toast.LENGTH_LONG)
				.show();
		;
	}

	/** 保存至本地 */
	public File saveToLocal(Bitmap bitmap) {
		Log.i(tag, "saveToLocal保存至本地");
		// 需要裁剪后保存为新图片

		File f = new File(Environment.getExternalStorageDirectory(),
				getPhotoFileName(1));
		try {
			f.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		FileOutputStream fOut = null;
		try {
			fOut = new FileOutputStream(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		/*
		 * fileName = f.getName(); uploadFilePath = f.getPath();
		 */
		return f;

	}

	boolean infoCheck(String res) {
		if (res != null && !res.equals("") && !res.equals("null")) {

			return true;
		} else {
			return false;
		}

	}

}
