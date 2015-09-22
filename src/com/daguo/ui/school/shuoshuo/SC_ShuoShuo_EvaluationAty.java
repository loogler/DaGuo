package com.daguo.ui.school.shuoshuo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.tsz.afinal.FinalBitmap;

import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import com.daguo.R;
import com.daguo.util.adapter.SC_ShuoShuo_EvaAdapter;
import com.daguo.util.beans.ShuoShuoContent;
import com.daguo.util.beans.ShuoShuo_Evaluate;
import com.daguo.utils.HttpUtil;
import com.daguo.view.dialog.CustomProgressDialog;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

public class SC_ShuoShuo_EvaluationAty extends Activity implements
		OnClickListener {
	private ImageView headView, imgView;
	private TextView user_nick, content, date, zan, pinglun;
	private RelativeLayout evaluat;

	private ListView listView;
	CustomProgressDialog dialog;
	private FinalBitmap finalBitmap;

	private PopupWindow editWindow;
	private EditText replyEdit;
	private Button sendBtn;
	private InputMethodManager manager;
	String id, good_count, feedback_count, content1, time, img_path, p_name,
			p_avator;
	String feedback_content;
	String p_id;

	// 数据
	private List<ShuoShuo_Evaluate> lists = new ArrayList<ShuoShuo_Evaluate>();
	private ShuoShuo_Evaluate list;
	private SC_ShuoShuo_EvaAdapter adapter;

	Message msg;

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				int cc = Integer.parseInt(good_count) + 1;
				zan.setText("点赞数 " + String.valueOf(cc));// 点赞数+1
				dialog.dismiss();

				break;
			case 1:
				int cc1 = Integer.parseInt(good_count) - 1;
				zan.setText("点赞数" + String.valueOf(cc1));// 点赞数-1
				dialog.dismiss();
				break;

			case 2:
				// 发表说说

				dialog.dismiss();

				break;
			case 3:
				// 初始化界面
				List<ShuoShuo_Evaluate> aaa = (List<ShuoShuo_Evaluate>) msg.obj;
				lists.clear();
				lists.addAll(aaa);
				adapter.notifyDataSetChanged();

				break;
			case 4:
				// 界面无评论内容
				Toast.makeText(SC_ShuoShuo_EvaluationAty.this, "暂无评论",
						Toast.LENGTH_SHORT).show();
				break;
			case 5:
				// 发表完评论处理

				editWindow.dismiss();

				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_sc_shuoshuo_evaluation);
		SharedPreferences sp = getSharedPreferences("userinfo",
				Context.MODE_WORLD_READABLE);
		p_id = sp.getString("id", "");
		Intent i = getIntent();
		id = i.getStringExtra("id");
		good_count = i.getStringExtra("good_count");
		feedback_count = i.getStringExtra("feedback_count");
		content1 = i.getStringExtra("content");
		time = i.getStringExtra("time");
		img_path = i.getStringExtra("img_path");
		p_name = i.getStringExtra("p_name");
		p_avator = i.getStringExtra("p_avator");

		init();
		new Thread(new Init()).start();
		adapter = new SC_ShuoShuo_EvaAdapter(SC_ShuoShuo_EvaluationAty.this,
				lists);
		listView.setAdapter(adapter);

	}

	private void init() {
		evaluat = (RelativeLayout) findViewById(R.id.evaluat);
		headView = (ImageView) findViewById(R.id.photo);
		imgView = (ImageView) findViewById(R.id.image_content);
		user_nick = (TextView) findViewById(R.id.name);
		content = (TextView) findViewById(R.id.content_text);
		date = (TextView) findViewById(R.id.date);
		zan = (TextView) findViewById(R.id.favuor_name);
		pinglun = (TextView) findViewById(R.id.pinglun_name);
		listView = (ListView) findViewById(R.id.listview);

		imgView.setOnClickListener(this);
		pinglun.setOnClickListener(this);
		zan.setOnClickListener(this);

		date.setText(handTime(time));
		FinalBitmap.create(SC_ShuoShuo_EvaluationAty.this).display(headView,HttpUtil.IMG_URL + p_avator
				);
		if (img_path != null && !img_path.equals("")
				&& !img_path.equals("null") && !img_path.equals("[]")) {
			FinalBitmap.create(SC_ShuoShuo_EvaluationAty.this).display(imgView,HttpUtil.IMG_URL + img_path
					);
			
			
		} else {
			imgView.setVisibility(View.GONE);
		}

		user_nick.setText(p_name);
		content.setText(content1);
		zan.setText("赞 (" + good_count + ")");
		pinglun.setText("发表评论 ");

	}

	/**
	 * 获取当前说说的信息
	 * 
	 * @author Bugs_Rabbit 時間： 2015-8-27 下午10:41:37
	 */
	class Init implements Runnable {

		@Override
		public void run() {

			try {
				List<ShuoShuo_Evaluate> ls = new ArrayList<ShuoShuo_Evaluate>();
				String url = HttpUtil.QUERY_SHUOSHUO_EVA + "&page=1&rows=20";
				Map<String, String> map = new HashMap<String, String>();
				map.put("t_id", id);
				String res = HttpUtil.postRequest(url, map);
				JSONObject js = new JSONObject(res);
				int total = js.getInt("total");
				if (total != 0) {
					// 有评论
					JSONArray array = js.getJSONArray("rows");
					for (int i = 0; i < array.length(); i++) {
						list = new ShuoShuo_Evaluate();
						String parent_id = array.optJSONObject(i).getString(
								"parent_id");
						String content = array.optJSONObject(i).getString(
								"content");
						String create_time = array.optJSONObject(i).getString(
								"create_time");
						String p_id = array.optJSONObject(i).getString("p_id");
						String p_name = array.optJSONObject(i).getString(
								"p_name");
						String head_info = array.optJSONObject(i).getString(
								"head_info");
						list.setContent(content);
						list.setCreate_time(create_time);
						list.setHead_info(head_info);
						list.setP_id(p_id);
						list.setP_name(p_name);
						list.setParent_id(parent_id);
						ls.add(list);
					}

					msg = handler.obtainMessage(3);
					msg.obj = ls;
					msg.sendToTarget();

				} else {
					// 无评论
					msg = handler.obtainMessage(4);
					msg.sendToTarget();

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/**
	 * 点赞 点赞接口调用自动增加1个数
	 * 
	 * @author Bugs_Rabbit 時間： 2015-8-27 下午10:41:28
	 */
	class Zan_Add implements Runnable {

		@Override
		public void run() {
			// 修改点赞数

			try {
				String url = HttpUtil.SUBMIT_SHUSHUO_EVA;
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id);// id
				String res = HttpUtil.postRequest(url, map);
				JSONObject js = new JSONObject(res);
				String aaa = js.getString("msg");// 返回字段
				if (aaa.contains("操作失败")) {
					// 失败
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(SC_ShuoShuo_EvaluationAty.this,
									"提交失败，请重试", Toast.LENGTH_SHORT).show();
						}
					});
				} else {
					// 界面赞数增加+1
					msg = handler.obtainMessage(0);
					msg.sendToTarget();
				}

			} catch (Exception e) {
			}

		}
	}

	/**
	 * 点赞数-1
	 * 
	 * @author Bugs_Rabbit 時間： 2015-8-31 上午9:01:33
	 */
	class Zan_Min implements Runnable {

		@Override
		public void run() {

			try {
				int cc = Integer.parseInt(good_count) - 2;
				String url = HttpUtil.SUBMIT_SHUSHUO_EVA;
				Map<String, String> map = new HashMap<String, String>();
				map.put("id", id);// id
				map.put("godd_count", String.valueOf(cc));
				String res = HttpUtil.postRequest(url, map);
				JSONObject js = new JSONObject(res);
				String aaa = js.getString("msg");// 返回字段
				if (aaa.contains("操作失败")) {
					// 失败
					runOnUiThread(new Runnable() {
						public void run() {
							Toast.makeText(SC_ShuoShuo_EvaluationAty.this,
									"提交失败，请重试", Toast.LENGTH_SHORT).show();
						}
					});

				} else {
					// 界面赞数增加-1
					msg = handler.obtainMessage(1);
					msg.sendToTarget();
				}

			} catch (Exception e) {
			}

		}

	}

	/**
	 * 评论说说
	 * 
	 * @author Bugs_Rabbit 時間： 2015-8-28 上午10:31:24
	 */
	class ShuoShuo implements Runnable {

		@Override
		public void run() {
			// /先调出输入框 获取文字 提交 显示在界面
			feedback_content = replyEdit.getText().toString();
			if (feedback_content != null && !feedback_content.equals("")) {
				String url = HttpUtil.SUBMIT_SHUSHUO_EVA;
//				String url="http://192.168.1.103:8080/XYYYT/service/topicFeedback/saveOrUpdate?android=1";
				Map<String, String> map = new HashMap<String, String>();
				map.put("t_id", id);
				map.put("content", feedback_content);
				map.put("p_id", p_id);

				try {
					String res = HttpUtil.postRequest(url, map);
					// TODO 对结果做判定

					msg = handler.obtainMessage(5);
					msg.sendToTarget();
					new Thread(new Init()).start();

				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				// 空内容
				runOnUiThread(new Runnable() {
					public void run() {
						Toast.makeText(SC_ShuoShuo_EvaluationAty.this,
								"文字不能为空", Toast.LENGTH_SHORT).show();
					}
				});
			}
			// dialog.dismiss();

		}
	}

	private void showDiscuss() {

		View editView = getLayoutInflater().inflate(
				R.layout.item_shuoshuo_reply, null);
		editWindow = new PopupWindow(editView, LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT, true);
		editWindow.setBackgroundDrawable(getResources().getDrawable(
				R.color.white));
		editWindow.setOutsideTouchable(true);
		replyEdit = (EditText) editView.findViewById(R.id.reply);
		sendBtn = (Button) editView.findViewById(R.id.send_msg);

		replyEdit.setFocusable(true);
		replyEdit.requestFocus();

		// 设置焦点，不然无法弹出输入法
		editWindow.setFocusable(true);

		// 以下两句不能颠倒
		editWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
		editWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		editWindow.showAtLocation(evaluat, Gravity.BOTTOM, 0, 0);

		// 显示键盘
		manager = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
		manager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
		editWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				manager.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
			}
		});
		sendBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				new Thread(new ShuoShuo()).start();
			}
		});

	}

	/**
	 * 处理时间
	 * 
	 * @param string
	 * @return
	 */
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private String handTime(String time) {
		if (time == null || "".equals(time.trim())) {
			return "";
		}
		try {
			Date date = format.parse(time);
			long tm = System.currentTimeMillis();// 当前时间戳
			long tm2 = date.getTime();// 发表动态的时间戳
			long d = (tm - tm2) / 1000;// 时间差距 单位秒
			if ((d / (60 * 60 * 24)) > 0) {
				return d / (60 * 60 * 24) + "天前";
			} else if ((d / (60 * 60)) > 0) {
				return d / (60 * 60) + "小时前";
			} else if ((d / 60) > 0) {
				return d / 60 + "分钟前";
			} else {
				// return d + "秒前";
				return "刚刚";
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 
	 * @param position
	 * @param urls
	 */
	private void imageBrower(int position, String[] urls) {
		Intent intent = new Intent(SC_ShuoShuo_EvaluationAty.this,
				com.daguo.modem.photo.ImagePagerActivity.class);
		// 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
		intent.putExtra(
				com.daguo.modem.photo.ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
		intent.putExtra(
				com.daguo.modem.photo.ImagePagerActivity.EXTRA_IMAGE_INDEX,
				position);
		startActivity(intent);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.favuor_name:
			// 点赞 后台处理 界面锁定 结束赞数加一 。
			/**
			 * dialog = CustomProgressDialog.createDialog(
			 * SC_ShuoShuo_EvaluationAty.this, "加载中。。。"); dialog.show();
			 */
			break;
		case R.id.pinglun_name:
			// dialog = CustomProgressDialog.createDialog(
			// SC_ShuoShuo_EvaluationAty.this, "加载中。。。");
			// dialog.show();

			showDiscuss();

			break;
		case R.id.image_content:
			String [] urls=new  String []{img_path};
			
					imageBrower(0, urls);
			
			
			break;
			
		default:
			break;
		}
	}
}
