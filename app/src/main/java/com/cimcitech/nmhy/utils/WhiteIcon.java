package com.cimcitech.nmhy.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.cimcitech.nmhy.R;

/**
 * Created by qianghe on 2019/1/8.
 */

public class WhiteIcon {
    private TextView tv;
    private Drawable drawable;
    private Context context;
    public WhiteIcon(Context context, TextView tv) {
        this.tv = tv;
        this.context = context;
        drawable = context.getResources().getDrawable(R.mipmap.white_icon);
        drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
    }

    public void setWhiteIcon(){
        tv.setClickable(false);
        //drawable left,top,right,bottom
        tv.setCompoundDrawables(null,null,drawable,null);
        //drawablePadding
        tv.setCompoundDrawablePadding((int) context.getResources().getDimension(R.dimen
                .drawable_padding_size));
    }
}
