package com.daguo.ui.school.shuoshuo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.util.adapter.SC_ShuoShuoAdapter;
import com.daguo.util.base.AutoListView;
import com.daguo.util.base.AutoListView.OnLoadListener;
import com.daguo.util.base.AutoListView.OnRefreshListener;
import com.daguo.util.beans.ShuoShuoContent;
import com.daguo.utils.HttpUtil;

public class SC_ShuoShuoAty extends Activity implements OnRefreshListener,
		OnLoadListener, OnClickListener, OnItemClickListener {
	private String tag = "SC_ShuoShuoAty";
	private String p_photo;
	private AutoListView autoListView;
	private ImageButton addButton, backButton;
	private SC_ShuoShuoAdapter adapter;
	private List<ShuoShuoContent> lists = new ArrayList<ShuoShuoContent>();
	private ShuoShuoContent list;
	// 页码
	private int pageIndex = 1;

	Message message;
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				// 加载
				if (msg.obj != null) {
					List<ShuoShuoContent> abc = (List<ShuoShuoContent>) msg.obj;
					lists.addAll(abc);
					adapter.notifyDataSetChanged();
				}
				break;

			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_sc_shuoshuo);
		SharedPreferences sp = getSharedPreferences("userinfo",
				Context.MODE_WORLD_READABLE);
		p_photo = sp.getString("photo", "");
		init();
		loadData(0);

	}

	/**
	 * init view
	 */
	private void init() {
		autoListView = (AutoListView) findViewById(R.id.autoListView);
		adapter = new SC_ShuoShuoAdapter(SC_ShuoShuoAty.this, lists);
		autoListView.setAdapter(adapter);
		autoListView.setOnRefreshListener(this);
		autoListView.setOnLoadListener(this);
		autoListView.setOnItemClickListener(this);
		addButton = (ImageButton) findViewById(R.id.friend_more);
		backButton = (ImageButton) findViewById(R.id.friend_back);
		addButton.setOnClickListener(this);
		backButton.setOnClickListener(this);

	}

	void loadData(int loadType) {
		switch (loadType) {
		case 0:
			new Thread(new Runnable() {
				public void run() {
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

					List<ShuoShuoContent> contents = new ArrayList<ShuoShuoContent>();
					try {
						String url = HttpUtil.QUERY_SHUOSHUO + "&rows=10&page="
								+ pageIndex;
						String res = HttpUtil.getRequest(url);
						if (res != null) {
							// 获取该类信息
							JSONObject js = new JSONObject(res);
							JSONArray array = js.getJSONArray("rows");

							if (array.length() > 0) {
								if (js.getInt("pageNum") < pageIndex) {
									Toast.makeText(SC_ShuoShuoAty.this,
											"到底了。。。", Toast.LENGTH_SHORT)
											.show();
								} else {

									for (int i = 0; i < array.length(); i++) {
										list = new ShuoShuoContent();
										String id = array.optJSONObject(i)
												.getString("id");
										String time = array.optJSONObject(i)
												.getString("create_time");
										String img_path = array
												.optJSONObject(i).getString(
														"img_path");
										String content = array.optJSONObject(i)
												.getString("content");
										String good_count = array
												.optJSONObject(i).getString(
														"good_count");
										String feedback_count = array
												.optJSONObject(i).getString(
														"feedback_count");
										String type = array.optJSONObject(i)
												.getString("type");
										String type_name = array.optJSONObject(
												i).getString("type_name");
										String p_id = array.optJSONObject(i)
												.getString("p_id");
										String p_name = array.optJSONObject(i)
												.getString("p_name");
										list.setContent(content);
										list.setCreatTime(time);
										list.setFeedback_count(feedback_count);
										list.setGood_count(good_count);
										list.setId(id);
										list.setImg_path(img_path);
										list.setP_id(p_id);
										list.setP_name(p_name);
										list.setP_photo(p_photo);
										list.setType(type);
										list.setType_name(type_name);
										contents.add(list);
									}
									message = handler.obtainMessage(0);
									message.obj = contents;
									message.sendToTarget();

								}
							} else {
								// 没有数据
								Log.e(tag, "没有数据");

							}

						} else {
							// 查不到该类信息
							Log.e(tag, "查不到该信息");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}).start();

			break;

		default:
			break;
		}
	}

	@Override
	public void onLoad() {
		// TODO Auto-generated method stub
		pageIndex++;
		autoListView.onLoadComplete();
		loadData(0);

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		lists.clear();
		pageIndex = 1;
		loadData(0);
		autoListView.onRefreshComplete();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.friend_back:
			// back
			finish();

			break;
		case R.id.friend_more:
			// add
			Intent intent = new Intent(SC_ShuoShuoAty.this,
					SC_ShuoShuo_WriteAty.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position,
			long arg3) {
		// TODO Auto-generated method stub

	}

}
