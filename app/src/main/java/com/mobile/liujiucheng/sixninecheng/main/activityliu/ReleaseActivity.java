package com.mobile.liujiucheng.sixninecheng.main.activityliu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.mobile.liujiucheng.main.utils.AssetManagerUtils;
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReleaseActivity extends Activity implements View.OnClickListener {
    //所有的“请选择”的TextView的声明
    private TextView release_price_tv2, release_area_tv2, release_age_tv2,
            release_interest_tv2, release_constellation_tv2, release_hometown_tv2, release_school_tv2,
            release_job_tv2, release_state_tv2;
    //各种“选择”
    private RelativeLayout release_price, release_area, release_age,
            release_interest, release_constellation, release_hometown, release_school,
            release_job;
    //“说明”布局的声明
    private RelativeLayout release_state;
    private ImageView release_exit;//退出按钮
    private ImageView release_icon_iv,//头像
            release_photo1, release_photo2, release_photo3;//添加图片

    private Button release_button_confirm;//确定发布
    private RadioGroup release_rg;//单选
    private EditText release_in_et, release_tel_et, release_name_et, release_title_et;
    private Map<String, String> map_release = new HashMap<>();
    private static final int RESULT_LOAD_IMAGE = 8;//选择图片的状态吗
    private BitmapUtils bitmapUtils;
    private Map<String, String> map;//记录图片tag的集合
    private String userID = null;//用户ID
    private HttpUtils http;
    private RadioButton rb1, rb2;
    private boolean flag = false, flag_img_head, flag_img1, flag_img2, flag_img3, flag_job, flag_native, flag_school;

    private List<String> list_img_bottom ;
    private File dir;
    private Context context;
    private String tenantId;
    private DialogUtils utils;
    private Map<String,String> map_release_load;
    private HttpUtils httpUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release);
        context=ReleaseActivity.this;

        //判断是否登陆了
        userID = SPUtils.getUserID(getBaseContext());
        if (userID == null) {
            Intent intent = new Intent(this, LoagingActivity.class);
            startActivity(intent);
        }
        dir = ReleaseActivity.this.getFilesDir();
        //初始化控件
        initView();
        Intent intentFrom = getIntent();
        if ("求租".equals(intentFrom.getExtras().getString("from"))){
            flag = true;
            tenantId=intentFrom.getExtras().getString("tenantId");
            loadInfo();
        }
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
                        if (map_release_load == null) {
                            map_release_load = new HashMap<>();
                        }
                        JSONObject jsonObject = new JSONObject(objectResponseInfo.result);
                        if ("Y".equals(jsonObject.getString("status"))) {
                            JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                            map_release_load.put("createTimeStr", jsonObject1.getString("createTimeStr"));
                            map_release_load.put("checkInTimeStr", jsonObject1.getString("checkInTimeStr"));
                            map_release_load.put("constellation", jsonObject1.getString("constellation"));
                            map_release_load.put("interest", jsonObject1.getString("interest"));
                            map_release_load.put("tenantId", jsonObject1.getString("tenantId"));
                            map_release_load.put("description", jsonObject1.getString("description"));
                            map_release_load.put("age", jsonObject1.getString("age"));
                            map_release_load.put("sex", jsonObject1.getString("sex"));
                            map_release_load.put("district", jsonObject1.getString("district"));
                            map_release_load.put("title", jsonObject1.getString("title"));
                            map_release_load.put("rent", jsonObject1.getString("rent"));
                            map_release_load.put("name", jsonObject1.getString("name"));
                            map_release_load.put("nativePlaceName", jsonObject1.getString("nativePlaceName"));
                            map_release_load.put("nativePlace", jsonObject1.getString("nativePlace"));
                            map_release_load.put("phone", jsonObject1.getString("phone"));
                            map_release_load.put("jobName", jsonObject1.getString("jobName"));
                            map_release_load.put("job", jsonObject1.getString("job"));
                            map_release_load.put("business", jsonObject1.getString("business"));
                            map_release_load.put("schoolName", jsonObject1.getString("schoolName"));
                            map_release_load.put("school", jsonObject1.getString("school"));
                            map_release_load.put("headImg", jsonObject1.getString("headImg"));

                            list_img_bottom = new ArrayList<String>();
                            JSONArray jsonArray = jsonObject1.getJSONArray("listAU");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                String str = jsonArray.getString(i);
                                list_img_bottom.add(str);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    showReleaseModify(map_release_load);
                    utils.closeDialog();
                }
                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(context,"获取信息失败",Toast.LENGTH_SHORT).show();
                    utils.closeDialog();
                }
            });
        }
    }


    private void showReleaseModify(Map<String,String> map1){
        release_price_tv2.setText(map1.get("rent"));
        release_area_tv2.setText(map1.get("district") + "-" + map1.get("business"));
        release_age_tv2.setText(map1.get("age"));
        release_interest_tv2.setText(map1.get("interest"));
        release_constellation_tv2.setText(map1.get("constellation"));
        release_hometown_tv2.setText(map1.get("nativePlaceName"));
        release_school_tv2.setText(map1.get("schoolName"));
        release_job_tv2.setText(map1.get("jobName"));
        release_state_tv2.setText(map1.get("description"));
        //EditView的初始化
        release_in_et.setText(map1.get("checkInTimeStr"));
        release_name_et.setText(map1.get("name"));
        release_tel_et.setText(map1.get("phone"));
        release_title_et.setText(map1.get("title"));
        if ("男".equals(map1.get("sex"))) {
            rb1.setChecked(true);
        } else {
            rb2.setChecked(true);
        }
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(ReleaseActivity.this);
        }
        bitmapUtils.display(release_icon_iv, map1.get("headImg"));
        Log.e("headImg", "headImg" + map1.get("headImg"));
        for (int i = 0; i < list_img_bottom.size(); i++) {
            if (i == 0) {
                bitmapUtils.display(release_photo1, list_img_bottom.get(i));
            }
            if (i == 1) {
                bitmapUtils.display(release_photo2, list_img_bottom.get(i));
            }
            if (i == 2) {
                bitmapUtils.display(release_photo3, list_img_bottom.get(i));
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //点击选择照片的返回值
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK
                && null != data) {
            //使用内容提供者来查询
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String picturePath = cursor.getString(columnIndex);
                //头像
                if (map.get("tag").equals("head")) {
                    flag_img_head = true;
                    bitmapUtils.display(release_icon_iv, picturePath);
                    map_release.put("files", picturePath);
                }
                //下面的三张图片
                else if (map.get("tag").equals("photo1")) {
                    flag_img1 = true;
                    bitmapUtils.display(release_photo1, picturePath);
                    map_release.put("release_one", picturePath);
                } else if (map.get("tag").equals("photo2")) {
                    flag_img2 = true;
                    bitmapUtils.display(release_photo2, picturePath);
                    map_release.put("release_two", picturePath);
                } else if (map.get("tag").equals("photo3")) {
                    flag_img3 = true;
                    bitmapUtils.display(release_photo3, picturePath);
                    map_release.put("release_three", picturePath);
                }

                //关掉cursor
                cursor.close();
            } else {
                Log.e("TAG", "cursor == " + cursor);
            }
        }
    }

    /**
     * 初始化控件及添加点击事件
     */
    private void initView() {
        //TextView 的初始化
        release_price_tv2 = (TextView) findViewById(R.id.release_price_tv2);
        release_area_tv2 = (TextView) findViewById(R.id.release_area_tv2);
        release_age_tv2 = (TextView) findViewById(R.id.release_age_tv2);
        release_interest_tv2 = (TextView) findViewById(R.id.release_interest_tv2);
        release_constellation_tv2 = (TextView) findViewById(R.id.release_constellation_tv2);
        release_hometown_tv2 = (TextView) findViewById(R.id.release_hometown_tv2);
        release_school_tv2 = (TextView) findViewById(R.id.release_school_tv2);
        release_job_tv2 = (TextView) findViewById(R.id.release_job_tv2);
        release_state_tv2 = (TextView) findViewById(R.id.release_state_tv2);
        //EditView的初始化
        release_in_et = (EditText) findViewById(R.id.release_in_et);
        release_name_et = (EditText) findViewById(R.id.release_name_et);
        release_tel_et = (EditText) findViewById(R.id.release_tel_et);
        release_title_et = (EditText) findViewById(R.id.release_title_et);
        //点击控件的初始化
        release_exit = (ImageView) findViewById(R.id.release_exit);
        release_icon_iv = (ImageView) findViewById(R.id.release_icon_iv);
        release_photo1 = (ImageView) findViewById(R.id.release_photo1);
        release_photo2 = (ImageView) findViewById(R.id.release_photo2);
        release_photo3 = (ImageView) findViewById(R.id.release_photo3);

        release_state = (RelativeLayout) findViewById(R.id.release_state);
        release_price = (RelativeLayout) findViewById(R.id.release_price);
        release_area = (RelativeLayout) findViewById(R.id.release_area);
        release_age = (RelativeLayout) findViewById(R.id.release_age);
        release_interest = (RelativeLayout) findViewById(R.id.release_interest);
        release_constellation = (RelativeLayout) findViewById(R.id.release_constellation);
        release_hometown = (RelativeLayout) findViewById(R.id.release_hometown);
        release_school = (RelativeLayout) findViewById(R.id.release_school);
        release_job = (RelativeLayout) findViewById(R.id.release_job);

        rb1 = (RadioButton) findViewById(R.id.release_sex_man);
        rb2 = (RadioButton) findViewById(R.id.release_sex_woman);
        release_rg = (RadioGroup) findViewById(R.id.release_rg);
        map_release.put("sex", "男");
        release_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.release_sex_man:
                        map_release.put("sex", "男");
                        break;
                    case R.id.release_sex_woman:
                        map_release.put("sex", "女");
                        break;
                }
            }
        });


        release_button_confirm = (Button) findViewById(R.id.release_button_confirm);
        //点击事件的初始化
        release_price.setOnClickListener(this);
        release_area.setOnClickListener(this);
        release_age.setOnClickListener(this);
        release_interest.setOnClickListener(this);
        release_constellation.setOnClickListener(this);
        release_hometown.setOnClickListener(this);
        release_school.setOnClickListener(this);
        release_job.setOnClickListener(this);
        release_state.setOnClickListener(this);
        release_exit.setOnClickListener(this);
        release_icon_iv.setOnClickListener(this);
        release_photo1.setOnClickListener(this);
        release_photo2.setOnClickListener(this);
        release_photo3.setOnClickListener(this);
        release_button_confirm.setOnClickListener(this);
        bitmapUtils = new BitmapUtils(this);
    }

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

    //对.json进行解析
    private List<Dialog_forlist> jSON2List(String jsonPath, String outName, String outName2, String inName, String id) throws JSONException {
        List<Dialog_forlist> list = new ArrayList<>();
        InputStream is = AssetManagerUtils.getData(jsonPath, ReleaseActivity.this);
        String str = StreamTools.readStream(is);

        JSONArray jsonArray = new JSONArray(str);
        for (int i = 0; i < jsonArray.length(); i++) {
            Dialog_forlist dialog_forlist = new Dialog_forlist();
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            dialog_forlist.setName(jsonObject.getString(outName));
            JSONArray jsonArray2 = jsonObject.getJSONArray(outName2);
            List<String> list_city = new ArrayList<>();
            List<Integer> list_id = new ArrayList<>();
            for (int j = 0; j < jsonArray2.length(); j++) {
                JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
                list_city.add(jsonObject2.getString(inName));
                list_id.add(jsonObject2.getInt(id));
            }
            dialog_forlist.setList(list_city);
            dialog_forlist.setList_id(list_id);
            list.add(dialog_forlist);
        }

        return list;
    }

    //对address.json进行解析
    private List<Dialog_forlist> jSON2List2(String jsonPath) throws JSONException {
        List<Dialog_forlist> list = new ArrayList<>();
        InputStream is = AssetManagerUtils.getData(jsonPath, ReleaseActivity.this);
        String str = StreamTools.readStream(is);

        JSONObject object = new JSONObject(str);
        JSONArray jsonArray = object.getJSONArray("city");
        JSONObject jsonObject = jsonArray.getJSONObject(0);
        JSONArray jsonArray1 = jsonObject.getJSONArray("tdistrict");
        for (int i = 0; i < jsonArray1.length(); i++) {
            Dialog_forlist dialog_forlist = new Dialog_forlist();
            List<String> list_city = new ArrayList<>();
            if (i == 0) {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                dialog_forlist.setName(jsonObject1.getString("name"));
                list_city.add("不限");
                dialog_forlist.setList(list_city);
            } else {
                JSONObject jsonObject1 = jsonArray1.getJSONObject(i);
                dialog_forlist.setName(jsonObject1.getString("name"));
                JSONArray jsonArray2 = jsonObject1.getJSONArray("bussinessareaList");
                for (int j = 0; j < jsonArray2.length(); j++) {
                    JSONObject jsonObject2 = jsonArray2.getJSONObject(j);
                    list_city.add(jsonObject2.getString("name"));
                }
                dialog_forlist.setList(list_city);
            }
            list.add(dialog_forlist);
        }

        return list;
    }

    //一列选择框
    private void dialogRoommate(String title, final String key, final TextView textView, final List<String> list) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(ReleaseActivity.this, R.layout.dialog_addroommate, null);
        TextView textView_title = (TextView) view.findViewById(R.id.dialog_title1);
        textView_title.setText(title);
        ListView listView = (ListView) view.findViewById(R.id.dialog_listview);
        listView.setFadingEdgeLength(0);
        builder.setView(view);
        final Dialog dialog = builder.show();
        listView.setAdapter(new ArrayAdapter<String>(this, R.layout.dialog_listview_layout, list));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str = list.get(position);
                textView.setText(str);
                dialog.dismiss();
            }
        });
    }

    private List<String> list_city;//城市名称的list
    private List<Integer> list_id;//城市ID的list

    //两列选择框
    private void dialogRoommate2(final String title, final String key1, final String key2, final TextView textView, List<Dialog_forlist> list) {
        final List<String> list_name = new ArrayList<>();
        final Map<String, List<String>> map_city = new HashMap<>();
        final Map<String, List<Integer>> map_id = new HashMap<>();
        for (Dialog_forlist ll : list) {
            list_name.add(ll.getName());
            map_city.put(ll.getName(), ll.getList());
            map_id.put(ll.getName(), ll.getList_id());
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(ReleaseActivity.this);
        View view = View.inflate(ReleaseActivity.this, R.layout.dialog_addroommate2, null);
        TextView textView_title = (TextView) view.findViewById(R.id.dialog_title);
        textView_title.setText(title);
        final ListView listView = (ListView) view.findViewById(R.id.dialog_listview);
        final ListView listView2 = (ListView) view.findViewById(R.id.dialog_listview2);
        builder.setView(view);


        final Dialog dialog = builder.show();

        listView.setAdapter(new ArrayAdapter(ReleaseActivity.this, R.layout.dialog_listview_layout, list_name));
        list_city = map_city.get(list_name.get(0));

        listView2.setAdapter(new ArrayAdapter(ReleaseActivity.this, R.layout.dialog_listview_layout, list_city));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                map_release.put(key1, list_name.get(position));
                //根据第一个ListView中item的值来确定ListView2的数据
                list_city = map_city.get(list_name.get(position));//城市名字的list
                list_id = map_id.get(list_name.get(position));
                listView2.setAdapter(new ArrayAdapter(ReleaseActivity.this, R.layout.dialog_listview_layout, list_city));
                listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        if ("区域".equals(title)) {
                            String string = map_release.get(key1) + "-" + list_city.get(position);
                            textView.setText(string);
                            map_release.put(key2, list_city.get(position).toString());
                        } else if ("籍贯".equals(title)) {
                            String string = map_release.get(key1) + "-" + list_city.get(position);
                            textView.setText(string);
                            map_release.put(key2, list_id.get(position).toString());
                        } else {
                            String string = list_city.get(position);
                            textView.setText(string);
                            map_release.put(key2, list_id.get(position).toString());
                        }
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    //职业的dialog
    private ArrayAdapter adapter2 = null, adapter3 = null;

    private void dialogRoommate3() throws JSONException {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReleaseActivity.this);
        View view = View.inflate(ReleaseActivity.this, R.layout.dialog_addroommate3, null);
        TextView textView_title = (TextView) view.findViewById(R.id.dialog_title);
        textView_title.setText("职业");
        final ListView listView = (ListView) view.findViewById(R.id.dialog_listview);
        final ListView listView2 = (ListView) view.findViewById(R.id.dialog_listview2);
        final ListView listView3 = (ListView) view.findViewById(R.id.dialog_listview3);
        builder.setView(view);
        InputStream is = AssetManagerUtils.getData("tjob.json", ReleaseActivity.this);
        final String str = StreamTools.readStream(is);
        final List<String> list1 = new ArrayList<>();
        final List<String> list2 = new ArrayList<>();
        final List<String> list3 = new ArrayList<>();
        final List<String> list3_id = new ArrayList<>();
        final JSONArray jsonArray = new JSONArray(str);
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            list1.add(jsonObject.getString("vocation"));
        }

        final Dialog dialog = builder.show();
        listView.setAdapter(new ArrayAdapter(ReleaseActivity.this, R.layout.dialog_listview_layout, list1));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONArray jsonArray = null;
                list2.clear();
                list3.clear();
                list3_id.clear();
                if (adapter3 != null) {
                    adapter3.clear();
                }
                final int p = position;
                try {
                    jsonArray = new JSONArray(str);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (i == position) {
                            JSONObject jsonObject = jsonArray.getJSONObject(position);
                            JSONArray jsonArray1 = jsonObject.getJSONArray("vocationChildList");
                            for (int j = 0; j < jsonArray1.length(); j++) {
                                JSONObject jsonObject1 = jsonArray1.getJSONObject(j);
                                list2.add(jsonObject1.getString("vocationChild"));
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                adapter2 = new ArrayAdapter(ReleaseActivity.this, R.layout.dialog_listview_layout, list2);
                adapter2.notifyDataSetChanged();
                listView2.setAdapter(adapter2);
                listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        JSONArray jsonArray = null;
                        list3.clear();
                        list3_id.clear();
                        try {
                            jsonArray = new JSONArray(str);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (i == p) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(p);
                                    JSONArray jsonArray1 = jsonObject.getJSONArray("vocationChildList");
                                    for (int j = 0; j < jsonArray1.length(); j++) {
                                        if (j == position) {
                                            JSONObject jsonObject1 = jsonArray1.getJSONObject(position);
                                            JSONArray jsonArray2 = jsonObject1.getJSONArray("jobList");
                                            for (int k = 0; k < jsonArray2.length(); k++) {
                                                JSONObject jsonObject2 = jsonArray2.getJSONObject(k);
                                                list3.add(jsonObject2.getString("jobName"));
                                                list3_id.add(Integer.toString(jsonObject2.getInt("jobId")));
                                            }
                                        }
                                    }
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter3 = new ArrayAdapter(ReleaseActivity.this, R.layout.dialog_listview_layout, list3);
                        adapter3.notifyDataSetChanged();
                        listView3.setAdapter(adapter3);
                        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                release_job_tv2.setText(list3.get(position));
                                map_release.put("job2", list3_id.get(position));
                                dialog.dismiss();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.release_area:
                List<Dialog_forlist> list_area = null;
                try {
                    list_area = jSON2List2("address.json");
                    dialogRoommate2("区域", "area_city", "area_business", release_area_tv2, list_area);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                break;
            case R.id.release_price:
                List<String> list_price = new ArrayList<>();
                list_price.add("500以下");
                list_price.add("500-1000");
                list_price.add("1000-2000");
                list_price.add("2000-3000");
                list_price.add("3000以上");
                dialogRoommate("价格", "price", release_price_tv2, list_price);

                break;
            case R.id.release_age:
                List<String> list_age = new ArrayList<>();
                list_age.add("不限");
                list_age.add("00后");
                list_age.add("95后");
                list_age.add("90后");
                list_age.add("85后");
                list_age.add("80后");
                list_age.add("75后");
                list_age.add("70后");
                dialogRoommate("年龄", "age", release_age_tv2, list_age);
                map_release.put("age", release_age_tv2.getText().toString().trim());
                break;
            case R.id.release_interest:
                List<String> list_interest = new ArrayList<>();
                list_interest.add("不限");
                list_interest.add("美食烹饪");
                list_interest.add("阅读写作");
                list_interest.add("电影/音乐");
                list_interest.add("旅行/户外");
                list_interest.add("健康/爱好");
                list_interest.add("时尚/艺术");
                list_interest.add("交友/聚会");
                list_interest.add("其它");
                dialogRoommate("兴趣", "interest", release_interest_tv2, list_interest);
                break;
            case R.id.release_constellation:
                List<String> list_constellation = new ArrayList<>();
                list_constellation.add("不限");
                list_constellation.add("水瓶座");
                list_constellation.add("双鱼座");
                list_constellation.add("白羊座");
                list_constellation.add("金牛座");
                list_constellation.add("双子座");
                list_constellation.add("巨蟹座");
                list_constellation.add("狮子座");
                list_constellation.add("处女座");
                list_constellation.add("天秤座");
                list_constellation.add("天蝎座");
                list_constellation.add("射手座");
                list_constellation.add("摩羯座");
                dialogRoommate("星座", "constellation", release_constellation_tv2, list_constellation);
                break;
            case R.id.release_hometown:
                try {
                    List<Dialog_forlist> list_hometown = jSON2List("hometown.json", "provinceName", "city", "provinceCityName", "provinceCityId");
                    dialogRoommate2("籍贯", "hometown_P", "hometown_C", release_hometown_tv2, list_hometown);
                    flag_native = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.release_school:
                try {
                    List<Dialog_forlist> list_school = jSON2List("school.json", "provinceName", "school", "schoolName", "schoolId");
                    dialogRoommate2("院校", "school_P", "school_N", release_school_tv2, list_school);
                    flag_school = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.release_job:
                try {
                    dialogRoommate3();
                    flag_job = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.release_state:
                AlertDialog.Builder builder = new AlertDialog.Builder(ReleaseActivity.this);
                View view = View.inflate(ReleaseActivity.this, R.layout.release_state_dialog, null);
                builder.setView(view);
                final EditText editText = (EditText) view.findViewById(R.id.release_state_dialog_et);
                final Dialog dialog = builder.show();
                Button button = (Button) view.findViewById(R.id.release_state_dialog_config);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String str = editText.getText().toString().trim();
                        release_state_tv2.setText(str);
                        map_release.put("state", str);
                        dialog.dismiss();
                    }
                });
                break;
            case R.id.release_exit:
                finish();
                break;
            case R.id.release_icon_iv:
                uploadPictures("head");
                break;
            case R.id.release_photo1:
                uploadPictures("photo1");
                break;
            case R.id.release_photo2:
                uploadPictures("photo2");
                break;
            case R.id.release_photo3:
                uploadPictures("photo3");
                break;
            case R.id.release_button_confirm:
                getData();
                if (getData()) {
                    netWork();
                }
                break;
            default:
        }
    }

    private void netWork() {
        utils=new DialogUtils(context);
        utils.show();
        RequestParams params = new RequestParams();
        if (!flag) {
            if (!TextUtils.isEmpty(map_release.get("files"))) {
                Bitmap bitmap = SubmitImage.getSmallBitmap(map_release.get("files"));
                File file = new File(dir, "release_head.jpeg");
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
                params.addBodyParameter("file", file);
            }
        }
        if (flag && flag_img_head) {
            Bitmap bitmap = SubmitImage.getSmallBitmap(map_release.get("files"));
            File file = new File(dir, "release_head.jpeg");
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
            params.addBodyParameter("file", file);
        }

        if (!TextUtils.isEmpty(map_release.get("release_one"))) {
            Bitmap bitmap = SubmitImage.getSmallBitmap(map_release.get("release_one"));
            File file = new File(dir, "release1.jpeg");
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
            params.addBodyParameter("photo1", file);
        }

        if (!TextUtils.isEmpty(map_release.get("release_two"))) {
            Bitmap bitmap = SubmitImage.getSmallBitmap(map_release.get("release_two"));
            File file = new File(dir, "release2.jpeg");
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
            params.addBodyParameter("photo2", file);
        }
        if (!TextUtils.isEmpty(map_release.get("release_three"))) {
            Bitmap bitmap = SubmitImage.getSmallBitmap(map_release.get("release_three"));
            File file = new File(dir, "release3.jpeg");
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
            params.addBodyParameter("photo3", file);
        }

        if (TextUtils.isEmpty(map_release.get("head")) && TextUtils.isEmpty(map_release.get("release_one")) &&
                TextUtils.isEmpty(map_release.get("release_two")) && TextUtils.isEmpty(map_release.get("release_three"))
                ) {
        }
        if (flag) {
            if (!flag_img_head) {
                params.addBodyParameter("file", bitmapUtils.getBitmapFileFromDiskCache(map_release_load.get("headImg")));
            }
            for (int i = 0; i < list_img_bottom.size(); i++) {
                if (i == 0 && !flag_img1) {
                    params.addBodyParameter("photo1", bitmapUtils.getBitmapFileFromDiskCache(list_img_bottom.get(0)));
                }
                if (i == 1 && !flag_img2) {
                    params.addBodyParameter("photo2", bitmapUtils.getBitmapFileFromDiskCache(list_img_bottom.get(1)));
                }
                if (i == 2 && !flag_img3) {
                    params.addBodyParameter("photo3", bitmapUtils.getBitmapFileFromDiskCache(list_img_bottom.get(2)));
                }
            }
        }
        params.addBodyParameter("userId", userID);
        params.addBodyParameter("city", "北京");
        params.addBodyParameter("district", map_release.get("area_city"));//区域
        params.addBodyParameter("bussiness", map_release.get("area_business"));//商圈
        params.addBodyParameter("rent", map_release.get("price"));//价格区间
        params.addBodyParameter("name", map_release.get("name"));//姓名
        params.addBodyParameter("phone", map_release.get("phone"));//电话
        params.addBodyParameter("job", map_release.get("job2"));//职业

        params.addBodyParameter("interest", map_release.get("interest"));//兴趣
        params.addBodyParameter("sex", map_release.get("sex"));//性别
        params.addBodyParameter("age", map_release.get("age"));//年龄
        params.addBodyParameter("constellation", map_release.get("constellation"));//星座
        params.addBodyParameter("nativePlace", map_release.get("hometown_C"));//籍贯
        params.addBodyParameter("title", release_title_et.getText().toString());
        params.addBodyParameter("description", map_release.get("state"));
        params.addBodyParameter("school", map_release.get("school_N"));//院校
        params.addBodyParameter("checkInTime", map_release.get("checkInTime"));//入住时间
        if (flag) {
            params.addBodyParameter("tenantId", map_release_load.get("tenantId"));
        }
        if (http == null) {
            http = new HttpUtils();
        }
        String url = null;
        if (flag) {
            url = UrlsUtils.urlReleaseModify;
        } else {
            url = UrlsUtils.urlRelease;
        }
        http.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {

                Toast.makeText(getApplicationContext(), "上传失败，请检查网络或填写的信息", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "解析失败");
                utils.closeDialog();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {

                Toast.makeText(getApplicationContext(), "上传成功,等待审核", Toast.LENGTH_LONG).show();
                utils.closeDialog();
                finish();
            }
        });
    }

    private boolean getData() {

        if (flag) {
            String area_city = getStringArray(release_area_tv2.getText().toString())[0];
            map_release.put("area_city", area_city);
            String area_business = getStringArray(release_area_tv2.getText().toString())[1];
            map_release.put("area_business", area_business);
            String null1 = judgeNull(area_city, "请选择区域");
            if (null1 == null) {
                return false;
            }
        }

        String price = release_price_tv2.getText().toString().trim();
        map_release.put("price", price);
        String null3 = judgeNull(price, "请选择价格");
        if (null3 == null) {
            return false;
        }
        String checkInTime = release_in_et.getText().toString().trim();
        map_release.put("checkInTime", checkInTime);
        String null4 = judgeNull(checkInTime, "请选择入住时间");
        if (null4 == null) {
            return false;
        }
        String name = release_name_et.getText().toString().trim();
        map_release.put("name", name);
        String null5 = judgeNull(name, "请输入姓名");
        if (null5 == null) {
            return false;
        }
        String phone = release_tel_et.getText().toString().trim();
        map_release.put("phone", phone);
        String null6 = judgeNull(phone, "请输入电话号码");
        if (null6 == null) {
            return false;
        }
        String age = release_age_tv2.getText().toString().trim();
        map_release.put("age", age);
        String null7 = judgeNull(age, "请选择年龄");
        if (null7 == null) {
            return false;
        }
        String interest = release_interest_tv2.getText().toString().trim();
        map_release.put("interest", interest);
        String null8 = judgeNull(interest, "请选择兴趣");
        if (null8 == null) {
            return false;
        }
        String constellation = release_constellation_tv2.getText().toString().trim();
        map_release.put("constellation", constellation);
        String null9 = judgeNull(constellation, "请选择星座");
        if (null9 == null) {
            return false;
        }
        if (flag) {
            if (!flag_job) {
                map_release.put("job2", map_release_load.get("job"));
            }
            if (!flag_school) {
                map_release.put("school_N", map_release_load.get("school"));
            }
            if (!flag_native) {
                map_release.put("hometown_C", map_release_load.get("nativePlace"));
            }
        } else {
            //暂时后台还没有将这三个ID放入，所以为空
            String null10 = judgeNull(map_release.get("hometown_C"), "请选择籍贯");
            if (null10 == null) {
                return false;
            }
            String null11 = judgeNull(map_release.get("job2"), "请选择职业");
            if (null11 == null) {
                return false;
            }
            String null12 = judgeNull(map_release.get("school_N"), "请选择院校");
            if (null12 == null) {
                return false;
            }
        }
        map_release.put("state", release_state_tv2.getText().toString().trim());
        return true;
    }

    private String[] getStringArray(String str) {
        String[] string = str.split("-");
        return string;
    }

    private String judgeNull(String data, String descriptor) {
        if (TextUtils.isEmpty(data)) {
            Toast.makeText(this, descriptor, Toast.LENGTH_SHORT).show();
            return null;
        }
        return "name";
    }


}
