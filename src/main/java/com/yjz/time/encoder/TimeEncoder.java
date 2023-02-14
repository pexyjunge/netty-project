package com.yjz.time.encoder;

import com.yjz.time.pojo.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

public class TimeEncoder extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
        UnixTime unixTime = (UnixTime) msg;
        ByteBuf byteBuf = ctx.alloc().buffer(4);
        byteBuf.writeInt((int) unixTime.getValue());
        ctx.write(byteBuf, promise);
    }
}
