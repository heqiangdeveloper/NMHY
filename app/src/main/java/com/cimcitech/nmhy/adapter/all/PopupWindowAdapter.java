package com.cimcitech.nmhy.adapter.all;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cimcitech.nmhy.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by cimcitech on 2017/8/1.
 */

public class PopupWindowAdapter extends BaseAdapter {

    private List<String> data;

    private LayoutInflater inflater;

    public PopupWindowAdapter(Context context, List<String> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public List<String> getAll() {
        return data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            view = inflater.inflate(R.layout.popup_window_item_view, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(data.get(i));
        return view;
    }

    static class ViewHolder {
        @Bind(R.id.textView)
        TextView textView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
