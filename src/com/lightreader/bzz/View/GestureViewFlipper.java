package com.lightreader.bzz.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ViewFlipper;

import com.lightreader.bzz.Activity.R;

/**
 * 首页大图轮播
 * 自定义的ViewFliper，监听滑动手势,以及自动指引
 * Created by ksafe on 14-5-24.
 */
public class GestureViewFlipper extends ViewFlipper implements GestureDetector.OnGestureListener {
    private FlipperFocusChangedListener flipperFocusChangedListener = null;
    private OnClickListener onClickListener = null;
    private GestureDetector gestureDetector = null;
    private Context context = null;

    public GestureViewFlipper(Context context) {
        super(context);
        this.context = context;
    }

    public GestureViewFlipper(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.context = context;

        gestureDetector = new GestureDetector(context, this);
        setLongClickable(true);
        setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

    @Override
    public void startFlipping() {
        super.startFlipping();
        setInAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_right_in));
        setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_left_out));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        stopFlipping();       //用户点击屏幕时，停止滑动
        setAutoStart(false);   //取消自动滑动
        return this.gestureDetector.onTouchEvent(event);   //把touch事件交给gesture处理
    }

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        setClickable(true);
        return true;    // 缺省值是false,在onTouchEvent后触发，如果为false，onFling将得不到down的事件即不触发
    }

    /**
     * 重写了onFling，为了判断手势，让手势滑动
     */
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        setClickable(false);
        if (e2.getX() - e1.getX() > 120) {    // 从左侧滑进
            setInAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_left_in));     //设置进出动画
            setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_right_out));
            //用户手势滑动结束，再次开始自动播放
            showPrevious();
            setAutoStart(true);
            startFlipping();

            return true;
        } else if (e2.getX() - e1.getX() < -120) {   //从右侧画出
            setInAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_right_in));  //设置进出动画
            setOutAnimation(AnimationUtils.loadAnimation(context, R.anim.animation_left_out));
            //滑动结束，再次自动播放
            showNext();
            setAutoStart(true);
            startFlipping();

            return true;
        }
        return false;
    }

    @Override
    public void showNext() {
        super.showNext();
        //监听向下向下翻页
        if (flipperFocusChangedListener != null)
            flipperFocusChangedListener.onFlipperChanged(getDisplayedChild());
    }

    @Override
    public void showPrevious() {
        super.showPrevious();
        //监听向上翻页
        if (flipperFocusChangedListener != null)
            flipperFocusChangedListener.onFlipperChanged(getDisplayedChild());
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if (isClickable()) {
            setClickable(false);

            if (onClickListener != null)
                onClickListener.onClick(this.getCurrentView());
        }
        return false;
    }

    public void setOnFocusChangedListener(FlipperFocusChangedListener flipperFocusChangedListener) {
        this.flipperFocusChangedListener = flipperFocusChangedListener;
    }

    public void setItemOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    /*
     * 回调接口，用于监听ViewFlipper切花事件
     */
    public interface FlipperFocusChangedListener {
        public void onFlipperChanged(int index);
    }
}
