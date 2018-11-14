package com.cimcitech.nmhy.activity.message;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.adapter.message.MessageAdapter;
import com.cimcitech.nmhy.adapter.message.MessagePopupWindowAdapter;
import com.cimcitech.nmhy.bean.message.MessageData;
import com.cimcitech.nmhy.receiver.MyReceiver;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by cimcitech on 2017/7/31.
 */

public class MessageFragment extends Fragment {
    @Bind(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @Bind(R.id.message_top_area)
    LinearLayout message_top_Area;
    @Bind(R.id.message_top_category_name)
    TextView message_top_category_Name;
    @Bind(R.id.message_top_category_label)
    TextView message_top_category_Label;

    private MessageAdapter adapter;
    private List<MessageData> data;
    private PopupWindow pop;

    public static final String REFRESH_UNREADMSG_BROADCAST = "com.cimcitech.cimctd" +
            ".refresh_unreadmsg_broadcast";
    public static final String KEY_MESSAGE = "content";
    public static final String KEY_EXTRAS = "title";
    private IntentFilter myIntentFilter;
    private Bundle savedInstanceState;
    private TextView opened_Tv;
    private TextView remove_Tv;
    private LinearLayout view2;
    private AlertDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_message, container, false);
        ButterKnife.bind(this,view);
        this.savedInstanceState = savedInstanceState;
        //initDialog();
        registBroadcastReceiver();

        data = new ArrayList<MessageData>();
        getData();
        return view;
    }

    public void registBroadcastReceiver(){
        myIntentFilter = new IntentFilter();
        myIntentFilter.addAction(MyReceiver.REFRESH_MESSAGE);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver,
                myIntentFilter);
    }

    public  void unRegistBroadcastReceiver(){
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
    }

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals(MyReceiver.REFRESH_MESSAGE)){
                adapter.notifyDataSetChanged();
                getData();
            }
        }
    };

    public void getData(){
//        new QueryMessageTask(getActivity(), new OnTaskFinishedListener<List<MessageData>>() {
//            @Override
//            public void onTaskFinished(final List<MessageData> datas) {
//                sendRefreshUnReadMsgBroadcast();//更新未读信息数
//                data = datas;
//                final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//                mRecyclerView.setLayoutManager(layoutManager);
//                adapter = new MessageAdapter(getActivity(),data);
//                mRecyclerView.setAdapter(adapter);
//                //给List添加点击事件
//                adapter.setOnItemClickListener(new MessageAdapter.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        int id = data.get(position).getId();
//                        int opened = data.get(position).getOpened();
//                        changeUnReadMsgAndOpened(id,opened);
//                        Intent intent = new Intent(getActivity(), MessageDetailActivity.class);
//                        intent.putExtra("quoteid",6);
//                        getActivity().startActivity(intent);
//                    }
//
//                    @Override
//                    public void onItemLongClick(View view, int position) {
//                        int id = data.get(position).getId();
//                        int opened = data.get(position).getOpened();
//                        showDialog(id,opened);
//                    }
//                });
//                //adapter.notifyDataSetChanged();
//            }
//        }).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void changeUnReadMsgAndOpened(int id,int opened){
//        if(opened == 0){
//            new UpdateMessageTask(getActivity(), new OnTaskFinishedListener<Boolean>() {
//                @Override
//                public void onTaskFinished(Boolean data) {
//                    if(data){
//                        getData();
//                        sendRefreshUnReadMsgBroadcast();
//                    }
//                }
//            },id,1).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//        }
    }

    public void sendRefreshUnReadMsgBroadcast(){
        Intent intent = new Intent();
        intent.setAction(MessageFragment.REFRESH_UNREADMSG_BROADCAST);
        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
    }

    public void showDialog(final int id,final int opened){
        /*String openedStr = (opened == 0) ? getResources().getString(R.string
                .message_dialog_is_opend):
                getResources().getString(R.string.message_dialog_not_opend);
        opened_Tv.setText(openedStr);*/

        String[] items = new String[2];
        items[0] = (opened == 0) ? getResources().getString(R.string.message_dialog_is_opend):
                getResources().getString(R.string.message_dialog_not_opend);
        items[1] = getResources().getString(R.string.message_dialog_remove);

        new AlertDialog.Builder(getActivity())
                //.setTitle()
        .setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case 0://标为已读/未读
                        dialog.dismiss();
                        changeMsgOpened(id,opened);
                        break;
                    case 1://移除
                        dialog.dismiss();
                        removeMsg(id);
                        break;
                }
            }
        }).create().show();
    }

    public void changeMsgOpened(int id,int opened){
        int mOpened = (opened == 0) ? 1:0;
//        new UpdateMessageTask(getActivity(), new OnTaskFinishedListener<Boolean>() {
//            @Override
//            public void onTaskFinished(Boolean data) {
//                if(data){
//                    getData();
//                }
//            }
//        },id,mOpened).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void removeMsg(int id){
//        new RemoveMessageTask(getActivity(), new OnTaskFinishedListener<Boolean>() {
//            @Override
//            public void onTaskFinished(Boolean data) {
//                if(data){
//                    getData();
//                }
//            }
//        },id).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @OnClick({R.id.message_top_area})
    public void onclick(View view) {
        switch (view.getId()){
            case R.id.message_top_area:
                message_top_category_Label.setText(getResources().getString(R.string.
                        message_top_category_label_open));
                List<String> list = new ArrayList<String>();
                list.add("全部");
                list.add("已接收");
                list.add("未接收");
                showContactUsPopWin(getActivity(), "", list);
                pop.showAtLocation(view, Gravity.CENTER, 0, 0);
                break;
        }
    }

    public void showContactUsPopWin(Context context, String title, final List<String> list) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_message, null);
        //view.getBackground().setAlpha(100);
        // 创建PopupWindow对象
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
        View pop_reward_view = view.findViewById(R.id.pop_reward_view);
        String content = message_top_category_Name.getText().toString();
        final MessagePopupWindowAdapter adapter = new MessagePopupWindowAdapter(context, list, content);
        ListView listView = view.findViewById(R.id.listContent);
        listView.setAdapter(adapter);
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        pop_reward_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                message_top_category_Label.setText(getResources().getString(R.string.
                        message_top_category_label_close));
                pop.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                message_top_category_Name.setText(list.get(i));
                message_top_category_Label.setText(getResources().getString(R.string.
                        message_top_category_label_close));
                pop.dismiss();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unRegistBroadcastReceiver();
    }
}
