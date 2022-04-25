package com.kg.nio.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * @author huqiang
 * @date 2022/4/18
 */
public class ServerHandle implements Runnable {

    private volatile boolean running;

    private ServerSocketChannel serverSocketChannel;

    private Selector selector;

    public ServerHandle(int port) {
        try {
            //log4j.properties.创建选择器
            selector = Selector.open();
            //2.创建服务器通道
            serverSocketChannel = ServerSocketChannel.open();
            //3.设置为非阻塞
            serverSocketChannel.configureBlocking(false);
            // 4.绑定端口
            serverSocketChannel.bind(new InetSocketAddress(port));
            //5.注册到选择器
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("服务器已启动，端口号：" + port);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {

        while (running) {
            try {
                selector.select(1000);
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();
                    handleInput(selectionKey);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        try {
            selector.close();
            serverSocketChannel.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private void handleInput(SelectionKey selectionKey) {
        try {
            if (selectionKey.isAcceptable()) {
                System.out.println("接收到了连接请求");
                handleAcceptable((ServerSocketChannel) selectionKey.channel());

            } else if (selectionKey.isReadable()) {
                handleReadable((SocketChannel) selectionKey.channel(), selectionKey);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        selectionKey.cancel();
    }

    private void handleReadable(SocketChannel channel, SelectionKey selectionKey) throws IOException {
        /*创建ByteBuffer，开辟一个缓冲区*/
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        /*读取请求码流，返回读取到的字节数*/
        int read = channel.read(buffer);
        if (read > 0) {
            buffer.flip();
            /*根据缓冲区可读字节数创建字节数组*/
            byte[] bytes = new byte[buffer.remaining()];
            /*将缓冲区可读字节数组复制到新建的数组中*/
            buffer.get(bytes);
            String message = new String(bytes, StandardCharsets.UTF_8);
            System.out.println("服务器收到消息：" + message);
            /*处理数据*/
            String result = response(message);
            /*发送应答消息*/
            doWrite(channel, result);
        } else if (read < 0) {
            System.out.println("客户端已断开连接");
            selectionKey.cancel();
            channel.close();
        }

    }

    /*发送应答消息*/
    private void doWrite(SocketChannel sc, String message) throws IOException {
        System.out.println("服务器发送消息：" + message);
        //将消息编码为字节数组
        byte[] bytes = message.getBytes();
        //根据数组容量创建ByteBuffer
        ByteBuffer buffer = ByteBuffer.allocate(bytes.length);
        //将字节数组复制到缓冲区
        buffer.put(bytes);
        //flip操作
        buffer.flip();
        //发送缓冲区的字节数组x
        sc.write(buffer);
    }

    public String response(String msg) {
        return "Hello," + msg + ",Now is " + new Date();
    }

    private void handleAcceptable(ServerSocketChannel channel) throws IOException {
        //创建连接通道
        SocketChannel socketChannel = channel.accept();
        System.out.println("创建了一个连接通道");
        //设置为非阻塞
        socketChannel.configureBlocking(false);
        //注册到选择器
        socketChannel.register(selector, SelectionKey.OP_READ);

        doWrite(socketChannel, response("client"));
    }

    public void start() {
        running = true;
        new Thread(this).start();
    }

    public void stop() {
        running = false;
    }
}
