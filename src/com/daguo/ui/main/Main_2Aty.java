package com.daguo.ui.main;

import com.daguo.R;
import com.daguo.ui.school.School_MainAty;
import com.daguo.util.alipay.PayDemoActivity;
import com.mopote.sdk.surface.SurfaceApplication;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main_2Aty extends Activity{
	Button btn1,btn2,btn3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_main2);
		btn1 = (Button) findViewById(R.id.button1);
		btn1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent= new Intent(Main_2Aty.this,School_MainAty.class);
				startActivity(intent);
			}
		});
		btn2=(Button) findViewById(R.id.button2);
		btn2.setOnClickListener(new  View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(Main_2Aty.this,PayDemoActivity.class);
				startActivity(intent);
			}
		});
		
		if (!SurfaceApplication.isApplicationInited()) {
			//application中为：
			//SurfaceApplication.setPdCode(this);
			//Activity中为：
			SurfaceApplication.setPdCode(getApplication());
}

		btn3=(Button) findViewById(R.id.button3);
		btn3.setOnClickListener(new  View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent =new Intent(Main_2Aty.this,com.mopote.sdk.surface.FlowShopActivity.class);
				startActivity(intent);
			}
		});
	}
}
