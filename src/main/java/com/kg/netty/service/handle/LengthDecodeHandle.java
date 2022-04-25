package com.kg.netty.service.handle;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author huqiang
 * @date 2022/4/22
 */
public class LengthDecodeHandle extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof ByteBuf byteBuf) {
            while (byteBuf.readableBytes() >= 4) {
                int length = byteBuf.readInt();
                ByteBuf buf = byteBuf.readBytes(length);
                ctx.fireChannelRead(buf);
            }
        } else {
            super.channelRead(ctx, msg);
        }
    }
}
