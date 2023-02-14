package com.yjz.time.handler;

import com.yjz.time.pojo.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

public class TimeClientHandler extends ChannelInboundHandlerAdapter {

    private ByteBuf buf;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        ByteBuf byteBuf = (ByteBuf) msg;
//        try {
//            long currentTimeMillis = (byteBuf.readUnsignedInt() - 2208988800L) * 1000L;
//            System.out.println(new Date(currentTimeMillis));
//            ctx.close();
//        } finally {
//            byteBuf.release();
//        }

        //使用ByteBuf解码
//        ByteBuf byteBuf = (ByteBuf) msg;
//        buf.writeBytes(byteBuf);//2
//        byteBuf.release();
//        if (buf.readableBytes() >=4) {//3
//            long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
//            System.out.println(currentTimeMillis);
//            ctx.close();
//        }

        //使用UnixTime解码器
        UnixTime unixTime = (UnixTime) msg;
        System.out.println(unixTime);
        ctx.close();
    }

    /**
     * 生命周期监听器开始方法
     * @param ctx
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        buf=ctx.alloc().buffer(4);//1
    }

    /**
     * 生命周期监听器结束方法
     * @param ctx
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        buf.release();
        buf = null;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) {
        throwable.printStackTrace();
        channelHandlerContext.close();
    }

}
