package com.netty.demo.time;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * 解码器
 *
 * Author 卡卡
 * Created by jing on 2017/3/31.
 */
public class TimeEncoder extends MessageToByteEncoder<UnixTime> {

    @Override
    protected void encode(ChannelHandlerContext ch, UnixTime msg, ByteBuf out) throws Exception {
        out.writeInt((int) msg.value());
    }


//    @Override  这个是继承ChannelOutboundHandlerAdapter
//    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
//        UnixTime m = (UnixTime) msg;
//        ByteBuf encoder = ctx.alloc().buffer(4);
//        encoder.writeInt((int) m.value());
//        ctx.write(encoder, promise);
//    }
}
