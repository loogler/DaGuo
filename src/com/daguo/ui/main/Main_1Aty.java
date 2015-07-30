package com.daguo.ui.main;

import com.daguo.R;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
	private RelativeLayout rl1;
	private ImageView pointImageView;
	/*
	 * 轮播广告
	 */
	private ViewPager tuijianViewPager;// 图片
	private RadioGroup radioGroup1;// 框
	private RadioButton rd0, rd1;// 圆点
	/*
	 * 中间通知
	 */
	private TextView tv_news;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_main1);
		initViews();

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

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.lin1:
			Toast.makeText(Main_1Aty.this, "lin1", Toast.LENGTH_SHORT).show();
			break;
		case R.id.iv_mail:

			break;
		case R.id.rl1:
			Toast.makeText(Main_1Aty.this, "tv-news", Toast.LENGTH_SHORT)
					.show();

			break;

		default:
			break;
		}
	}
}
