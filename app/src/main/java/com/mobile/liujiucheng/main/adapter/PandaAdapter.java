package com.mobile.liujiucheng.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.mobile.liujiucheng.main.bean.RentListDataBean;
import com.mobile.liujiucheng.sixninecheng.main.R;

import java.util.List;

/**
 * Created by DOUjunjun on 2015/7/17.
 */
public class PandaAdapter extends BaseAdapter {

    private Context context;
    private List<RentListDataBean> datas;
    private ViewHolders holder;

    public PandaAdapter(Context context, List<RentListDataBean> datas) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.panda_lv_item, parent, false);
            convertView.setTag(new ViewHolders(convertView));
        }
        holder = ((ViewHolders) convertView.getTag());
        RentListDataBean rentListDataBean = datas.get(position);
//        fffffffffffffff
        BitmapUtils bitmapUtils = new BitmapUtils(context);
        holder.iv_panda_icon = ((ImageView) convertView.findViewById(R.id.iv_panda_icon));
        bitmapUtils.display(holder.iv_panda_icon,rentListDataBean.getImgUrl());   //设置图片
        holder.tv_panda_title = ((TextView) convertView.findViewById(R.id.tv_panda_title));
        holder.tv_panda_title.setText(rentListDataBean.getTitle());                //设置标题
        holder.tv_panda_floor = ((TextView) convertView.findViewById(R.id.tv_panda_floor));
        holder.tv_panda_floor.setText(rentListDataBean.getRentFloor()+"/"+rentListDataBean.getFloor()+"层");                //设置层
        holder.tv_panda_square = ((TextView) convertView.findViewById(R.id.tv_panda_square));
        holder.tv_panda_square.setText(rentListDataBean.getSpec()+"㎡");
        holder.tv_panda_rent = ((TextView) convertView.findViewById(R.id.tv_panda_rent));
//                        holder.tv_panda_rent.setText(rentListBean.getData().get(position).get);                        //租房的类型暂时没有
        holder.tv_panda_price = ((TextView) convertView.findViewById(R.id.tv_panda_price));
        holder.tv_panda_price.setText(rentListDataBean.getRent()+"元/月");       //租金
        holder.tv_panda_name = ((TextView) convertView.findViewById(R.id.tv_panda_name));
        holder.tv_panda_name.setText(rentListDataBean.getLinkMan());              //人名
        holder.tv_panda_age = ((TextView) convertView.findViewById(R.id.tv_panda_age));
        if(rentListDataBean.getAge().equals("null")){
            holder.tv_panda_age.setText("");
        }else{
            holder.tv_panda_age.setText(rentListDataBean.getAge());               //80后
        }
        holder.tv_panda_job = ((TextView) convertView.findViewById(R.id.tv_panda_job));               //房主的类型没有写，没有数据
        holder.tv_panda_job.setText("");

        return convertView;
    }

    class ViewHolders {
        ImageView iv_panda_icon;       //图片
        TextView tv_panda_title;       //银枫家园朝阳酒仙桥彩虹路6号-北卧
        TextView tv_panda_floor;       //1/6层
        TextView tv_panda_square;      //20㎡
        TextView tv_panda_rent;        //合租
        TextView tv_panda_price;       //2000元/月
        TextView tv_panda_name;        //张先生
        TextView tv_panda_age;         //80后
        TextView tv_panda_job;         //人事经理

        public ViewHolders(View itemView) {
            iv_panda_icon = ((ImageView) itemView.findViewById(R.id.iv_panda_icon));
            tv_panda_title = ((TextView) itemView.findViewById(R.id.tv_panda_title));
            tv_panda_floor = ((TextView) itemView.findViewById(R.id.tv_panda_floor));
            tv_panda_square = ((TextView) itemView.findViewById(R.id.tv_panda_square));
            tv_panda_rent = ((TextView) itemView.findViewById(R.id.tv_panda_rent));
            tv_panda_price = ((TextView) itemView.findViewById(R.id.tv_panda_price));
            tv_panda_name = ((TextView) itemView.findViewById(R.id.tv_panda_name));
            tv_panda_age = ((TextView) itemView.findViewById(R.id.tv_panda_age));
            tv_panda_job = ((TextView) itemView.findViewById(R.id.tv_panda_job));               //房主的类型没有写，没有数据


        }
    }
}
