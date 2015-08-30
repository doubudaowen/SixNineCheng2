package com.mobile.liujiucheng.sixninecheng.main.click;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.mobile.liujiucheng.sixninecheng.main.AboutActivity;
import com.mobile.liujiucheng.sixninecheng.main.AboutLiuJiuChengActivity;
import com.mobile.liujiucheng.sixninecheng.main.ContactActivity;
import com.mobile.liujiucheng.sixninecheng.main.CooperationActivity;
import com.mobile.liujiucheng.sixninecheng.main.FeedBackActivity;
import com.mobile.liujiucheng.sixninecheng.main.R;
import com.mobile.liujiucheng.sixninecheng.main.TradingActivity;
import com.mobile.liujiucheng.sixninecheng.main.UseInformationActivity;

public class SlidingClickEvent implements OnClickListener {

    private Context context;
    private View view;
    /**
     * 合作申请
     */
    private LinearLayout mLinearLayout01;
    /**
     * 交易回答
     */
    private LinearLayout mLinearLayout02;
    /**
     * 关于我们
     */
    private LinearLayout mLinearLayout03;

    /**
     * 关于六九城
     */
    private RelativeLayout ll_about69;
    /**
     * 意见反馈
     */
    private RelativeLayout ll_yijian;
    /**
     * 联系我们
     */
    private RelativeLayout ll_lianxi;
    /**
     * 使用须知
     */
    private RelativeLayout ll_shiyong;

    public SlidingClickEvent(Context context, View view) {
        this.view = view;
        this.context = context;
        initView();
    }

    /**
     * 初始化UI
     */
    private void initView() {

        mLinearLayout01 = (LinearLayout) ((Activity) context).findViewById(R.id.ll_cooperation);
        mLinearLayout02 = (LinearLayout) ((Activity) context).findViewById(R.id.ll_trading);
        mLinearLayout03 = (LinearLayout) ((Activity) context).findViewById(R.id.ll_about);

        ll_about69 = (RelativeLayout) ((Activity) context).findViewById(R.id.ll_about69);
        ll_yijian = (RelativeLayout) ((Activity) context).findViewById(R.id.ll_yijian);
        ll_lianxi = (RelativeLayout) ((Activity) context).findViewById(R.id.ll_lianxi);
        ll_shiyong = (RelativeLayout) ((Activity) context).findViewById(R.id.ll_shiyong);

        mLinearLayout01.setOnClickListener(this);
        mLinearLayout02.setOnClickListener(this);
        mLinearLayout03.setOnClickListener(this);
        mLinearLayout01.setVisibility(View.GONE);    //暂时不需要隐藏
        mLinearLayout02.setVisibility(View.GONE);
        mLinearLayout03.setVisibility(View.GONE);

        ll_about69.setOnClickListener(this);
        ll_yijian.setOnClickListener(this);
        ll_lianxi.setOnClickListener(this);
        ll_shiyong.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_cooperation:
                startNewActivity(CooperationActivity.class);
                break;
            case R.id.ll_trading:
                //加载网页
                startNewActivity(TradingActivity.class);
                break;
            case R.id.ll_about:
                startNewActivity(AboutActivity.class);
                break;

            case R.id.ll_about69:
                startNewActivity(AboutLiuJiuChengActivity.class);
                break;
            case R.id.ll_yijian:
                startNewActivity(FeedBackActivity.class);
                break;
            case R.id.ll_lianxi:
                startNewActivity(ContactActivity.class);
                break;
            case R.id.ll_shiyong:
                startNewActivity(UseInformationActivity.class);
                break;
        }
    }

    /**
     * 开启新的界面
     */
    public void startNewActivity(Class clazz) {
        Intent intent = new Intent(context, clazz);
        context.startActivity(intent);
    }
}
