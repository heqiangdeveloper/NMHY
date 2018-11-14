package com.cimcitech.nmhy.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.cimcitech.nmhy.activity.main.MainActivity;
import com.cimcitech.nmhy.bean.message.MessageContent;
import com.cimcitech.nmhy.bean.message.MessageData;
import com.cimcitech.nmhy.utils.Config;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

import cn.jpush.android.api.JPushInterface;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by qianghe on 2018/7/4.
 */

public class MyReceiver extends BroadcastReceiver {
    private static final String TAG = "jiguang";
    public final static String REFRESH_MESSAGE = "com.cimcitech.nmhy.receiver.refresh.message";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();
            Log.d(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Log.d(TAG, "[MyReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                Log.d("jg", "[MyReceiver] 接收到推送下来的通知");
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Log.d("jg", "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);

                String content = bundle.getString(JPushInterface.EXTRA_ALERT);
                Log.d("mymsg", "通知:" + content);
                //将新消息写入数据库中
                addMsg(context, content);

            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
                String content = bundle.getString(JPushInterface.EXTRA_ALERT);

                //先判断用户是否已登录
                Intent i = null;
//                if(isLogin(context)){
//                    //打开自定义的Activity
//                    i = new Intent(context, MainActivity.class);
//                    i.putExtra("content", content);
//                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                }else{
//                    i = new Intent(context, LoginActivity.class);
//                }
                i = new Intent(context, MainActivity.class);
                i.putExtra("content", content);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
                Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
                Log.w(TAG, "[MyReceiver]" + intent.getAction() + " connected state change to " + connected);
            } else {
                Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        } catch (Exception e) {

        }
    }

    private boolean isLogin(Context context){
        //如果是已登录过的，直接跳过登录界面
//        SharedPreferences sp = context.getSharedPreferences(Config.KEY_LOGIN_AUTO, MODE_PRIVATE);
//        if(sp.getString("user_name","").length() != 0 &&
//                sp.getString("password","").length() != 0 &&
//                sp.getString("realName","").length() != 0 &&
//                sp.getLong("userId",0) != 0){
//            return true;
//        }else{
//            return false;
//        }
        return true;
    }

    private void addMsg(final Context context, String content){
        //time
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String time = format.format(calendar.getTime());

        MessageContent messageContent = new MessageContent("for test",null,null,null);
        MessageData messageData = new MessageData(0,content,time,messageContent);//默认未读

//        new AddMessageTask(context, new OnTaskFinishedListener<Boolean>() {
//            @Override
//            public void onTaskFinished(Boolean data) {
//                //通知MessageFragment页面进行更新
//                if(data){
//                    Config.unReadMsg++;
//                    Intent intent = new Intent();
//                    intent.setAction(MyReceiver.REFRESH_MESSAGE);
//                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
//                }
//            }
//        }, messageData).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        //if (MainActivity.isForeground) {
//            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
//            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            Intent msgIntent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//            msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);
//            if (extras.length() != 0 ) {
//                try {
//                    JSONObject extraJson = new JSONObject(extras);
//                    if (extraJson.length() > 0) {
//                        msgIntent.putExtra(MainActivity.KEY_EXTRAS, extras);
//                    }
//                } catch (JSONException e) {
//
//                }
//
//            }
//            LocalBroadcastManager.getInstance(context).sendBroadcast(msgIntent);
        //}
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " +json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.get(key));
            }
        }
        return sb.toString();
    }
}
