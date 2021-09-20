package org.tinygame.herostory;


import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 消息识别器
 * @author sunquanhu
 */
public class GameMsgRecognizer {

    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GameMsgRecognizer.class);

    /**
     * msgCode 对应的Message类
     */
    private static final Map<Integer,GeneratedMessageV3> _msgCodeAndMsgObjMap = new ConcurrentHashMap<>();

    /**
     * message 对应的msgCode类
     */
    private static final Map<Class<?>,Integer> _msgObjAndMsgCodeMap = new ConcurrentHashMap<>();


    /**
     * 私有化默认构造器
     */
    private GameMsgRecognizer(){}


    /**
     * 初始化消息识别器
     * case GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE:
     *                     messageV3 = GameMsgProtocol.UserEntryCmd.parseFrom(msgBody);
     */
//    public static void init(){
//        // 获取内部类
//        Class<?>[] innerClazzArray = GameMsgProtocol.class.getDeclaredClasses();
//
//        for (Class<?> innerClazz : innerClazzArray) {
//            if (null == innerClazz ||
//                    !GeneratedMessageV3.class.isAssignableFrom(innerClazz)) {
//                // 如果不是消息类,
//                continue;
//            }
//
//            // 获取类名称并小写
//            String clazzName = innerClazz.getSimpleName();
//            clazzName = clazzName.toLowerCase();
//
//            for (GameMsgProtocol.MsgCode msgCode : GameMsgProtocol.MsgCode.values()) {
//                if (null == msgCode) {
//                    continue;
//                }
//
//                // 获取消息编码
//                String strMsgCode = msgCode.name();
//                strMsgCode = strMsgCode.replaceAll("_", "");
//                strMsgCode = strMsgCode.toLowerCase();
//
//                if (!strMsgCode.startsWith(clazzName)) {
//                    continue;
//                }
//
//                try {
//                    // 相当于调用 UserEntryCmd.getDefaultInstance();
//                    Object returnObj = innerClazz.getDeclaredMethod("getDefaultInstance").invoke(innerClazz);
//
//                    LOGGER.info(
//                            "{} <==> {}",
//                            innerClazz.getName(),
//                            msgCode.getNumber()
//                    );
//
//                    _msgCodeAndMsgObjMap.put(
//                            msgCode.getNumber(),
//                            (GeneratedMessageV3) returnObj
//                    );
//
//                    _msgObjAndMsgCodeMap.put(
//                            innerClazz,
//                            msgCode.getNumber()
//                    );
//                } catch (Exception ex) {
//                    // 记录错误日志
//                    LOGGER.error(ex.getMessage(), ex);
//                }
//            }
//        }
//    }
   public static void init(){

       //获取所有内部类
      Class[] innerClassArrays =  GameMsgProtocol.class.getDeclaredClasses();
       //遍历说有内部类
       for (Class<?> innerClazz : innerClassArrays){
           //如果不是消息类
           if (null == innerClazz ||
                   !GeneratedMessageV3.class.isAssignableFrom(innerClazz)){
               continue;
           }
           //获取类名
           String clazzName = innerClazz.getSimpleName();
           //类名统一转换为小写
           clazzName = clazzName.toLowerCase();
           //遍历所有的msgCode
           for (GameMsgProtocol.MsgCode msgCode : GameMsgProtocol.MsgCode.values()){
               if (null == msgCode){
                   return;
               }
               //获取msgCode的name,并替换其中的"_"后统一转换为小写
               String msgCodeName = msgCode.name();
               msgCodeName = msgCodeName.replaceAll("_","").toLowerCase();
               //判断内部类是否是根据msgCodeName开头
               if (!msgCodeName.startsWith(clazzName)){
                   continue;
               }
               try {
                   //调用内部类的getDefaultInstance方法生成对象,相当于调用 UserEntryCmd.getDefaultInstance()
                  Object returnObj = innerClazz.getDeclaredMethod("getDefaultInstance").invoke(innerClazz);
                  LOGGER.info("当前msgCode:{},类名：{}",msgCode.getNumber(),innerClazz.getName());
                   _msgCodeAndMsgObjMap.put(msgCode.getNumber(), (GeneratedMessageV3) returnObj);
                   _msgObjAndMsgCodeMap.put(innerClazz,msgCode.getNumber());

               }catch (Exception e){
                   LOGGER.error(e.getMessage(),e);
               }
           }
       }




   }
    /**
     * 根据msgCode识别消息构建器
     */
    public static Message.Builder msgRecognizer(int msgCode){
        if (msgCode == -1){
            return null;
        }
        GeneratedMessageV3 generatedMessageV3 =  _msgCodeAndMsgObjMap.get(msgCode);
       if (null == generatedMessageV3){
           return null;
       }
       return generatedMessageV3.newBuilderForType();
    }

    /**
     * 根据消息类获取对应的msgCode
     * @param clazz
     * @return
     */
    public static int getMsgCode(Class<?> clazz){
        if (null == clazz){
            return -1;
        }
        return _msgObjAndMsgCodeMap.get(clazz);
    }
}
