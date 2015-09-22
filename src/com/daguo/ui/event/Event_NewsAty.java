package com.daguo.ui.event;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.daguo.R;
import com.daguo.util.adapter.NewsAdapter;
import com.daguo.util.base.AutoListView;
import com.daguo.util.base.AutoListView.OnLoadListener;
import com.daguo.util.base.AutoListView.OnRefreshListener;
import com.daguo.util.beans.News;
import com.daguo.utils.HttpUtil;

public class Event_NewsAty extends Activity implements OnRefreshListener,
		OnLoadListener {
	private ImageButton backBtn;
	private AutoListView listView;
	private int pageIndex = 1;
	Message msg;

	List<News> lists = new ArrayList<News>();
	NewsAdapter adapter;

	/*
	 * 轮播广告
	 */
	private ViewPager tuijianViewPager;// 图片
	private RadioGroup radioGroup1;// 框
	private RadioButton rd0, rd1;// 圆点
	private ArrayList<View> items = new ArrayList<View>();// view视图
	private int[] imgResIDs = new int[] { R.drawable.tabshop_lunbo1,
			R.drawable.tabhome_ad01 }; // 资源，本地的图片
	private int[] radioButtonID = new int[] { R.id.radio0, R.id.radio1 };// 圆点id
	private int mCurrentItem = 0;// 当前item
	private Runnable mPagerAction;// 线程
	private int mItem;// item
	private boolean isFrist = true;// 判断是否是第一个

	Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				// 有新闻
				List<News> aaa = (List<News>) msg.obj;
				lists.addAll(aaa);
				listView.setResultSize(lists.size());
				adapter.notifyDataSetChanged();
				break;
			case 1:
				// 无任何新闻

				break;

			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_event_news);
		init();
		loadData();
		adapter = new NewsAdapter(Event_NewsAty.this, lists);
		listView.setAdapter(adapter);

		tuijianViewPager = (ViewPager) findViewById(R.id.tuijian_vPager);
		radioGroup1 = (RadioGroup) findViewById(R.id.radioGroup1);
		initAllItems();
		tuijianViewPager.setAdapter(new PA());
		tuijianViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(final int arg0) {
				mCurrentItem = arg0 % items.size();
				tuijianViewPager.setCurrentItem(mCurrentItem);
				radioGroup1.check(radioButtonID[mCurrentItem]);
				items.get(arg0).findViewById(R.id.tuijian_header_img)
						.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// Toast.makeText(Main_1Aty.this,
								// "选择了第" + arg0 + "个界面，跳转至相关推荐页", 1000)
								// .show();
								// if (arg0 == 0) {
								// Intent intent = new Intent(Main_1Aty.this,
								// Event_AwardsAty.class);
								// startActivity(intent);
								//
								// } else if (arg0 == 1) {
								// // TODO 选号界面
								// Intent intent = new Intent(Main_1Aty.this,
								// MobileAty.class);
								// startActivity(intent);
								//
								// } else if (arg0 == 2) {
								// Toast.makeText(Main_1Aty.this,
								// "恭喜您已正式成为大果校园新生！",
								// Toast.LENGTH_SHORT).show();
								//
								// }
							}
						});
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

		});

	}

	/**
	 * 初始化 点击监听
	 */
	private void init() {
		backBtn = (ImageButton) findViewById(R.id.friend_back);// 后退按钮
		listView = (AutoListView) findViewById(R.id.autoListView);// listview
		listView.setOnRefreshListener(this);
		listView.setOnLoadListener(this);

		backBtn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 返回
				finish();
			}
		});
		// listView.setOnItemClickListener(new OnItemClickListener() {
		//
		// @Override
		// public void onItemClick(AdapterView<?> parent, View view,
		// int position, long ids) {
		// // 点击进入详情
		//
		//
		// }
		// });
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long ids) {
				// 点击进入详情
				Intent intent = new Intent(Event_NewsAty.this,
						Event_News_DetailAty.class);
				intent.putExtra("id", lists.get(position-1).getId());
				startActivity(intent);
			}
		});
	}

	void loadData() {
		new Thread(new Runnable() {
			public void run() {
				List<News> infos = new ArrayList<News>();
				News info = null;
				try {
					String url = HttpUtil.QUERY_NEWS + "&rows=12&page="
							+ pageIndex;
					String res = HttpUtil.getRequest(url);
					JSONObject js = new JSONObject(res);
					int aaa = js.getInt("total");
					if (aaa != 0) {
						JSONArray array = js.getJSONArray("rows");

						for (int i = 0; i < array.length(); i++) {
							info = new News();
							String id = array.optJSONObject(i).getString("id");
							String content = array.optJSONObject(i).getString(
									"content");
							String img_path = array.optJSONObject(i).getString(
									"img_path");
							String img_src = array.optJSONObject(i).getString(
									"img_src");
							String menu_id = array.optJSONObject(i).getString(
									"menu_id");
							String school_id = array.optJSONObject(i)
									.getString("school_id");
							String status = array.optJSONObject(i).getString(
									"status");
							String title = array.optJSONObject(i).getString(
									"title");
							String title2 = array.optJSONObject(i).getString(
									"title2");

							info.setId(id);
							info.setContent(content);
							info.setImg_path(img_path);
							info.setImg_src(img_src);
							info.setMenu_id(menu_id);
							info.setSchool_id(school_id);
							info.setStatus(status);
							info.setTitle(title);
							info.setTitle2(title2);
							infos.add(info);
						}
						msg = handler.obtainMessage(0);
						msg.obj = infos;
						msg.sendToTarget();

					} else {
						// 空的数据
						msg = handler.obtainMessage(1);
						msg.sendToTarget();
					}

				} catch (Exception e) {
				}
			}
		}).start();
	}

	@Override
	public void onLoad() {
		pageIndex++;
		loadData();
		listView.onRefreshComplete();
	}

	@Override
	public void onRefresh() {
		pageIndex = 1;
		lists.clear();
		loadData();
		listView.onRefreshComplete();
	}

	/**
	 * 用于初始化界面
	 * ******************************************************************
	 * ************
	 */
	/**
	 * 翻页适配
	 * 
	 * @author Bugs_Rabbit 時間： 2015-8-3 上午10:51:09
	 */
	class PA extends PagerAdapter {

		@Override
		public Object instantiateItem(View container, int position) {
			View layout = items.get(position % items.size());
			tuijianViewPager.addView(layout);
			return layout;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			View layout = items.get(position % items.size());
			tuijianViewPager.removeView(layout);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;

		}

		@Override
		public int getCount() {
			return imgResIDs.length;
		}

	}

	private void initAllItems() {
		for (int i = 0; i < imgResIDs.length; i++) {
			items.add(initPagerItem(imgResIDs[i]));
		}
		mItem = items.size();
	}

	private View initPagerItem(int resID) {
		View layout1 = getLayoutInflater().inflate(R.layout.item_lunbo_header,
				null);
		ImageView imageView1 = (ImageView) layout1
				.findViewById(R.id.tuijian_header_img);
		imageView1.setImageResource(resID);
		return layout1;
	}

}
