package com.vanhitech.vanhitech;

import android.test.AndroidTestCase;

import com.vanhitech.vanhitech.utils.FileUtils;
import com.vanhitech.vanhitech.utils.LogUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

/**
 * 创建者     heyn
 * 创建时间   2016/3/22 0:22
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class Testfile  extends AndroidTestCase {

    /**
     * 保存帐号
     */
    public void testFile(){
        String name ="2121";
        String pass ="2121";
        //返回值也是一个File对象，其路径是data/data/com.itheima.apirwinrom/cache
//        File file = new File("data/data/com.vanhitech.vanhitech/cache", "info.txt");
        File file = new File(FileUtils.getCachePath(), "infoa.txt");
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(file);
            fos.write((name + "##" + pass).getBytes());
            fos.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    /**
     * 读取帐号
     */
    public void testreadAccount(){

//    	File file = new File(getFilesDir(), "info.txt");
//        File file = new File(getCacheDir(), "info.txt");
        //"data/data/com.vanhitech.vanhitech/cache"

        File file = new File(FileUtils.getCachePath(), "infoa.txt");
        if(file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                //把字节流转换成字符流
                BufferedReader br = new BufferedReader(new InputStreamReader(fis));
                //读取txt文件里的用户名和密码
                String text = br.readLine();
                String[] s = text.split("##");
                    LogUtils.i(s[0]+"---------------"+s[1]);
//                et_name.setText(s[0]);
//                et_pass.setText(s[1]);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    /**
     * 工具测试
     */
        public  void testmVoid(){
//            LogUtils.i(FileUtils.getDir("aa---------------"));
//            LogUtils.i("----工具测试------",FileUtils.getCacheDir());
//            LogUtils.log2File("sasa",FileUtils.getCacheDir()+"/test.txt");
//           Boolean aBoolean= FileUtils.createDirs(FileUtils.getCachePath()+"test1");
//            LogUtils.i("----工具测试------",aBoolean+"");

//            LogUtils.log2File("sasasa", FileUtils.getCachePath()+"test1"+"1.txt");
//            LogUtils.i("----工具测试------",FileUtils.getCachePath()+"test1"+"1.txt");

            LogUtils.i(FileUtils.getCachePath()+"cache目录-----------");
            LogUtils.i(FileUtils.getCacheDir()+"获取缓存目录-------------");
            LogUtils.i(FileUtils.getDownloadDir()+"下载目录");
            LogUtils.i(FileUtils.getExternalStoragePath()+"SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的");
            LogUtils.i(FileUtils.getDir("test"));








            LogUtils.i("----工具测试------",FileUtils.getCachePath()+"test1"+"1.txt");
        }


}

