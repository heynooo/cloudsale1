package com.vanhitech.vanhitech.event;

/**
 * 当前类注释:EventBus测试 First事件类
 */
public class EventFirst {
     private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public EventFirst(String msg){
         this.msg=msg;
     }
}
