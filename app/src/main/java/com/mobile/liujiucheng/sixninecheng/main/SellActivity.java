package com.mobile.liujiucheng.sixninecheng.main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.mobile.liujiucheng.sixninecheng.main.activityliu.SubmitImage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 出售
 *
 * @author pc
 */
public class SellActivity extends BaseActivity implements OnClickListener {

    private EditText et_name;//小区
    private EditText et_one;//室
    private EditText et_two;//厅
    private EditText et_three;//卫
    private EditText et_floor;//楼层
    private EditText et_total;//总楼层
    private EditText et_direction;//朝向
    private EditText et_address;//地区
    private EditText et_area_house;//面积
    private EditText et_price;//价格
    private EditText et_house_title;//房子的标题
    private EditText et_house_description;//房子的描述

    private EditText et_contact;//联系人
    private EditText et_way;//联系方式

    private EditText et_area_circle;//商圈

    private EditText tv_area; //区域
    private TextView tv_decorate; //装修
    private RelativeLayout rl_decorate;//装修
    private TextView tv_property; //产权
    private RelativeLayout rl_property;//产权
    private TextView tv_class_house; //类型

    private TextView tv_title_data; //类型
    private RelativeLayout rl_class_house;//类型
    private LinearLayout rl_area;//小区
    private LinearLayout ll_feature;//特点
    //描述图片
    private ImageView iv_one;
    private ImageView iv_two;
    private ImageView iv_three;

    private Button mExit;//发布到服务器
    private Context context;

    private BitmapUtils bitmapUtils;
    private ImageView mImageView;
    private boolean flag = false;
    private TextView wy;
    private boolean flag1 = false, flag2 = false, flag3 = false;
    private File dir;

    private DialogUtils utils;
    private HttpUtils httpUtils;
    private Map<String, String> map_rent, map_second;
    private String tenantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_secondary_release);
        Intent intent = getIntent();//getIntent将该项目中包含的原始intent检索出来，将检索出来的intent赋值给一个Intent类型的变量intent
        Bundle bundle = intent.getExtras();//.getExtras()得到intent所附带的额外数据
        tag = bundle.getString("tag");
        dir = SellActivity.this.getFilesDir();
        tenantId = bundle.getString("tenantId");
        Log.e("dir", "dir" + dir.getAbsolutePath().toString());
        initView();
        if ("出租".equals(bundle.getString("from")) && "1".equals(bundle.getString("tag"))) {
            flag = true;
            loadRentInfo();
        } else if ("二手房".equals(bundle.getString("from")) && "2".equals(bundle.getString("tag"))) {
            flag = true;
            loadSecondInfo();
        }

    }

    private void loadRentInfo() {
        if (utils == null) {
            utils = new DialogUtils(context);
        }
        utils.show();
        if (map_rent == null) {
            map_rent = new HashMap<>();
        }
        if (map_rent != null) {
            map_rent.clear();
        }
        boolean isConnect = NetworkUtils.isConnection(context);
        if (isConnect) {
            NetHead head = new NetHead(context);
            RequestParams params = head.setHeader();
            params.addBodyParameter("type", "detailsHouse");
            params.addBodyParameter("sid", tenantId);
            if (httpUtils == null) {
                httpUtils = new HttpUtils();
            }
            httpUtils.send(HttpRequest.HttpMethod.POST, UrlsUtils.urlMyReleaseSecond, params, new RequestCallBack<String>() {
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
                            map_rent.put("houseType", jsonObject1.getString("houseType"));
                            map_rent.put("sid", jsonObject1.getString("sid"));
                            map_rent.put("imageJSON", jsonObject1.getString("imageJSON"));
                            map_rent.put("houseType", jsonObject1.getString("houseType"));//户型
                            map_rent.put("tenement", jsonObject1.getString("tenement"));//小区
                            map_rent.put("aspect", jsonObject1.getString("aspect"));//朝向
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("imageJSON");
                            map_rent.put("img0", jsonObject2.getString("img0"));
                            map_rent.put("img1", jsonObject2.getString("img1"));
                            map_rent.put("img2", jsonObject2.getString("img2"));

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showInfoModifyRent(map_rent);
                    utils.closeDialog();
                }

                @Override
                public void onFailure(HttpException e, String s) {

                }
            });
        }
    }

    private void loadSecondInfo() {
        if (utils == null) {
            utils = new DialogUtils(context);
        }
        utils.show();
        if (map_second == null) {
            map_second = new HashMap<>();
        }
        boolean isConnect = NetworkUtils.isConnection(context);
        if (isConnect) {
            NetHead head = new NetHead(context);
            RequestParams params = head.setHeader();
            params.addBodyParameter("type", "detailsSecondHouse");
            params.addBodyParameter("sid", tenantId);
            if (httpUtils == null) {
                httpUtils = new HttpUtils();
            }
            httpUtils.send(HttpRequest.HttpMethod.POST, UrlsUtils.urlMyReleaseSecond, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> objectResponseInfo) {

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
                            JSONObject jsonObject2 = jsonObject1.getJSONObject("imageJSON");
                            map_second.put("img0", jsonObject2.getString("img0"));
                            map_second.put("img1", jsonObject2.getString("img1"));
                            map_second.put("img2", jsonObject2.getString("img2"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showInfoModifySecond(map_second);
                    utils.closeDialog();
                }

                @Override
                public void onFailure(HttpException e, String s) {

                }
            });
        }
    }

    private void showInfoModifySecond(Map<String, String> map) {
        et_name.setText(map.get("tenement"));//小区名
        Log.e("户型", "户型" + map.get("houseType"));
        et_one.setText(map.get("houseType").charAt(0) + "");//室
        et_two.setText(map.get("houseType").charAt(2) + "");//厅
        et_three.setText(map.get("houseType").charAt(4) + "");//卫
        et_floor.setText(map.get("floor"));//楼层
        et_total.setText(map.get("rentFloor"));//总楼层
        et_direction.setText(map.get("aspect"));//方向
        et_address.setText(map.get("address"));//地址
        et_area_house.setText(map.get("spec"));//面积
        et_price.setText(map.get("rent"));//价格
        et_house_title.setText(map.get("title"));//标题
        et_house_description.setText(map.get("description"));//描述
        et_contact.setText(map.get("linkMan"));//姓名
        et_way.setText(map.get("linkPhone"));//手机号
        et_area_circle.setText(map.get("bussinessarea"));//
        tv_area.setText(map.get("district"));//区域
        tv_decorate.setText(map.get("interiordesign"));//装修
        if (tag.equals("2")) {
            tv_property.setText(map.get("property") + "年产权");//产权
            if ("2".equals(map.get("tenementType"))) {
                tv_class_house.setText("住宅");
            } else if ("3".equals(map.get("tenementType"))) {
                tv_class_house.setText("别墅");
            } else if ("5".equals(map.get("tenementType"))) {
                tv_class_house.setText("商铺");
            } else if ("6".equals(map.get("tenementType"))) {
                tv_class_house.setText("写字楼");
            }
        }
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(this);
        }
        bitmapUtils.display(iv_one, map.get("img0"));
        bitmapUtils.display(iv_two, map.get("img1"));
        bitmapUtils.display(iv_three, map.get("img2"));


    }


    private void showInfoModifyRent(Map<String, String> map) {
        et_name.setText(map.get("tenement"));//小区名
        et_one.setText(map.get("houseType").charAt(0) + "");//室
        et_two.setText(map.get("houseType").charAt(2) + "");//厅
        et_three.setText(map.get("houseType").charAt(4) + "");//卫
        et_floor.setText(map.get("floor"));//楼层
        et_total.setText(map.get("rentFloor"));//总楼层
        et_direction.setText(map.get("aspect"));//方向
        et_address.setText(map.get("address"));//地址
        et_area_house.setText(map.get("spec"));//面积
        et_price.setText(map.get("rent"));//价格
        et_house_title.setText(map.get("title"));//标题
        et_house_description.setText(map.get("description"));//描述
        et_contact.setText(map.get("linkMan"));//姓名
        et_way.setText(map.get("linkPhone"));//手机号
        et_area_circle.setText(map.get("bussinessarea"));//
        tv_area.setText(map.get("district"));//区域
        tv_decorate.setText(map.get("interiordesign"));//装修
        et_direction.setText(map.get("aspect"));//方向
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(this);
        }
        bitmapUtils.display(iv_one, map.get("img0"));
        bitmapUtils.display(iv_two, map.get("img1"));
        bitmapUtils.display(iv_three, map.get("img2"));

    }

    private void initView() {
        ll_feature = (LinearLayout) findViewById(R.id.ll_feature);//特点
        ll_feature.setVisibility(View.GONE);
        mImageView = (ImageView) findViewById(R.id.iv_exit);
        mImageView.setOnClickListener(this);
        context = this;
        if (mAddress == null) {
            mAddress = new HashMap<String, String>();
        }
        wy = (TextView) findViewById(R.id.SBCNM);
        // style= (TextView) findViewById(R.id.TESE);
        if (tag.equals("1")) {
            wy.setText("元");
            //   style.setText("特色");
        }
        bitmapUtils = new BitmapUtils(context);
        et_name = (EditText) findViewById(R.id.et_name);
        et_one = (EditText) findViewById(R.id.et_one);
        et_two = (EditText) findViewById(R.id.et_two);

        et_contact = (EditText) findViewById(R.id.et_contact);

        et_three = (EditText) findViewById(R.id.et_three);
        et_floor = (EditText) findViewById(R.id.et_floor);
        et_total = (EditText) findViewById(R.id.et_total);

        et_direction = (EditText) findViewById(R.id.et_direction);
        et_address = (EditText) findViewById(R.id.et_address);
        et_area_house = (EditText) findViewById(R.id.et_area_house);
        et_way = (EditText) findViewById(R.id.et_way);

        et_price = (EditText) findViewById(R.id.et_price);
        et_house_title = (EditText) findViewById(R.id.et_house_title);
        et_area_circle = (EditText) findViewById(R.id.et_area_circle);
        et_house_description = (EditText) findViewById(R.id.et_house_description);

        tv_area = (EditText) findViewById(R.id.tv_area);
        tv_decorate = (TextView) findViewById(R.id.tv_decorate);
        tv_property = (TextView) findViewById(R.id.tv_property);
        tv_class_house = (TextView) findViewById(R.id.tv_class_house);

        tv_title_data = (TextView) findViewById(R.id.tv_title_data);
        if (tag.equals("1")) {
            tv_title_data.setText("出租");
        }
        iv_one = (ImageView) findViewById(R.id.iv_one);
        iv_two = (ImageView) findViewById(R.id.iv_two);
        iv_three = (ImageView) findViewById(R.id.iv_three);

        rl_decorate = (RelativeLayout) findViewById(R.id.rl_decorate);
        rl_property = (RelativeLayout) findViewById(R.id.rl_property);
        rl_class_house = (RelativeLayout) findViewById(R.id.rl_class_house);
        rl_area = (LinearLayout) findViewById(R.id.rl_area);

        mExit = (Button) findViewById(R.id.bu_register);

        if (tag.equals("1")) {
            rl_property.setVisibility(View.GONE);
            rl_class_house.setVisibility(View.GONE);
        }

        rl_decorate.setOnClickListener(this);
        rl_property.setOnClickListener(this);
        rl_class_house.setOnClickListener(this);
        rl_area.setOnClickListener(this);
        mExit.setOnClickListener(this);
        iv_one.setOnClickListener(this);
        iv_two.setOnClickListener(this);
        iv_three.setOnClickListener(this);

    }

    private Map<String, String> mAddress;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                if (map.get("tag").equals("1")) {
                    bitmapUtils.display(iv_one, picturePath);

                    mAddress.put("one", picturePath);
                    flag1 = true;
                } else if (map.get("tag").equals("2")) {
                    bitmapUtils.display(iv_two, picturePath);


                    mAddress.put("two", picturePath);
                    flag2 = true;
                } else {
                    bitmapUtils.display(iv_three, picturePath);

                    mAddress.put("three", picturePath);
                    flag3 = true;
                }
                cursor.close();
            } else {
                Log.e("TAG", "cursor == " + cursor);
            }
        }
        //产权
        if (resultCode == 200) {
            String extra = data.getStringExtra("data");
            tv_property.setText(extra);
        }
        //装修
        if (resultCode == 300) {
            String extra = data.getStringExtra("data");
            tv_decorate.setText(extra);
        }
        //类型
        if (resultCode == 400) {
            String extra = data.getStringExtra("data");
            tv_class_house.setText(extra);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_exit:
                finish();
                break;
            case R.id.rl_decorate://装修
                newStart(DecorateActivity.class);
                break;
            case R.id.iv_one://传图片
                uploadPictures("1");
                break;
            case R.id.iv_two://传图片
                uploadPictures("2");
                break;
            case R.id.iv_three://传图片
                uploadPictures("3");
                break;
            case R.id.rl_property://产权
                newStart(PropertyActivity.class);
                break;
            case R.id.rl_class_house://房屋类型
                newStart(ClassHouseActivity.class);
                break;
            case R.id.bu_register://发布

                name = et_name.getText().toString().trim();
                String null1 = judgeNull(name, "小区名称不能为空");
                if (null1 == null) {
                    return;
                }
                one = et_one.getText().toString().trim();
                String null2 = judgeNull(one, "室不能为空");
                if (null2 == null) {
                    return;
                }
                two = et_two.getText().toString().trim();
                String null3 = judgeNull(two, "厅不能为空");
                if (null3 == null) {
                    return;
                }
                three = et_three.getText().toString().trim();
                String null4 = judgeNull(three, "卫不能为空");
                if (null4 == null) {
                    return;
                }
                floor = et_floor.getText().toString().trim();
                String null5 = judgeNull(floor, "楼层不能为空");
                if (null5 == null) {
                    return;
                }
                total = et_total.getText().toString().trim();
                String null6 = judgeNull(total, "总楼层不能为空");
                if (null6 == null) {
                    return;
                }
                direction = et_direction.getText().toString().trim();
                String null7 = judgeNull(direction, "房屋的方向不能为空");
                if (null7 == null) {
                    return;
                }
                address = et_address.getText().toString().trim();
                String null8 = judgeNull(address, "地区不能为空");
                if (null8 == null) {
                    return;
                }
                price = et_price.getText().toString().trim();
                String null9 = judgeNull(price, "价格不能为空");
                if (null9 == null) {
                    return;
                }
                house_title = et_house_title.getText().toString().trim();
                String null10 = judgeNull(house_title, "房子的标题不能为空");
                if (null10 == null) {
                    return;
                }
                contact = et_contact.getText().toString().trim();
                String null11 = judgeNull(contact, "联系人不能为空");
                if (null11 == null) {
                    return;
                }
                way = et_way.getText().toString().trim();
                String null12 = judgeNull(way, "联系方式不能为空");
                if (null12 == null) {
                    return;
                }
                area = tv_area.getText().toString().trim();
                String null13 = judgeNull(area, "区域不能为空");
                if (null13 == null) {
                    return;
                }
                if (!tag.equals("1")) {
                    mclass = tv_class_house.getText().toString().trim();
                    String null16 = judgeNull(mclass, "房子的类型不能为空");
                    if (null16 == null) {
                        return;
                    }
                }
                if (!tag.equals("1")) {
                    property = tv_property.getText().toString().trim();
                    String null15 = judgeNull(property, "产权不能为空");
                    if (null15 == null) {
                        return;
                    }
                }
                decorate = tv_decorate.getText().toString().trim();
                String null14 = judgeNull(decorate, "装修不能为空");
                if (null14 == null) {
                    return;
                }
                circle = et_area_circle.getText().toString().trim();
                String null17 = judgeNull(circle, "商圈不能为空");
                if (null17 == null) {
                    return;
                }
                house = et_area_house.getText().toString().trim();
                String null18 = judgeNull(house, "商圈不能为空");
                if (null18 == null) {
                    return;
                }
                description = et_house_description.getText().toString().trim();
                String null19 = judgeNull(description, "描述信息不能为空");
                if (null19 == null) {
                    return;
                }
                Drawable drawableOne = iv_one.getDrawable();
                Drawable drawableTwo = iv_two.getDrawable();
                Drawable drawableThree = iv_three.getDrawable();
                if (drawableOne == null || drawableTwo == null || drawableThree == null) {
                    Toast.makeText(context, "图片不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //上传服务器
                network();
                break;
        }
    }

    private static int RESULT_LOAD_IMAGE = 1;
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
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    private HttpUtils http;
    private String one;
    private String two;
    private String three;
    private String name;
    private String floor;
    private String total;
    private String direction;
    private String address;
    private String price;
    private String house_title;
    private String contact;
    private String way;
    private String area;
    private String decorate;
    private String property;
    private String mclass;
    private String circle;
    private String house;
    private String description;
    private String tag;

    /**
     * 上传到服务器
     */
    private void network() {
        final DialogUtils dialogUtils = new DialogUtils(context);
        dialogUtils.createDiaLog();
        dialogUtils.show();

        RequestParams params = new RequestParams();
        if (!TextUtils.isEmpty(mAddress.get("one"))) {

            Bitmap bitmap = SubmitImage.getSmallBitmap(mAddress.get("one"));
            File file = new File(dir, "rent1.jpeg");
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
            params.addBodyParameter("one", file);

        }
        if (!TextUtils.isEmpty(mAddress.get("two"))) {
            Bitmap bitmap = SubmitImage.getSmallBitmap(mAddress.get("two"));
            File file = new File(dir, "rent2.jpeg");
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
            params.addBodyParameter("two", file);
        }
        if (!TextUtils.isEmpty(mAddress.get("three"))) {
            Bitmap bitmap = SubmitImage.getSmallBitmap(mAddress.get("three"));
            File file = new File(dir, "rent3.jpeg");
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
            params.addBodyParameter("three", file);

        }
        if (!TextUtils.isEmpty(mAddress.get("one")) && !TextUtils.isEmpty(mAddress.get("two")) && !TextUtils.isEmpty(mAddress.get("three"))) {
        }

        if (flag) {
            if ("2".equals(tag)) {
                if (!flag1) {
                    params.addBodyParameter("one", bitmapUtils.getBitmapFileFromDiskCache(map_second.get("img0")));
                }
                if (!flag2) {
                    params.addBodyParameter("two", bitmapUtils.getBitmapFileFromDiskCache(map_second.get("img1")));
                }
                if (!flag3) {
                    params.addBodyParameter("three", bitmapUtils.getBitmapFileFromDiskCache(map_second.get("img2")));
                }
            }
        }

        if (flag) {
            if ("1".equals(tag)) {
                if (!flag1) {
                    params.addBodyParameter("one", bitmapUtils.getBitmapFileFromDiskCache(map_rent.get("img0")));
                }
                if (!flag2) {
                    params.addBodyParameter("two", bitmapUtils.getBitmapFileFromDiskCache(map_rent.get("img1")));
                }
                if (!flag3) {
                    params.addBodyParameter("three", bitmapUtils.getBitmapFileFromDiskCache(map_rent.get("img2")));
                }
            }
        }

        params.addBodyParameter("houseType", one + "室" + two + "厅" + three + "卫");
        params.addBodyParameter("city", "北京");
        params.addBodyParameter("userId", SPUtils.getUserID(context));

        params.addBodyParameter("district", area);
        params.addBodyParameter("bussinessarea", circle);
        params.addBodyParameter("rentFloor", total);
        params.addBodyParameter("aspect", direction);
        params.addBodyParameter("floor", floor);
        params.addBodyParameter("address", address);
        params.addBodyParameter("spec", house);
        if (flag) {
            if ("2".equals(tag)) {
                params.addBodyParameter("secondId",map_second.get("sid"));
            }
            if ("1".equals(tag)) {
                params.addBodyParameter("houseId", map_rent.get("sid"));
            }
        }
        params.addBodyParameter("title", house_title);
        params.addBodyParameter("rent", price);
        params.addBodyParameter("linkMan", contact);
        params.addBodyParameter("linkPhone", way);

        params.addBodyParameter("description", description);
        params.addBodyParameter("interiordesign", decorate);

        if (tag.equals("2")) {
            params.addBodyParameter("property", property.substring(0, 2));
            if ("住宅".equals(mclass)) {
                params.addBodyParameter("tenementType", "2");
            } else if ("写字楼".equals(mclass)) {
                params.addBodyParameter("tenementType", "6");
            } else if ("商铺".equals(mclass)) {
                params.addBodyParameter("tenementType", "5");
            } else {
                params.addBodyParameter("tenementType", "3");
            }
        }
        params.addBodyParameter("tenement", name);
        if (http == null) {
            http = new HttpUtils();
        }

        String url = null;
        if (!flag) {
            url = UrlsUtils.urlUpload;
            if (tag.equals("1")) {
                url = UrlsUtils.urlUploadHouse;
            }
        } else {
            url = UrlsUtils.urlMyReleaseSecondModify;
            if (tag.equals("1")) {
                url = UrlsUtils.urlMyReleaseRentModify2;
            }
        }
        http.send(HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                dialogUtils.closeDialog();
                Toast.makeText(getApplicationContext(), "网络不给力" + arg1, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                dialogUtils.closeDialog();
                Toast.makeText(getApplicationContext(), "发布房源成功,等待审核", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private String judgeNull(String data, String descriptor) {
        if (TextUtils.isEmpty(data)) {
            Toast.makeText(context, descriptor, Toast.LENGTH_SHORT).show();
            return null;
        }
        return "name";
    }

    /**
     * 开启一个新的界面
     *
     * @param clazz
     */
    private void newStart(Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        startActivityForResult(intent, 100);
    }

}
