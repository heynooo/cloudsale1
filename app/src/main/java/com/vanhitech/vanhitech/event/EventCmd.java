package com.vanhitech.vanhitech.event;

import com.vanhitech.protocol.ClientCMDHelper;
import com.vanhitech.protocol.cmd.ServerCommand;

/**
 * 当前类注释:EventBus测试 First事件类
 */
public class EventCmd  implements ClientCMDHelper.CommandListener {
     private ServerCommand cmd;

    public ServerCommand getMsg() {
        return cmd;
    }

    public void setMsg(ServerCommand cmd) {
        this.cmd = cmd;
    }

    public EventCmd(ServerCommand cmd){
         this.cmd=cmd;
     }

    @Override
    public void onReceiveCommand(ServerCommand serverCommand) {

    }

    @Override
    public void onSocketConnected() {

    }

    @Override
    public void onSocketClosed() {

    }
}
