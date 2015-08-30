package com.mobile.liujiucheng.sixninecheng.main.activityliu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mobile.liujiucheng.main.utils.DialogUtils;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.NetworkUtils;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.sixninecheng.main.LoagingActivity;
import com.mobile.liujiucheng.sixninecheng.main.R;
import com.mobile.liujiucheng.sixninecheng.main.SPUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyReleaseSeeSecondActivity extends Activity {
    private ImageView exit;
    private TextView title, time, browse,  price, houseStyle, spec, price_one,
            fitment,//装修
            floor, towards, equity,//产权
            type, plot, address, state, linkman, phone;

    private HttpUtils httpUtils;
    private DialogUtils utils;
    private Context context;
    private String userId;
    private String tenantId;
    private Map<String,String> map_second;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_release_see_sell);
        context=MyReleaseSeeSecondActivity.this;
        userId= SPUtils.getUserID(context);
        if(userId==null){
            Intent intent=new Intent(context, LoagingActivity.class);
            startActivity(intent);
        }
        initView();
        getTenantId();
        loadInfo();
    }

    private void loadInfo() {
        if(utils==null){
            utils=new DialogUtils(context);
        }
        utils.show();
        if(map_second==null){
            map_second=new HashMap<>();
        }
        boolean isConnect= NetworkUtils.isConnection(context);
        if(isConnect){
            NetHead head=new NetHead(context);
            RequestParams params=head.setHeader();
            params.addBodyParameter("type", "detailsSecondHouse");
            params.addBodyParameter("sid", tenantId);
            if(httpUtils==null){
                httpUtils=new HttpUtils();
            }
            httpUtils.send(HttpRequest.HttpMethod.POST, UrlsUtils.urlMyReleaseSecond,params,new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                    Toast.makeText(context, "获取详情信息成功", Toast.LENGTH_SHORT).show();
                    Log.e("EEEEEEEEEEEE", "EEEEEEEEEE---二手房" + objectResponseInfo.result);
                    try {
                        if (map_second == null) {
                            map_second = new HashMap<>();
                        }
                        JSONObject jsonObject = new JSONObject(objectResponseInfo.result);
                        if ("Y".equals(jsonObject.getString("status"))) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            map_second.put("description", jsonObject1.getString("description"));//描述
                            map_second.put("tenement", jsonObject1.getString("tenement"));//小区
                            map_second.put("property", jsonObject1.getString("property"));//产权
                            map_second.put("unitPrice", jsonObject1.getString("unitPrice"));//单价
                            map_second.put("click", jsonObject1.getString("click"));//浏览量
                            map_second.put("interiordesign", jsonObject1.getString("interiordesign"));//装修
                            map_second.put("sid", jsonObject1.getString("sid"));
                            map_second.put("district", jsonObject1.getString("district"));//区域
                            map_second.put("title", jsonObject1.getString("title"));//标题
                            map_second.put("rent", jsonObject1.getString("rent"));//总价
                            map_second.put("linkMan", jsonObject1.getString("linkMan"));//联系人
                            map_second.put("spec", jsonObject1.getString("spec"));//面积
                            map_second.put("linkPhone", jsonObject1.getString("linkPhone"));//手机
                            map_second.put("floor", jsonObject1.getString("floor"));//楼层
                            map_second.put("rentFloor", jsonObject1.getString("rentFloor"));//总楼层
                            map_second.put("bussinessarea", jsonObject1.getString("bussinessarea"));//商圈
                            map_second.put("address", jsonObject1.getString("address"));//地址
                            map_second.put("pubTime", jsonObject1.getString("pubTime"));//创建时间
                            map_second.put("aspect", jsonObject1.getString("aspect"));//朝向
                            map_second.put("tenementType", jsonObject1.getString("tenementType"));//住宅类型
                            map_second.put("houseType", jsonObject1.getString("houseType"));//户型
                            map_second.put("sid", jsonObject1.getString("sid"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showInfo(map_second);
                    utils.closeDialog();
                }

                @Override
                public void onFailure(HttpException e, String s) {

                }
            });
        }
    }

    private void getTenantId() {
        tenantId=getIntent().getExtras().getString("tenantId");
    }

    //将信息展示在控件上
    private void showInfo(Map<String,String> map) {

        title.setText(map.get("title"));
        time.setText(map.get("pubTime"));
        browse.setText("被浏览"+map.get("click")+"次");
        price.setText(map.get("rent")+"万元");
        houseStyle.setText(map.get("houseType"));//户型
        spec.setText(map.get("spec")+"㎡");
        price_one.setText(map.get("unitPrice")+"元/㎡");//单价
        fitment.setText(map.get("interiordesign"));//装修
        floor.setText(map.get("floor")+"/"+map.get("rentFloor")+"层");
        towards.setText(map.get("aspect"));//朝向
        equity.setText(map.get("property")+"年产权");//产权
        plot.setText(map.get("tenement"));//小区
        address.setText(map.get("address"));
        state.setText(map.get("description"));
        linkman.setText(map.get("linkMan"));
        phone.setText(map.get("linkPhone"));

        if("2".equals(map.get("tenementType"))){
            type.setText("住宅");
        }else if("3".equals(map.get("tenementType"))){
            type.setText("别墅");
        }else if("5".equals(map.get("tenementType"))){
            type.setText("商铺");
        }else if("6".equals(map.get("tenementType"))){
            type.setText("写字楼");
        }
    }

    //初始化控件
    private void initView() {

        title = (TextView) findViewById(R.id.my_release_see_sell_title);
        time = (TextView) findViewById(R.id.my_release_see_sell_time);
        browse = (TextView) findViewById(R.id.my_release_see_sell_browse);
        price = (TextView) findViewById(R.id.my_release_see_sell_price);
        houseStyle = (TextView) findViewById(R.id.my_release_see_sell_houseStyle);
        spec = (TextView) findViewById(R.id.my_release_see_sell_spec);
        price_one = (TextView) findViewById(R.id.my_release_see_sell_price_one);
        fitment = (TextView) findViewById(R.id.my_release_see_sell_fitment);
        floor = (TextView) findViewById(R.id.my_release_see_sell_floor);
        towards = (TextView) findViewById(R.id.my_release_see_sell_towards);
        equity = (TextView) findViewById(R.id.my_release_see_sell_equity);
        type = (TextView) findViewById(R.id.my_release_see_sell_type);
        plot = (TextView) findViewById(R.id.my_release_see_sell_plot);
        address = (TextView) findViewById(R.id.my_release_see_sell_address);
        state = (TextView) findViewById(R.id.my_release_see_sell_state);
        linkman = (TextView) findViewById(R.id.my_release_see_sell_linkman);
        phone = (TextView) findViewById(R.id.my_release_see_sell_phone);

        exit = (ImageView) findViewById(R.id.my_release_see_sell_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
