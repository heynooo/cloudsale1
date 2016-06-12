package com.vanhitech.vanhitech.controller.home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.SwipeMenulistview.PullToRefreshSwipeMenuListView;
import com.vanhitech.vanhitech.SwipeMenulistview.SwipeMenu;
import com.vanhitech.vanhitech.SwipeMenulistview.SwipeMenuCreator;
import com.vanhitech.vanhitech.SwipeMenulistview.SwipeMenuItem;
import com.vanhitech.vanhitech.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     heyn
 * 创建时间   2016/3/15 20:57
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class HomeListController implements ViewPager.OnPageChangeListener {
    public View mRootView;
    public Context mContext;
    private List<Device> mDeviceList;
    private TextView mTextView;
    private String mPlace;
//    private final BitmapUtils mBitmapUtils;
//    @ViewInject(R.id.homelist_pager_viewpager)
    ViewPager mViewPager;
    @ViewInject(R.id.home_pager_title)
    TextView mTvTitle;
    @ViewInject(R.id.homelist_pager_listview)
    PullToRefreshSwipeMenuListView mListView;
    @ViewInject(R.id.left_room_info)
    Button mBtLeftInfo;

    private Animation mPush_left_in;
    private Animation mPush_right_in;
//test-----------------------------

    private  int moveleft=0;
    private  int moveright=0;

//test-----------------------------
    //homelist_pager_listview



    private float mDownX;
    private float mDownY;
    public static final String TAG = HomeListController.class.getSimpleName();

    public HomeListController(Context context, List<Device> device) {
        mDeviceList=new ArrayList<Device>();
        mContext = context;
        mDeviceList.addAll(device);//这里是一个相同场景的设备
        mRootView = initView(context);
//        mBitmapUtils = new BitmapUtils(mContext);
//        mViewPager.setOffscreenPageLimit(3);
//        mViewPager.setPageMargin(30);
        initData(context);
    }

    private void initData(Context context) {


//        mTextView.setText(mDevice.toString());
        mViewPager.setAdapter(new TopHomePagerAdater());
        mTvTitle.setText(mDeviceList.get(0).place);
//        mViewPager.setOnPageChangeListener(this);
        mListView.setAdapter(new HomeAdapter());//TODO

        mBtLeftInfo.setOnClickListener(new LeftClickListener());

        //-------------test-----------------------------
//        mViewPager.setOnTouchListener(new MyOngestureListener());
        GestureDetector gd= new GestureDetector(new MyOngestureListener());

        mViewPager.setOnTouchListener(new MyTouchListener());
//        mListView.setOnTouchListener(new MyTouchListener());
        //-------------test-----------------------------



//        Animation animation= AnimationUtils.loadLayoutAnimation(mContext, R.animator.slide_right);
//        Animation ani1 = AnimationUtils.loadAnimation(mContext, R.anim.list_anim_layout);
//        LayoutAnimationController lac=new LayoutAnimationController(ani1);
//        lac.setOrder(LayoutAnimationController.ORDER_REVERSE);
//        lac.setDelay(1);
//        mListView.setLayoutAnimation(lac);
//        mListView.setAdapter(new HomeAdapter());
//        2
        //----------TEST------------------------
        //#############################listview 滑动删除+下拉刷新#test1##########################################################
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(mContext);
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                // set item width
                openItem.setWidth(dp2px(9000));
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9, 0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(dp2px(90));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(creator);

        //----------TEST------------------------

        //----------------------------------------------
    }

    class HomeAdapter extends BaseAdapter {

        /**
         * getCount 里面存放 比如说 客厅有多少设备 这里就返回多少个
         *      这里的device 是一个
         * @return
         */
        @Override
        public int getCount() {
            if (mDeviceList != null) {
                int i= mDeviceList.size();
                return i;//TODO
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mDeviceList != null) {
                return mDeviceList.get(position);//TODO
            }
            return null;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;  //根视图
            if(convertView==null){

                convertView=View.inflate(mContext,R.layout.item_homelist,null);
                holder = new ViewHolder();
                holder.mIvicon = (ImageView) convertView.findViewById(R.id.item_homelist_icon);
                holder.mTvName = (TextView) convertView.findViewById(R.id.item_homelist_name);
                holder.mTvStatus = (TextView) convertView.findViewById(R.id.item_homelist_status);


                convertView.setTag(holder);
            }else{
                holder  = (ViewHolder) convertView.getTag();
            }
            //------test----------------
            //------test----------------
            //data
//            Device devicebean = mDeviceList.get(position);


            //数据绑定
            holder.mTvName.setText(mDeviceList.get(position).name);
            holder.mTvStatus.setText("暂时不设置");
//            mBitmapUtils.configDefaultLoadingImage(R.mipmap.devicedefault);
//            mBitmapUtils.display(holder.mIvicon,null);//TODO 暂不知道去哪获取图片 listview图片加载地方
//            convertView.setAnimation(mPush_right_in); //动画 没效果
            return convertView;
        }
        class ViewHolder{
            TextView mTvName;
            TextView mTvStatus;
            ImageView mIvicon;
        }
    }


    private View initView(Context context) {
//        mTextView = new TextView(context);
//        mTextView.setGravity(Gravity.CENTER);
//        mTextView.setTextColor(Color.BLUE);

        View view = View.inflate(mContext, R.layout.homelist_pager, null);
        ViewUtils.inject(this, view);
        //----------------------

        //---------------------

        return view;


    }

    //place 客厅          place:未指定   place:客厅   按设备分
    class TopHomePagerAdater extends PagerAdapter {

        @Override
        public int getCount() {
            if (mDeviceList != null)//TODO
                return 0;
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = new ImageView(mContext);
            iv.setScaleType(ImageView.ScaleType.FIT_XY); //图片的拉伸模式
            iv.setBackgroundResource(R.drawable.defaultroot);
            //图片加载
            String url = "R.drwable.defaultroot.png";
//
//            mBitmapUtils.configDefaultLoadFailedImage(R.mipmap.roottemp);
//            mBitmapUtils.display(iv, url);

            //加入容器
            container.addView(iv);

            return iv;
        }
        //销毁
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //当前选中的position
        LogUtils.i("---onPageScrolled滑动------"+positionOffsetPixels+"--"+positionOffset+"--"+position);


    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
//手势识别

    private class MyOngestureListener implements GestureDetector.OnGestureListener{
        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            LogUtils.i("---滑动事件－－－",e1.toString()+"--e2:"+e2.toString());
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {

        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }
    }
///---------------------

//    setOnTouchListener

    /**
     *(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

    }
    });
     mBtLeftInfo
     * 左边房间按键点击事件
     */
    class LeftClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //TODO 跳转房间信息 activity


        }
    }

    class MyTouchListener implements View.OnTouchListener {

        private float mViewPagerDownX;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.i("HomeListController", "MotionEvent.ACTION_DOWN");
//                    test();
                    //按下
                    mDownX = event.getRawX();
                    mDownY = event.getRawY();
                    //按下
                    mViewPagerDownX = mViewPager.getScaleX();

                    LogUtils.i(mViewPager.getScrollX()+"按下mviewpager的x位置getScrollX");
                    LogUtils.i(mViewPager.getScaleX()+"按下mviewpager的x位置getScaleX");

                    break;
                case MotionEvent.ACTION_MOVE:
                    Log.i("HomeListController", "MotionEvent.ACTION_MOVE");
                    //移动 希望的效果是在move中去触发 listview
                    LogUtils.i(mViewPager.getX()+"移动mviewpager的x位置");
                    LogUtils.i(mViewPager.getScrollX()+"移动mviewpager的x位置getScrollX");
                    LogUtils.i(mViewPager.getScaleX()+"移动mviewpager的x位置getScaleX");
                    LogUtils.i(mListView.getX()+"mListView的位置");


                    //getScrollX
                    int diffXPager = (int) (mViewPagerDownX-mViewPager.getScrollX() + .5f);

                    //test------------------------------
                    float moveX = event.getRawX();
                    float moveY = event.getRawY();
                    int diffX = (int) (moveX - mDownX + .5f);
                    int diffY = (int) (moveY - mDownY + .5f);

                    if (diffX > 0) {//往右拖动
                        Log.i(TAG, "往右拖动-->");
                        Itemleft(-diffXPager);
                    } else {
                        Log.i(TAG, "往左拖动<--");
                        Itemright(diffXPager);//右边往左进  再触发 , 只要移动的数值和刚才不一样 ,再次触发
                    }

                    //test------------------------------


                    break;
                case MotionEvent.ACTION_UP:
                    Log.i("HomeListController", "MotionEvent.ACTION_UP");
                    //弹起
                    //松开后,移动开和viewpager 一样的位置 直接坐标赋值

                    break;
                case MotionEvent.ACTION_CANCEL:
                    //取消
                    Log.i("HomeListController", "MotionEvent.ACTION_CANCEL");
                    /*
                    mAutoScrollTask.start();

					*/
                    break;

                default:
                    break;
            }
            return true;

        }

    }


    public void Itemright(final int right) {
        //------------

        mListView.setAdapter(new HomeAdapter());
//        Animation animation = (Animation) AnimationUtils.loadAnimation(mContext, R.anim.push_right_in);
//        TranslateAnimation anim = new TranslateAnimation( 0, 50, 0, 0)
        TranslateAnimation animation = new TranslateAnimation(
//                //X轴初始位置
//                Animation.RELATIVE_TO_SELF,r1,
//                //X轴移动的结束位置
//                Animation.RELATIVE_TO_SELF, 0.0f,
//                //y轴开始位置
//                Animation.RELATIVE_TO_SELF,0.0f,
//                //y轴移动后的结束位置
//                Animation.RELATIVE_TO_SELF,0.0f
//                r1,
//                0,
//                0,
//                0
                moveright,moveright+ right, 0, 0

        );
        moveright=moveright+ right;
        animation.setFillAfter(true);
        animation.setDuration(300);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        controller.setDelay(0.5f);//动画延迟
        mListView.setLayoutAnimation(controller);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
//结束后设置位置
//                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                        mListView.getLayoutParams());
//                params.setMargins(right, 0, 0, 0);
//                mListView.clearAnimation();
//                mListView.setLayoutParams(params);



            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    public void Itemleft(int left) {
        //------------

        mListView.setAdapter(new HomeAdapter());
        TranslateAnimation animation = new TranslateAnimation( moveleft,moveleft+left, 0, 0

        );
        moveleft=moveleft+left;
        animation.setFillAfter(true);
        animation.setDuration(300);
        LayoutAnimationController controller = new LayoutAnimationController(animation);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        controller.setDelay(0.5f);
        mListView.setLayoutAnimation(controller);
    }
    //########################################################################################



    //########################################################################################
    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
    }
    //########################################################################################

}
