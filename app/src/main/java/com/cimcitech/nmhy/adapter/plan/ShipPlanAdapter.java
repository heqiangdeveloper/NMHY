package com.cimcitech.nmhy.adapter.plan;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.bean.oil.OilReportHistoryVo;
import com.cimcitech.nmhy.bean.plan.ShipBean;
import com.cimcitech.nmhy.bean.plan.ShipPlanVo;
import com.cimcitech.nmhy.utils.Config;

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
            //船名
            String shipNameStr =
                    item.getcShipName() != null && !item.getcShipName().equals("") ?
                    item.getcShipName() : "";
            //航次号
            String voyageNoStr =
                    item.getVoyageNo() != null && !item.getVoyageNo().equals("") ?
                    item.getVoyageNo() : "";
            //航次顺序
            String portTransportOrderStr =
                    item.getPortTransportOrder() != null && !item.getPortTransportOrder().equals("") ?
                    item.getPortTransportOrder() : "";
            //预计起航时间
            String estimatedSailingTimeStr =
                    item.getEstimatedSailingTime() != null && !item.getEstimatedSailingTime().equals("") ?
                    item.getEstimatedSailingTime() : "";
            //预计止航时间
            String  estimatedStopTimeStr=
                    item.getEstimatedStopTime() != null && !item.getEstimatedStopTime().equals("") ?
                    item.getEstimatedStopTime() : "";
            //实际起航时间
            String actualSailingTimeStr =
                    item.getActualSailingTime() != null && !item.getActualSailingTime().equals("") ?
                    item.getActualSailingTime() : "";
            //实际止航时间
            String actualStopTimeStr =
                    item.getActualStopTime() != null && !item.getActualStopTime().equals("") ?
                    item.getActualStopTime() : "";

            ((ItemViewHolder) holder).ship_name_Tv.setText(Html.fromHtml(context.getResources().getString(R.string.ship_name_label) + ": " +
                    "<font color='#666666'>" + shipNameStr + "</font>"));
            ((ItemViewHolder) holder).voyageNo_Tv.setText(Html.fromHtml(context.getResources().getString(R.string.voyageNo_label) + ": " +
                    "<font color='#666666'>" + voyageNoStr + "</font>"));
            ((ItemViewHolder) holder).portTransportOrder_Tv.setText(Html.fromHtml(context.getResources().getString(R.string.portTransportOrder_label) + ": " +
                    "<font color='#666666'>" + portTransportOrderStr + "</font>"));
            ((ItemViewHolder) holder).estimatedSailingTime_Tv.setText(Html.fromHtml(context.getResources().getString(R.string.estimatedSailingTime_label) + ": " +
                    "<font color='#666666'>" + estimatedSailingTimeStr + "</font>"));
            ((ItemViewHolder) holder).estimatedStopTime_Tv.setText(Html.fromHtml(context.getResources().getString(R.string.estimatedStopTime_label) + ": " +
                    "<font color='#666666'>" + estimatedStopTimeStr + "</font>"));
            ((ItemViewHolder) holder).actualSailingTime_Tv.setText(Html.fromHtml(context.getResources().getString(R.string.actualSailingTime_label) + ": " +
                    "<font color='#666666'>" + actualSailingTimeStr + "</font>"));
            ((ItemViewHolder) holder).actualStopTime_Tv.setText(Html.fromHtml(context.getResources().getString(R.string.actualStopTime_label) + ": " +
                    "<font color='#666666'>" + actualStopTimeStr + "</font>"));

            int fStatus = 0;
            if(item.getFstatus() != null && !item.getFstatus().equals("")){
                fStatus = Integer.parseInt(item.getFstatus());
            }
            String fStatusStr = Config.fStatusStrList.get(fStatus);
            if(fStatus == 2){//航行中
                ((ItemViewHolder) holder).fStatus_Tv.setText(Html.fromHtml(context.getResources().getString(R.string.fStatus_label) + ": " +
                        "<font color='#228B22'>" + fStatusStr + "</font>"));
            }else if(fStatus == 3){//航次结束
                ((ItemViewHolder) holder).fStatus_Tv.setText(Html.fromHtml(context.getResources().getString(R.string.fStatus_label) + ": " +
                        "<font color='#E96E53'>" + fStatusStr + "</font>"));
            }else{//计划中-确定 和 计划中-不确定
                ((ItemViewHolder) holder).fStatus_Tv.setText(Html.fromHtml(context.getResources().getString(R.string.fStatus_label) + ": " +
                        "<font color='#79C4EC'>" + fStatusStr + "</font>"));
            }

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
        TextView ship_name_Tv,voyageNo_Tv, portTransportOrder_Tv,estimatedSailingTime_Tv,
                estimatedStopTime_Tv,actualSailingTime_Tv,actualStopTime_Tv, fStatus_Tv;
        public ItemViewHolder(View view) {
            super(view);
            ship_name_Tv = view.findViewById(R.id.ship_name_tv);
            voyageNo_Tv = view.findViewById(R.id.voyageNo_tv);
            portTransportOrder_Tv = view.findViewById(R.id.portTransportOrder_tv);
            estimatedSailingTime_Tv = view.findViewById(R.id.estimatedSailingTime_tv);
            estimatedStopTime_Tv = view.findViewById(R.id.estimatedStopTime_tv);
            actualSailingTime_Tv = view.findViewById(R.id.actualSailingTime_tv);
            actualStopTime_Tv = view.findViewById(R.id.actualStopTime_tv);
            fStatus_Tv = view.findViewById(R.id.fStatus_tv);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }
}
