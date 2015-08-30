package com.mobile.liujiucheng.sixninecheng.main.activityliu;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
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
import java.util.logging.LoggingPermission;

/**
 * 添加室友
 */
public class AddRoommateActivity extends Activity implements View.OnClickListener {

    private ImageView addRoommate_exit, addRoommate_icon_iv;
    private EditText addRoommate_name_et, addRoommate_tel_et;
    private RadioGroup addRoommate_rg;
    private TextView addRoommate_age_tv2, addRoommate_interest_tv2, addRoommate_constellation_tv2,
            addRoommate_hometown_tv2, addRoommate_school_tv2, addRoommate_job_tv2, addRoommate_houseStyle_tv2;
    private Map<String, String> map_roommate = new HashMap<>();
    private BitmapUtils bitmapUtils;
    private Button addRoommate_confirm;
    private RadioButton rb1, rb2;
    private RelativeLayout addRoommate_age, addRoommate_interest, addRoommate_constellation,
            addRoommate_hometown, addRoommate_school, addRoommate_job, addRoommate_houseStyle;
    private boolean flag = false;
    private HttpUtils httpUtils;
    private boolean flag_icon = false, flag_job = false, flag_native = false, flag_school = false;
    private String userID = null;
    private File dir;
    private String tenantId,houseId;
    private DialogUtils utils;
    private Map<String,String> map_panda_add;
    private Context context;
    private HttpUtils httpUtils2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_roommate);
        context=AddRoommateActivity.this;
        userID = SPUtils.getUserID(getBaseContext());
        initView();
        dir = AddRoommateActivity.this.getFilesDir();
        Intent intent = getIntent();
        flag = intent.getExtras().getBoolean("flag");
        tenantId=intent.getExtras().getString("tenantId");
        houseId=intent.getExtras().getString("houseId");
        if (flag) {
            loadInfo();
        }
    }

    private void loadInfo() {
        utils=new DialogUtils(context);
        utils.show();
        if(map_panda_add==null){
            map_panda_add=new HashMap<>();
        }
        boolean isConnect= NetworkUtils.isConnection(context);
        if(isConnect){
            NetHead head=new NetHead(context);
            RequestParams params=head.setHeader();
            params.addBodyParameter("houseId",houseId);
            params.addBodyParameter("tenantId",tenantId);
            params.addBodyParameter("userId",userID);
            if(httpUtils2==null){
                httpUtils2=new HttpUtils();
            }
            httpUtils2.send(HttpRequest.HttpMethod.POST,UrlsUtils.urlReleaseRoommateInfo,params,new RequestCallBack<String>() {
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
                    showInfoModify(map_panda_add);
                    utils.closeDialog();
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    utils.closeDialog();

                }
            });
        }
    }


    private void showInfoModify(Map<String, String> map) {
        addRoommate_age_tv2.setText(map.get("age"));
        addRoommate_interest_tv2.setText(map.get("interest"));
        addRoommate_constellation_tv2.setText(map.get("constellation"));
        addRoommate_hometown_tv2.setText(map.get("nativePlaceName"));
        addRoommate_school_tv2.setText(map.get("schoolName"));
        addRoommate_job_tv2.setText(map.get("jobName"));
        addRoommate_houseStyle_tv2.setText(map.get("houseName"));
        addRoommate_name_et.setText(map.get("name"));
        addRoommate_tel_et.setText(map.get("phone"));
        if ("男".equals(map.get("sex"))) {
            rb1.setChecked(true);
        } else {
            rb2.setChecked(true);
        }
        if (bitmapUtils == null) {
            bitmapUtils = new BitmapUtils(AddRoommateActivity.this);
        }
        bitmapUtils.display(addRoommate_icon_iv, map.get("headImg"));
    }

    private final int RESULT_LOAD_IMAGE = 2;

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

                flag_icon = true;

                bitmapUtils.display(addRoommate_icon_iv, picturePath);
                map_roommate.put("picturePath", picturePath);
                cursor.close();
            } else {
                Log.e("TAG", "cursor == " + cursor);
            }
        }
    }

    /**
     * 上传图片
     */
    private void uploadPictures() {
        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);// external_content_uri
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    private void initView() {
        addRoommate_exit = (ImageView) findViewById(R.id.addRoommate_exit);
        addRoommate_icon_iv = (ImageView) findViewById(R.id.addRoommate_icon_iv);
        addRoommate_name_et = (EditText) findViewById(R.id.addRoommate_name_et);
        addRoommate_tel_et = (EditText) findViewById(R.id.addRoommate_tel_et);
        addRoommate_rg = (RadioGroup) findViewById(R.id.addRoommate_rg);
        map_roommate.put("sex", "男");
        addRoommate_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (group.getCheckedRadioButtonId()) {
                    case R.id.release_sex_man:
                        map_roommate.put("sex", "男");
                        break;
                    case R.id.release_sex_woman:
                        map_roommate.put("sex", "女");
                        break;
                }
            }
        });


        rb1 = (RadioButton) findViewById(R.id.addRoommate_sex_man);
        rb2 = (RadioButton) findViewById(R.id.addRoommate_sex_woman);

        addRoommate_confirm = (Button) findViewById(R.id.addRoommate_confirm);
        addRoommate_age_tv2 = (TextView) findViewById(R.id.addRoommate_age_tv2);
        addRoommate_interest_tv2 = (TextView) findViewById(R.id.addRoommate_interest_tv2);
        addRoommate_constellation_tv2 = (TextView) findViewById(R.id.addRoommate_constellation_tv2);
        addRoommate_hometown_tv2 = (TextView) findViewById(R.id.addRoommate_hometown_tv2);
        addRoommate_school_tv2 = (TextView) findViewById(R.id.addRoommate_school_tv2);
        addRoommate_job_tv2 = (TextView) findViewById(R.id.addRoommate_job_tv2);
        addRoommate_houseStyle_tv2 = (TextView) findViewById(R.id.addRoommate_houseStyle_tv2);

        addRoommate_age = (RelativeLayout) findViewById(R.id.addRoommate_age);
        addRoommate_interest = (RelativeLayout) findViewById(R.id.addRoommate_interest);
        addRoommate_constellation = (RelativeLayout) findViewById(R.id.addRoommate_constellation);
        addRoommate_hometown = (RelativeLayout) findViewById(R.id.addRoommate_hometown);
        addRoommate_school = (RelativeLayout) findViewById(R.id.addRoommate_school);
        addRoommate_job = (RelativeLayout) findViewById(R.id.addRoommate_job);
        addRoommate_houseStyle = (RelativeLayout) findViewById(R.id.addRoommate_houseStyle);

        addRoommate_age.setOnClickListener(this);
        addRoommate_interest.setOnClickListener(this);
        addRoommate_constellation.setOnClickListener(this);
        addRoommate_hometown.setOnClickListener(this);
        addRoommate_school.setOnClickListener(this);
        addRoommate_job.setOnClickListener(this);
        addRoommate_houseStyle.setOnClickListener(this);

        bitmapUtils = new BitmapUtils(this);
        addRoommate_exit.setOnClickListener(this);
        addRoommate_icon_iv.setOnClickListener(this);
        addRoommate_confirm.setOnClickListener(this);

    }

    //对hometown.json进行解析
    private List<Dialog_forlist> jSON2List(String jsonPath, String outName, String outName2, String inName, String id) throws JSONException {
        List<Dialog_forlist> list = new ArrayList<>();
        InputStream is = AssetManagerUtils.getData(jsonPath, AddRoommateActivity.this);
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

    //一列选择框
    private void dialogRoommate(String title, final String key, final TextView textView, final List<String> list) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = View.inflate(AddRoommateActivity.this, R.layout.dialog_addroommate, null);
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
                map_roommate.put(key, str);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(AddRoommateActivity.this);
        View view = View.inflate(AddRoommateActivity.this, R.layout.dialog_addroommate2, null);
        TextView textView_title = (TextView) view.findViewById(R.id.dialog_title);
        textView_title.setText(title);
        final ListView listView = (ListView) view.findViewById(R.id.dialog_listview);
        final ListView listView2 = (ListView) view.findViewById(R.id.dialog_listview2);
        builder.setView(view);


        final Dialog dialog = builder.show();

        listView.setAdapter(new ArrayAdapter(AddRoommateActivity.this, R.layout.dialog_listview_layout, list_name));
        list_city = map_city.get(list_name.get(0));

        listView2.setAdapter(new ArrayAdapter(AddRoommateActivity.this, R.layout.dialog_listview_layout, list_city));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                map_roommate.put(key1, list_name.get(position));
                //根据第一个ListView中item的值来确定ListView2的数据
                list_city = map_city.get(list_name.get(position));//城市名字的list
                list_id = map_id.get(list_name.get(position));
                //  Toast.makeText(AddRoommateActivity.this,list_get_list.toString(),Toast.LENGTH_LONG).show();
                listView2.setAdapter(new ArrayAdapter(AddRoommateActivity.this, R.layout.dialog_listview_layout, list_city));
                listView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        if ("籍贯".equals(title)) {
                            String string = map_roommate.get(key1) + "-" + list_city.get(position);
                            textView.setText(string);
                        } else {
                            String string = list_city.get(position);
                            textView.setText(string);
                        }
                        map_roommate.put(key2, list_id.get(position).toString());
                        dialog.dismiss();
                    }
                });
            }
        });
    }

    //职业的dialog
    private ArrayAdapter adapter2 = null, adapter3 = null;

    private void dialogRoommate3() throws JSONException {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddRoommateActivity.this);
        View view = View.inflate(AddRoommateActivity.this, R.layout.dialog_addroommate3, null);
        TextView textView_title = (TextView) view.findViewById(R.id.dialog_title);
        textView_title.setText("职业");
        final ListView listView = (ListView) view.findViewById(R.id.dialog_listview);
        final ListView listView2 = (ListView) view.findViewById(R.id.dialog_listview2);
        final ListView listView3 = (ListView) view.findViewById(R.id.dialog_listview3);
        builder.setView(view);
        InputStream is = AssetManagerUtils.getData("tjob.json", AddRoommateActivity.this);
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

        listView.setAdapter(new ArrayAdapter(AddRoommateActivity.this, R.layout.dialog_listview_layout, list1));
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

                adapter2 = new ArrayAdapter(AddRoommateActivity.this, R.layout.dialog_listview_layout, list2);
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
                        adapter3 = new ArrayAdapter(AddRoommateActivity.this, R.layout.dialog_listview_layout, list3);
                        adapter3.notifyDataSetChanged();
                        listView3.setAdapter(adapter3);
                        listView3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                addRoommate_job_tv2.setText(list3.get(position));
                                map_roommate.put("jobId", list3_id.get(position));
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

        switch (v.getId()) {
            case R.id.addRoommate_exit://退出
                finish();
                break;
            case R.id.addRoommate_icon_iv://头像
                uploadPictures();
                break;
            case R.id.addRoommate_houseStyle://室友所在的房间
                final List<String> list_houseStyle = new ArrayList<>();
                list_houseStyle.add("主卧");
                list_houseStyle.add("次卧");
                list_houseStyle.add("单间");
                list_houseStyle.add("隔断");
                dialogRoommate("房间", "houseStyle", addRoommate_houseStyle_tv2, list_houseStyle);
                break;

            case R.id.addRoommate_age://年龄
                List<String> list_age = new ArrayList<>();
                list_age.add("不限");
                list_age.add("00后");
                list_age.add("95后");
                list_age.add("90后");
                list_age.add("85后");
                list_age.add("80后");
                list_age.add("75后");
                list_age.add("70后");
                dialogRoommate("年龄", "age", addRoommate_age_tv2, list_age);
                break;
            case R.id.addRoommate_interest://兴趣
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
                dialogRoommate("兴趣", "interest", addRoommate_interest_tv2, list_interest);
                break;
            case R.id.addRoommate_constellation://星座
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
                dialogRoommate("星座", "constellation", addRoommate_constellation_tv2, list_constellation);
                break;
            case R.id.addRoommate_hometown://籍贯
                try {
                    List<Dialog_forlist> list_hometown = jSON2List("hometown.json", "provinceName", "city", "provinceCityName", "provinceCityId");
                    dialogRoommate2("籍贯", "hometown_P", "hometown_C", addRoommate_hometown_tv2, list_hometown);
                    flag_native = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.addRoommate_school://院校
                try {
                    List<Dialog_forlist> list_school = jSON2List("school.json", "provinceName", "school", "schoolName", "schoolId");
                    dialogRoommate2("院校", "school_P", "school_N", addRoommate_school_tv2, list_school);
                    flag_school = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.addRoommate_job://职业
                try {
                    dialogRoommate3();
                    flag_job = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.addRoommate_confirm://确定添加
                getDataName();
                if (!flag) {
                    if (getDataName()) {
                        if (TextUtils.isEmpty(addRoommate_job_tv2.getText().toString().trim())) {
                            Toast.makeText(this, "请选择职业", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (TextUtils.isEmpty(addRoommate_hometown_tv2.getText().toString().trim())) {
                            Toast.makeText(this, "请选择籍贯", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (TextUtils.isEmpty(addRoommate_age_tv2.getText().toString().trim())) {
                            Toast.makeText(this, "请选择年龄", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (TextUtils.isEmpty(addRoommate_interest_tv2.getText().toString().trim())) {
                            Toast.makeText(this, "请选择兴趣爱好", Toast.LENGTH_SHORT).show();
                            return;
                        } else if (TextUtils.isEmpty(addRoommate_houseStyle_tv2.getText().toString().trim())) {
                            Toast.makeText(this, "请选择房间", Toast.LENGTH_SHORT).show();
                            return;
                        } else {
                            Intent intent = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putString("name", map_roommate.get("name"));//室友姓名
                            bundle.putString("tel", map_roommate.get("tel"));//室友电话
                            bundle.putString("picturePath", map_roommate.get("picturePath"));//头像路径
                            bundle.putString("sex", map_roommate.get("sex"));//性别
                            bundle.putString("age", addRoommate_age_tv2.getText().toString().trim());//年龄
                            bundle.putString("interest", addRoommate_interest_tv2.getText().toString().trim());//兴趣
                            bundle.putString("constellation", addRoommate_constellation_tv2.getText().toString().trim());//星座
                            bundle.putString("hometown", map_roommate.get("hometown_C"));//籍贯
                            bundle.putString("school", map_roommate.get("school_N"));//院校
                            bundle.putString("job", map_roommate.get("jobId"));//职业
                            bundle.putString("houseStyle", addRoommate_houseStyle_tv2.getText().toString().trim());//室友房间
                            intent.putExtras(bundle);
                            setResult(6, intent);
                            finish();
                        }
                    }
                } else {
                    if (getDataName()) {
                        RequestParams params = new RequestParams();
                        params.addBodyParameter("userId", userID);
                        params.addBodyParameter("sex", map_roommate.get("sex"));
                        params.addBodyParameter("name", map_roommate.get("name"));

                        params.addBodyParameter("phone", map_roommate.get("tel"));

                        params.addBodyParameter("age", addRoommate_age_tv2.getText().toString().trim());

                        params.addBodyParameter("interest", addRoommate_interest_tv2.getText().toString().trim());


                        params.addBodyParameter("constellation", addRoommate_constellation_tv2.getText().toString().trim());

                        if (flag_native) {
                            params.addBodyParameter("nativePlace", map_roommate.get("hometown_C"));
                        } else {
                            params.addBodyParameter("nativePlace", map_panda_add.get("nativePlace"));
                        }

                        if (flag_school) {
                            params.addBodyParameter("school", map_roommate.get("school_N"));
                        } else {
                            params.addBodyParameter("school", map_panda_add.get("school"));
                        }
                        params.addBodyParameter("houseName", addRoommate_houseStyle_tv2.getText().toString().trim());
                        if (flag_job) {
                            params.addBodyParameter("job", map_roommate.get("jobId"));
                        } else {
                            params.addBodyParameter("job", map_panda_add.get("job"));
                        }
                        if (!flag_icon) {
                            params.addBodyParameter("file", bitmapUtils.getBitmapFileFromDiskCache(map_panda_add.get("headImg")));

                        } else {
                            Bitmap bitmap = SubmitImage.getSmallBitmap(map_roommate.get("picturePath"));
                            File file = new File(dir, "roommate.jpeg");
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
                        params.addBodyParameter("houseId", map_panda_add.get("houseId"));
                        params.addBodyParameter("tenantId", map_panda_add.get("tenantId"));


                        if (httpUtils == null) {
                            httpUtils = new HttpUtils();
                        }

                        httpUtils.send(HttpRequest.HttpMethod.POST, UrlsUtils.urlReleaseRoommateModify, params, new RequestCallBack<String>() {
                            @Override
                            public void onSuccess(ResponseInfo<String> stringResponseInfo) {
                                Toast.makeText(getApplicationContext(), "室友修改成功,等待审核", Toast.LENGTH_LONG).show();
                                finish();
                            }

                            @Override
                            public void onFailure(HttpException e, String s) {
                                Toast.makeText(getApplicationContext(), "上传失败，请检查网络或填写的信息", Toast.LENGTH_SHORT).show();
                                Log.e("TAG", "解析失败" + s);
                            }
                        });

                    }
                }
        }
    }

    public boolean getDataName() {

        String addRoommate_name = addRoommate_name_et.getText().toString().trim();
        String null1 = judgeNull(addRoommate_name, "请输入室友姓名");
        if (null1 == null) {
            return false;
        }
        String addRoommate_tel = addRoommate_tel_et.getText().toString().trim();
        if (rb1.isChecked()) {
            map_roommate.put("sex", "男");
        } else if (rb2.isChecked()) {
            map_roommate.put("sex", "女");
        }
        Drawable drawableOne = addRoommate_icon_iv.getDrawable();
        if (drawableOne == null) {
            Toast.makeText(AddRoommateActivity.this, "头像不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        map_roommate.put("name", addRoommate_name);
        map_roommate.put("tel", addRoommate_tel);
        return true;
    }

    private String judgeNull(String data, String descriptor) {
        if (TextUtils.isEmpty(data)) {
            Toast.makeText(this, descriptor, Toast.LENGTH_SHORT).show();
            return null;
        }
        return "name";
    }


}
