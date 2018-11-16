
package com.cimcitech.nmhy.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cimcitech.nmhy.R;

public class DialogUtils {
    /**
     * 长时间的Toast提示
     *
     * @param context
     * @param msg
     */
    public static void showToast(Context context, String msg) {

        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间的Toast提示
     *
     * @param context
     * @param msgId
     */
    public static void showToast(Context context, int msgId) {

        Toast.makeText(context, msgId, Toast.LENGTH_LONG).show();
    }

    /**
     * 短时间的Toast提示
     *
     * @param context
     * @param msgId
     */
    public static void showToastShort(Context context, int msgId) {

        Toast.makeText(context, msgId, Toast.LENGTH_SHORT).show();
    }

    /**
     * 短时间的Toast提示
     *
     * @param context
     * @param
     */
    public static void showToastShort(Context context, String msg) {

        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * 创建自定义ProgressDialog
     *
     * @param context
     * @return
     */
//    public static Dialog createLoadingDialog(Context context) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View v = inflater.inflate(R.layout.layout_loading_dialog, null); // 得到加载view
//        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view); // 加载布局
//        Dialog loadingDialog = new Dialog(context, R.style.loading_dialog); // 创建自定义样式dialog
//        loadingDialog.setCancelable(false); // 不可以用"返回键"取消
//        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT));
//        return loadingDialog;
//    }

//    public static Dialog createLoginDialog(Context context) {
//        LayoutInflater inflater = LayoutInflater.from(context);
//        View v = inflater.inflate(R.layout.layout_login_dialog, null); // 得到加载view
//        LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view); // 加载布局
//        Dialog loginDialog = new Dialog(context, R.style.login_dialog); // 创建自定义样式dialog
//        loginDialog.setCancelable(false); // 不可以用"返回键"取消
//        loginDialog.setContentView(layout, new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.MATCH_PARENT,
//                LinearLayout.LayoutParams.MATCH_PARENT));
//        return loginDialog;
//    }

    public static Dialog createCommittingDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.layout_committing_dialog, null); // 得到加载view
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.dialog_view); // 加载布局
        Dialog committingDialog = new Dialog(context, R.style.committing_dialog); // 创建自定义样式dialog
        committingDialog.setCancelable(false); // 不可以用"返回键"取消
        committingDialog.setContentView(layout, new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));
        return committingDialog;
    }
}
