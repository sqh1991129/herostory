package org.tinygame.herostory;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * netty的广播
 * @author sunquanhu
 */
public class Broadcaster {

    /**
     * 信道组, 注意这里一定要用 static,
     * 否则无法实现群发
     */
    private static ChannelGroup _channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private Broadcaster(){}

    /**
     * 添加信息
     */
    public static void addChannel(Channel channel){
        if (null != _channelGroup){
            _channelGroup.add(channel);
        }
    }
    /**
     * 广播消息
     */
    public static void broadcast(Object msg){
        if (null != _channelGroup){
            System.out.println("广播消息");
            _channelGroup.writeAndFlush(msg);
        }
    }
}
