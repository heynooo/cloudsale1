package com.vanhitech.vanhitech.fragment;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseFragment;
import com.vanhitech.vanhitech.conf.Constants;
import com.vanhitech.vanhitech.controller.BaseController;
import com.vanhitech.vanhitech.controller.HomeController;
import com.vanhitech.vanhitech.controller.ScenerController;
import com.vanhitech.vanhitech.controller.settingController;
import com.vanhitech.vanhitech.views.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     heyn
 * 创建时间   2016/3/14 19:39
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */


public class ContentFragment extends BaseFragment {
    @ViewInject(R.id.content_rb_home)
    RadioButton mRbHome;


    @ViewInject(R.id.content_rb_setting)
    RadioButton mRbSetting;


    @ViewInject(R.id.content_rg)
    RadioGroup mRg;

    @ViewInject(R.id.content_viewpager)
    NoScrollViewPager mViewPager;


    private static int mCurTabIndex; //当前的索引
    private List<BaseController> mControllers;

    public ContentFragment(ServerCommand cmd) {
        super();
        new HomeController(mContext,mActivity,cmd);
    }

    @Override
    public View initView() {
        View view = View.inflate(mContext, R.layout.fragment_content, null);
        ViewUtils.inject(this, view);
        return view;
    }

    public ContentFragment(){
       super();
    }


    @Override
    public void initData() {
        //数据集
        mControllers = new ArrayList<BaseController>();
        mControllers.add(new HomeController(mContext,mActivity));
        mControllers.add(new ScenerController(mContext,mActivity));
        mControllers.add(new settingController(mContext,mActivity));
        //-----------------------test-----




        //-----------------------test-----




        mViewPager.setAdapter(new ContentPagerAdapter());
        //设置RadioGroup默认选中首页
        mRg.check(Constants.isRid);//选中首页button-->首页button被选中

        super.initData();
    }

    @Override
    public void initListener() {
        mRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {//代表当前选中的id
                switch (checkedId) {
                    case R.id.content_rb_home://选中Home
                        mCurTabIndex = 0;
                        break;
                    case R.id.content_rb_scener://选中场景
                        mCurTabIndex = 1;
                        break;
                    case R.id.content_rb_setting://选中设置
                        mCurTabIndex = 2;
                        break;
                    default:
                        break;


                }
                //进行ViewPager的切换
                mViewPager.setCurrentItem(mCurTabIndex);
            }
        });
        super.initListener();

    }

    class ContentPagerAdapter extends PagerAdapter {


        @Override
        public int getCount() {
            if (mControllers != null) {
                return mControllers.size();
            }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //模拟展示
            //data
            //view
            //从集合得到
            BaseController baseController = mControllers.get(position);

            View rootView = baseController.mRootView;//视图和数据绑定
            //data+view 加入容器
            container.addView(rootView);

            //加载对应的数据,进行ui绑定
            baseController.initData();
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
