package com.mobile.liujiucheng.sixninecheng.main.activityliu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class MyReleaseSeeRentActivity extends Activity {

    private TextView title,checkInTime,browse,rent,houseStyle,spec,floor,
            fitment,towards,plot,address,description,linkMan, linkPhone,config;
    private ImageView exit;
    private Context context;
    private HttpUtils httpUtils;
    private DialogUtils utils;
    private Map<String,String> map_rent;
    private String tenantId;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_release_see_rent);
        context=MyReleaseSeeRentActivity.this;
        userID= SPUtils.getUserID(context);
        if(userID==null){
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
        if(map_rent==null){
            map_rent=new HashMap<>();
        }
        if(map_rent!=null){
            map_rent.clear();
        }
        boolean isConnect= NetworkUtils.isConnection(context);
        if(isConnect){
            NetHead head=new NetHead(context);
            RequestParams params=head.setHeader();
            params.addBodyParameter("type", "detailsHouse");
            params.addBodyParameter("sid", tenantId);
            if(httpUtils==null){
                httpUtils=new HttpUtils();
            }
            httpUtils.send(HttpRequest.HttpMethod.POST, UrlsUtils.urlMyReleaseSecond,params,new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                    Toast.makeText(context, "获取详情信息成功", Toast.LENGTH_SHORT).show();
                    try {
                        if (map_rent == null) {
                            map_rent = new HashMap<>();
                        }
                        JSONObject jsonObject = new JSONObject(objectResponseInfo.result);
                        if ("Y".equals(jsonObject.getString("status"))) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            map_rent.put("address", jsonObject1.getString("address"));
                            map_rent.put("bussinessarea", jsonObject1.getString("bussinessarea"));
                            map_rent.put("description", jsonObject1.getString("description"));
                            map_rent.put("district", jsonObject1.getString("district"));
                            map_rent.put("floor", jsonObject1.getString("floor"));
                            map_rent.put("interiordesign", jsonObject1.getString("interiordesign"));//装修
                            map_rent.put("linkMan", jsonObject1.getString("linkMan"));
                            map_rent.put("linkPhone", jsonObject1.getString("linkPhone"));
                            map_rent.put("rent", jsonObject1.getString("rent"));//租金
                            map_rent.put("rentFloor", jsonObject1.getString("rentFloor"));
                            map_rent.put("spec", jsonObject1.getString("spec"));
                            map_rent.put("title", jsonObject1.getString("title"));//标题
                            map_rent.put("houseType", jsonObject1.getString("houseType"));//户型
                            map_rent.put("tenement", jsonObject1.getString("tenement"));//小区
                            map_rent.put("aspect", jsonObject1.getString("aspect"));//朝向
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showInfo(map_rent);
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

    private void showInfo(Map<String,String> map) {
        title.setText(map.get("title"));
        checkInTime.setText(map.get(""));
        browse.setText(map.get(""));
        rent.setText(map.get("rent")+"元");
        houseStyle.setText(map.get("houseType"));
        spec.setText(map.get("spec")+"㎡");
        floor.setText(map.get("floor")+"/"+map.get("rentFloor")+"层");
        fitment.setText(map.get("interiordesign"));
        towards.setText(map.get("aspect"));//朝向
        plot.setText(map.get("tenement"));//小区
        address.setText(map.get("address"));//地址
        description.setText(map.get("description"));//描述
        linkMan.setText(map.get("linkMan"));//；联系人
        linkPhone.setText(map.get("linkPhone"));//手机号
    }

    private void initView() {
        title= (TextView) findViewById(R.id.my_release_see_rent_title);//标题
        checkInTime= (TextView) findViewById(R.id.my_release_see_rent_time);//入住时间
        browse= (TextView) findViewById(R.id.my_release_see_rent_browse);//浏览次数
        rent= (TextView) findViewById(R.id.my_release_see_rent_price);//租金
        houseStyle= (TextView) findViewById(R.id.my_release_see_rent_houseStyle);//fangwuleixing
        spec= (TextView) findViewById(R.id.my_release_see_rent_spec);//面积
        floor= (TextView) findViewById(R.id.my_release_see_rent_floor);//楼层
        fitment= (TextView) findViewById(R.id.my_release_see_rent_fitment);//装修
        towards= (TextView) findViewById(R.id.my_release_see_rent_towards);//朝向
        plot= (TextView) findViewById(R.id.my_release_see_rent_plot);//小区
        address= (TextView) findViewById(R.id.my_release_see_rent_address);//地址
        description= (TextView) findViewById(R.id.my_release_see_rent_state);//描述
        linkMan= (TextView) findViewById(R.id.my_release_see_rent_linkman);//联系人
        linkPhone = (TextView) findViewById(R.id.my_release_see_rent_phone);//联系人电话
        config= (TextView) findViewById(R.id.my_release_see_rent_config);//配置
        config.setVisibility(View.GONE);
        exit= (ImageView) findViewById(R.id.my_release_see_rent_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
