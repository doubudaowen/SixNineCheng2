package com.mobile.liujiucheng.sixninecheng.main.activityliu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobile.liujiucheng.sixninecheng.main.R;

public class PandaRentalStateActivity extends Activity implements View.OnClickListener{

    private ImageView panda_rental_state_exit;
    private EditText panda_rental_state_et;
    private LinearLayout panda_rental_state_remove;
    private TextView panda_rental_state_send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panda_rental_state);
        initView();
    }

    private void initView() {
        panda_rental_state_exit= (ImageView) findViewById(R.id.panda_rental_state_exit);
        panda_rental_state_et= (EditText) findViewById(R.id.panda_rental_state_et);
        panda_rental_state_remove= (LinearLayout) findViewById(R.id.panda_rental_state_remove);
        panda_rental_state_send= (TextView) findViewById(R.id.panda_rental_state_send);
        panda_rental_state_send.setOnClickListener(this);
        panda_rental_state_exit.setOnClickListener(this);
        panda_rental_state_remove.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.panda_rental_state_exit:
                finish();
                break;
            case R.id.panda_rental_state_remove:
                panda_rental_state_et.setText("");
                break;
            case R.id.panda_rental_state_send:
                Intent intent=new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("state",panda_rental_state_et.getText().toString().trim());
                intent.putExtras(bundle);
                setResult(3,intent);
                finish();
                break;
        }
    }
}
