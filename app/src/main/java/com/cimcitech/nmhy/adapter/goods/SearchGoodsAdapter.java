package com.cimcitech.nmhy.adapter.goods;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.bean.goods.SearchGoodsDataBean;
import com.cimcitech.nmhy.bean.oil.OilReportHistoryVo;
import com.cimcitech.nmhy.bean.plan.ShipBean;
import com.cimcitech.nmhy.bean.plan.ShipPlanVo;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.utils.EnumUtil;

import java.util.List;

/**
 * Created by cimcitech on 2017/7/31.
 */

public class SearchGoodsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private List<SearchGoodsDataBean.ListBean> data;
    private LayoutInflater inflater;
    private static final int TYPE_END = 2;
    private boolean isNotMoreData = false;
    private Context context;

    public SearchGoodsAdapter(Context context, List<SearchGoodsDataBean.ListBean> data) {
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
            View view = inflater.inflate(R.layout.search_goods_list_item, parent, false);
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
            final SearchGoodsDataBean.ListBean item = data.get(position);
            //起运港
            String loadPort = item.getLoadPort() != null && !item.getLoadPort().equals("") ? item.getLoadPort() : "";
            //目的港
            String unloadPort = item.getUnloadPort() != null && !item.getUnloadPort().equals("") ? item.getUnloadPort() : "";
            //捆包号
            String baleNum = item.getBaleNum() != null && !item.getBaleNum().equals("")
                    ? item.getBaleNum() : "";
            //品名
            String cargoName = item.getCargoName() != null && !item.getCargoName().equals("")
                    ? item.getCargoName() : "";
            //资源号
            String resourcesNum = item.getResourcesNum() != null && !item.getResourcesNum().equals("")
                    ? item.getResourcesNum() : "";
            //合同号
            String contractNo = item.getContractNo() != null && !item.getContractNo().equals("")
                    ? item.getContractNo() : "";
            //毛重
            double roughWeight = item.getRoughWeight();
            //规格
            String spec = item.getSpec() != null && !item.getSpec().equals("")
                    ? item.getSpec() : "";
            //是否已运
            //fstatus表示： “3”已运完   2 "正在运"  其他的0和null都是 未运
            String isLoad = item.getFstatus() != null && !item.getFstatus().equals("")
                    ? item.getFstatus() : "1";
            ((ItemViewHolder) holder).loadPort_Tv.setText(loadPort);
            ((ItemViewHolder) holder).unloadPort_Tv.setText(unloadPort);
            ((ItemViewHolder) holder).baleNum_Tv.setText(context.getResources().getString(R
                    .string.baleNum_label) + ": " +baleNum);
            ((ItemViewHolder) holder).cargoName_Tv.setText(context.getResources().getString(R
                    .string.cargoName_label) + ": " +cargoName);
            ((ItemViewHolder) holder).resourcesNum_Tv.setText(context.getResources().getString(R
                    .string.resourcesNum_label) + ": " +resourcesNum + "");
            ((ItemViewHolder) holder).contractNo_Tv.setText(context.getResources().getString(R
                    .string.contractNo_label) + ": " +contractNo + "");
            ((ItemViewHolder) holder).roughWeight_Tv.setText(context.getResources().getString(R
                    .string.roughWeight_label) + ": " +roughWeight + "");
            ((ItemViewHolder) holder).spec_Tv.setText(context.getResources().getString(R
                    .string.spec_label) + ": " +spec + "");
            CharSequence cs;
            if(isLoad.equals("3")){
                isLoad = context.getResources().getString(R.string.loaded_label);//已运完
                cs = Html.fromHtml(context.getResources()
                        .getString(R.string.load_status_label) + ": " + "<font color='#ff0000'>"
                        + isLoad + "</font>");
            }else if(isLoad.equals("2")){
                isLoad = context.getResources().getString(R.string.loading_label);//正在运
                cs = Html.fromHtml(context.getResources()
                        .getString(R.string.load_status_label) + ": " + "<font color='#00ff00'>"
                        + isLoad + "</font>");
            }else{
                isLoad = context.getResources().getString(R.string.unLoad_label);//unLoad_label
                cs = Html.fromHtml(context.getResources()
                        .getString(R.string.load_status_label) + ": " + "<font color='#0000ff'>"
                        + isLoad + "</font>");
            }
            ((ItemViewHolder) holder).isLoad_Tv.setText(cs);
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
        TextView loadPort_Tv,unloadPort_Tv,baleNum_Tv,cargoName_Tv,resourcesNum_Tv,isLoad_Tv,
        contractNo_Tv,roughWeight_Tv,spec_Tv;
        public ItemViewHolder(View view) {
            super(view);
            loadPort_Tv = view.findViewById(R.id.loadPort_tv);
            unloadPort_Tv = view.findViewById(R.id.unloadPort_tv);
            baleNum_Tv = view.findViewById(R.id.baleNum_tv);
            cargoName_Tv = view.findViewById(R.id.cargoName_tv);
            resourcesNum_Tv = view.findViewById(R.id.resourcesNum_tv);
            contractNo_Tv = view.findViewById(R.id.contractNo_tv);
            roughWeight_Tv = view.findViewById(R.id.roughWeight_tv);
            spec_Tv = view.findViewById(R.id.spec_tv);
            isLoad_Tv = view.findViewById(R.id.isLoad_tv);
        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {

        public FootViewHolder(View view) {
            super(view);
        }
    }
}
