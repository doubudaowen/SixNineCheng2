package com.mobile.liujiucheng.sixninecheng.main.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;

import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.mobile.liujiucheng.main.bean.AcquireDatailBean;
import com.mobile.liujiucheng.main.utils.DialogUtils;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.NetworkUtils;
import com.mobile.liujiucheng.main.utils.StreamTools;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.sixninecheng.main.LoagingActivity;
import com.mobile.liujiucheng.sixninecheng.main.R;
import com.mobile.liujiucheng.sixninecheng.main.SPUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AcquireItemActivity extends Activity implements View.OnClickListener {

    private ImageView iv_collection;
    private ImageView iv_icon;
    private ImageView iv_call;
    private ImageView iv_back;
    private ImageView iv_sex;

    private TextView tv_name;
    private TextView tv_age;
    private TextView tv_job;
    private TextView dianhua;
    private TextView tv_qiwang;
    private TextView tv_zujing;
    private TextView tv_ruzhu;
    private TextView tv_xingqu;
    private TextView tv_xingzuo;
    private TextView tv_jiguan;
    private TextView tv_yuanxiao;
    private TextView tv_ziliao;
    private ImageView iv_1,iv_2,iv_3,iv_4,iv_5,iv_6;
    private ImageView iv[] = new ImageView[6];
    private BitmapUtils bit;

    private boolean delete = true;


    private DialogUtils dialogUtils;
    private String mResult = "";
    private String tenantId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_acquire_item);

        Intent intent = getIntent();
        Bundle bundles = intent.getExtras();
        tenantId = bundles.getString("tenantId");
        initView();
        getUseId();
        if (TextUtils.isEmpty(useId)) {
            Intent intent1 = new Intent(AcquireItemActivity.this, LoagingActivity.class);
            startActivity(intent1);
            finish();
            return;
        }
//        Toast.makeText(AcquireItemActivity.this, tenantId + "", Toast.LENGTH_LONG).show();
        mDialog();
        requestNet();

        if (isFirst) {
            isFirstCollection();
        }
        setOnClick();
    }

    private void initView() {

        iv_1 = ((ImageView) findViewById(R.id.iv_1));
        iv_2 = ((ImageView) findViewById(R.id.iv_2));
        iv_3 = ((ImageView) findViewById(R.id.iv_3));
        iv_4 = ((ImageView) findViewById(R.id.iv_4));
        iv_5 = ((ImageView) findViewById(R.id.iv_5));
        iv_6 = ((ImageView) findViewById(R.id.iv_6));
        iv[0] = iv_1;
        iv[1] = iv_2;
        iv[2] = iv_3;
        iv[3] = iv_4;
        iv[4] = iv_5;
        iv[5] = iv_6;
        iv_collection = ((ImageView) findViewById(R.id.iv_collection));
        iv_icon = ((ImageView) findViewById(R.id.iv_icon));
        iv_sex = ((ImageView) findViewById(R.id.iv_sex));
        iv_call = ((ImageView) findViewById(R.id.iv_call));
        iv_back = ((ImageView) findViewById(R.id.iv_back));

        tv_name = ((TextView) findViewById(R.id.tv_name));
        tv_age = ((TextView) findViewById(R.id.tv_age));
        tv_job = ((TextView) findViewById(R.id.tv_job));
        dianhua = ((TextView) findViewById(R.id.dianhua));
        tv_qiwang = ((TextView) findViewById(R.id.tv_qiwang));
        tv_zujing = ((TextView) findViewById(R.id.tv_zujing));
        tv_ruzhu = ((TextView) findViewById(R.id.tv_ruzhu));
        tv_xingqu = ((TextView) findViewById(R.id.tv_xingqu));
        tv_xingzuo = ((TextView) findViewById(R.id.tv_xingzuo));
        tv_jiguan = ((TextView) findViewById(R.id.tv_jiguan));
        tv_yuanxiao = ((TextView) findViewById(R.id.tv_yuanxiao));
        tv_ziliao = ((TextView) findViewById(R.id.tv_ziliao));
        mView = View.inflate(AcquireItemActivity.this, R.layout.layout_dialog, null);
    }

    private void setData() {
        if(bit != null){
            bit.display(iv_icon,acquireDatailBean.getHeadImg());
        }else {
            bit = new BitmapUtils(AcquireItemActivity.this);
            bit.display(iv_icon,acquireDatailBean.getHeadImg());
        }
        tv_name.setText("姓名：" + acquireDatailBean.getName());
        tv_age.setText("年龄：" + acquireDatailBean.getAge());
        if(!"null".equals(acquireDatailBean.getJobName())){
            tv_job.setText("职业：" + acquireDatailBean.getJobName());
        }else {
            tv_job.setText("职业：");
        }
        if ((acquireDatailBean.getPhone().equals("null"))) {
            dianhua.setText("电话：");
        } else {
            dianhua.setText("电话：" + acquireDatailBean.getPhone());
        }
        tv_qiwang.setText("期望区域：" + acquireDatailBean.getDistrict());      //期望职业没有字段
        tv_zujing.setText("租金：" + acquireDatailBean.getRent());
        if(acquireDatailBean.getRent().equals("0-500")){
            tv_zujing.setText("租金：" +"500以下");
        }if(acquireDatailBean.getRent().equals("3000")){
            tv_zujing.setText("租金：" +"3000以上");
        }
        tv_ruzhu.setText("入住：" + acquireDatailBean.getCreateTimeStr());
        tv_xingqu.setText("兴趣：" + acquireDatailBean.getInterest());
        tv_xingzuo.setText("星座：" + acquireDatailBean.getConstellation());
        if (!"null".equals(acquireDatailBean.getNativePlaceName())){
            tv_jiguan.setText("籍贯：" + acquireDatailBean.getNativePlaceName());
        }else {
            tv_jiguan.setText("籍贯：");
        }
        tv_yuanxiao.setText("院校：" + acquireDatailBean.getSchoolName());
        tv_ziliao.setText(acquireDatailBean.getDescription());

        for (int i=0;i<imgList.size();i++){
            iv[i].setVisibility(View.VISIBLE);
            bit.display(iv[i],imgList.get(i));
        }
    }

    /**
     * 进行网络请求
     */
    private void requestNet() {
        NetHead head = new NetHead(AcquireItemActivity.this);
        RequestParams params = head.setHeader();
        if (TextUtils.isEmpty(tenantId + "")) {
            return;
        }
        params.addBodyParameter("tenantId", tenantId + "");
        params.addBodyParameter("userId", useId);
//        params.addBodyParameter("userId", 137 + "");           //测试用
        HttpUtils http = new HttpUtils();
        http.send(HttpRequest.HttpMethod.POST, UrlsUtils.acquireDetail, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Toast.makeText(AcquireItemActivity.this, StreamTools.getString(),
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
                        parse2Bean(mResult);
                    }
                });
    }

    /**
     * 获取SharedPreference里面的userId
     */
    private String useId;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private void getUseId() {
        sp = AcquireItemActivity.this.getSharedPreferences("load", MODE_PRIVATE);
//        editor = sp.edit();
        useId = sp.getString("userID", "");
//        Toast.makeText(AcquireItemActivity.this, "userID" + useId, Toast.LENGTH_LONG).show();
    }

    private void setOnClick() {
        iv_call.setOnClickListener(this);
        iv_collection.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        iv_collection.setOnClickListener(this);
    }


    private AlertDialog dialog;
    private View mView;
    private TextView tv_cancal;
    private TextView tv_confirm;
    private TextView tv_make_call;

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_call:
                if (!acquireDatailBean.getPhone().equals("null")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    if (dialog == null) {
                        dialog = builder.create();
                    }
                    dialog.setView(mView, 0, 0, 0, 0);
                    tv_cancal = (TextView) mView.findViewById(R.id.tv_cancal);
                    tv_confirm = (TextView) mView.findViewById(R.id.tv_confirm);
                    tv_make_call = ((TextView) mView.findViewById(R.id.tv_make_call));
                    String phone = acquireDatailBean.getPhone();                      //没有实例化
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
                }
                break;
            case R.id.iv_collection:    //进行收藏房源的请求
                if (delete) {
                    collection();            //收藏
                } else {
                    netdelete();             //删除
                }

                break;
            case R.id.iv_back:
                finish();
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
        }
    }

    protected void phoneCall() {
//        String phone = tv_my_telphone.getText().toString().trim();
        String phone = acquireDatailBean.getPhone();   //测试用
        if (TextUtils.isEmpty(phone)) {
//            phone = "01057490090";
            return;
        }
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel://" + phone));
        startActivity(intent);
    }

    /**
     * 解析数据
     */
    private List<String> imgList = new ArrayList<String>();   //将图片放在集合中，现在没用
    private AcquireDatailBean acquireDatailBean;

    private void parse2Bean(String str) {
        try {
            acquireDatailBean = new AcquireDatailBean();
            JSONObject object1 = new JSONObject(str);
            String msg = object1.getString("msg");
            acquireDatailBean.setMsg(msg);
            String status = object1.getString("status");
//            if (status.equals("N")) {            //没有权限
//                finish();
//                Toast.makeText(AcquireItemActivity.this, "请先发布合租信息", Toast.LENGTH_LONG).show();
//            }
            acquireDatailBean.setStatus(status);
            JSONObject data = object1.getJSONObject("data");
            String code = data.getString("code");
            acquireDatailBean.setCode(code);
            String schoolName = data.getString("schoolName");
            acquireDatailBean.setSchoolName(schoolName);
            String createTimeStr = data.getString("createTimeStr");
            acquireDatailBean.setCreateTimeStr(createTimeStr);
            String description = data.getString("description");
            acquireDatailBean.setDescription(description);
            int tenantId = data.getInt("tenantId");
            acquireDatailBean.setTenantId(tenantId);
            String phone = data.getString("phone");
            acquireDatailBean.setPhone(phone);
            String sex = data.getString("sex");
            acquireDatailBean.setSex(sex);
            String jobName = data.getString("jobName");
            acquireDatailBean.setJobName(jobName);
            String name = data.getString("name");
            acquireDatailBean.setName(name);
            String age = data.getString("age");
            acquireDatailBean.setAge(age);
            String business = data.getString("business");
            acquireDatailBean.setBusiness(business);
            String city = data.getString("city");
            acquireDatailBean.setCity(city);
            String constellation = data.getString("constellation");
            acquireDatailBean.setConstellation(constellation);
            String checkInTimeStr = data.getString("checkInTimeStr");
            acquireDatailBean.setCheckInTimeStr(checkInTimeStr);
            String district = data.getString("district");
            acquireDatailBean.setDistrict(district);
            String interest = data.getString("interest");
            acquireDatailBean.setInterest(interest);
            String userId = data.getString("userId");
            acquireDatailBean.setUserId(userId);
            String rent = data.getString("rent");
            acquireDatailBean.setRent(rent);
            String nativePlaceName = data.getString("nativePlaceName");
            acquireDatailBean.setNativePlaceName(nativePlaceName);
            String headImg = data.getString("headImg");
            acquireDatailBean.setHeadImg(headImg);
            JSONArray arr = data.getJSONArray("listAU");
            for (int i = 0; i < arr.length(); i++) {
                String imgUrl = arr.getString(i);
                imgList.add(imgUrl);
            }

            setData();
            Log.e("TAG22222222222222", "mResult==" + acquireDatailBean.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 收藏
     */
    private HttpUtils http;

    private void collection() {
        // 判断是否登录
        String userID = SPUtils.getUserID(AcquireItemActivity.this);
        if (TextUtils.isEmpty(userID)) {
            Intent intent = new Intent(AcquireItemActivity.this, LoagingActivity.class);
            startActivity(intent);
        } else {
            boolean b = isConnection();
            if (!b) {
                return;
            }
            dialogUtils.show();
            NetHead head = new NetHead(AcquireItemActivity.this);
            RequestParams params = head.setHeader();

            params.addBodyParameter("userId", SPUtils.getUserID(AcquireItemActivity.this));
//            params.addBodyParameter("userId", 137 + "");              //测试用
            params.addBodyParameter("tenantId", tenantId + "");
            if (http == null) {
                http = new HttpUtils();
            }
            http.send(HttpRequest.HttpMethod.POST, UrlsUtils.acquireCollection, params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            dialogUtils.closeDialog();
                            Toast.makeText(AcquireItemActivity.this, StreamTools.getString(), Toast.LENGTH_SHORT).show();
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
                                            .setImageResource(R.drawable.details_collect_link2);
                                    Toast.makeText(AcquireItemActivity.this, "此房源已经收藏",
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    iv_collection
                                            .setImageResource(R.drawable.details_collect_link2);
                                    Toast.makeText(AcquireItemActivity.this, "收藏成功",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    /**
     * 删除收藏
     */
    private void netdelete() {
        // 判断是否登录
        String userID = SPUtils.getUserID(AcquireItemActivity.this);
        if (TextUtils.isEmpty(userID)) {
            Intent intent = new Intent(AcquireItemActivity.this, LoagingActivity.class);
            startActivity(intent);
        } else {
            boolean b = isConnection();
            if (!b) {
                return;
            }
            if (!isFirst) {
                dialogUtils.show();
            }
            NetHead head = new NetHead(AcquireItemActivity.this);
            RequestParams params = head.setHeader();
            params.addBodyParameter("userId", SPUtils.getUserID(AcquireItemActivity.this));
//            params.addBodyParameter("userId", 137 + "");              //测试用
            params.addBodyParameter("tenantId", tenantId + "");
//            Log.e("CCCCCCCCCCCC", "*******************"+listBean.get(deleteTag).getId()+"");
            if (http == null) {
                http = new HttpUtils();
            }
            http.send(HttpRequest.HttpMethod.POST, UrlsUtils.acquireDelCollection, params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
                            dialogUtils.closeDialog();
                            Toast.makeText(AcquireItemActivity.this, "网络连接超时",
                                    Toast.LENGTH_SHORT).show();
                            isFirst = false;
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
                            delete = true;
                            dialogUtils.closeDialog();
                            String result = arg0.result;
                            Log.e("CCCCCCCCCCCC", "*******************" + result);
                            if (isFirst) {

                            } else {
                                Toast.makeText(AcquireItemActivity.this, "取消收藏成功",
                                        Toast.LENGTH_SHORT).show();
                            }
                            iv_collection
                                    .setImageResource(R.drawable.details_collect2);
                            isFirst = false;
                        }
                    });
        }
    }


    /**
     * 判断是否有网
     */
    private boolean isConnection() {
        int type = NetworkUtils.getConnctedType(this);
        if (type == -1) {
            Toast.makeText(AcquireItemActivity.this, AcquireItemActivity.this.getResources().getText(R.string.net_connection), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void mDialog() {
        dialogUtils = new DialogUtils(AcquireItemActivity.this);
        dialogUtils.show();
    }

    /**
     * 第一次判断是否收藏
     */
    private boolean isFirst = true;

    private void isFirstCollection() {
        // 判断是否登录
        String userID = SPUtils.getUserID(AcquireItemActivity.this);
        if (TextUtils.isEmpty(userID)) {
            Intent intent = new Intent(AcquireItemActivity.this, LoagingActivity.class);
            startActivity(intent);
        } else {
            boolean b = isConnection();
            if (!b) {
                return;
            }
//            dialogUtils.show();
            NetHead head = new NetHead(AcquireItemActivity.this);
            RequestParams params = head.setHeader();

            params.addBodyParameter("userId", SPUtils.getUserID(AcquireItemActivity.this));
//            params.addBodyParameter("userId", 137 + "");              //测试用
            params.addBodyParameter("tenantId", tenantId + "");
            if (http == null) {
                http = new HttpUtils();
            }
            http.send(HttpRequest.HttpMethod.POST, UrlsUtils.acquireCollection, params,
                    new RequestCallBack<String>() {
                        @Override
                        public void onFailure(HttpException arg0, String arg1) {
//                            dialogUtils.closeDialog();
//                            Toast.makeText(AcquireItemActivity.this, StreamTools.getString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(ResponseInfo<String> arg0) {
//                            delete = false;
//                            dialogUtils.closeDialog();
                            String result = arg0.result;
                            Log.e("WWWWW", "result****" + result);

                            JSONObject josn = null;
                            try {
                                josn = new JSONObject(result);
                                String statue = josn.getString("status");
                                if ("N".equals(statue)) {
                                    iv_collection
                                            .setImageResource(R.drawable.details_collect_link2);
                                    delete = false;
//                                    Toast.makeText(AcquireItemActivity.this, "此房源已经收藏",
//                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    iv_collection
                                            .setImageResource(R.drawable.details_collect2);
                                    delete = true;
//                                    Toast.makeText(AcquireItemActivity.this, "取消收藏成功",
//                                            Toast.LENGTH_SHORT).show();
                                    //在这里要删除收藏
                                    netdelete();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }
}
