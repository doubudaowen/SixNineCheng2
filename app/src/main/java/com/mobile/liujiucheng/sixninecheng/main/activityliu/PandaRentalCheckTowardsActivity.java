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

public class PandaRentalCheckTowardsActivity extends Activity {

    private ListView listView;
    private ImageView panda_rental_check_exit;
    private TextView panda_rental_check;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panda_rental_check_towards);
        listView= (ListView) findViewById(R.id.panda_rental_towards_list);
        listView.setAdapter(new ArrayAdapter(PandaRentalCheckTowardsActivity.this,R.layout.panda_rental_check_item,initData()));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String str=initData().get(position);
                Intent intent=new Intent();
                intent.putExtra("towards",str);
                setResult(2,intent);
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
        panda_rental_check.setText("朝向");
    }

    private List<String> initData() {
        List<String> list=new ArrayList<>();
        list.add("东");
        list.add("南");
        list.add("西");
        list.add("北");
        list.add("东南");
        list.add("西南");
        list.add("东北");
        list.add("西北");
        return list;
    }
}
