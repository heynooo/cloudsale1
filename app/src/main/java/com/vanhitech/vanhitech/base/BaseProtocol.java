package com.vanhitech.vanhitech.base;

import com.vanhitech.protocol.object.device.Device;

import java.io.IOException;
import java.util.List;

/**
 * 创建者
 * 创建时间
 * 描述
 * 更新者
 * 更新时间
 * 更新描述
 */
public abstract class BaseProtocol<T> {


    /**
     * @param
     * @return
     * @throws IOException
     */
    public List<Device> loadData() throws IOException {

        //1.从内存
        MyApplication app = (MyApplication) MyApplication.getContext();
        List<Device> list = app.getDevices();


        return list;
    }

}
