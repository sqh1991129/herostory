package org.tinygame.herostory;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.cmdhandler.CmdHandlerFactory;
import org.tinygame.herostory.cmdhandler.ICmdHandler;

/**
 * 消息处理
 * @author sunquanhu
 */
public class MainMsgProcess {

    private static final Logger LOGGER = LoggerFactory.getLogger(MainMsgProcess.class);

    /**
     * 处理消息
     */
   static void  process(ChannelHandlerContext ctx, Object msg){

       final Class<?> msgClazz = msg.getClass();
       LOGGER.info(
               "收到客户端消息, msgClazz = {}, msgObj = {}",
               msgClazz.getSimpleName(),
               msg
       );

       //获取消息处理器
      ICmdHandler<? extends GeneratedMessageV3> iCmdHandler =  CmdHandlerFactory.create(msgClazz);
      if (null == iCmdHandler){
          return;
      }
      iCmdHandler.handler(ctx,cast(msg));
    }


    /**
     * 转型为命令对象
     */
    private static <TCmd extends GeneratedMessageV3> TCmd cast(Object msg){
        if (!(msg instanceof GeneratedMessageV3)) {
            return null;
        } else {
            return (TCmd) msg;
        }
    }
}
