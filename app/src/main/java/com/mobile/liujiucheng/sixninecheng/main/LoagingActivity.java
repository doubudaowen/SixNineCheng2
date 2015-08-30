package com.mobile.liujiucheng.sixninecheng.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mobile.liujiucheng.main.utils.DialogUtils;
import com.mobile.liujiucheng.main.utils.JudgePhoneUtils;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.sixninecheng.main.view.ForgetPasswordActivity;

/**
 * 登录界面
 * @author pc
 */

public class LoagingActivity extends BaseActivity implements OnClickListener {

    private TextView mTextView;
    private TextView et_num;// 手机号码
    private TextView et_pass;// 密码
    private Button bu_loading;
    private Context context;
    private SharedPreferences sp;
    private ImageView mImageView;
    private TextView tv_forget;

    private ImageView iv_remember;// 记住密码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_loading);
        initView();
    }

    private void initView() {
        context = this;
        if (sp == null) {
            sp = this.getSharedPreferences("load", MODE_PRIVATE);
        }
        iv_remember = (ImageView) findViewById(R.id.iv_remember);
        mImageView = (ImageView) findViewById(R.id.iv_exit);
        mImageView.setOnClickListener(this);
        bu_loading = (Button) findViewById(R.id.bu_loading);
        mTextView = (TextView) findViewById(R.id.tv_register);
        et_pass = (TextView) findViewById(R.id.et_pass);
        et_num = (TextView) findViewById(R.id.et_num);
        tv_forget = ((TextView) findViewById(R.id.tv_forget));
        mTextView.setOnClickListener(this);
        bu_loading.setOnClickListener(this);
        iv_remember.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            String extra = data.getStringExtra("success");
            if (extra.equals("1")) {
                finish();
            }
        }
    }

    private boolean b = true;
    private String tag = "记住密码";

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_register:
                Intent intent = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivityForResult(intent, 100);
                break;
            case R.id.iv_exit:
                finish();
                break;

            case R.id.tv_forget:    //忘记密码
                Intent intent2 = new Intent(getApplicationContext(),
                        ForgetPasswordActivity.class);
                startActivity(intent2);
                break;
            case R.id.iv_remember:// 记住密码
                if (b) {
                    iv_remember.setImageResource(R.drawable.enter_recorded_box);
                    b = false;
                    tag = "记住密码";
                } else {
                    iv_remember.setImageResource(R.drawable.enter_check_box);
                    b = true;
                    tag = "取消记住";
                }
                break;
            case R.id.bu_loading:
                String num = et_num.getText().toString().trim();
                String pass = et_pass.getText().toString().trim();
                if (TextUtils.isEmpty(num)) {
                    Toast.makeText(context, "号码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                boolean b = JudgePhoneUtils.mPhone(num);
                if (b) {
                    if (TextUtils.isEmpty(pass)) {
                        Toast.makeText(context, "密码不能为空", Toast.LENGTH_SHORT)
                                .show();
                    } else {
                        dialogUtils = new DialogUtils(context);
                        dialogUtils.show();
                        network(num, pass);
                    }
                } else {
                    Toast.makeText(context, "请输入正确的号码格式", Toast.LENGTH_SHORT)
                            .show();
                }
        }
    }

    private HttpUtils http;
    private String mResult = "";
    private String status = "";
    private String mPhone = "";
    private String userID;
    private DialogUtils dialogUtils;

    private void network(String mValues, String mPass) {
        NetHead head = new NetHead(context);
        RequestParams params = head.setHeader();
        params.addBodyParameter("type", "userLogin");
        params.addBodyParameter("phone", mValues);
        params.addBodyParameter("pwd", mPass);
        if (http == null) {
            http = new HttpUtils();
        }
        http.send(HttpMethod.POST, UrlsUtils.urlLoadingDeng, params,
                new RequestCallBack<String>() {
                    @Override
                    public void onFailure(HttpException arg0, String arg1) {
                        // PopupWindowUtils.closePop();
                        dialogUtils.closeDialog();
                        Toast.makeText(context, "网络连接超时", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(ResponseInfo<String> arg0) {
                        dialogUtils.closeDialog();
                        mResult = arg0.result;
                        Log.e("TAG", "mResult==" + mResult);
                        parseJson();
                    }
                });
    }

    private String mImageUrl = null;
    private String name;

    protected void parseJson() {
        try {
            JSONObject jsonObject = new JSONObject(mResult);
            status = jsonObject.getString("status");
            if (status.equals("N")) {
                Toast.makeText(context, "用户名和密码不匹配", Toast.LENGTH_SHORT).show();
            } else {
                JSONObject object = jsonObject.getJSONObject("data");
                mPhone = object.getString("phone");
                userID = object.getString("sid");
                mImageUrl = object.getString("imgUrl");
                name = object.getString("name");
                Log.e("TAG", "mImageUrl======" + mImageUrl);
                getPhotoNetwork();
            }
        } catch (Exception e) {
            Toast.makeText(context, "数据不正确", Toast.LENGTH_SHORT).show();
        }
    }

    private static final String NETWORKEXCEPTION = "网络异常";

    /**
     * 获取网络图片
     */
    private void getPhotoNetwork() {
        save();
        if (TextUtils.isEmpty(mImageUrl)) {
            finish();
            return;
        }
        new Thread() {
            public void run() {
                try {
                    URL url = new URL(mImageUrl);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setConnectTimeout(3000); // 请求超时时间
                    conn.setReadTimeout(3000); // 读取超时时间
                    conn.setRequestMethod("GET");
                    int code = conn.getResponseCode();
                    if (code == 200) {
                        // 连接服务器成功
                        InputStream inputStream = conn.getInputStream();
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        File dir = context.getFilesDir();
                        File file = new File(dir, "mImage.jpeg");
                        compressBmpToFile(bitmap, file);
                        if (edit == null) {
                            edit = sp.edit();
                        }
                        edit.putString("imageUrl", mImageUrl);
                        edit.commit();
                    }
                    finish();
                } catch (Exception e) {
                    Toast.makeText(context, NETWORKEXCEPTION, Toast.LENGTH_SHORT).show();
                }
            }

            ;
        }.start();
    }

    /**
     * 将图片保存在文件中
     *
     * @param bmp
     * @param file
     */
    public void compressBmpToFile(Bitmap bmp, File file) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;// 个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 100) {
            baos.reset();
            options -= 10;
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final String mName = "六九城";
    private Editor edit;

    /**
     * 记录登陆的状态
     */
    private void save() {
        edit = sp.edit();
        edit.putBoolean("loadingStatu", true);
        edit.putString("phone", mPhone);
        edit.putString("userID", userID);
        edit.putString("imageUrl", mImageUrl);
        if (TextUtils.isEmpty(name)) {
            name = mName;
        }
        edit.putString("name", name);
        if (tag.equals("记住密码")) {
            edit.putString("keepPassword", tag);
        }
        edit.commit();
    }

    @Override
    public void finish() {
        super.finish();
    }
}
