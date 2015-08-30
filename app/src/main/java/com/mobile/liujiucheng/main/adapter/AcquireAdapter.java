package com.mobile.liujiucheng.main.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.mobile.liujiucheng.main.bean.AcquireList;
import com.mobile.liujiucheng.main.bean.RentListDataBean;
import com.mobile.liujiucheng.sixninecheng.main.R;

import java.util.List;

/**
 * Created by DOUjunjun on 2015/7/21.
 * 适配器
 */
public class AcquireAdapter extends BaseAdapter{
    private Context context;
    private List<AcquireList> datas;
    private ViewHolders holder;

    public AcquireAdapter(Context context, List<AcquireList> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.acquirelist_item, parent, false);
            convertView.setTag(new ViewHolders(convertView));
        }
        holder = ((ViewHolders) convertView.getTag());
        AcquireList acquireList = datas.get(position);
        BitmapUtils bitmapUtils = new BitmapUtils(context);
        bitmapUtils.display(holder.iv_acquire,acquireList.getImgUrl());   //设置图片
        if(acquireList.getSex().equals("男")){
            holder.iv_sex.setImageResource(R.drawable.man_icon);
        }
        if(acquireList.getSex().equals("女")){
            holder.iv_sex.setImageResource(R.drawable.woman_icon);
        }
        holder.tv_qiwang.setText("期望区域："+acquireList.getDistrict());                //设置区域
        holder.tv_xingming.setText(acquireList.getName());                //设置姓名
        holder.tv_nianling.setText(acquireList.getAge());       //设置年龄
        holder.tv_xingzuo.setText("星座："+acquireList.getConstellation());              //星座
        if(!"null".equals(acquireList.getJobName())){
            holder.tv_zhiye.setText("职业："+acquireList.getJobName());
        }else {
            holder.tv_zhiye.setText("职业：");
        }
        
        if(!"null".equals(acquireList.getNativePlace())){
            holder.tv_jiguan.setText("籍贯："+acquireList.getNativePlace());
        }else {
            holder.tv_jiguan.setText("籍贯：");
        }
        if(!"null".equals(acquireList.getTitle())){
            holder.tv_biaoti.setText(acquireList.getTitle());
        }
        return convertView;
    }

    class ViewHolders {
        ImageView iv_acquire;            //图片
        ImageView iv_sex;                //性别
        TextView tv_qiwang;              //期望区域
        TextView tv_xingming;            //姓名
        TextView tv_nianling;           //年龄
        TextView tv_xingzuo;            //星座
        TextView tv_zhiye;              //职业
        TextView tv_jiguan;             //籍贯
        TextView tv_biaoti;             //标题

        public ViewHolders(View itemView) {
            iv_acquire = ((ImageView) itemView.findViewById(R.id.iv_acquire));
            iv_sex = ((ImageView) itemView.findViewById(R.id.iv_sex));
            tv_qiwang = ((TextView) itemView.findViewById(R.id.tv_qiwang));
            tv_xingming = ((TextView) itemView.findViewById(R.id.tv_xingming));
            tv_nianling = ((TextView) itemView.findViewById(R.id.tv_nianling));
            tv_xingzuo = ((TextView) itemView.findViewById(R.id.tv_xingzuo));
            tv_zhiye = ((TextView) itemView.findViewById(R.id.tv_zhiye));
            tv_jiguan = ((TextView) itemView.findViewById(R.id.tv_jiguan));
            tv_biaoti = ((TextView) itemView.findViewById(R.id.tv_biaoti));

        }
    }
}
