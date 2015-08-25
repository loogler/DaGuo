package com.daguo.util.base;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.daguo.R;
import com.daguo.ui.before.MainLoginAty;
import com.daguo.ui.before.MainRegisterAty;
import com.daguo.utils.GetScreenRecUtil;

/**
 * 在主页下面的显示用于提示用户尚未登录/注册的去完成任务 属于弹窗
 * 
 * @author Bugs_Rabbit 時間： 2015-8-4 上午10:39:41
 */
public class LoginDialogView extends RelativeLayout {
	private Scroller mScroller;
	private int mScreenHeigh = 0;
	private int mScreenWidth = 0;
	private int downY = 0;
	private int moveY = 0;
	private int scrollY = 0;
	private int upY = 0;
	private Boolean isMoving = false;
	private int viewHeight = 0;
	public boolean isShow = false;
	public boolean mEnabled = true;
	public boolean mOutsideTouchable = true;
	private int mDuration = 800;
	private final static String TAG = "LoginView";

	public LoginDialogView(Context context) {
		super(context);
		init(context);
	}

	public LoginDialogView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public LoginDialogView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(final Context context) {
		setDescendantFocusability(FOCUS_AFTER_DESCENDANTS);
		setFocusable(true);
		mScroller = new Scroller(context);
		mScreenHeigh = GetScreenRecUtil.getWindowHeigh(context);
		mScreenWidth = GetScreenRecUtil.getWindowWidth(context);
		this.setBackgroundColor(Color.argb(0, 0, 0, 0));
		final View view = LayoutInflater.from(context).inflate(
				R.layout.view_login, null);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT);
		addView(view, params);// ViewGroup
		this.setBackgroundColor(Color.argb(0, 0, 0, 0));
		view.post(new Runnable() {

			@Override
			public void run() {
				viewHeight = view.getHeight();
			}
		});
		LoginDialogView.this.scrollTo(0, mScreenHeigh);
		ImageView btn_close = (ImageView) view.findViewById(R.id.btn_close);
		btn_close.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
		LinearLayout ll_login = (LinearLayout) view.findViewById(R.id.ll_login);
		ll_login.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent =new  Intent(context ,MainLoginAty.class);
				context.startActivity(intent);
				//不结束activity 注册/登录结束还要回来。
			}
		});
		LinearLayout ll_register = (LinearLayout) view
				.findViewById(R.id.ll_register);
		ll_register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent= new Intent(context,MainRegisterAty.class);
				context.startActivity(intent);
				//不结束activity 注册/登录结束还要回来。
			}
		});
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (!mEnabled) {
			return false;
		}
		return super.onInterceptTouchEvent(ev);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = (int) event.getY();
			Log.d(TAG, "downY = " + downY);
			if (isShow) {
				return true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			moveY = (int) event.getY();
			scrollY = moveY - downY;
			if (scrollY > 0) {
				if (isShow) {
					scrollTo(0, -Math.abs(scrollY));
				}
			} else {
				if (mScreenHeigh - this.getTop() <= viewHeight && !isShow) {
					scrollTo(0, Math.abs(viewHeight - scrollY));
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			upY = (int) event.getY();
			if (isShow) {
				if (this.getScrollY() <= -(viewHeight / 2)) {
					startMoveAnim(this.getScrollY(),
							-(viewHeight - this.getScrollY()), mDuration);
					isShow = false;
					Log.d("isShow", "false");
				} else {
					startMoveAnim(this.getScrollY(), -this.getScrollY(),
							mDuration);
					isShow = true;
					Log.d("isShow", "true");
				}
			}
			Log.d("this.getScrollY()", "" + this.getScrollY());
			changed();
			break;
		case MotionEvent.ACTION_OUTSIDE:
			Log.d(TAG, "ACTION_OUTSIDE");
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	public void startMoveAnim(int startY, int dy, int duration) {
		isMoving = true;
		mScroller.startScroll(0, startY, 0, dy, duration);
		invalidate();// ֪ͨUI�̵߳ĸ���
	}

	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			postInvalidate();
			isMoving = true;
		} else {
			isMoving = false;
		}
		super.computeScroll();
	}

	public void show() {
		if (!isShow && !isMoving) {
			LoginDialogView.this.startMoveAnim(-viewHeight, viewHeight,
					mDuration);
			isShow = true;
			Log.d("isShow", "true");
			changed();
		}
	}

	public void dismiss() {
		if (isShow && !isMoving) {
			LoginDialogView.this.startMoveAnim(0, -viewHeight, mDuration);
			isShow = false;
			Log.d("isShow", "false");
			changed();
		}
	}

	public boolean isShow() {
		return isShow;
	}

	public boolean isSlidingEnabled() {
		return mEnabled;
	}

	public void setSlidingEnabled(boolean enabled) {
		mEnabled = enabled;
	}

	public void setOnStatusListener(onStatusListener listener) {
		this.statusListener = listener;
	}

	public void setOutsideTouchable(boolean touchable) {
		mOutsideTouchable = touchable;
	}

	public void changed() {
		if (statusListener != null) {
			if (isShow) {
				statusListener.onShow();
			} else {
				statusListener.onDismiss();
			}
		}
	}

	public onStatusListener statusListener;

	public interface onStatusListener {
		public void onShow();

		public void onDismiss();
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		super.onLayout(changed, l, t, r, b);
	}
}
