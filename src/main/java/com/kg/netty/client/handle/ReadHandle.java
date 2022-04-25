package com.kg.netty.client.handle;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

/**
 * @author huqiang
 * @date 2022/4/19
 */
public class ReadHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;
        System.out.println("客户端接收到消息：" + byteBuf.toString(StandardCharsets.UTF_8));
        ctx.fireChannelRead(msg);
    }
}
