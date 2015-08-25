package com.daguo.modem.adress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daguo.R;
import com.daguo.util.alipay.PayDemoActivity;
import com.daguo.utils.HttpUtil;

public class PersonAddress extends Activity {

	private ListView listView;

	private List<AddressInfo> address = new ArrayList<AddressInfo>();
	private AddressDB addressDB;
	private MyAdapter adapter;
	private RelativeLayout dizhi;
	private Button queding, tianjia;
	private String recivingAdress = null;
	private int posi = 0;
	public static String orderInfoId_unPay;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myset_address);

		listView = (ListView) this.findViewById(R.id.myset_address_listView1);
		dizhi = (RelativeLayout) this.findViewById(R.id.dizhi);

		addressDB = AddressDB.getInstance(getBaseContext());
		address = addressDB.queryAddress();

		if (address == null) {
			dizhi.setVisibility(View.VISIBLE);
		}

		adapter = new MyAdapter(getBaseContext());
		listView.setAdapter(adapter);
		tianjia = (Button) this.findViewById(R.id.pd_tianjia);
		tianjia.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent(PersonAddress.this,
						AddressChoose.class);
				startActivity(intent);
				// finish(); 不用注销
			}
		});
		queding = (Button) this.findViewById(R.id.pd_queding);
		queding.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (address == null) {
					// 没写收货地址
					Toast.makeText(PersonAddress.this, "收货地址还没有填写！",
							Toast.LENGTH_SHORT).show();
				} else {
					// 填写了地址，跳转付款界面，提交未付款的订单到服务器。
					new SubmitThread().start();
					Intent intent = new Intent(PersonAddress.this,
							PayDemoActivity.class);
					startActivity(intent);

				}
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					final int position, long arg3) {
				final AddressInfo a = address.get(position);
				AlertDialog dialog = new AlertDialog.Builder(PersonAddress.this)
						.setTitle("删除收货地址")
						.setMessage("确定删除这个收货地址吗?")
						.setPositiveButton("确定",
								new AlertDialog.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (addressDB.deleteAddress(a)) {
											Toast.makeText(getBaseContext(),
													"删除成功", Toast.LENGTH_LONG)
													.show();
										} else {
											Toast.makeText(getBaseContext(),
													"删除失败", Toast.LENGTH_LONG)
													.show();
										}

										address.remove(position);
										adapter.notifyDataSetChanged();

										if (address.size() == 0) {
											dizhi.setVisibility(View.VISIBLE);
										}
									}
								})
						.setNegativeButton("取消",
								new AlertDialog.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).create();// 创建
				// 显示对话框
				dialog.show();

				return true;
			}
		});

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				Intent i = new Intent(PersonAddress.this, BuyAddress.class);
				Bundle b = new Bundle();
				b.putSerializable("address", address.get(position));
				i.putExtra("address_id", b);
				startActivity(i);
			}
		});
	}

	class MyAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;

		public MyAdapter(Context context) {
			this.context = context;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return address != null ? address.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder viewHolder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.myset_adress_list_item,
						null);
				viewHolder = new ViewHolder();
				viewHolder.name = (TextView) convertView
						.findViewById(R.id.myset_address_item_name);
				viewHolder.listViewItem = (LinearLayout) convertView
						.findViewById(R.id.listview_item);
				viewHolder.provinces = (TextView) convertView
						.findViewById(R.id.myset_address_item_provinces);
				viewHolder.street = (TextView) convertView
						.findViewById(R.id.myset_address_item_street);
				viewHolder.phone = (TextView) convertView
						.findViewById(R.id.myset_address_phone);
				viewHolder.moren = (CheckBox) convertView
						.findViewById(R.id.loading_checkbox);
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			viewHolder.moren.setClickable(false);
			viewHolder.name.setText(address.get(position).getName());
			viewHolder.provinces.setText(address.get(position).getProvinces());
			viewHolder.street.setText(address.get(position).getStreet());
			viewHolder.phone.setText(address.get(position).getPhone());
			viewHolder.listViewItem.setTag(address.get(position).getId());

			if (address.get(position).isStatus()) {
				viewHolder.moren.setChecked(true);
				posi = position;

			} else {
				viewHolder.moren.setChecked(false);
			}

			return convertView;
		}

		class ViewHolder {
			LinearLayout listViewItem;
			TextView name, provinces, street, phone;
			CheckBox moren;
		}
	}

	class SubmitThread extends Thread {
		@Override
		public void run() {
			super.run();
			recivingAdress = "ID= " + address.get(posi).getId() + "名字= "
					+ address.get(posi).getName() + "手机号码= "
					+ address.get(posi).getPhone() + "模糊地址= "
					+ address.get(posi).getProvinces() + "精确地址= "
					+ address.get(posi).getStreet();
			try {
				String url = HttpUtil.SUBMIT_ORDER_PUB;
				Map<String, String> sss = new HashMap<String, String>();
//				sss.put("adress", recivingAdress);
//				sss.put("name", BroadBandOrderAty.name);
//				sss.put("order_tel", BroadBandOrderAty.tel);
//				sss.put("order_id_card", BroadBandOrderAty.id);
//				sss.put("broadband_id", BroadBandAty.orderid);
//				sss.put("p_id", "9d2b3efd-f3d0-4636-bfb4-12876764ac66");
//				sss.put("status", "0");
//				sss.put("order_type", "1");
//				sss.put("pay_status", "0");
//				sss.put("pay_type", "ba6cb325-65c1-4f53-87a7-bfe67b37adfe");
				String resString = HttpUtil.postRequest(url, sss);

				JSONObject jsonObject = new JSONObject(resString);
				String orderDetailId = jsonObject.getString("id");
				orderInfoId_unPay = orderDetailId;
				Log.i("orderInfo_di", orderInfoId_unPay);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
