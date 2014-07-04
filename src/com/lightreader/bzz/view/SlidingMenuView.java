package com.lightreader.bzz.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ldm on 13-11-11.
 * 仿网易新闻的滑动菜单，body中可以支持ViewPager，但需要调用setViewPager方法将ViewPager传进来
 */
public class SlidingMenuView extends ViewGroup {
    private static final int TOUCH_STATE_REST = 0;     //空闲状态
    private static final int TOUCH_STATE_SCROLLING = 1;//滚动状态
    private int touchState = TOUCH_STATE_REST;         //当前TOUCH状态

    private static final int AUTO_MOVE_ANIM_SPEED = 9;  //数值越大 速度越快

    private final int MENU_LEFT_OPEN = -1;//左侧菜单开启
    private final int MENU_NORMAL_OPEN = 0;//菜单常规
    private int menuNowState = MENU_NORMAL_OPEN;//菜单当前状态

    private int LEFT_MENU_WIDTH;
    private float LEFT_PADDING;

    private float startX;//Touch时用来记录坐标

    private ViewPager viewPager;
    private View leftMenu;//左侧菜单句柄
    private View body;//内容体
    
    public SlidingMenuView(Context context) {
        super(context);
    }

    public SlidingMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * onTouchEvent之前进行拦截
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = ev.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isDirection(startX, ev.getX())) {//用户手指向右拉动---->
                        startX = ev.getX();
                        return viewPager.getCurrentItem()==0?true:false;
                    }else{//用户手指向左拉动<----
                    	startX = ev.getX();
                        return menuNowState == MENU_LEFT_OPEN ;
                    }
            }
            return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event != null) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    startX = event.getX();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if ((Math.abs(event.getX() - startX) > 5)) {

                        touchState = TOUCH_STATE_SCROLLING;
                        LEFT_PADDING += event.getX() - startX;
                        if(isDirection(startX, event.getX())){
                           LEFT_PADDING = (LEFT_PADDING > LEFT_MENU_WIDTH) ? LEFT_MENU_WIDTH : LEFT_PADDING;//向右拉动不可超出LEFT_MENU_WIDTH
                        }else{
                           LEFT_PADDING = (LEFT_PADDING < 0) ? 0 : LEFT_PADDING;
                        }

                        //隐藏不显示的菜单
                        if (leftMenu != null) {
                            if (LEFT_PADDING > 0) {
                                leftMenu.setVisibility(VISIBLE);
                            } else if (LEFT_PADDING < 0) {
                                leftMenu.setVisibility(GONE);
                            }
                        }
                        startX = event.getX();//记录坐标
                        requestLayout();//可理解为重新显示布局，刷新view
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    if (menuNowState == MENU_NORMAL_OPEN) {
                            if (LEFT_PADDING > LEFT_MENU_WIDTH * 0.33) {//打开左侧菜单
                                menuNowState = MENU_LEFT_OPEN;
                            }else {//还原回正常状态
                                menuNowState = MENU_NORMAL_OPEN;
                            }
                        } else if (menuNowState == MENU_LEFT_OPEN) {
                            if (LEFT_PADDING < LEFT_MENU_WIDTH * 0.66) {//达到要求 关闭左侧菜单
                                menuNowState = MENU_NORMAL_OPEN;
                            } else {//恢复左侧菜单开启状态
                                menuNowState = MENU_LEFT_OPEN;
                            }
                    } 
                    touchState = TOUCH_STATE_REST;//设置空闲状态

                    invalidate();//ui线程中刷新view，postinvalidate()在非ui线程刷新view
                    break;
                default:
                    break;
            }
        }
        return true;
    }


    /**
     * 检测给定值的移动方向
     *
     * @param startX 起点X坐标
     * @param endX   终点X坐标
     * @return 返回true表示手指向右滑动，false相反
     */
    private boolean isDirection(float startX, float endX) {
        return endX - startX > 0;
    }


    @Override
    public void computeScroll() {
        if (touchState == TOUCH_STATE_REST) {
            if (menuNowState == MENU_LEFT_OPEN) {
                /**
                 * 左侧菜单仅能在正常状态下开启
                 */
                if (LEFT_PADDING < LEFT_MENU_WIDTH) {
                    LEFT_PADDING = LEFT_PADDING + AUTO_MOVE_ANIM_SPEED > LEFT_MENU_WIDTH ? LEFT_MENU_WIDTH : LEFT_PADDING + AUTO_MOVE_ANIM_SPEED;
                    postInvalidate();
                    requestLayout();
                }
            } else if (menuNowState == MENU_NORMAL_OPEN) {
                /**
                 * 正常状态的恢复有2中可能：
                 * 1、LEFT_PADDING > 0 表示左侧菜单关闭时执行的恢复状态
                 * 2、LEFT_PADDING < 0 表示右侧菜单关闭时执行的恢复状态
                 * 此处停止的条件是LEFT_PADDING == 0（LEFT_PADDING等于0时，表示状态恢复完成）
                 */
                if (LEFT_PADDING > 0) {
                    LEFT_PADDING = LEFT_PADDING - AUTO_MOVE_ANIM_SPEED > 0 ? LEFT_PADDING - AUTO_MOVE_ANIM_SPEED : 0;
                    postInvalidate();
                    requestLayout();
                } else if (LEFT_PADDING < 0) {
                    LEFT_PADDING = LEFT_PADDING + AUTO_MOVE_ANIM_SPEED < 0 ? LEFT_PADDING + AUTO_MOVE_ANIM_SPEED : 0;
                    postInvalidate();
                    requestLayout();
                }
            } 
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        leftMenu = getChildAt(0);
        if (leftMenu != null) {
            if (LEFT_MENU_WIDTH <= 0) {
                    LEFT_MENU_WIDTH = leftMenu.getMeasuredWidth();
                }
              leftMenu.measure(LEFT_MENU_WIDTH, heightMeasureSpec);
        }
        
        body = getChildAt(2);
        if (body != null) {
            body.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //左菜单
        if (leftMenu != null && leftMenu.getLayoutParams() != null) {
            leftMenu.layout(0, 0, LEFT_MENU_WIDTH, leftMenu.getMeasuredHeight());
        }

        //内容体
        if (body != null) {
            body.layout((int) (0 + LEFT_PADDING), 0, (int) (body.getMeasuredWidth() + LEFT_PADDING), body.getMeasuredHeight());
        }
    }


    /**
     * 开启左侧菜单
     */
    public void openLeftMenu() {
        if (menuNowState == MENU_NORMAL_OPEN) {
            menuNowState = MENU_LEFT_OPEN;
            computeScroll();
        }
    }

    /**
     * 关闭左侧菜单
     */
    public void closeLeftMenu() {
        if (menuNowState == MENU_LEFT_OPEN) {
            menuNowState = MENU_NORMAL_OPEN;
            computeScroll();
        }
    }
    
    public void setViewPager(ViewPager viewPager) {
        this.viewPager = viewPager;
    }
}
