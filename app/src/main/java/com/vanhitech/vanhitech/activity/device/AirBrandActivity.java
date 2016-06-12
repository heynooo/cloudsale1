package com.vanhitech.vanhitech.activity.device;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.adapter.AirBrandAdapter;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;

public class AirBrandActivity extends BaseActivityController {
	public static final String str[] = new String[] { "兰谷", "格力", "美的/东芝",
			"长虹", "志高", "松下/乐声", "海尔", "三菱", "格兰仕", "科龙/华宝", "奥克斯", "夏普/声宝",
			"大金", "海信", "富士通", "华凌", "LG", "日立", "TCL", "三洋", "惠而浦", "乐华",
			"伊莱克斯", "约克", "澳柯玛", "春兰", "新科", "三星", "开利", "蓝波", "新飞 ", "麦克维尔",
			"汇丰", "南风 ", "大宇", "先科", "胜风", "扬子", "万宝", "波尔卡", "天元", "东宝", "飞鹿",
			"小鸭", "双鹿", "凉宇", "小天鹅", "索华", "朗歌", "皇冠", "众力", "塔兰迪", "雾峰", "杂牌",
			"其它品牌" };
	private AirBrandAdapter adapter;
	private boolean[] isChoose;
	private ListView lv_air_list;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);


	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		super.onResume();
	}
	@Override
	public void initView() {
		setContentView(R.layout.activity_airname_list);


		ActivityTitleManager.getInstance().init(this);
		ActivityTitleManager.getInstance().showCommonTitle();
		ActivityTitleManager.getInstance().changeTitle("请选中空调品牌");

		// TODO Auto-generated method stub
		lv_air_list = (ListView) findViewById(R.id.lv_air_list);

	}
	@Override
	public void initData() {
		super.initData();
		isChoose = new boolean[str.length];
		isChoose[0] = true;
	}
	public void initEvent() {
		super.initEvent();
		adapter = new AirBrandAdapter(str, this, isChoose);
		lv_air_list.setAdapter(adapter);

	}

	@Override
	public void initListener() {
		super.initListener();
		ActivityTitleManager.getInstance().goback.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mActivity.finish();
			}
		});
	}
}
