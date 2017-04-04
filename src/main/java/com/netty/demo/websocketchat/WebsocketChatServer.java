package com.netty.demo.websocketchat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Author 卡卡
 * Created by jing on 2017/4/4.
 */
public class WebsocketChatServer {

    private int port;

    public WebsocketChatServer(int port) {
        this.port = port;
    }

    public void run() throws Exception{
        EventLoopGroup bossGroup = new NioEventLoopGroup();  //1
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();  //2
            bootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)  //3
                    .childHandler(new WebsocketChatServerInitializer())  //4
                    .option(ChannelOption.SO_BACKLOG, 128)              //5
                    .childOption(ChannelOption.SO_KEEPALIVE, true);    //6

            System.out.println("WebsocketChatServer启动了");

            // 绑定端口，开始接收进来的连接
            ChannelFuture future = bootstrap.bind(port).sync();    //7
            // 等待服务器 socket 关闭 。
            // 在这个例子中，这不会发生，但你可以优雅地关闭你的服务器
            future.channel().closeFuture().sync();
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();

            System.out.println("WebsocketChatServer关闭了");
        }
    }

    public static void main(String[] args) throws Exception {
        int port;
        if (args.length > 0){
            port = Integer.parseInt(args[0]);
        }else {
            port = 8080;
        }
        new WebsocketChatServer(port).run();
    }
}
