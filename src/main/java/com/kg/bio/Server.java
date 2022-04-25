package com.kg.bio;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author huqiang
 * @date 2022/4/11
 */
public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = new ServerSocket();
        serverSocket.bind(new InetSocketAddress(8888));
        System.out.println("socket server start!");
        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(() -> {
                ObjectInputStream inputStream = null;
                ObjectOutputStream outputStream = null;
                try {
                    inputStream = new ObjectInputStream(socket.getInputStream());
                    outputStream = new ObjectOutputStream(socket.getOutputStream());
                    String message = inputStream.readUTF();
                    System.out.println("receive client message:" + message);
                    String sendMessage = "hello client";
                    System.out.println("send client message：" + sendMessage);
                    outputStream.writeUTF(sendMessage);
                    outputStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        socket.close();
                        if (inputStream != null) {
                            inputStream.close();
                        }
                        if (outputStream != null) {
                            outputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

}
