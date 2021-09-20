package org.tinygame.herostory.cmdhandler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;

/**
 * cmd消息处理器接口
 * @author sunquanhu
 */
public interface ICmdHandler<TCmd extends GeneratedMessageV3> {

    /**
     * 处理消息
     * @param ctx  channel上下文
     * @param msg  消息
     */
    void handler(ChannelHandlerContext ctx, TCmd msg);


}
