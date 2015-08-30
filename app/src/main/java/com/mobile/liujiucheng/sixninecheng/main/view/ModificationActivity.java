package com.mobile.liujiucheng.sixninecheng.main.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import com.mobile.liujiucheng.main.utils.JudgePhoneUtils;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.NetworkUtils;
import com.mobile.liujiucheng.main.utils.ToastUtils;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.sixninecheng.main.R;

import org.json.JSONException;
import org.json.JSONObject;

public class ModificationActivity extends Activity implements View.OnClickListener {

    private ImageView iv_exit;
    private TextView tv_code;   //获取验证码
    private EditText et_mtele;  //输入手机号
    private EditText et_code;   //输入验证码
    private EditText et_pass0;   //输入旧密码
    private EditText et_pass1;   //输入新密码
    private EditText et_pass2;   //确认密码
    private Button bu_register;  //确认修改

    private String mValues;
    private int mCurrent = 60;
    private HttpUtils http;
    private String mResult = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_modification);

        initView();
        getPws();
    }

    private void initView() {
        if (sp == null) {
            sp = this.getSharedPreferences("load", MODE_PRIVATE);
        }
        iv_exit = ((ImageView) findViewById(R.id.iv_exit));
        tv_code = ((TextView) findViewById(R.id.tv_code));
        et_mtele = ((EditText) findViewById(R.id.et_mtele));
        et_code = ((EditText) findViewById(R.id.et_code));
        et_pass0 = ((EditText) findViewById(R.id.et_pass0));
        et_pass1 = ((EditText) findViewById(R.id.et_pass1));
        et_pass2 = ((EditText) findViewById(R.id.et_pass2));
        bu_register = ((Button) findViewById(R.id.bu_register));

        iv_exit.setOnClickListener(this);
        tv_code.setOnClickListener(this);
        bu_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.iv_exit:
                finish();
                break;

            case R.id.tv_code:    //获取验证码
                mValues = et_mtele.getText().toString().trim();
                boolean b = JudgePhoneUtils.mPhone(mValues);
                if (b) {
                    getValidateCode();    //倒计时
                    // 请求网络
                    netWork();
                } else {
                    Toast.makeText(ModificationActivity.this, "请你输入正确的电话号码", Toast.LENGTH_SHORT)
                            .show();
                }
                break;

            case R.id.bu_register:             //提交修改
                //提交按钮动作
                if(!et_mtele.getText().toString().trim().equals(mValues)){
                    Toast.makeText(ModificationActivity.this, "电话号码输入错误", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if(!et_code.getText().toString().trim().equals(code)){
                    Toast.makeText(ModificationActivity.this, "验证码输入错误", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if(!TextUtils.isEmpty(oldPws)){
                    if(!oldPws.equals(et_pass0.getText().toString().trim())){             //这样不合理，所以没加
                        Toast.makeText(ModificationActivity.this, "旧密码输入有误", Toast.LENGTH_SHORT)
                                .show();
                        return;
                    }
                }
                if(!et_pass1.getText().toString().trim().equals(et_pass2.getText().toString().trim())){

                    Toast.makeText(ModificationActivity.this, "密码确认有误", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                pwd = et_pass1.getText().toString().trim();
                //进行网络提交
                netRegister();
                break;
        }
    }
    private String pwd;
    /**
     * 获取验证码
     */
    private void getValidateCode() {
        if (ModificationActivity.this != null && !NetworkUtils.isNetworkConnected(ModificationActivity.this)) {
            ToastUtils.show(ModificationActivity.this, "请检查网络");
            return;
        }
        tv_code.setText("正在发送验证码");
        tv_code.setClickable(false);

        new Thread() {
            public void run() {
                while (mCurrent > 0) {
                    countDown();
                }
            }

            private void countDown() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mCurrent == 0) {
                            tv_code.setClickable(true);
                            tv_code.setText("获取验证码");
                            mCurrent = 60;
                        } else {
                            tv_code.setText(--mCurrent + "秒后尝试");
                            tv_code.setClickable(false);
                        }
                    }
                });
            }

            ;
        }.start();
    }

    private void netWork() {
        NetHead head = new NetHead(ModificationActivity.this);
        RequestParams params = head.setHeader();

        params.addBodyParameter("type", "getVerifiableCode");
        params.addBodyParameter("phone", mValues);
        params.addBodyParameter("itype", "modifyPwd");

        if (http == null) {
            http = new HttpUtils();
        }

        http.send(HttpRequest.HttpMethod.POST, UrlsUtils.urlModificationCode, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Toast.makeText(ModificationActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        Toast.makeText(getApplicationContext(), "获取成功", Toast.LENGTH_LONG)
                                .show();
                        Log.e("TAG", "arg0.result====" + arg0.result);
                        mResult = arg0.result;
                        parseJson();
                    }
                });
    }

    private String status;
    private String code;  //验证码
    protected void parseJson() {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            status = jsonObject.getString("status");
            if (status.equals("N")) {
                Toast.makeText(ModificationActivity.this, "输入有误", Toast.LENGTH_SHORT).show();
                return;
            }
            // 获取验证码
            JSONObject object = jsonObject.getJSONObject("data");
            code = object.getString("verifiableCode");
        } catch (Exception e) {
            Toast.makeText(ModificationActivity.this, "数据不正确", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取SharedPreference里面取密码
     */
    private SharedPreferences sp;
    private String oldPws;
    private void getPws() {
        sp = ModificationActivity.this.getSharedPreferences("load", MODE_PRIVATE);
        oldPws = sp.getString("pwd", "");
    }

    private void netRegister() {
        NetHead head = new NetHead(ModificationActivity.this);
        RequestParams params = head.setHeader();
        params.addBodyParameter("type", "registerAndModifyPwd");
        params.addBodyParameter("phone", mValues);
        params.addBodyParameter("itype", "modifyPwd");
        params.addBodyParameter("pwd", pwd);
        if (http == null) {
            http = new HttpUtils();
        }
        http.send(HttpRequest.HttpMethod.POST, UrlsUtils.urlModification, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Toast.makeText(getApplicationContext(), "提交失败", Toast.LENGTH_LONG)
                                .show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {

                        Log.e("TAGGGGGGG", "arg0.result====" + arg0.result);
                        mResult = arg0.result;
                        try {
                            JSONObject object = new JSONObject(mResult);
                            String status = object.getString("status");
                            if(status.equals("Y")){
                                //将密码保存
                                SharedPreferences.Editor edit = sp.edit();
                                edit.putString("pwd", pwd);
                                edit.commit();
                                Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_LONG)
                                        .show();
                            }
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
