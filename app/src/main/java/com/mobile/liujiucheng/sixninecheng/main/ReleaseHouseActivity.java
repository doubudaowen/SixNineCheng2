package com.mobile.liujiucheng.sixninecheng.main;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.liujiucheng.main.adapter.MyAdapter;
import com.mobile.liujiucheng.sixninecheng.main.activityliu.PandaRentalActivity;
import com.mobile.liujiucheng.sixninecheng.main.activityliu.ReleaseActivity;

/**
 * 发布房源
 *
 * @author pc
 */
public class ReleaseHouseActivity extends BaseActivity implements OnClickListener {

    private List<String> mList;
    private ListView mListView;
    private MyAdapter mAdapter;
    private Context context;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_release_class);
        initView();
    }

    private void initView() {
        context = this;
        mListView = (ListView) findViewById(R.id.lv_year);
        mImageView = (ImageView) findViewById(R.id.iv_exit);
        mImageView.setOnClickListener(this);
        initData();
    }

    private void initData() {
        mList = new ArrayList<String>();
        mList.add("二手房");
        mList.add("出租房");
        mList.add("熊猫合租");
        setMyAdapter();
    }

    private void setMyAdapter() {
        if (mAdapter == null) {
            mAdapter = new MyAdapter(mList, context) {
                @Override
                public View getView(int position, View convertView,
                                    ViewGroup parent) {
                    View view;
                    if (convertView == null) {
                        view = View.inflate(context,
                                R.layout.layout_listview_item, null);
                    } else {
                        view = convertView;
                    }
                    TextView tv = (TextView) view.findViewById(R.id.tv_item);
                    tv.setText(mList.get(position));
                    return view;
                }
            };
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.notifyDataSetChanged();
        }
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0://二手房
                        if (TextUtils.isEmpty(SPUtils.getPhone(context))) {
                            Intent intent = new Intent(context, LoagingActivity.class);
                            startActivity(intent);
                        } else {
                            newStart(SellActivity.class, "2");
                        }
                        break;
                    case 1://出租房
                        if (TextUtils.isEmpty(SPUtils.getPhone(context))) {
                            Intent intent = new Intent(context, LoagingActivity.class);
                            startActivity(intent);
                        } else {
                            newStart(SellActivity.class, "1");
                        }
                        break;

                    case 2://熊猫合租
                        if (TextUtils.isEmpty(SPUtils.getPhone(context))) {
                            Intent intent = new Intent(context, LoagingActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(context, PandaRentalActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("from", "");
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }

                        Toast.makeText(ReleaseHouseActivity.this, "熊猫合租", Toast.LENGTH_LONG).show();
                        //接入点
                        break;

                }
            }
        });
    }

    /**
     * 开启一个新的界面
     *
     * @param clazz
     */
    private void newStart(@SuppressWarnings("rawtypes") Class clazz, String tag) {
        Intent intent = new Intent(context, clazz);
        intent.putExtra("tag", tag);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_exit:
                finish();
                break;
        }
    }
}
