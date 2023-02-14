package com.yjz.discard.handler;


import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import java.nio.ByteBuffer;

/**
 * 处理服务端通道
 */
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * ByteBuf是一个引用计数对象，
     * 必须通过该release()方法显式释放。请记住，释放传递给处理程序的任何引用计数对象是处理程序的责任。
     * 通常，channelRead()处理程序方法的实现如下
     *  try {
     *         // Do something with msg
     *     } finally {
     *         ReferenceCountUtil.release(msg);
     *     }
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf)msg;
//        ByteBuf byteBuf = in.copy();
        try {
            while (in.isReadable()) {
                System.out.println((char) in.readByte());
//                System.out.flush();
            }
        }finally {
//            ReferenceCountUtil.release(msg);
        }
        in.resetReaderIndex();
        ctx.writeAndFlush(msg);

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
 }