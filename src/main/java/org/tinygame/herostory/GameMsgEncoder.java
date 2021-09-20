package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 自定义消息解码器
 *
 * @author sunquanhu
 */
public class GameMsgEncoder extends ChannelHandlerAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgEncoder.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        try {

            if (null == ctx || null == msg || null == promise) {
                return;
            }

            if (!(msg instanceof GeneratedMessageV3)) {

                super.write(ctx, msg, promise);
                return;
            }
            int msgCode = GameMsgRecognizer.getMsgCode(msg.getClass());
            LOGGER.info("msgCode={}",msgCode);
            byte[] msgBody = ((GeneratedMessageV3) msg).toByteArray();
            ByteBuf byteBuf = ctx.alloc().buffer();
            byteBuf.writeShort(msgBody.length);
            byteBuf.writeShort(msgCode);
            byteBuf.writeBytes(msgBody);
            BinaryWebSocketFrame webSocketFrame = new BinaryWebSocketFrame(byteBuf);
            super.write(ctx, webSocketFrame, promise);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
