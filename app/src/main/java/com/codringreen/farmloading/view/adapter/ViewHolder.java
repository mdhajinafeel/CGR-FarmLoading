package com.codringreen.farmloading.view.adapter;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.SparseArray;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    private SparseArray<View> mViews;

    public ViewHolder(View itemView) {
        super(itemView);
        this.mViews = new SparseArray<>();
    }

    public ViewHolder(View itemView, int forPagination) {
        super(itemView);
    }

    public <T extends View> T getView(int i) {
        T t = (T) mViews.get(i);
        if (t != null) {
            return t;
        }
        T t2 = (T) itemView.findViewById(i);
        mViews.put(i, t2);
        return t2;
    }

    public ViewHolder setViewText(int viewId, CharSequence text) {
        ReflectHelper.invokeMethodIfExists("setText", getView(viewId), new Class[]{CharSequence.class}, text);
        return this;
    }

    public ViewHolder setViewText(int viewId, int resId) {
        ReflectHelper.invokeMethodIfExists("setText", getView(viewId), new Class[]{Integer.TYPE}, resId);
        return this;
    }

    public ViewHolder setViewTextColor(int viewId, int color) {
        ReflectHelper.invokeMethodIfExists("setTextColor", getView(viewId), new Class[]{Integer.TYPE}, color);
        return this;
    }

    public ViewHolder setViewTextSize(int viewId, float size) {
        ReflectHelper.invokeMethodIfExists("setTextSize", getView(viewId), new Class[]{Float.TYPE}, size);
        return this;
    }

    public ViewHolder setViewTextSize(int viewId, int unit, float size) {
        ReflectHelper.invokeMethodIfExists("setTextSize", getView(viewId), new Class[]{Integer.TYPE, Float.TYPE}, unit, size);
        return this;
    }

    public ViewHolder setViewImageResource(int viewId, int resId) {
        ReflectHelper.invokeMethodIfExists("setImageResource", getView(viewId), new Class[]{Integer.TYPE}, resId);
        return this;
    }

    public ViewHolder setViewImageBitmap(int viewId, Bitmap bitmap) {
        ReflectHelper.invokeMethodIfExists("setImageBitmap", getView(viewId), new Class[]{Bitmap.class}, bitmap);
        return this;
    }

    public ViewHolder setViewImageDrawable(int viewId, Drawable drawable) {
        ReflectHelper.invokeMethodIfExists("setImageDrawable", getView(viewId), new Class[]{Drawable.class}, drawable);
        return this;
    }

    public ViewHolder setViewImageURI(int viewId, Uri uri) {
        ReflectHelper.invokeMethodIfExists("setImageURI", getView(viewId), new Class[]{Uri.class}, uri);
        return this;
    }

    public ViewHolder setViewChecked(int viewId, boolean checked) {
        ReflectHelper.invokeMethodIfExists("setChecked", getView(viewId), new Class[]{Boolean.TYPE}, checked);
        return this;
    }

    public ViewHolder setViewOnClickListener(int viewId, View.OnClickListener listener) {
        ReflectHelper.invokeMethodIfExists("setOnClickListener", getView(viewId), new Class[]{View.OnClickListener.class}, listener);
        return this;
    }

    public ViewHolder setViewOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        ReflectHelper.invokeMethodIfExists("setOnLongClickListener", getView(viewId), new Class[]{View.OnLongClickListener.class}, listener);
        return this;
    }

    public ViewHolder setViewVisibility(int viewId, int visibility) {
        ReflectHelper.invokeMethodIfExists("setVisibility", getView(viewId), new Class[]{Integer.TYPE}, visibility);
        return this;
    }

    public ViewHolder setViewTypeface(int viewId, Typeface typeface) {
        ReflectHelper.invokeMethodIfExists("setTypeface", getView(viewId), new Class[]{Typeface.class}, typeface);
        return this;
    }

    public ViewHolder setViewPaintFlags(int viewId, int flags) {
        ReflectHelper.invokeMethodIfExists("setPaintFlags", getView(viewId), new Class[]{Integer.TYPE}, flags);
        return this;
    }

    public ViewHolder setViewProperty(int viewId, String propertyName, Object... params) {
        ReflectHelper.invokeMethodIfExists("set" + (Character.toUpperCase(propertyName.charAt(0)) + propertyName.substring(1)), getView(viewId), params);
        return this;
    }

    public ViewHolder setSelected(int viewId, boolean checked) {
        ReflectHelper.invokeMethodIfExists("setSelected", getView(viewId), new Class[]{Boolean.TYPE}, checked);
        return this;
    }
}