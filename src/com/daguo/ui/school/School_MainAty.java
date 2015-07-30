package com.daguo.ui.school;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;

/**
 * 
 * @author Bugs_rabbit
 * @function 校园主页 模仿同学说软件
 */
@SuppressWarnings("deprecation")
public class School_MainAty extends TabActivity {
	private TabHost tabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_school_main);
		tabHost = getTabHost();
		setTabs();

	}

	void setTabs() {
		addTabs("校园", R.drawable.school_main_tabselector_drawable1,
				School_Main1Aty.class);
		addTabs("课外", R.drawable.school_main_tabselector_drawable2,
				School_Main2Aty.class);
		addTabs("课外", R.drawable.school_main_tabselector_drawable2,
				School_Main2Aty.class);
		addTabs("说说", R.drawable.school_main_tabselector_drawable3,
				School_Main3Aty.class);
		addTabs("我", R.drawable.school_main_tabselector_drawable4,
				School_Main4Aty.class);
	}
/**
 * 
 * @param labelId 标签
 * @param drawableId 图片路径
 * @param c 跳转的class
 * 动态添加每个tab的方法。
 */
	void addTabs(String labelId, int drawableId, Class<?> c) {
		Intent intent = new Intent(this, c);
		TabHost.TabSpec spec = tabHost.newTabSpec("tab" + labelId);

		View tabIndicator = LayoutInflater.from(this).inflate(
				R.layout.item_school_main_tabindicator, getTabWidget(), false);
		TextView title = (TextView) tabIndicator.findViewById(R.id.title);
		title.setText(labelId);
		ImageView icon = (ImageView) tabIndicator.findViewById(R.id.icon);
		icon.setImageResource(drawableId);
		spec.setIndicator(tabIndicator);
		spec.setContent(intent);
		tabHost.addTab(spec);

	}
/**
 * 
 * @param b 参数view 
 * 点击事件，此处是覆盖的方法使底部焦点被夺去。
 */
	public void openCameraActivity(View b) {
		Toast.makeText(School_MainAty.this, "大果校园！！", Toast.LENGTH_SHORT)
				.show();
	}
	


	}


