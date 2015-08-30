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
import com.mobile.liujiucheng.main.bean.AcquireCollectionBean;
import com.mobile.liujiucheng.main.bean.AcquireCollectionListBean;
import com.mobile.liujiucheng.main.bean.base.BaseFragment;
import com.mobile.liujiucheng.main.utils.DialogUtils;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.NetworkUtils;
import com.mobile.liujiucheng.main.utils.StreamTools;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.main.widget.PullToRefreshBase;
import com.mobile.liujiucheng.main.widget.PullToRefreshListView;
import com.mobile.liujiucheng.sixninecheng.main.LoagingActivity;
import com.mobile.liujiucheng.sixninecheng.main.R;
import com.mobile.liujiucheng.sixninecheng.main.SPUtils;
import com.mobile.liujiucheng.sixninecheng.main.view.AcquireItemActivity;
import com.mobile.liujiucheng.sixninecheng.main.view.PandaItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DOUjunjun on 2015/7/22.
 */
public class AcquireFragment extends BaseFragment implements View.OnClickListener{

    private View mView;
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private MyAdapter adapter;
    private TextView mTextView;
    private Context context;

    private TextView mDelete;//删除房源
    private TextView mCancle;//取消删除
//    private List<PandaCollectionBean> listBean;
    private List<AcquireCollectionListBean> acquireLists;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        if (acquireLists == null) {
//            listBean = new ArrayList<PandaCollectionBean>();
            acquireLists = new ArrayList<AcquireCollectionListBean>();
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
//            params.addBodyParameter("userId", 137+"");    //测试用
            if (http == null) {
                http = new HttpUtils();
            }
            http.send(HttpRequest.HttpMethod.POST, UrlsUtils.getAcquireCollectionList, params,
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
            adapter = new MyAdapter(acquireLists, getActivity()) {
                @Override
                public View getView(int position, View convertView,
                                    ViewGroup parent) {
                    View view;
                    ViewHolder holder;
                    if (convertView == null) {
                        holder = new ViewHolder();
                        view = View.inflate(getActivity(),
                                R.layout.acquirelist_item,null);
                        holder.iv_acquire = ((ImageView) view.findViewById(R.id.iv_acquire));
                        holder.iv_sex = ((ImageView) view.findViewById(R.id.iv_sex));
                        holder.tv_qiwang = ((TextView) view.findViewById(R.id.tv_qiwang));
                        holder.tv_xingming = ((TextView) view.findViewById(R.id.tv_xingming));
                        holder.tv_nianling = ((TextView) view.findViewById(R.id.tv_nianling));
                        holder.tv_xingzuo = ((TextView) view.findViewById(R.id.tv_xingzuo));
                        holder.tv_zhiye = ((TextView) view.findViewById(R.id.tv_zhiye));
                        holder.tv_jiguan = ((TextView) view.findViewById(R.id.tv_jiguan));
                        holder.tv_biaoti = ((TextView) view.findViewById(R.id.tv_biaoti));
                        view.setTag(holder);
                    } else {
                        view = convertView;
                        holder = (ViewHolder) view.getTag();
                    }
                    AcquireCollectionListBean acquireCollectionListBean = acquireLists.get(position);
                    BitmapUtils bitmapUtils = new BitmapUtils(context);
                    bitmapUtils.display(holder.iv_acquire,acquireCollectionListBean.getImgUrl());   //设置图片
                    if(acquireCollectionListBean.getSex().equals("男")){
                        holder.iv_sex.setImageResource(R.drawable.man_icon);
                    }
                    if(acquireCollectionListBean.getSex().equals("女")){
                        holder.iv_sex.setImageResource(R.drawable.woman_icon);
                    }
                    holder.tv_qiwang.setText("期望区域："+acquireCollectionListBean.getDistrict());                //设置区域
                    holder.tv_xingming.setText(acquireCollectionListBean.getName());                               //设置姓名
                    holder.tv_nianling.setText(acquireCollectionListBean.getAge());       //设置年龄
                    holder.tv_xingzuo.setText("星座："+acquireCollectionListBean.getConstellation());              //星座
                    holder.tv_zhiye.setText("职业："+acquireCollectionListBean.getJobName());
                    holder.tv_jiguan.setText("籍贯："+acquireCollectionListBean.getNativePlace());
                    holder.tv_biaoti.setText(acquireCollectionListBean.getTitle());

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
                Intent intent = new Intent(context, AcquireItemActivity.class);
                if (acquireLists.size() > 0) {
                    intent.putExtra("tenantId", acquireLists.get(position - 1).getId() + "");
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
        ImageView iv_acquire;            //图片
        ImageView iv_sex;                //性别
        TextView tv_qiwang;              //期望区域
        TextView tv_xingming;            //姓名
        TextView tv_nianling;           //年龄
        TextView tv_xingzuo;            //星座
        TextView tv_zhiye;              //职业
        TextView tv_jiguan;             //籍贯
        TextView tv_biaoti;             //标题
    }

    private AcquireCollectionBean acquireCollectionBean = new AcquireCollectionBean();

    protected void parseJson() {
        try {
            acquireLists.clear();
            JSONObject obj = new JSONObject(mResult);
            String msg = obj.getString("msg");
            acquireCollectionBean.setMsg(msg);
            String statuss = obj.getString("status");
            acquireCollectionBean.setStatus(statuss);
            JSONArray arr = obj.getJSONArray("data");
            for (int i = 0; i < arr.length(); i++) {
                AcquireCollectionListBean acquireCollectionListBean = new AcquireCollectionListBean();
                JSONObject object = arr.getJSONObject(i);
                int id = object.getInt("id");
                acquireCollectionListBean.setId(id);
                String code = object.getString("code");
                acquireCollectionListBean.setCode(code);
                String nativePlace = object.getString("nativePlace");
                acquireCollectionListBean.setNativePlace(nativePlace);
                String constellation = object.getString("constellation");
                acquireCollectionListBean.setConstellation(constellation);
                String jobName = object.getString("jobName");
                acquireCollectionListBean.setJobName(jobName);
                String sex = object.getString("sex");
                acquireCollectionListBean.setSex(sex);
                String district = object.getString("district");
                acquireCollectionListBean.setDistrict(district);
                String imgUrl = object.getString("imgUrl");
                acquireCollectionListBean.setImgUrl(imgUrl);
                String city = object.getString("city");
                acquireCollectionListBean.setCity(city);
                String age = object.getString("age");
                acquireCollectionListBean.setAge(age);
                String interest = object.getString("interest");
                acquireCollectionListBean.setInterest(interest);
                String rent = object.getString("rent");
                acquireCollectionListBean.setRent(rent);
                String checkInTimeStr = object.getString("checkInTimeStr");
                acquireCollectionListBean.setCheckInTimeStr(checkInTimeStr);
                String schoolName = object.getString("schoolName");
                acquireCollectionListBean.setSchoolName(schoolName);
                String name = object.getString("name");
                acquireCollectionListBean.setName(name);
                String title = object.getString("title");
                acquireCollectionListBean.setTitle(title);
                acquireLists.add(acquireCollectionListBean);
            }
            Log.e("TAGbbbbb", "acquireLists==" + acquireLists.toString());
            acquireCollectionBean.setList(acquireLists);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if ("N".equals(acquireCollectionBean.getStatus())) {
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
            params.addBodyParameter("userId", SPUtils.getUserID(context));
//            params.addBodyParameter("userId", 137 + "");              //测试用
            params.addBodyParameter("tenantId", acquireLists.get(deleteTag).getId() + "");
//            params.addBodyParameter("userId", SPUtils.getUserID(context));
            if (http == null) {
                http = new HttpUtils();
            }
            http.send(HttpRequest.HttpMethod.POST, UrlsUtils.acquireDelCollection, params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            utils.closeDialog();
                            Toast.makeText(context, "网络连接超时", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            utils.closeDialog();
                            acquireLists.remove(deleteTag);
                            if (acquireLists.size() == 0) {
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
