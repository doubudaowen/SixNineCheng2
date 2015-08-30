package com.mobile.liujiucheng.sixninecheng.main.activityliu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mobile.liujiucheng.sixninecheng.main.R;

import java.util.ArrayList;
import java.util.List;

public class PandaRentalCheckPayActivity extends Activity {

    private ListView listView;
    private ImageView panda_rental_check_exit;
    private TextView panda_rental_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panda_rental_check_pay);
        listView= (ListView) findViewById(R.id.panda_rental_towards_list);
        listView.setAdapter(new ArrayAdapter(PandaRentalCheckPayActivity.this,R.layout.panda_rental_check_item,initData()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent();
                intent.putExtra("payment",Integer.toString(position));
                intent.putExtra("paymentString",initData().get(position));
                setResult(4, intent);
                finish();
            }
        });
        panda_rental_check_exit= (ImageView) findViewById(R.id.panda_rental_check_exit);
        panda_rental_check_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        panda_rental_check= (TextView) findViewById(R.id.panda_rental_check);
        panda_rental_check.setText("付款方式");
    }

    private List<String> initData() {
        List<String> list=new ArrayList<>();
        list.add("月付");
        list.add("压一付一");
        list.add("压一付二");
        list.add("压一付三");
        list.add("压二付一");
        list.add("压二付二");
        list.add("压二付三");
        list.add("半年付");
        list.add("年付");
        list.add("面议");
        return list;
    }


}
