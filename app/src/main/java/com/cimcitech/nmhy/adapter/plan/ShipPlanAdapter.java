package com.cimcitech.nmhy.adapter.plan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.bean.oil.OilReportHistoryVo;
import com.cimcitech.nmhy.bean.plan.ShipBean;
import com.cimcitech.nmhy.bean.plan.ShipPlanVo;

import java.util.List;

/**
 * Created by cimcitech on 2017/7/31.
 */

public class ShipPlanAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private List<ShipPlanVo.DataBean> data;
    private LayoutInflater inflater;
    private static final int TYPE_END = 2;
    private boolean isNotMoreData = false;
    private Context context;

    public ShipPlanAdapter(Context context, List<ShipPlanVo.DataBean> data) {
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
            View view = inflater.inflate(R.layout.ship_plan_list_item, parent, false);
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
            final ShipPlanVo.DataBean item = data.get(position);
            String voyageNoStr = item.getVoyageNo() != null && !item.getVoyageNo()
                   .equals("") ? "航次号: " + item.getVoyageNo() : "航次号: " + "";
            ((ItemViewHolder) holder).voyageNo_Tv.setText(voyageNoStr);
            String routeNameStr = item.getRouteName() != null && !item.getRouteName().equals
                    ("") ? "线路名称: " + item.getRouteName() : "线路名称: " + "";
            ((ItemViewHolder) holder).routeName_Tv.setText(routeNameStr);
//            String portTransportOrderStr = item.getPortTransportOrder() != null && !item
//                    .getPortTransportOrder().equals("") ? "航次顺序: " + item.getPortTransportOrder() : "航次顺序: " + "";
//            ((ItemViewHolder) holder).portTransportOrder_Tv.setText(portTransportOrderStr);
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
        TextView voyageNo_Tv, routeName_Tv, portTransportOrder_Tv;
        public ItemViewHolder(View view) {
            super(view);
            voyageNo_Tv = view.findViewById(R.id.voyageNo_tv);
            routeName_Tv = view.findViewById(R.id.routeName_tv);
            portTransportOrder_Tv = view.findViewById(R.id.portTransportOrder_tv);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }
}
