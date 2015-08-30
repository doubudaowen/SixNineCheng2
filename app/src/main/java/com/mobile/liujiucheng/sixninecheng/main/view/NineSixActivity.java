package com.mobile.liujiucheng.sixninecheng.main.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lidroid.xutils.BitmapUtils;
import com.mobile.liujiucheng.sixninecheng.main.R;

public class NineSixActivity extends Activity implements View.OnClickListener{

    private ImageView iv_back;
    private ImageView iv_main;
    private TextView title;
    private RelativeLayout RL;
    private int position;
    private BitmapUtils bit;
    private String imgUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nine_six);

        position = intent.getIntExtra("position", 0);
        imgUrl = intent.getStringExtra("img");
        initView();
    }

    private void initView(){
        iv_back = ((ImageView) findViewById(R.id.iv_back));
        iv_main = ((ImageView) findViewById(R.id.iv_main));
        title = ((TextView) findViewById(R.id.title));
        RL = ((RelativeLayout) findViewById(R.id.RL));

        if(bit == null){
            bit = new BitmapUtils(getApplication());
            bit.display(iv_main,imgUrl);
        }else {
            bit.display(iv_main,imgUrl);
        }

        if(position == 0){
            title.setText("六九城");
            RL.setBackgroundColor(Color.parseColor("#5fbfeb"));
        }
        if(position == 1){
            title.setText("合租房屋");
            RL.setBackgroundColor(Color.parseColor("#faaf40"));
        }
        if(position == 2){
            title.setText("合作申请");
            RL.setBackgroundColor(Color.parseColor("#b3d465"));
        }

        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_back:
              finish();
              break;
        }
    }
}
