package com.daguo.util.adapter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.daguo.R;
import com.daguo.util.beans.ShuoShuoContent;
import com.daguo.utils.HttpUtil;
import com.nostra13.universalimageloader.core.ImageLoader;

public class SC_ShuoShuoAdapter extends BaseAdapter {
	@SuppressLint("SimpleDateFormat")
	private Context context;
	private List<ShuoShuoContent> infos;
	private ShuoShuoContent info;
	LayoutInflater inflater;
	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public SC_ShuoShuoAdapter(Context context, List<ShuoShuoContent> infos) {
		this.context = context;
		this.infos = infos;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return infos.size();
	}

	@Override
	public Object getItem(int arg0) {
		return infos.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		ViewHoldler holdler = null;
		if (view == null) {
			view = inflater.inflate(R.layout.item_sc_shuoshuoadapter, null);
			holdler = getHolder(view);
			view.setTag(holdler);

		} else {
			holdler = (ViewHoldler) view.getTag();
		}

		if (infos != null) {

			info = infos.get(position);
			holdler.content.setText(info.getContent());
			holdler.name.setText(info.getP_name());
			// holdler.photo.setImageDrawable(info.getP_photo());
			holdler.pinglun.setText("评论(" + info.getFeedback_count() + ")");
			holdler.time.setText(handTime(info.getCreatTime()));
			holdler.zan.setText("赞(" + info.getGood_count() + ")");
			// holdler.img_content.setImageDrawable(info.getImg_path());

			if (info.getP_photo() != null && !info.getP_photo().equals("")) {

				ImageLoader.getInstance().displayImage(
						HttpUtil.IMG_URL + info.getP_photo(), holdler.photo);
			} else {
				holdler.photo.setImageResource(R.drawable.user_logo);
			}
			if (info.getImg_path() != null && !info.getImg_path().equals("")) {

				ImageLoader.getInstance().displayImage(
						HttpUtil.IMG_URL + info.getImg_path(),
						holdler.img_content);
			} else {
				holdler.img_content.setVisibility(View.GONE);
			}

		} else {
			// no !
		}

		return view;
	}

	class ViewHoldler {
		ImageView photo;
		TextView name;
		TextView content;
		TextView time;
		TextView zan;
		TextView pinglun;
//		ImageButton caozuo;
		ImageView img_content;

	}

	ViewHoldler getHolder(View view) {
		ViewHoldler holdler = new ViewHoldler();
		holdler.content = (TextView) view.findViewById(R.id.content_text);
		holdler.name = (TextView) view.findViewById(R.id.name);
		holdler.photo = (ImageView) view.findViewById(R.id.photo);
		holdler.pinglun = (TextView) view.findViewById(R.id.pinglun_name);
		holdler.time = (TextView) view.findViewById(R.id.date);
		holdler.zan = (TextView) view.findViewById(R.id.favuor_name);
//		holdler.caozuo = (ImageButton) view.findViewById(R.id.reply_content);
		holdler.img_content = (ImageView) view.findViewById(R.id.image_content);
		return holdler;
	}

	/**
	 * 处理时间
	 * 
	 * @param string
	 * @return
	 */
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

}
