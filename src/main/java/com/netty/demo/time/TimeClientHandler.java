package com.netty.demo.time;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 *
 * Author 卡卡
 * Created by jing on 2017/3/31.
 */
public class TimeClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//        ByteBuf m = (ByteBuf) msg;
//        try {
//            long currentTimeMillis = (m.readUnsignedInt() - 2208988800L) * 1000L;
//            System.out.println(new Date(currentTimeMillis));
//            ctx.close();
//        } finally {
//            m.release();
//        }

        UnixTime m = (UnixTime) msg;
        System.out.println(m);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
