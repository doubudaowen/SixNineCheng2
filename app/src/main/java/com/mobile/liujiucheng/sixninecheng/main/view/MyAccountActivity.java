package com.mobile.liujiucheng.sixninecheng.main.view;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.sixninecheng.main.R;
import com.mobile.liujiucheng.sixninecheng.main.SPUtils;

public class MyAccountActivity extends Activity implements View.OnClickListener {
    private ImageView iv_exit;
    private TextView tv_commit;               //完成/修改
    //    private TextView tv_yonghuming;          //用户名
    private TextView tv_phone;                //手机
//    private TextView tv_address;              //常用地址

    private EditText et_yonghuming;
    private EditText et_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_account);

        initView();
    }

    private void initView() {
        iv_exit = ((ImageView) findViewById(R.id.iv_exit));
        tv_commit = ((TextView) findViewById(R.id.tv_commit));
//        tv_yonghuming = ((TextView) findViewById(R.id.tv_yonghuming));
        tv_phone = ((TextView) findViewById(R.id.tv_phone));
//        tv_address = ((TextView) findViewById(R.id.tv_address));

        et_yonghuming = ((EditText) findViewById(R.id.et_yonghuming));
        et_address = ((EditText) findViewById(R.id.et_address));

        iv_exit.setOnClickListener(this);
        tv_commit.setOnClickListener(this);
//        tv_yonghuming.setOnClickListener(this);
        tv_phone.setOnClickListener(this);
//        tv_address.setOnClickListener(this);

        et_yonghuming.setOnClickListener(this);
        et_address.setOnClickListener(this);

        if (!TextUtils.isEmpty(SPUtils.getPhone(MyAccountActivity.this))) {
            tv_phone.setText("手机号：" + SPUtils.getPhone(MyAccountActivity.this));
        }

        if (!TextUtils.isEmpty(SPUtils.getName(MyAccountActivity.this))) {
            et_yonghuming.setText(SPUtils.getName(MyAccountActivity.this));
        }

        if (!TextUtils.isEmpty(SPUtils.getPosition(MyAccountActivity.this))) {
            et_address.setText(SPUtils.getPosition(MyAccountActivity.this));
        }

        et_yonghuming.setEnabled(false);
        et_address.setEnabled(false);
    }

    private boolean iscommit = false;

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.iv_exit:
                finish();
                break;

            case R.id.tv_commit:
                if (iscommit) {
                    iscommit = false;
                    tv_commit.setText("修改");
                    et_yonghuming.setEnabled(false);
                    et_address.setEnabled(false);
                    //提交
                    if (!TextUtils.isEmpty(et_address.getText().toString().trim())) {    //提交地址
                        SPUtils.dataPosition(et_address.getText().toString().trim());
                    } else {
                        Toast.makeText(MyAccountActivity.this, this.getResources().getText(R.string.surrounding_content), Toast.LENGTH_SHORT).show();
                    }

                    //提交用户名
                    if(!TextUtils.isEmpty(et_yonghuming.getText().toString().trim())) {
                        network(et_yonghuming.getText().toString().trim());
                    }
                } else {
                    iscommit = true;
                    tv_commit.setText("完成");
                    et_yonghuming.setEnabled(true);
                    et_address.setEnabled(true);
                }
                break;
        }
    }


    private HttpUtils http;
    /**
     * 修改名称
     * @param name
     */
    private void network(String name) {

        NetHead head = new NetHead(MyAccountActivity.this);
        RequestParams params = head.setHeader();
        params.addBodyParameter("sid", SPUtils.getUserID(MyAccountActivity.this));
        params.addBodyParameter("name", name);
        final DialogUtils dialogUtils = new DialogUtils(MyAccountActivity.this);
        dialogUtils.show();
        if (http == null) {
            http = new HttpUtils();
        }
        http.send(HttpRequest.HttpMethod.POST, UrlsUtils.urlModifyName, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        dialogUtils.closeDialog();
                        Toast.makeText(MyAccountActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        dialogUtils.closeDialog();
                        Toast.makeText(MyAccountActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                        SPUtils.dataName(et_yonghuming.getText().toString().trim());
                    }
                });
    }
}
