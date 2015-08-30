package com.mobile.liujiucheng.sixninecheng.main.view;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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

public class ForgetPasswordActivity extends Activity implements View.OnClickListener{

    private ImageView iv_exit;
    private TextView tv_code;
    private String mValues;
    private EditText et_mtele;
    private int mCurrent = 60;
    private HttpUtils http;
    private String mResult = "";
    private EditText et_code,et_pass,et_pass2;
    private Button bu_register;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forget_password);

        initView();
    }

    private void initView(){
        if (sp == null) {
            sp = this.getSharedPreferences("load", MODE_PRIVATE);
        }
        iv_exit = ((ImageView) findViewById(R.id.iv_exit));
        tv_code = ((TextView) findViewById(R.id.tv_code));
        et_mtele = ((EditText) findViewById(R.id.et_mtele));  //电话号码
        et_code = ((EditText) findViewById(R.id.et_code));    //验证码
        et_pass = ((EditText) findViewById(R.id.et_pass));    //密码
        et_pass2 = ((EditText) findViewById(R.id.et_pass2));  //确认密码
        bu_register = ((Button) findViewById(R.id.bu_register));   //提交按钮

        iv_exit.setOnClickListener(this);
        tv_code.setOnClickListener(this);
        bu_register.setOnClickListener(this);
    }

    private String pwd;
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.iv_exit:
                finish();
                break;
            case R.id.tv_code:
                mValues = et_mtele.getText().toString().trim();
                boolean b = JudgePhoneUtils.mPhone(mValues);
                if (b) {
                    getValidateCode();    //倒计时
                    // 请求网络
                    netWork();
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "请你输入正确的电话号码", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.bu_register:
               //提交按钮动作
                if(!et_mtele.getText().toString().trim().equals(mValues)){
                    Toast.makeText(ForgetPasswordActivity.this, "电话号码输入错误", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if(!et_code.getText().toString().trim().equals(code)){
                    Toast.makeText(ForgetPasswordActivity.this, "验证码输入错误", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if(!et_pass.getText().toString().trim().equals(et_pass2.getText().toString().trim())){

                    Toast.makeText(ForgetPasswordActivity.this, "密码确认有误", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                pwd = et_pass.getText().toString().trim();

                //进行网络提交
                netRegister();
                break;

        }
    }

    /**
     * 获取验证码
     */
    private void getValidateCode() {
        if (ForgetPasswordActivity.this != null && !NetworkUtils.isNetworkConnected(ForgetPasswordActivity.this)) {
            ToastUtils.show(ForgetPasswordActivity.this, "请检查网络");
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
        NetHead head = new NetHead(ForgetPasswordActivity.this);
        RequestParams params = head.setHeader();

//        params.addBodyParameter("type", "getVerifiableCode");
        params.addBodyParameter("phone", mValues);
//        params.addBodyParameter("itype", "register");

        if (http == null) {
            http = new HttpUtils();
        }

        http.send(HttpRequest.HttpMethod.POST, UrlsUtils.forgetPasswordCode, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Toast.makeText(ForgetPasswordActivity.this, "网络连接超时", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        Toast.makeText(getApplicationContext(), "解析成功", Toast.LENGTH_LONG)
                                .show();
                        Log.e("TAG", "arg0.result====" + arg0.result);
                        mResult = arg0.result;
                        parseJson();
                    }
                });
    }

    private String status;
    private String code;
    protected void parseJson() {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            status = jsonObject.getString("status");
            if (status.equals("N")) {
                Toast.makeText(ForgetPasswordActivity.this, "输入有误", Toast.LENGTH_SHORT).show();
                return;
            }
            // 获取验证码
            JSONObject object = jsonObject.getJSONObject("data");
            code = object.getString("verifiableCode");
        } catch (Exception e) {
            Toast.makeText(ForgetPasswordActivity.this, "数据不正确", Toast.LENGTH_SHORT).show();
        }
    }

    private void netRegister() {
        NetHead head = new NetHead(ForgetPasswordActivity.this);
        RequestParams params = head.setHeader();
        params.addBodyParameter("phone", mValues);
        params.addBodyParameter("verifiableCode", code);
        params.addBodyParameter("pwd", pwd);
        if (http == null) {
            http = new HttpUtils();
        }
        http.send(HttpRequest.HttpMethod.POST, UrlsUtils.forgetPassword, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Toast.makeText(getApplicationContext(), "提交失败", Toast.LENGTH_LONG)
                                .show();
                        //Log.e("TAG", "解析失败");
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

