package com.kg.netty.client;

import com.kg.netty.client.handle.*;
import com.kg.netty.service.handle.WriteHandle;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author huqiang
 * @date 2022/4/19
 */
public class NettyClient {

    private final int port;

    private final String host;

    private EventLoopGroup workerGroup;

    private Bootstrap bootstrap;

    public static boolean stated;

    public static NioSocketChannel channel;

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            try {
                new NettyClient("127.0.0.1", 8888).start();
                System.out.println("client started");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
        Thread.sleep(2000L);
        new Thread(() -> {
            while (stated) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("请输入消息：");
                String message = new Scanner(System.in).nextLine();
                if(channel!=null){
                 channel.writeAndFlush(Unpooled.copiedBuffer(message, StandardCharsets.UTF_8));
                }else {
                    break;
                }
            }
        }).start();
    }

    public NettyClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws InterruptedException {
        workerGroup = new NioEventLoopGroup(1);
        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) {
                ch.pipeline()
                        .addLast(new ConnectHandle())
                        .addLast(new LengthDecodeHandle())
                        .addLast(new LengthEncodeHandle())
                        .addLast(new WriteHandle())
                        .addLast(new ReadHandle());

            }
        });
        bootstrap.connect(host, port).sync();
        stated = true;
    }
}
