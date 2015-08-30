package com.mobile.liujiucheng.main.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mobile.liujiucheng.main.adapter.MyAdapter;
import com.mobile.liujiucheng.main.bean.PandaCollectionBean;
import com.mobile.liujiucheng.main.bean.PandaSelectCollection;
import com.mobile.liujiucheng.main.bean.Retalbean;
import com.mobile.liujiucheng.main.bean.SecondHouse;
import com.mobile.liujiucheng.main.bean.base.BaseFragment;
import com.mobile.liujiucheng.main.utils.DialogUtils;
import com.mobile.liujiucheng.main.utils.GsonUtil;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.NetworkUtils;
import com.mobile.liujiucheng.main.utils.StreamTools;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.main.widget.PullToRefreshBase;
import com.mobile.liujiucheng.main.widget.PullToRefreshListView;
import com.mobile.liujiucheng.sixninecheng.main.DetailsActivity;
import com.mobile.liujiucheng.sixninecheng.main.LoagingActivity;
import com.mobile.liujiucheng.sixninecheng.main.R;
import com.mobile.liujiucheng.sixninecheng.main.SPUtils;
import com.mobile.liujiucheng.sixninecheng.main.view.PandaItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DOUjunjun on 2015/7/19.
 */
public class PandaFragment extends BaseFragment implements View.OnClickListener {

    private View mView;
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private MyAdapter adapter;
    private TextView mTextView;
    private Context context;

    private TextView mDelete;//删除房源
    private TextView mCancle;//取消删除
    private List<PandaCollectionBean> listBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = View.inflate(getActivity(),
                R.layout.layout_my_collection_second, null);

        return mView;
    }

    /**
     * 填充UI
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
        network();

    }

    private void initData() {
        context = getActivity();
        deleteView = View.inflate(context, R.layout.layout_delete_house, null);
        mDelete = (TextView) deleteView.findViewById(R.id.tv_delete);
        mCancle = (TextView) deleteView.findViewById(R.id.tv_cancal);

        mDelete.setOnClickListener(this);
        mCancle.setOnClickListener(this);
        if (listBean == null) {
            listBean = new ArrayList<PandaCollectionBean>();
        }
        mTextView = (TextView) mView.findViewById(R.id.tv_collection);
        mPullRefreshListView = (PullToRefreshListView) mView
                .findViewById(R.id.lv_content);

        mPullRefreshListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener() {
            public void onRefresh() {
                // 访问网络数据
                network();
            }
        });
        mListView = mPullRefreshListView.getRefreshableView();
    }

    private HttpUtils http;
    private String mResult;
    private DialogUtils utils;

    private void network() {
        boolean connected = NetworkUtils.isConnection(getActivity());
        if (connected) {
            // 访问网络
            utils = new DialogUtils(getActivity());
            utils.show();
            NetHead head = new NetHead(getActivity());
            RequestParams params = head.setHeader();
            params.addBodyParameter("userId", SPUtils.getUserID(getActivity()));
            if (http == null) {
                http = new HttpUtils();
            }
            http.send(HttpRequest.HttpMethod.POST,UrlsUtils.pandaCollectionList, params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            Toast.makeText(getActivity(),
                                    StreamTools.getString(), Toast.LENGTH_SHORT)
                                    .show();
                            utils.closeDialog();
                            colseHead();
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            colseHead();
                            mResult = arg0.result;
                            Log.e("TAGSSSS", "mResult==" + mResult);
                            utils.closeDialog();
                            parseJson();
                        }
                    });
            mTextView.setVisibility(View.GONE);
        } else {
            showWords("当前网络不可用,请你检查网络的情况");
        }
    }
//    PullToRefreshBase.OnRefreshListener mOnrefreshListener = new PullToRefreshBase.OnRefreshListener() {
//        public void onRefresh() {
//            // 访问网络数据
//            network();
//        }
//    };

    protected void setListAdapter() {
        if (adapter == null) {
            adapter = new MyAdapter(listBean, getActivity()) {
                @Override
                public View getView(int position, View convertView,
                                    ViewGroup parent) {
                    View view;
                    ViewHolder holder;
                    if (convertView == null) {
                        holder = new ViewHolder();
                        view = View.inflate(getActivity(),
                                R.layout.panda_lv_item, null);
                        holder.iv_panda_icon = (ImageView) view
                                .findViewById(R.id.iv_panda_icon);
                        holder.tv_panda_title = (TextView) view
                                .findViewById(R.id.tv_panda_title);
                        holder.tv_panda_floor = (TextView) view
                                .findViewById(R.id.tv_panda_floor);
                        holder.tv_panda_square = (TextView) view
                                .findViewById(R.id.tv_panda_square);
                        holder.tv_panda_rent = (TextView) view
                                .findViewById(R.id.tv_panda_rent);
                        holder.tv_panda_price = (TextView) view
                                .findViewById(R.id.tv_panda_price);
                        holder.tv_panda_name = (TextView) view
                                .findViewById(R.id.tv_panda_name);
                        holder.tv_panda_age = (TextView) view
                                .findViewById(R.id.tv_panda_age);
                        holder.tv_panda_job = (TextView) view
                                .findViewById(R.id.tv_panda_job);

                        view.setTag(holder);
                    } else {
                        view = convertView;
                        holder = (ViewHolder) view.getTag();
                    }
                    BitmapUtils bitmapUtils = new BitmapUtils(context);
                    bitmapUtils.display(holder.iv_panda_icon,listBean.get(position).getImgUrl());   //设置图片
                    holder.tv_panda_title.setText(listBean.get(position).getTitle());                //设置标题
                    holder.tv_panda_floor.setText(listBean.get(position).getRentFloor()+"/"+listBean.get(position).getFloor()+"层");                //设置层
                    holder.tv_panda_square.setText(listBean.get(position).getSpec()+"㎡");
                    holder.tv_panda_price.setText(listBean.get(position).getRent()+"元/月");       //租金
                    holder.tv_panda_name.setText(listBean.get(position).getLinkMan());              //人名
                    if(listBean.get(position).getAge().equals("null")){
                        holder.tv_panda_age.setText("");
                    }else{
                        holder.tv_panda_age.setText(listBean.get(position).getAge());               //80后
                    }
                    holder.tv_panda_job.setText("");
                    return view;
                }
            };
            mListView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(context, PandaItem.class);
                if (listBean.size() > 0) {
                    intent.putExtra("houseId", listBean.get(position - 1).getId() + "");
                    context.startActivity(intent);
                }
            }
        });
        /**
         * 长按删除收藏房源
         */
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                deleteTag = position - 1;
                deleteHouse();
                return true;
            }
        });
    }

    private PopupWindow popupWindow;
    private View deleteView;

    protected void deleteHouse() {
        if (popupWindow == null) {
            popupWindow = new PopupWindow(deleteView);
            ColorDrawable dw = new ColorDrawable(0x60000000);
            popupWindow.setBackgroundDrawable(dw);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
            popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        }
        popupWindow.showAtLocation(mView, Gravity.BOTTOM, 0, 0);
        deleteView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                deletePopwindow();
                return true;
            }
        });
    }

    private void deletePopwindow() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
    }

    static class ViewHolder {
        ImageView iv_panda_icon;       //图片
        TextView tv_panda_title;       //银枫家园朝阳酒仙桥彩虹路6号-北卧
        TextView tv_panda_floor;       //1/6层
        TextView tv_panda_square;      //20㎡
        TextView tv_panda_rent;        //合租
        TextView tv_panda_price;       //2000元/月
        TextView tv_panda_name;        //张先生
        TextView tv_panda_age;         //80后
        TextView tv_panda_job;         //人事经理
    }

    private PandaSelectCollection pandaSelectCollection = new PandaSelectCollection();

    protected void parseJson() {
        try {
            listBean.clear();
            JSONObject obj = new JSONObject(mResult);
            String msg = obj.getString("msg");
            pandaSelectCollection.setMsg(msg);
            String statuss = obj.getString("status");
            pandaSelectCollection.setStatus(statuss);
            JSONArray arr = obj.getJSONArray("data");
            for (int i = 0; i < arr.length(); i++) {
                PandaCollectionBean pandaCollectionBean = new PandaCollectionBean();
                JSONObject object = arr.getJSONObject(i);
                int id = object.getInt("id");
                pandaCollectionBean.setId(id);
                String title = object.getString("title");
                pandaCollectionBean.setTitle(title);
                String linkMan = object.getString("linkMan");
                pandaCollectionBean.setLinkMan(linkMan);
                int rent = object.getInt("rent");
                pandaCollectionBean.setRent(rent + "");
                String houseType = object.getString("houseType");
                pandaCollectionBean.setHouseType(houseType);
                int spec = object.getInt("spec");
                pandaCollectionBean.setSpec(spec + "");
                int rentFloor = object.getInt("rentFloor");
                pandaCollectionBean.setRentFloor(rentFloor);
                int floor = object.getInt("floor");
                pandaCollectionBean.setFloor(floor);
                int rentStyle = object.getInt("rentStyle");
                pandaCollectionBean.setRentStyle(rentStyle);
                String imgUrl = object.getString("imgUrl");
                pandaCollectionBean.setImgUrl(imgUrl);
                String characteristic = object.getString("characteristic");
                pandaCollectionBean.setCharacteristic(characteristic);
                String age = object.getString("age");
                pandaCollectionBean.setAge(age);
                String job = object.getString("job");
                pandaCollectionBean.setJob(job);
                listBean.add(pandaCollectionBean);
            }
            Log.e("TAGbbbbb", "listBean==" + listBean.toString());
            pandaSelectCollection.setList(listBean);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if ("N".equals(pandaSelectCollection.getStatus())) {
            showWords((String) getResources().getText(R.string.collection_house));
        } else {
            mTextView.setVisibility(View.GONE);
            setListAdapter();
        }
    }

    private void showWords(String data) {
        mTextView.setText(data);
        mTextView.setVisibility(View.GONE);
    }

    private void colseHead() {
        if (mPullRefreshListView != null) {
            mPullRefreshListView.onRefreshComplete();
        }
    }

    private int deleteTag = 0;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_delete:
                netdelete();
                deletePopwindow();
                break;
            case R.id.tv_cancal:
                deletePopwindow();
                break;
        }
    }

    private void netdelete() {
        // 判断是否登录
        String userID = SPUtils.getUserID(context);
        if (TextUtils.isEmpty(userID)) {
            Intent intent = new Intent(context, LoagingActivity.class);
            startActivity(intent);
        } else {
            boolean connection = NetworkUtils.isConnection(context);
            if (!connection) {
                showWords("当前网络不可用,请你检查网络的情况");
                return;
            }
            utils.show();
            NetHead head = new NetHead(context);
            RequestParams params = head.setHeader();
            params.addBodyParameter("type","deleteColect");
            params.addBodyParameter("houseId", listBean.get(deleteTag).getId()+"");
            Log.e("CCCCCCCCCCCC", "*******************"+listBean.get(deleteTag).getId()+"");
//            Toast.makeText(context,listBean.get(deleteTag).getId(),Toast.LENGTH_LONG).show();
            params.addBodyParameter("userId", SPUtils.getUserID(context));
            if (http == null) {
                http = new HttpUtils();
            }
            http.send(HttpRequest.HttpMethod.POST,UrlsUtils.pandaDelCollection, params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            utils.closeDialog();
                            Toast.makeText(context, "网络连接超时", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            utils.closeDialog();
                            listBean.remove(deleteTag);
                            if (listBean.size() == 0) {
                                showWords((String) getResources().getText(R.string.collection_house));
                            }
                            adapter.notifyDataSetChanged();
                            String result = arg0.result;
                            JSONObject josn = null;
                            try {
                                josn = new JSONObject(result);
                                String statue = josn.getString("status");
                                if (!"N".equals(statue)) {
                                    Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }
}
