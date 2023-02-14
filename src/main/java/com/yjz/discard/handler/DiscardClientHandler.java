package com.yjz.discard.handler;

import com.yjz.discard.client.DiscardClient;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class DiscardClientHandler extends SimpleChannelInboundHandler<Object> {


    private ByteBuf content;
    private ChannelHandlerContext ctx;
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
        // 服务器应该什么都不发送，但如果它发送了一些东西，就丢弃它。
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //初始化信息
        content = ctx.alloc().directBuffer(DiscardClient.SIZE).writeZero(DiscardClient.SIZE);
        generateTraffic();
    }

    private void generateTraffic() {
        // 将出站缓冲区刷新到套接字。
        // 刷新后，再次生成相同的流量。
        ctx.writeAndFlush(content.retainedDuplicate()).addListener(trafficGenerator);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    private final ChannelFutureListener trafficGenerator = channelFuture -> {
        if (channelFuture.isSuccess()) {
            generateTraffic();
        } else {
            channelFuture.cause().printStackTrace();
            channelFuture.channel().closeFuture();
        }
    };

}
