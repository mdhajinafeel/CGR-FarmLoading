package com.codringreen.farmloading.view.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public abstract class CommonRecyclerViewAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    private static final int VIEW_TYPE_HEADER = 0;
    private static final int VIEW_TYPE_ITEM = 1;
    private final Context mContext;
    private List<T> mDataList;
    private final int mLayoutId;
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private int mHeaderLayoutId = -1; // Optional

    public CommonRecyclerViewAdapter(Context context, List<T> dataList, int layoutId) {
        mContext = context;
        mDataList = dataList;
        mLayoutId = layoutId;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutToUse = (viewType == VIEW_TYPE_HEADER) ? mHeaderLayoutId : mLayoutId;
        View view = LayoutInflater.from(mContext).inflate(layoutToUse, parent, false);
        return new ViewHolder(view);
    }

//    @Override
//    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
//        if (mOnItemClickListener != null) {
//            holder.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, holder.getLayoutPosition()));
//        }
//        if (mOnItemLongClickListener != null) {
//            holder.itemView.setOnLongClickListener(v -> mOnItemLongClickListener.onItemLongClick(v, holder.getLayoutPosition()));
//        }
//        onPostBindViewHolder(holder, mDataList.get(position));
//    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            return;
        }

        int actualPosition = hasHeader() ? position - 1 : position;

        if (mOnItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> mOnItemClickListener.onItemClick(v, actualPosition));
        }

        if (mOnItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(v -> mOnItemLongClickListener.onItemLongClick(v, actualPosition));
        }

        onPostBindViewHolder(holder, mDataList.get(actualPosition));
    }

    @Override
    public int getItemCount() {
        return (mDataList == null) ? 0 : mDataList.size() + (hasHeader() ? 1 : 0);
    }

    public abstract void onPostBindViewHolder(ViewHolder holder, T t);

    public T getItem(int position) {
        return mDataList.get(hasHeader() ? position - 1 : position);
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(View view, int position);
    }

    public void add(T t) {
        mDataList.add(t);
        notifyItemInserted(mDataList.size() - 1);
    }

    public void addAll(List<T> items) {
        for (T t : mDataList) {
            add(t);
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    public void filterList(List<T> filteredNames) {
        this.mDataList = filteredNames;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateData(List<T> viewModels) {
        mDataList.clear();
        mDataList.addAll(viewModels);
        notifyDataSetChanged();
    }

    public void setHeaderLayout(int headerLayoutId) {
        this.mHeaderLayoutId = headerLayoutId;
    }

    public boolean hasHeader() {
        return mHeaderLayoutId != -1;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader() && position == 0) {
            return VIEW_TYPE_HEADER;
        }
        return VIEW_TYPE_ITEM;
    }
}