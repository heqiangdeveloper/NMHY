package com.cimcitech.nmhy.adapter.oil;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.bean.oil.OilReportData;
import java.util.List;

/**
 * Created by qianghe on 2018/10/11.
 */

public class OilReportAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private List<OilReportData> data;
    private LayoutInflater inflater;
    private static final int TYPE_END = 2;
    private boolean isNotMoreData = false;
    private Context context;

    public OilReportAdapter(Context context, List<OilReportData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
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

        void onDeleteBtnClickListener(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = inflater.inflate(R.layout.oil_report_item_view, parent, false);
            return new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            View view = inflater.inflate(R.layout.recycler_end_view, parent, false);
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

                ((ItemViewHolder) holder).delete_Iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onDeleteBtnClickListener(((ItemViewHolder) holder).delete_Iv, position);
                    }
                });
            }
            OilReportData item = data.get(position);
            ((ItemViewHolder) holder).fuelKindTv.setText(item.getFuelKind() + "");
            ((ItemViewHolder) holder).unitTv.setText(item.getUnit() + "");
            ((ItemViewHolder) holder).addFuelQtyTv.setText(item.getAddFuelQty() + "");

            //最后一个item的分割线隐藏
            if(position == data.size() - 1){
                ((ItemViewHolder) holder).divide_Vw.setVisibility(View.GONE);
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
        TextView fuelKindTv,unitTv,addFuelQtyTv;
        ImageView delete_Iv;
        LinearLayout root_Ll;
        View divide_Vw;

        public ItemViewHolder(View view) {
            super(view);
            fuelKindTv = view.findViewById(R.id.fuelKind_tv);
            unitTv = view.findViewById(R.id.unit_tv);
            addFuelQtyTv = view.findViewById(R.id.addFuelQty_tv);
            delete_Iv = view.findViewById(R.id.delete_iv);
            root_Ll = view.findViewById(R.id.root_ll);
            divide_Vw = view.findViewById(R.id.divide_vw);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }

    /**
     * 提供给Activity刷新数据
     * @param list
     */
    public void updateList(List<OilReportData> list){
        this.data = list;
        notifyDataSetChanged();
    }
}
