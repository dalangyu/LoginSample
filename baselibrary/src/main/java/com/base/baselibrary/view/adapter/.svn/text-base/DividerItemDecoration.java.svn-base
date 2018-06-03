package com.base.baselibrary.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by 15600 on 2017/5/17.
 */

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    public static final int HORIZONTAL = LinearLayout.HORIZONTAL;
    public static final int VERTICAL = LinearLayout.VERTICAL;


    public static final int VERTICAL_HORIZONTAL = 2;

    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};

    private Drawable mDivider;

    private Drawable vSpanDivider;

    /**
     * Current orientation. Either {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    private int mOrientation;

    private final Rect mBounds = new Rect();

    public void setHeaderDiverder(boolean headerDivider) {
        isHeaderDivider = headerDivider;
    }

    private boolean isHeaderDivider = false;

    public void setHeaderVSpanDivider(boolean headerVSpanDivider) {
        isHeaderVSpanDivider = headerVSpanDivider;
    }

    private boolean isHeaderVSpanDivider = false;


    /**
     * Creates a divider {@link RecyclerView.ItemDecoration} that can be used with a
     * {@link LinearLayoutManager}.
     *
     * @param context     Current mContext, it will be used to access resources.
     * @param orientation Divider orientation. Should be {@link #HORIZONTAL} or {@link #VERTICAL}.
     */
    public DividerItemDecoration(Context context, int orientation) {
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
        setOrientation(orientation);
    }

    /**
     * Sets the orientation for this divider. This should be called if
     * {@link RecyclerView.LayoutManager} changes orientation.
     *
     * @param orientation {@link #HORIZONTAL} or {@link #VERTICAL}
     */
    public void setOrientation(int orientation) {
        if (orientation != HORIZONTAL && orientation != VERTICAL && orientation !=
                VERTICAL_HORIZONTAL) {
            throw new IllegalArgumentException(
                    "Invalid orientation. It should be either HORIZONTAL or VERTICAL");
        }
        mOrientation = orientation;
    }

    /**
     * Sets the {@link Drawable} for this divider.
     *
     * @param drawable Drawable that should be used as a divider.
     */
    public void setDrawable(@NonNull Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable cannot be null.");
        }
        mDivider = drawable;
    }

    public void setvSpanDivider(Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable cannot be null.");
        }
        this.vSpanDivider = drawable;
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        if (parent.getLayoutManager() == null) {
            return;
        }
        if (mOrientation == VERTICAL) {
            drawVertical(c, parent);
        } else if (mOrientation == HORIZONTAL) {
            drawHorizontal(c, parent);
        } else {
            drawHV(c, parent);
        }
    }

    @SuppressLint("NewApi")
    private void drawVertical(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int left;
        final int right;
        if (parent.getClipToPadding()) {
            left = parent.getPaddingLeft();
            right = parent.getWidth() - parent.getPaddingRight();
            canvas.clipRect(left, parent.getPaddingTop(), right,
                    parent.getHeight() - parent.getPaddingBottom());
        } else {
            left = 0;
            right = parent.getWidth();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getDecoratedBoundsWithMargins(child, mBounds);
            int bottom, top;
            if (parent.getChildAdapterPosition(child) == 0 && isHeaderDivider) {
                top = mBounds.top + Math.round(ViewCompat.getTranslationY(child));
                bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }
            if (isHeaderVSpanDivider && parent.getChildAdapterPosition(child) == 0) {
                top = mBounds.top + Math.round(ViewCompat.getTranslationY(child));
                bottom = top + vSpanDivider.getIntrinsicHeight();
                vSpanDivider.setBounds(left, top, right, bottom);
                vSpanDivider.draw(canvas);
            }

            if ((parent.getChildAdapterPosition(child) + 1) % 6 == 0 && vSpanDivider != null) {
                bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
                top = bottom - vSpanDivider.getIntrinsicHeight();
                vSpanDivider.setBounds(left, top, right, bottom);
                vSpanDivider.draw(canvas);
            } else {
                bottom = mBounds.bottom + Math.round(ViewCompat.getTranslationY(child));
                top = bottom - mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(canvas);
            }


        }
        canvas.restore();
    }

    @SuppressLint("NewApi")
    private void drawHorizontal(Canvas canvas, RecyclerView parent) {
        canvas.save();
        final int top;
        final int bottom;
        if (parent.getClipToPadding()) {
            top = parent.getPaddingTop();
            bottom = parent.getHeight() - parent.getPaddingBottom();
            canvas.clipRect(parent.getPaddingLeft(), top,
                    parent.getWidth() - parent.getPaddingRight(), bottom);
        } else {
            top = 0;
            bottom = parent.getHeight();
        }

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            parent.getLayoutManager().getDecoratedBoundsWithMargins(child, mBounds);
            final int right = mBounds.right + Math.round(ViewCompat.getTranslationX(child));
            final int left = right - mDivider.getIntrinsicWidth();
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(canvas);
        }
        canvas.restore();
    }

    @SuppressLint("NewApi")
    private void drawHV(Canvas canvas, RecyclerView parent) {
        drawVertical(canvas, parent);
        drawHorizontal(canvas, parent);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        if (mOrientation == VERTICAL) {
            if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1) {
//                int top=mDivider.getIntrinsicHeight();
//                outRect.set(0, top, 0, 0);
            } else {
                int top = 0;
                if (parent.getChildAdapterPosition(view) == 0 && isHeaderDivider) {
                    top = mDivider.getIntrinsicHeight();
                }
                outRect.set(0, top, 0, mDivider.getIntrinsicHeight());
            }

        } else if (mOrientation == HORIZONTAL) {
            outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
        } else {
            int height = 0, top = 0;
            int divisionCount = 3;
            int count;
            int rowCount = 1;
            int indexNum = (parent.getChildAdapterPosition(view) + 1);
            int itemCount;
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1)
            {//这样的意义是CommonAdapter默认最后有一个（状态view statusResId）所以需要避免
                height = mDivider.getIntrinsicHeight();
            }
            if (parent.getLayoutManager() instanceof GridLayoutManager) {
                rowCount = ((GridLayoutManager) parent.getLayoutManager()).getSpanCount();
                itemCount = rowCount * divisionCount;
            } else {
                itemCount = divisionCount;
            }
            if (isHeaderVSpanDivider && vSpanDivider != null && parent.getChildAdapterPosition
                    (view) < rowCount) {//应用于头部的vSpanDivider
                top = vSpanDivider.getIntrinsicHeight();
            }
            if (vSpanDivider != null) {
                count = indexNum % itemCount;
                if ((itemCount - count) <= rowCount - 1 || count == 0) {
                    height += vSpanDivider.getIntrinsicHeight();
                }
            }
            outRect.set(0, top, mDivider.getIntrinsicWidth(), height);
        }
    }

}
