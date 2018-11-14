package com.cimcitech.nmhy.adapter.message;

/**
 * Created by cimcitech on 2017/8/2.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.bean.message.MessageData;
import com.cimcitech.nmhy.utils.MessageDataSortClass;
import com.cimcitech.nmhy.utils.MessageTime;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by cimcitech on 2017/8/1.
 */

public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private List<MessageData> data;
    private LayoutInflater inflater;
    private static final int TYPE_END = 2;
    private boolean isNotMoreData = false;
    private Context context;
    private MessageTime msgTime;
    private SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public MessageAdapter(Context context, List<MessageData> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
        this.context = context;
        msgTime = new MessageTime();
        sort();
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
        void onItemLongClick(View view, int position);
    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.message_item_view, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        String dispalyTimeStr;
        Date date;
        MessageData item = null;
        if (holder instanceof ItemViewHolder) {
            if (onItemClickListener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        int position = holder.getLayoutPosition();
                        onItemClickListener.onItemClick(holder.itemView, position);
                    }

                });
            }
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View view) {
                    int position = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, position);
                    return false;
                }
            });

            item = data.get(position);
            ((ItemViewHolder) holder).msg_title_Tv.setText(item.getTitle() == null ? "标题":item.getTitle());
            ((ItemViewHolder) holder).msg_content_desc_Tv.setText(item.getMessageContent()
                    .getContentDesc()== null ? "测试消息..":item.getMessageContent().getContentDesc());

            dispalyTimeStr = item.getTime();
            try {
                date = format2.parse(dispalyTimeStr);
                dispalyTimeStr = msgTime.getTimePoint(date.getTime());
            }catch (Exception e){
                e.printStackTrace();
            }
            ((ItemViewHolder) holder).msg_time_Tv.setText(dispalyTimeStr);

            if(item.getOpened() == 0){//未读
                ((ItemViewHolder) holder).msg_count_Bt.setVisibility(View.VISIBLE);
            }else{
                ((ItemViewHolder) holder).msg_count_Bt.setVisibility(View.INVISIBLE);
            }
        }
    }

//    public int getImage(String fileName){
//        int resourceId ;
//        int dotIndex = fileName.lastIndexOf(".");
//        if (dotIndex < 0) {
//            return R.drawable.format_unkown;
//        }
//        /* 获取文件的后缀名 */
//        String end = fileName.substring(dotIndex, fileName.length()).toLowerCase();
//        switch (end){
//            //word
//            case ".doc":
//            case ".docx":
//                resourceId = R.drawable.format_word;
//                break;
//            //excel
//            case ".xls":
//            case ".xlsx":
//                resourceId = R.drawable.format_excel;
//                break;
//            //ppt
//            case ".ppt":
//            case ".pptx":
//                resourceId = R.drawable.format_ppt;
//                break;
//            //pdf
//            case ".pdf":
//                resourceId = R.drawable.format_pdf;
//                break;
//            //txt
//            case ".txt":
//                resourceId = R.drawable.format_text;
//                break;
//            //picture
//            case ".jpg":
//            case ".jpeg":
//            case ".png":
//                resourceId = R.drawable.format_picture;
//                break;
//            //zip,rar
//            case ".zip":
//            case ".rar":
//                resourceId = R.drawable.format_zip;
//                break;
//            //flash
//            case ".swf":
//            case ".gif":
//                resourceId = R.drawable.format_flash;
//                break;
//            //html
//            case ".html":
//                resourceId = R.drawable.format_html;
//                break;
//            //chm
//            case ".chm":
//                resourceId = R.drawable.format_chm;
//                break;
//            //vedio
//            case ".rmvb":
//            case ".wmv":
//            case ".avi":
//            case ".3gp":
//            case ".mp4":
//                resourceId = R.drawable.format_media;
//                break;
//            //audio
//            case ".mp3":
//                resourceId = R.drawable.format_music;
//                break;
//            case ".wav":
//            case ".mid":
//            case ".ogg":
//                resourceId = R.drawable.file_audio;
//                break;
//            default:
//                resourceId = R.drawable.format_unkown;
//                break;
//        }
//        return resourceId;
//    }

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
        return data.size();
    }


    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView msg_title_Tv,msg_time_Tv,msg_content_desc_Tv;
        Button msg_count_Bt;

        public ItemViewHolder(View view) {
            super(view);
            msg_title_Tv = view.findViewById(R.id.msg_title_tv);
            msg_time_Tv = view.findViewById(R.id.msg_time_tv);
            msg_content_desc_Tv = view.findViewById(R.id.msg_content_desc_tv);
            msg_count_Bt = view.findViewById(R.id.msg_count_bt);
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
    public void updateList(List<MessageData> list){
        this.data = list;
        sort();
        notifyDataSetChanged();
    }

    //对消息按时间先后排序
    public void sort(){
        MessageDataSortClass msgDataSortClass = new MessageDataSortClass();
        Collections.sort(data, msgDataSortClass);
    }
}
