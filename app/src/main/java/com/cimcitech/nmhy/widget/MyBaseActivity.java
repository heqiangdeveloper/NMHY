/*******************************************************************************
 *
 * Copyright (c) Weaver Info Tech Co. Ltd
 *
 * BaseActivity
 *
 * app.ui.BaseActivity.java
 * TODO: File description or class description.
 *
 * @author: Administrator
 * @since:  2014-9-03
 * @version: 1.0.0
 *
 * @changeLogs:
 *     1.0.0: First created this class.
 *
 ******************************************************************************/
package com.cimcitech.nmhy.widget;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * @author gao_chun
 * 该类为Activity基类
 */
public class MyBaseActivity extends AppCompatActivity {

    public static final String TAG = "tdlog";

    //在基类中初始化Dialog
    public Dialog mLoading,mLoginDialog,mCommittingDialog;

    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*if (!ValidateUtils.isNetworkAvailable(this)){
            DialogUtils.showToast(this,R.string.text_network_unavailable);
        }*/
        //mLoading = DialogUtils.createLoadingDialog(this);
        //mLoginDialog = DialogUtils.createLoginDialog(this);
        mCommittingDialog = DialogUtils.createCommittingDialog(this);
    }
}
