package com.vanhitech.vanhitech.controller;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.flyco.dialog.widget.popup.base.BasePopup;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.db.sqlite.WhereBuilder;
import com.lidroid.xutils.exception.DbException;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.protocol.cmd.client.CMD08_ControlDevice;
import com.vanhitech.protocol.cmd.client.CMD10_DelDevice;
import com.vanhitech.protocol.cmd.client.CMD12_ModifyDevice;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.protocol.object.device.LightCWDevice;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.SwipeMenulistview.PullToRefreshSwipeMenuListView;
import com.vanhitech.vanhitech.SwipeMenulistview.PullToRefreshSwipeMenuListView.IXListViewListener;
import com.vanhitech.vanhitech.SwipeMenulistview.SwipeMenu;
import com.vanhitech.vanhitech.SwipeMenulistview.SwipeMenuCreator;
import com.vanhitech.vanhitech.SwipeMenulistview.SwipeMenuItem;
import com.vanhitech.vanhitech.activity.device.AddRoomActivity;
import com.vanhitech.vanhitech.activity.device.AirMainActivity;
import com.vanhitech.vanhitech.activity.device.CurtainActivity1;
import com.vanhitech.vanhitech.activity.device.DeviceInfoActivity;
import com.vanhitech.vanhitech.activity.device.LightListActivity;
import com.vanhitech.vanhitech.activity.device.LightRgbActivity;
import com.vanhitech.vanhitech.activity.device.LightWhiteActivity;
import com.vanhitech.vanhitech.activity.device.LightsingleRgbActivity;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.base.MyApplication;
import com.vanhitech.vanhitech.bean.Place;
import com.vanhitech.vanhitech.bean.Power;
import com.vanhitech.vanhitech.conf.Constants;
import com.vanhitech.vanhitech.conf.LoginConstants;
import com.vanhitech.vanhitech.controller.home.HomeListController;
import com.vanhitech.vanhitech.controller.home.HomeRoomInfoActivity;
import com.vanhitech.vanhitech.controller.setting.SettingInstallDeviceActivity;
import com.vanhitech.vanhitech.popupwindow.SelectDevicePopupWindow;
import com.vanhitech.vanhitech.pulltorefresh.RefreshTime;
import com.vanhitech.vanhitech.utils.AnimationController;
import com.vanhitech.vanhitech.utils.DataUtils;
import com.vanhitech.vanhitech.utils.DateUtils;
import com.vanhitech.vanhitech.utils.LogUtils;
import com.vanhitech.vanhitech.utils.T;
import com.vanhitech.vanhitech.utils.ThreadUtils;
import com.vanhitech.vanhitech.utils.UIUtils;
import com.vanhitech.vanhitech.utils.ViewFindUtils;
import com.vanhitech.vanhitech.utils.YHDCollectionUtils;
import com.vanhitech.vanhitech.views.LinkedViewPager.MyPagerAdapter;
import com.vanhitech.vanhitech.views.LinkedViewPager.NoScrollViewPager;
import com.vanhitech.vanhitech.zxing.CaptureActivity;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 创建者     heyn
 * 创建时间   2016/3/14 21:38
 * 描述	   1.提供视图,2接收数据,加载数据,3数据和视图的绑定
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class HomeController extends BaseController implements com.vanhitech.vanhitech.views.LinkedViewPager.ViewPager.OnPageChangeListener, IXListViewListener {
    private BitmapUtils mBitmapUtils;
    @ViewInject(R.id.home_pager_listview)
    PullToRefreshSwipeMenuListView mListView;
    public static final String TAG = HomeController.class.getSimpleName();
    private List<Device> mDeviceList;
    @ViewInject(R.id.left_room_info)
    Button mBtLeftInfo;
    @ViewInject(R.id.right_room_add)
    Button mBtRightAdd;
    @ViewInject(R.id.pager_title)
    TextView mHomeTvTitle;
    private boolean doNotifyDataSetChangedOnce = false;
    private int mPosition = 0;
    private ServerCommand mMCmd05;
    private List<HomeListController> mHomelistControllers;
    private HomeAdapter mListvdapter;
    private TopHomePagerAdater mTopHomePagerAdater;
    private float mTranslationX = 0;
    private float mDownX;
    private float mDownY;
    private DbUtils mDb = DataUtils.getInstance().mDb;
    private LayoutAnimationController mMLac;
    private LayoutAnimationController mMRac;
    private View mView;
    private List<Place> Plist;
    private List<String> lisPager;
    private List<LightCWDevice> listCWDDevice;
    private Set<String> set;
    private List<Device> devices;//装设备
    private boolean sw = true;
    /**
     * 定时去操作
     */

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    if (MyApplication.getIsbrushStart()) {
                        homeBrush();
                    }
                    break;
            }
        }
    };

    private void homeBrush() {
        if (sw) {
            CmdBaseActivity.getInstance().sendCmd04();
            sw = false;
        } else {
            String time = DataUtils.getInstance().updateTime(0 + "");
            if (time == null) {
                time = "20160523112459";
            }
            Boolean flg = MyApplication.getIsbrushRoom();
            if (flg || DateUtils.isNewLine(time)) {
                updateUI();
                if (mPosition + "" == null) {
                    mPosition = 0;
                }

                if (flg) {
                    mPosition=0;
                    processData(0);
                    mArticleAdapter_home = new ArticleAdapter_home();
                    mViewPager.setAdapter(mArticleAdapter_home);
                    MyApplication.setIsbrushStart(false);
                } else {
                    if (mArticleAdapter_home != null)
                        mArticleAdapter_home.notifyDataSetChanged();
                    processData(mPosition);

                }
                mListvdapter.notifyDataSetChanged();
                MyApplication.setIsbrushStart(false);

            } else {

            }
            sw = true;
        }
    }

    private ArticleAdapter_home mArticleAdapter_home;

    private HomeChangeNamePop mHomeChangeNamePop;

    public void start(Boolean flag) {
        if (flag) {
            TimerTask tt = new TimerTask() {
                public void run() {
                    handler.sendEmptyMessage(1);
                }
            };
            Timer timer = new Timer();
            timer.schedule(tt, 500, 500);
        }
    }


    public HomeController(Context context, Activity activity, ServerCommand cmd) {
        super(context, activity);
    }

    public HomeController(Context context, Activity activity) {
        super(context, activity);
        MyApplication.setIsbrushStart(true);

        start(MyApplication.getIsbrushStart());//定时


    }

    private void updateUI() {
        set = new HashSet<>();

//        Plist = DataUtils.getInstance().getcrePlaceSave();
        Plist = DataUtils.getInstance().getPlaceUserid(LoginConstants.USERNAME);
        if (Plist != null)
            for (Place s : Plist) {
                set.add(s.place);
            }

        lisPager = new ArrayList<>();
        devices = new ArrayList<>();//装设备
        listCWDDevice = new ArrayList<>();
        dbDeviceData(); //数据


        for (Device dplace : devices) {
            set.add(dplace.place);
        }
        set.removeAll(YHDCollectionUtils.nullCollection());
        lisPager.addAll(set);
        lisPager.add(set.size(), "添加房间");
        if (lisPager == null) {
            lisPager.add("请添加设备");//
        }

        //更新viwepager
        if (Constants.updateViewpager) {
            if (lisPager != null)
                for (int i = 0; i < lisPager.size() - 1; i++) {
                    if (mViewPager != null) {
                        ImageView tvRecord = (ImageView) mViewPager.findViewWithTag("tvRecord" + i);
                        if (tvRecord != null)
                            viewpagerImager(lisPager.get(i), tvRecord);
//                        LogUtils.i("更新viwepager.....");
                        TextView tvtitlRecord = (TextView) mViewPager.findViewWithTag("titeRecord" + i);
                        if (tvtitlRecord != null)
                            tvtitlRecord.setText(lisPager.get(i));
//                    LogUtils.i("更新textview.....");
                    }
                }
            Constants.updateViewpager = true;
        }

    }

    private void dbDeviceData() {
        List<Device> device = new ArrayList<>();//
        device = DataUtils.getInstance().getdataDevice();
        nulladd(devices, device);
        device = DataUtils.getInstance().getdataRGBDevice();
        nulladd(devices, device);
        device = DataUtils.getInstance().getdataCWDDevice();//
        nulladd(devices, device);
        device = DataUtils.getInstance().getdataAIRDevice();//
        nulladd(devices, device);
        device = DataUtils.getInstance().getdataLockDoorDevice();// 如果数据为空,就报错
        nulladd(devices, device);
    }

    private void nulladd(List<Device> devices, List<Device> device) {
        if (device != null) {
            devices.addAll(device);//数据库加载数据
            device.clear();
        }
    }

    @Override
    public View initContentView(Context context) {
        updateUI();
        /**
         * data+view
         */
        mView = View.inflate(mContext, R.layout.home_viewpager, null);
        //通过注入找孩子
        ViewUtils.inject(this, mView);

        initViewPager();
//        // 1.设置幕后item的缓存数目
//        mViewPager.setOffscreenPageLimit(3);
//        // 2.设置页与页之间的间距
//        mViewPager.setPageMargin(30);

        CmdBaseActivity.getInstance().sendCmd04();
        MyApplication.setIsbrushRoom(false);
        MyApplication.setIsPrompt(false);
        return mView;

    }

    private NoScrollViewPager mPager;

    private ArrayList<View> mPageViews;

    private MyPagerAdapter mPageAdapter;

    private com.vanhitech.vanhitech.views.LinkedViewPager.InnerViewPager mViewPager;

    private ArrayList<View> mFramePageViews;


    private void initViewPager() {

        mPager = (NoScrollViewPager) mView.findViewById(R.id.pager);
        mViewPager = (com.vanhitech.vanhitech.views.LinkedViewPager.InnerViewPager) mView.findViewById(R.id.home_pager_viewpager);
        // 1.设置幕后item的缓存数目
        mViewPager.setOffscreenPageLimit(3);
        // 2.设置页与页之间的间距
        mViewPager.setPageMargin(10);

        mPageViews = new ArrayList<>();
        mFramePageViews = new ArrayList<>();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view1 = inflater.inflate(R.layout.home_viewpager_transparent, null);
        mListView = (PullToRefreshSwipeMenuListView) view1.findViewById(R.id.home_pager_listview);
        mListvdapter = new HomeAdapter();
        mListView.setAdapter(mListvdapter);
        initPagerView(view1);

        mPageViews.add(view1);

        mPageAdapter = new MyPagerAdapter(mPageViews);
        mPager.setAdapter(mPageAdapter);
        mViewPager.setFollowViewPager(mPager);//关键
    }

    public void initPagerView(View view1) {
        mHomeTvTitle = (TextView) view1.findViewById(R.id.pager_title);
        mBtRightAdd = (Button) view1.findViewById(R.id.right_room_add);
        mBtLeftInfo = (Button) view1.findViewById(R.id.left_room_info);
    }

    @Override
    public void initTitleBar() {
        mTvTitle.setText("云海智能家居");
    }

    /**
     * 1.加载数据,进行视图的刷新
     * 2.视图和数据的绑定
     */
    public void initData() {

        mDeviceList = new ArrayList<>();


        mBitmapUtils = new BitmapUtils(mContext);//图片加载处理
        mTopHomePagerAdater = new TopHomePagerAdater();

//        mViewPager.setAdapter(mTopHomePagerAdater);//TODO
        mArticleAdapter_home = new ArticleAdapter_home();
        mViewPager.setAdapter(mArticleAdapter_home);//TODO
        mArticleAdapter_home.notifyDataSetChanged();
        mViewPager.setCurrentItem(0);//进入显示的页面

        processData(0);

        swipeRefresh();
        //listview 的item点击事件
        mListView.setOnItemClickListener(new ListViewCilickListener());
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);

        //监听viewpager的选中
        mViewPager.setOnPageChangeListener(this);

        //################################
        mViewPager.setOnTouchListener(new MyTouchListener());

        Animationtini();
        Animationlete();


    }

    /**
     * 滑动删除
     * 下拉刷新
     */
    private void swipeRefresh() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(mContext);
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
                // set item width
                openItem.setWidth(dp2px(66));
                // set item title
                openItem.setTitle("名称");
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
                deleteItem.setWidth(dp2px(66));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        mListView.setMenuCreator(creator);

        mListView.setOnMenuItemClickListener(new PullToRefreshSwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                Device item = mDeviceList.get(position);
                switch (index) {
                    case 0:
                        open(item);

                        LogUtils.i(TAG, "open------------------------");
                        break;
                    case 1:
                        // delete
                        // delete(item);

                        NormalDialogStyleTwo(position, item);

                        break;
                }
            }
        });

        // set SwipeListener
        mListView.setOnSwipeListener(new PullToRefreshSwipeMenuListView.OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
                System.out.println("onSwipeStart");
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
                System.out.println("onSwipeEnd");
            }
        });


        // test item long click
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                Toast.makeText(mContext, position + " long click", Toast.LENGTH_SHORT).show();
//                return false;  //返回false 长按后还会触发点击事件
                longClickSwitch(position);


                return true;
            }
        });


    }

    private void longClickSwitch(int position) {
        /**
         * 直接在这里根据position 去操作灯的开关好了,这个方法封装一下
         * 1.先拿到设备信息
         * ２.判断设备灯开关状态
         * ３.取反
         * ４.发送
         */
//                Device
        //-------
        //从数据库获取开关是开还是关，然后赋值
        Device device = devices.get(position - 1);
//            Boolean temp=!flag;
//           device.power.set(0,temp);
//            Boolean is = device.power.get(0).on;//换从数据库取值
        Boolean is ;//换从数据库取值

//            try {
        Power power = null;
        try {
            power = mDb.findFirst(Selector.from(Power.class).where("id", "=", device.id));
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (power.power0 != null && power.power0.equals("1")) {
            is = false;
        } else {
            is = true;
        }
//            } catch (DbException e) {
//                e.printStackTrace();
//            }
//        com.vanhitech.protocol.object.Power vp=new com.vanhitech.protocol.object.Power(0,is);
        device.setPower(is);
//            device.power.get(0).on = is;
        CMD08_ControlDevice cmd08 = new CMD08_ControlDevice(device);
//            try {
//                HomeController.mHelper.sendCMD(cmd08);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
        CmdBaseActivity.getInstance().sendCmd(cmd08);

        //-------
    }

    /**
     * 启动设备信息
     *
     * @param item
     */
    private void open(Device item) {
        mHomeChangeNamePop = new HomeChangeNamePop(mContext, item);
        mHomeChangeNamePop.widthScale(0.9f);
        mHomeChangeNamePop.heightScale(0.25f);
        mHomeChangeNamePop.gravity(Gravity.BOTTOM)

                .offset(0, 0)
                .dimEnabled(true)
                .show();
    }

    /**
     * 监听viepager的选中
     *
     * @param position
     * @param positionOffset       滚动的百分比
     * @param positionOffsetPixels
     */
    @Override   //页面滚动
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        doNotifyDataSetChangedOnce = true;//标识
        mTranslationX = scrollIndicatorsListView(position, positionOffset);


    }

    /**
     * 滚动listview
     *
     * @param position
     * @param positionOffset
     */     //计算移动偏移量
    private float scrollIndicatorsListView(int position, float positionOffset) {
        //移动的量等于屏幕的宽乘以偏移量
        int screenWidth = UIUtils.getScreenWidth(mContext);
        mTranslationX = screenWidth * position + screenWidth * positionOffset;

        return mTranslationX;
    }

    @Override   //当某页被选择
    public void onPageSelected(int position) {
        if (lisPager.size() != 0) {
            mPosition = position;//TODO
            processData(position);
            mListvdapter.notifyDataSetChanged();
            mArticleAdapter_home.notifyDataSetChanged();
        }

    }

    //页面状态改变的时候
    @Override
    public void onPageScrollStateChanged(int state) {
//        mListvdapter.notifyDataSetChanged();
//        mArticleAdapter_home.notifyDataSetChanged();

    }

    //下拉刷新
    @Override
    public void onRefresh() {
        ThreadUtils.mHander.postDelayed(new Runnable() {
            @Override
            public void run() {

//                 CmdBaseActivity.getInstance().sendCmd04();
                SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                RefreshTime.setRefreshTime(mContext, df.format(new Date()));
                onLoad();
                CmdBaseActivity.getInstance().closeSocket();//TODO closeServer
                CmdBaseActivity.getInstance().starToConnectServer();
                new Handler().postDelayed(new Runnable() {//延迟3秒去更新ui
                    public void run() {
                        //execute the task
                        updateUI();
                        if (mPosition + "" == null) {
                            mPosition = 0;
                        }
//                mDeviceList.clear();
                        processData(0);//mPositon 这里换成 mArticleAdapter的position
                        mArticleAdapter_home = new ArticleAdapter_home();
                        mViewPager.setAdapter(mArticleAdapter_home);//TODO
                        mListvdapter.notifyDataSetChanged();
//                        LoginConstants.fresh = true;
                    }
                }, 1000);

            }
        }, 1000);

    }

    //上拉刷新
    @Override
    public void onLoadMore() {
//        LoginConstants.fresh = true;
//        updateUI();
//        if (mPosition + "" == null) {
//            mPosition = 0;
//        }
//        processData(mPosition);
//        ThreadUtils.mHander.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mListvdapter.notifyDataSetChanged();
//                mArticleAdapter_home.notifyDataSetChanged();
//
//            }
//        }, 10);
        onLoad();

    }

    private void onLoad() {
        mListView.setRefreshTime(RefreshTime.getRefreshTime(mContext));
        mListView.stopRefresh();
        mListView.stopLoadMore();

    }

    //Viewpager按下监听事件
    class MyTouchListener implements View.OnTouchListener {

        private float mViewPagerDownX;

        @Override
        public boolean onTouch(View v, MotionEvent event) {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.i("HomeListController", "MotionEvent.ACTION_DOWN");
                    mDownX = event.getRawX();
                    mDownY = event.getRawY();
                    //按下
                    mViewPagerDownX = mViewPager.getScaleX();


                    break;
                case MotionEvent.ACTION_MOVE:
                    int diffXPager = (int) (mViewPagerDownX - mViewPager.getScrollX() + .5f);
                    float moveX = event.getRawX();
                    float moveY = event.getRawY();
                    int diffX = (int) (moveX - mDownX + .5f);
                    int diffY = (int) (moveY - mDownY + .5f);

                    if (diffX > 0) {//往右拖动
//                        Log.i(TAG, "往右拖动-->");
                        mListView.clearAnimation();
                        mListView.setLayoutAnimation(mMRac);
                    } else {
                        mListView.clearAnimation();
                        mListView.setLayoutAnimation(mMLac);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    break;
                case MotionEvent.ACTION_CANCEL:

                    break;

                default:
                    break;
            }
            return false;

        }

    }

    //数据整理
    private void processData(int position) {
        mDeviceList = new ArrayList<>();
        if (lisPager.size() > 0) {
            lisPager.removeAll(YHDCollectionUtils.nullCollection());
            devices.removeAll(YHDCollectionUtils.nullCollection());
            //如果lisPager
            LogUtils.i("lisPager.sizi:" + lisPager.size() + "position:" + position);
            if (lisPager.size() > position) {//保证数组不越界
                String placetemp = lisPager.get(position);
                if (devices != null) ;
                for (Device d : devices) {
                    if (d != null) {
                        String place = d.place;

                        if (placetemp == null) {
                            placetemp = "null";
                        }
                        if (place == null) {
                            place = "null";
                        }
                        if (placetemp.equals(place.trim())) {
                            mDeviceList.add(d);
                        }
//                }
                    }
                }
            }
        }//TODO
    }

    //place 客厅          place:未指定   place:客厅   按设备分
//    class TopHomePagerAdater extends PagerAdapter {
    class TopHomePagerAdater extends com.vanhitech.vanhitech.views.LinkedViewPager.PagerAdapter {
        @Override
        public int getCount() {
//            if (doNotifyDataSetChangedOnce) {
//                doNotifyDataSetChangedOnce = false;
//                notifyDataSetChanged();
//            }
//
//            if (lisPager != null)//TODO
//                return lisPager.size();
//            return 0;
            return null != lisPager ? lisPager.size() : 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView iv = new ImageView(mContext);
            iv.setScaleType(ImageView.ScaleType.FIT_XY); //图片的拉伸模式
            iv.setBackgroundResource(R.mipmap.roottemp);
            mBitmapUtils.configDefaultLoadFailedImage(R.mipmap.roottemp);
            viewpagerImager(lisPager.get(position), iv);//
            if (position == lisPager.size() - 1) {
                iv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mIntent = new Intent();
                        mIntent.setClass(mContext, AddRoomActivity.class);
                        mActivity.startActivity(mIntent);
                        mActivity.overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
                    }
                });
            }
            String key = "tvRecord" + position;
            iv.setTag(key);//TODO
            container.addView(iv);

            return iv;
        }

        //销毁
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void notifyDataSetChanged() {
            refreshData();//TODO
            super.notifyDataSetChanged();
        }

        private void refreshData() {
//            mItems = mController.getSelected();

        }

    }

    private void viewpagerImager(String mIntentPlace, ImageView iv) {
        if (mIntentPlace != null)
            try {

                Place place = DataUtils.getInstance().mDb.findFirst(Selector.from(Place.class).where("place", "=", mIntentPlace.replaceAll(" ", "")));//TODO 这里的0要换成postion
                if (place != null)
                    if (null != place.url) {

                        mBitmapUtils.display(iv, place.url);//TODO
//                iv.setImageDrawable(Drawable.createFromPath(place.url));
//                Log.i("HomeController", place.url.toString());
                    } else {
//                        mBitmapUtils.display(iv, "assets/img/roottemp.bmp");//TODO
                        nameToimager(iv, mIntentPlace);

                    }
            } catch (DbException e) {
                e.printStackTrace();
            }
    }


    class HomeAdapter extends BaseAdapter {


        /**
         * getCount 里面存放 比如说 客厅有多少设备 这里就返回多少个
         * 这里的device 是一个
         *
         * @return
         */
        @Override
        public int getCount() {
            if (mDeviceList != null) {
                int i = mDeviceList.size();
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
            if (convertView == null) {

                convertView = View.inflate(mContext, R.layout.item_homelist, null);
                holder = new ViewHolder();
                holder.mIvicon = (ImageView) convertView.findViewById(R.id.item_homelist_icon);
                holder.mTvName = (TextView) convertView.findViewById(R.id.item_homelist_name);
                holder.mTvStatus = (TextView) convertView.findViewById(R.id.item_homelist_status);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String idtemp = mDeviceList.get(position).id;
            holder.mTvName.setText(mDeviceList.get(position).name + "-" + idtemp.substring(idtemp.length() - 3));
            if (mDeviceList.get(position).online) {
                holder.mTvStatus.setText("在线");
            } else {
                holder.mTvStatus.setText("离线");
            }
            String ID = mDeviceList.get(position).id.substring(0, 2);
            if (ID.equals(Constants.id1)) {
                mBitmapUtils.display(holder.mIvicon, "assets/img/home01.bmp");
            } else if (ID.equals(Constants.id2)) {

            } else if (ID.equals(Constants.id3)) {//2.4G灯开关控制  !
                mBitmapUtils.display(holder.mIvicon, "assets/img/home03.bmp");
            } else if (ID.equals(Constants.id4)) {

            } else if (ID.equals(Constants.id5)) {
                mBitmapUtils.display(holder.mIvicon, "assets/img/home0a.bmp");
            } else if (ID.equals(Constants.id6)) {
                mBitmapUtils.display(holder.mIvicon, "assets/img/home06.bmp");
            } else if (ID.equals(Constants.id7)) {
                mBitmapUtils.display(holder.mIvicon, "assets/img/home07.bmp");
            } else if (ID.equals(Constants.id8)) {
                mBitmapUtils.display(holder.mIvicon, "assets/img/home08.bmp");
            } else if (ID.equals(Constants.id9)) {
                mBitmapUtils.display(holder.mIvicon, "assets/img/home00.bmp");
            } else if (ID.equals(Constants.idA)) {
                mBitmapUtils.display(holder.mIvicon, "assets/img/home0a.bmp");
            } else if (ID.equals(Constants.idB)) { //2.4G RGB灯控制  !
                mBitmapUtils.display(holder.mIvicon, "assets/img/home0b.bmp");
            } else if (ID.equals(Constants.idC)) { //2.4G RGB灯控制  !
                mBitmapUtils.display(holder.mIvicon, "assets/img/home06.bmp");
            } else if (ID.equals(Constants.idD)) {//2.4G冷暖灯开关及调光控制 !
                mBitmapUtils.display(holder.mIvicon, "assets/img/home0b.bmp");
            } else if (ID.equals(Constants.idE)) {//2.4G RGB灯控制
            } else if (ID.equals(Constants.idF)) {//2.4G RGB灯控制  !
                mBitmapUtils.display(holder.mIvicon, "assets/img/home06.bmp");
            } else if (ID.equals(Constants.id10)) {
                mBitmapUtils.display(holder.mIvicon, "assets/img/home10.bmp");
            } else if (ID.equals(Constants.id11)) {
            } else if (ID.equals(Constants.id12)) {
            } else if (ID.equals(Constants.id13)) {
                mBitmapUtils.display(holder.mIvicon, "assets/img/home13.bmp");
            } else if (ID.equals(Constants.id14)) {
                mBitmapUtils.display(holder.mIvicon, "assets/img/home14.bmp");
            } else {
                mBitmapUtils.display(holder.mIvicon, "assets/img/home00.bmp");
            }
            return convertView;
        }

        class ViewHolder {
            TextView mTvName;
            TextView mTvStatus;
            ImageView mIvicon;
        }
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
    }

    /**
     * mBtLeftInfo
     * 左边房间按键点击事件
     */
    class LeftClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            LoginConstants.fresh = false;
            Intent intent = new Intent();
            intent.setClass(mContext, HomeRoomInfoActivity.class);
            String intPlace = lisPager.get(mPosition) + "";
            intent.putExtra("place", intPlace);//TODO 传String place值
            mActivity.startActivity(intent);
            mActivity.overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);

        }
    }

    /**
     * mBtRightAdd
     * 右边按键跳转扫描添加房间
     */
    SelectDevicePopupWindow photoWindow;

    class RightClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            photoWindow = new SelectDevicePopupWindow(mActivity, itemsOnClick);
            photoWindow.showAtLocation(mActivity.findViewById(R.id.home_viewpager_id),
                    Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
                    0); //设置layout在PopupWindow中显示的位置


        }
    }

    //listview的点击事件
    class ListViewCilickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            position = position - 1;
            String ID = mDeviceList.get(position).id.substring(0, 2);
            String name = mDeviceList.get(position).name;
            // str=str.Substring(0,i);  0x06525300000001~0x065253FFFFFFFF 截取前6个字符来判断设备类别
            //虽然说明上面是区别是前6，但实际看上去，应该是前3个数字来判断类别
            //再次更改为前2位   007的先不管 到时候再添加一个00
            LogUtils.i("被点击" + position + "id:" + id + "---" + ID); //点击后根据什么去取值好？
            Intent intent = new Intent();
            if (ID.equals(Constants.id0)) {
                intent.setClass(mActivity, DeviceInfoActivity.class);
                intent.putExtra("name", name);
                intent.putExtra("id", mDeviceList.get(position).id);
                mActivity.startActivity(intent);
            } else if (ID.equals(Constants.id1)) {////2.4G插座
                goDevie(position, intent, LightListActivity.class);
            } else if (ID.equals(Constants.id2)) {

            } else if (ID.equals(Constants.id3)) {//2.4G灯开关控制  !
                intent.setClass(mActivity, LightWhiteActivity.class);
                intent.putExtra("name", mDeviceList.get(position).id);
                mActivity.startActivity(intent);
            } else if (ID.equals(Constants.id4)) {

            } else if (ID.equals(Constants.id5)) {//2.4G空调万用遥控器
                openActivity(AirMainActivity.class, mDeviceList.get(position), mActivity, false);
            } else if (ID.equals(Constants.id6)) {
                openActivity(LightsingleRgbActivity.class, mDeviceList.get(position), mActivity, false);
            } else if (ID.equals(Constants.id7)) {

            } else if (ID.equals(Constants.id8)) {

            } else if (ID.equals(Constants.id9)) {

            } else if (ID.equals(Constants.idA)) {// //空调功率插头+遥控器
                openActivity(AirMainActivity.class, mDeviceList.get(position), mActivity, false);
            } else if (ID.equals(Constants.idB)) { //2.4G RGB灯控制  !
                intent.setClass(mActivity, LightRgbActivity.class);
                intent.putExtra("name", mDeviceList.get(position).id);
                mActivity.startActivity(intent);
            } else if (ID.equals(Constants.idC)) { //2.4G RGB灯控制  !
                intent.setClass(mActivity, LightRgbActivity.class);
                intent.putExtra("name", mDeviceList.get(position).id);
                mActivity.startActivity(intent);
            } else if (ID.equals(Constants.idD)) {//2.4G冷暖灯开关及调光控制 !
                intent.setClass(mActivity, LightWhiteActivity.class);
                intent.putExtra("name", mDeviceList.get(position).id);
                mActivity.startActivity(intent);
            } else if (ID.equals(Constants.idE)) {//2.4G RGB灯控制
                intent.setClass(mActivity, LightRgbActivity.class);
                intent.putExtra("name", mDeviceList.get(position).id);
                mActivity.startActivity(intent);
            } else if (ID.equals(Constants.idF)) {//2.4G RGB灯控制  !
                intent.setClass(mActivity, LightsingleRgbActivity.class);
                intent.putExtra("name", mDeviceList.get(position).id);
                mActivity.startActivity(intent);
            } else if (ID.equals(Constants.id10)) {//窗帘
//                intent.setClass(mActivity, CurtainActivity1.class);
//                intent.putExtra("name", mDeviceList.get(position).id);
//                mActivity.startActivity(intent);
                openActivity(CurtainActivity1.class, mDeviceList.get(position), mActivity, false);
            } else if (ID.equals(Constants.id11)) {

            } else if (ID.equals(Constants.id12)) {

            } else if (ID.equals(Constants.id13)) {

            } else if (ID.equals(Constants.id14)) {

            } else {
                //不处理
            }
            T.hideToast();
            mActivity.overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
        }
    }

    private void goDevie(int position, Intent intent, Class<?> deviceClass) {
        intent.setClass(mActivity, deviceClass);
        intent.putExtra("name", mDeviceList.get(position).name);
        intent.putExtra("id", mDeviceList.get(position).id);
        mActivity.startActivity(intent);
    }


    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            photoWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    popTakePhoto();
                    break;
                case R.id.btn_pick_photo:
                    popPickPhoto();
                    break;
                default:
                    break;
            }
        }
    };

    private void popPickPhoto() {
        LoginConstants.fresh = false;
        Intent intent = new Intent();
        intent.putExtra("place", lisPager.get(mPosition) + "");//TODO 传String place值
        intent.setClass(mContext, CaptureActivity.class);
        mActivity.startActivity(intent);
        mActivity.overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
    }

    private void popTakePhoto() {
        LoginConstants.fresh = false;
        Intent mIntent = new Intent();
        mIntent.putExtra("place", lisPager.get(mPosition) + "");//TODO 传String place值
        mIntent.setClass(mContext, SettingInstallDeviceActivity.class);
        mActivity.startActivity(mIntent);
        mActivity.overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
    }


    public void Animationtini() {
        /** LayoutAnimation */
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 2f, Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration(550);   //TODO

        mMLac = new LayoutAnimationController(animation, 0.12f);
        mMLac.setInterpolator(new DecelerateInterpolator());

    }

    public void Animationlete() {
        /** LayoutAnimation */
        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -2f, Animation.RELATIVE_TO_SELF,
                0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.setDuration(550);   //TODO

        mMRac = new LayoutAnimationController(animation, 0.12f);
        mMRac.setInterpolator(new DecelerateInterpolator());

    }


    class ArticleAdapter_home extends com.vanhitech.vanhitech.views.LinkedViewPager.PagerAdapter {
        //    List<Place> list;
//        List<String> Stplace= lisPager;
        private com.vanhitech.vanhitech.views.LinkedViewPager.ViewPager pager;
        LayoutInflater inflater = LayoutInflater.from(mContext);
        //用于存储回收掉的View
        private List<WeakReference<LinearLayout>> viewList = new ArrayList<WeakReference<LinearLayout>>();

        private Place mArticle;


        @Override
        public int getCount() {
            return null != lisPager ? lisPager.size() : 0;
        }

        @Override
        public Object instantiateItem(View container, int position) { // 这个方法用来实例化页卡

            if (pager == null) {
                pager = (com.vanhitech.vanhitech.views.LinkedViewPager.ViewPager) container;
            }
            View view = null;
            // 从废弃的里去取 取到则使用 取不到则创建
            if (viewList.size() > 0) {
                if (viewList.get(0) != null) {
                    view = initView(viewList.get(0).get(), position);
                    viewList.remove(0);
                }
            }
            view = initView(null, position);
            pager.addView(view);


            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            //存储离屏的View 等待复用
            LinearLayout view = (LinearLayout) object;
            container.removeView(view);
            viewList.add(new WeakReference<LinearLayout>(view));

        }

        private View initView(LinearLayout view, int position) {
            ViewHolder viewHolder = null;
            if (view == null) {
                view = (LinearLayout) inflater.inflate(R.layout.item_viewpager_home2, null);
                viewHolder = new ViewHolder();
                viewHolder.title_TV = (TextView) view
                        .findViewById(R.id.home_pager_title);
                viewHolder.mImageView = (ImageView) view
                        .findViewById(R.id.homelist_pager_imageview);
                viewHolder.mLeftRoomInfo = (Button) view
                        .findViewById(R.id.left_room_info);
                viewHolder.mRightRoomAdd = (Button) view
                        .findViewById(R.id.right_room_add);
                viewHolder.mHomeRoomName = (LinearLayout) view.findViewById(R.id.home_room_name);


                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            /**
             * 初始化数据
             */
            //getPlace
//            mPosition = position;
//            mBitmapUtils.configDefaultLoadFailedImage(R.mipmap.roottemp);
            //viewHolder.mHomeRoomName
            viewHolder.mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            viewHolder.mImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
//            viewHolder.mHomeRoomName.setScaleType(ImageView.ScaleType.FIT_XY);
            mBitmapUtils.display(viewHolder.mHomeRoomName, "assets/img/home_info_name.bmp");
            if (lisPager != null && position < lisPager.size()) {
//                mArticle = DataUtils.getInstance().getPlace(lisPager.get(position));
                mArticle = DataUtils.getInstance().getPlaceUserid(lisPager.get(position), LoginConstants.USERNAME);
                if (mArticle != null) {
                    if (mArticle.place == null) {
                        mArticle.place = "请添加房间";
                        viewHolder.title_TV.setText("请添加房间");
                    } else {
                        viewHolder.title_TV.setText(mArticle.place);

                    }
                    if (mArticle.url != null) {
                        viewHolder.mImageView.setBackgroundDrawable(null);
                        mBitmapUtils.display(viewHolder.mImageView, mArticle.url);
                    } else {//"assets/img/roottemp.bmp"
//                        mBitmapUtils.display(viewHolder.mImageView, "assets/img/img_room_living.bmp");
                        /*
                        根据名字判断给地址吧

                         */
                        nameToimager(viewHolder.mImageView, mArticle.place);
                    }


                } else {
                    viewHolder.title_TV.setText(lisPager.get(position));
                }

            }
            viewHolder.mLeftRoomInfo.setOnClickListener(new LeftClickListener());
            viewHolder.mRightRoomAdd.setOnClickListener(new RightClickListener());
            //TODO
            int sizi = lisPager.size();
            if (position == sizi - 1) {
                AnimationController.hide(viewHolder.mLeftRoomInfo);
                AnimationController.hide(viewHolder.mRightRoomAdd);
                mBitmapUtils.display(viewHolder.mImageView, "assets/img/img_add_room.bmp");
                viewHolder.mImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent mIntent = new Intent();
                        mIntent.setClass(mActivity, AddRoomActivity.class);
                        mActivity.startActivity(mIntent);
                        mActivity.overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
                    }
                });
            }

            String key = "tvRecord" + position;
            viewHolder.mImageView.setTag(key);//TODO
            String keytitl = "titeRecord" + position;
            viewHolder.title_TV.setTag(keytitl);
            Constants.updateViewpager = true;
            return view;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        private class ViewHolder {
            TextView title_TV;
            ImageView mImageView;
            Button mLeftRoomInfo;
            Button mRightRoomAdd;
            LinearLayout mHomeRoomName;
        }


    }

    private void nameToimager(View imageView, String place) {
        /**
         * ("assets/img/img_room_bedroom.bmp",10+"", "默认卧室");
         ("assets/img/img_room_balcony.bmp",11+"", "默认阳台");
         ("assets/img/img_room_kitchen.bmp",12+"", "默认厨房");
         ("assets/img/img_room_living.bmp",13+"", "默认客厅");
         ("assets/img/img_room_restaunt.bmp",14+"", "默认餐厅");
         ("assets/img/img_room_study.bmp",15+"", "默认书房");
         */
        switch (place) {
            case "卧室":
                mBitmapUtils.display(imageView, "assets/img/img_room_bedroom.bmp");
                break;
            case "阳台":
                mBitmapUtils.display(imageView, "assets/img/img_room_balcony.bmp");
                break;
            case "厨房":
                mBitmapUtils.display(imageView, "assets/img/img_room_kitchen.bmp");
                break;
            case "客厅":
                mBitmapUtils.display(imageView, "assets/img/img_room_living.bmp");
                break;
            case "餐厅":
                mBitmapUtils.display(imageView, "assets/img/img_room_restaunt.bmp");
                break;
            case "书房":
                mBitmapUtils.display(imageView, "assets/img/img_room_study.bmp");
                break;
            case "请添加房间":
                mBitmapUtils.display(imageView, "assets/img/img_add_room.bmp");
                break;
            default:
                mBitmapUtils.display(imageView, "assets/img/img_room_bedroom.bmp");
                break;

        }
    }


    private void NormalDialogStyleTwo(final int position, final Device item) {

        final NormalDialog dialog = new NormalDialog(mContext);

        dialog.content("将要删除该设备。")//
                .style(NormalDialog.STYLE_TWO)//
                .titleTextSize(23)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {

                        mDeviceList.remove(position);

                        //删除设备
                        try {
                            CMD10_DelDevice cmd10 = new CMD10_DelDevice(item.id);
                            CmdBaseActivity.getInstance().sendCmd(cmd10);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {
                            DataUtils.getInstance().mDb.delete(Device.class, WhereBuilder.b("id", "=", item.id));
                        } catch (DbException e) {
                            e.printStackTrace();
                        }
                        updateUI();
                        //
                        mListvdapter.notifyDataSetChanged();
                        mArticleAdapter_home.notifyDataSetChanged();


                        dialog.dismiss();
                    }
                });

    }

    /**
     * 修改名称的ppw
     */
    private class HomeChangeNamePop extends BasePopup<HomeChangeNamePop> {
        private TextView mTvChangeNameTitle;
        private EditText mTvChangeName;
        private TextView mTvChangeCancle;
        private TextView mTvChangeConfirm;
        private Context context;
        private Device item;


        public HomeChangeNamePop(Context context, Device device) {
            super(context);
            item = device;
        }

        @Override
        public View onCreatePopupView() {

            View inflate = View.inflate(mContext, R.layout.dialog_change_name, null);
            mTvChangeNameTitle = ViewFindUtils.find(inflate, R.id.tv_change_name_title);
            mTvChangeName = ViewFindUtils.find(inflate, R.id.tv_change_name);
            mTvChangeCancle = ViewFindUtils.find(inflate, R.id.tv_change_cancle);
            mTvChangeConfirm = ViewFindUtils.find(inflate, R.id.tv_change_confirm);


            return inflate;
        }

        @Override
        public void setUiBeforShow() {
            /**
             *   if (!mPass.isEmpty()) {
             //数据库中取
             //保存到数据库
             */
            mTvChangeName.setText(item.name);


            mTvChangeCancle.setOnClickListener(new View.OnClickListener() {//取消
                @Override
                public void onClick(View v) {
                    mHomeChangeNamePop.dismiss();
                }
            });
            mTvChangeConfirm.setOnClickListener(new View.OnClickListener() {//确定
                @Override
                public void onClick(View v) {

                    String name = mTvChangeName.getText().toString().trim();
                    if (name.isEmpty()) {
                        return;
                    } else {
                        item.name = name;
                        DataUtils.getInstance().save(item);
                        mListvdapter.notifyDataSetChanged();

                    }

                    CMD12_ModifyDevice cmd12 = new CMD12_ModifyDevice(item);
                    CmdBaseActivity.getInstance().sendCmd(cmd12);
                    mHomeChangeNamePop.dismiss();
                }
            });

        }
    }

    public void openActivity(Class<?> pClass, Device device, Activity activity, Boolean finish) {
        Intent _Intent = new Intent();
        _Intent.setClass(mActivity, pClass);
        _Intent.putExtra("name", device.name);
        _Intent.putExtra("id", device.id);
        activity.startActivity(_Intent);
        activity.overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
        if (finish) {
            activity.finish();
        }
    }


}


