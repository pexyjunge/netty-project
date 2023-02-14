package com.yjz.time.decoder;

import com.yjz.time.pojo.UnixTime;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class TimeDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() < 4) {
            return;
        }
        byteBuf.readInt();
//        System.out.println("byteBUfdaix" + byteBuf.readInt());
        list.add(new UnixTime(byteBuf.readUnsignedInt()));
    }
}
