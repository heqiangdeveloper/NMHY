package com.cimcitech.nmhy.adapter.oil;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.bean.oil.OilReportHistoryVo;
import com.cimcitech.nmhy.bean.oil.OilRequestHistoryVo;

import java.util.List;

/**
 * Created by cimcitech on 2017/7/31.
 */

public class OilRequestHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private List<OilRequestHistoryVo.DataBean.ListBean> data;
    private LayoutInflater inflater;
    private static final int TYPE_END = 2;
    private boolean isNotMoreData = false;
    private Context context;

    public OilRequestHistoryAdapter(Context context, List<OilRequestHistoryVo.DataBean.ListBean> data) {
        inflater = LayoutInflater.from(context);
        this.context = context;
        this.data = data;
    }

    public void setNotMoreData(boolean b) {
        this.isNotMoreData = b;
    }

    public boolean isNotMoreData() {
        return isNotMoreData;
    }

    public List getAll() {
        return data;
    }


    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClickListener(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.oil_request_history_list_item, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = inflater.inflate(R.layout.recycler_foot_view, parent, false);
            return new FootViewHolder(view);
        } else if (viewType == TYPE_END) {
            View view = inflater.inflate(R.layout.recycler_end_view, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, position);
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View view) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, position);
                        return false;
                    }
                });
            }
            final OilRequestHistoryVo.DataBean.ListBean item = data.get(position);
            String applyTimeStr = item.getApplyTime() != null && !item.getApplyTime()
                   .equals("") ? item.getApplyTime() : "today";
            ((ItemViewHolder) holder).applyTime_Tv.setText(applyTimeStr);
            String applyReasonStr = item.getBargeName() != null && !item.getBargeName().equals
                    ("") ? item.getBargeName() : "today";
            ((ItemViewHolder) holder).applyReason_Tv.setText(applyReasonStr);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            if (isNotMoreData())
                return TYPE_END;
            else
                return TYPE_FOOTER;
        } else
            return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return data.size() == 0 ? 0 : data.size() + 1;
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView applyReason_Tv, applyTime_Tv;
        public ItemViewHolder(View view) {
            super(view);
            applyReason_Tv = view.findViewById(R.id.applyReason_tv);
            applyTime_Tv = view.findViewById(R.id.applyTime_tv);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }
}
