package com.mobile.liujiucheng.sixninecheng.main.view;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mobile.liujiucheng.main.bean.ImageBean;
import com.mobile.liujiucheng.main.bean.ItemBean;
import com.mobile.liujiucheng.main.bean.ListTenant;
import com.mobile.liujiucheng.main.fragment.VpFragment;
import com.mobile.liujiucheng.main.utils.DialogUtils;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.NetworkUtils;
import com.mobile.liujiucheng.main.utils.StreamTools;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.sixninecheng.main.DetailMapActivity;
import com.mobile.liujiucheng.sixninecheng.main.LoagingActivity;
import com.mobile.liujiucheng.sixninecheng.main.R;
import com.mobile.liujiucheng.sixninecheng.main.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PandaItem extends FragmentActivity implements View.OnClickListener {

    private String houseId;
    private DialogUtils dialogUtils;
    private String mResult = "";
//    private int chang = 0;
//    private int tuchang = 0;
    public static List<String> listImgUrl = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_panda_item);

        initView();
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            houseId = bundle.getString("houseId");
//            Toast.makeText(PandaItem.this, "houseId" + houseId, Toast.LENGTH_LONG).show();
            boolean b = isConnection();
            // 访问网络
            if (b) {
                mDialog();
                requestNet(houseId);
                if (isFirst) {
                    isFirstCollection();
                }
            }
        }
    }

    private void mDialog() {
        dialogUtils = new DialogUtils(PandaItem.this);
        dialogUtils.show();
    }

    /**
     * 进行网络请求
     */
    private void requestNet(String id) {
        NetHead head = new NetHead(PandaItem.this);
        RequestParams params = head.setHeader();
        params.addBodyParameter("houseId", id);
//        params.addBodyParameter("houseId", 389098 + "");           //测试改了
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlsUtils.pandaDetail, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Toast.makeText(PandaItem.this, StreamTools.getString(),
                                Toast.LENGTH_SHORT).show();
                        if (dialogUtils != null) {
                            dialogUtils.closeDialog();
                        }
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        if (dialogUtils != null) {
                            dialogUtils.closeDialog();
                        }
                        mResult = arg0.result;
                        Log.e("TAG************", "mResult==" + mResult);
                        parse2Json(mResult);
                    }
                });
    }

    /**
     * 进行解析
     */
    private ItemBean itemBean;
    private List<String> listimg2 = new ArrayList<>();

    private void parse2Json(String str) {
        try {
            itemBean = new ItemBean();
            JSONObject object01 = new JSONObject(str);
            String msg = object01.getString("msg");
            itemBean.setMsg(msg);
            String status = object01.getString("status");
            itemBean.setStatus(status);
            JSONObject data = object01.getJSONObject("data");
            int id = data.getInt("id");
            itemBean.setId(id);
            String title = data.getString("title");
            itemBean.setTitle(title);
            String linkMan = data.getString("linkMan");
            itemBean.setLinkMan(linkMan);
            int rent = data.getInt("rent");
            itemBean.setRent(rent);
            int payStyle = data.getInt("payStyle");
            itemBean.setPayStyle(payStyle);
            String linkPhone = data.getString("linkPhone");
            itemBean.setLinkPhone(linkPhone);
//            int pubTime = data.getInt("pubTime");
//            itemBean.setPubTime(pubTime);
            int statuss = data.getInt("status");
            itemBean.setStatuss(statuss);
            String houseType = data.getString("houseType");
            itemBean.setHouseType(houseType);
            int spec = data.getInt("spec");
            itemBean.setSpec(spec);
            int rentFloor = data.getInt("rentFloor");
            itemBean.setRentFloor(rentFloor);
            int floor = data.getInt("floor");
            itemBean.setFloor(floor);
            int rentStyle = data.getInt("rentStyle");
            itemBean.setRentStyle(rentStyle);
//            int tenementType = data.getInt("tenementType");
//            itemBean.setTenementType(tenementType);
//            String interiordesign = data.getString("interiordesign");
//            itemBean.setInteriordesign(interiordesign);
            String config = data.getString("config");
            itemBean.setConfig(config);
            String aspect = data.getString("aspect");
            itemBean.setAspect(aspect);
            String description = data.getString("description");
            itemBean.setDescription(description);
            String address = data.getString("address");
            itemBean.setAddress(address);
            String city = data.getString("city");
            itemBean.setCity(city);
            String district = data.getString("district");
            itemBean.setDistrict(district);
//            String tenement = data.getString("tenement");
//            itemBean.setTenement(tenement);
            String bussinessarea = data.getString("bussinessarea");
            itemBean.setBussinessarea(bussinessarea);
//            int infoSource = data.getInt("infoSource");
//            itemBean.setInfoSource(infoSource);
//            int enterStatus = data.getInt("enterStatus");
//            itemBean.setEnterStatus(enterStatus);
//            int isDeal = data.getInt("isDeal");
//            itemBean.setIsDeal(isDeal);
//            int isIndex = data.getInt("isIndex");
//            itemBean.setIsIndex(isIndex);
//            int isTop = data.getInt("isTop");
//            itemBean.setIsTop(isTop);
            String zuobiaoX = data.getString("zuobiaoX");
            itemBean.setZuobiaoX(zuobiaoX);
            String zuobiaoY = data.getString("zuobiaoY");
            itemBean.setZuobiaoY(zuobiaoY);

            JSONArray arr = data.getJSONArray("listTenant");

            List<ListTenant> listTenants = new ArrayList<>();
            for (int a = 0; a < arr.length(); a++) {
                ListTenant listTenant = new ListTenant();
                JSONObject jsonObject = arr.getJSONObject(a);
                int idid = jsonObject.getInt("id");
                listTenant.setId(idid);
                String sex = jsonObject.getString("sex");
                listTenant.setSex(sex);
                String age = jsonObject.getString("age");
                listTenant.setAge(age);
                String constellation = jsonObject.getString("constellation");
                listTenant.setConstellation(constellation);
                String jobName = jsonObject.getString("jobName");
                listTenant.setJobName(jobName);
                String nativePlaceName = jsonObject.getString("nativePlaceName");
                listTenant.setNativePlaceName(nativePlaceName);
                String imgUrl = jsonObject.getString("imgUrl");
                if (!TextUtils.isEmpty(imgUrl)) {
                    listimg2.add(imgUrl);
                }
                listTenant.setImgUrl(imgUrl);
                listTenants.add(listTenant);
            }

            itemBean.setImgList(listTenants);

            JSONArray imageList = data.getJSONArray("imageList");
            listImgUrl.clear();
            for (int j = 0; j < imageList.length(); j++) {
                String image = imageList.getString(j);
//                tuchang++;
                ImageBean imageBean = new ImageBean();
                imageBean.setImg(image);
                listImgUrl.add(image);
            }

            Log.e("IIIIIII", "listImgUrl==" + listImgUrl.toString());
//            List<ListTenant> listTenants = new ArrayList<ListTenant>();  //注意这是集合
//            for (int i = 0; i < arr.length(); i++) {
//                chang = arr.length();
//                ListTenant listTenant = new ListTenant();
//                int idid = arr.getJSONObject(i).getInt("id");
//                listTenant.setId(idid);
//                String sex = arr.getJSONObject(i).getString("sex");
//                listTenant.setSex(sex);
//                String age = arr.getJSONObject(i).getString("age");
//                listTenant.setAge(age);
//                String constellation = arr.getJSONObject(i).getString("constellation");
//                listTenant.setConstellation(constellation);
//                String jobName = arr.getJSONObject(i).getString("jobName");
//                listTenant.setJobName(jobName);
//                String nativePlaceName = arr.getJSONObject(i).getString("nativePlaceName");
//                listTenant.setNativePlaceName(nativePlaceName);
//                String imgUrl = arr.getJSONObject(i).getString("imgUrl");
//
//
////                if (!TextUtils.isEmpty(imgUrl)) {
////                    tuchang++;
////                    listImgUrl.add(imgUrl);
////                }
//
//                listTenant.setImgUrl(imgUrl);
//                listTenants.add(listTenant);
//            }
            for (int j = 0; j < listImgUrl.size(); j++) {
                listZongDian.get(j).setVisibility(View.VISIBLE);
                listDian.add(listZongDian.get(j));
            }
            itemBean.setImgList(listTenants);
//            String phoneInfo = data.getString("phoneInfo");
//            itemBean.setPhoneInfo(phoneInfo);
//            int phoneType = data.getInt("phoneType");
//            itemBean.setPhoneType(phoneType);
//            int click = data.getInt("click");
//            itemBean.setClick(click);
//            String imgUrl = data.getString("imgUrl");
//            itemBean.setImgUrl(imgUrl);
//            int num = data.getInt("num");
//            itemBean.setNum(num);
//            String bidid = data.getString("bidid");
//            itemBean.setBidid(bidid);
            Log.e("sssss", "itemBean==" + itemBean.toString());
//            Toast.makeText(PandaItem.this,"itemBean++++++++++++++++++"+itemBean.toString(),Toast.LENGTH_LONG).show();
            setData();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private ImageView iv_main;          //主图片
    private ImageView iv_back;
    private ImageView iv_fengxiang;
    private LinearLayout ll_collection;
    private TextView tv_address;       //地址
    private ImageView iv_collection;   //收藏
    private TextView tv_price;         //租价
    private TextView tv_huxing;        //户型
    private TextView tv_mianji;        //面积
    private TextView tv_yangtai;      //阳台     没数据
    private TextView tv_weishengjian;//卫生间   没数据
    private TextView tv_louceng;      //楼层
    private TextView tv_chaoxiang;    //朝向
    private TextView tv_fukuan;       //付款
    private TextView tv_my_telphone; //电话     还没做
    private TextView tv_my_telphone2; //电话
    private TextView tv_address2;    //地址2
    //注意：合租室友还没有写
    private TextView tv_peizhi;      //配置
    private TextView tv_xiangqing;   //详情
    private ImageView iv_map;         //地图
    private ImageView iv_message;    //消息
    private ImageView iv_call;       //电话
    private LinearLayout linearLayout;
    private ViewPager viewPager;

    private ImageView iv1, iv2, iv3, iv4, iv5, iv6;
    private List<ImageView> listDian = new ArrayList<>();
    private List<ImageView> listZongDian;

    private void initView() {
        linearLayout = ((LinearLayout) findViewById(R.id.huadong));
        viewPager = (ViewPager) findViewById(R.id.main_view_pager);
        listZongDian = new ArrayList<>();
        iv1 = ((ImageView) findViewById(R.id.iv_one));
        listZongDian.add(iv1);
        iv2 = ((ImageView) findViewById(R.id.iv_two));
        listZongDian.add(iv2);
        iv3 = ((ImageView) findViewById(R.id.iv_three));
        listZongDian.add(iv3);
        iv4 = ((ImageView) findViewById(R.id.iv_four));
        listZongDian.add(iv4);
        iv5 = ((ImageView) findViewById(R.id.iv_five));
        listZongDian.add(iv5);
        iv6 = ((ImageView) findViewById(R.id.iv_six));
        listZongDian.add(iv6);

//        iv_main = ((ImageView) findViewById(R.id.iv_main));
        iv_back = ((ImageView) findViewById(R.id.iv_back));
        iv_fengxiang = ((ImageView) findViewById(R.id.iv_fengxiang));
        tv_address = ((TextView) findViewById(R.id.tv_address));
        ll_collection = ((LinearLayout) findViewById(R.id.ll_collection));
        iv_collection = ((ImageView) findViewById(R.id.iv_collection));
        tv_price = ((TextView) findViewById(R.id.tv_prices));
        tv_huxing = ((TextView) findViewById(R.id.tv_huxing));
        tv_mianji = ((TextView) findViewById(R.id.tv_mianji));
        tv_yangtai = ((TextView) findViewById(R.id.tv_yangtai));
        tv_weishengjian = ((TextView) findViewById(R.id.tv_weishengjian));
        tv_louceng = ((TextView) findViewById(R.id.tv_louceng));
        tv_chaoxiang = ((TextView) findViewById(R.id.tv_chaoxiang));
        tv_fukuan = ((TextView) findViewById(R.id.tv_fukuan));
        tv_my_telphone = ((TextView) findViewById(R.id.tv_my_telphone));
        tv_my_telphone2 = ((TextView) findViewById(R.id.tv_my_telphone2));
        tv_address2 = ((TextView) findViewById(R.id.tv_address2));
        tv_peizhi = ((TextView) findViewById(R.id.tv_peizhi));
        tv_xiangqing = ((TextView) findViewById(R.id.tv_xiangqing));
        iv_map = ((ImageView) findViewById(R.id.iv_map));
        iv_message = ((ImageView) findViewById(R.id.iv_message));
        iv_call = ((ImageView) findViewById(R.id.iv_call));
        mView = View.inflate(PandaItem.this, R.layout.layout_dialog, null);

        iv_back.setOnClickListener(this);
        iv_fengxiang.setOnClickListener(this);
        ll_collection.setOnClickListener(this);
        iv_call.setOnClickListener(this);
        iv_message.setOnClickListener(this);
        iv_map.setOnClickListener(this);
    }

    private BitmapUtils bitmapUtils = new BitmapUtils(PandaItem.this);

    private void setData() {
//        bitmapUtils.display(iv_main,itemBean.getImgUrl());   //设置图片
        tv_address.setText(itemBean.getTitle());
        tv_price.setText(itemBean.getRent() + "");
        tv_huxing.setText(itemBean.getHouseType());
        tv_mianji.setText(itemBean.getSpec() + "㎡");
        tv_louceng.setText(itemBean.getRentFloor() + "/" + itemBean.getFloor() + "层");
        tv_chaoxiang.setText(itemBean.getAspect());
        tv_address2.setText(itemBean.getAddress());
        tv_peizhi.setText(itemBean.getConfig());
        if(!itemBean.getLinkPhone().equals("null")){
            tv_my_telphone.setText(itemBean.getLinkPhone());
            tv_my_telphone2.setText(itemBean.getLinkPhone());
        }else {
            tv_my_telphone.setText("");
            tv_my_telphone2.setText("");
        }

        tv_xiangqing.setText(itemBean.getDescription());
        bitmapUtils.display(iv_map, AccessToImages(itemBean.getZuobiaoX(), itemBean.getZuobiaoY()));   //设置图片
        if (itemBean.getPayStyle() == 0) {
            tv_fukuan.setText("月付");
        }
        if (itemBean.getPayStyle() == 1) {
            tv_fukuan.setText("压一付一");
        }
        if (itemBean.getPayStyle() == 2) {
            tv_fukuan.setText("压一付二");
        }
        if (itemBean.getPayStyle() == 3) {
            tv_fukuan.setText("压一付三");
        }
        if (itemBean.getPayStyle() == 4) {
            tv_fukuan.setText("压二付一");
        }
        if (itemBean.getPayStyle() == 5) {
            tv_fukuan.setText("压二付二");
        }
        if (itemBean.getPayStyle() == 6) {
            tv_fukuan.setText("压二付三");
        }
        if (itemBean.getPayStyle() == 7) {
            tv_fukuan.setText("半年付");
        }
        if (itemBean.getPayStyle() == 8) {
            tv_fukuan.setText("年付");
        }
        if (itemBean.getPayStyle() == 9) {
            tv_fukuan.setText("面议");
        }

        viewPager.setAdapter(new VpAdapter(getSupportFragmentManager()));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                for (int i = 0; i < listImgUrl.size(); i++) {
                    if (i == position) {
                        listDian.get(i).setImageResource(R.drawable.home_carousel_not_show);
                    } else {
                        listDian.get(i).setImageResource(R.drawable.home_carousel_show);

                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
/**
 * 还没有填数据
 */
        BitmapUtils bitmapUtils1 = new BitmapUtils(PandaItem.this);
//        listTenants   listimg2
        for (int i = 0; i < itemBean.getImgList().size(); i++) {                //还没加
            View view = View.inflate(PandaItem.this, R.layout.allrent, null);
// 获取手机屏幕的宽高信息
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);
            int width = metrics.widthPixels * 1 / 2;
            view.setLayoutParams(new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT)); //注意这里要回去屏幕的宽度

            TextView tv_shi = (TextView) view.findViewById(R.id.tv_shi);
            if (i == 0) {
                tv_shi.setText("01室");
            }
            if (i == 1) {
                tv_shi.setText("02室");
            }
            if (i == 2) {
                tv_shi.setText("03室");
            }
            if (i == 3) {
                tv_shi.setText("04室");
            }
            if (i == 4) {
                tv_shi.setText("05室");
            }
            if (i == 5) {
                tv_shi.setText("06室");
            }
            if (i >= 6) {
                return;
            }

            ImageView iv_tu = (ImageView) view.findViewById(R.id.iv_tu);
            bitmapUtils1.display(iv_tu, itemBean.getImgList().get(i).getImgUrl());
            ImageView tv_xingbie = (ImageView) view.findViewById(R.id.tv_xingbie);
            if ("男".equals(itemBean.getImgList().get(i).getSex())) {
                tv_xingbie.setImageResource(R.drawable.man_icon);
            }
            if ("女".equals(itemBean.getImgList().get(i).getSex())) {
                tv_xingbie.setImageResource(R.drawable.woman_icon);
            }
            TextView tv_nian = (TextView) view.findViewById(R.id.tv_nian);
            tv_nian.setText("年龄：" + itemBean.getImgList().get(i).getAge());
            TextView tv_zhi = (TextView) view.findViewById(R.id.tv_zhi);
            tv_zhi.setText("职业：" + itemBean.getImgList().get(i).getJobName());
            TextView tv_xin = (TextView) view.findViewById(R.id.tv_xin);
            tv_xin.setText("星座：" + itemBean.getImgList().get(i).getConstellation());
            TextView tv_jiguan = (TextView) view.findViewById(R.id.tv_jiguan);
            tv_jiguan.setText("籍贯：" + itemBean.getImgList().get(i).getNativePlaceName());
            linearLayout.addView(view);
        }
    }

    /**
     * 判断是否有网
     */
    private boolean isConnection() {
        int type = NetworkUtils.getConnctedType(this);
        if (type == -1) {
            Toast.makeText(PandaItem.this, PandaItem.this.getResources().getText(R.string.net_connection), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String UrlBaiDuImage = "http://api.map.baidu.com/staticimage?&width=334&height=253&zoom=18";

    /**
     * 获取带marke的地图图片
     *
     * @param mX
     * @param mY
     */
    private String AccessToImages(String mX, String mY) {
        if (TextUtils.isEmpty(mY) || TextUtils.isEmpty(mX)) {
            return null;
        }
        String URLS = UrlBaiDuImage + "center=" + mY + "," + mX + ""
                + "&markers=" + mY + "," + "" + mX + "";
        //http://api.map.baidu.com/staticimage?&width=334&height=253&zoom=18center=116.340393,39.725306&markers=116.340393,39.725306
        return URLS;
    }

    private boolean delete = true;
    private AlertDialog dialog;
    private View mView;
    private TextView tv_cancal;
    private TextView tv_confirm;
    private TextView tv_make_call;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_fengxiang:
                sendShare();
                break;
            case R.id.ll_collection:// 收藏房源
                if (delete) {
                    collection();
                } else {
                    netdelete();
                }
                break;
            case R.id.iv_call:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                if (dialog == null) {
                    dialog = builder.create();
                }
                dialog.setView(mView, 0, 0, 0, 0);
                tv_cancal = (TextView) mView.findViewById(R.id.tv_cancal);
                tv_confirm = (TextView) mView.findViewById(R.id.tv_confirm);
                tv_make_call = ((TextView) mView.findViewById(R.id.tv_make_call));
                String phone = tv_my_telphone.getText().toString().trim();                      //没有实例化
                if (!TextUtils.isEmpty(phone)) {
                    tv_make_call.setText("是否拨打电话 : " + phone);
                }
                tv_cancal.setOnClickListener(this);
                tv_confirm.setOnClickListener(this);
                dialog.show();
                WindowManager.LayoutParams params = dialog.getWindow()
                        .getAttributes();
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                dialog.getWindow().setAttributes(params);
                break;
            case R.id.tv_cancal:
                if (dialog != null) {
                    dialog.dismiss();
                }
                break;
            case R.id.tv_confirm:
                phoneCall();
                dialog.dismiss();
                break;
            case R.id.iv_message:
                sendMessage();
                break;
            case R.id.iv_map:
                Intent intent = new Intent(PandaItem.this, DetailMapActivity.class);
                if (itemBean != null) {
                    intent.putExtra("latitude", itemBean.getZuobiaoX());
                    intent.putExtra("longitude", itemBean.getZuobiaoY());
                }
                startActivity(intent);
                break;
        }
    }

    private void sendShare() {

        // 分享代码
        Intent intent = new Intent("android.intent.action.SEND");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, "http://www.69cheng.com/houseSearch/"+houseId+"/info");
        startActivity(intent);
    }

    protected void phoneCall() {
        String phone = tv_my_telphone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            phone = "01057490090";
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
        this.startActivity(intent);
    }

    //01057490090
    private void sendMessage() {
        Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
        sendIntent.setData(Uri.parse("smsto:" + itemBean.getLinkPhone()));
        if (itemBean != null) {
            sendIntent.putExtra("sms_body", "您好，我从六九城网站上看到您在"+itemBean.getAddress()+"有房子出租，请问您的房子租出去了吗？我方便看一下吗?");
        }
        PandaItem.this.startActivity(sendIntent);
    }

    /**
     * 收藏
     */

    private HttpUtils http;

    private void collection() {
        // 判断是否登录
        String userID = SPUtils.getUserID(PandaItem.this);
        if (TextUtils.isEmpty(userID)) {
            Intent intent = new Intent(PandaItem.this, LoagingActivity.class);
            startActivity(intent);
        } else {
            boolean b = isConnection();
            if (!b) {
                return;
            }
            dialogUtils.show();
            NetHead head = new NetHead(PandaItem.this);
            RequestParams params = head.setHeader();

            params.addBodyParameter("type", "collectHouse");
            params.addBodyParameter("userId", SPUtils.getUserID(PandaItem.this));
            params.addBodyParameter("houseId", itemBean.getId() + "");
            if (http == null) {
                http = new HttpUtils();
            }
            http.send(HttpRequest.HttpMethod.POST, UrlsUtils.pandaCollection, params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            dialogUtils.closeDialog();
                            Toast.makeText(PandaItem.this, StreamTools.getString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            delete = false;
                            dialogUtils.closeDialog();
                            String result = arg0.result;
                            Log.e("WWWWW", "result****" + result);
                            JSONObject josn = null;
                            try {
                                josn = new JSONObject(result);
                                String statue = josn.getString("status");
                                if ("N".equals(statue)) {
                                    iv_collection
                                            .setImageResource(R.drawable.details_collect_link);
                                    Toast.makeText(PandaItem.this, "此房源已经收藏",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    iv_collection
                                            .setImageResource(R.drawable.details_collect_link);
                                    Toast.makeText(PandaItem.this, "收藏成功",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    class VpAdapter extends FragmentPagerAdapter {

        public VpAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return new VpFragment(PandaItem.this, position);
        }

        @Override
        public int getCount() {
            return listImgUrl.size();
        }
    }


    /**
     * 删除收藏
     */
    private void netdelete() {
        // 判断是否登录
        String userID = SPUtils.getUserID(PandaItem.this);
        if (TextUtils.isEmpty(userID)) {
            Intent intent = new Intent(PandaItem.this, LoagingActivity.class);
            startActivity(intent);
        } else {
            boolean b = isConnection();
            if (!b) {
                return;
            }
            if(!isFirst){
                dialogUtils.show();
            }
            NetHead head = new NetHead(PandaItem.this);
            RequestParams params = head.setHeader();
            params.addBodyParameter("type", "deleteColect");
            params.addBodyParameter("houseId", houseId + "");
//            Log.e("CCCCCCCCCCCC", "*******************"+listBean.get(deleteTag).getId()+"");
            params.addBodyParameter("userId", SPUtils.getUserID(PandaItem.this));
            if (http == null) {
                http = new HttpUtils();
            }
            http.send(HttpRequest.HttpMethod.POST, UrlsUtils.pandaDelCollection, params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            dialogUtils.closeDialog();
                            Toast.makeText(PandaItem.this, "网络连接超时",
                                    Toast.LENGTH_SHORT).show();
                            isFirst = false;
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            delete = true;
                            dialogUtils.closeDialog();
                            String result = arg0.result;
//                            JSONObject josn = null;
//                            try {
//                                josn = new JSONObject(result);
//                                String statue = josn.getString("status");
//                                if (!"N".equals(statue)) {
                            if (isFirst) {

                            } else {
                                Toast.makeText(PandaItem.this, "取消收藏成功",
                                        Toast.LENGTH_SHORT).show();
                            }
                            iv_collection
                                    .setImageResource(R.drawable.details_collect);
//
                            isFirst = false;
                        }
                    });

        }
    }

    /**
     * 第一次判断是否收藏
     */
    private boolean isFirst = true;

    private void isFirstCollection() {
        // 判断是否登录
        String userID = SPUtils.getUserID(PandaItem.this);
        if (TextUtils.isEmpty(userID)) {
            Intent intent = new Intent(PandaItem.this, LoagingActivity.class);
            startActivity(intent);
        } else {
            boolean b = isConnection();
            if (!b) {
                return;
            }
            NetHead head = new NetHead(PandaItem.this);
            RequestParams params = head.setHeader();

            params.addBodyParameter("type", "collectHouse");
            params.addBodyParameter("userId", SPUtils.getUserID(PandaItem.this));
//            params.addBodyParameter("houseId", itemBean.getId() + "");
            params.addBodyParameter("houseId", houseId);
            if (http == null) {
                http = new HttpUtils();
            }
            http.send(HttpRequest.HttpMethod.POST, UrlsUtils.pandaCollection, params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            delete = false;
                            String result = arg0.result;
                            Log.e("WWWWW", "result****" + result);

                            JSONObject josn = null;
                            try {
                                josn = new JSONObject(result);
                                String statue = josn.getString("status");
                                if ("N".equals(statue)) {
                                    iv_collection
                                            .setImageResource(R.drawable.details_collect_link);
//                                    Toast.makeText(PandaItem.this, "此房源已经收藏",
//                                            Toast.LENGTH_SHORT).show();
                                    delete = false;
                                } else {
                                    iv_collection
                                            .setImageResource(R.drawable.details_collect);
                                    delete = true;
//                                    Toast.makeText(PandaItem.this, "取消收藏成功",
//                                            Toast.LENGTH_SHORT).show();
                                    netdelete();   //在这里要取消收藏

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }
}
