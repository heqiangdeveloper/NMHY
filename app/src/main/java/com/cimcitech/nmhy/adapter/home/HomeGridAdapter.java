package com.cimcitech.nmhy.adapter.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cimcitech.nmhy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cimcitech on 2017/7/25.
 */

public class HomeGridAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private Context context;
    private String AppAuthStr;
    private List<Integer> ImageLists = new ArrayList<Integer>();

    private List<String> TextLists = new ArrayList<String>(){{
    }};

    private final String ad = "A";//管理员
    private final String mw = "G";//门卫
    private final String ck = "M";//仓管
    private final String sc = "N";//生产人员
    private final String ld = "Y";//领导
    private final String sa = "S";//业务员

    public List<String> getAll() {
        return TextLists;
    }

    public HomeGridAdapter(Context context, String AppAuthStr) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.AppAuthStr = AppAuthStr;
        initAppAuth();
    }

    /*
    * 1.模块授权：在gc_user的app_auth列中授权
    *   G-门卫（车辆出厂、扫描出厂）
    *   M-仓库管理员（车辆入库、扫码入库、生产进度）
    *   N-生产人员（生产进度，检验）
    *   Y-领导（客户拜访、意向跟踪、报价单、订单合同、工作汇报、我的客户、联系人、问题反馈、回款跟踪、生产进度、报表）
    *   S-业务员（客户拜访、意向跟踪、报价单、订单合同、工作汇报、我的客户、联系人、问题反馈、回款跟踪、生产进度、发车申请)；另外不授权，缺省为业务员
    *   A-管理员（所有权限）
     */
    public void initAppAuth(){
        if(AppAuthStr != null && AppAuthStr.length() == 1){
            addModule(AppAuthStr);
        }else if(AppAuthStr != null && AppAuthStr.length() > 1){
            String[] authStrs = AppAuthStr.split(",");
            if(AppAuthStr.contains(ad)){//管理员
                addModule(ad);
            }else{
                for(int i = 0; i < authStrs.length; i++){
                    addModule(authStrs[i]);
                }
                removeDuplicate();//移除掉ImageLists，TextLists中重复的项
            }
        }else{
            addModule(sa);//缺省值
        }
    }

    public HomeGridAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        addCustomerVisit();
    }

    public void addModule(String str){
        switch (str) {
            case ck://仓库
                addCKData();
                break;
            case mw://门卫
                addMWData();
                break;
            case ld://领导
                addLDData();
                break;
            case sa://业务员
                addSAData();
                break;
            case ad://管理员
                addADData();
                break;
            case sc://生产人员
                addSCData();
                break;
        }
    }

    public void removeDuplicate(){//移除掉ImageLists，TextLists中重复的项
        for ( int i = 0 ; i < ImageLists.size() - 1 ; i ++ ) {   //从左向右循环
            for ( int j = ImageLists.size() - 1 ; j > i; j -- ) {  //从右往左内循环
                if (ImageLists.get(j).equals(ImageLists.get(i))) {
                    ImageLists.remove(j);                              //相等则移除
                }
            }
        }
        for ( int i = 0 ; i < TextLists.size() - 1 ; i ++ ) {   //从左向右循环
            for ( int j = TextLists.size() - 1 ; j > i; j -- ) {  //从右往左内循环
                if (TextLists.get(j).equals(TextLists.get(i))) {
                    TextLists.remove(j);                              //相等则移除
                }
            }
        }
    }

    //客户拜访
    public void addCustomerVisit(){
        ImageLists.add(R.mipmap.oil_request);
        TextLists.add(context.getResources().getString(R.string.item_oil_request));
        ImageLists.add(R.mipmap.oil_report);
        TextLists.add(context.getResources().getString(R.string.add_oil_report_label));
        ImageLists.add(R.mipmap.oil_history);
        TextLists.add(context.getResources().getString(R.string.item_oil_request_history));
        ImageLists.add(R.mipmap.oil_report_history);
        TextLists.add(context.getResources().getString(R.string.item_oil_report_history));

        ImageLists.add(R.mipmap.ship);
        TextLists.add(context.getResources().getString(R.string.item_ship));
        ImageLists.add(R.mipmap.plan);
        TextLists.add(context.getResources().getString(R.string.item_plan));
    }

    //意向跟踪
    public void addIntentionTrack(){
        ImageLists.add(R.mipmap.ic_launcher);
        TextLists.add("意向跟踪");
    }

    //报价单
    public void addQuotedPrice(){
        ImageLists.add(R.mipmap.ic_launcher);
        TextLists.add("报价单");
    }

    //订单合同
    public void addOrderContract(){
        ImageLists.add(R.mipmap.ic_launcher);
        TextLists.add("订单合同");
    }

    //工作汇报
    public void addWorkWeekly(){
        ImageLists.add(R.mipmap.ic_launcher);
        TextLists.add("工作汇报");
    }

    //我的客户
    public void addMyClient(){
        ImageLists.add(R.mipmap.ic_launcher);
        TextLists.add("我的客户");
    }

    //联系人
    public void addContactPerson(){
        ImageLists.add(R.mipmap.ic_launcher);
        TextLists.add("联系人");
    }

    //问题反馈
    public void addFeedBack(){
        ImageLists.add(R.mipmap.ic_launcher);
        TextLists.add("问题反馈");
    }

    //回款跟踪
    public void addPayment(){
        ImageLists.add(R.mipmap.ic_launcher);
        TextLists.add("回款跟踪");
    }

    //生产进度
    public void addProduction(){
        ImageLists.add(R.mipmap.ic_launcher);
        TextLists.add("生产进度");
    }

    //车辆入库
    public void addCarInStorage(){
        ImageLists.add(R.mipmap.ic_launcher);
        TextLists.add("车辆入库");
    }

    //发车申请
    public void addDepartRequest(){
        ImageLists.add(R.mipmap.ic_launcher);
        TextLists.add("发车申请");
    }

    //车辆出厂
    public void addCarOutFactory(){
        ImageLists.add(R.mipmap.ic_launcher);
        TextLists.add("车辆出厂");
    }

    //扫码入库
    public void addQRCodeInStorage(){
        ImageLists.add(R.mipmap.ic_launcher);
        TextLists.add("扫码入库");
    }

    //扫码出厂
    public void addQRCodeOutFactory(){
        ImageLists.add(R.mipmap.ic_launcher);
        TextLists.add("扫码出厂");
    }

    //报表
    public void addReportMain(){
        ImageLists.add(R.mipmap.ic_launcher);
        TextLists.add("报表");
    }
    //检验
    public void addQRCodeTest(){
        ImageLists.add(R.mipmap.ic_launcher);
        TextLists.add("检验");
    }

    public void addADData(){
        addCustomerVisit();
        addIntentionTrack();
        addQuotedPrice();
        addOrderContract();
        addWorkWeekly();
        addMyClient();
        addContactPerson();
        addFeedBack();
        addPayment();
        addProduction();
        addCarInStorage();
        addDepartRequest();
        addCarOutFactory();
        addQRCodeInStorage();
        addQRCodeOutFactory();
        addReportMain();
        addQRCodeTest();
    }

    public void addMWData(){
        addCarOutFactory();
        addQRCodeOutFactory();
    }

    public void addCKData(){
        addCarInStorage();
        addQRCodeInStorage();
    }

    public void addSCData(){
        addProduction();
        addQRCodeTest();
    }

    public void addLDData(){
        addCustomerVisit();
        addIntentionTrack();
        addQuotedPrice();
        addOrderContract();
        addWorkWeekly();
        addMyClient();
        addContactPerson();
        addFeedBack();
        addPayment();
        addProduction();
        addReportMain();
    }

    public void addSAData(){
        addCustomerVisit();
    }

    @Override
    public int getCount() {
        return ImageLists.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImgTextWrapper wrapper;
        if (convertView == null) {
            wrapper = new ImgTextWrapper();
            convertView = inflater.inflate(R.layout.home_grid_item, null);
            wrapper.imageButton = (ImageView) convertView.findViewById(R.id.logoButton);
            wrapper.textView = (TextView) convertView.findViewById(R.id.tv_logo);
            convertView.setTag(wrapper);
            convertView.setPadding(5, 0, 5, 0);
        } else {
            wrapper = (ImgTextWrapper) convertView.getTag();
        }
        wrapper.imageButton.setBackgroundResource(ImageLists.get(position));
        wrapper.textView.setText(TextLists.get(position));
        return convertView;
    }

    class ImgTextWrapper {
        ImageView imageButton;
        TextView textView;
    }
}
