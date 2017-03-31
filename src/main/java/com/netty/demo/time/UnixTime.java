package com.netty.demo.time;

import java.util.Date;

/**
 * 用POJO代替ByteBuf
 * 好处：通过从ChannelHandler 中提取出 ByteBuf 的代码，将会使 ChannelHandler的实现变得更加可维护和可重用。
 * Author 卡卡
 * Created by jing on 2017/3/31.
 */
public class UnixTime {
    private final long value;

    public UnixTime(){
        this(System.currentTimeMillis() / 1000L + 2208988800L);
    }

    public UnixTime(long value) {
        this.value = value;
    }

    public long value() {
        return value;
    }

    @Override
    public String toString() {
        return new Date((value() - 2208988800L) * 1000L).toString();
    }
}
