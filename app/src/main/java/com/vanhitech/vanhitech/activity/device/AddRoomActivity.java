package com.vanhitech.vanhitech.activity.device;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.vanhitech.protocol.cmd.ServerCommand;
import com.vanhitech.protocol.cmd.client.CMD68_EditGroup;
import com.vanhitech.protocol.cmd.server.CMD69_ServerEditGroyp;
import com.vanhitech.protocol.object.GroupInfo;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.adapter.ArticleAdapter;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.base.CmdBaseActivity;
import com.vanhitech.vanhitech.base.MyApplication;
import com.vanhitech.vanhitech.bean.Place;
import com.vanhitech.vanhitech.conf.LoginConstants;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.utils.DataUtils;
import com.vanhitech.vanhitech.utils.LogUtils;
import com.vanhitech.vanhitech.utils.StringUtils;
import com.vanhitech.vanhitech.utils.T;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 14:42
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class AddRoomActivity extends BaseActivityController implements ViewPager.OnPageChangeListener {
    private ViewPager mHomePagerViewpager;
    private EditText mRoomName;
    private ArrayList<Place> mRoomList;
    private int mPosition;
    private String mName;
    private Context mContext;
    private BitmapUtils mBitmapUtils;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_device_addroom);
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changehelpText("保存");
        ActivityTitleManager.getInstance().changeTitle("添加房间");
        assignViews();
        mContext = this;
    }

    @Override
    public void initData() {
        super.initData();
        mRoomList = new ArrayList();//"assets/img/roottemp.bmp"
        Place placeRoom1 = new Place("assets/img/img_room_bedroom.bmp",10+"", "默认卧室");
        Place placeRoom2 = new Place("assets/img/img_room_balcony.bmp",11+"", "默认阳台");
        Place placeRoom3 = new Place("assets/img/img_room_kitchen.bmp",12+"", "默认厨房");
        Place placeRoom4 = new Place("assets/img/img_room_living.bmp",13+"", "默认客厅");
        Place placeRoom5 = new Place("assets/img/img_room_restaunt.bmp",14+"", "默认餐厅");
        Place placeRoom6 = new Place("assets/img/img_room_study.bmp",15+"", "默认书房");

        mRoomList.add(placeRoom1);
        mRoomList.add(placeRoom2);
        mRoomList.add(placeRoom3);
        mRoomList.add(placeRoom4);
        mRoomList.add(placeRoom5);
        mRoomList.add(placeRoom6);

//        this.roomHashMap = GlobalData.roomHashMap;
//        this.ResId = localDefalulRoom1.getResId();

        mHomePagerViewpager.setOffscreenPageLimit(3);
        mHomePagerViewpager.setPageMargin((30));
        // 1.设置幕后item的缓存数目


        mHomePagerViewpager.setAdapter(new ArticleAdapter(AddRoomActivity.this,mRoomList));


        mHomePagerViewpager.setOnPageChangeListener(this);

    }

    @Override
    public void initEvent() {
        super.initEvent();
        setListener();
        mBitmapUtils = new BitmapUtils(AddRoomActivity.this);//图片加载处理
    }

    @Override
    public void  initListener(){
        super.initListener();
        ActivityTitleManager.getInstance().goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mActivity.finish();
            }
        });
        ActivityTitleManager.getInstance().help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mName = mRoomName.getText().toString().trim();
                if (StringUtils.isEmpty(mName)) {
                    T.showShort(mContext, "房间名称不能为空，请先填写!");
                    return;
                }
                Place place = mRoomList.get(mPosition);
                place.place=mName;
                place.userid=LoginConstants.USERNAME;
                /*
                id 根据数量的多少+1 了 不确定是否正确 不确定是否需要+1
                 */
//                try {
//                    int id=  (int)DataUtils.getInstance().mDb.count(Place.class);
//                    place.id=id+"";
//                } catch (DbException e) {
//                    e.printStackTrace();
//                }


                try {
                    DataUtils.getInstance().savePlaceRoom(place);
                } catch (DbException e) {
                    e.printStackTrace();
                }
                //发送添加网络命令 无效

                List<GroupInfo> groups = new ArrayList<GroupInfo>();
                GroupInfo groupInfo = new GroupInfo(null,mName,place.hashCode());
                groups.add(groupInfo);
                CMD68_EditGroup cmd68 = new CMD68_EditGroup("add",groups);
                CmdBaseActivity.getInstance().mHelper.setCommandListener(AddRoomActivity.this);
                CmdBaseActivity.getInstance().sendCmd(cmd68);
                MyApplication.setIsbrushRoom(true);
                MyApplication.setIsbrushStart(true);
                mActivity.finish();
                //保存到数据库, 存在就覆盖
            }
        });
        //OnPageChangeListener
    }

    @Override
    public void onReceiveCommand(ServerCommand cmd) {
        super.onReceiveCommand(cmd);
        switch (cmd.CMDByte) {
            case CMD69_ServerEditGroyp.Command:
                CMD69_ServerEditGroyp cmd69 = (CMD69_ServerEditGroyp)cmd;
                List<GroupInfo> list= cmd69.groups;
                for(GroupInfo g:list){
                    DataUtils.getInstance().save(g);
                }

                break;


        }
    }

    private void setListener() {

    }

    private void assignViews() {
        mHomePagerViewpager = (ViewPager) findViewById(R.id.home_pager_viewpager);
        mRoomName = (EditText) findViewById(R.id.roomName);
    }




    private void viewpagerImager(String mIntentPlace, ImageView iv) {
        if (mIntentPlace != null)
            try {

                Place place = DataUtils.getInstance().mDb.findFirst(Selector.from(Place.class).where("place", "=", mIntentPlace.replaceAll(" ", "")));//TODO 这里的0要换成postion
                if (place != null)
                    if (null != place.url) {
                        mBitmapUtils.display(iv, place.url);//TODO
                    } else {
                        mBitmapUtils.display(iv, "assets/img/roottemp.bmp");//TODO
//                        LogUtils.i("显示默认图片");

                    }
            } catch (DbException e) {
                e.printStackTrace();
            }
    }


    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override ////当某页被选择
    public void onPageSelected(int position) {
        LogUtils.i("onPageSelected2222222222222222"+position);
        mPosition=position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

}
