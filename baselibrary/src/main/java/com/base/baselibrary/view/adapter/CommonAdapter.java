/*
 * @Title CommonAdapter.java
 * @Copyright Copyright 2010-2015 Yann Software Co,.Ltd All Rights Reserved.
 * @Description��
 * @author Yann
 * @date 2015-8-5 ����10:39:05
 * @version 1.0
 */
package com.base.baselibrary.view.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.baselibrary.R;
import com.base.baselibrary.model.entity.BaseView;

import java.util.List;

/**
 * Created by langlang on 2016/10/24.
 */

public abstract class CommonAdapter<T extends BaseView> extends RecyclerView.Adapter<ViewHolder> {

    protected Context mContext;

    protected List<T> mData;

    private int resId;

    protected Action mLoadMoreAction;

    private boolean hasHeader = false;
    private boolean hasFooter = false;

    public int headerResAndTypeId = HEADER_TYPE_VIEW;
    public int footerResAndTypeId = FOOTER_TYPE_VIEW;

    private final static int ERROR = -1;

    private final static int FOOTER_TYPE_VIEW = -2;

    private final static int HEADER_TYPE_VIEW = -3;

    private LoaderViewListener headerLoaderView;

    private LoaderViewListener footerLoaderView;

    /**
     * 默认的item构建器
     */
    private LoaderViewListener defLoaderView;

    private final static int DEF_TYPE_VIEW = -4;

    private boolean isFooterBind = false;

    private boolean isHeaderBind = false;


    private int statusResId = R.layout.view_status_last;
    public boolean isRefreshing = false; //刷新
    public boolean isLoadingMore = false; //正在加载
    public boolean isShowNoMore = false;//停止加载
    public boolean isLoadEnd = false;
    public boolean loadMoreAble = false;
    protected int mViewCount = 0;
    protected View mStatusView;
    protected LinearLayout mLoadMoreView;
    public TextView mNoMoreView;


    private ItemOnClickListener<T> onItemClick;

    private ItemOnLongClickListener<T> onItemLongClick;

    public void setOnItemClick(ItemOnClickListener<T> onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setOnItemLongClick(ItemOnLongClickListener<T> onItemLongClick) {
        this.onItemLongClick = onItemLongClick;
    }


    public CommonAdapter(Context context, List<T> data) {
        this.mContext = context;
        resId = getDefLayoutId();
        defLoaderView = getDefView();
        if (resId != ERROR && defLoaderView != null) {
            throw new RuntimeException("getDefLayoutId() and getDefView() can only override one");
        }
        if (defLoaderView != null) resId = DEF_TYPE_VIEW;
        if (data != null) {
            mData = data;
            mViewCount += data.size();
            mViewCount++;//给view_status_last加的
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int id;
        //RecyclerView.Adapter getItemViewType(position)方法默认返回0\
        //证明不是Header、Footer、Last，也没有重写getItemType方法返回的自定
        //义layout文件ID,所以获取默认布局ID(getDefLayoutId返回的值)
        if (viewType == ERROR) {
            if (resId == ERROR) {
                throw new RuntimeException(
                        "没有任何布局请重写getDefLayoutId，" +
                                "或者重写getItemType方法返回List对象的index " +
                                "CommonAdapter会根据单个BaseBean的配置来初始化视图!");
            }
            id = resId;
        } else id = viewType;
        boolean isBinding = false;
        View view = null;
        if (id == headerResAndTypeId) {
            if (headerLoaderView != null) {
                view = headerLoaderView.builderView();
            } else if (isHeaderBind) {
                isBinding = true;
            }
        } else if (id == footerResAndTypeId) {
            if (footerLoaderView != null) {
                view = footerLoaderView.builderView();
            } else if (isFooterBind) {
                isBinding = true;
            }
        } else if (id == statusResId) {

        } else if (id == resId) {
            if (defLoaderView != null) {
                view = defLoaderView.builderView();
            } else {
                if (getDefVariableId() != BaseView.BR_ID_DEFAULT) isBinding = true;
            }

        } else {
            if (mData.get(id).getLoaderView() != null) {
                view = mData.get(id).getLoaderView().invoke();
            } else {
                if (mData.get(id).getBrId() != BaseView.BR_ID_DEFAULT) {//证明是用了binding的layout
                    id = mData.get(id).getResId();
                    isBinding = true;
                }
            }
        }
        if (isBinding) {
            ViewDataBinding binding = DataBindingUtil.inflate(LayoutInflater
                    .from(viewGroup.getContext()), id, viewGroup, false);
            return new BindingViewHolder(binding, id);//不是（viewType），最终都以resId为主
        }
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(id, viewGroup, false);
            if (id == statusResId) {
                mStatusView = view;
                mLoadMoreView = (LinearLayout) view.findViewById(R.id.load_more_view);
                mNoMoreView = (TextView) view.findViewById(R.id.no_more_view);
            }
        }
        return new DefViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        int index = getDataPostion(position);
        if (index >= 0 && mData.size() > index) {
            T data = mData.get(index);
            if (data.getLoaderView() == null) {
                if (viewHolder instanceof BindingViewHolder) {
                    BindingViewHolder holder = (BindingViewHolder) viewHolder;
                    holder.getBinding().setVariable(data.getBrId() != BaseView.BR_ID_DEFAULT
                            ? data.getBrId() : getDefVariableId(), data);
                    holder.getBinding().executePendingBindings();
                }
            }
            beforeConvert(viewHolder, data, position);
        } else {
            beforeConvert(viewHolder, null, position);
        }

        //加载到最后一个Item，显示加载更多
        if (position == mViewCount - 1 && !isShowNoMore && !isRefreshing && loadMoreAble &&
                getItemCount() > 10) {
            if (hasHeader && !hasFooter && position != 0) { //有header，没有footer
                mLoadMoreView.setVisibility(View.VISIBLE);
            } else if (hasFooter && !hasHeader && position != 0) { //有footer，没有header
                mLoadMoreView.setVisibility(View.VISIBLE);
            } else if (!hasHeader && !hasFooter) { //mViewCount - 2 == -1不用处理
                mLoadMoreView.setVisibility(View.VISIBLE);
            } else if (hasHeader && hasFooter && position != 1) { //都有
                mLoadMoreView.setVisibility(View.VISIBLE);
            }
            isLoadEnd = true;
            isLoadingMore = true;
            if (mLoadMoreAction != null) {
                mLoadMoreAction.onAction();
            }
        }


    }

    public void setData(List<T> data) {
        if (data != null) {
            isShowNoMore = false;
            computeCount();
            this.mData = data;
            mViewCount += data.size();
            super.notifyDataSetChanged();
        } else {
            throw new NullPointerException("com.base.baselibrary.ui.adapter data is not null");
        }
    }

    public void addData(List<T> data) {
        if (data == null)
            throw new NullPointerException("com.base.baselibrary.ui.adapter data is not null");
        if (!isShowNoMore) {
            isLoadingMore = false;
            if (mData != null) {
                mData.addAll(data);
                mViewCount += data.size();
                super.notifyDataSetChanged();
            }
        }
    }

    public void update() {
        if (mData != null) {
            isLoadingMore = false;
            computeCount();
            mViewCount += mData.size();
            super.notifyDataSetChanged();
        } else throw new NullPointerException("com.base.baselibrary.ui.adapter data is not null");
    }

    public void computeCount() {
        mViewCount = 1;
        if (hasHeader) {
            mViewCount++;
        }
        if (hasFooter) {
            mViewCount++;
        }
    }

    public void errorStop() {
        mViewCount = 0;
    }

    public void setLoadMoreAction(Action action) {
        mLoadMoreAction = action;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public int getItemCount() {
        return mViewCount;
    }

    private int getDataPostion(int position) {
        int i = ERROR;
        if (!hasHeader && !hasFooter && mData != null && position < mData.size()) {
            //没有Header和Footer
            i = position;
        } else if (hasHeader && !hasFooter && position > 0 && position < mViewCount - 1) {
            //有Header没有Footer
            i = position - 1;
        } else if (!hasHeader && position < mViewCount - 2) { //没有Header，有Footer
            i = position;
        } else { //都有
            if (position > 0 && position < mViewCount - 2) {
                i = position - 1;
            }
        }
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader && position == 0) {  //header
            return headerResAndTypeId;
        } else if (hasFooter && position == mViewCount - 2) { //footer
            return footerResAndTypeId;
        } else if (position == mViewCount - 1) { //添加最后的状态view
            return statusResId;
        }
        int dataPosition = getDataPostion(position);
        if (dataPosition != ERROR) {
            int itemType = getItemType(dataPosition);
            if (itemType != ERROR) {
                return itemType;
            }
        }
        return ERROR;
    }

    public void showNoMore(final boolean isVisible) {
        isShowNoMore = true;
        mLoadMoreView.post(new Runnable() {
            @Override
            public void run() {
                mLoadMoreView.setVisibility(View.GONE);
                if (isVisible)
                    mNoMoreView.setVisibility(View.VISIBLE);
            }
        });

    }

    public void showMore() {
        isShowNoMore = false;
        if (mLoadMoreView != null) {
            mLoadMoreView.post(new Runnable() {
                @Override
                public void run() {
//                    mLoadMoreView.setVisibility(View.VISIBLE);
                    if (mNoMoreView != null) mNoMoreView.setVisibility(View.GONE);
                }
            });
        }
    }


    public void setHeader(@LayoutRes int res, boolean isHeaderBind) {
        hasHeader = true;
        this.isHeaderBind = isHeaderBind;
        headerResAndTypeId = res;
        headerLoaderView = null;
        mViewCount++;
    }

    public void setFooter(@LayoutRes int res, boolean isFooterBind) {
        hasFooter = true;
        this.isFooterBind = isFooterBind;
        footerResAndTypeId = res;
        footerLoaderView = null;
        mViewCount++;
    }

    public void setHeader(LoaderViewListener listener) {
        hasHeader = true;
        this.isHeaderBind = false;
        headerResAndTypeId = HEADER_TYPE_VIEW;
        headerLoaderView = listener;
        mViewCount++;
    }

    public void setFooter(LoaderViewListener listener) {
        hasFooter = true;
        this.isFooterBind = false;
        footerResAndTypeId = FOOTER_TYPE_VIEW;
        footerLoaderView = listener;
        mViewCount++;
    }

    public void removeHeader() {
        if (hasHeader) {
            hasHeader = false;
        }
    }

    public void removeFooter() {
        if (hasFooter) {
            hasFooter = false;
        }
    }

    public void showLoad() {
        if (mLoadMoreView != null && !isLoadingMore) {
            mLoadMoreView.setVisibility(View.VISIBLE);
            mNoMoreView.setVisibility(View.GONE);
            isLoadingMore = true;
            isShowNoMore = false;
        }

    }

    public void hideLoad() {
        if (mLoadMoreView != null && isLoadingMore) {
            mLoadMoreView.setVisibility(View.GONE);
            isLoadingMore = false;
            isShowNoMore = false;
        }

    }

    public void clear() {
        if (mData != null) {
            if (mData.size() > 0)
                mData.clear();
        }
        mViewCount = 0;
        isRefreshing = false;
        isShowNoMore = false;
        isLoadingMore = false;
        if (mLoadMoreView != null)
            mLoadMoreView.setVisibility(View.GONE);
        if (mNoMoreView != null)
            mNoMoreView.setVisibility(View.GONE);
    }


    /**
     * heander、footer、默认的item的自定义构建器
     */
    public interface LoaderViewListener {
        View builderView();
    }

    /**
     * heander、footer、默认的item的自定义构建器
     */
    public interface ItemOnClickListener<T> {

        void onItemClick(ViewHolder holder, T t, int position);
    }

    public interface ItemOnLongClickListener<T> {

        boolean onItemClick(ViewHolder holder, T t, int position);
    }


    public void setLoadMoreAble(boolean loadMoreAble) {
        this.loadMoreAble = loadMoreAble;
    }

    private void beforeConvert(final ViewHolder holder, final T t, final int position) {
        if (onItemClick != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.getItemViewType() != R.layout.view_status_last) {
                        onItemClick.onItemClick(holder, t, position);
                    }

                }
            });
        }
        if (onItemLongClick != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return onItemLongClick.onItemClick(holder, t, position);
                }
            });
        }
        convert(holder, t, position);
    }

    public List<T> getData() {
        return mData;
    }

    protected abstract void convert(ViewHolder holder, T t, int position);

    protected int getItemType(int postion) {
        return ERROR;
    }

    protected int getDefVariableId() {
        return BaseView.BR_ID_DEFAULT;
    }

    protected @LayoutRes
    int getDefLayoutId() {
        return ERROR;
    }

    protected LoaderViewListener getDefView() {
        return null;
    }

}
