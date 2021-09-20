package org.tinygame.herostory.cmdhandler;

import io.netty.channel.ChannelHandlerContext;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * 谁在线
 * @author sunquanhu
 */
public class WhoElseIsHereCmdHandler implements ICmdHandler<GameMsgProtocol.WhoElseIsHereCmd>{

    @Override
    public void handler(ChannelHandlerContext ctx, GameMsgProtocol.WhoElseIsHereCmd msg) {
        GameMsgProtocol.WhoElseIsHereResult.Builder builder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();
        GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
        userInfoBuilder.setUserId(1);
        userInfoBuilder.setHeroAvatar("Hero_Hammer");
        builder.addUserInfo(userInfoBuilder);
        GameMsgProtocol.WhoElseIsHereResult result = builder.build();
        ctx.writeAndFlush(result);
    }
}
