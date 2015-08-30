package com.mobile.liujiucheng.sixninecheng.main.activityliu;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;


import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import com.mobile.liujiucheng.main.utils.DialogUtils;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.NetworkUtils;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.sixninecheng.main.BaseActivity;
import com.mobile.liujiucheng.sixninecheng.main.LoagingActivity;
import com.mobile.liujiucheng.sixninecheng.main.R;
import com.mobile.liujiucheng.sixninecheng.main.SPUtils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PandaRentalActivity extends BaseActivity {
    //按顺序声明编辑框
    private EditText panda_rental_plot_et, panda_rental_area_et2, panda_rental_area_et, panda_rental_type_et,
            panda_rental_floor_et, panda_rental_floor2_et,
            panda_rental_address_et, panda_rental_size_et, panda_rental_price_et,
            panda_rental_linkman_et, panda_rental_title_et,
            panda_rental_tel_et;
    private Map<String, String> map_pandaRental = new HashMap<>();
    //配置的checkBox
    private CheckBox panda_rental_deploy_cb1, panda_rental_deploy_cb2, panda_rental_deploy_cb3,
            panda_rental_deploy_cb4, panda_rental_deploy_cb5, panda_rental_deploy_cb6,
            panda_rental_deploy_cb7, panda_rental_deploy_cb8, panda_rental_deploy_cb9;
    //添加室友的checkBox
    private CheckBox panda_rental_roommate_checkbox1, panda_rental_roommate_checkbox2,
            panda_rental_roommate_checkbox3, panda_rental_roommate_checkbox4;
    private Context context = PandaRentalActivity.this;
    private Button confirm;
    private TextView panda_rental_roommate_add;//添加室友
    private ImageView panda_rental_exit;
    private RelativeLayout panda_rental_photo_left, panda_rental_photo_right;
    private HttpUtils http;
    private Map<String, String> map_addRoommate;
    private BitmapUtils bitmapUtils;
    private Map<String, String> map_imagePath = new HashMap<String, String>();
    private DialogUtils utils;
    private RelativeLayout panda_rental_state_rl, panda_rental_towards, panda_rental_payment;
    private TextView panda_rental_state_tv2, panda_rental_towards_tv2, panda_rental_payment_tv2;
    private static final int ROOMMATE_REQUEST_CODE = 9;
    private static final int TOWARDS_REQUEST_CODE = 2;
    private static final int STATE_REQUEST_CODE = 3;
    private static final int IMAGE_REQUEST_CODE = 5;
    private static final int PAYMENT_REQUEST_CODE = 5;
    private List<String> list_img_bottom;
    private ViewPager viewPager;
    private List<ImageView> list;
    private int index = 0;
    private String userID = null;
    private boolean flag = false;
    private String houseId = null;
    private boolean flag1 = false, flag2 = false, flag3 = false, flag4 = false, flag5 = false, flag6 = false;
    private HttpUtils http3;
    private File dir;
    private String tenantId;
    private Map<String,String> map_panda,map_panda_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panda_rental);
        userID = SPUtils.getUserID(getBaseContext());
        if (userID == null) {
            Intent intent = new Intent(this, LoagingActivity.class);
            startActivity(intent);
        }
        initView();
        dir = PandaRentalActivity.this.getFilesDir();
        Intent intent = getIntent();
        if ("合租".equals(intent.getExtras().getString("from"))) {
            houseId = intent.getExtras().getString("houseId");
            loadInfo();
            flag = true;
        }
    }


    private void loadInfo() {
        if(utils==null){
            utils=new DialogUtils(context);
        }
        utils.show();

        boolean isConnect= NetworkUtils.isConnection(context);
        if(isConnect) {
            NetHead head = new NetHead(context);
            RequestParams params = head.setHeader();

            params.addBodyParameter("houseId", houseId);
            if (httpUtils == null) {
                httpUtils = new HttpUtils();
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
                                JSONArray jsonArray = jsonObject1.getJSONArray("imageList");
                                if (list_img_bottom == null) {
                                    list_img_bottom = new ArrayList<String>();
                                } else {
                                    list_img_bottom.clear();
                                }
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    String str = jsonArray.getString(i);
                                    list_img_bottom.add(str);
                                }
                                if (jsonObject1.getJSONArray("listTenant") != null) {
                                    tenantId = jsonObject1.getJSONArray("listTenant").getJSONObject(0).getString("id");
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
                        showInfoModify(map_panda,map_panda_add);
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


    private void showInfoModify(Map<String, String> map, Map<String, String> map2) {
        panda_rental_plot_et.setText(map.get("tenement"));
        panda_rental_area_et2.setText(map.get("bussinessarea"));
        panda_rental_area_et.setText(map.get("district"));
        panda_rental_type_et.setText(map.get("houseType"));
        panda_rental_floor_et.setText(map.get("rentFloor"));
        panda_rental_floor2_et.setText(map.get("floor"));
        panda_rental_address_et.setText(map.get("address"));
        panda_rental_size_et.setText(map.get("spec"));
        panda_rental_price_et.setText(map.get("rent"));
        panda_rental_linkman_et.setText(map.get("linkMan"));
        panda_rental_title_et.setText(map.get("title"));
        panda_rental_tel_et.setText(map.get("linkPhone"));
        panda_rental_state_tv2.setText(map.get("description"));
        panda_rental_towards_tv2.setText(map.get("aspect"));

        if ("0".equals(map.get("payStyle"))) {
            panda_rental_payment_tv2.setText("月付");
        }
        if ("1".equals(map.get("payStyle"))) {
            panda_rental_payment_tv2.setText("压一付一");
        }
        if ("2".equals(map.get("payStyle"))) {
            panda_rental_payment_tv2.setText("压一付二");
        }
        if ("3".equals(map.get("payStyle"))) {
            panda_rental_payment_tv2.setText("压一付三");
        }
        if ("4".equals(map.get("payStyle"))) {
            panda_rental_payment_tv2.setText("压二付一");
        }
        if ("5".equals(map.get("payStyle"))) {
            panda_rental_payment_tv2.setText("压二付二");
        }
        if ("6".equals(map.get("payStyle"))) {
            panda_rental_payment_tv2.setText("压二付三");
        }
        if ("7".equals(map.get("payStyle"))) {
            panda_rental_payment_tv2.setText("半年付");
        }
        if ("8".equals(map.get("payStyle"))) {
            panda_rental_payment_tv2.setText("年付");
        }
        if ("9".equals(map.get("payStyle"))) {
            panda_rental_payment_tv2.setText("面议");
        }

        String[] configs = map.get("config").split("/");
        for (int i = 0; i < configs.length; i++) {
            if (configs[i].equals("电视")) {
                panda_rental_deploy_cb1.setChecked(true);
            } else if (configs[i].equals("家具")) {
                panda_rental_deploy_cb2.setChecked(true);
            } else if (configs[i].equals("暖气")) {
                panda_rental_deploy_cb3.setChecked(true);
            } else if (configs[i].equals("煤气")) {
                panda_rental_deploy_cb4.setChecked(true);
            } else if (configs[i].equals("宽带")) {
                panda_rental_deploy_cb5.setChecked(true);
            } else if (configs[i].equals("冰箱")) {
                panda_rental_deploy_cb6.setChecked(true);
            } else if (configs[i].equals("空调")) {
                panda_rental_deploy_cb7.setChecked(true);
            } else if (configs[i].equals("热水器")) {
                panda_rental_deploy_cb8.setChecked(true);
            } else if (configs[i].equals("洗衣机")) {
                panda_rental_deploy_cb9.setChecked(true);
            }
        }
        if (map2 != null) {
            if ("主卧".equals(map2.get("houseName"))) {
                panda_rental_roommate_checkbox1.setChecked(true);
            }
            if ("次卧".equals(map2.get("houseName"))) {
                panda_rental_roommate_checkbox2.setChecked(true);
            }
            if ("单间".equals(map2.get("houseName"))) {
                panda_rental_roommate_checkbox3.setChecked(true);
            }
            if ("隔断".equals(map2.get("houseName"))) {
                panda_rental_roommate_checkbox4.setChecked(true);
            }
        }
        for (int i = 0; i < list_img_bottom.size(); i++) {
            bitmapUtils.display(list.get(i), list_img_bottom.get(i));
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ROOMMATE_REQUEST_CODE && resultCode == 6
                && null != data) {
            map_addRoommate = new HashMap<>();
            map_addRoommate.put("name", data.getExtras().getString("name"));//室友姓名
            map_addRoommate.put("tel", data.getExtras().getString("tel"));
            map_addRoommate.put("picturePath", data.getExtras().getString("picturePath"));
            map_addRoommate.put("sex", data.getExtras().getString("sex"));
            map_addRoommate.put("age", data.getExtras().getString("age"));
            map_addRoommate.put("interest", data.getExtras().getString("interest"));
            map_addRoommate.put("constellation", data.getExtras().getString("constellation"));
            map_addRoommate.put("hometown", data.getExtras().getString("hometown"));
            map_addRoommate.put("school", data.getExtras().getString("school"));
            map_addRoommate.put("job", data.getExtras().getString("job"));
            map_addRoommate.put("houseStyle", data.getExtras().getString("houseStyle"));
            Toast.makeText(this, "室友添加成功", Toast.LENGTH_LONG).show();
            if ("主卧".equals(data.getExtras().getString("houseStyle"))) {
                panda_rental_roommate_checkbox1.setChecked(true);
            }
            if ("次卧".equals(data.getExtras().getString("houseStyle"))) {
                panda_rental_roommate_checkbox2.setChecked(true);
            }
            if ("单间".equals(data.getExtras().getString("houseStyle"))) {
                panda_rental_roommate_checkbox3.setChecked(true);
            }
            if ("隔断".equals(data.getExtras().getString("houseStyle"))) {
                panda_rental_roommate_checkbox4.setChecked(true);
            }
        }

        //获取到最下面的三张图片的路径

        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                if (map.get("tag").equals("0")) {
                    flag1 = true;
                    bitmapUtils.display(list.get(0), picturePath);
                    map_imagePath.put("panda_one", picturePath);
                } else if (map.get("tag").equals("1")) {
                    flag2 = true;
                    bitmapUtils.display(list.get(1), picturePath);
                    map_imagePath.put("panda_two", picturePath);
                } else if (map.get("tag").equals("2")) {
                    flag3 = true;
                    bitmapUtils.display(list.get(2), picturePath);
                    map_imagePath.put("panda_three", picturePath);
                } else if (map.get("tag").equals("3")) {
                    flag4 = true;
                    bitmapUtils.display(list.get(3), picturePath);
                    map_imagePath.put("panda_four", picturePath);
                } else if (map.get("tag").equals("4")) {
                    flag5 = true;
                    bitmapUtils.display(list.get(4), picturePath);
                    map_imagePath.put("panda_five", picturePath);
                } else if (map.get("tag").equals("5")) {
                    flag6 = true;
                    bitmapUtils.display(list.get(5), picturePath);
                    map_imagePath.put("panda_six", picturePath);
                }
                cursor.close();
            } else {
                Log.e("TAG", "cursor == " + cursor);
            }
        }

        //获取描述界面的传值
        if (requestCode == STATE_REQUEST_CODE && resultCode == 3
                && null != data) {
            String str = data.getExtras().getString("state");
            panda_rental_state_tv2.setText(str);
        }

        //获取到朝向的数据
        if (requestCode == TOWARDS_REQUEST_CODE && resultCode == 2
                && null != data) {
            String str = data.getStringExtra("towards");
            panda_rental_towards_tv2.setText(str);
        }
        //获取到付款方式的数据
        if (requestCode == PAYMENT_REQUEST_CODE && resultCode == 4
                && null != data) {
            String str = data.getStringExtra("paymentString");
            panda_rental_payment_tv2.setText(str);
        }
    }

    private Map<String, String> map;

    /**
     * 上传图片
     */
    private void uploadPictures(String tag) {
        if (map == null) {
            map = new HashMap<String, String>();
        }
        map.put("tag", tag);
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// external_content_uri
        startActivityForResult(i, IMAGE_REQUEST_CODE);
    }

    private void initView() {
        panda_rental_plot_et = (EditText) findViewById(R.id.panda_rental_plot_et);
        panda_rental_area_et = (EditText) findViewById(R.id.panda_rental_area_et);
        panda_rental_type_et = (EditText) findViewById(R.id.panda_rental_type_et);
        panda_rental_floor_et = (EditText) findViewById(R.id.panda_rental_floor_et);
        panda_rental_floor2_et = (EditText) findViewById(R.id.panda_rental_floor2_et);
        panda_rental_address_et = (EditText) findViewById(R.id.panda_rental_address_et);
        panda_rental_size_et = (EditText) findViewById(R.id.panda_rental_size_et);
        panda_rental_price_et = (EditText) findViewById(R.id.panda_rental_price_et);
        panda_rental_linkman_et = (EditText) findViewById(R.id.panda_rental_linkman_et);
        panda_rental_tel_et = (EditText) findViewById(R.id.panda_rental_tel_et);
        panda_rental_area_et2 = (EditText) findViewById(R.id.panda_rental_area_et2);
        panda_rental_state_tv2 = (TextView) findViewById(R.id.panda_rental_state_tv2);
        panda_rental_title_et = (EditText) findViewById(R.id.panda_rental_title_et);


        //朝向的选择
        panda_rental_towards = (RelativeLayout) findViewById(R.id.panda_rental_towards);
        panda_rental_towards_tv2 = (TextView) findViewById(R.id.panda_rental_towards_tv2);
        panda_rental_towards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PandaRentalActivity.this, PandaRentalCheckTowardsActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        //付款方式的选择
        panda_rental_payment_tv2 = (TextView) findViewById(R.id.panda_rental_payment_tv2);
        panda_rental_payment = (RelativeLayout) findViewById(R.id.panda_rental_payment);
        panda_rental_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PandaRentalActivity.this, PandaRentalCheckPayActivity.class);
                startActivityForResult(intent, PAYMENT_REQUEST_CODE);
            }
        });

        //配置的checkBox的初始化
        initCheckBox();

        //底部的图片
        viewPager = (ViewPager) findViewById(R.id.panda_rental_photo_vp);
        list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setPadding(5, 0, 5, 0);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setImageResource(R.drawable.release_picture_background);
            final int finalI = i;
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadPictures(Integer.toString(finalI));
                }
            });
            list.add(imageView);
        }
        viewPager.setAdapter(new MyPagerAdapter(list));
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                index = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        panda_rental_photo_left = (RelativeLayout) findViewById(R.id.panda_rental_photo_left);
        panda_rental_photo_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(--index);
            }
        });
        panda_rental_photo_right = (RelativeLayout) findViewById(R.id.panda_rental_photo_right);
        panda_rental_photo_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(++index);
            }
        });

        bitmapUtils = new BitmapUtils(context);

        //描述
        panda_rental_state_rl = (RelativeLayout) findViewById(R.id.panda_rental_state_rl);
        panda_rental_state_rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PandaRentalActivity.this, PandaRentalStateActivity.class);
                startActivityForResult(intent, STATE_REQUEST_CODE);
            }
        });


        //返回按钮
        panda_rental_exit = (ImageView) findViewById(R.id.panda_rental_exit);
        panda_rental_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //确定发布
        confirm = (Button) findViewById(R.id.panda_rental_confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
                if (getData()) {
                    map_pandaRental.put("config", getCheckBoxString());
                    netWork();
                }
            }
        });

        //添加室友
        panda_rental_roommate_add = (TextView) findViewById(R.id.panda_rental_roommate_add);

        panda_rental_roommate_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PandaRentalActivity.this, AddRoommateActivity.class);
                Bundle bundle = new Bundle();
                if (flag) {
                    bundle.putBoolean("flag", flag);
                    bundle.putString("houseId",houseId);
                    bundle.putString("tenantId",tenantId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else {
                    bundle.putBoolean("flag", false);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, ROOMMATE_REQUEST_CODE);

                }

            }
        });
    }


    private void initCheckBox() {
        panda_rental_deploy_cb1 = (CheckBox) findViewById(R.id.panda_rental_deploy_cb1);
        panda_rental_deploy_cb2 = (CheckBox) findViewById(R.id.panda_rental_deploy_cb2);
        panda_rental_deploy_cb3 = (CheckBox) findViewById(R.id.panda_rental_deploy_cb3);
        panda_rental_deploy_cb4 = (CheckBox) findViewById(R.id.panda_rental_deploy_cb4);
        panda_rental_deploy_cb5 = (CheckBox) findViewById(R.id.panda_rental_deploy_cb5);
        panda_rental_deploy_cb6 = (CheckBox) findViewById(R.id.panda_rental_deploy_cb6);
        panda_rental_deploy_cb7 = (CheckBox) findViewById(R.id.panda_rental_deploy_cb7);
        panda_rental_deploy_cb8 = (CheckBox) findViewById(R.id.panda_rental_deploy_cb8);
        panda_rental_deploy_cb9 = (CheckBox) findViewById(R.id.panda_rental_deploy_cb9);

        panda_rental_roommate_checkbox1 = (CheckBox) findViewById(R.id.panda_rental_roommate_checkbox1);
        panda_rental_roommate_checkbox2 = (CheckBox) findViewById(R.id.panda_rental_roommate_checkbox2);
        panda_rental_roommate_checkbox3 = (CheckBox) findViewById(R.id.panda_rental_roommate_checkbox3);
        panda_rental_roommate_checkbox4 = (CheckBox) findViewById(R.id.panda_rental_roommate_checkbox4);


    }

    //    判断所需要必填的信息是否为空
    public boolean getData() {
        if (map_pandaRental == null) {
            map_pandaRental = new HashMap<String, String>();
        }

        String panda_rental_plot = panda_rental_plot_et.getText().toString().trim();
        String null1 = judgeNull(panda_rental_plot, "小区不能为空");
        if (null1 == null) {
            return false;
        }
        String panda_rental_area = panda_rental_area_et.getText().toString().trim();
        String null2 = judgeNull(panda_rental_area, "区域不能为空");
        if (null2 == null) {
            return false;
        }
        String panda_rental_area2 = panda_rental_area_et2.getText().toString().trim();
        String null21 = judgeNull(panda_rental_area, "区域不能为空");
        if (null21 == null) {
            return false;
        }
        String panda_rental_type = panda_rental_type_et.getText().toString().trim();
        String null3 = judgeNull(panda_rental_type, "户型不能为空");
        if (null3 == null) {
            return false;
        }
        String panda_rental_floor = panda_rental_floor_et.getText().toString().trim();
        String null4 = judgeNull(panda_rental_floor, "楼层不能为空");
        if (null4 == null) {
            return false;
        }
        String panda_rental_floor2 = panda_rental_floor2_et.getText().toString().trim();
        String null5 = judgeNull(panda_rental_floor2, "总楼层不能为空");
        if (null5 == null) {
            return false;
        }
        String panda_rental_towards = panda_rental_towards_tv2.getText().toString().trim();
        String null6 = judgeNull(panda_rental_towards, "朝向不能为空");
        map_pandaRental.put("towards", panda_rental_towards);
        if (null6 == null) {
            return false;
        }
        String panda_rental_address = panda_rental_address_et.getText().toString().trim();
        String null7 = judgeNull(panda_rental_address, "地址不能为空");
        if (null7 == null) {
            return false;
        }
        String panda_rental_size = panda_rental_size_et.getText().toString().trim();
        String null8 = judgeNull(panda_rental_size, "面积不能为空");
        if (null8 == null) {
            return false;
        }
        String panda_rental_price = panda_rental_price_et.getText().toString().trim();
        String null9 = judgeNull(panda_rental_price, "价格不能为空");
        if (null9 == null) {
            return false;
        }
        String panda_rental_payment = panda_rental_payment_tv2.getText().toString().trim();
        String null10 = judgeNull(panda_rental_payment, "付款方式不能为空");
        if (null10 == null) {
            return false;
        }
        if ("月付".equals(panda_rental_payment)) {
            map_pandaRental.put("payment", "0");
        }
        if ("压一付一".equals(panda_rental_payment)) {
            map_pandaRental.put("payment", "1");
        }
        if ("压一付二".equals(panda_rental_payment)) {
            map_pandaRental.put("payment", "2");
        }
        if ("压一付三".equals(panda_rental_payment)) {
            map_pandaRental.put("payment", "3");
        }
        if ("压二付一".equals(panda_rental_payment)) {
            map_pandaRental.put("payment", "4");
        }
        if ("压二付二".equals(panda_rental_payment)) {
            map_pandaRental.put("payment", "5");
        }
        if ("压二付三".equals(panda_rental_payment)) {
            map_pandaRental.put("payment", "6");
        }
        if ("半年付".equals(panda_rental_payment)) {
            map_pandaRental.put("payment", "7");
        }
        if ("年付".equals(panda_rental_payment)) {
            map_pandaRental.put("payment", "8");
        }
        if ("面议".equals(panda_rental_payment)) {
            map_pandaRental.put("payment", "9");
        }
        String panda_rental_linkman = panda_rental_linkman_et.getText().toString().trim();
        String null12 = judgeNull(panda_rental_linkman, "联系人不能为空");
        if (null12 == null) {
            return false;
        }
        String panda_rental_tel = panda_rental_tel_et.getText().toString().trim();
        String null13 = judgeNull(panda_rental_tel, "手机号不能为空");
        if (null13 == null) {
            return false;
        }
        String panda_rental_title = panda_rental_title_et.getText().toString().trim();
        String null15 = judgeNull(panda_rental_title, "标题不能为空");
        if (null15 == null) {
            return false;
        }
        String state = panda_rental_state_tv2.getText().toString().trim();
        String null16 = judgeNull(panda_rental_title, "描述不能为空");
        if (null16 == null) {
            return false;
        }

        map_pandaRental.put("plot", panda_rental_plot);
        map_pandaRental.put("area", panda_rental_area);
        map_pandaRental.put("type", panda_rental_type);
        map_pandaRental.put("area2", panda_rental_area2);
        map_pandaRental.put("floor", panda_rental_floor);
        map_pandaRental.put("floor2", panda_rental_floor2);
        map_pandaRental.put("address", panda_rental_address);
        map_pandaRental.put("size", panda_rental_size);
        map_pandaRental.put("price", panda_rental_price);
        map_pandaRental.put("linkman", panda_rental_linkman);
        map_pandaRental.put("tel", panda_rental_tel);
        map_pandaRental.put("title", panda_rental_title);

        map_pandaRental.put("description", state);
        return true;
    }

    //判断是否为空
    private String judgeNull(String data, String descriptor) {
        if (TextUtils.isEmpty(data)) {
            Toast.makeText(this, descriptor, Toast.LENGTH_SHORT).show();
            return null;
        }
        return data;
    }


    private HttpUtils httpUtils;


    //上传信息
    private void netWork() {
        utils=new DialogUtils(context);
        utils.show();
        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(map_imagePath.get("panda_one"))) {
            Bitmap bitmap = SubmitImage.getSmallBitmap(map_imagePath.get("panda_one"));
            File file = new File(dir, "panda1.jpeg");
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            params.addBodyParameter("panda_one", file);

        }

        if (!TextUtils.isEmpty(map_imagePath.get("panda_two"))) {
            Bitmap bitmap = SubmitImage.getSmallBitmap(map_imagePath.get("panda_two"));
            File file = new File(dir, "panda2.jpeg");
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            params.addBodyParameter("panda_two", file);
        }

        if (!TextUtils.isEmpty(map_imagePath.get("panda_three"))) {

            Bitmap bitmap = SubmitImage.getSmallBitmap(map_imagePath.get("panda_three"));
            File file = new File(dir, "panda3.jpeg");
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            params.addBodyParameter("panda_three", file);
        }
        if (!TextUtils.isEmpty(map_imagePath.get("panda_four"))) {
            Bitmap bitmap = SubmitImage.getSmallBitmap(map_imagePath.get("panda_four"));
            File file = new File(dir, "panda4.jpeg");
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            params.addBodyParameter("panda_four", file);
        }
        if (!TextUtils.isEmpty(map_imagePath.get("panda_five"))) {
            Bitmap bitmap = SubmitImage.getSmallBitmap(map_imagePath.get("panda_five"));
            File file = new File(dir, "panda5.jpeg");
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            params.addBodyParameter("panda_five", file);
        }
        if (!TextUtils.isEmpty(map_imagePath.get("panda_six"))) {
            Bitmap bitmap = SubmitImage.getSmallBitmap(map_imagePath.get("panda_six"));
            File file = new File(dir, "panda6.jpeg");
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } finally {
                if (outputStream != null) {
                    try {
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            params.addBodyParameter("panda_six", file);
        }
        if (TextUtils.isEmpty(map_imagePath.get("panda_one")) && TextUtils.isEmpty(map_imagePath.get("panda_two")) &&
                TextUtils.isEmpty(map_imagePath.get("panda_three")) && TextUtils.isEmpty(map_imagePath.get("panda_four"))
                && TextUtils.isEmpty(map_imagePath.get("panda_five")) && TextUtils.isEmpty(map_imagePath.get("panda_six"))) {
        }

        if (flag) {
            for (int i = 0; i < list_img_bottom.size(); i++) {
                if (i == 0) {
                    if (!flag1) {
                        if (list_img_bottom.get(0) != null) {
                            params.addBodyParameter("panda_one", bitmapUtils.getBitmapFileFromDiskCache(list_img_bottom.get(0)));
                        }
                    }
                }
                if (i == 1) {
                    if (!flag2) {
                        if (list_img_bottom.get(1) != null) {
                            params.addBodyParameter("panda_two", bitmapUtils.getBitmapFileFromDiskCache(list_img_bottom.get(1)));
                        }
                    }
                }
                if (i == 2) {
                    if (!flag3) {
                        if (list_img_bottom.get(2) != null) {
                            params.addBodyParameter("panda_three", bitmapUtils.getBitmapFileFromDiskCache(list_img_bottom.get(2)));
                        }
                    }
                }
                if (i == 3) {
                    if (!flag4) {
                        if (list_img_bottom.get(3) != null) {
                            params.addBodyParameter("panda_four", bitmapUtils.getBitmapFileFromDiskCache(list_img_bottom.get(3)));
                        }
                    }
                }
                if (i == 4) {
                    if (!flag5) {
                        if (list_img_bottom.get(4) != null) {
                            params.addBodyParameter("panda_five", bitmapUtils.getBitmapFileFromDiskCache(list_img_bottom.get(4)));
                        }
                    }
                }
                if (i == 5) {
                    if (!flag6) {
                        if (list_img_bottom.get(5) != null) {
                            params.addBodyParameter("panda_six", bitmapUtils.getBitmapFileFromDiskCache(list_img_bottom.get(5)));
                        }
                    }
                }
            }
        }
        params.addBodyParameter("userId", userID);
        params.addBodyParameter("city", "北京");
        if (flag) {
            params.addBodyParameter("houseId", houseId);
        }
        params.addBodyParameter("tenement", map_pandaRental.get("plot"));//小区
        params.addBodyParameter("district", map_pandaRental.get("area"));//区域
        params.addBodyParameter("bussiness", map_pandaRental.get("area2"));//商圈
        params.addBodyParameter("houseType", map_pandaRental.get("type"));//户型
        params.addBodyParameter("rentFloor", map_pandaRental.get("floor"));//第几层
        params.addBodyParameter("floor", map_pandaRental.get("floor2"));//楼层数
        params.addBodyParameter("aspect", map_pandaRental.get("towards"));//朝向
        params.addBodyParameter("address", map_pandaRental.get("address"));//地址
        params.addBodyParameter("spec", map_pandaRental.get("size"));//面积
        params.addBodyParameter("rent", map_pandaRental.get("price"));//租金
        params.addBodyParameter("payStyle", map_pandaRental.get("payment"));//付款方式                                                               //描述
        params.addBodyParameter("linkMan", map_pandaRental.get("linkman"));//联系人
        params.addBodyParameter("linkPhone", map_pandaRental.get("tel"));//手机号
        params.addBodyParameter("config", map_pandaRental.get("config"));//配置
        params.addBodyParameter("title", map_pandaRental.get("title"));
        params.addBodyParameter("description", map_pandaRental.get("description"));
        if (http == null) {
            http = new HttpUtils();
        }

        String url = null;
        if (!flag) {
            url = UrlsUtils.urlUploadPandaRelease;
        } else {
            url = UrlsUtils.urlMyReleasePandaModify;
        }
        http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {

                Toast.makeText(getApplicationContext(), "上传失败，请检查网络或填写的信息", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "解析失败");
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {

                Toast.makeText(getApplicationContext(), "上传成功,等待审核", Toast.LENGTH_LONG).show();
                if (map_addRoommate != null && !flag) {
                    RequestParams params2 = new RequestParams();
                    try {
                        JSONObject object = new JSONObject(arg0.result.toString());
                        JSONObject object1 = object.getJSONObject("data");
                        String str = object1.getString("houseId");
                        params2.addBodyParameter("houseId", str);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (!TextUtils.isEmpty(map_addRoommate.get("picturePath"))) {
                        Bitmap bitmap = SubmitImage.getSmallBitmap(map_addRoommate.get("picturePath"));
                        File file = new File(dir, "panda_roommate.jpeg");
                        FileOutputStream outputStream = null;
                        try {
                            outputStream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 40, outputStream);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } finally {
                            if (outputStream != null) {
                                try {
                                    outputStream.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        params2.addBodyParameter("file", file);

                    }
                    params2.addBodyParameter("userId", userID);
                    params2.addBodyParameter("sex", map_addRoommate.get("sex"));
                    params2.addBodyParameter("name", map_addRoommate.get("name"));
                    params2.addBodyParameter("phone", map_addRoommate.get("tel"));
                    params2.addBodyParameter("age", map_addRoommate.get("age"));
                    params2.addBodyParameter("interest", map_addRoommate.get("interest"));
                    params2.addBodyParameter("constellation", map_addRoommate.get("constellation"));
                    params2.addBodyParameter("nativePlace", map_addRoommate.get("hometown"));
                    params2.addBodyParameter("school", map_addRoommate.get("school"));
                    params2.addBodyParameter("houseName", map_addRoommate.get("houseStyle"));
                    params2.addBodyParameter("job", map_addRoommate.get("job"));

                    if (httpUtils == null) {
                        httpUtils = new HttpUtils();
                    }
                    String url2 = null;
                    if (!flag) {
                        url2 = UrlsUtils.urlAddRoommate;
                    } else {
                        url2 = UrlsUtils.urlReleaseRoommateModify;
                    }
                    httpUtils.send(HttpMethod.POST, url2, params2, new RequestCallBack<String>() {
                        @Override
                        public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                            Toast.makeText(getApplicationContext(), "室友上传成功,等待审核", Toast.LENGTH_LONG).show();
                            utils.closeDialog();
                            finish();
                        }

                        @Override
                        public void onFailure(HttpException e, String s) {
                            Toast.makeText(getApplicationContext(), "上传失败，请检查网络或填写的信息", Toast.LENGTH_SHORT).show();
                            Log.e("TAG", "解析失败" + s);
                            utils.closeDialog();
                        }
                    });
                }
            }
        });
    }

    String str = "";
    StringBuffer stringBuffer = new StringBuffer(str);
    List<String> list1 = new ArrayList<>();

    // 获取配置中多个checkBox的选中的值
    private String getCheckBoxString() {


        if (panda_rental_deploy_cb1.isChecked()) {
            list1.add("电视");
        }
        if (panda_rental_deploy_cb2.isChecked()) {
            list1.add("家具");
        }
        if (panda_rental_deploy_cb3.isChecked()) {
            list1.add("暖气");
        }
        if (panda_rental_deploy_cb4.isChecked()) {
            list1.add("煤气");
        }
        if (panda_rental_deploy_cb5.isChecked()) {
            list1.add("宽带");
        }
        if (panda_rental_deploy_cb6.isChecked()) {
            list1.add("冰箱");
        }
        if (panda_rental_deploy_cb7.isChecked()) {
            list1.add("空调");
        }
        if (panda_rental_deploy_cb8.isChecked()) {
            list1.add("热水器");
        }
        if (panda_rental_deploy_cb9.isChecked()) {
            list1.add("洗衣机");
        }
        for (int i = 0; i < list1.size(); i++) {
            stringBuffer.append(list1.get(i));
            if (!(i == list1.size() - 1)) {
                stringBuffer.append("/");
            }
        }


        return stringBuffer.toString();
    }

    public class MyPagerAdapter extends PagerAdapter {

        private List<ImageView> list;

        public MyPagerAdapter(List<ImageView> list) {
            this.list = list;
        }

        @Override
        public float getPageWidth(int position) {
            return 1 / 3f;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(list.get(position));
            return list.get(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(list.get(position));
        }
    }


}
