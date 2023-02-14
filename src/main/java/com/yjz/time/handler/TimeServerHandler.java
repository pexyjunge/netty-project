package com.yjz.time.handler;


import com.yjz.time.pojo.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {

    public void channelActive(final ChannelHandlerContext ctx) {
//        final ByteBuf time = ctx.alloc().buffer(4);
//        time.writeInt((int) (System.currentTimeMillis() / 1000L + 2208988800L));
//        final ChannelFuture future = ctx.writeAndFlush(time);
//        future.addListener((ChannelFutureListener) channelFuture -> {
//            assert future == channelFuture;
//            ctx.close();
//        });
//        future.addListener(ChannelFutureListener.CLOSE);

        //使用解码器发送消息
        ChannelFuture channelFuture = ctx.writeAndFlush(new UnixTime());
        channelFuture.addListener(ChannelFutureListener.CLOSE);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
