package com.codringreen.farmloading.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;

import com.codringreen.farmloading.R;
import com.codringreen.farmloading.model.MenuModel;

import java.util.List;

import javax.annotation.Nonnull;

public class NavigationAdapters extends ArrayAdapter<MenuModel> {
    private final LayoutInflater inflater;
    private final int layoutResourceId;
    private final List<MenuModel> menuModels;

    public NavigationAdapters(Context myContext, int layoutResourceId, List<MenuModel> menuModels) {
        super(myContext, layoutResourceId, menuModels);
        this.layoutResourceId = layoutResourceId;
        this.menuModels = menuModels;
        this.inflater = LayoutInflater.from(myContext);
    }

    @Override
    @Nonnull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            view = this.inflater.inflate(this.layoutResourceId, parent, false);
            viewHolder.imageViewIcon = view.findViewById(R.id.imageViewIcon);
            viewHolder.textViewName = view.findViewById(R.id.textViewName);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MenuModel menuModel = this.menuModels.get(position);
        viewHolder.imageViewIcon.setImageResource(menuModel.getMenuIcon());
        viewHolder.textViewName.setText(menuModel.getMenuName());
        return view;
    }

    private static class ViewHolder {
        private AppCompatImageView imageViewIcon;
        private AppCompatTextView textViewName;

        private ViewHolder() {
        }
    }
}