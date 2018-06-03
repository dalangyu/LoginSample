package com.base.baselibrary.view.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import java.util.ArrayList;
import java.util.List;

/**
 * 横向翻页效果
 *
 * @author langlang
 */
public class MyScrollView extends HorizontalScrollView {

    private int subChildCount = 0;//共有几页

    private ViewGroup firstChild = null;//根控件

    private int downX = 0;//左右移动的距离

    private int currentPage = 0;//当前移动所在的页面数
    private MoveListener moveListener;
    private TipListener tipListener;
    private List<Integer> pointList = new ArrayList<>();

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyScrollView(Context context) {
        super(context);
        init();
    }

    public boolean isEnd(){
        if(currentPage==pointList.size()-1){
            return true;
        }
        return false;
    }

    public void setTipListener(TipListener tipListener) {
        this.tipListener = tipListener;
    }

    public void init() {
        setHorizontalScrollBarEnabled(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        receiveChildInfo();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public int getCurrentPage() {
        return currentPage;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (subChildCount <= 1) return false;
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (moveListener != null) {
                    float num = ev.getX() - downX;
                    if (num > 0) {
                        move(Math.abs(num) / getWidth(), 1, false);
                    } else {
                        move(Math.abs(num) / getWidth(), 2, false);
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (Math.abs(ev.getX() - downX) > getWidth() / 4) {
                    if (ev.getX() - downX > 0) {
                        if (currentPage == 0) {
                            smoothScrollToCurrent();
                        } else {
                            smoothScrollToPrePage();
                        }

                    } else {
                        if (currentPage == subChildCount - 1) {
                            smoothScrollToCurrent();
                        } else {
                            smoothScrollToNextPage();
                        }

                    }

                } else {
                    smoothScrollToCurrent();
                }
                return true;
        }
        return super.onTouchEvent(ev);
    }

    public void receiveChildInfo() {
        pointList.clear();
        firstChild = (ViewGroup) getChildAt(0);
        if (firstChild != null) {
            subChildCount = firstChild.getChildCount();
            for (int i = 0; i < subChildCount; i++) {
                if (firstChild.getChildAt(i).getWidth() > 0)
                    pointList.add(firstChild.getChildAt(i).getLeft());
            }
        }
    }

    public void smoothScrollToCurrent() {
        if(pointList.size()>0){
            smoothScrollTo(pointList.get(currentPage), 0);
            move(0, 0, true);
        }

    }

    public void smoothScrollToNextPage() {
        if (currentPage < subChildCount - 1 && pointList.size()>0) {
            currentPage++;
            if (tipListener != null) {
                tipListener.toPage(currentPage);
            }
            smoothScrollTo(pointList.get(currentPage), 0);
            move(0, 2, true);
        }
    }

    public void smoothScrollToPrePage() {
        if (currentPage > 0 && pointList.size()>0) {
            currentPage--;
            if (tipListener != null) {
                tipListener.toPage(currentPage);
            }
            smoothScrollTo(pointList.get(currentPage), 0);
            move(0, 1, true);
        }
    }

    public void move(float centum, int moveType, boolean isMoveEnd) {
        if (moveListener != null) {
            moveListener.translation(currentPage, centum, moveType, isMoveEnd);
        }
    }

    public boolean gotoPage(int page) {
        if (page >= 0 && page < subChildCount - 1 && pointList.size()>0) {
            smoothScrollTo(pointList.get(page), 0);
            currentPage = page;
            move(0, 1, true);
            return true;
        }
        return false;
    }

    public void setMoveListener(MoveListener moveListener) {
        this.moveListener = moveListener;
    }

    /**
     * 移动接口
     *
     * @author langlang
     */
    public interface MoveListener {
        /**
         * 移动定位圆点
         *
         * @param currentPage 当前页数
         * @param centum      移动的百分比
         */
        void translation(int currentPage, float centum, int moveType, boolean isMoveEnd);
    }

    /**
     * tip接口
     *
     * @author langlang
     */
    public interface TipListener {
        void toPage(int index);
    }


}
