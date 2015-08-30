package com.mobile.liujiucheng.main.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.mobile.liujiucheng.main.bean.ShufflingBean;
import com.mobile.liujiucheng.main.bean.ShufflingBean.DData;
import com.mobile.liujiucheng.main.utils.GsonUtil;
import com.mobile.liujiucheng.main.utils.NetHead;
import com.mobile.liujiucheng.main.utils.StreamTools;
import com.mobile.liujiucheng.main.utils.UrlsUtils;
import com.mobile.liujiucheng.sixninecheng.main.CheckingActivity;
import com.mobile.liujiucheng.sixninecheng.main.CooperationActivity;
import com.mobile.liujiucheng.sixninecheng.main.EntrustHouseActivity;
import com.mobile.liujiucheng.sixninecheng.main.LoagingActivity;
import com.mobile.liujiucheng.sixninecheng.main.MapActivity;
import com.mobile.liujiucheng.sixninecheng.main.R;
import com.mobile.liujiucheng.sixninecheng.main.ReleaseHouseActivity;
import com.mobile.liujiucheng.sixninecheng.main.RentActivity;
import com.mobile.liujiucheng.sixninecheng.main.RetalActivity;
import com.mobile.liujiucheng.sixninecheng.main.SPUtils;
import com.mobile.liujiucheng.sixninecheng.main.SearchActivity;
import com.mobile.liujiucheng.sixninecheng.main.view.AcquireRentActivity;
import com.mobile.liujiucheng.sixninecheng.main.view.HouseDetailsActivity;
import com.mobile.liujiucheng.sixninecheng.main.view.NineSixActivity;
import com.mobile.liujiucheng.sixninecheng.main.view.PandaRentActivity;

public class HomeFragment extends Fragment implements OnClickListener {

    private View view;

    private LinearLayout ziying;
    private LinearLayout mLayoutRent;
    private LinearLayout mLayoutSecond;
    private LinearLayout mLayoutMap;

    private LinearLayout tv_houseing;

    private LinearLayout ll_entrust;//发布房源

    private LinearLayout mLayoutChecking;//看房行程
    private LinearLayout mLayoutHousekeeping;//家政

//	private LinearLayout mLayoutAgent;//经纪人

    private EditText mEditText;

    private ViewPager mViewPager;

    private BitmapUtils bitmapUtils;

    private List<String> mUrls;

    private Myadapter mAdapter;

    private int middle = 0;//当前位置

    private Timer timer;

    private ImageView mImageView;
    private Context context;

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            mViewPager.setCurrentItem(msg.what);
            //Log.e("TAG", "mViewPager.getCurrentItem()==="+  mViewPager.getCurrentItem());
            return true;
        }
    });

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_two, container, false);
        return view;
    }

    @Override
    public void onPause() {
        super.onPause();
        pauseTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdapter != null && mUrls != null) {
            timerOfHeader();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    /**
     * 填充UI
     */

    private void initData() {
        context = getActivity();
        ziying = ((LinearLayout) view.findViewById(R.id.ziying));
        mImage = (ImageView) view.findViewById(R.id.iv_one);
        mImage01 = (ImageView) view.findViewById(R.id.iv_two);
        mImage02 = (ImageView) view.findViewById(R.id.iv_three);

        tv_houseing = (LinearLayout) view.findViewById(R.id.tv_houseing);
        ll_entrust = (LinearLayout) view.findViewById(R.id.ll_entrust);

        mUrls = new ArrayList<String>();
        bitmapUtils = new BitmapUtils(getActivity());
        mViewPager = (ViewPager) view.findViewById(R.id.main_view_pager);
        mViewPager.setCurrentItem(0);
        fillData();
        mImageView = (ImageView) view.findViewById(R.id.iv_sliding);

        mLayoutRent = (LinearLayout) view.findViewById(R.id.ll_rent);
        mLayoutSecond = (LinearLayout) view.findViewById(R.id.ll_second);   //熊猫合租
        mLayoutMap = (LinearLayout) view.findViewById(R.id.ll_map);

        mLayoutChecking = (LinearLayout) view.findViewById(R.id.ll_checking);
//		mLayoutAgent = (LinearLayout) view.findViewById(R.id.ll_agent);
//		mLayoutHousekeeping = (LinearLayout) view.findViewById(R.id.ll_housekeeping);

        mEditText = (EditText) view.findViewById(R.id.et_search);
        //
        mEditText.setOnClickListener(this);

        mLayoutRent.setOnClickListener(this);
        mLayoutSecond.setOnClickListener(this);
        mLayoutMap.setOnClickListener(this);
        mImageView.setOnClickListener(this);
        ziying.setOnClickListener(this);

        mLayoutChecking.setOnClickListener(this);
//		mLayoutAgent.setOnClickListener(this);
//		mLayoutHousekeeping.setOnClickListener(this);

        tv_houseing.setOnClickListener(this);
        ll_entrust.setOnClickListener(this);

    }

    /**
     * 填充
     */
    private void fillData() {
        //mUrls.add("http://www.69cheng.com/reception/images/indexbj0331.jpg");
        //mUrls.add("http://www.69cheng.com/reception/images/indexbj0304.jpg");
        //mUrls.add("http://www.69cheng.com/reception/images/indexbj02.png");
        netWork();
        mViewPager.setOnTouchListener(new OnTouchListener() {
            private int downX;
            private long downTime;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        pauseTimer();
                        downX = (int) event.getX();
                        downTime = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        int upX = (int) event.getX();
                        if (upX == downX && System.currentTimeMillis() - downTime < 500) {
                            // 响应点击事件，回调(先定义一个接口，暴露一个未实现方法，谁用谁实现，实现完了后在必要的地方调用)
                        }
                        timerOfHeader();
                        break;
                }
                return false;
            }
        });

        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                middle = position;
                //全部设置成默认值
                switch (position) {
                    case 0:
                        setDefaultColor(position);
                        break;
                    case 1:
                        setDefaultColor(position);
                        break;
                    case 2:
                        setDefaultColor(position);
                        break;
                }
            }

            /**
             * 设置成默认的颜色
             * @param
             */
            private void setDefaultColor(int position) {
                mImage.setImageResource(R.drawable.home_carousel_show);
                mImage01.setImageResource(R.drawable.home_carousel_show);
                mImage02.setImageResource(R.drawable.home_carousel_show);
                switch (position) {
                    case 0:
                        mImage.setImageResource(R.drawable.home_carousel_not_show);
                        break;
                    case 1:
                        mImage01.setImageResource(R.drawable.home_carousel_not_show);
                        break;
                    case 2:
                        mImage02.setImageResource(R.drawable.home_carousel_not_show);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private HttpUtils http;
    private ShufflingBean bean;
    private List<DData> mList;

    private void netWork() {
        NetHead head = new NetHead(context);
        RequestParams params = head.setHeader();
        params.addBodyParameter("type", "getCarouselImages");
        if (http == null) {
            http = new HttpUtils();
        }
        http.send(HttpMethod.POST, UrlsUtils.urlLoadingH, params, new RequestCallBack<String>() {
            @Override
            public void onFailure(HttpException arg0, String arg1) {
                Toast.makeText(context, StreamTools.getString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                bean = GsonUtil.json2Bean(arg0.result, ShufflingBean.class);
                String status = bean.status;
                if ("N".equals(status)) {
                    Toast.makeText(context, StreamTools.getString(), Toast.LENGTH_SHORT).show();
                }
                mList = bean.data;
                Log.e("TAG", "mList ==" + arg0.result.toString());
                for (int x = 0; x < mList.size(); x++) {
                    mUrls.add(bean.data.get(x).imgUrl);
                }
                setAdapter();
            }
        });
    }

    private void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new Myadapter();
            mViewPager.setAdapter(mAdapter);
            timerOfHeader();
        } else {
            mAdapter.notifyDataSetChanged();
        }
        //Log.e("TAG", "mViewPager.getCurrentItem()==="+  mViewPager.getCurrentItem());
    }

    protected void timerOfHeader() {
        if (timer == null) {
            timer = new Timer();
        }
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (mViewPager) {
                    middle = (middle + 1) % mUrls.size();
                    Message msg = new Message();
                    msg.what = middle;
                    mHandler.sendMessage(msg);
                }
            }
        }, 3000, 3000);
    }

    protected void pauseTimer() {
        if (timer == null) {
            return;
        }
        timer.cancel(); // 退出计时器
        timer.purge();
        timer = null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ll_rent:     //二手房源
//			startNewActivity(RetalActivity.class);
                startNewActivity(RentActivity.class);
                break;
            case R.id.ll_second:   //熊猫合租
//			startNewActivity(RentActivity.class);
                startNewActivity(PandaRentActivity.class);
                break;
            case R.id.ll_map:       //个人出租
//			startNewActivity(PandaRentActivity.class);
                startNewActivity(RetalActivity.class);
                break;
            case R.id.et_search:    //合作申请
                startNewActivity(SearchActivity.class);
                break;
            //看房行程
            case R.id.ll_checking:
//			if(TextUtils.isEmpty(SPUtils.getUserID(context))){
//				startNewActivity(LoagingActivity.class);
//			}else{
                startNewActivity(CooperationActivity.class);
//			}
                break;
            //经纪人
//		case R.id.ll_agent:
//			AlertDialog.Builder builder = new Builder(getActivity());
//			builder.setTitle("六九城");
//			builder.setIcon(R.drawable.ic_launcher);
//			builder.setMessage("直接拨打经纪人电话快速租房或买房");
//			builder.setPositiveButton("拨打", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					phoneCall();
//				}
//			});
//			builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//				}
//			});
//			builder.show();
//			//startNewActivity(SearchActivity.class);
//			break;
//			//家政
//		case R.id.ll_housekeeping:
//			//startNewActivity(SearchActivity.class);
//			Toast.makeText(context, "暂时没有开通", Toast.LENGTH_SHORT).show();
//			break;
            case R.id.iv_sliding:
                //startNewActivity(SearchActivity.class);
                //退出侧拉栏
                closeSliding.close();
                break;
            case R.id.tv_houseing://发布房源
                startNewActivity(ReleaseHouseActivity.class);
                break;
            case R.id.ll_entrust://委托
//			startNewActivity(EntrustHouseActivity.class);
                startNewActivity(AcquireRentActivity.class);
                break;
            case R.id.ziying:
                startNewActivity(HouseDetailsActivity.class);
                break;
        }
    }

    private void phoneCall() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "01057490090"));
        getActivity().startActivity(intent);
    }

    /**
     * 开启新的界面
     */
    public void startNewActivity(Class<?> clazz) {
        Intent intent = new Intent(getActivity(), clazz);
        startActivity(intent);
    }

    /**
     * 回调方法
     */
    private CloseSliding closeSliding;

    private ImageView mImage;

    private ImageView mImage01;

    private ImageView mImage02;

    public interface CloseSliding {
        public void close();
    }

    public void setCloseSliding(CloseSliding closeSliding) {
        this.closeSliding = closeSliding;
    }

    private class Myadapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mUrls.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View mView = View.inflate(getActivity(), R.layout.layout_imageview_item, null);
            ImageView mImageView = (ImageView) mView.findViewById(R.id.iv_lunbo_image);
            bitmapUtils.display(mImageView, mUrls.get(position));
            mImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NineSixActivity.class);
                    intent.putExtra("position", position);
                    intent.putExtra("img", mList.get(position).linkUrl);
                    startActivity(intent);
                }
            });
            container.addView(mView);
            return mView;
        }
    }
}
