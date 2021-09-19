package org.tinygame.herostory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务启动主类
 * @author sunquanhu
 */
public class ServerMain {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerMain.class);

    public static void main(String[] args) {

        //初始化数据库连接
        MysqlSessionFactory.init();

        //老板
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //工人
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        //服务端
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup,workerGroup);
        //服务端信道的处理方式
        serverBootstrap.channel(NioServerSocketChannel.class);
        //信道处理方式
        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(
                        //HTTP服务器解码器
                        new HttpServerCodec(),
                        //内容长度
                        new HttpObjectAggregator(65535),
                        //WebSocket 协议处理器, 在这里处理握手、ping、pong 等消息
                        new WebSocketServerProtocolHandler("/websocket"),
                        //有顺序，解码器,编码器必须在前
                        new GameMsgDecoder(),
                        new GameMsgEncoder(),
                        new GameMsgHandler()
                );

            }
        });
        try {
            //设置服务端端口
          ChannelFuture future =  serverBootstrap.bind(2000).sync();
          if (future.isSuccess()){
              LOGGER.info("服务端启动成功");
          }
          //等待服务器信道关闭
          future.channel().closeFuture().sync();
        }catch (Exception e){
           LOGGER.error(e.getMessage(),e);
        }finally {
            LOGGER.info("服务端启动关闭");

            //关闭服务器
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }



    }
}
