package com.codringreen.farmloading.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.codringreen.farmloading.R;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private final Drawable mDivider;

    public DividerItemDecoration(Context context) {
        this.mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider);
    }

    @Override
    public void onDrawOver(@NonNull Canvas c, RecyclerView parent, @NonNull RecyclerView.State state) {
        int paddingLeft = parent.getPaddingLeft();
        int width = parent.getWidth() - parent.getPaddingRight();
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = parent.getChildAt(i);
            int bottom = childAt.getBottom() + ((RecyclerView.LayoutParams) childAt.getLayoutParams()).bottomMargin;
            this.mDivider.setBounds(paddingLeft, bottom, width, this.mDivider.getIntrinsicHeight() + bottom);
            this.mDivider.draw(c);
        }
    }
}
