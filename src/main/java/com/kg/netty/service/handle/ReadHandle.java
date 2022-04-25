package com.kg.netty.service.handle;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

/**
 * @author huqiang
 * @date 2022/4/19
 */
public class ReadHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf in = (ByteBuf) msg;
        System.out.println("服务器接收到的消息：" + in.toString(CharsetUtil.UTF_8));
        ctx.fireChannelRead(msg);
    }
}
