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

public class MyReleaseSeePandaActivity extends Activity {
    private ImageView exit;
    private TextView title, time, browse,  price, houseStyle,
            spec, floor, towards, payment, address, config, state,
            linkman, phone;
    private TextView roommate_houseStyle, roommate_name, roommate_sex,
            roommate_age, roommate_job, roommate_constellation,
            roommate_interest, roommate_hometown, roommate_school;
    private String houseId;

    private Context context;
    private DialogUtils utils;
    private HttpUtils httpUtils,http;
    private Map<String,String> map_panda,map_panda_add;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_release_see_panda);
        context=MyReleaseSeePandaActivity.this;
        userID= SPUtils.getUserID(context);
        if(userID==null){
            Intent intent=new Intent(context, LoagingActivity.class);
            startActivity(intent);
        }
        initView();
        houseId=getHouseId();
        loadInfo();
    }

    private void loadInfo() {
        if(utils==null){
            utils=new DialogUtils(context);
        }
        utils.show();

        boolean isConnect= NetworkUtils.isConnection(context);
        if(isConnect){
            NetHead head=new NetHead(context);
            RequestParams params=head.setHeader();

            params.addBodyParameter("houseId",houseId);
            if(httpUtils==null){
                httpUtils=new HttpUtils();
            }
            httpUtils.send(HttpRequest.HttpMethod.POST, UrlsUtils.pandaDetail,params,new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                    {
                        Toast.makeText(context, "获取详情信息成功", Toast.LENGTH_SHORT).show();
                        Log.e("RRRREREREEERERE", "EEEEEEEEEEEEEE---合租" + objectResponseInfo.result.toString());
                        try {
                            if (map_panda == null) {
                                map_panda = new HashMap<>();
                            }

                            JSONObject jsonObject = new JSONObject(objectResponseInfo.result);
                            if ("Y".equals(jsonObject.getString("status"))) {
                                JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                                map_panda.put("title", jsonObject1.getString("title"));
                                map_panda.put("linkMan", jsonObject1.getString("linkMan"));
                                map_panda.put("tenantId", jsonObject1.getString("id"));
                                map_panda.put("linkPhone", jsonObject1.getString("linkPhone"));
                                map_panda.put("rent", jsonObject1.getString("rent"));
                                map_panda.put("payStyle", jsonObject1.getString("payStyle"));
                                map_panda.put("pubTime", jsonObject1.getString("pubTimeStr"));
                                map_panda.put("houseType", jsonObject1.getString("houseType"));
                                map_panda.put("spec", jsonObject1.getString("spec"));
                                map_panda.put("rentFloor", jsonObject1.getString("rentFloor"));
                                map_panda.put("floor", jsonObject1.getString("floor"));
                                map_panda.put("rentStyle", jsonObject1.getString("rentStyle"));
                                map_panda.put("tenementType", jsonObject1.getString("tenementType"));
                                map_panda.put("interiordesign", jsonObject1.getString("interiordesign"));
                                map_panda.put("config", jsonObject1.getString("config"));
                                map_panda.put("aspect", jsonObject1.getString("aspect"));
                                map_panda.put("description", jsonObject1.getString("description"));
                                map_panda.put("address", jsonObject1.getString("address"));
                                map_panda.put("district", jsonObject1.getString("district"));
                                map_panda.put("bussinessarea", jsonObject1.getString("bussinessarea"));
                                map_panda.put("tenement", jsonObject1.getString("tenement"));//小区
                                map_panda.put("imageList", jsonObject1.getString("imageList"));
                                map_panda.put("click", jsonObject1.getString("click"));

                                if (jsonObject1.getJSONArray("listTenant") != null) {
                                    final String tenantId = jsonObject1.getJSONArray("listTenant").getJSONObject(0).getString("id");
                                    if (tenantId == null) {
                                        Toast.makeText(context, "您没有添加室友", Toast.LENGTH_SHORT).show();

                                    }
                                    RequestParams params3 = new RequestParams();
                                    params3.addBodyParameter("houseId", houseId);
                                    params3.addBodyParameter("tenantId", tenantId);
                                    params3.addBodyParameter("userId", userID);
                                    if (http == null) {
                                        http = new HttpUtils();
                                    }
                                    http.send(HttpRequest.HttpMethod.POST, UrlsUtils.urlReleaseRoommateInfo, params3, new RequestCallBack<String>() {
                                        @Override
                                        public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                                            map_panda_add = new HashMap<String, String>();
                                            Log.e("室友信息", "室友信息" + objectResponseInfo.result);
                                            try {
                                                JSONObject jsonObject = new JSONObject(objectResponseInfo.result);
                                                if ("Y".equals(jsonObject.getString("status"))) {
                                                    JSONObject jsonObject2 = jsonObject.getJSONObject("data");
                                                    map_panda_add.put("name", jsonObject2.getString("name"));
                                                    map_panda_add.put("nativePlaceName", jsonObject2.getString("nativePlaceName"));
                                                    map_panda_add.put("nativePlace", jsonObject2.getString("nativePlace"));
                                                    map_panda_add.put("constellation", jsonObject2.getString("constellation"));
                                                    map_panda_add.put("interest", jsonObject2.getString("interest"));
                                                    map_panda_add.put("jobName", jsonObject2.getString("jobName"));
                                                    map_panda_add.put("job", jsonObject2.getString("job"));
                                                    map_panda_add.put("age", jsonObject2.getString("age"));
                                                    map_panda_add.put("sex", jsonObject2.getString("sex"));
                                                    map_panda_add.put("houseName", jsonObject2.getString("houseName"));
                                                    map_panda_add.put("school", jsonObject2.getString("school"));
                                                    map_panda_add.put("phone", jsonObject2.getString("phone"));
                                                    map_panda_add.put("schoolName", jsonObject2.getString("schoolName"));
                                                    map_panda_add.put("tenantId", tenantId);
                                                    map_panda_add.put("headImg", jsonObject2.getString("headImg"));
                                                    map_panda_add.put("houseId", houseId);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        @Override
                                        public void onFailure(HttpException e, String s) {

                                            Toast.makeText(context, "室友信息获取失败", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        showInfo(map_panda,map_panda_add);
                        utils.closeDialog();
                    }
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(context,"信息获取失败",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //获取houseId
    private String getHouseId() {
        Intent intent=getIntent();
        String str=intent.getExtras().getString("houseId");
        return str;
    }

    private void showInfo(Map<String, String> map, Map<String, String> map2) {
        title.setText(map.get("title"));
        time.setText(map.get("pubTime"));
        browse.setText("被浏览了"+map.get("click")+"次");
        price.setText(map.get("rent")+"元");
        houseStyle.setText(map.get("houseType"));
        spec.setText(map.get("spec")+"㎡");
        floor.setText(map.get("rentFloor")+"/"+map.get("floor")+"层");
        towards.setText(map.get("aspect"));

        if("0".equals(map.get("payStyle"))){
            payment.setText("月付");
        }
        if("1".equals(map.get("payStyle"))){
            payment.setText("压一付一");
        }
        if("2".equals(map.get("payStyle"))){
            payment.setText("压一付二");
        }
        if("3".equals(map.get("payStyle"))){
            payment.setText("压一付三");
        }
        if("4".equals(map.get("payStyle"))){
            payment.setText("压二付一");
        }
        if("5".equals(map.get("payStyle"))){
            payment.setText("压二付二");
        }
        if("6".equals(map.get("payStyle"))){
            payment.setText("压二付三");
        }
        if("7".equals(map.get("payStyle"))){
            payment.setText("半年付");
        }
        if("8".equals(map.get("payStyle"))){
            payment.setText("年付");
        }
        if("9".equals(map.get("payStyle"))){
            payment.setText("面议");
        }

        address.setText(map.get("address"));
        config.setText(map.get("config"));
        state.setText(map.get("description"));
        linkman.setText(map.get("linkMan"));
        phone.setText(map.get("linkPhone"));

        roommate_houseStyle.setText("");
        roommate_name.setText("");
        roommate_sex.setText("");
        roommate_age.setText("");
        roommate_job.setText("");
        roommate_constellation.setText("");
        roommate_interest.setText("");
        roommate_hometown.setText("");
        roommate_school.setText("");

        if (map2 != null) {
            roommate_houseStyle.setText(map2.get("houseName"));
            roommate_name.setText(map2.get("name"));
            roommate_sex.setText(map2.get("sex"));
            roommate_age.setText(map2.get("age"));
            roommate_job.setText(map2.get("jobName"));
            roommate_constellation.setText(map2.get("constellation"));
            roommate_interest.setText(map2.get("interest"));
            roommate_hometown.setText(map2.get("nativePlaceName"));
            roommate_school.setText(map2.get("schoolName"));
        }
    }

    private void initView() {
        exit = (ImageView) findViewById(R.id.my_release_see_panda_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        title = (TextView) findViewById(R.id.my_release_see_panda_title);
        time = (TextView) findViewById(R.id.my_release_see_panda_time);
        browse = (TextView) findViewById(R.id.my_release_see_panda_browse);
        price = (TextView) findViewById(R.id.my_release_see_panda_price);
        houseStyle = (TextView) findViewById(R.id.my_release_see_panda_houseStyle);
        spec = (TextView) findViewById(R.id.my_release_see_panda_spec);
        floor = (TextView) findViewById(R.id.my_release_see_panda_floor);
        towards = (TextView) findViewById(R.id.my_release_see_panda_towards);
        payment = (TextView) findViewById(R.id.my_release_see_panda_payment);
        address = (TextView) findViewById(R.id.my_release_see_panda_address);
        config = (TextView) findViewById(R.id.my_release_see_panda_config);
        state = (TextView) findViewById(R.id.my_release_see_panda_state);
        linkman = (TextView) findViewById(R.id.my_release_see_panda_linkman);
        phone = (TextView) findViewById(R.id.my_release_see_panda_phone);


        roommate_houseStyle = (TextView) findViewById(R.id.my_release_roommate_houseStyle);
        roommate_name = (TextView) findViewById(R.id.my_release_roommate_name);
        roommate_sex = (TextView) findViewById(R.id.my_release_roommate_sex);
        roommate_age = (TextView) findViewById(R.id.my_release_roommate_age);
        roommate_job = (TextView) findViewById(R.id.my_release_roommate_job);
        roommate_constellation = (TextView) findViewById(R.id.my_release_roommate_constellation);
        roommate_interest = (TextView) findViewById(R.id.my_release_roommate_interest);
        roommate_hometown = (TextView) findViewById(R.id.my_release_roommate_hometown);
        roommate_school = (TextView) findViewById(R.id.my_release_roommate_school);
    }
}
