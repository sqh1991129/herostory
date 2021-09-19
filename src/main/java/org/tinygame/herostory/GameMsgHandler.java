package org.tinygame.herostory;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

public class GameMsgHandler extends SimpleChannelInboundHandler<Object> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        if (null == ctx ||
            null == msg ){
            return;
        }
        LOGGER.info("收到客户端消息：msgClass={},msg={}",msg.getClass().getSimpleName(),msg);
        //用户入场消息
        if (msg instanceof GameMsgProtocol.UserEntryCmd){
            GameMsgProtocol.UserEntryCmd userEntryCmd = (GameMsgProtocol.UserEntryCmd) msg;
            int userId = userEntryCmd.getUserId();
            String heroAvatar = userEntryCmd.getHeroAvatar();
            //获取用户信息并返回
            GameMsgProtocol.UserEntryResult.Builder builder = GameMsgProtocol.UserEntryResult.newBuilder();
            builder.setUserId(userId);
            builder.setHeroAvatar(heroAvatar);
            GameMsgProtocol.UserEntryResult result =  builder.build();
            ctx.writeAndFlush(result);
        }
        //谁在线
        if (msg instanceof GameMsgProtocol.WhoElseIsHereCmd){
            GameMsgProtocol.WhoElseIsHereResult.Builder builder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();
            GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
            userInfoBuilder.setUserId(1);
            userInfoBuilder.setHeroAvatar("Hero_Hammer");
            builder.addUserInfo(userInfoBuilder.build());
            GameMsgProtocol.WhoElseIsHereResult result = builder.build();
            ctx.writeAndFlush(result);
        }

    }

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {

    }
}
