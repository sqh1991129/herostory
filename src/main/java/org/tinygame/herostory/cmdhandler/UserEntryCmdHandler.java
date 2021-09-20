package org.tinygame.herostory.cmdhandler;


import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.Broadcaster;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * 用户入场消息处理
 * @author sunquanhu
 */
public class UserEntryCmdHandler implements ICmdHandler<GameMsgProtocol.UserEntryCmd> {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserEntryCmdHandler.class);
    @Override
    public void handler(ChannelHandlerContext ctx, GameMsgProtocol.UserEntryCmd msg) {
        int userId = msg.getUserId();
        LOGGER.info("用户编号：{}",userId);
        ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);
        //获取
        int sessionId = (int) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        LOGGER.info("缓存中的用户编号：{}",sessionId);
        String heroAvatar = msg.getHeroAvatar();
        //获取用户信息并返回
        GameMsgProtocol.UserEntryResult.Builder builder = GameMsgProtocol.UserEntryResult.newBuilder();
        builder.setUserId(userId);
        builder.setHeroAvatar(heroAvatar);
        GameMsgProtocol.UserEntryResult result = builder.build();
        //ctx.writeAndFlush(result);
        //广播消息
        Broadcaster.broadcast(result);

    }
}

