package team.coc.service;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

/**
 * WebSocket服务端
 */
public class WebSocketService extends WebSocketServer {

    public WebSocketService(int port) {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {

    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {

    }

    /**
     * 接收字符串
     * @param webSocket
     * @param s
     */
    @Override
    public void onMessage(WebSocket webSocket, String s) {

        String str = "{'msg':'success'}";
        System.out.println("服务器接收到来自客户端的消息: " + s);
        webSocket.send(str);
        System.out.println("服务器向客户端返回数据: " + str);

    }

    /**
     * 接收二进制数据信息
     * @param webSocket
     * @param message
     */
    @Override
    public void onMessage(WebSocket webSocket, ByteBuffer message) {

    };


    @Override
    public void onError(WebSocket webSocket, Exception e) {

    }

}
