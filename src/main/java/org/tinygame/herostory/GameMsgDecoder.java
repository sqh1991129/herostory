package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.InvalidProtocolBufferException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * 自定义消息解码器
 *
 * @author sunquanhu
 */
public class GameMsgDecoder extends ChannelHandlerAdapter {

    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgDecoder.class);


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {

        try {


            if (null == ctx ||
                    null == msg) {
                return;
            }

            //不是websocket消息
            if (!(msg instanceof BinaryWebSocketFrame)) {
                return;
            }
            LOGGER.info("收到消息："+msg);
            //读取消息
            BinaryWebSocketFrame webSocketFrame = (BinaryWebSocketFrame) msg;
            ByteBuf byteBuf = webSocketFrame.content();

            //读取消息长度
            byteBuf.readShort();
            //读取消息编号
            int msgCode = byteBuf.readShort();
            //读取消息内容
            byte[] msgBody = new byte[byteBuf.readableBytes()];
            byteBuf.readBytes(msgBody);

            GeneratedMessageV3 messageV3 = null;
            switch (msgCode) {
                case GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE:
                    messageV3 = GameMsgProtocol.UserEntryCmd.parseFrom(msgBody);
                case GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE:
                    messageV3 = GameMsgProtocol.WhoElseIsHereCmd.parseFrom(msgBody);
                default:
                    break;
            }
            if (null != messageV3) {
                ctx.fireChannelRead(messageV3);
            }
        } catch (Exception e) {

            LOGGER.error(e.getMessage(),e);

        }

    }
}
