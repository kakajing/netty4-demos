package com.netty.demo.websocketchat;

import io.netty.channel.*;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedNioFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * 处理 Http 请求
 *
 * Author 卡卡
 * Created by jing on 2017/4/4.
 */

                                    //扩展 SimpleChannelInboundHandler 用于处理 FullHttpRequest信息
public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    private final String wsUri;
    private static final File INDEX;

    static {
        URL location = HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
        try {
            String path = location.toURI() + "WebsocketChatCLient.html";
            path = !path.contains("file:") ? path : path.substring(5);
            INDEX = new File(path);
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Unable to locate WebsocketChatClient.html", e);
        }
    }

    public HttpRequestHandler(String wsUri){
        this.wsUri = wsUri;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
        if (wsUri.equalsIgnoreCase(request.getUri())){
            //如果请求是WebSocket升级，递增引用计数器（保留）并且将它传递给在ChannelPipeline中的下个ChannelInboundHandler
            ctx.fireChannelRead(request.retain());  //2
        }else {
            if (HttpHeaders.is100ContinueExpected(request)){
                send100Continue(ctx);   //3
            }

            //4 读取默认的 WebsocketChatClient.html 页面
            RandomAccessFile file = new RandomAccessFile(INDEX, "r");

            HttpResponse response = new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html;charset=UTF-8");

            boolean keepAlive = HttpHeaders.isKeepAlive(request);

            if (keepAlive){  //5
                response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, file.length());
                response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            }
            ctx.write(response);   //6

            //7 写index.html到客户端，判断SslHandler是否在ChannelPipeline来决定是使用DefaultFileRegion还是ChunkedNioFile
            if (ctx.pipeline().get(SslHandler.class) == null){
                ctx.write(new DefaultFileRegion(file.getChannel(), 0, file.length()));
            }else {
                ctx.write(new ChunkedNioFile(file.getChannel()));
            }

            //8 写并刷新 LastHttpContent 到客户端，标记响应完成
            ChannelFuture future = ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
            if (!keepAlive){
                future.addListener(ChannelFutureListener.CLOSE);   //9
            }
            file.close();
        }
    }

    private static void send100Continue(ChannelHandlerContext ctx){
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
        ctx.writeAndFlush(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel channel = ctx.channel();
        System.out.println("Client: " + channel.remoteAddress() + "异常");
        cause.printStackTrace();
        ctx.close();
    }
}
