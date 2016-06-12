package com.vanhitech.vanhitech.utils;

import java.io.Closeable;
import java.io.IOException;

/**
 * 创建者     heyn
 * 创建时间   2016/3/9 17:09
 * 描述	      ${TODO}
 * 更新者     $Author$
 * 更新时间   $Date$
 * 更新描述   ${TODO}
 */
public class IOUtils {

    /** 关闭流 */
    public static boolean close(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (IOException e) {
                LogUtils.e(e);
            }
        }
        return true;
    }
}
