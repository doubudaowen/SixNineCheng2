package com.mobile.liujiucheng.main.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.mobile.liujiucheng.sixninecheng.main.R;
import com.mobile.liujiucheng.sixninecheng.main.view.PandaItem;

/**
 * Created by DOUjunjun on 2015/7/18.
 */
@SuppressLint("ValidFragment")
public class VpFragment extends Fragment {
    private int position;
    private Context context;
    private BitmapUtils bitmapUtils;

    public VpFragment(Context context,int position) {
        this.context = context;
        this.position = position;
    }

//    public static VpFragment getFragment(int position){
//        VpFragment vpFragment = new VpFragment();
//        Bundle bundle = new Bundle();
//        bundle.putInt(KEY,position);
//        vpFragment.setArguments(bundle);
//        return vpFragment;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        BitmapUtils bitmapUtils = new BitmapUtils(context);

        View view = inflater.inflate(R.layout.viewpager_item,container,false);
        ImageView iv = ((ImageView) view.findViewById(R.id.ViewPager_img));
        bitmapUtils.display(iv, PandaItem.listImgUrl.get(position));
//        bitmapUtils.display(iv, "https://www.baidu.com/img/baidu_jgylogo3.gif?v=37140902.gif"); // 测试用

        return view;
    }
}
