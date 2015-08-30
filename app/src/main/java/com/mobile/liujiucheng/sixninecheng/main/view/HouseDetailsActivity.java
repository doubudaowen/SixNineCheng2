package com.mobile.liujiucheng.sixninecheng.main.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mobile.liujiucheng.sixninecheng.main.R;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

public class HouseDetailsActivity extends Activity implements View.OnClickListener{

    private Context context;
    private LinearLayout fengxiang;
    private ImageView iv_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_details);

        initView();
    }

    private void initView(){
        context = this;

        fengxiang = ((LinearLayout) findViewById(R.id.fengxiang));
        iv_back = ((ImageView) findViewById(R.id.iv_back));

        fengxiang.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.fengxiang:
                doShare();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void doShare() {
        final UMSocialService mController = UMServiceFactory
                .getUMSocialService("com.umeng.share");
        mController.getConfig().closeToast();

        // 分享的是百度图片
        final UMImage umImage = new UMImage(context,
                "https://www.baidu.com/img/bdlogo.png");

        // 添加微信、微信朋友圈平台
        addWXPlatform(context);
        WeiXinShareContent weixinContent = new WeiXinShareContent();
        weixinContent.setTitle("");
        //分享内容
        weixinContent.setShareContent("六九城");
        weixinContent.setShareImage(umImage);
        //点击分享内容跳转到的URL
        weixinContent.setTargetUrl("https://www.baidu.com/img/bdlogo.png");
        mController.setShareMedia(weixinContent);

        // 设置朋友圈分享的内容
        CircleShareContent circleMedia = new CircleShareContent();
        circleMedia.setTitle("六九城");
        circleMedia.setShareContent(" ");
        circleMedia.setShareImage(umImage);
        circleMedia.setTargetUrl("https://www.baidu.com/img/bdlogo.png");
        mController.setShareMedia(circleMedia);

        //友盟分享时，需要将申请的新浪的 APPID APPSECRET配置在友盟后台,微信平台不需要
        // 添加新浪SSO授权
        mController.getConfig().setSsoHandler(new SinaSsoHandler(context));
        // 新浪分享内容
        SinaShareContent sinaContent = new SinaShareContent();
        sinaContent.setShareContent("六九城");
        sinaContent.setShareImage(umImage);
        sinaContent.setTargetUrl("https://www.baidu.com/img/bdlogo.png");
        mController.setShareMedia(sinaContent);

        /**分享的监听事件*/
        mController.registerListener(new SocializeListeners.SnsPostListener() {

            @Override
            public void onStart() {
                Toast.makeText(context, "分享开始", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onComplete(SHARE_MEDIA platform, int eCode, SocializeEntity entity) {
                Toast.makeText(context, "分享结果的状态码 : " + eCode, Toast.LENGTH_SHORT).show();
            }
        });

        //分享
        mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,SHARE_MEDIA.SINA);
        mController.openShare((Activity) context, false);
    }

    protected static void addWXPlatform(Context context) {
        String appId = "wx67e1eaa61b4e084e";
        String appSecret = "89b5f78777d3cfc48e131a2736c1114b";
        // 添加微信平台
        UMWXHandler wxHandler = new UMWXHandler(context, appId, appSecret);
        // wxHandler.showCompressToast(false);
        wxHandler.addToSocialSDK();

        // 支持微信朋友圈
        UMWXHandler wxCircleHandler = new UMWXHandler(context, appId,
                appSecret);

        wxCircleHandler.setToCircle(true);
        wxCircleHandler.addToSocialSDK();
    }
}
