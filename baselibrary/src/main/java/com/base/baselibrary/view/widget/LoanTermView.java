package com.base.baselibrary.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.base.baselibrary.R;
import com.base.baselibrary.util.StringUtils;

import java.util.List;

import static com.base.baselibrary.util.ScreenUtils.dip2px;


public class LoanTermView extends ViewGroup {

    private int lineCount = 1;

    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;


    public static final int SELECT_RADIO = 1;

    public static final int SELECT_CHECKBOX = 2;

    public static final int SELECT_NONE = 0;

    private int mSelectType = 0;

    private int mOrientation = 0;

    private int itemPaddingTop = 0;

    private int itemPaddingBottom = 0;

    private int itemPaddingLeft = 0;

    private int itemPaddingRight = 0;

    private int itemTextSize = 0;

    private int itemTextColor = 0;

    private Drawable mBackground;

    private Drawable mSelectBg;

    // horizontal space among children views
    int hSpace = dip2px(12);
    // vertical space among children views
    int vSpace = dip2px(12);

    private List<Item> mItems;

    private OnItemClickListener listener;

    public interface OnItemClickListener {

        void itemClick(View v, Item item);
    }

    public static class Item implements Parcelable {

        private String label;

        private String tag;

        private boolean isSelect = false;

        public Item(String label, String tag) {
            this.label = label;
            this.tag = tag;
        }

        public Item(String label) {
            this.label = label;
        }

        protected Item(Parcel in) {
            label = in.readString();
            tag = in.readString();
            isSelect = in.readByte() != 0;
        }

        public static final Creator<Item> CREATOR = new Creator<Item>() {
            @Override
            public Item createFromParcel(Parcel in) {
                return new Item(in);
            }

            @Override
            public Item[] newArray(int size) {
                return new Item[size];
            }
        };

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public boolean isSelect() {
            return isSelect;
        }

        public void setSelect(boolean select) {
            isSelect = select;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(label);
            parcel.writeString(tag);
            parcel.writeByte((byte) (isSelect ? 1 : 0));
        }
    }


    public LoanTermView(Context context) {
        this(context, null);
    }

    public LoanTermView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoanTermView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.LoanTermView, defStyleAttr, 0);
        mOrientation = a.getInt(R.styleable.LoanTermView_orientation, 0);
        mSelectType = a.getInt(R.styleable.LoanTermView_selectType, 0);
        mSelectBg = a.getDrawable(R.styleable.LoanTermView_itemSelectBackground);
        mBackground = a.getDrawable(R.styleable.LoanTermView_itemDefBackground);
        itemPaddingTop = a.getDimensionPixelSize(R.styleable.LoanTermView_itemPaddingTop, -1);
        itemPaddingBottom = a.getDimensionPixelSize(R.styleable.LoanTermView_itemPaddingBottom, -1);
        itemPaddingLeft = a.getDimensionPixelSize(R.styleable.LoanTermView_itemPaddingLeft, -1);
        itemPaddingRight = a.getDimensionPixelSize(R.styleable.LoanTermView_itemPaddingRight, -1);
        itemTextSize = a.getDimensionPixelSize(R.styleable.LoanTermView_itemTextSize, -1);
        itemTextColor = a.getDimensionPixelSize(R.styleable.LoanTermView_itemTextColor, -1);
        a.recycle();
    }

    public static class LayoutParams extends MarginLayoutParams {

        public int left = 0;
        public int top = 0;

        public LayoutParams(Context arg0, AttributeSet arg1) {
            super(arg0, arg1);
        }

        public LayoutParams(int arg0, int arg1) {
            super(arg0, arg1);
        }

        public LayoutParams(ViewGroup.LayoutParams arg0) {
            super(arg0);
        }

    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(
            AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }


    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(
            ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p instanceof LayoutParams;
    }


    public void setData(final List<Item> items) {
        if(mItems!=null)
        mItems.clear();

        mItems = items;

        if (mItems == null || mItems.isEmpty())
            return;

        int paddingTop = 0;
        int paddingBottom = 0;
        int paddingLeft = dip2px(6);
        int paddingRight = dip2px(6);
        if (itemPaddingTop >= 0) {
            paddingTop = itemPaddingTop;
        }
        if (itemPaddingBottom >= 0) {
            paddingBottom = itemPaddingBottom;
        }
        if (itemPaddingLeft >= 0) {
            paddingLeft = itemPaddingLeft;
        }
        if (itemPaddingRight >= 0) {
            paddingRight = itemPaddingRight;
        }

        this.removeAllViews();

        for (int i = 0; i < items.size(); i++) {
            if(StringUtils.isEmpty(items.get(i).getLabel())){
                Log.e("标签","跳出执行的坐标:"+i);
                continue;
            }
            Log.e("标签","标签坐标:"+i);
            AppCompatTextView txt = new AppCompatTextView(getContext(), null);
            final Item item = items.get(i);

//            txt.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    settingView(item);
//                    if (listener != null) listener.itemClick(v, item);
//                }
//            });

            setBackGround(txt, mBackground);
            txt.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
            txt.setText(item.getLabel());
            if (itemTextSize >= 0) {
                txt.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemTextSize);
            } else {
                txt.setTextSize(14);
            }

            txt.setTextColor(Color.parseColor("#F5A623"));

//            if (itemTextColor == 0)
//
//            else
//                txt.setTextColor(itemTextColor);
            addView(txt);
        }
        requestLayout();
    }

    private void settingView(Item item) {
        for (int i = 0; i < mItems.size(); i++) {
            View view = getChildAt(i);
            if (mItems.get(i) == item) {
                if (item.isSelect) {
                    setBackGround(view, mBackground);
                    item.isSelect = false;
                } else {
                    setBackGround(view, mSelectBg);
                    item.isSelect = true;
                }
            } else {
                if (mSelectType == SELECT_RADIO) {
                    setBackGround(view, mBackground);
                    mItems.get(i).isSelect = false;
                }
            }

        }
    }

    public void setOnItemClick(OnItemClickListener listener) {
        this.listener = listener;
    }

    private void setBackGround(View v, Drawable bg) {
        if (bg != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                v.setBackground(bg);
            } else {
                v.setBackgroundDrawable(bg);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int rw = MeasureSpec.getSize(widthMeasureSpec);
        int actualRw = rw - getPaddingLeft() - getPaddingRight();
        int childWidth = getPaddingLeft(), rowCount = 0, oldChildWidth;
        int childCount = this.getChildCount();

        //循环子控件数量
        for (int i = 0; i < childCount; i++) {

            View child = this.getChildAt(i);
            this.measureChild(child, widthMeasureSpec, heightMeasureSpec);
            oldChildWidth = childWidth;
            childWidth += child.getMeasuredWidth() + hSpace;
            if (mOrientation == VERTICAL) {
                if (childWidth > actualRw) {
                    childWidth = getPaddingLeft() + child.getMeasuredWidth() + hSpace;
                    oldChildWidth = getPaddingLeft();
                    rowCount++;
                }
            }
            LayoutParams lParams = (LayoutParams) child.getLayoutParams();
            lParams.left = oldChildWidth;
            lParams.top = rowCount * (child.getMeasuredHeight() + vSpace) + getPaddingTop();
            child.setLayoutParams(lParams);
        }
        int rh = getPaddingTop() + getPaddingBottom();
        if (mOrientation == HORIZONTAL) {
            vSpace = 0;
            if (childCount > 0) {
                int width=0;
                for (int i = 0; i < childCount; i++) {
                    width=width+getChildAt(i).getMeasuredWidth() + hSpace;
                }
                    rw =width +  getPaddingLeft() +   getPaddingRight();
            } else {
                rw = childCount * hSpace + getPaddingLeft() +
                        getPaddingRight();
            }

        }
        if (childCount > 0) {
            rh += (rowCount + 1) * (getChildAt(0).getMeasuredHeight() + vSpace);
        }
        setMeasuredDimension(rw, rh);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = this.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = this.getChildAt(i);
            LayoutParams lParams = (LayoutParams) child.getLayoutParams();
            child.layout(lParams.left, lParams.top, lParams.left + child.getMeasuredWidth(),
                    lParams.top + child.getMeasuredHeight());
        }
    }
}
