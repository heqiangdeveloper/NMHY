package com.cimcitech.nmhy.activity.user;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cimcitech.nmhy.R;
import com.cimcitech.nmhy.utils.Config;
import com.cimcitech.nmhy.utils.DataCleanManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by cimcitech on 2017/7/31.
 */

public class UserFragment extends Fragment {
    @Bind(R.id.userName_tv)
    TextView userName_Tv;
    public File mFile = null;
    public ProgressDialog pd;
    public int MAXSIZE = 0;
    public int TOTALSIZE = 0;
    public final int UPDATE_APK = 1;
    public final int TIMEOUT = 2;
    public final int DOWNLOAD_ERROR = 3;
    public final int DOWNLOAD_SUCCESS = 4;
    public final int MAX_PRO_LENGTH = 5;
    public final int UPDATEPROGRESS = 6;
    private SharedPreferences sp;
    //private ApkUpdateVo apkUpdateVo;

    public DataCleanManager manager = null;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_APK:
                    break;
                case TIMEOUT:
                    Toast.makeText(getActivity(), "连接超时，请检查网络", Toast.LENGTH_SHORT)
                            .show();
                    break;
                case DOWNLOAD_ERROR:
                    break;
                case DOWNLOAD_SUCCESS:
                    installApk(mFile);
                    break;
                case MAX_PRO_LENGTH:
                    if (MAXSIZE != 0) {
                        pd.setMax(MAXSIZE);
                    }
                    break;
                case UPDATEPROGRESS:
                    pd.setProgress(TOTALSIZE);
                    break;
            }
        }
    };

    private void getUserInfo() {
        //userName_Tv.setText(Config.userName);
        //userName_Tv.setText(Config.USERNAME );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user, container, false);
        ButterKnife.bind(this, view);
        //sp = getActivity().getSharedPreferences(Config.KEY_LOGIN_AUTO, MODE_PRIVATE);
        getUserInfo();
        //getData();

        return view;
    }

//    public boolean checkApkVersion() {
//        if (apkUpdateVo != null)
//            if (apkUpdateVo.isSuccess()) {
//                int versionCode = Integer.parseInt(
//                        ApkUpdateUtil.getVersionCode(getActivity()));
//                int webVersionCode = apkUpdateVo.getData().getVersioncode();
//                if (webVersionCode > versionCode) {
//                    return true;
//                } else
//                    return false;
//            }
//        return false;
//    }

    @OnClick({R.id.my_info_tv, R.id.my_company_tv,R.id.settings_tv})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.my_info_tv://个人资料
                //startActivity(new Intent(getActivity(),MyInfoActivity.class));
                break;
            case R.id.my_company_tv://我的公司
                break;
            case R.id.settings_tv://设置
                startActivity(new Intent(getActivity(),SettingsActivity.class));
                //getActivity().finish();
                break;
        }
    }

    public void ModifyUserInfoPreference(){
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("password", "");
        editor.putString("realName", "");
        editor.putLong("userId",0);
        editor.commit();
    }


    public void startToDownload() {
        String filePath = Environment.getExternalStorageDirectory() + "/newversion.apk";
        Log.d("heqiang", filePath);
        pd = new ProgressDialog(getActivity());
        pd.setTitle("正在下载");
        pd.setIndeterminate(false);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setCancelable(false);
        pd.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
//                try {
//                    mFile = getFileFromServer(apkUpdateVo.getData().getApkurl(), getActivity());
//                    Thread.sleep(1000);
//                    pd.dismiss();
//                    sendMsg(DOWNLOAD_SUCCESS);
//                } catch (Exception e) {
//                    pd.dismiss();
//                    sendMsg(DOWNLOAD_ERROR);
//                }
            }
        }).start();
    }

    public File getFileFromServer(String path, Context context) throws Exception {
        //如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            //获取到文件的大小
            //pd.setMax(conn.getContentLength());
            MAXSIZE = conn.getContentLength();
            sendMsg(MAX_PRO_LENGTH);
            InputStream is = conn.getInputStream();
            File file = new File(Environment.getExternalStorageDirectory(), "newversion.apk");
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] buffer = new byte[1024];
            int len;
            int total = 0;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
                total += len;
                //获取当前下载量
                //pd.setProgress(total);
                TOTALSIZE = total;
                sendMsg(UPDATEPROGRESS);
            }
            fos.close();
            bis.close();
            is.close();
            return file;
        } else {
            return null;
        }
    }

    protected void installApk(File file) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        if (Build.VERSION.SDK_INT >= 24)//7.0
        {
            Uri apkUri =
                    FileProvider.getUriForFile(getActivity(), "com.cimcitech.cimcly" +
                            ".fileprovider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }
        getActivity().startActivity(intent);
    }

    public boolean isNetworkAvalible(Context context) {
        ConnectivityManager manager = (ConnectivityManager) (context.getSystemService(Context
                .CONNECTIVITY_SERVICE));
        NetworkInfo[] infos = manager.getAllNetworkInfo();
        for (int i = 0; i < infos.length; i++) {
            if (infos[i].getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }
        return false;
    }

    public void sendMsg(int flag) {
        Message message = new Message();
        message.what = flag;
        handler.sendMessage(message);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
