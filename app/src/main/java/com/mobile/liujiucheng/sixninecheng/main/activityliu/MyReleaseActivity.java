package com.mobile.liujiucheng.sixninecheng.main.activityliu;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import com.mobile.liujiucheng.sixninecheng.main.SellActivity;
import com.mobile.liujiucheng.sixninecheng.main.activityliu.MyReleaseSeePandaActivity;
import com.mobile.liujiucheng.sixninecheng.main.activityliu.MyReleaseSeeReleaseActivity;
import com.mobile.liujiucheng.sixninecheng.main.activityliu.MyReleaseSeeRentActivity;
import com.mobile.liujiucheng.sixninecheng.main.activityliu.MyReleaseSeeSecondActivity;
import com.mobile.liujiucheng.sixninecheng.main.activityliu.PandaRentalActivity;
import com.mobile.liujiucheng.sixninecheng.main.activityliu.ReleaseActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyReleaseActivity extends Activity {
    private ImageView exit;//退出
    private RadioGroup radioGroup;//单选按钮
    private RadioButton rb_second, rb_rent, rb_panda, rb_release;
    private ListView listView;//显示列表
    private RelativeLayout rl_empty;//没有发布时显示
    private DialogUtils utils;
    private Context context;
    private String userID;
    private HttpUtils httpUtils;
    private View view1,view2,view3,view4;
    //存储列表信息
    private List<Map<String, String>> list_second, list_rent, list_panda, list_release;
    //声明各个adapter
    private MyReleaseListAdapter adapter_second, adapter_rent, adapter_panda, adapter_release;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_release);
        context = MyReleaseActivity.this;
        userID = SPUtils.getUserID(context);
        if (userID == null) {
            Intent intent = new Intent(context, LoagingActivity.class);
            startActivity(intent);
        }
        initView();
        showList();
    }

    private void showList() {
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //设置默认的显示列表
        rb_second.setChecked(true);
        loadSecondList();
        if(adapter_second==null){

        }
        rb_second.setTextColor(Color.parseColor("#5fbfeb"));
        rb_rent.setTextColor(Color.parseColor("#333333"));
        rb_panda.setTextColor(Color.parseColor("#333333"));
        rb_release.setTextColor(Color.parseColor("#333333"));
        view1.setVisibility(View.VISIBLE);
        view2.setVisibility(View.INVISIBLE);
        view3.setVisibility(View.INVISIBLE);
        view4.setVisibility(View.INVISIBLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, MyReleaseSeeSecondActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("tenantId", list_second.get(position).get("tenantId"));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        //各个radioButton的初始化
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.my_release_rb1:
                        loadSecondList();
                        rb_second.setTextColor(Color.parseColor("#5fbfeb"));
                        rb_rent.setTextColor(Color.parseColor("#333333"));
                        rb_panda.setTextColor(Color.parseColor("#333333"));
                        rb_release.setTextColor(Color.parseColor("#333333"));
                        view1.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.INVISIBLE);
                        view3.setVisibility(View.INVISIBLE);
                        view4.setVisibility(View.INVISIBLE);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(context, MyReleaseSeeSecondActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("tenantId", list_second.get(position).get("tenantId"));
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        break;
                    case R.id.my_release_rb2:
                        loadRentList();
                        rb_second.setTextColor(Color.parseColor("#333333"));
                        rb_rent.setTextColor(Color.parseColor("#5fbfeb"));
                        rb_panda.setTextColor(Color.parseColor("#333333"));
                        rb_release.setTextColor(Color.parseColor("#333333"));
                        view2.setVisibility(View.VISIBLE);
                        view1.setVisibility(View.INVISIBLE);
                        view3.setVisibility(View.INVISIBLE);
                        view4.setVisibility(View.INVISIBLE);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(context, MyReleaseSeeRentActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("tenantId", list_rent.get(position).get("tenantId"));
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        break;
                    case R.id.my_release_rb3:
                        loadPandaList();
                        rb_second.setTextColor(Color.parseColor("#333333"));
                        rb_panda.setTextColor(Color.parseColor("#5fbfeb"));
                        rb_rent.setTextColor(Color.parseColor("#333333"));
                        rb_release.setTextColor(Color.parseColor("#333333"));
                        view3.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.INVISIBLE);
                        view1.setVisibility(View.INVISIBLE);
                        view4.setVisibility(View.INVISIBLE);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(context, MyReleaseSeePandaActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("tenantId", list_panda.get(position).get("tenantId"));
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        break;
                    case R.id.my_release_rb4:
                        loadReleaseList();
                        rb_second.setTextColor(Color.parseColor("#333333"));
                        rb_release.setTextColor(Color.parseColor("#5fbfeb"));
                        rb_panda.setTextColor(Color.parseColor("#333333"));
                        rb_rent.setTextColor(Color.parseColor("#333333"));
                        view4.setVisibility(View.VISIBLE);
                        view2.setVisibility(View.INVISIBLE);
                        view3.setVisibility(View.INVISIBLE);
                        view1.setVisibility(View.INVISIBLE);
                        listView.setAdapter(adapter_release);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent intent = new Intent(context, MyReleaseSeeReleaseActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("tenantId", list_release.get(position).get("tenantId"));
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        break;
                }
            }
        });
    }


    //请求"二手房"列表信息
    private void loadSecondList() {
        //显示dialog
        if (utils == null) {
            utils = new DialogUtils(context);
        }
        utils.show();
        //判断list是否为空
        if (list_second == null) {
            list_second = new ArrayList<>();
        }
        if (list_second != null) {
            list_second.clear();
        }
        boolean connected = NetworkUtils.isConnection(context);
        if (connected) {
            // 访问网络
            NetHead head = new NetHead(context);
            RequestParams params = head.setHeader();
            params.addBodyParameter("userId", userID);
            params.addBodyParameter("type", "findUserHouse");
            params.addBodyParameter("houseT", "2");
            if (httpUtils == null) {
                httpUtils = new HttpUtils();
            }
            httpUtils.send(HttpRequest.HttpMethod.POST, UrlsUtils.urlMyReleaseSecond, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                    String str = objectResponseInfo.result;
                    JSONObject object = null;
                    try {
                        object = new JSONObject(str);
                        if (object.getString("status").equals("Y")) {
                            JSONArray jsonArray = object.getJSONArray("data");
                            if (jsonArray.length() != 0) {
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                                    Map<String, String> map_for_list = new HashMap<String, String>();
                                    map_for_list.put("createTimeStr", jsonObject.getString("pubTime"));
                                    map_for_list.put("title", jsonObject.getString("title"));
                                    map_for_list.put("click", "被浏览了" + jsonObject.getString("click") + "次");
                                    map_for_list.put("type", "二手房");
                                    map_for_list.put("tenantId", Integer.toString(jsonObject.getInt("sid")));
                                    list_second.add(map_for_list);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (adapter_second == null) {
                        adapter_second = new MyReleaseListAdapter(list_second, context);
                    }
                    if (adapter_second != null) {
                        adapter_second.notifyDataSetChanged();
                    }
                    listView.setAdapter(adapter_second);
                    utils.closeDialog();
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(context, "数据加载失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    utils.closeDialog();
                }
            });
        }
    }

    //请求"出租房"列表信息
    private void loadRentList() {
        //显示dialog
        if (utils == null) {
            utils = new DialogUtils(context);
        }
        utils.show();
        //判断list是否为空
        if (list_rent == null) {
            list_rent = new ArrayList<>();
        }
        if (list_rent != null) {
            list_rent.clear();
        }
        boolean connected = NetworkUtils.isConnection(context);
        if (connected) {
            // 访问网络
            NetHead head = new NetHead(context);
            RequestParams params = head.setHeader();
            params.addBodyParameter("userId", userID);
            params.addBodyParameter("type", "findUserHouse");
            params.addBodyParameter("houseT", "1");
            if (httpUtils == null) {
                httpUtils = new HttpUtils();
            }
            httpUtils.send(HttpRequest.HttpMethod.POST, UrlsUtils.urlMyReleaseSecond, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                    String str = objectResponseInfo.result;
                    JSONObject object = null;
                    try {
                        object = new JSONObject(str);
                        if (object.getString("status").equals("Y")) {
                            JSONArray jsonArray = object.getJSONArray("data");
                            if (jsonArray.length() != 0) {
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                                    Map<String, String> map_for_list = new HashMap<String, String>();
                                    map_for_list.put("createTimeStr", jsonObject.getString("pubTime"));
                                    map_for_list.put("title", jsonObject.getString("title"));
                                    map_for_list.put("click", "被浏览了" + jsonObject.getString("click") + "次");
                                    map_for_list.put("type", "出租");
                                    map_for_list.put("tenantId", Integer.toString(jsonObject.getInt("sid")));
                                    list_rent.add(map_for_list);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (adapter_rent == null) {
                        adapter_rent = new MyReleaseListAdapter(list_rent, context);
                    }
                    if (adapter_rent != null) {
                        adapter_rent.notifyDataSetChanged();
                    }
                    listView.setAdapter(adapter_rent);
                    utils.closeDialog();
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(context, "数据加载失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    utils.closeDialog();
                }
            });
        }
    }

    //请求"熊猫合租"列表信息
    private void loadPandaList() {
        //显示dialog
        if (utils == null) {
            utils = new DialogUtils(context);
        }
        utils.show();
        //判断list是否为空
        if (list_panda == null) {
            list_panda = new ArrayList<>();
        }
        if (list_panda != null) {
            list_panda.clear();
        }
        boolean connected = NetworkUtils.isConnection(context);
        if (connected) {
            // 访问网络
            NetHead head = new NetHead(context);
            RequestParams params = head.setHeader();
            params.addBodyParameter("userId", userID);
            params.addBodyParameter("type", "findUserHouse");
            params.addBodyParameter("houseT", "3");
            if (httpUtils == null) {
                httpUtils = new HttpUtils();
            }
            httpUtils.send(HttpRequest.HttpMethod.POST, UrlsUtils.urlMyReleaseSecond, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                    String str = objectResponseInfo.result;
                    JSONObject object = null;
                    try {
                        object = new JSONObject(str);
                        if (object.getString("status").equals("Y")) {
                            JSONArray jsonArray = object.getJSONArray("data");
                            if (jsonArray.length() != 0) {
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                                    Map<String, String> map_for_list = new HashMap<String, String>();
                                    map_for_list.put("createTimeStr", jsonObject.getString("pubTime"));
                                    map_for_list.put("title", jsonObject.getString("title"));
                                    map_for_list.put("click", "被浏览了" + jsonObject.getString("click") + "次");
                                    map_for_list.put("type", "合租");
                                    map_for_list.put("tenantId", Integer.toString(jsonObject.getInt("sid")));
                                    list_panda.add(map_for_list);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (adapter_panda == null) {
                        adapter_panda = new MyReleaseListAdapter(list_panda, context);
                    }
                    if (adapter_panda != null) {
                        adapter_panda.notifyDataSetChanged();
                    }
                    listView.setAdapter(adapter_panda);
                    utils.closeDialog();
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(context, "数据加载失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    utils.closeDialog();
                }
            });
        }
    }

    //请求"精准求组"列表信息
    private void loadReleaseList() {
        //显示dialog
        if (utils == null) {
            utils = new DialogUtils(context);
        }
        utils.show();
        //判断list是否为空
        if (list_release == null) {
            list_release = new ArrayList<>();
        }
        if (list_release != null) {
            list_release.clear();
        }
        boolean connected = NetworkUtils.isConnection(context);
        if (connected) {
            // 访问网络
            NetHead head = new NetHead(context);
            RequestParams params = head.setHeader();
            params.addBodyParameter("userId", userID);
            if (httpUtils == null) {
                httpUtils = new HttpUtils();
            }
            httpUtils.send(HttpRequest.HttpMethod.POST, UrlsUtils.urlReleaseLoadInfo, params, new RequestCallBack<String>() {
                @Override
                public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                    String str = objectResponseInfo.result;
                    JSONObject object = null;
                    try {
                        object = new JSONObject(str);
                        if (object.getString("status").equals("Y")) {
                            JSONArray jsonArray = object.getJSONArray("data");
                            if (jsonArray.length() != 0) {
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(j);
                                    Map<String, String> map_for_list = new HashMap<String, String>();
                                    map_for_list.put("createTimeStr", jsonObject.getString("createTimeStr"));
                                    map_for_list.put("title", jsonObject.getString("title"));
                                    map_for_list.put("type", "求租");
                                    map_for_list.put("tenantId", Integer.toString(jsonObject.getInt("id")));
                                    list_release.add(map_for_list);
                                }
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (adapter_release == null) {
                        adapter_release = new MyReleaseListAdapter(list_release, context);
                    }
                    if (adapter_release != null) {
                        adapter_release.notifyDataSetChanged();
                    }
                    listView.setAdapter(adapter_release);
                    utils.closeDialog();
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Toast.makeText(context, "数据加载失败，请稍后再试", Toast.LENGTH_SHORT).show();
                    utils.closeDialog();
                }
            });
        }
    }

    class MyReleaseListAdapter extends BaseAdapter {
        private List<Map<String, String>> list;
        private LayoutInflater inflater;

        public MyReleaseListAdapter(List<Map<String, String>> list, Context context) {
            this.list = list;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = inflater.inflate(R.layout.my_release_listview_adapter, null);
                viewHolder.textView1 = (TextView) convertView.findViewById(R.id.my_release_listView_tv1);
                viewHolder.textView2 = (TextView) convertView.findViewById(R.id.my_release_listView_tv2);
                viewHolder.textView3 = (TextView) convertView.findViewById(R.id.my_release_listView_tv3);
                viewHolder.button1 = (Button) convertView.findViewById(R.id.my_release_button_modify);
                viewHolder.button2 = (Button) convertView.findViewById(R.id.my_release_button_delete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.textView1.setText(list.get(position).get("title"));
            viewHolder.textView2.setText(list.get(position).get("createTimeStr"));
            viewHolder.textView3.setText(list.get(position).get("click"));
            //修改按钮的监听
            viewHolder.button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Bundle bundle = new Bundle();
                    if ("求租".equals(list.get(position).get("type"))) {
                        Intent intent = new Intent(context, ReleaseActivity.class);
                        bundle.putString("from", "求租");
                        bundle.putString("tenantId", list.get(position).get("tenantId"));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if ("合租".equals(list.get(position).get("type"))) {
                        Intent intent = new Intent(context, PandaRentalActivity.class);
                        bundle.putString("from", "合租");
                        bundle.putString("houseId", list.get(position).get("tenantId"));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else if ("二手房".equals(list.get(position).get("type"))) {
                        Intent intent = new Intent(context, SellActivity.class);
                        bundle.putString("from", "二手房");
                        bundle.putString("tenantId", list.get(position).get("tenantId"));
                        bundle.putString("tag", "2");
                        intent.putExtras(bundle);
                        startActivity(intent);

                    } else if ("出租".equals(list.get(position).get("type"))) {
                        Intent intent = new Intent(context, SellActivity.class);
                        bundle.putString("from", "出租");
                        bundle.putString("tenantId", list.get(position).get("tenantId"));
                        bundle.putString("tag", "1");
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            });

            //删除按钮的监听
            viewHolder.button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("您确定要删除该条消息吗？");

                    builder.setNegativeButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            RequestParams params = new RequestParams();
                            params.addBodyParameter("userId", userID);
                            if (httpUtils == null) {
                                httpUtils = new HttpUtils();
                            }
                            String url = null;
                            if ("求租".equals(list.get(position).get("type"))) {
                                params.addBodyParameter("tenantId", list.get(position).get("tenantId"));//消息的ID
                                url = UrlsUtils.urlReleaseDelete;
                            } else if ("合租".equals(list.get(position).get("type"))) {
                                params.addBodyParameter("houseId", list.get(position).get("tenantId"));
                                url = UrlsUtils.urlMyReleasePandaDelete;
                            } else if ("二手房".equals(list.get(position).get("type"))) {
                                params.addBodyParameter("houseId", list.get(position).get("tenantId"));
                                url = UrlsUtils.urlMyReleaseSecondDelete;
                            } else if ("出租".equals(list.get(position).get("type"))) {
                                params.addBodyParameter("houseId", list.get(position).get("tenantId"));
                                url = UrlsUtils.urlMyReleaseRentDelete;
                            }
                            httpUtils.send(HttpRequest.HttpMethod.POST, url, params, new RequestCallBack<String>() {
                                @Override
                                public void onSuccess(ResponseInfo<String> objectResponseInfo) {
                                    Log.e("SSSSSS", "SSSSSSS" + objectResponseInfo.result);
                                    try {
                                        JSONObject jsonObject = new JSONObject(objectResponseInfo.result);
                                        if ("Y".equals(jsonObject.getString("status"))) {
                                            if ("求租".equals(list.get(position).get("type"))) {
                                                list_release.remove(position);
                                                if (list_release.size() == 0) {
                                                    adapter_release.notifyDataSetChanged();
                                                    rl_empty.setVisibility(View.VISIBLE);
                                                    listView.setVisibility(View.GONE);
                                                } else {
                                                    adapter_release.notifyDataSetChanged();
                                                }
                                            } else if ("合租".equals(list.get(position).get("type"))) {
                                                list_panda.remove(position);

                                                if (list_panda.size() == 0) {
                                                    adapter_panda.notifyDataSetChanged();
                                                    rl_empty.setVisibility(View.VISIBLE);
                                                    listView.setVisibility(View.GONE);
                                                } else {
                                                    adapter_panda.notifyDataSetChanged();
                                                }
                                            } else if ("二手房".equals(list.get(position).get("type"))) {
                                                list_second.remove(position);
                                                if (list_second.size() == 0) {
                                                    adapter_second.notifyDataSetChanged();
                                                    listView.setVisibility(View.GONE);
                                                    rl_empty.setVisibility(View.VISIBLE);
                                                } else {
                                                    adapter_second.notifyDataSetChanged();
                                                }
                                            } else if ("出租".equals(list.get(position).get("type"))) {
                                                list_rent.remove(position);

                                                if (list_rent.size() == 0) {
                                                    adapter_rent.notifyDataSetChanged();
                                                    rl_empty.setVisibility(View.VISIBLE);
                                                    listView.setVisibility(View.GONE);
                                                } else {
                                                    adapter_rent.notifyDataSetChanged();
                                                }
                                            }
                                        }
                                        Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onFailure(HttpException e, String s) {
                                    Log.e("false", "false" + s);
                                    Toast.makeText(context, "操作失败，请稍后再试", Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog.dismiss();
                        }
                    });
                    builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            });
            return convertView;
        }

        public class ViewHolder {
            public TextView textView1, textView2, textView3;
            public Button button1, button2;
        }
    }

    private void initView() {
        exit = (ImageView) findViewById(R.id.my_release_exit);
        radioGroup = (RadioGroup) findViewById(R.id.my_release_rg);
        rb_second = (RadioButton) findViewById(R.id.my_release_rb1);
        rb_rent = (RadioButton) findViewById(R.id.my_release_rb2);
        rb_panda = (RadioButton) findViewById(R.id.my_release_rb3);
        rb_release = (RadioButton) findViewById(R.id.my_release_rb4);
        listView = (ListView) findViewById(R.id.my_release_listView);
        rl_empty = (RelativeLayout) findViewById(R.id.my_release_empty_message);
        view1=findViewById(R.id.view1);
        view2=findViewById(R.id.view2);
        view3=findViewById(R.id.view3);
        view4=findViewById(R.id.view4);
    }

}
