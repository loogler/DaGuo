package com.daguo.ui.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.RadioGroup;

import com.daguo.R;

@SuppressWarnings("deprecation")
/**
 * 
 * @author Bugs_Rabbit
 *  時間： 2015-7-28 下午2:09:30
 */
public class MainActivity extends Activity {



	private int index = 1;// 当前页卡编号
	private ViewPager viewPage;// 页卡内容
	private List<View> listViews;// Tab页面列表
	private RadioGroup radioGroup;// 底部栏
	private LocalActivityManager manager = null;
	private MyPagerAdapter mpAdapter = null;

	@Override
	protected void onStart() {
		Log.i("", "onStart()");
		super.onStart();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		Log.i("", "onNewIntent()");
		setIntent(intent);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
	}

	@Override
	public void onBackPressed() {
		Log.i("", "onBackPressed()");
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		Log.i("", "onPause()");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.i("", "onStop()");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		Log.i("", "onDestroy()");
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		if (getIntent() != null) {
			// 这个才是默认页面的决定因素
			index = getIntent().getIntExtra("index", 0);
			viewPage.setCurrentItem(index);
			setIntent(null);
		} else {
			if (index < 3) {
				index = index + 1;
				viewPage.setCurrentItem(index);
				index = index - 1;
				viewPage.setCurrentItem(index);

			} else if (index == 3) {
				index = index - 1;
				viewPage.setCurrentItem(index);
				index = index + 1;
				viewPage.setCurrentItem(index);
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		radioGroup = (RadioGroup) findViewById(R.id.rdg);
		viewPage = (ViewPager) findViewById(R.id.vPager);
		manager = new LocalActivityManager(this, true);
		manager.dispatchCreate(savedInstanceState);
		initViewPager();
		viewPage.setCurrentItem(0);
		viewPage.setOnPageChangeListener(new MyOnPageChangeListener());
		radioGroup
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup radioGroup, int id) {
						switch (id) {
						case R.id.home:
							index = 0;
							listViews.set(
									0,
									getView("A", new Intent(MainActivity.this,
											Main_1Aty.class)));
							mpAdapter.notifyDataSetChanged();
							viewPage.setCurrentItem(0);

							break;
						case R.id.interaction:
							index = 1;
							listViews.set(
									1,
									getView("B", new Intent(MainActivity.this,
											Main_2Aty.class)));
							mpAdapter.notifyDataSetChanged();
							viewPage.setCurrentItem(1);

							break;
						case R.id.shop:
							index = 2;
							listViews.set(
									2,
									getView("C", new Intent(MainActivity.this,
											Main_3Aty.class)));
							mpAdapter.notifyDataSetChanged();
							viewPage.setCurrentItem(2);

							break;
						case R.id.cent:
							index = 3;
							listViews.set(
									3,
									getView("D", new Intent(MainActivity.this,
											Main_4Aty.class)));
							mpAdapter.notifyDataSetChanged();
							viewPage.setCurrentItem(3);

							break;

						default:
							break;
						}
					}
				});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	void initViewPager() {

		Intent intent = null;
		listViews = new ArrayList<View>();
		mpAdapter = new MyPagerAdapter(listViews);
		intent = new Intent(MainActivity.this, Main_1Aty.class);
		listViews.add(getView("A", intent));
		intent = new Intent(MainActivity.this, Main_2Aty.class);
		listViews.add(getView("B", intent));
		intent = new Intent(MainActivity.this, Main_3Aty.class);
		listViews.add(getView("C", intent));
		intent = new Intent(MainActivity.this, Main_4Aty.class);
		listViews.add(getView("D", intent));
		viewPage.setOffscreenPageLimit(0);
		viewPage.setAdapter(mpAdapter);

		viewPage.setCurrentItem(0);
//		viewPage.setOnPageChangeListener(new MyOnPageChangeListener());

	}

	/**
	 * 
	 * @param id
	 * @param intent
	 * @return
	 */

	public View getView(String id, Intent intent) {
		return manager.startActivity(id, intent).getDecorView();
	}

	/*****************************split line**********************************/
	/**
	 * 
	 * @author Bugs_rabbit
	 * @function 页卡适配
	 */
	class MyPagerAdapter extends PagerAdapter {

		public List<View> mListViews;

		public MyPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPager) arg0).removeView(mListViews.get(arg1));
		}

		@Override
		public void finishUpdate(View arg0) {
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public Object instantiateItem(View arg0, int arg1) {
			((ViewPager) arg0).addView(mListViews.get(arg1), 0);
			return mListViews.get(arg1);
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == (arg1);
		}

		@Override
		public void restoreState(Parcelable arg0, ClassLoader arg1) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View arg0) {
		}

	}

	/**
	 * 
	 * @author Bugs_rabbit
	 * @function 翻页监听
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {

		public void onPageSelected(int arg0) {
			manager.dispatchResume();
			switch (arg0) {
			case 0:
				index = 0;
				radioGroup.check(R.id.home);
				listViews.set(
						0,
						getView("A", new Intent(MainActivity.this,
								Main_1Aty.class)));
				mpAdapter.notifyDataSetChanged();
				break;
			case 1:
				index = 1;
				radioGroup.check(R.id.interaction);
				listViews.set(
						1,
						getView("B", new Intent(MainActivity.this,
								Main_2Aty.class)));
				mpAdapter.notifyDataSetChanged();
				break;
			case 2:
				index = 2;
				radioGroup.check(R.id.shop);
				listViews.set(
						2,
						getView("C", new Intent(MainActivity.this,
								Main_3Aty.class)));
				mpAdapter.notifyDataSetChanged();
				break;
			case 3:
				index = 3;
				radioGroup.check(R.id.cent);
				listViews.set(
						3,
						getView("D", new Intent(MainActivity.this,
								Main_4Aty.class)));
				mpAdapter.notifyDataSetChanged();
				break;

			}
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		public void onPageScrollStateChanged(int arg0) {
		}
	}



}
