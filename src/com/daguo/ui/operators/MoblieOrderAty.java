package com.daguo.ui.operators;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.ui.before.MyAppliation;
import com.daguo.ui.before.UserAgreementAty;
import com.daguo.ui.user.UserInfo_ModifyAty;
import com.daguo.util.alipay.PayDemoActivity;
import com.daguo.util.base.LineEditText;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.UploadUtil;
import com.daguo.view.dialog.MySelf_Mod_Photo;

public class MoblieOrderAty extends Activity implements OnClickListener {
	String tag = "mobileAty";
	private String p_id,orderInfoId,p_name;
	private LineEditText nameEditText, idEditText, telEditText,
			logisticsEditText;
	private Button uploadBtn, submitBtn;
	public static String num_name, num_id, num_tel, num_logistics, num_orderId,
			imgString;
	private TextView xieyi ;
	// 弹出窗
	MySelf_Mod_Photo menuWindow;
	// 拍照参数
	private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
	private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
	private static final int PHOTO_REQUEST_CUT = 3;// 结果

	// 创建一个以当前时间为名称的文件，获取相册资源
	private File tempFile;
	private File uploadFile;
	private boolean isPicChange = false;

	Message message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_mobileorder);
		MyAppliation.getInstance().addActivity(this);
		SharedPreferences sp = getSharedPreferences("userinfo",
				Context.MODE_WORLD_READABLE);
		p_id = sp.getString("id", "");
		p_name=sp.getString("name", "");

		nameEditText = (LineEditText) findViewById(R.id.nameEdit);
		idEditText = (LineEditText) findViewById(R.id.idEdit);
		telEditText = (LineEditText) findViewById(R.id.telEdit);
		logisticsEditText = (LineEditText) findViewById(R.id.logisticsEdit);
		xieyi = (TextView) findViewById(R.id.xieyi);
		xieyi.setOnClickListener(this);
		uploadBtn = (Button) findViewById(R.id.uploadbtn);
		uploadBtn.setOnClickListener(this);
		submitBtn = (Button) findViewById(R.id.submitbtn);
		submitBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.uploadbtn:
			menuWindow = new MySelf_Mod_Photo(MoblieOrderAty.this, this);// shilihua
			menuWindow.showAtLocation(
					MoblieOrderAty.this.findViewById(R.id.linearlayout_1),
					Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			Log.i(tag, "menuWindow执行完毕");
			// 上传身份证照片

			break;
		case R.id.submitbtn:
			String name = nameEditText.getText().toString().trim();
			String id = idEditText.getText().toString().trim();
			String tel = telEditText.getText().toString().trim();
			String logistics = logisticsEditText.getText().toString().trim();
			
			
			if (name != null && id != null & tel != null && logistics != null
					&& isPicChange) {
				num_name=name;
				num_id=id;
				num_tel=tel;
				num_logistics=logistics;
				
				if (p_name!=null &&!p_name.equals("")) {
					new Thread(UploadPhoto).start();
					// Intent intent = new Intent(MoblieOrderAty.this,
					// Pay_NumberAty.class);
					// startActivity(intent);
					
				}else {
					new AlertDialog.Builder(MoblieOrderAty.this)
					.setMessage("请先完善个人资料")
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(
										DialogInterface arg0,
										int arg1) {
									Intent intent = new Intent(
											MoblieOrderAty.this,
											UserInfo_ModifyAty.class);
									startActivity(intent);
								}
							}).create().show();
				}

			} else {
				Toast.makeText(MoblieOrderAty.this, "信息还没输入完整呢",
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
		case R.id.xieyi:
		
			// ComponentName componentname = new ComponentName(
			// BroadBandOrderAty.this, UserAgreementAty.class);
			Intent intent = new Intent(MoblieOrderAty.this,
					UserAgreementAty.class);
			MoblieOrderAty.this.startActivity(intent);
			// intent.setComponent(componentname);
		
			break;
		default:
			break;
		}
	}

	/**
	 * 获取照片，名称 使用系统当前日期加以调整作为照片的名称
	 */
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
			if (data != null)
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
		// Drawable drawable = new BitmapDrawable(bitmap);
		// imag_pic.setImageDrawable(drawable);
		// picChange = 1;
		isPicChange = true;
		Log.i(tag, "setPicToView显示至ui");

		Toast.makeText(getApplicationContext(), "照片已提交，审核通过后才可以显示！",
				Toast.LENGTH_LONG).show();
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

	Thread UploadPhoto = new Thread() {

		@Override
		public void run() {
			Log.i(tag, "UploadPhoto上传至网站");
			super.run();
			String request = null;
			String url = HttpUtil.IMG_URL_SUB;
			System.out.println("uploadFile--->" + uploadFile.getName());
//			UploadPhotoUtil up = new UploadPhotoUtil();

			request = UploadUtil.uploadFile(uploadFile, url);
			// progressDialog.dismiss();
			message = handler.obtainMessage(3);

			message.obj = request;
			message.sendToTarget();
		}
	};

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 3:
				if (UploadUtil.rescode == 200) {
					try {
						JSONObject jsonObject1 = new JSONObject(
								msg.obj.toString());
						String imgSRC = jsonObject1
								.getString("fileRelativePath");
						imgString = imgSRC;
						getOrder(HttpUtil.SUBMIT_ORDER_PUB, p_id, "2", "ba6cb325-65c1-4f53-87a7-bfe67b37adfe");
//
//						String url = HttpUtil.SUBMIT_ORDER_PUB;
//						Map<String, String> map = new HashMap<String, String>();
//						map.put("p_id", p_id);
//						map.put("pay_type",
//								"ba6cb325-65c1-4f53-87a7-bfe67b37adfe");
//						map.put("status", "0");
//						map.put("order_type", "1");
//						map.put("pay_status", "0");
//
//						String res = HttpUtil.postRequest(url, map);
//						JSONObject jsonObject = new JSONObject(res);
//						String orderDetailId = jsonObject.getJSONObject("obj")
//								.getString("id");
//						num_orderId = orderDetailId;
						String url1 = HttpUtil.SUBMIT_NUMBER_DETAIL;
						Map<String, String> map1 = new HashMap<String, String>();
						map1.put("order_id", orderInfoId);
						map1.put("order_name", num_name);
						map1.put("order_tel", num_tel);
						map1.put("order_id_card", num_id);
						map1.put("order_id_card_copy", imgString);
						map1.put("address", num_logistics);
						map1.put("phone_id", MobileAty.num_id);

						String res1 = HttpUtil.postRequest(url1, map1);
//						JSONObject jsObject = new JSONObject(res1);

						if (res1 != null && !res1.equals("")) {
							// 成功
						} else {
							// 失败
							Toast.makeText(MoblieOrderAty.this, "订单提交失败！",
									Toast.LENGTH_SHORT).show();
						}

						// TOD
					} catch (Exception e) {
					}

					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(MoblieOrderAty.this, "上传成功，请提交资料",
									Toast.LENGTH_SHORT).show();
							isPicChange = true;
						}
					});
				} else {
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(MoblieOrderAty.this, "上传失败，请重新上传",
									Toast.LENGTH_SHORT).show();
							isPicChange = false;
						}
					});
				}

			default:
				break;
			}

		};
	};
	

	/**
	 * // 生成一个订单，（公用订单），主表
	 * 
	 * @param pid
	 *            ===== 个人id
	 * @param order_type
	 *            ==== 商品类型 1 :宽带入网 2：入网选号 3：商品购物'
	 * @param pay_type
	 *            === 支付宝： ba6cb325-65c1-4f53-87a7-bfe67b37adfe
	 * @throws JSONException
	 */
	void getOrder(String urls, String pid, String order_type, String pay_type)
			throws JSONException {
		try {
			String url = urls;
			Map<String, String> sss = new HashMap<String, String>();
			sss.put("p_id", pid);// 个人账号id
			sss.put("status", "0");// 0 未处理 --1已处理 无需处理，只要我提交为0
			sss.put("order_type", order_type);// 商品类型 1 :宽带入网 2：入网选号
			// 3：商品购物'
			sss.put("pay_status", "0");// 支付状态
			sss.put("pay_type", pay_type);// 支付类型

			String resString;
			resString = HttpUtil.postRequest(url, sss);
			JSONObject jsonObject = new JSONObject(resString);
			if (jsonObject != null && !jsonObject.equals("")) {

				orderInfoId = jsonObject.getJSONObject("obj").getString("id");
				Intent intent = new Intent(MoblieOrderAty.this,
						PayDemoActivity.class);

				intent.putExtra("price", MobileAty.num_price);
				intent.putExtra("name", MobileAty.num_tel);
				intent.putExtra("detail", "手机号码");
				intent.putExtra("orderInfoId", orderInfoId);

				startActivity(intent);
			} else {
				Log.e("生成主表订单", "=====失败");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Log.d(tag, "orderInfoId*****" + orderInfoId);
	}


}
