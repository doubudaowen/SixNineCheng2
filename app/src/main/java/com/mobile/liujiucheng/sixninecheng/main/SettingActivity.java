package com.mobile.liujiucheng.sixninecheng.main;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mobile.liujiucheng.main.adapter.MyAdapter;
import com.mobile.liujiucheng.sixninecheng.main.view.ModificationActivity;
import com.mobile.liujiucheng.sixninecheng.main.view.MyAccountActivity;

/**
 * 设置界面
 *
 * @author pc
 */
public class SettingActivity extends Activity implements OnClickListener {

    private List<String> mList;
    private ListView mListView;
    private MyAdapter mAdapter;
    private Context context;
    private ImageView mImageView;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_personal_setting);
        initView();
    }

    private void initView() {
        context = this;
        mButton = (Button) findViewById(R.id.bu_register_exit);
        mListView = (ListView) findViewById(R.id.my_person);
        mImageView = (ImageView) findViewById(R.id.iv_exit);
        mImageView.setOnClickListener(this);
        mButton.setOnClickListener(this);
        initData();
    }

    private void initData() {
        mList = new ArrayList<String>();
//        mList.add("我的手机号");
//        mList.add("我的昵称");
//        mList.add("常用地址");
        mList.add("我的账号");
        mList.add("修改密码");
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
//                    case 0:
//                        newStart(MyPhoneActivity.class);
//                        break;
//                    case 1:
//                        newStart(MyNameActivity.class);
//                        break;
//                    case 2:
//                        newStart(MyPositionActivity.class);
//                        break;
                    case 0:
                        newStart(MyAccountActivity.class);          //我的账号
                        break;
                    case 1:
                        newStart(ModificationActivity.class);       //修改密码
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
    private void newStart(Class<?> clazz) {
        Intent intent = new Intent(context, clazz);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_exit:
                finish();
                break;
            case R.id.bu_register_exit://退出登录
                SPUtils.destructionData(context);
                //SPUtils.deleteImageUrl();
                finish();
                break;
        }
    }
}
