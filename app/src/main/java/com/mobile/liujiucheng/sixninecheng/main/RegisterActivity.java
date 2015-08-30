package com.mobile.liujiucheng.sixninecheng.main;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
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
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mobile.liujiucheng.main.utils.JudgePhoneUtils;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.NetworkUtils;
import com.mobile.liujiucheng.main.utils.ToastUtils;
import com.mobile.liujiucheng.main.utils.UrlsUtils;

/**
 * 注册界面
 *
 * @author pc
 */
public class RegisterActivity extends Activity implements OnClickListener {

    private int mCurrent = 60;
    private TextView mTextView;

    private EditText et_mtele;// 电话号码
    private EditText et_code;// 验证码
    private EditText et_pass;// 密码
    private EditText et_pass2;//再次确认密码
    private EditText et_licheng;//昵称

    private Button mRegister;
    private Context context;

    private SharedPreferences sp;
    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_register);
        initView();
    }

    private void initView() {
        mImageView = (ImageView) findViewById(R.id.iv_exit);
        mImageView.setOnClickListener(this);
        if (sp == null) {
            sp = this.getSharedPreferences("load", MODE_PRIVATE);
        }
        context = this;
        mTextView = (TextView) findViewById(R.id.tv_code);

        et_mtele = (EditText) findViewById(R.id.et_mtele);
        et_code = (EditText) findViewById(R.id.et_code);
        et_pass = (EditText) findViewById(R.id.et_pass);
        et_pass2 = (EditText) findViewById(R.id.et_pass2);
        mRegister = (Button) findViewById(R.id.bu_register);
        et_licheng = ((EditText) findViewById(R.id.et_licheng));

        mTextView.setOnClickListener(this);
        mRegister.setOnClickListener(this);

        et_mtele.setOnClickListener(this);
        et_code.setOnClickListener(this);
        et_pass.setOnClickListener(this);
        et_pass2.setOnClickListener(this);
    }

    private void save() {
        Editor edit = sp.edit();
        edit.putBoolean("loadingStatu", true);
        edit.putString("phone", mPhone);
        edit.putString("userID", userID);
        edit.putString("pwd",mPass);
        edit.putString("name",name);
        edit.commit();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.iv_exit:
                finish();
                break;
            case R.id.tv_code:// 验证码
                mValues = et_mtele.getText().toString().trim();
                boolean b = JudgePhoneUtils.mPhone(mValues);
                if (b) {
                    getValidateCode();
                    // 请求网络
                    netWork();
                } else {
                    Toast.makeText(context, "请你输入正确的电话号码", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case R.id.bu_register:// 注册
                String mCode = et_code.getText().toString().trim();
                name = et_licheng.getText().toString().trim();
                mPass = et_pass.getText().toString().trim();
                mPass2 = et_pass2.getText().toString().trim();
                if (TextUtils.isEmpty(mCode)) {
                    Toast.makeText(context, "验证码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (!mCode.equals(code)) { //!
                        Toast.makeText(context, "验证码不正确", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        if (TextUtils.isEmpty(mPass)) {
                            Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            if (!mPass.equals(mPass2)) {
                                Toast.makeText(context, "密码输入不一致", Toast.LENGTH_SHORT)
                                        .show();
                            }
                            // 上传
                            netRegister();
                        }
                    }
                }
                break;
        }
    }

    private void netRegister() {
        NetHead head = new NetHead(context);
        RequestParams params = head.setHeader();
        params.addBodyParameter("type", "registerAndModifyPwd");
        params.addBodyParameter("phone", mValues);
        params.addBodyParameter("itype", "register");
        params.addBodyParameter("pwd", mPass);
        params.addBodyParameter("userName",name);
        if (http == null) {
            http = new HttpUtils();
        }
        http.send(HttpMethod.POST, UrlsUtils.urlLoadingDeng, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Toast.makeText(getApplicationContext(), "解析失败", Toast.LENGTH_LONG)
                                .show();
                        //Log.e("TAG", "解析失败");
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
//                        Toast.makeText(getApplicationContext(), "解析成功", Toast.LENGTH_LONG)
//                                .show();
                        Log.e("TAG", "arg0.result====" + arg0.result);
                        mResult = arg0.result;
                        parseJsonRegister();
                    }
                });
    }

    protected void parseJsonRegister() {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            status = jsonObject.getString("status");
            if (status.equals("N")) {
                Toast.makeText(context, "此号码已经被注册", Toast.LENGTH_SHORT).show();
            } else {
                //转到已登录成功界面,并记录登陆的状态
                JSONObject object = jsonObject.getJSONObject("data");
                mPhone = object.getString("phone");
                userID = object.getString("sid");
                name = object.getString("name");
                save();
                //Intent intent = this.getIntent();
                Intent intent = new Intent();
                intent.putExtra("success", "1");
                this.setResult(200, intent);
                finish();
//                Intent intent1 = new Intent(RegisterActivity.this,LoagingActivity.class);
//                startActivity(intent1);
            }
        } catch (Exception e) {

        }
    }

    /**
     * 访问网路
     */
    private HttpUtils http;
    private String mResult = "";
    private String status = "";
    private String code = "";
    private String mValues;
    private String mPass;
    private String mPass2;
    private String mPhone;
    private String userID;
    private String name;

    private void netWork() {
        NetHead head = new NetHead(context);
        RequestParams params = head.setHeader();

        params.addBodyParameter("type", "getVerifiableCode");
        params.addBodyParameter("phone", mValues);
        params.addBodyParameter("itype", "register");

        if (http == null) {
            http = new HttpUtils();
        }

        http.send(HttpMethod.POST, UrlsUtils.urlLoadingDeng, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        Toast.makeText(context, "网络连接超时", Toast.LENGTH_SHORT).show();
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

    protected void parseJson() {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            status = jsonObject.getString("status");
            if (status.equals("N")) {
                Toast.makeText(context, "此号码已经被注册", Toast.LENGTH_SHORT).show();
                return;
            }
            // 获取验证码
            JSONObject object = jsonObject.getJSONObject("data");
            code = object.getString("verifiableCode");
        } catch (Exception e) {
            Toast.makeText(context, "数据不正确", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 获取验证码
     */
    private void getValidateCode() {
        if (context != null && !NetworkUtils.isNetworkConnected(context)) {
            ToastUtils.show(context, "请检查网络");
            return;
        }
        mTextView.setText("正在发送验证码");
        mTextView.setClickable(false);

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
                            mTextView.setClickable(true);
                            mTextView.setText("获取验证码");
                            mCurrent = 60;
                        } else {
                            mTextView.setText(--mCurrent + "后再尝试");
                            mTextView.setClickable(false);
                        }
                    }
                });
            }

            ;
        }.start();
    }
}
