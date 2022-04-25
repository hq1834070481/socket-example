package com.kg.netty.service.handle;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.CharsetUtil;

/**
 * @author huqiang
 * @date 2022/4/21
 */
public class WriteHandle extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        System.out.println("服务端发送消息："+ buf.toString(CharsetUtil.UTF_8));
        ctx.write(msg,promise);
    }

}
