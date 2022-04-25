package com.kg.netty.client.handle;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

/**
 * 编码器
 * @author huqiang
 * @date 2022/4/22
 */
public class LengthEncodeHandle extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ByteBuf byteBuf) {
            int length = byteBuf.readableBytes();
            byte[] lenBytes = new byte[4];
            lenBytes[0] = (byte) (length >> 24);
            lenBytes[1] = (byte) (length >> 16);
            lenBytes[2] = (byte) (length >> 8);
            lenBytes[3] = (byte) (length);
            ctx.write(Unpooled.copiedBuffer(lenBytes), promise);
            ctx.write(byteBuf.retain());
        } else {
            ctx.write(msg, promise);
        }

    }
}
