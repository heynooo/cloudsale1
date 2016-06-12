package com.vanhitech.vanhitech.controller;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.vanhitech.protocol.cmd.client.CMD30_DelScene;
import com.vanhitech.protocol.cmd.client.CMD34_QuerySceneList;
import com.vanhitech.protocol.object.SceneMode;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.SwipeMenulistview.PullToRefreshSwipeMenuListView;
import com.vanhitech.vanhitech.SwipeMenulistview.SwipeMenu;
import com.vanhitech.vanhitech.SwipeMenulistview.SwipeMenuCreator;
import com.vanhitech.vanhitech.SwipeMenulistview.SwipeMenuItem;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.base.MyApplication;
import com.vanhitech.vanhitech.controller.scener.ScenerAddModeActivity;
import com.vanhitech.vanhitech.controller.scener.ScenerDeviceActivity;
import com.vanhitech.vanhitech.pulltorefresh.RefreshTime;
import com.vanhitech.vanhitech.utils.AnimationController;
import com.vanhitech.vanhitech.utils.DataUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 创建者     heyn
 * 创建时间   2016/3/14 21:38
 * 描述	      1.提供视图,2接收数据,加载数据,3数据和视图的绑定
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}   implements PullToRefreshSwipeMenuListView.IXListViewListener
 */
public class ScenerController extends BaseController implements PullToRefreshSwipeMenuListView.IXListViewListener {

    @ViewInject(R.id.homelist_pager_listview)
    PullToRefreshSwipeMenuListView mListView;

    @ViewInject(R.id.scener_ll_addmode)
    LinearLayout mllAddmode;

    //
    @ViewInject(R.id.scener_addmode_bt)
    Button mScenerAddmode;

    private List<SceneMode> mSceneModeList;
    private SceneModeAdapter mAdapter;
    private Handler mHandler;
    private Boolean isEditSave = false;
    private Activity mActivity;
    private BitmapUtils mBitmapUtils;

    public ScenerController(Context context, Activity activity) {
        super(context, activity);
        mActivity = activity;
        MyApplication.setIsSendScener(true);
    }


    private void onLoad() {

        new Handler().postDelayed(new Runnable() {//延迟2秒去更新
            public void run() {
                mSceneModeList = new ArrayList<SceneMode>();
                List<SceneMode> mlis = new ArrayList<SceneMode>();
                mlis = DataUtils.getInstance().getdataSceneMode(SceneMode.class, mlis);
                mSceneModeList.addAll(mlis);
                if (mlis.size() != 0) {
                    AnimationController.hide(mllAddmode);
                }
                mAdapter.notifyDataSetChanged();

//                mAdapter.notifyDataSetInvalidated();
            }
        }, 10);

        System.out.println("onLoad");
        mListView.setRefreshTime(RefreshTime.getRefreshTime(mContext.getApplicationContext()));
        mListView.stopRefresh();
        mListView.stopLoadMore();


    }

    private void sendCmd34() {
        CMD34_QuerySceneList cmd34 = new CMD34_QuerySceneList();
        CmdBaseActivity.getInstance().sendCmd(cmd34);
    }

    public void onRefresh() {
        System.out.println("onRefresh");
        sendCmd34();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
                RefreshTime.setRefreshTime(mContext.getApplicationContext(), df.format(new Date()));
                onLoad();
            }
        }, 1000);
    }

    public void onLoadMore() {
        System.out.println("onLoadMore");
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                onLoad();
            }
        }, 2000);
    }

    private void open(SceneMode item) {
        // 携带 id 传过去
        openActivity(ScenerAddModeActivity.class, item, mActivity, false);
    }


    /**
     * @param context
     * @return
     * @des 初始化容器布局里面具体应该放置的视图
     */

    @Override
    public View initContentView(Context context) {
        View view = View.inflate(mContext, R.layout.scener_controller_layout, null);
        ViewUtils.inject(this, view);
        return view;
    }

    @Override
    public void initTitleBar() {
        mTvTitle.setText("场景");
        mIbMenu.setText("编辑");
    }

    /**
     * 1.加载数据,进行视图的刷新
     * 2.视图和数据的绑定
     */
    public void initData() {
        mSceneModeList = new ArrayList<>();

        mSceneModeList = DataUtils.getInstance().getdataSceneMode(SceneMode.class, mSceneModeList);
        if (mSceneModeList.size() == 0) {
            isShowAddmodebt();
        }

        mAdapter = new SceneModeAdapter();
        mListView.setAdapter(mAdapter);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(false);//上拉刷新
        mListView.setXListViewListener(this);
        mHandler = new Handler();

        // step 1. create a MenuCreator
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
                deleteItem.setWidth(dp2px(66));
                // set a icon
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);
        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new PullToRefreshSwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                SceneMode item = mSceneModeList.get(position);
                switch (index) {
                    case 0:
                        System.out.println(" ");
                        // open
                        open(item);
                        break;
                    case 1:
                        // delete
                        // delete(item);
                        delete(position, item);
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
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext, position + " long click", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        mListView.setOnItemClickListener(new scenerlistviewClickListener());


        mIbMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isEditSave = !isEditSave;
                if (isEditSave) {
                    mIbMenu.setText("保存");
                } else {
                    mIbMenu.setText("编辑");

                }
                mAdapter.notifyDataSetChanged();
            }
        });


    }

    private void delete(int position, SceneMode item) {
        DataUtils.getInstance().deleteSceneMode(item.id + "");
        //网络命令
        CMD30_DelScene cmd30 = new CMD30_DelScene(item.id);
        CmdBaseActivity.getInstance().sendCmd(cmd30);
        mSceneModeList.remove(position);
        isShowAddmodebt();
        mAdapter.notifyDataSetChanged();
        //

    }

    private void isShowAddmodebt() {
        if (mSceneModeList.size() == 0) {
            AnimationController.show(mllAddmode);

            mScenerAddmode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SceneMode sceneMode = new SceneMode("", 12, "");
                    openActivity(ScenerAddModeActivity.class, sceneMode, mActivity, false);
                }
            });
        } else {
            AnimationController.hide(mllAddmode);
        }
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
    }

    class SceneModeAdapter extends BaseAdapter {
        Boolean flgshow = true;

        @Override
        public int getCount() {
            return null != mSceneModeList ? mSceneModeList.size() : 0;
        }

        @Override
        public SceneMode getItem(int position) {
            return mSceneModeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_scener_mode, null);
                new ViewHolder(convertView);
            }
            ViewHolder holder = (ViewHolder) convertView.getTag();
            SceneMode item = getItem(position);
            holder.mScenerModeIcon.setScaleType(ImageView.ScaleType.FIT_XY);
            holder.mScenerModeIcon.setBackground(mContext.getResources().getDrawable(setIcon(item.imageno)));
            holder.mScenerModeName.setText(item.name);

            if (position == (mSceneModeList.size() - 1) || mSceneModeList.size() == 0) {
                AnimationController.show(holder.scener_addmode_bt);
            } else {
                AnimationController.hide(holder.scener_addmode_bt);
            }
            holder.scener_addmode_bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SceneMode sceneMode = new SceneMode();
                    openActivity(ScenerAddModeActivity.class, sceneMode, mActivity, false);
                }
            });

            if (isEditSave) {
                AnimationController.show(holder.mItemArrow);
                AnimationController.transparent(holder.mItemButton);
            } else {
                AnimationController.transparent(holder.mItemArrow);
                AnimationController.show(holder.mItemButton);
            }
            return convertView;
        }

        class ViewHolder {

            LinearLayout mScenerModeList;
            ImageView mScenerModeIcon;
            ImageView mItemArrow;
            TextView mScenerModeName;
            ImageView mItemButton;
            Button scener_addmode_bt;

            public ViewHolder(View view) {

                mScenerModeList = (LinearLayout) view.findViewById(R.id.scener_mode_list);
                mScenerModeIcon = (ImageView) view.findViewById(R.id.scener_mode_icon);
                mItemArrow = (ImageView) view.findViewById(R.id.scener_item_arrow);
                mItemButton = (ImageView) view.findViewById(R.id.scener_item_button);
                scener_addmode_bt = (Button) view.findViewById(R.id.scener_addmode_bt);
                mScenerModeName = (TextView) view.findViewById(R.id.scener_mode_name);

                view.setTag(this);
            }
        }
    }

    public int setIcon(int num) {
        switch (num) {
            case 0: //1_1 scener_sports_checked
                return num = R.drawable.scener_sports_checked;
            case 1: //1_2
                return num = R.drawable.scener_gohome_checked;
            case 2: //1_3
                return num = R.drawable.scener_leavehome_checked;
            case 3:  //1_4
                return num = R.drawable.scener_lady_checked;
            case 4:  //2_1
                return num = R.drawable.scener_movie_checked;
            case 5:  //2_2
                return num = R.drawable.scener_sleep_checked;
            case 6:    //2_3
                return num = R.drawable.scener_eat_checked;
            case 7:    //2_4
                return num = R.drawable.scener_receive_checked;
            case 8:      //3_1
                return num = R.drawable.scener_clean_checked;
            case 9:   //3_2
                return num = R.drawable.scener_meet_checked;
            case 10:   //3_3
                return num = R.drawable.scener_read_checked;
            case 11:  //3_4
                return num = R.drawable.scener_music_checked;
            default:
                return num = R.drawable.scener_music_checked;
        }
    }


    class scenerlistviewClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //1.判断是否是编辑状态 2.点击跳转 3.携带情景模式id sceneid
            SceneMode mode = mSceneModeList.get(position - 1);
            if (isEditSave) {
                openActivity(ScenerDeviceActivity.class, mode, mActivity, false);
            } else {
                //
                if (MyApplication.getIsSendScener()) {
//                T.showShort(mContext,"发送");
                    MyApplication.setIsSendScener(false);
                    MyApplication.setIsSaveDevice(true);
                    CmdBaseActivity.getInstance().openScener(mode.id, true);
                }


            }
        }
    }
}
