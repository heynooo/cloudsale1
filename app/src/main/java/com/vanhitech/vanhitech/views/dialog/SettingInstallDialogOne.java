package com.vanhitech.vanhitech.views.dialog;

import android.content.Context;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.animation.Attention.Swing;
import com.flyco.dialog.widget.base.BaseDialog;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.utils.ViewFindUtils;


public class SettingInstallDialogOne extends BaseDialog<SettingInstallDialogOne> {
    public ImageView flashBall;
    public TextView tvConfigFlashBtn;

    public SettingInstallDialogOne(Context context) {
        super(context);
    }
    public SettingInstallDialogOne(Context context,Boolean flag) {
        super(context,flag);
    }

    @Override
    public View onCreateView() {
        widthScale(1f);
        heightScale(1f);
        superDismiss();

        showAnim(new Swing());

        // dismissAnim(this, new ZoomOutExit());
        View inflate = View.inflate(mContext, R.layout.activity_set_installdevice_ppw1, null);

        flashBall = ViewFindUtils.find(inflate,R.id.flash_ball);

        tvConfigFlashBtn =  ViewFindUtils.find(inflate,R.id.tv_config_flash_btn);


        return inflate;
    }

    @Override
    public void setUiBeforShow() {


        AlphaAnimation animation = new AlphaAnimation(0.2f, 1.0f);

        // 定义动画的执行时间
        animation.setDuration(100);

        // 指定循环播放这个动画
        animation.setRepeatCount(Animation.INFINITE);

        // 指定动画重复的时候，是倒退着播放
        animation.setRepeatMode(Animation.REVERSE);

        // 指定某个空间播放动画
        flashBall.startAnimation(animation);




    }
}
