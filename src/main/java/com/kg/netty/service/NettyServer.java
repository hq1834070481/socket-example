package com.kg.netty.service;

import com.kg.netty.service.handle.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author huqiang
 * @date 2022/4/19
 */
public class NettyServer {

    private final int port;

    private boolean started = false;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    public static void main(String[] args) throws InterruptedException {
        new NettyServer(8888).start();
    }


    public NettyServer(int port) {
        this.port = port;
    }

    public void start() throws InterruptedException {
        if (started) {
            return;
        }
        started = true;
        bossGroup = new NioEventLoopGroup(1);
        workerGroup = new NioEventLoopGroup();
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new ConnectHandle())
                                .addLast(new LengthDecodeHandle())
                                .addLast(new LengthEncodeHandle())
                                .addLast(new WriteHandle())
                                .addLast(new ReadHandle());

                    }
                });
        serverBootstrap.bind(port).sync();
        System.out.println("netty server start success on port:" + port);
    }

    public void stop() {
        if (!started) {
            return;
        }
        started = false;
        bossGroup.shutdownGracefully();
        workerGroup.shutdownGracefully();
    }

}

