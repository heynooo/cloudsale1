package com.vanhitech.vanhitech.controller.setting;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.webkit.WebView;
import android.widget.ImageView;

import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 14:42
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class SettingExpQuestionActivity extends BaseActivityController {
    private ImageView backB;

    private WebView webView;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_set_expquestion);
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("常见问题");
//        ActivityTitleManager.getInstance().changeTitle("常见问题");

        webView = (WebView) findViewById(R.id.helpWeb);
        webView.loadUrl("file:///android_asset/help.html");


    }

    @Override
    public void initData() {
        super.initData();

    }

    @Override
    public void initEvent() {
        super.initEvent();

    }


}
