package com.vanhitech.vanhitech.controller.home;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.vanhitech.protocol.object.device.Device;
import com.vanhitech.vanhitech.R;
import com.vanhitech.vanhitech.base.BaseActivityController;
import com.vanhitech.vanhitech.base.MyApplication;
import com.vanhitech.vanhitech.bean.Place;
import com.vanhitech.vanhitech.bean.UpdateTime;
import com.vanhitech.vanhitech.conf.Constants;
import com.vanhitech.vanhitech.conf.LoginConstants;
import com.vanhitech.vanhitech.cropview.CorpActivity;
import com.vanhitech.vanhitech.cropview.FileSizeUtil;
import com.vanhitech.vanhitech.cropview.FontUtils;
import com.vanhitech.vanhitech.cropview.ImageUtil;
import com.vanhitech.vanhitech.manager.ActivityTitleManager;
import com.vanhitech.vanhitech.popupwindow.SelectPicPopupWindow;
import com.vanhitech.vanhitech.utils.DataUtils;
import com.vanhitech.vanhitech.utils.DateUtils;
import com.vanhitech.vanhitech.utils.FileUtils;
import com.vanhitech.vanhitech.utils.IOUtils;
import com.vanhitech.vanhitech.utils.LogUtils;
import com.vanhitech.vanhitech.views.LightRgbTagView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * 创建者     heyn
 * 创建时间   2016/3/18 14:42
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class HomeRoomInfoActivity extends BaseActivityController {
    public LightRgbTagView mLrt_tag;
    private PopupWindow mPw;
    private View mPopupView;
    private ImageView mPhoto;
    SelectPicPopupWindow photoWindow;
    private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int PICK_IMAGE_ACTIVITY_REQUEST_CODE = 200;
    private DbUtils mDb;
    private String mIntentPlace;
    private Place mPlace;
    private TextView mBedroor;
    private Button mRootdelete;
    private BitmapUtils mBitmapUtils;


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }

    @Override
    public void initView() {
        super.initView();
        setContentView(R.layout.activity_set_roominfo);
        mPhoto = (ImageView) findViewById(R.id.roominfo_photo);
        mBedroor = (TextView) findViewById(R.id.bedroor);
        mRootdelete = (Button) findViewById(R.id.rootdelete);//删除房间
        ActivityTitleManager.getInstance().init(this);
        ActivityTitleManager.getInstance().showCommonTitle();
        ActivityTitleManager.getInstance().changeTitle("房间信息");
        ActivityTitleManager.getInstance().changehelpText("");
        mBitmapUtils = new BitmapUtils(this);
        mBitmapUtils.configDefaultLoadFailedImage(R.mipmap.roottemp);
        //mIntentPlace
        Intent intent = getIntent();
        mIntentPlace = intent.getStringExtra("place");//
        //place

/**
 * 先从数据库取url ,判断是否一样,不一样则重新显示,一样则不做处理
 */bitmapDb();

    }

    /**
     * 保存图片地址到数据库,裁剪图片
     */
    private void bitmapDb() {
        //#######################方案2############################################

        mDb = DbUtils.create(this);
        //接收传过来的position
        try {
            //TODO 这里的0要换成postion
            mPlace = mDb.findFirst(Selector.from(Place.class).where("place", "=", mIntentPlace.replaceAll(" ", "")).and("userid", "=", LoginConstants.USERNAME)); //TODO 查下条件需要改变,换成place
            if (mPlace != null && mPlace.url != null) {
                mBitmapUtils.display(mPhoto, mPlace.url);
            } else {
                mBitmapUtils.display(mPhoto, "assets/img/roottemp.bmp");
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        //        // apply custom font
        FontUtils.setFont((ViewGroup) findViewById(R.id.layout_home_roominfo));
        //        // get cropped bitmap from Application
        Bitmap cropped = ((MyApplication) getApplication()).cropped;
        //        // set cropped bitmap to ImageView
        //保存url地址
        BufferedOutputStream out = null;
        try {
            //存自己目录下的一个文件夹好了  //TODO 不一定每次进来都要执行这里 ,判断cropped

            if (cropped != null) {//如果图片存在,就先删除图片,在生成
                File dump = new File(FileUtils.getDir("test"), mIntentPlace.replaceAll(" ", "") + ".png");//TODO 保存路径  这里保存路径带有中文
                if (dump.exists()) {
                    Boolean temp = dump.delete();
                    if (!dump.exists()) {
                        mBitmapUtils.clearCache();//清理缓存
                        Constants.updateViewpager = true;//标记一下,一会更新
                        UpdateTime time = new UpdateTime(0 + "", DateUtils.getCurTimeAddXM(25));
                        DataUtils.getInstance().save(time);
                    }
                    LogUtils.i(temp + "");
                }
                out = new BufferedOutputStream(new FileOutputStream(dump));
                if (cropped.compress(Bitmap.CompressFormat.PNG, 100, out)) {
//                    Toast.makeText(getApplication(), "保存成功" + dump.toString(), Toast.LENGTH_SHORT).show();
                    MyApplication.setIsbrushRoom(true);
                    MyApplication.setIsbrushStart(true);
                    Log.i("ResultActivity", dump.toString());//getPlaceUserid
                    mPlace=DataUtils.getInstance().getPlaceUserid(mIntentPlace.replaceAll(" ", ""),LoginConstants.USERNAME);
//                    mPlace = mDb.findFirst(Selector.from(Place.class).where("place", "=", mIntentPlace.replaceAll(" ", "")));//TODO 查找条件改成place
                    mPlace.url = dump.toString();
//                mDb.save(mPlace);
                    mDb.update(mPlace);
                    mPhoto.setImageBitmap(cropped);
                    ((MyApplication) getApplication()).cropped = null;
                } else {
                    Log.i("ResultActivity", "保存失败");
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DbException e) {
            e.printStackTrace();
        } finally {

            IOUtils.close(out);
        }


//            ((ImageView)findViewById(R.id.result_image)).setImageBitmap(cropped);

        ((MyApplication) getApplication()).cropped = null;
//###################################################################
    }

    @Override
    public void initData() {
        super.initData();


        mBedroor.setText(mIntentPlace);

    }

    @Override
    public void initEvent() {
        super.initEvent();

        //test--------------------------
        mPhoto.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                photoWindow = new SelectPicPopupWindow(HomeRoomInfoActivity.this, itemsOnClick);
                //显示窗口
                photoWindow.showAtLocation(HomeRoomInfoActivity.this.findViewById(R.id.layout_home_roominfo),
                        Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0,
                        0); //设置layout在PopupWindow中显示的位置
            }
        });


        mRootdelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
        //房间查找设备
                Boolean flg =false;
                List<Device> list = DataUtils.getInstance().getAllDevice();
                for(Device d:list){
                    if(d.place.equals(mIntentPlace)){
                        flg=true;
                    }
                }
                if (flg) {
                    //提示该房间有设备 不可以删除
                    remindDialogStyleTwo();
                } else {
                    deleteDialogStyleTwo(mIntentPlace);
                    //直接操作数据库
                    DataUtils.getInstance().deletePlace(mIntentPlace);
                    //还需要给服务器发送删除房间信息 无效
                   /*
                    List<GroupInfo> groups = new ArrayList<GroupInfo>();
                    GroupInfo groupInfo = new GroupInfo(null,mIntentPlace,0);
                    groups.add(groupInfo);
                    CMD68_EditGroup cmd68 = new CMD68_EditGroup("delete",groups);
                    CmdBaseActivity.getInstance().sendCmd(cmd68);
                    */

                    finishDialogStyleTwo();

                }

            }
        });

    }
    private void finishDialogStyleTwo() {

        final NormalDialog dialog = new NormalDialog(mContext);
        MyApplication.setIsbrushRoom(true);
        MyApplication.setIsbrushStart(true);
        dialog.content("删除成功，请下拉刷新下界面。")//
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
                        dialog.dismiss();  //TODO 有销毁问题 ！！

                        finish();
                    }
                });

    }

    private void remindDialogStyleTwo() {

        final NormalDialog dialog = new NormalDialog(mContext);

        dialog.content("该房间有设备,不可以删除。")//
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
                        dialog.dismiss();
                    }
                });

    }

    private void deleteDialogStyleTwo(final String place) {

        final NormalDialog dialog = new NormalDialog(mContext);

        dialog.content("是否确定删除房间。")//
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
                        DataUtils.getInstance().deletePlace(place);
                        MyApplication.setIsbrushRoom(true);
                        MyApplication.setIsbrushStart(true);
                        dialog.dismiss();
                    }
                });

    }

    private File mCaptureFile = null;
    private static final int REQUEST_CAPTURE_IMAGE = 0;
    //为弹出窗口实现监听类
    private View.OnClickListener itemsOnClick = new View.OnClickListener() {

        public void onClick(View v) {
            photoWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo: //相机
                    //#############方案2################
                    mCaptureFile = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM),
                            "" + new Date().getTime() + ".jpg");
                    Intent intenttake = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intenttake.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCaptureFile));
                    startActivityForResult(intenttake, REQUEST_CAPTURE_IMAGE);
                    break;
                case R.id.btn_pick_photo:  //相册
                    Toast.makeText(HomeRoomInfoActivity.this, "pickphoto", Toast.LENGTH_SHORT).show();
                    //##############方案2###############
                    Intent intentpick = new Intent();
                    intentpick.setAction(Intent.ACTION_PICK);//Pick an item from the data
                    intentpick.setType("image/*");//从所有图片中进行选择
                    startActivityForResult(intentpick, REQUEST_CAPTURE_IMAGE);
                    break;
                default:
                    break;
            }


        }

    };

    private static String picFileFullName;

    //拍照
    public void takePicture() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File outDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!outDir.exists()) {
                outDir.mkdirs();
            }
            File outFile = new File(outDir, System.currentTimeMillis() + ".jpg");
            picFileFullName = outFile.getAbsolutePath();
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(outFile));
            intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        } else {
            toast("请确认已经插入SD卡");
        }
    }

    //打开本地相册
    public void openAlbum() {
        //############################方案1#########################################

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        this.startActivityForResult(intent, PICK_IMAGE_ACTIVITY_REQUEST_CODE);
        //#############################方案2########################################

    }


    //设置照片
    private void setImageView(String realPath) {
        Bitmap bmp = BitmapFactory.decodeFile(realPath);
        int degree = readPictureDegree(realPath);
        if (degree <= 0) {
            mPhoto.setImageBitmap(bmp);
        } else {
            toast("rotate:" + degree);
            //创建操作图片是用的matrix对象
            Matrix matrix = new Matrix();
            //旋转图片动作
            matrix.postRotate(degree);
            //创建新图片
            Bitmap resizedBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);
            mPhoto.setImageBitmap(resizedBitmap);
        }
    }
//获取

    /**
     * This method is used to get real path of file from from uri<br/>
     * http://stackoverflow.com/questions/11591825/how-to-get-image-path-just-captured-from-camera
     *
     * @param contentUri
     * @return String
     */
    public String getRealPathFromURI(Uri contentUri) {
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            // Do not call Cursor.close() on a cursor obtained using this method,
            // because the activity will do that for you at the appropriate time
            Cursor cursor = this.managedQuery(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        }
    }

    /**
     * 读取照片exif信息中的旋转角度<br/>
     *
     * @param path 照片路径
     * @return角度
     */
    public static int readPictureDegree(String path) {
        int degree = 0;
        String pathtemp = path;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {//TODO
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 10;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 10;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 10;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    //###################方案2#######################################
    private final int minification = 2;//图片压缩倍数

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CAPTURE_IMAGE) {
            try {
                Uri uri = data.getData();
                if (uri != null) {
                    Cursor cr = getContentResolver().query(uri,
                            new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                    if (cr.moveToFirst()) {
                        File file = new File(cr.getString(cr.getColumnIndex(MediaStore.Images.Media.DATA)));//获取照片地址
                        Bitmap bitmap = ImageUtil.loadBitmap(file, minification);//TODO

                        setBitmapJumpActivity(bitmap);
                        Log.i("resulteeeee", "1 " + FileSizeUtil.getFileOrFilesSize(file.getAbsolutePath(), FileSizeUtil.SIZETYPE_KB));
                    }
                    cr.close();
                } else {
                    Bundle bundle = data.getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    Log.i("resulteeeee", "2 " + FileSizeUtil.getBitmapsize(bitmap, FileSizeUtil.SIZETYPE_KB));
                    setBitmapJumpActivity(ImageUtil.changeBitmap(bitmap,
                            bitmap.getWidth() / minification, bitmap.getHeight() / minification));
                }

            } catch (Exception e) {
                if (mCaptureFile != null && mCaptureFile.exists()) {
                    Log.i("resulteeeee", "3");
                    Log.i("resulteeeee", "1 " + FileSizeUtil.getFileOrFilesSize(mCaptureFile.getAbsolutePath(), FileSizeUtil.SIZETYPE_KB));

                    Bitmap bitmap = ImageUtil.loadBitmap(mCaptureFile, minification);
                    setBitmapJumpActivity(bitmap);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setBitmapJumpActivity(Bitmap bitmap) {

        ((MyApplication) getApplication()).cropped = getRoundImage(bitmap, 1);//getRoundImage(bitmap,12);是想做圆角处理,没有实现
        Intent intent = new Intent(this, CorpActivity.class);
        intent.putExtra("place", mIntentPlace + ""); //TODO 这里也需要修改成place
        startActivity(intent);
        overridePendingTransition(R.animator.nextenteranim, R.animator.nextexitanim);
        finish();
    }

    /**
     * TODO<图片圆角处理>
     *
     * @param srcBitmap 源图片的bitmap
     * @param ret       圆角的度数
     * @return Bitmap
     * @throw
     */
    public static Bitmap getRoundImage(Bitmap srcBitmap, float ret) {

        if (null == srcBitmap) {

            return null;
        }

        int bitWidth = srcBitmap.getWidth();
        int bitHight = srcBitmap.getHeight();

        BitmapShader bitmapShader = new BitmapShader(srcBitmap,
                Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(bitmapShader);

        RectF rectf = new RectF(0, 0, bitWidth, bitHight);

        Bitmap outBitmap = Bitmap.createBitmap(bitWidth, bitHight,
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(outBitmap);
        canvas.drawRoundRect(rectf, ret, ret, paint);
        canvas.save();
        canvas.restore();

        return outBitmap;
    }


}
