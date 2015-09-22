package com.daguo.ui.main;

import java.util.ArrayList;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.modem.schedule.Main_Aty;
import com.daguo.ui.event.Event_AwardsAty;
import com.daguo.ui.event.Event_NewsAty;
import com.daguo.ui.operators.BroadBandAty;
import com.daguo.ui.operators.MobileAty;
import com.daguo.ui.operators.OperatorAty;
import com.daguo.ui.school.School_MainAty;
import com.daguo.ui.school.shuoshuo.SC_ShuoShuoAty;
import com.daguo.ui.user.UserInfo_ModifyAty;

/**
 * 
 * @author Bugs_rabbit
 * @function 主页（脸面设计）
 */
public class Main_1Aty extends Activity implements OnClickListener {
	private static String tag = "Main_1Aty";
	/*
	 * 头部image
	 */
	private LinearLayout lin1;

	private ImageView pointImageView;
	/*
	 * 轮播广告
	 */
	private ViewPager tuijianViewPager;// 图片
	private RadioGroup radioGroup1;// 框
	private RadioButton rd0, rd1;// 圆点
	private ArrayList<View> items = new ArrayList<View>();// view视图
	private int[] imgResIDs = new int[] { R.drawable.tabhome_lunbo_ad03,
			R.drawable.tabhome_lunbo_ad04, R.drawable.tabhome_ad02,
			R.drawable.tabhome_ad01 }; // 资源，本地的图片
	private int[] radioButtonID = new int[] { R.id.radio0, R.id.radio1,
			R.id.radio2, R.id.radio3 };// 圆点id
	private int mCurrentItem = 0;// 当前item
	private Runnable mPagerAction;// 线程
	private int mItem;// item
	private boolean isFrist = true;// 判断是否是第一个
	/*
	 * 中间通知
	 */
	private TextView tv_news;

	/**
	 * 中间功能按钮
	 */
	private LinearLayout lll;
	private RelativeLayout rl1, rl2, rl3, rl4, rl5, rl6, rl7, rl8;
	private ImageView iv_jiantou;

	/**
	 * 三张图片
	 */
	private ImageView iv1, iv2, iv3;

	/**
	 * 底部活动
	 */
	private LinearLayout ll_bottom1, ll_bottom2;
	private ImageView iv_buttom2, iv_buttom1;
	/**
	 * dialog 提示用户登录/注册
	 */
	private Dialog dia;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_main1);
		initViews();

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
								if (arg0 == 0) {
									Toast.makeText(Main_1Aty.this,
											"恭喜您已正式成为大果校园新生！",
											Toast.LENGTH_SHORT).show();

								} else if (arg0 == 1) {
									// TODO 选号界面
									Intent intent = new Intent(Main_1Aty.this,
											MobileAty.class);
									startActivity(intent);

								} else if (arg0 == 2) {

								} else if (arg0 == 3) {
									Intent intent = new Intent(Main_1Aty.this,
											Event_AwardsAty.class);
									startActivity(intent);
								}
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
		mPagerAction = new Runnable() {
			@Override
			public void run() {

				if (mItem != 0) {
					if (isFrist == true) {
						mCurrentItem = 0;
						isFrist = false;
					} else {
						if (mCurrentItem == items.size() - 1) {
							mCurrentItem = 0;
						} else {
							mCurrentItem++;
						}
					}

					tuijianViewPager.setCurrentItem(mCurrentItem);
					radioGroup1.check(radioButtonID[mCurrentItem]);

				}
				tuijianViewPager.postDelayed(mPagerAction, 4500);
			}
		};
		tuijianViewPager.postDelayed(mPagerAction, 100);

	}

	/**
	 * 实例化界面
	 */
	void initViews() {
		lin1 = (LinearLayout) this.findViewById(R.id.lin1);
		lin1.setOnClickListener(this);
		rl1 = (RelativeLayout) findViewById(R.id.rl1);
		rl1.setOnClickListener(this);
		pointImageView = (ImageView) this.findViewById(R.id.iv_point);
		tuijianViewPager = (ViewPager) this.findViewById(R.id.tuijian_vPager);
		radioGroup1 = (RadioGroup) this.findViewById(R.id.radioGroup1);
		rd0 = (RadioButton) this.findViewById(R.id.radio0);
		rd1 = (RadioButton) this.findViewById(R.id.radio1);
		tv_news = (TextView) this.findViewById(R.id.tv_news);
		tv_news.setOnClickListener(this);

		iv_jiantou = (ImageView) findViewById(R.id.iv_jiantou);
		lll = (LinearLayout) findViewById(R.id.lll);
		rl1 = (RelativeLayout) this.findViewById(R.id.rl1);
		rl2 = (RelativeLayout) this.findViewById(R.id.rl2);
		rl3 = (RelativeLayout) this.findViewById(R.id.rl3);
		rl4 = (RelativeLayout) this.findViewById(R.id.rl4);
		rl5 = (RelativeLayout) this.findViewById(R.id.rl5);
		rl6 = (RelativeLayout) this.findViewById(R.id.rl6);
		rl7 = (RelativeLayout) this.findViewById(R.id.rl7);
		rl8 = (RelativeLayout) this.findViewById(R.id.rl8);
		iv_jiantou.setOnClickListener(this);
		rl1.setOnClickListener(this);
		rl2.setOnClickListener(this);
		rl3.setOnClickListener(this);
		rl4.setOnClickListener(this);
		rl5.setOnClickListener(this);
		rl6.setOnClickListener(this);
		rl7.setOnClickListener(this);
		rl8.setOnClickListener(this);

		iv1 = (ImageView) this.findViewById(R.id.iv_pic1);
		iv2 = (ImageView) this.findViewById(R.id.iv_pic2);
		iv3 = (ImageView) this.findViewById(R.id.iv_pic3);
		iv1.setOnClickListener(this);
		iv2.setOnClickListener(this);
		iv3.setOnClickListener(this);

		iv_buttom2 = (ImageView) findViewById(R.id.iv_buttom2);
		iv_buttom1 = (ImageView) findViewById(R.id.iv_buttom1);
		iv_buttom2.setOnClickListener(this);
		iv_buttom1.setOnClickListener(this);

		// ll_bottom1 = (LinearLayout) this.findViewById(R.id.ll_bottom1);
		// ll_bottom2 = (LinearLayout) this.findViewById(R.id.ll_bottom2);
		// ll_bottom1.setOnClickListener(this);
		// ll_bottom2.setOnClickListener(this);

	}

	/********************************* split line ****************************************/
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

	/**
	 * 用于初始化界面
	 */
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

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lin1:
			Intent intents = new Intent(Main_1Aty.this, UserInfo_ModifyAty.class);
			startActivity(intents);

			// Toast.makeText(Main_1Aty.this, "大果校园欢迎您！", Toast.LENGTH_SHORT)
			// .show();
			// TODO 判断用户是否登录 sp取一下试试
			// dia = new Dialog(Main_1Aty.this);
			// dia.requestWindowFeature(Window.FEATURE_NO_TITLE);
			// dia.show();
			// final Window wd = dia.getWindow();
			// wd.setContentView(R.layout.view_login);
			// ImageView btn_close = (ImageView)
			// wd.findViewById(R.id.btn_close);
			// btn_close.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// dia.dismiss();
			// }
			// });
			// LinearLayout ll_login = (LinearLayout) wd
			// .findViewById(R.id.ll_login);
			// ll_login.setOnClickListener(new View.OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// Intent intent = new Intent(Main_1Aty.this,
			// MainLoginAty.class);
			// startActivity(intent);
			// dia.dismiss();
			// // 不结束activity 注册/登录结束还要回来。
			// }
			// });
			// LinearLayout ll_register = (LinearLayout) wd
			// .findViewById(R.id.ll_register);
			// ll_register.setOnClickListener(new View.OnClickListener() {
			//
			// @Override
			// public void onClick(View arg0) {
			// Intent intent = new Intent(Main_1Aty.this,
			// MainRegisterAty.class);
			// startActivity(intent);
			// dia.dismiss();
			// // 不结束activity 注册/登录结束还要回来。
			// }
			// });

			break;
		case R.id.iv_mail:

			break;
		case R.id.rl11:
			Toast.makeText(Main_1Aty.this, "功能咱未开放 ", Toast.LENGTH_SHORT)
					.show();

			break;
		case R.id.tv_news:
			Toast.makeText(getBaseContext(), "功能咱未开放 ", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.rl1:
			Intent intent2 = new Intent(Main_1Aty.this, School_MainAty.class);
			startActivity(intent2);
			// finish(); 不能在这里结束activity，保持让所有进程被结束时 还能回到这里。

			break;
		case R.id.rl2:

			Intent intent8 = new Intent(Main_1Aty.this, Event_NewsAty.class);
			startActivity(intent8);
			break;
		case R.id.rl3:

			// Toast.makeText(getBaseContext(), "跳转至移动生活", Toast.LENGTH_SHORT)
			// .show();
			Intent intent3 = new Intent(Main_1Aty.this, OperatorAty.class);
			startActivity(intent3);
			break;

		case R.id.rl4:
			Intent intent = new Intent(Main_1Aty.this, Main_Aty.class);
			startActivity(intent);
			break;
		case R.id.rl5:
			Toast.makeText(getBaseContext(), "往右翻页就可以啦", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.rl6:
			Toast.makeText(getBaseContext(), "功能咱未开放 ", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.rl7:
			Intent intent7 = new Intent(Main_1Aty.this, School_MainAty.class);
			startActivity(intent7);
			break;

		case R.id.rl8:
			Toast.makeText(getBaseContext(), "功能咱未开放 ", Toast.LENGTH_SHORT)
					.show();

			break;
		case R.id.iv_pic1:
			// Toast.makeText(getBaseContext(), "跳转至广告宣传/活动/功能1",
			// Toast.LENGTH_SHORT).show();
			break;
		case R.id.iv_pic2:
			// Toast.makeText(getBaseContext(), "跳转至广告宣传/活动/功能2",
			// Toast.LENGTH_SHORT).show();
			break;
		case R.id.iv_pic3:
			// Toast.makeText(getBaseContext(), "跳转至广告宣传/活动/功能3",
			// Toast.LENGTH_SHORT).show();
			Intent intent4 = new Intent(Main_1Aty.this, BroadBandAty.class);
			startActivity(intent4);
			break;
		case R.id.iv_buttom2:
			// Toast.makeText(getBaseContext(), "跳转至广告宣传/活动/功能1++",
			// Toast.LENGTH_SHORT).show();

			break;
		case R.id.iv_buttom1:
			// Toast.makeText(getBaseContext(), "跳转至广告宣传/活动/功能2++",
			// Toast.LENGTH_SHORT).show();
			Intent intent5 = new Intent(Main_1Aty.this, Event_AwardsAty.class);
			startActivity(intent5);
			break;
		case R.id.iv_jiantou:
			if (lll.getVisibility() == View.GONE) {
				lll.setVisibility(View.VISIBLE);
				iv_jiantou
						.setImageResource(R.drawable.tabhome1_function_shouqi);
			} else if (lll.getVisibility() == View.VISIBLE) {
				lll.setVisibility(View.GONE);
				iv_jiantou
						.setImageResource(R.drawable.tabhome1_function_zhankai);
			}

			break;
		default:
			break;
		}
	}
}
