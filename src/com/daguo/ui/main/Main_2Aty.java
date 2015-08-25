package com.daguo.ui.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.base.Fragment_Mall_Item;
import com.daguo.util.beans.Type;
import com.daguo.utils.HttpUtil;
import com.daguo.utils.NetCheckUtil;

public class Main_2Aty extends FragmentActivity {
	// 取服务器商品大类字典表 需要的变量
	public static List<String> nameList = new ArrayList<String>();
	// public static List<String> idList = new ArrayList<String>();
	private String toolsList[];
	private TextView toolsTextViews[];
	private View views[];
	// public static Map<String, String> map = new HashMap<String, String>();
	public static List<Type> lists = new ArrayList<Type>();
	// 界面
	private ScrollView scrollView;
	private ViewPager shop_pager;
	private ShopAdapter shopAdapter;

	private int scrllViewWidth = 0, scrollViewMiddle = 0;

	private LayoutInflater inflater;
	private int currentItem = 0;

	@Override
	protected void onResume() {
		super.onResume();
		NetCheckUtil.checkNetworkState(Main_2Aty.this);
		Log.i("Main2Aty", "onresume");
	}

	@Override
	public View onCreateView(String name, Context context, AttributeSet attrs) {
		return super.onCreateView(name, context, attrs);
	}

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_main2);
		scrollView = (ScrollView) findViewById(R.id.tools_scrlllview);

		shopAdapter = new ShopAdapter(this.getSupportFragmentManager());
		inflater = LayoutInflater.from(this);

		new Thread(new Runnable() {
			public void run() {
				try {
					String urlString = HttpUtil.DICT_GOODS;
					String resString = HttpUtil.getRequest(urlString);
					JSONObject jsonObject = new JSONObject(resString);
					JSONArray array = jsonObject.getJSONArray("rows");
					if (array.length() > 0) {

						for (int i = 0; i < array.length(); i++) {

							String name = array.optJSONObject(i).getString(
									"name");
							String id = array.optJSONObject(i).getString("id");
							nameList.add(name);
							// idList.add(id);
							// map.put(id, name);
						}
						if (toolsList == null) {

							toolsList = nameList.toArray(new String[(nameList
									.size())]);
						}

					} else {
						// 数组小于1，说明没有字典，不过这个一般不会这样 ，
					}

					List<Type> infos = new ArrayList<Type>();
					Type info = null;

					String url = HttpUtil.QUERY_GOODSLIST + "&page=1";
					String res = HttpUtil.getRequest(url);
					JSONObject json = new JSONObject(res);
					JSONArray arr = json.getJSONArray("rows");
					for (int i = 0; i < arr.length(); i++) {

						info = new Type();
						String id = arr.optJSONObject(i).getString("id");
						String img_path = arr.optJSONObject(i).getString(
								"img_path");
						String name = arr.optJSONObject(i).getString("name");
						String price = arr.optJSONObject(i).getString("price");
						String score = arr.optJSONObject(i).getString("score");
						String type_id = arr.optJSONObject(i).getString(
								"type_id");
						String type_name = arr.optJSONObject(i).getString(
								"type_name");
						String goods_desc = arr.optJSONObject(i).getString(
								"goods_desc");
						String thumb_path = arr.optJSONObject(i).getString(
								"thumb_path");

						info.setId(id);
						info.setImg_path(img_path);
						info.setName(name);
						info.setPrice(price);
						info.setScore(score);
						info.setType_id(type_id);
						info.setType_name(type_name);
						info.setGoods_desc(goods_desc);
						info.setThumb_path(thumb_path);
						infos.add(info);

					}
					if (lists.size()==0) {

						lists.addAll(infos);
					}
				} catch (Exception e) {
				}
				runOnUiThread(new Runnable() {
					public void run() {

						showToolsView();
						initPager();
					}
				});
			}
		}).start();

	}// oncreate

	@Override
	public void onAttachFragment(android.app.Fragment fragment) {
		super.onAttachFragment(fragment);
		Log.i("Main2Aty", "onattachfragment");
	}

	@Override
	public View onCreateView(View parent, String name, Context context,
			AttributeSet attrs) {
		Log.i("Main2Aty", "oncreateview");
		return super.onCreateView(parent, name, context, attrs);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i("Main2Aty", "onstart");
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		Log.i("Main2Aty", "onrestart");
	}

	@Override
	protected void onResumeFragments() {
		super.onResumeFragments();
		Log.i("Main2Aty", "onresume");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i("Main2Aty", "onpause");
	}

	@Override
	protected void onStop() {
		super.onStop();
		Log.i("Main2Aty", "onStop");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("Main2Aty", "ondestory");
	}

	private void initPager() {
		shop_pager = (ViewPager) findViewById(R.id.goods_pager);
		shop_pager.setAdapter(shopAdapter);
		shop_pager.setOnPageChangeListener(onPageChangeListener);
	}

	private void showToolsView() {
		LinearLayout toolsLayout = (LinearLayout) findViewById(R.id.tools);
		toolsTextViews = new TextView[toolsList.length];
		views = new View[toolsList.length];
		for (int i = 0; i < toolsList.length; i++) {
			View view = inflater.inflate(R.layout.item_b_top_nav_layout, null);
			view.setId(i);
			view.setOnClickListener(toolsItemListener);
			TextView textView = (TextView) view.findViewById(R.id.text);
			textView.setText(toolsList[i]);
			toolsLayout.addView(view);
			toolsTextViews[i] = textView;
			views[i] = view;
		}
		changeTextColor(0);
	}

	/**
	 * 字体颜色
	 * 
	 * @param id
	 */
	private void changeTextColor(int id) {
		for (int i = 0; i < toolsTextViews.length; i++) {
			if (i != id) {
				toolsTextViews[i]
						.setBackgroundResource(android.R.color.transparent);
				toolsTextViews[i].setTextColor(0xff000000);
			}
		}
		toolsTextViews[id].setBackgroundResource(android.R.color.white);
		toolsTextViews[id].setTextColor(0xffff5d5e);
	}

	/**
	 * 
	 */
	private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int arg0) {
			if (shop_pager.getCurrentItem() != arg0)
				shop_pager.setCurrentItem(arg0);
			if (currentItem != arg0) {
				changeTextColor(arg0);
				changeTextLocation(arg0);
			}
			currentItem = arg0;
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};

	private void changeTextLocation(int clickPosition) {

		int x = (views[clickPosition].getTop() - getScrollViewMiddle() + (getViewheight(views[clickPosition]) / 2));
		scrollView.smoothScrollTo(0, x);
	}

	private int getScrollViewMiddle() {
		if (scrollViewMiddle == 0)
			scrollViewMiddle = getScrollViewheight() / 2;
		return scrollViewMiddle;
	}

	private int getScrollViewheight() {
		if (scrllViewWidth == 0)
			scrllViewWidth = scrollView.getBottom() - scrollView.getTop();
		return scrllViewWidth;
	}

	private int getViewheight(View view) {
		return view.getBottom() - view.getTop();
	}

	/**
	 * 总分类栏目 点击后显示的详细物品
	 */
	private View.OnClickListener toolsItemListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
			shop_pager.setCurrentItem(v.getId());
		}
	};

	/**
	 * 
	 * @author Bugs_Rabbit 時間： 2015-8-12 下午5:32:18
	 */
	private class ShopAdapter extends FragmentPagerAdapter {
		public ShopAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int arg0) {
			Fragment fragment = new Fragment_Mall_Item();
			Bundle bundle = new Bundle();
			String str = toolsList[arg0];
			bundle.putString("typename", str);
			fragment.setArguments(bundle);
			return fragment;
		}

		@Override
		public int getCount() {
			return toolsList.length;
		}
	}

}
