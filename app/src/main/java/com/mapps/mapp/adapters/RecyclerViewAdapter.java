package com.mapps.mapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.AdapterView.OnItemLongClickListener;

import com.mapps.mapp.listeners.OnScrolledToEndListener;
import com.mapps.mapp.utils.ItemControl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class RecyclerViewAdapter<T, P extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<P> {

    protected Context context;

    protected ViewStyle viewStyle;

//	private PagingFragment pagingFragment;

    protected OnItemClickedListener clickListener;
    protected OnItemLongClickListener longClickListener;
    protected OnScrolledToEndListener onScrolledToEndListener;

    protected List<T> itemsList;

    public RecyclerViewAdapter(Context context) {
        this(context, null);
    }

    public RecyclerViewAdapter(Context context, OnItemClickedListener clickListener) {
        this.context = context;
        setOnItemClickListener(clickListener);
        itemsList = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickedListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener) {
        this.longClickListener = longClickListener;
    }

    public void setOnScrollToEndListener(OnScrolledToEndListener onScrolledToEndListener) {
        this.onScrolledToEndListener = onScrolledToEndListener;
    }

//	public void setPagingFragment(PagingFragment pagingFragment) {
//		this.pagingFragment = pagingFragment;
//	}

    @Override
    public void onBindViewHolder(P p, int position) {
        if (position == itemsList.size() - 1 && onScrolledToEndListener != null)
            onScrolledToEndListener.onScrolledToEnd();
    }

    @Override
    public int getItemCount() {
        return itemsList.size();
    }

    public interface OnItemClickedListener {
        void onClicked(int position, ItemControl control, Object... params);
    }

    public final void setViewStyle(ViewStyle viewStyle) {
        this.viewStyle = viewStyle;

        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return itemsList.get(position);
    }

    public void addAll(List<T> items) {
        int itemCount = getItemCount();
        itemsList.addAll(items);
//        notifyDataSetChanged();
        notifyItemRangeInserted(itemCount, items.size());
    }

    public void add(T item) {
        itemsList.add(item);
//        notifyDataSetChanged();
        notifyItemInserted(itemsList.size() - 1);
    }

    public void remove(T item) {
        itemsList.remove(item);

        notifyDataSetChanged();
    }

    public void clear() {
        itemsList.clear();

//		if (pagingFragment != null) {
//			pagingFragment.resetLayoutManager();
//		}
    }

    public void setItems(List<T> items) {
        itemsList.clear();
        itemsList.addAll(items);

        notifyDataSetChanged();

//		if (pagingFragment != null) {
//			pagingFragment.resetLayoutManager();
//		}
    }

    public List<T> getItems() {
        return Collections.unmodifiableList(itemsList);
    }

    public boolean isEmpty() {
        return itemsList.isEmpty();
    }

    public enum ViewStyle {
        STAGGERED, GRID, LIST
    }
}