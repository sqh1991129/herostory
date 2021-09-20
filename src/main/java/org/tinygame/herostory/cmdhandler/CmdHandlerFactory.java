package org.tinygame.herostory.cmdhandler;

import com.google.protobuf.GeneratedMessageV3;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.util.PackageUtil;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * cmd处理工厂
 *
 * @author sunquanhu
 */
public class CmdHandlerFactory {

    /**
     * 日志对象
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(CmdHandlerFactory.class);

    private static final Map<Class<?>, ICmdHandler<? extends GeneratedMessageV3>> _handlerMap = new ConcurrentHashMap<>();

    /**
     * 初始化handler
     */
    public static void init() {
        LOGGER.info("开始初始化cmdHandler");
        //获取指定包名中的所有ICmdHandler的实现类
        Set<Class<?>> classSet = PackageUtil.listSubClazz(ICmdHandler.class.getPackage().getName(), true, ICmdHandler.class);
        for (Class<?> clazz : classSet) {
            if (null == clazz ||
                    //获取类中修饰符
                    0 != (clazz.getModifiers() & Modifier.ABSTRACT)) {
                continue;
            }
            //获取方法数组
            Method[] methods = clazz.getDeclaredMethods();
            // 消息类型
            Class<?> cmdClazz = null;
            //循环数组
            for (Method method : methods) {
                //判断当前方法是否是handler
                if (null == method ||
                        !method.getName().equals("handler")) {
                    continue;
                }
                //获取方法的参数类型
                Class<?>[] parameterTypes = method.getParameterTypes();
                //判断是否是想要的handler方法handler(ChannelHandlerContext ctx, Object msg)
                if (parameterTypes.length < 2 ||
                        parameterTypes[1] == GeneratedMessageV3.class ||
                        !GeneratedMessageV3.class.isAssignableFrom(parameterTypes[1])) {
                    continue;
                }
                cmdClazz = parameterTypes[1];
                break;
            }
            //
            if (null == cmdClazz) {
                continue;
            }
            //创建cmd实例
            try {
                ICmdHandler<? extends GeneratedMessageV3> iCmdHandler = (ICmdHandler<? extends GeneratedMessageV3>) clazz.newInstance();
                LOGGER.info(
                        "{} <==> {}",
                        cmdClazz.getName(),
                        clazz.getName()
                );
                _handlerMap.put(cmdClazz, iCmdHandler);

            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
            LOGGER.info("结束初始化cmdHandler");
        }

    }

    /**
     * 创建cmdHandler
     *
     * @return
     */
    public static ICmdHandler<? extends GeneratedMessageV3> create(Class<?> clazz) {
        return _handlerMap.get(clazz);
    }
}
