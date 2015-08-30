package com.mobile.liujiucheng.sixninecheng.main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobile.liujiucheng.main.fragment.AcquireFragment;
import com.mobile.liujiucheng.main.fragment.FunctionFragment;
import com.mobile.liujiucheng.main.fragment.NewCenterFragment;
import com.mobile.liujiucheng.main.fragment.PandaFragment;

/**
 * 我的收藏
 * @author pc Mr_Gang
 */

public class MyCollectionActivity extends FragmentActivity implements OnClickListener {

    private FrameLayout mLayout;
    private TextView mTextSuccess;  //收藏的二手房
    private TextView mTextFail;     //收藏的出租房
    private TextView mTextPanda;    //收藏熊猫合租
    private TextView mTextAcquire;  //精准求租
    private ImageView mImageView;

    private View[] views = new View[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_my_collection);
        initView();
    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.iv_exit);
        mImageView.setOnClickListener(this);
        mLayout = (FrameLayout) findViewById(R.id.fl_layout_content);
        mTextSuccess = (TextView) findViewById(R.id.tv_success);
        mTextFail = (TextView) findViewById(R.id.tv_fails);
        mTextPanda = (TextView) findViewById(R.id.tv_panda);
        mTextAcquire = ((TextView) findViewById(R.id.tv_acquire));
        mTextSuccess.setOnClickListener(this);
        mTextFail.setOnClickListener(this);
        mTextPanda.setOnClickListener(this);
        mTextAcquire.setOnClickListener(this);
        views[0] = findViewById(R.id.search_view1);
        views[1] = findViewById(R.id.search_view2);
        views[2] = findViewById(R.id.search_view3);
        views[3] = findViewById(R.id.search_view4);
        chooseLayout(0);
    }

    private void chooseLayout(int index) {
        Fragment fragment = (Fragment) adapter.instantiateItem(mLayout, index);
        //2,替换操作
        adapter.setPrimaryItem(mLayout, 0, fragment);
        //3,提交
        adapter.finishUpdate(mLayout);
    }

    FragmentStatePagerAdapter adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public Fragment getItem(int arg0) {
            Fragment fragment = null;
            switch (arg0) {
                case 0:
                    fragment = new NewCenterFragment(); //二手房
                    break;
                case 1:
                    fragment = new PandaFragment();     //熊猫合租房
                    break;
                case 2:
                    fragment = new FunctionFragment();  //出租房
                    break;
                case 3:
                    fragment = new AcquireFragment();   //精准求租
                    break;
            }
            return fragment;
        }
    };

    @Override
    public void onClick(View v) {
        defaultColor();
        int id = v.getId();
        switch (id) {
            case R.id.tv_success:
                mTextSuccess.setTextColor(Color.parseColor("#66c6f2"));
                chooseLayout(0);
                for (int i = 0; i < views.length; i++) {
                    if (0 == i) {
                        views[i].setVisibility(View.VISIBLE);
                    } else {
                        views[i].setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case R.id.tv_fails:
                mTextFail.setTextColor(Color.parseColor("#66c6f2"));
                chooseLayout(1);
                for (int i = 0; i < views.length; i++) {
                    if (1 == i) {
                        views[i].setVisibility(View.VISIBLE);
                    } else {
                        views[i].setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case R.id.tv_panda:
                mTextPanda.setTextColor(Color.parseColor("#66c6f2"));
                chooseLayout(2);
                for (int i = 0; i < views.length; i++) {
                    if (2 == i) {
                        views[i].setVisibility(View.VISIBLE);
                    } else {
                        views[i].setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case R.id.tv_acquire:
                mTextAcquire.setTextColor(Color.parseColor("#66c6f2"));
                chooseLayout(3);
                for (int i = 0; i < views.length; i++) {
                    if (3 == i) {
                        views[i].setVisibility(View.VISIBLE);
                    } else {
                        views[i].setVisibility(View.INVISIBLE);
                    }
                }
                break;
            case R.id.iv_exit:
                finish();
                break;
        }
    }

    private void defaultColor() {
        mTextSuccess.setTextColor(Color.parseColor("#000000"));
        mTextFail.setTextColor(Color.parseColor("#000000"));
        mTextPanda.setTextColor(Color.parseColor("#000000"));
        mTextPanda.setTextColor(Color.parseColor("#000000"));
        mTextAcquire.setTextColor(Color.parseColor("#000000"));
    }
}
