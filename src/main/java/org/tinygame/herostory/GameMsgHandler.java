package org.tinygame.herostory;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (null == ctx){
            return;
        }
        super.channelActive(ctx);
        Broadcaster.addChannel(ctx.channel());

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (null == ctx ||
            null == msg ){
            return;
        }
        //添加channel
        MainMsgProcess.process(ctx, msg);

    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {

    }
}
