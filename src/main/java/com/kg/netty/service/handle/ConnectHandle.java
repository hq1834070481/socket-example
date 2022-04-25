package com.kg.netty.service.handle;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author huqiang
 * @date 2022/4/19
 */
public class ConnectHandle extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) {
        System.out.println("客户端注册成功");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        Channel channel = ctx.channel();
        System.out.println("客户端连接成功，channelId:"+ channel.id().asLongText());
    }


}
