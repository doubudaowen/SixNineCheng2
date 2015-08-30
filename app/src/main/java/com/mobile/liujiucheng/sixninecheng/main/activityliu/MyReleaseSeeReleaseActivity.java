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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyReleaseSeeReleaseActivity extends Activity {

    private TextView title, date, area, rent, timeIn, age, sex, interest,
            constellation, hometown, school, state, linkman, phone, job;
    private ImageView exit;
    private HttpUtils httpUtils;
    private DialogUtils utils;
    private Map<String, String> map_release;
    private String userID;
    private String tenantId;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_release_see_release);
        context = MyReleaseSeeReleaseActivity.this;
        userID = SPUtils.getUserID(context);
        if (userID == null) {
            Intent intent = new Intent(context, LoagingActivity.class);
            startActivity(intent);
        }
        initView();
        getTenantId();
        loadInfo();
    }

    private void getTenantId() {
        tenantId = getIntent().getExtras().getString("tenantId");
    }

    private void loadInfo() {
        if (utils == null) {
            utils = new DialogUtils(context);
        }
        utils.show();

        boolean isConnect = NetworkUtils.isConnection(context);
        if (isConnect) {
            NetHead head = new NetHead(context);
            RequestParams params = head.setHeader();
            params.addBodyParameter("userId", userID);
            params.addBodyParameter("tenantId", tenantId);
            if (httpUtils == null) {
                httpUtils = new HttpUtils();
            }
            httpUtils.send(HttpRequest.HttpMethod.POST, UrlsUtils.getUrlReleaseLoadInfo, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                    Toast.makeText(context, "获取详情信息成功", Toast.LENGTH_SHORT).show();
                    Log.e("EEEEEEEEEEEEEE", "EEEEEEEEEEEEE---求租" + objectResponseInfo.result);
                    try {
                        if (map_release == null) {
                            map_release = new HashMap<>();
                        }
                        JSONObject jsonObject = new JSONObject(objectResponseInfo.result);
                        if ("Y".equals(jsonObject.getString("status"))) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            map_release.put("createTimeStr", jsonObject1.getString("createTimeStr"));
                            map_release.put("checkInTimeStr", jsonObject1.getString("checkInTimeStr"));
                            map_release.put("constellation", jsonObject1.getString("constellation"));
                            map_release.put("interest", jsonObject1.getString("interest"));
                            map_release.put("tenantId", jsonObject1.getString("tenantId"));
                            map_release.put("description", jsonObject1.getString("description"));
                            map_release.put("age", jsonObject1.getString("age"));
                            map_release.put("sex", jsonObject1.getString("sex"));
                            map_release.put("district", jsonObject1.getString("district"));
                            map_release.put("title", jsonObject1.getString("title"));
                            map_release.put("rent", jsonObject1.getString("rent"));
                            map_release.put("name", jsonObject1.getString("name"));
                            map_release.put("nativePlaceName", jsonObject1.getString("nativePlaceName"));
                            map_release.put("nativePlace", jsonObject1.getString("nativePlace"));
                            map_release.put("phone", jsonObject1.getString("phone"));
                            map_release.put("jobName", jsonObject1.getString("jobName"));
                            map_release.put("job", jsonObject1.getString("job"));
                            map_release.put("business", jsonObject1.getString("business"));
                            map_release.put("schoolName", jsonObject1.getString("schoolName"));
                            map_release.put("school", jsonObject1.getString("school"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showInfo(map_release);
                    utils.closeDialog();
                }
                @Override
                public void onFailure(HttpException e, String s) {

                }
            });
        }
    }


    private void showInfo(Map<String, String> map) {
        title.setText(map.get("title"));
        date.setText(map.get("createTimeStr"));
        area.setText(map.get("district") + "-" + map.get("business"));
        rent.setText(map.get("rent"));
        timeIn.setText(map.get("checkInTimeStr"));
        age.setText(map.get("age"));
        sex.setText(map.get("sex"));
        interest.setText(map.get("interest"));
        constellation.setText(map.get("constellation"));
        hometown.setText(map.get("nativePlaceName"));
        school.setText(map.get("schoolName"));
        state.setText(map.get("description"));
        linkman.setText(map.get("name"));
        phone.setText(map.get("phone"));
        job.setText(map.get("jobName"));
    }

    private void initView() {
        title = (TextView) findViewById(R.id.my_release_see_release_title);
        date = (TextView) findViewById(R.id.my_release_see_release_date2);
        area = (TextView) findViewById(R.id.my_release_see_release_area);
        rent = (TextView) findViewById(R.id.my_release_see_release_rent);
        timeIn = (TextView) findViewById(R.id.my_release_see_release_in);
        age = (TextView) findViewById(R.id.my_release_see_release_age);
        sex = (TextView) findViewById(R.id.my_release_see_release_sex);
        interest = (TextView) findViewById(R.id.my_release_see_release_interest);
        constellation = (TextView) findViewById(R.id.my_release_see_release_constellation);
        hometown = (TextView) findViewById(R.id.my_release_see_release_hometown);
        school = (TextView) findViewById(R.id.my_release_see_release_school);
        state = (TextView) findViewById(R.id.my_release_see_release_state);
        linkman = (TextView) findViewById(R.id.my_release_see_release_linkman);
        phone = (TextView) findViewById(R.id.my_release_see_release_phone);
        job = (TextView) findViewById(R.id.my_release_see_release_job);

        exit = (ImageView) findViewById(R.id.my_release_see_release_exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
