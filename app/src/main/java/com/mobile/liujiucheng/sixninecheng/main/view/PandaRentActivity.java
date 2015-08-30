package com.mobile.liujiucheng.sixninecheng.main.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.view.View.OnClickListener;
import android.widget.PopupWindow;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mobile.liujiucheng.main.adapter.PandaAdapter;
import com.mobile.liujiucheng.main.bean.Citys;
import com.mobile.liujiucheng.main.bean.ParseMyJSON.Area;
import com.mobile.liujiucheng.main.adapter.MyAdapter;
import com.mobile.liujiucheng.main.bean.ParseMyJSON;
import com.mobile.liujiucheng.main.bean.ParseMyJSON.Address;
import com.mobile.liujiucheng.main.bean.PlaceBean;
import com.mobile.liujiucheng.main.bean.RentListBean;
import com.mobile.liujiucheng.main.bean.RentListDataBean;
import com.mobile.liujiucheng.main.bean.SchoolBean;
import com.mobile.liujiucheng.main.bean.Schools;
import com.mobile.liujiucheng.main.utils.AssetManagerUtils;
import com.mobile.liujiucheng.main.utils.DialogUtils;
import com.mobile.liujiucheng.main.utils.GsonUtil;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.NetworkUtils;
import com.mobile.liujiucheng.main.utils.StreamTools;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.main.widget.PullToRefreshBase;
import com.mobile.liujiucheng.main.widget.PullToRefreshListView;
import com.mobile.liujiucheng.sixninecheng.main.R;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.ActionBar.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PandaRentActivity extends Activity implements OnClickListener {

    private ImageView iv_exit;
    private LinearLayout ll_panda_position;    //区域选择
    private LinearLayout ll_panda_price;       //价格选择
    private LinearLayout ll_panda_job;         //职业选择
    private LinearLayout ll_panda_more;       //更多选择

    private View viewPosition;                //区域弹框
    private View viewPrice;                   //价格弹框
    private View viewJob;                     //职业弹框
    private View viewMore;                    //更多弹框

    private View mView;                       //加载提示框

    private PopupWindow popWindow;

    /**
     * 位置ListView
     */
    private ListView positionListView01, positionListView02, positionListView03;

    /**
     * 价格ListView
     */
    private ListView priceListView;

    /**
     * 职业ListView
     */
    private ListView jobListView01, jobListView02;

    /**
     * 更多ListView
     */
    private ListView moreListView01, moreListView02, moreListView03;

    /**
     * 区域3个集合
     */
    private List<String> positionList01, positionList02;
    private List<String> positionList03 = new ArrayList<String>();
    /**
     * 价格集合
     */
    private List<String> priceList;

    /**
     * 职业2个集合
     */
    private List<String> jobList01;

    /**
     * 更多2个集合
     */
    private List<String> moreList01;
    private List<String> moreList02 = new ArrayList<String>();

    /**
     * 记录区域的各种位置
     */
    private Map<String, String> mMap = new HashMap<String, String>();   //没多大有用

    /**
     * 记录用户选择的过滤类型
     */
    private Map<String, String> selectMap = new HashMap<String, String>();

    /**
     * 刷新的ListView
     */
    private PullToRefreshListView pullToRefreshListView;
    private ListView pullListView;
    private MainScrollListener mainScrollListener;
    private View mFootView;           //添加尾部

    /**
     * 添加的头部
     */
    private View listViewFootView;

    /**
     * ListView适配器
     */
    private MyAdapter adapter01, adapter02, adapter03, adapterPrice, adapterJob01, adapterJob02, adapterMore01, adapterMore02, adapterMore03;

    private int select_tag02 = 0;          //选择的位置2信息
    private int select_tag03 = 0;          //选择的位置3信息
    private int currentPositionPrice = 0; //记录价格的位置
    private int job01Position = 0;
    private int job02Position = 0;
    private int more01Position = 0;
    private int more02Position = 0;
    private int more03Position = 0;


    private RelativeLayout aaa;
    /**
     * SharedPreference
     */
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private String useId;

    private int pager = 1;

    private boolean isSelect = false;

    private ImageView iv_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_panda_rent);

        getUseId();
        readString();
        praseJson();
        parsePlaceJson();
        parseSchoolJson();
        initView();
        selectRequest();
        itemSelece();
    }

    private void initView() {
        aaa = ((RelativeLayout) findViewById(R.id.aaa));
        aaa.setVisibility(View.GONE);
        iv_exit = (ImageView) findViewById(R.id.iv_exit);
        iv_message = ((ImageView) findViewById(R.id.iv_message));

        ll_panda_position = ((LinearLayout) findViewById(R.id.ll_panda_position));
        ll_panda_price = ((LinearLayout) findViewById(R.id.ll_panda_price));
        ll_panda_job = ((LinearLayout) findViewById(R.id.ll_panda_job));
        ll_panda_more = ((LinearLayout) findViewById(R.id.ll_panda_more));

        //筛选按钮的过滤点击事件
        ll_panda_position.setOnClickListener(this);
        ll_panda_price.setOnClickListener(this);
        ll_panda_job.setOnClickListener(this);
        ll_panda_more.setOnClickListener(this);
        iv_exit.setOnClickListener(this);
        iv_message.setOnClickListener(this);

        viewPosition = View.inflate(this, R.layout.popwindow_area, null);
        viewPrice = View.inflate(this, R.layout.popwindow_price, null);
//        viewJob = View.inflate(this, R.layout.job_layout, null);
        viewJob = View.inflate(this, R.layout.popwindow_area, null);           //修改的
        viewMore = View.inflate(this, R.layout.more_layout, null);

        pullToRefreshListView = ((PullToRefreshListView) findViewById(R.id.lv_panda_list));
        pullListView = pullToRefreshListView.getRefreshableView();
        pullToRefreshListView.setOnRefreshListener(onRefreshListener);        //设置下拉刷新的监听事件
        mainScrollListener = new MainScrollListener();
        pullListView.setOnScrollListener(mainScrollListener);
        mFootView = View.inflate(PandaRentActivity.this, R.layout.footer, null);

        utilss = new DialogUtils(PandaRentActivity.this);
        utilss.show();   //测试不需要
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_panda_position:
                setPopWindow_panda(v, viewPosition);
                setPopClick(viewPosition, id);
                break;
            case R.id.ll_panda_price:
                setPopWindow_panda(v, viewPrice);
                setPopClick(viewPrice, id);
                break;
            case R.id.ll_panda_job:       //修改的Job
//                ddddddddd
//                getJob2();
//                setPopWindow_panda(v, viewJob);
//                setPopClick(viewJob, id);
                setPopWindow_panda(v, viewJob);
                setPopClick(viewJob, id);
                break;
            case R.id.ll_panda_more:
                setPopWindow_panda(v, viewMore);
                setPopClick(viewMore, id);
                break;
            case R.id.iv_exit:
                finish();
                break;
            case R.id.iv_message:
                //跳转到发布界面
                break;
        }
    }

    /**
     * 设置弹框的界面
     */
    @SuppressLint("InlinedApi")
    private void setPopWindow_panda(View v, View view) {
        if (popWindow == null) {
            popWindow = new PopupWindow(this);
            popWindow.setHeight(LayoutParams.MATCH_PARENT);
            popWindow.setWidth(LayoutParams.MATCH_PARENT);
            popWindow.setFocusable(true);
            popWindow.setOutsideTouchable(true);
            ColorDrawable dw = new ColorDrawable(0x60000000);
            popWindow.setBackgroundDrawable(dw);
            popWindow.setContentView(view);
            popWindow.showAsDropDown(v);
        } else {
            popWindow.setContentView(view);
            popWindow.showAsDropDown(v);
        }
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popWindow != null) {
                    popWindow.dismiss();
                }
                return true;
            }
        });
    }

    private void setPopClick(View view, int id) {
        initPopWindowView_panda(view, id);
    }

    /**
     * 实例点击选择后PopWindow的视图
     */
    private void initPopWindowView_panda(View view, int id) {
        switch (id) {
            case R.id.ll_panda_position:
                init(view, id);
                break;
            case R.id.ll_panda_price:
                initPriceData(view);
                break;
            case R.id.ll_panda_job:
//                initJob(view);
                initJobs(view);
                break;
            case R.id.ll_panda_more:
                initMore(view);
                break;
        }
    }

    private ListView jobListView001;
    private ListView jobListView002;
    private ListView jobListView003;

    private void initJobs(View view){
        jobListView001 = ((ListView) view.findViewById(R.id.lv_area));
        jobListView002 = ((ListView) view.findViewById(R.id.lv_line));
        jobListView003 = ((ListView) view.findViewById(R.id.lv_position));

        initJobData();
    }

    private List<String> jobList001 = new ArrayList<String>();
    private List<String> jobList002 = new ArrayList<String>();
    private List<String> jobList003 = new ArrayList<String>();
    private List<Integer> jobList003JobId = new ArrayList<>();

    private void initJobData() {

        if (jobList001.size() == 0) {
            try {
                JSONArray arr1 = new JSONArray(streamJob);
                for (int a = 0; a < arr1.length(); a++) {
                    JSONObject jsonObject1 = arr1.getJSONObject(a);
                    String vocation = jsonObject1.getString("vocation");
                    jobList001.add(vocation);
                }
                Log.e("LLLLLLLLLLL", jobList001.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        //给职业设置适配器
    }
        setMyJobAdapter01();

    }

    private MyAdapter jobAdapter01,jobAdapter02,jobAdapter03;
    private int positionJob01 = 0,positionJob02 = 0,positionJob03 = 0;

    private void setMyJobAdapter01() {
        jobAdapter01 = new MyAdapter(jobList001, this) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                    convertView = View.inflate(PandaRentActivity.this, R.layout.layout_text_item_area, null);
                    TextView tv = (TextView) convertView.findViewById(R.id.tv_context);
                    tv.setText(jobList001.get(position));

                    if (positionJob01 == position) {
                        tv.setTextColor(getResources().getColor(R.color.house_item_color));
                        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    } else {
                        tv.setTextColor(getResources().getColor(R.color.hidden_search_word));
                        //view.setBackgroundColor(Color.parseColor("#EEEEEE"));
                    }
                return convertView;

            }
        };
        jobListView001.setAdapter(jobAdapter01);

        jobListView001.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionJob01 = position;
                positionJob02 = 0;
                positionJob03 = 0;
                jobList002.clear();
                jobList003.clear();
                if(jobAdapter03 == null){

                }else{
                    jobAdapter03.notifyDataSetChanged();
                }
                jobAdapter01.notifyDataSetChanged();

                if(position == 0){
                   // 网络请求
                    selectMap.put("job", "");
                    isAdd = false;
                    isRefresh = false;
                    isSelect = true;
                    pager = 1;
                    popWindow.dismiss();
                    selectRequest();
                }else{
                    //进行job2的解析
                    try {
                        JSONArray array = new JSONArray(streamJob);
                        JSONObject jsonObject = array.getJSONObject(positionJob01);
                        JSONArray vocationChildList = jsonObject.getJSONArray("vocationChildList");
                        for(int i=0;i<vocationChildList.length();i++){
                            JSONObject jsonObject1 = vocationChildList.getJSONObject(i);
                            String vocationChild = jsonObject1.getString("vocationChild");
                            jobList002.add(vocationChild);
                        }
                        Log.e("OOOOOOO",jobList002.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //给job2设置适配器
                    setMyJobAdapter02();
                }

            }
        });
    }

    //job2设置适配器
    private void setMyJobAdapter02(){
        jobAdapter02 = new MyAdapter(jobList002, this) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                convertView = View.inflate(PandaRentActivity.this, R.layout.layout_text_item_area, null);
                TextView tv = (TextView) convertView.findViewById(R.id.tv_context);
                tv.setText(jobList002.get(position));          //报错了  数组越界   ？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？
//ddddddddddddddd
                if (positionJob02 == position) {
                    tv.setTextColor(getResources().getColor(R.color.house_item_color));
                    //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.hidden_search_word));
                    //view.setBackgroundColor(Color.parseColor("#EEEEEE"));
                }
                return convertView;

            }
        };
        jobListView002.setAdapter(jobAdapter02);

        jobListView002.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionJob02 = position;
                jobAdapter02.notifyDataSetChanged();

                positionJob03 = 0;

                if(position == 0){
                    // 网络请求
                    selectMap.put("job", "");
                    isAdd = false;
                    isRefresh = false;
                    isSelect = true;
                    pager = 1;
                    popWindow.dismiss();
                    selectRequest();
                }else{
                    //进行job2的解析
                    try {
                        JSONArray array = new JSONArray(streamJob);
                        jobList003.clear();
                        jobList003JobId.clear();
                        JSONObject jsonObject = array.getJSONObject(positionJob01);
                        JSONArray vocationChildList = jsonObject.getJSONArray("vocationChildList");
//                    for(int i=0;i<vocationChildList.length();i++){
                        JSONObject jsonObject1 = vocationChildList.getJSONObject(positionJob02);
                        JSONArray jobList = jsonObject1.getJSONArray("jobList");
                        for(int b=0;b<jobList.length();b++){
                            JSONObject jsonObject2 = jobList.getJSONObject(b);
                            int jobIds = jsonObject2.getInt("jobId");
                            jobList003JobId.add(jobIds);
                            String jobName = jsonObject2.getString("jobName");
                            jobList003.add(jobName);
                        }
                        Log.e("PPPPPP",jobList003.toString());
//                    }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //给job3设置适配器
                    setMyJobAdapter03();
                }
            }
        });
    }
//    ffffffff
    private void setMyJobAdapter03(){
        jobAdapter03 = new MyAdapter(jobList003, this) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                convertView = View.inflate(PandaRentActivity.this, R.layout.layout_text_item_area, null);
                TextView tv = (TextView) convertView.findViewById(R.id.tv_context);
                tv.setText(jobList003.get(position));

                if (positionJob03 == position) {
                    tv.setTextColor(getResources().getColor(R.color.house_item_color));
                    //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.hidden_search_word));
                    //view.setBackgroundColor(Color.parseColor("#EEEEEE"));
                }
                return convertView;

            }
        };
        jobListView003.setAdapter(jobAdapter03);

        jobListView003.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionJob03 = position;
                jobAdapter03.notifyDataSetChanged();

                if(position == 0){
                    // 网络请求
                    selectMap.put("job", "");
                    isAdd = false;
                    isRefresh = false;
                    isSelect = true;
                    pager = 1;
                    popWindow.dismiss();
                    selectRequest();
                }else{
                    // 网络请求
                    selectMap.put("job", jobList003JobId.get(position)+"");
                    Log.e("PPPPPPPPPPPPPPP",jobList003JobId.get(position)+"");
                    isAdd = false;
                    isRefresh = false;
                    isSelect = true;
                    pager = 1;
                    popWindow.dismiss();
                    selectRequest();
                }
//                gggggggggg
            }
        });
    }

    private void init(View view, int id) {
        positionListView01 = ((ListView) view.findViewById(R.id.lv_area));
        positionListView02 = ((ListView) view.findViewById(R.id.lv_line));
        positionListView03 = ((ListView) view.findViewById(R.id.lv_position));

        priceListView = ((ListView) view.findViewById(R.id.lv_price));
        initData();
    }

    private void initPriceData(View view) {
        priceListView = (ListView) view.findViewById(R.id.lv_price);
        priceList = new ArrayList<String>();
        priceList.add("不限");
        priceList.add("500以下");
        priceList.add("500-1000");
        priceList.add("1000-2000");
        priceList.add("2000-3000");
        priceList.add("3000以上");

        setPriceAdapter();
    }

    private void initMore(View view) {
        moreListView01 = ((ListView) view.findViewById(R.id.lv_more01));
        moreListView02 = ((ListView) view.findViewById(R.id.lv_more02));
        moreListView03 = ((ListView) view.findViewById(R.id.lv_more03));

        moreList01 = new ArrayList<String>();
        moreList01.add("居室");
        moreList01.add("兴趣");
        moreList01.add("性别");
        moreList01.add("星座");
        moreList01.add("年龄");
        moreList01.add("院校");
        moreList01.add("籍贯");

        setMore01Adapter();
    }

    private void setFirstMore02() {
        moreList02.clear();
        moreList02.add("不限");
        moreList02.add("一室");
        moreList02.add("二室");
        moreList02.add("三室");
        moreList02.add("四室");
        moreList02.add("四室以上");
    }

    private void setMore01Adapter() {        //更多listView01设置适配器

        adapterMore01 = new MyAdapter(moreList01, PandaRentActivity.this) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                convertView = View.inflate(PandaRentActivity.this, R.layout.layout_text_item_area, null);
                TextView tv = (TextView) convertView.findViewById(R.id.tv_context);
                tv.setText(moreList01.get(position));
                if (more01Position == position) {
                    tv.setTextColor(getResources().getColor(R.color.house_item_color));
                    //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.hidden_search_word));
                    //view.setBackgroundColor(Color.parseColor("#EEEEEE"));
                }
                return convertView;
            }
        };
        moreListView01.setAdapter(adapterMore01);
        if (adapterMore02 == null) {
            setFirstMore02();
            setMore02Adapter();
        } else {
            adapterMore02.notifyDataSetChanged();
        }

        moreListView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                more01Position = position;
                adapterMore01.notifyDataSetChanged();
                if (position == 0) {    //居室

                    moreListView03.setVisibility(View.INVISIBLE);
                    moreList02.clear();
                    moreList02.add("不限");
                    moreList02.add("一室");
                    moreList02.add("二室");
                    moreList02.add("三室");
                    moreList02.add("四室");
                    moreList02.add("四室以上");

                    more02Position = 0;
                    setMore02Adapter();

                }
                if (position == 1) {     //兴趣

                    moreListView03.setVisibility(View.INVISIBLE);
                    moreList02.clear();
                    moreList02.add("不限");
                    moreList02.add("美食烹饪");
                    moreList02.add("阅读写作");
                    moreList02.add("电影/音乐");
                    moreList02.add("旅行/户外");
                    moreList02.add("健康/爱好");
                    moreList02.add("时尚/艺术");
                    moreList02.add("交友/聚会");
                    moreList02.add("其它");

                    more02Position = 0;
                    setMore02Adapter();
                }
                if (position == 2) {     //性别

                    moreListView03.setVisibility(View.INVISIBLE);
                    moreList02.clear();
                    moreList02.add("不限");
                    moreList02.add("男");
                    moreList02.add("女");

                    more02Position = 0;
                    setMore02Adapter();
                }
                if (position == 3) {      //星座

                    moreListView03.setVisibility(View.INVISIBLE);
                    moreList02.clear();
                    moreList02.add("不限");
                    moreList02.add("水瓶座");
                    moreList02.add("双鱼座");
                    moreList02.add("白羊座");
                    moreList02.add("金牛座");
                    moreList02.add("双子座");
                    moreList02.add("巨蟹座");
                    moreList02.add("狮子座");
                    moreList02.add("处女座");
                    moreList02.add("天秤座");
                    moreList02.add("天蝎座");
                    moreList02.add("射手座");
                    moreList02.add("摩羯座");

                    more02Position = 0;
                    setMore02Adapter();
                }
                if (position == 4) {      //年龄

                    moreListView03.setVisibility(View.INVISIBLE);
                    moreList02.clear();
                    moreList02.add("不限");
                    moreList02.add("00后");
                    moreList02.add("95后");
                    moreList02.add("90后");
                    moreList02.add("85后");
                    moreList02.add("80后");
                    moreList02.add("75后");
                    moreList02.add("70后");

                    more02Position = 0;
                    setMore02Adapter();
                }

                if (position == 5) {      //院校
                    yuanxiao = true;
                    jiguan = false;
                    more03Position = 0;
                    moreListView03.setVisibility(View.VISIBLE);
                    moreList02.clear();
                    moreList02.addAll(school_privaceList);
                    setMore023Adapter();
                }
                if (position == 6) {      //籍贯
                    jiguan = true;
                    yuanxiao = false;
                    more02Position = 0;
                    moreListView03.setVisibility(View.VISIBLE);
                    moreList02.clear();
                    moreList02.addAll(privaceList);
                    setMore022Adapter();
                }
            }
        });
    }
    private boolean yuanxiao = false;
    private boolean jiguan = false;
    private void setMore02Adapter() {
        adapterMore02 = new MyAdapter(moreList02, PandaRentActivity.this) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = View.inflate(PandaRentActivity.this, R.layout.layout_text_item, null);
                TextView tv = (TextView) convertView.findViewById(R.id.tv_context);
                tv.setText(moreList02.get(position));
                if (more02Position == position) {
                    tv.setTextColor(getResources().getColor(R.color.house_item_color));
                    //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.hidden_search_word));
                    //view.setBackgroundColor(Color.parseColor("#EEEEEE"));
                }
                return convertView;
            }
        };
        moreListView02.setAdapter(adapterMore02);
        moreListView02.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                more02Position = position;
                adapterMore02.notifyDataSetChanged();
                if (more01Position == 0) {
                    if (position != 0) {
                        selectMap.put("houseType", moreList02.get(position));  //
                        selectMap.put("interest", "");
                        selectMap.put("sex", "");
                        selectMap.put("constellation", "");
                        selectMap.put("age", "");
                        selectMap.put("school", "");
                        selectMap.put("nativePlace", "");
                        popWindow.dismiss();
                        pager = 1;
                        isAdd = false;
                        isRefresh = false;
                        isSelect = true;
                        listData.clear();
                        selectRequest();
                    }
                    if (position == 0) {
                        selectMap.put("houseType", "");  //
                        selectMap.put("interest", "");
                        selectMap.put("sex", "");
                        selectMap.put("constellation", "");
                        selectMap.put("age", "");
                        selectMap.put("school", "");
                        selectMap.put("nativePlace", "");
                        popWindow.dismiss();
                        pager = 1;
                        isAdd = false;
                        isRefresh = false;
                        isSelect = true;
                        listData.clear();
                        selectRequest();
                    }
//                    Toast.makeText(PandaRentActivity.this, moreList02.get(position), Toast.LENGTH_LONG).show();
                }
                if (more01Position == 1) {
                    if (position != 0) {
                        selectMap.put("interest", moreList02.get(position));  //
                        selectMap.put("houseType", "");
                        selectMap.put("sex", "");
                        selectMap.put("constellation", "");
                        selectMap.put("age", "");
                        selectMap.put("school", "");
                        selectMap.put("nativePlace", "");
                        popWindow.dismiss();
                        pager = 1;
                        isAdd = false;
                        isRefresh = false;
                        isSelect = true;
                        listData.clear();
                        selectRequest();
                    }
                    if (position == 0) {
                        selectMap.put("interest", "");  //
                        selectMap.put("houseType", "");
                        selectMap.put("sex", "");
                        selectMap.put("constellation", "");
                        selectMap.put("age", "");
                        selectMap.put("school", "");
                        selectMap.put("nativePlace", "");
                        popWindow.dismiss();
                        pager = 1;
                        isAdd = false;
                        isRefresh = false;
                        isSelect = true;
                        listData.clear();
                        selectRequest();
                    }
                }
                if (more01Position == 2) {
                    if (position != 0) {
                        selectMap.put("sex", moreList02.get(position));       //
                        selectMap.put("interest", "");
                        selectMap.put("houseType", "");
                        selectMap.put("constellation", "");
                        selectMap.put("age", "");
                        selectMap.put("school", "");
                        selectMap.put("nativePlace", "");
                        popWindow.dismiss();
                        pager = 1;
                        isAdd = false;
                        isRefresh = false;
                        isSelect = true;
                        listData.clear();
                        selectRequest();
                    }
                    if (position == 0) {
                        selectMap.put("sex", "");       //
                        selectMap.put("interest", "");
                        selectMap.put("houseType", "");
                        selectMap.put("constellation", "");
                        selectMap.put("age", "");
                        selectMap.put("school", "");
                        selectMap.put("nativePlace", "");
                        popWindow.dismiss();
                        pager = 1;
                        isAdd = false;
                        isRefresh = false;
                        isSelect = true;
                        listData.clear();
                        selectRequest();
                    }
                }
                if (more01Position == 3) {
                    if (position != 0) {
                        selectMap.put("constellation", moreList02.get(position));       //
                        selectMap.put("sex", "");
                        selectMap.put("interest", "");
                        selectMap.put("houseType", "");
                        selectMap.put("age", "");
                        selectMap.put("school", "");
                        selectMap.put("nativePlace", "");
                        popWindow.dismiss();
                        pager = 1;
                        isAdd = false;
                        isRefresh = false;
                        isSelect = true;
                        listData.clear();
                        selectRequest();
                    }
                    if (position == 0) {
                        selectMap.put("constellation", "");       //
                        selectMap.put("sex", "");
                        selectMap.put("interest", "");
                        selectMap.put("houseType", "");
                        selectMap.put("age", "");
                        selectMap.put("school", "");
                        selectMap.put("nativePlace", "");
                        popWindow.dismiss();
                        pager = 1;
                        isAdd = false;
                        isRefresh = false;
                        isSelect = true;
                        listData.clear();
                        selectRequest();
                    }

                }
                if (more01Position == 4) {
                    if (position != 0) {
                        selectMap.put("age", moreList02.get(position));      //
                        selectMap.put("constellation", "");
                        selectMap.put("sex", "");
                        selectMap.put("interest", "");
                        selectMap.put("houseType", "");
                        selectMap.put("school", "");
                        selectMap.put("nativePlace", "");
                        popWindow.dismiss();
                        pager = 1;
                        isAdd = false;
                        isRefresh = false;
                        isSelect = true;
                        listData.clear();
                        selectRequest();
                    }
                    if (position == 0) {
                        selectMap.put("age", "");      //
                        selectMap.put("constellation", "");
                        selectMap.put("sex", "");
                        selectMap.put("interest", "");
                        selectMap.put("houseType", "");
                        selectMap.put("school", "");
                        selectMap.put("nativePlace", "");
                        popWindow.dismiss();
                        pager = 1;
                        isAdd = false;
                        isRefresh = false;
                        isSelect = true;
                        listData.clear();
                        selectRequest();
                    }
                }
            }
        });
    }

    private int moreTag002;

    private void setMore022Adapter() {                          //籍贯适配器
        adapterMore02 = new MyAdapter(moreList02, PandaRentActivity.this) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = View.inflate(PandaRentActivity.this, R.layout.layout_text_item, null);
                TextView tv = (TextView) convertView.findViewById(R.id.tv_context);
                tv.setText(moreList02.get(position));
                if (more02Position == position) {    //省的位置
                    tv.setTextColor(getResources().getColor(R.color.house_item_color));
                    //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.hidden_search_word));
                    //view.setBackgroundColor(Color.parseColor("#EEEEEE"));
                }
                return convertView;
            }
        };
        moreListView02.setAdapter(adapterMore02);
        listCity.clear();
        listSchool.clear();
        setMore03Adapter();       //清楚3数据


        moreListView02.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                more02Position = position;
                adapterMore02.notifyDataSetChanged();

                if (position == 0) {
//                    listCity.clear();
                    setMore03Adapter();
                    //网络请求g
                    listData.clear();
                    popWindow.dismiss();
                    pager = 1;
                    selectRequest();

                } else {
                    shi = 0;
                    listCity.clear();
                    listCity.add("不限");
                    for (int i = 0; i < placeBeans.get(position - 1).getCity().size(); i++) {
                        listCity.add(placeBeans.get(position - 1).getCity().get(i).getCityName());      //将省底下的市的信息填充到listCity集合中
                    }
                    setMore03Adapter();
                }
            }
        });
    }

    private int shi = 0;
    //获取籍贯”省“底下的“市”的信息   placeBeans
    private List<String> listCity = new ArrayList<String>();

    private void setMore03Adapter() {
        adapterMore03 = new MyAdapter(listCity, PandaRentActivity.this) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = View.inflate(PandaRentActivity.this, R.layout.layout_text_item, null);
                TextView tv = (TextView) convertView.findViewById(R.id.tv_context);
                tv.setText(listCity.get(position));
                if (shi == position) {
                    tv.setTextColor(getResources().getColor(R.color.house_item_color));
                    //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.hidden_search_word));
                    //view.setBackgroundColor(Color.parseColor("#EEEEEE"));
                }
                return convertView;
            }
        };
        moreListView03.setAdapter(adapterMore03);

        if(jiguan){
            moreListView03.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                    shi = position;
                    setMore03Adapter();

                    //进行网络请求
                    if (position == 0) {
                        selectMap.put("nativePlace", "");
                        selectMap.put("houseType", "");  //
                        selectMap.put("interest", "");
                        selectMap.put("sex", "");
                        selectMap.put("constellation", "");
                        selectMap.put("age", "");
                        selectMap.put("school","");

                        popWindow.dismiss();
                        pager = 1;
                        isAdd = false;
                        isRefresh = false;
                        isSelect = true;
                        listData.clear();
                        selectRequest();
                    } else {
//                        schoolBeans.get(more03Position - 1).getSchool().get(position - 1).getSchoolId();   //有问题
                        int nativePlace = placeBeans.get(more02Position - 1).getCity().get(position - 1).getCityId();
                        selectMap.put("nativePlace", nativePlace + "");
                        selectMap.put("houseType", "");  //
                        selectMap.put("interest", "");
                        selectMap.put("sex", "");
                        selectMap.put("constellation", "");
                        selectMap.put("age", "");
                        selectMap.put("school","");

                        popWindow.dismiss();
                        pager = 1;
                        isAdd = false;
                        isRefresh = false;
                        isSelect = true;
                        listData.clear();
                        selectRequest();
//               hhhhhh
//                        Toast.makeText(PandaRentActivity.this, nativePlace + "", Toast.LENGTH_LONG).show();
//                ffff
//                Toast.makeText(PandaRentActivity.this,listSchool.get(position),Toast.LENGTH_LONG).show();
                    }
                }

            });
        }
    }

    //获取籍贯”省“底下的“院校”的信息   SchoolBeans
    private List<String> listSchool = new ArrayList<String>();

    private void setMore023Adapter() {                          //院校适配器
        adapterMore02 = new MyAdapter(moreList02, PandaRentActivity.this) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = View.inflate(PandaRentActivity.this, R.layout.layout_text_item, null);
                TextView tv = (TextView) convertView.findViewById(R.id.tv_context);
                tv.setText(moreList02.get(position));
                if (more03Position == position) {
                    tv.setTextColor(getResources().getColor(R.color.house_item_color));
                    //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.hidden_search_word));
                    //view.setBackgroundColor(Color.parseColor("#EEEEEE"));
                }
                return convertView;
            }
        };
        moreListView02.setAdapter(adapterMore02);
        listSchool.clear();
        listCity.clear();
        setMore03Adapter();

        moreListView02.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                more03Position = position;
                more04Position = 0;
                selectMap.put("school", "");
                setMore032Adapter();
                adapterMore02.notifyDataSetChanged();

                if (position == 0) {
                    listSchool.clear();
                    setMore03Adapter();

                    //进行网络请求
                    listData.clear();
                    popWindow.dismiss();
                    pager = 1;
                    selectRequest();

                } else {
                    listSchool.clear();
                    listSchool.add("不限");
                    for (int i = 0; i < schoolBeans.get(position - 1).getSchool().size(); i++) {
                        listSchool.add(schoolBeans.get(position - 1).getSchool().get(i).getSchoolName());      //将省底下的市的信息填充到listCity集合中
                    }
                    setMore032Adapter();
                }
            }
        });
    }

    private int more04Position = 0;

    private void setMore032Adapter() {         //院校适配器
        adapterMore03 = new MyAdapter(listSchool, PandaRentActivity.this) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = View.inflate(PandaRentActivity.this, R.layout.layout_text_item, null);
                TextView tv = (TextView) convertView.findViewById(R.id.tv_context);
                tv.setText(listSchool.get(position));
                if (more04Position == position) {
                    tv.setTextColor(getResources().getColor(R.color.house_item_color));
                    //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.hidden_search_word));
                    //view.setBackgroundColor(Color.parseColor("#EEEEEE"));
                }
                return convertView;
            }
        };
        moreListView03.setAdapter(adapterMore03);
        if(yuanxiao){
        moreListView03.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                more04Position = position;
                setMore032Adapter();

                //进行网络请求
                if (position == 0) {
                    selectMap.put("school", "");
                    selectMap.put("nativePlace", "");
                    selectMap.put("houseType", "");  //
                    selectMap.put("interest", "");
                    selectMap.put("sex", "");
                    selectMap.put("constellation", "");
                    selectMap.put("age", "");
                    popWindow.dismiss();
                    pager = 1;
                    isAdd = false;
                    isRefresh = false;
                    isSelect = true;
                    listData.clear();
                    selectRequest();
                } else {
                    int schoolId = schoolBeans.get(more03Position - 1).getSchool().get(position - 1).getSchoolId();   //有问题

                    selectMap.put("school", schoolId + "");
                    selectMap.put("nativePlace", "");
                    selectMap.put("houseType", "");  //
                    selectMap.put("interest", "");
                    selectMap.put("sex", "");
                    selectMap.put("constellation", "");
                    selectMap.put("age", "");

                    popWindow.dismiss();
                    pager = 1;
                    isAdd = false;
                    isRefresh = false;
                    isSelect = true;
                    listData.clear();
                    selectRequest();
//               hhhhhh
//                    Toast.makeText(PandaRentActivity.this, schoolId + "", Toast.LENGTH_LONG).show();
//                ffff
//                Toast.makeText(PandaRentActivity.this,listSchool.get(position),Toast.LENGTH_LONG).show();
                }
            }
        });}
    }

    private void initJob(View view) {
        jobListView01 = ((ListView) view.findViewById(R.id.lv_job01));
        jobListView02 = ((ListView) view.findViewById(R.id.lv_job02));
        jobList01 = new ArrayList<String>();
        jobList01.add("不限");
        jobList01.add("人事/行政/高级管理");
        jobList01.add("会计/金融/银行/保险");
        jobList01.add("公务员/翻译/其他");
        jobList01.add("咨询/法律/教育/科研");
        jobList01.add("广告/市场/媒体/艺术");
        jobList01.add("建筑/房地产");
        jobList01.add("物流/仓储");
        jobList01.add("生产/营运/采购/物流");
        jobList01.add("计算机/互联网/通信/电子");
        jobList01.add("销售/客服/技术支持");

        setJobAdapter01();
    }

    private void setJobAdapter01() {        //设置职位ListView01的适配器

        adapterJob01 = new MyAdapter(jobList01, PandaRentActivity.this) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                convertView = View.inflate(PandaRentActivity.this, R.layout.layout_text_item_area, null);
                TextView tv = (TextView) convertView.findViewById(R.id.tv_context);
                tv.setText(jobList01.get(position));
                if (job01Position == position) {
                    tv.setTextColor(getResources().getColor(R.color.house_item_color));
                    //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else {
                    tv.setTextColor(getResources().getColor(R.color.hidden_search_word));
                    //view.setBackgroundColor(Color.parseColor("#EEEEEE"));
                }
                return convertView;
            }
        };
        jobListView01.setAdapter(adapterJob01);

        jobListView01.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                job01Position = position;
                adapterJob01.notifyDataSetChanged();
                job02Position = 0;
                if (adapterJob02 != null) {
                    adapterJob02.notifyDataSetChanged();
                }
                if (position != 0) {
                    List<Map<String, String>> listever = listJob22.get(position - 1);
                    listName = new ArrayList<String>();
                    listName.clear();
                    for (int i = 0; i < listever.size(); i++) {
                        listName.add(listever.get(i).get("jobName"));
                    }
                    setJobAdapter2();
                } else {
                    if (listName != null) {
                        listName.clear();
                    }
                    selectMap.put("job", "");
                    isAdd = false;
                    isRefresh = false;
                    isSelect = true;
                    pager = 1;
                    popWindow.dismiss();
                    selectRequest();
                }
            }
        });
    }

    List<String> listName;

    private void setJobAdapter2() {
        adapterJob02 = new MyAdapter(listName, this) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v;
                ViewHolder holder;
                if (convertView == null) {
                    holder = new ViewHolder();
                    v = View.inflate(PandaRentActivity.this, R.layout.layout_text_item_area, null);
                    holder.mTextView = (TextView) v
                            .findViewById(R.id.tv_context);
                    v.setTag(holder);
                } else {
                    v = convertView;
                    holder = (ViewHolder) v.getTag();
                }
                holder.mTextView.setText(listName.get(position));
                if (job02Position == position) {
                    holder.mTextView.setTextColor((PandaRentActivity.this.getResources().getColor(R.color.house_item_color)));
                } else {
                    holder.mTextView.setTextColor((PandaRentActivity.this.getResources().getColor(R.color.hidden_search_word)));
                }
                return v;
            }
        };
        jobListView02.setAdapter(adapterJob02);

        jobListView02.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                job02Position = position;
                adapterJob02.notifyDataSetChanged();

                selectMap.put("job", listJob22.get(job01Position - 1).get(position).get("jobId"));
//                Toast.makeText(PandaRentActivity.this, listJob22.get(job01Position - 1).get(position).get("jobName"), Toast.LENGTH_LONG).show();
                isAdd = false;
                isRefresh = false;
                isSelect = true;
                popWindow.dismiss();
                pager = 1;
                selectRequest();

            }
        });
    }

    private void initData() {
        positionList01 = new ArrayList<String>();
        positionList02 = new ArrayList<String>();     //记录位置：朝阳等。。。

        positionList01.add("区域");

        positionList02.add("不限");
        positionList02.add("朝阳");
        positionList02.add("海淀");
        positionList02.add("东城");
        positionList02.add("西城");
        positionList02.add("崇文");
        positionList02.add("宣武");
        positionList02.add("丰台");
        positionList02.add("通州");
        positionList02.add("石景山");
        positionList02.add("昌平");
        positionList02.add("大兴");
        positionList02.add("延庆");
        positionList02.add("平谷");
        positionList02.add("燕郊");
        positionList02.add("北京周边");
        positionList02.add("房山");
        positionList02.add("顺义");
        positionList02.add("密云");
        positionList02.add("怀柔");
        positionList02.add("门头沟");

        setMyAdapter01();
        setMyAdapter02();

        positionListView02.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positionList03.clear();
                select_tag02 = position;
                if (position != 0) {
                    for (int i = 0; i < mAList.get(position).bussinessareaList.size(); i++) {
                        positionList03.add(mAList.get(position).bussinessareaList.get(i).name);
                    }
                    select_tag03 = 0;
                    setMyAdapter03();
                } else {                                                    //position=0   不限时
                    selectMap.put("district", "");
                    selectMap.put("bussiness", "");
                    isSelect = true;
                    isAdd = false;
                    isRefresh = false;
                    listData.clear();
                    popWindow.dismiss();
                    pager = 1;
                    selectRequest();

                }
                adapter02.notifyDataSetChanged();
            }
        });

        positionListView03.setOnItemClickListener(new AdapterView.OnItemClickListener() {                      //区域3ListView的点击事件
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                select_tag03 = position;
                setMyAdapter03();
                //进行请求数据
                if (position == 0) {
                    selectMap.put("bussiness", "");
                } else {
                    selectMap.put("bussiness", positionList03.get(position));
                }
//                Toast.makeText(PandaRentActivity.this, positionList03.get(position), Toast.LENGTH_LONG).show();
                popWindow.dismiss();
                isSelect = true;
                isAdd = false;
                isRefresh = false;
                pager = 1;
                selectRequest();

            }
        });
    }

    private void setMyAdapter01() {
        adapter01 = new MyAdapter(positionList01, this) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = View.inflate(PandaRentActivity.this, R.layout.layout_text_item_area, null);
                    TextView tv = (TextView) convertView.findViewById(R.id.tv_context);
                    tv.setText(positionList01.get(position));
                    tv.setTextColor(getResources().getColor(R.color.house_item_color));
                }
                return convertView;

            }
        };
        positionListView01.setAdapter(adapter01);
    }

    public class ViewHolder {
        TextView mTextView;
    }

    private void setMyAdapter02() {
        adapter02 = new MyAdapter(positionList02, this) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View v;
                ViewHolder holder;
                if (convertView == null) {
                    holder = new ViewHolder();
                    v = View.inflate(PandaRentActivity.this, R.layout.layout_text_item_area, null);
                    holder.mTextView = (TextView) v
                            .findViewById(R.id.tv_context);
                    v.setTag(holder);
                } else {
                    v = convertView;
                    holder = (ViewHolder) v.getTag();
                }
                holder.mTextView.setText(positionList02.get(position));
                if (select_tag02 == position) {
                    holder.mTextView.setTextColor((PandaRentActivity.this.getResources().getColor(R.color.house_item_color)));
                } else {
                    holder.mTextView.setTextColor((PandaRentActivity.this.getResources().getColor(R.color.hidden_search_word)));
                }
                return v;
            }
        };
        positionListView02.setAdapter(adapter02);
    }

    private void setMyAdapter03() {
        if (adapter03 == null) {
            adapter03 = new MyAdapter(positionList03, this) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View v;
                    ViewHolder holder;
                    if (convertView == null) {
                        holder = new ViewHolder();
                        v = View.inflate(PandaRentActivity.this, R.layout.layout_text_item_area, null);
                        holder.mTextView = (TextView) v
                                .findViewById(R.id.tv_context);
                        v.setTag(holder);
                    } else {
                        v = convertView;
                        holder = (ViewHolder) v.getTag();
                    }
                    holder.mTextView.setText(positionList03.get(position));
                    if (select_tag03 == position) {
                        holder.mTextView.setTextColor((PandaRentActivity.this.getResources().getColor(R.color.house_item_color)));
                    } else {
                        holder.mTextView.setTextColor((PandaRentActivity.this.getResources().getColor(R.color.hidden_search_word)));
                    }
                    return v;
                }
            };
            positionListView03.setAdapter(adapter03);
        } else {
            adapter03.notifyDataSetChanged();
        }
    }


    private List<Address> mPlist;
    private String stream, streamJob;

    private void readString() {                      //读出address.json的字符串
        InputStream is = AssetManagerUtils.getData("address.json", PandaRentActivity.this);
        stream = StreamTools.readStream(is);

        InputStream iss = AssetManagerUtils.getData("tjob.json", PandaRentActivity.this);
        streamJob = StreamTools.readStream(iss);
    }


    private ParseMyJSON bean;
    private List<Area> mAList;

    private void praseJson() {                     //解析address.json读出的字符串
        bean = GsonUtil.json2Bean(stream, ParseMyJSON.class);
        String name = bean.city.get(0).name;// 北京
        mMap.put("beijing", name);
        mAList = bean.city.get(0).tdistrict;                                                     //城市集合
//        Toast.makeText(PandaRentActivity.this, mAList.get(1).bussinessareaList.get(1).name, Toast.LENGTH_LONG).show();
    }


    private void setPriceAdapter() {
        if (adapterPrice == null) {
            adapterPrice = new MyAdapter(priceList, PandaRentActivity.this) {
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    View view;
                    if (convertView == null) {
                        view = View.inflate(PandaRentActivity.this, R.layout.layout_text_item, null);
                    } else {
                        view = convertView;
                    }
                    TextView tv = (TextView) view.findViewById(R.id.tv_context);
                    tv.setText(priceList.get(position));
                    if (currentPositionPrice == position) {
                        tv.setTextColor(getResources().getColor(R.color.house_item_color));
                        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    } else {
                        tv.setTextColor(getResources().getColor(R.color.hidden_search_word));
                        //view.setBackgroundColor(Color.parseColor("#EEEEEE"));
                    }
                    return view;
                }
            };
            priceListView.setAdapter(adapterPrice);
        } else {
            adapterPrice.notifyDataSetChanged();
        }
        priceListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                currentPositionPrice = position;
                adapterPrice.notifyDataSetChanged();
                if (position == 0) {
                    selectMap.put("rent", "");
                }
                if (position == 1) {
                    selectMap.put("rent", "0-500");
                }
                if (position == 2) {
                    selectMap.put("rent", "500-1000");
                }
                if (position == 3) {
                    selectMap.put("rent", "1000-2000");
                }
                if (position == 4) {
                    selectMap.put("rent", "2000-3000");
                }
                if (position == 5) {
                    selectMap.put("rent", "3000");
                }
                popWindow.dismiss();
                pager = 1;
                isAdd = false;
                isRefresh = false;
                isSelect = true;
                listData.clear();
                selectRequest();
            }
        });
    }

    List<Map<String, String>> listJob21;
    List<List<Map<String, String>>> listJob22 = new ArrayList<>();
    Map<String, String> mapJob;

    private void getJob2() {
        listJob22.clear();

        mapJob = new HashMap<String, String>();       //人事/行政/高级管理
        listJob21 = new ArrayList<>();

        mapJob.put("jobId", "0");
        mapJob.put("jobName", "不限");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "39");
        mapJob.put("jobName", "人力资源");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "40");
        mapJob.put("jobName", "高级管理");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "41");
        mapJob.put("jobName", "行政/后勤");
        listJob21.add(mapJob);
        listJob22.add(listJob21);

        mapJob = new HashMap<String, String>();       //会计/金融/银行/保险
        listJob21 = new ArrayList<>();

        mapJob.put("jobId", "0");
        mapJob.put("jobName", "不限");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "14");
        mapJob.put("jobName", "财务/审计/税务");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "15");
        mapJob.put("jobName", "证券/金融/投资");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "16");
        mapJob.put("jobName", "银行");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "17");
        mapJob.put("jobName", "保险");
        listJob21.add(mapJob);
        listJob22.add(listJob21);

        mapJob = new HashMap<String, String>();       //公务员/翻译/其他
        listJob21 = new ArrayList<>();

        mapJob.put("jobId", "0");
        mapJob.put("jobName", "不限");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "54");
        mapJob.put("jobName", "翻译");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "55");
        mapJob.put("jobName", "在校学生");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "56");
        mapJob.put("jobName", "储备干部/培训生/实习生");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "57");
        mapJob.put("jobName", "兼职 其他");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "58");
        mapJob.put("jobName", "环保");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "59");
        mapJob.put("jobName", "农/林/牧/渔");
        listJob21.add(mapJob);
        listJob22.add(listJob21);

        mapJob = new HashMap<String, String>();       //咨询/法律/教育/科研
        listJob21 = new ArrayList<>();

        mapJob.put("jobId", "0");
        mapJob.put("jobName", "不限");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "42");
        mapJob.put("jobName", "咨询/顾问");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "43");
        mapJob.put("jobName", "律师/法务/合规");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "44");
        mapJob.put("jobName", "教师");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "45");
        mapJob.put("jobName", "培训");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "46");
        mapJob.put("jobName", "科研人员");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "47");
        mapJob.put("jobName", "服务业");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "48");
        mapJob.put("jobName", "餐饮/娱乐");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "49");
        mapJob.put("jobName", "酒店/旅游");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "50");
        mapJob.put("jobName", "美容/健身/体育");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "51");
        mapJob.put("jobName", "百货/连锁/零售服务");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "52");
        mapJob.put("jobName", "交通运输服务");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "53");
        mapJob.put("jobName", "保安/家政/其他服务");
        listJob21.add(mapJob);
        listJob22.add(listJob21);

        mapJob = new HashMap<String, String>();       //广告/市场/媒体/艺术
        listJob21 = new ArrayList<>();

        mapJob.put("jobId", "0");
        mapJob.put("jobName", "不限");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "30");
        mapJob.put("jobName", "广告");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "31");
        mapJob.put("jobName", "公关/媒介");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "32");
        mapJob.put("jobName", "市场/营销");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "33");
        mapJob.put("jobName", "影视/媒体");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "34");
        mapJob.put("jobName", "写作/出版/印刷");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "35");
        mapJob.put("jobName", "艺术/设计");
        listJob21.add(mapJob);
        listJob22.add(listJob21);

        mapJob = new HashMap<String, String>();       //建筑/房地产
        listJob21 = new ArrayList<>();

        mapJob.put("jobId", "0");
        mapJob.put("jobName", "不限");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "36");
        mapJob.put("jobName", "建筑工程");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "37");
        mapJob.put("jobName", "房地产");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "38");
        mapJob.put("jobName", "物业管理");
        listJob21.add(mapJob);
        listJob22.add(listJob21);

        mapJob = new HashMap<String, String>();       //物流/仓储
        listJob21 = new ArrayList<>();

        mapJob.put("jobId", "0");
        mapJob.put("jobName", "不限");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "26");
        mapJob.put("jobName", "生物/制药/医疗/护理");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "27");
        mapJob.put("jobName", "生物/制药/医疗器械");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "28");
        mapJob.put("jobName", "化工");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "29");
        mapJob.put("jobName", "医院/医疗/护理");
        listJob21.add(mapJob);
        listJob22.add(listJob21);

        mapJob = new HashMap<String, String>();       //生产/营运/采购/物流
        listJob21 = new ArrayList<>();

        mapJob.put("jobId", "0");
        mapJob.put("jobName", "不限");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "18");
        mapJob.put("jobName", "生产/营运");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "19");
        mapJob.put("jobName", "质量/安全管理");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "20");
        mapJob.put("jobName", "工程/机械/能源");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "21");
        mapJob.put("jobName", "汽车");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "22");
        mapJob.put("jobName", "技工");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "23");
        mapJob.put("jobName", "服装/纺织/皮革");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "24");
        mapJob.put("jobName", "采购");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "25");
        mapJob.put("jobName", "贸易");
        listJob21.add(mapJob);
        listJob22.add(listJob21);

        mapJob = new HashMap<String, String>();       //计算机/互联网/通信/电子
        listJob21 = new ArrayList<>();

        mapJob.put("jobId", "0");
        mapJob.put("jobName", "不限");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "1");
        mapJob.put("jobName", "计算机硬件");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "2");
        mapJob.put("jobName", "计算机软件");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "5");
        mapJob.put("jobName", "IT-管理");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "6");
        mapJob.put("jobName", "互联网开发及应用");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "7");
        mapJob.put("jobName", "IT-品管、技术支持及其它");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "8");
        mapJob.put("jobName", "通信技术");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "9");
        mapJob.put("jobName", "电子/电器/半导体/仪器仪表");
        listJob21.add(mapJob);
        listJob22.add(listJob21);

        mapJob = new HashMap<String, String>();       //销售/客服/技术支持
        listJob21 = new ArrayList<>();

        mapJob.put("jobId", "0");
        mapJob.put("jobName", "不限");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "10");
        mapJob.put("jobName", "销售管理");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "11");
        mapJob.put("jobName", "销售人员");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "12");
        mapJob.put("jobName", "销售行政及商务");
        listJob21.add(mapJob);
        mapJob = new HashMap<String, String>();
        mapJob.put("jobId", "13");
        mapJob.put("jobName", "客服及技术支持");
        listJob21.add(mapJob);
        listJob22.add(listJob21);
    }

    /**
     * 解析place.json到实体类
     */
    private String placeString;
    private List<PlaceBean> placeBeans;
    private List<String> privaceList = new ArrayList<String>();

    private void parsePlaceJson() {
        InputStream is = AssetManagerUtils.getData("place.json", PandaRentActivity.this);
        placeString = StreamTools.readStream(is);

        try {
            JSONArray array = new JSONArray(placeString);
            placeBeans = new ArrayList<PlaceBean>();
            PlaceBean placeBean = null;
            for (int i = 0; i < array.length(); i++) {
                placeBean = new PlaceBean();
                JSONObject obj = array.getJSONObject(i);
                String privaceName = obj.getString("provinceName");
                placeBean.setPrivaceName(privaceName);
                JSONArray array2 = obj.getJSONArray("city");
                List<Citys> cityList = new ArrayList<Citys>();
                for (int j = 0; j < array2.length(); j++) {
                    JSONObject obb = array2.getJSONObject(j);
                    Citys city = new Citys();
                    city.setCityId(obb.getInt("provinceCityId"));
                    city.setCityName(obb.getString("provinceCityName"));

                    cityList.add(city);
                }
                placeBean.setCity(cityList);
                placeBeans.add(placeBean);
            }

            privaceList.add("不限");
            for (int k = 0; k < placeBeans.size(); k++) {
                privaceList.add(placeBeans.get(k).getPrivaceName());
            }
            System.out.println("*********>" + privaceList.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 解析schoollist.json到实体类
     */
    private String schoolString;
    private List<SchoolBean> schoolBeans;
    private List<String> school_privaceList = new ArrayList<String>();

    private void parseSchoolJson() {
        InputStream is = AssetManagerUtils.getData("schoollist.json", PandaRentActivity.this);
        schoolString = StreamTools.readStream(is);

        try {
            JSONArray array = new JSONArray(schoolString);
            schoolBeans = new ArrayList<SchoolBean>();
            SchoolBean schoolBean = null;
            for (int i = 0; i < array.length(); i++) {
                schoolBean = new SchoolBean();
                JSONObject obj = array.getJSONObject(i);
                String privaceName = obj.getString("provinceName");
                schoolBean.setProvinceName(privaceName);
                JSONArray array2 = obj.getJSONArray("school");
                List<Schools> schoolList = new ArrayList<Schools>();
                for (int j = 0; j < array2.length(); j++) {
                    JSONObject obb = array2.getJSONObject(j);
                    Schools school = new Schools();
                    school.setSchoolId(obb.getInt("schoolId"));
                    school.setSchoolName(obb.getString("schoolName"));

                    schoolList.add(school);
                }
                schoolBean.setSchool(schoolList);
                schoolBeans.add(schoolBean);
            }

            school_privaceList.add("不限");
            for (int k = 0; k < schoolBeans.size(); k++) {
                school_privaceList.add(schoolBeans.get(k).getProvinceName());
            }
            System.out.println("*********>" + privaceList.size());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断网络的连接情况
     */
    private boolean isConnection() {
        int type = NetworkUtils.getConnctedType(PandaRentActivity.this);
        if (type == -1) {
            Toast.makeText(this, this.getResources().getText(R.string.net_connection), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private int jobId;  //存放职业Id的

    private int parseInt(String str) {              //将String类型的数据转化成int型的
        int i = Integer.parseInt(str);
        return i;
    }

    /**
     * 关闭下拉刷新的转圈圈
     */
    private void closeHeader() {
        if (pullToRefreshListView != null) {
            pullToRefreshListView.onRefreshComplete();
        }
    }

    private boolean isRefresh;                //下拉刷新

    PullToRefreshBase.OnRefreshListener onRefreshListener = new PullToRefreshBase.OnRefreshListener() {
        @Override
        public void onRefresh() {

            //进行访问网络数据

            pager = 1;
            isRefresh = true;
            isAdd = false;
            isSelect = false;

            selectRequest();
//            Toast.makeText(PandaRentActivity.this, "nnnnnn", Toast.LENGTH_LONG).show();
        }
    };

    private RequestParams getHeader() {
        NetHead head = new NetHead(PandaRentActivity.this);
        RequestParams params = head.setHeader();
        params.addBodyParameter("city", "北京");
        return params;
    }

    /**
     * 访问网络
     */
    private HttpUtils http;
    private DialogUtils utilss;
    private String mResult = "";


    private void selectRequest() {                     //选择请求

        isRequesting = true;
        boolean connection = isConnection();
        if (!connection) {
            return;
        }
        if (!isAdd && !isRefresh) {
            utilss.show();
        }
        if (isAdd) {
            pullListView.addFooterView(mFootView);
        }
        if (isRefresh) {
            listData.clear();
        }
        if (isSelect) {
            listData.clear();
        }
        NetHead head = new NetHead(PandaRentActivity.this);
        RequestParams params = head.setHeader();
        params.addBodyParameter("userId", useId);
        params.addBodyParameter("city", "北京");
        params.addBodyParameter("district", selectMap.get("district"));
        params.addBodyParameter("bussiness", selectMap.get("bussiness"));
        params.addBodyParameter("houseType", selectMap.get("houseType"));
        params.addBodyParameter("rent", selectMap.get("rent"));
        params.addBodyParameter("job", selectMap.get("job"));
        params.addBodyParameter("interest", selectMap.get("interest"));
        params.addBodyParameter("sex", selectMap.get("sex"));
        params.addBodyParameter("age", selectMap.get("age"));
        params.addBodyParameter("constellation", selectMap.get("constellation"));
        params.addBodyParameter("nativePlace", selectMap.get("nativePlace"));
        params.addBodyParameter("school", selectMap.get("school"));
        params.addBodyParameter("page", pager + "");
        if (http == null) {
            http = new HttpUtils();
        }
        http.send(HttpRequest.HttpMethod.POST, UrlsUtils.pandaList, params, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                closeHeader();
                pullListView.removeFooterView(mFootView);
                utilss.closeDialog();
                mResult = arg0.result;
                Log.e("TAG00", "mResult =" + mResult);
                parseMain2Bean(mResult);
                isRequesting = false;

            }

            @Override
            public void onFailure(HttpException e, String s) {
                utilss.closeDialog();
                pullListView.removeFooterView(mFootView);
            }
        });
    }

    /**
     * 主ListView的下拉加载
     */
    private boolean isAdd = false;

    private class MainScrollListener implements AbsListView.OnScrollListener {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            isRefresh = false;
            if (first + count == total && scrollState == SCROLL_STATE_IDLE) {
                if (!isRequesting) {
                    pager++;

                    isAdd = true;
                    isSelect = false;
                    isRefresh = false;

                    selectRequest();
                } else {
                    Toast.makeText(PandaRentActivity.this, "正在请求数据", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            first = firstVisibleItem;
            count = visibleItemCount;
            total = totalItemCount;
        }
    }

    private int first;
    private int count;
    private int total;
    private boolean isRequesting = false;

    /**
     * 获取SharedPreference里面的userId
     */
    private void getUseId() {
        sp = PandaRentActivity.this.getSharedPreferences("load", MODE_PRIVATE);
//        editor = sp.edit();
        useId = sp.getString("userID", "");
//        Toast.makeText(PandaRentActivity.this, "userID" + useId, Toast.LENGTH_LONG).show();
    }

    /**
     * 对主ListView的数据进行解析
     */

    private List<RentListDataBean> listData = new ArrayList<RentListDataBean>();
    private RentListBean rentListBean;  //保存数据的实体类

    private void parseMain2Bean(String str) {
        try {
            JSONObject object01 = new JSONObject(str);
            rentListBean = new RentListBean();
            String msg = object01.getString("msg");
            if (msg.equals("未查询到任何信息")) {
//                listData.clear();        //有问题
                Toast.makeText(PandaRentActivity.this, "没有更多信息", Toast.LENGTH_LONG).show();
            } else {
                rentListBean.setMsg(msg);
                String status = object01.getString("status");
                rentListBean.setStatus(status);
                String error = object01.getString("error");
                rentListBean.setError(error);
                JSONArray datass = object01.getJSONArray("data");

                for (int i = 0; i < datass.length(); i++) {
                    RentListDataBean rentListDataBean = new RentListDataBean();
                    JSONObject object02 = datass.getJSONObject(i);
                    int id = object02.getInt("id");  //
                    rentListDataBean.setId(id);
                    String title = object02.getString("title");
                    rentListDataBean.setTitle(title);
                    String linkMan = object02.getString("linkMan");
                    rentListDataBean.setLinkMan(linkMan);
//                String linkPhone = object02.getString("linkPhone");
//                rentListDataBean.setLinkPhone(linkPhone);
                    int rent = object02.getInt("rent");  //
                    rentListDataBean.setRent(rent + "");
//                String payStyle = object02.getString("payStyle");
//                rentListDataBean.setPayStyle(payStyle);
//                String pubTime = object02.getString("pubTime");
//                rentListDataBean.setPubTime(pubTime);
//                String status1 = object02.getString("status");
//                rentListDataBean.setStatus(status1);
                    String houseType = object02.getString("houseType");
                    rentListDataBean.setHouseType(houseType);
                    double spec = object02.getDouble("spec");    //
                    rentListDataBean.setSpec(spec + "");
                    int rentFloor = object02.getInt("rentFloor");
                    rentListDataBean.setRentFloor(rentFloor);
                    int floor = object02.getInt("floor");
                    rentListDataBean.setFloor(floor);
                    int rentStyle = object02.getInt("rentStyle");
                    rentListDataBean.setRentStyle(rentStyle);
//                String tenementType = object02.getString("tenementType");
//                rentListDataBean.setTenementType(tenementType);
//                String interiordesign = object02.getString("interiordesign");
//                rentListDataBean.setInteriordesign(interiordesign);
//                String config = object02.getString("config");
//                rentListDataBean.setConfig(config);
//                String aspect = object02.getString("aspect");
//                rentListDataBean.setAspect(aspect);
//                String description = object02.getString("description");
//                rentListDataBean.setDescription(description);
//                String address = object02.getString("address");
//                rentListDataBean.setAddress(address);
//                String city = object02.getString("city");
//                rentListDataBean.setCity(city);
//                String district = object02.getString("district");
//                rentListDataBean.setTenement(district);
//                String tenement = object02.getString("tenement");
//                rentListDataBean.setTenement(tenement);
//                String bussinessarea = object02.getString("bussinessarea");
//                rentListDataBean.setBussinessarea(bussinessarea);
//                String infoSource = object02.getString("infoSource");
//                rentListDataBean.setInfoSource(infoSource);
//                String enterStatus = object02.getString("enterStatus");
//                rentListDataBean.setEnterStatus(enterStatus);
//                String tuserId = object02.getString("tuserId");
//                rentListDataBean.setTuserId(tuserId);
//                String isDeal = object02.getString("isDeal");
//                rentListDataBean.setIsDeal(isDeal);
//                String subTime = object02.getString("subTime");
//                rentListDataBean.setSubTime(subTime);
//                String isIndex = object02.getString("isIndex");
//                rentListDataBean.setIsIndex(isIndex);
//                String idTop = object02.getString("idTop");
//                rentListDataBean.setIsIndex(idTop);
//                String zuobiaoX = object02.getString("zuobiaoX");
//                rentListDataBean.setZuobiaoX(zuobiaoX);
//                String zuobiaoY = object02.getString("zuobiaoY");
//                rentListDataBean.setZuobiaoY(zuobiaoY);
//                String rejectReason = object02.getString("rejectReason");
//                rentListDataBean.setRejectReason(rejectReason);
//                String phoneInfo = object02.getString("phoneInfo");
//                rentListDataBean.setPhoneInfo(phoneInfo);
//                String phoneType = object02.getString("phoneType");
//                rentListDataBean.setPhoneType(phoneType);
                    String click = object02.getString("click");
                    rentListDataBean.setClick(click);
                    String imgUrl = object02.getString("imgUrl");
                    rentListDataBean.setImgUrl(imgUrl);
//                String imgPath = object02.getString("imgPath");
//                rentListDataBean.setImgPath(imgPath);
                    String num = object02.getString("num");
                    rentListDataBean.setNum(num);
//                String indexFlag = object02.getString("indexFlag");
//                rentListDataBean.setIndexFlag(indexFlag);
                    String characteristic = object02.getString("characteristic");
                    rentListDataBean.setCharacteristic(characteristic);
                    String zuobiaoXD = object02.getString("zuobiaoXD");
//                rentListDataBean.setZuobiaoXD(zuobiaoXD);
//                String zuobiaoYD = object02.getString("zuobiaoYD");
//                rentListDataBean.setZuobiaoYD(zuobiaoYD);
//                String startX = object02.getString("startX");
//                rentListDataBean.setStartX(startX);
//                String endX = object02.getString("endX");
//                rentListDataBean.setEndX(endX);
//                String startY = object02.getString("startY");
//                rentListDataBean.setStartY(startY);
//                String endY = object02.getString("endY");
//                rentListDataBean.setEndY(endY);
//                String pageNo = object02.getString("pageNo");
//                rentListDataBean.setPageNo(pageNo);
                    String pageSize = object02.getString("pageSize");
//                rentListDataBean.setPageSize(pageSize);
//                String minRent = object02.getString("minRent");
//                rentListDataBean.setMinRent(minRent);
//                String maxRent = object02.getString("maxRent");
//                rentListDataBean.setMaxRent(maxRent);
//                String minSpec = object02.getString("minSpec");
//                rentListDataBean.setMinSpec(minSpec);
//                String maxSpec = object02.getString("maxSpec");
//                rentListDataBean.setMaxSpec(maxSpec);
//                String houseTypeValue = object02.getString("houseTypeValue");
//                rentListDataBean.setHouseTypeValue(houseTypeValue);
//                String sumHouse = object02.getString("sumHouse");
//                rentListDataBean.setSumHouse(sumHouse);
//                String sort = object02.getString("sort");
//                rentListDataBean.setSort(sort);
//                String order = object02.getString("order");
//                rentListDataBean.setOrder(order);
//                String countOfTel = object02.getString("countOfTel");
//                rentListDataBean.setCountOfTel(countOfTel);
//                String imageList = object02.getString("imageList");
//                rentListDataBean.setImageList(imageList);
//                String bidid = object02.getString("bidid");
//                rentListDataBean.setBidid(bidid);
//                String listTenant = object02.getString("listTenant");
//                rentListDataBean.setListTenant(listTenant);
                    String age = object02.getString("age");
                    rentListDataBean.setAge(age);
                    String job = object02.getString("job");
                    rentListDataBean.setJob(job);
                    listData.add(rentListDataBean);
                }
                rentListBean.setData(listData);

                Log.e("DDDD", "listData" + isSelect + listData.toString());
            }
            if (pandaAdapter == null) {
                pandaAdapter = new PandaAdapter(PandaRentActivity.this, listData);
                pullListView.setAdapter(pandaAdapter);
            } else {
                pandaAdapter.notifyDataSetChanged();

                Log.e("AAAAAA", "************" + listData.toString());
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private PandaAdapter pandaAdapter;


    /**
     * item的点击事件
     */
    private void itemSelece() {
        pullListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Toast.makeText(PandaRentActivity.this, "点过了", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(PandaRentActivity.this, PandaItem.class);
                if (listData.size() > 0) {
                    intent.putExtra("houseId", listData.get(position - 1).getId() + "");
                    PandaRentActivity.this.startActivity(intent);
                }
            }
        });
    }
//解析job
}