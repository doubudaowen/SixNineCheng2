package com.mobile.liujiucheng.main.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mobile.liujiucheng.main.adapter.MyAdapter;
import com.mobile.liujiucheng.main.utils.DialogUtils;
import com.mobile.liujiucheng.main.utils.FileUtils;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.sixninecheng.main.LoagingActivity;
import com.mobile.liujiucheng.sixninecheng.main.MainActivity;
import com.mobile.liujiucheng.sixninecheng.main.MainActivity.PersonalValues;
import com.mobile.liujiucheng.sixninecheng.main.MyCollectionActivity;
import com.mobile.liujiucheng.sixninecheng.main.MyHouseActivity;
import com.mobile.liujiucheng.sixninecheng.main.MyWalletActivity;
import com.mobile.liujiucheng.sixninecheng.main.R;
import com.mobile.liujiucheng.sixninecheng.main.SPUtils;
import com.mobile.liujiucheng.sixninecheng.main.SettingActivity;
import com.mobile.liujiucheng.sixninecheng.main.activityliu.MyReleaseActivity;
import com.mobile.liujiucheng.sixninecheng.main.view.CircleImageView;
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

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author pc Mr_Gang
 */
public class PersonalFragment extends Fragment implements OnClickListener {

    private View view;
    private ListView mListView;
    private MyAdapter adapter;
    private Context context;

    private List<String> mlist;

    private TextView mTextView;
    private SharedPreferences sp;
    private MainActivity activity;

    //private BitmapUtils bitmapUtils;
    LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT);
    private int[] mRids;
    private TextView share_tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_personal, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        context = getActivity();
        initImage();
        initData();
        super.onActivityCreated(savedInstanceState);
    }

    private void initImage() {
        mRids = new int[] { R.drawable.my_my, R.drawable.my_collect,
                R.drawable.my_evaluate, R.drawable.my_entrust,
                R.drawable.my_housekeeping, R.drawable.my_wallet,
                R.drawable.my_set };
    }

    @SuppressWarnings("static-access")
    private void initData() {
        //bitmapUtils = new BitmapUtils(context);
        if (sp == null) {
            sp = getActivity().getSharedPreferences("load",
                    getActivity().MODE_PRIVATE);
        }
        activity = (MainActivity) getActivity();
        activity.setPersonalValues(new PersonalValues() {
            @Override
            public void mValues() {
                assignmentData();
            }
        });
        mTextView = (TextView) view.findViewById(R.id.tv_loading_register);
        roundImageView = (ImageView) view
                .findViewById(R.id.default_avatar1);
        assignmentData();
        mTextView.setOnClickListener(this);
        roundImageView.setOnClickListener(this);

        /**分享*/
        share_tv = ((TextView) view.findViewById(R.id.personal_tv));
        share_tv.setOnClickListener(this);

        mlist = new ArrayList<String>();
        fillData();
        mListView = (ListView) view.findViewById(R.id.my_person);
        adapter = new MyAdapter(mlist, context) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view;
                View mView;
                view = View.inflate(context, R.layout.layout_listview_personal,
                        null);
                mView = view.findViewById(R.id.view_line);
                TextView tv = (TextView) view.findViewById(R.id.tv_item);
                ImageView mImageView = (ImageView) view
                        .findViewById(R.id.iv_set_image);
                mImageView.setImageResource(mRids[position]);
                if (position == 3 || position == 4) {
                    tv.setText(mlist.get(position));
                    tv.setTextColor(getResources().getColor(R.color.my_color));
                } else {
                    tv.setText(mlist.get(position));
                }
                if (position == 5 || position == 2) {
                    mView.setVisibility(View.VISIBLE);
                } else {
                    mView.setVisibility(View.GONE);
                }
                return view;
            }
        };
        mListView.setAdapter(adapter);
        /**
         * 处理点击事件
         */
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                String string = getPhone();
                switch (position) {
                    case 0:// 我的发布房源
                        if (string == null) {
                            newStart(LoagingActivity.class);
                        } else {
                            newStart(MyReleaseActivity.class);
                        }
                        break;
                    case 1:// 我的收藏
                        if (string == null) {
                            newStart(LoagingActivity.class);
                        } else {
                            newStart(MyCollectionActivity.class);
                        }
                        break;
//				case 2:// 我的委托
//					/*
//					 * if(string == null){ newStart(LoagingActivity.class);
//					 * }else{ newStart(EntrustActivity.class); }
//					 */
//					break;
//				case 3:// 我的家政
//						// Toast.makeText(context, "即将开通",
//						// Toast.LENGTH_SHORT).show();
//					break;
//				case 4:// 评价管理
//						// Toast.makeText(context, "即将开通",
//						// Toast.LENGTH_SHORT).show();
//					break;
//				case 5:// 我的钱包
//					if (string == null) {
//						newStart(LoagingActivity.class);
//					} else {
//						newStart(MyWalletActivity.class);
//					}
//					break;
                    case 2:// 设置
                        if (string == null) {
                            newStart(LoagingActivity.class);
                        } else {
                            newStart(SettingActivity.class);
                        }
                        break;
                }
            }
        });
    }
    // 获取返回的数据
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // 用户没有进行有效的设置操作，返回
        switch (requestCode) {
            case CODE_GALLERY_REQUEST:// 本地
                cropRawPhoto(estimateNull(intent));
                break;
            case CODE_CAMERA_REQUEST:// 照相机
                if (hasSdcard()) {
                    File tempFile = new File(Environment.getExternalStorageDirectory(),IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getActivity(), "没有SDCard!", Toast.LENGTH_LONG).show();
                }
                break;
            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }
    private Bitmap photo = null;
    private void setImageToHeadView(Intent intent) {
//		Bundle extras = intent.getExtras();
//		if (extras != null) {
//			Bitmap photo = extras.getParcelable("data");
//			headImage.setImageBitmap(photo);
//		}

        // 如何将uri地址装换成文件的路径
        // intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);的使用
        photo = null;
        Uri photoUri = intent.getData();
        if (photoUri != null) {
            photo = BitmapFactory.decodeFile(photoUri.getPath());
        }
        if (photo == null) {
            Bundle extra = intent.getExtras();
            if (extra != null) {
                photo = (Bitmap)extra.get("data");
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            }
        }
        File dir = context.getFilesDir();
        File file = new File(dir, "mImage.jpeg");
        if(photo != null){
            compressBmpToFile(photo, file);
            uploadAndPicture();
        }
        //TODO将图像上传网络
    }
    public  void compressBmpToFile(Bitmap bmp,File file){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 80;//个人喜欢从80开始,
        bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);
        while (baos.toByteArray().length / 1024 > 200) {
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
    private HttpUtils http;
    /**
     * 上传头像
     * @param
     */
    private void uploadAndPicture() {
        final DialogUtils dialogUtils = new DialogUtils(context);
        dialogUtils.createDiaLog();
        dialogUtils.show();
        File dir = context.getFilesDir();
        File file = new File(dir, "mImage.jpeg");
        RequestParams params = new RequestParams();
        params.addBodyParameter("phone", sp.getString("phone", ""));
        params.addBodyParameter("files", file);
        if(http == null){
            http = new HttpUtils();
        }
        http.send(HttpMethod.POST,UrlsUtils.urlUploadImage, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                dialogUtils.closeDialog();
                Toast.makeText(context, "网络不给力", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                Log.e("AAAAAAAAAAAAAAA",arg0.result.toString());
                String str = arg0.result;
                try {
                    JSONObject object = new JSONObject(str);
                    JSONObject data = object.getJSONObject("data");
                    String imgUrl = data.getString("imgUrl");
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("imageUrl", imgUrl);
                    edit.commit();
                    String imageUrl = sp.getString("imageUrl", null);
//                    Toast.makeText(context, imageUrl, Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                dialogUtils.closeDialog();
                Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();
                roundImageView.setImageBitmap(photo);
            }
        });
    }
    /**
     * 检查设备是否存在SDCard的工具方法
     */
    private boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }
    /**
     * 裁剪原始的图片
     */
    private void cropRawPhoto(Uri uri) {
        if(uri == null){
            return;
        }
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);// 去黑边

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("return-data", true);
        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }
    private Uri estimateNull(Intent intent) {
        /**
         * 判断URL是不是为空
         */
        if(intent == null){
            return null;
        }
        Uri data = intent.getData();
        ContentResolver resolver = getActivity().getContentResolver();
        Bitmap photo1 = null;
        if (data == null) {
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                photo1 = (Bitmap) bundle.get("data"); // get bitmap
                data = Uri.parse(MediaStore.Images.Media.insertImage(
                        resolver, photo1, null, null));
            }
        } else {
            try {
                photo1 = MediaStore.Images.Media.getBitmap(resolver, data);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    private void assignmentData() {
        if (!TextUtils.isEmpty(sp.getString("phone", null))) {
            mTextView.setText(SPUtils.getName(context));
        } else {
            mTextView.setText("登录/注册");
        }
        //TODO
        String imageUrl = sp.getString("imageUrl", null);
        Log.e("TAG", "========="+imageUrl);
        if(!TextUtils.isEmpty(imageUrl)){
            Bitmap bitmap = BitmapFactory.decodeFile(FileUtils.getFile(context).getPath());
            if(bitmap != null) {
                roundImageView.setImageBitmap(bitmap);
            }else {
                new BitmapUtils(context).display(roundImageView,imageUrl);
            }
        }else{
            roundImageView.setImageResource(R.drawable.ic_launcher);
        }
    }
    /***
     * 填充数据
     */
    private void fillData() {
        mlist.add("我的房源");
        mlist.add("收藏房源");
//		mlist.add("我的委托");
//		mlist.add("我的家政");
//		mlist.add("评价管理");
//		mlist.add("我的钱包");
        mlist.add("设置");
    }
    /**
     * 开启一个新的界面
     * @param clazz
     */
    private void newStart(@SuppressWarnings("rawtypes") Class clazz) {
        Intent intent = new Intent(context, clazz);
        startActivity(intent);
    }

    private String[] mItem = { "相册", "相机" };

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {

            case R.id.iv_person_setting:

                break;
            case R.id.default_avatar1:// 弹出一个窗口
                //choseHeadImageFromCameraCapture();
                String string = getPhone();
                if(string ==  null){
                    newStart(LoagingActivity.class);
                }else{
                    AlertDialog.Builder builder = new Builder(context);
                    builder.setIcon(R.drawable.ic_launcher);
                    builder.setTitle("选择头像");
                    //AlertDialog dialog = builder.create();
                    builder.setItems(mItem,
                            new android.content.DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    switch (which) {
                                        case 0:
                                            choseHeadImageFromGallery();
                                            close(dialog);
                                            break;
                                        case 1:
                                            choseHeadImageFromCameraCapture();
                                            close(dialog);
                                            break;
                                    }
                                }
                            });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
                break;
            case R.id.tv_loading_register:// 登陆界面
                String str = sp.getString("phone", null);
                if (TextUtils.isEmpty(str)) {
                    newStart(LoagingActivity.class);
                }
                break;
        }
    }

    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";

    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;

    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 80;
    private static int output_Y = 80;

    private ImageView roundImageView;
    /**
     * 相机中获取图片
     */
    protected void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }
        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }
    /**
     * 本地获取图片
     */
    protected void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }
    private void close(DialogInterface dialog) {
        if(dialog != null){
            dialog.dismiss();
        }
    }
    private String getPhone() {
        String str = sp.getString("phone", null);
        return str;
    }
}
