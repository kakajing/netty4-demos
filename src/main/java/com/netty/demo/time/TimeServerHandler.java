package com.netty.demo.time;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Author 卡卡
 * Created by jing on 2017/3/31.
 */
public class TimeServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
//        final ByteBuf time = ctx.alloc().buffer(4);
//        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
//
//        final ChannelFuture future = ctx.writeAndFlush(time);
//        future.addListener(new ChannelFutureListener() {
//            @Override
//            public void operationComplete(ChannelFuture channelFuture) throws Exception {
//                assert future == channelFuture;
//                ctx.close();
//            }
//        });

        ChannelFuture future = ctx.writeAndFlush(new UnixTime());
        future.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
