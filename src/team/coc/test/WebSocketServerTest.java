package team.coc.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.java_websocket.handshake.ClientHandshake;


public class WebSocketServerTest extends org.java_websocket.server.WebSocketServer {

	private org.java_websocket.WebSocket webSocket = null; // 存储第一个连接服务器的用户连接
	
	public WebSocketServerTest(int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
	}

	public WebSocketServerTest(InetSocketAddress address) {
		super(address);
	}
	
	
	/**
	 * 打开连接
	 */
	@Override
	public void onOpen(org.java_websocket.WebSocket conn, ClientHandshake handshake) {}

	
	/**
	 * 关闭连接
	 */
	@Override
	public void onClose(org.java_websocket.WebSocket conn, int code, String reason, boolean remote) {

	}

	
	/**
	 * 接收二进制数据信息
	 */
	@Override
	public void onMessage(org.java_websocket.WebSocket conn, ByteBuffer message) {
		
	};
	
	
	/**
	 * 接收字符串
	 */
	@Override
	public void onMessage(org.java_websocket.WebSocket conn, String message) {
		
		if (message.equals("getMusic")) {
			this.webSocket = conn;
			
			System.out.println("准备向客户端发送文件.....");
			
			String filePath = "C:/develop/Workspaces/MyEclipse2017/java_websocket_transmitFile/data/白山茶.mp3";
			
			try {
				
				File file=new File(filePath); // 获取音乐文件 
				InputStream fis = new FileInputStream(file); // 创建文件字节读取流对象
				byte[] buffer = new byte[fis.available()];
				fis.read(buffer); // 从输入流中读取字节，并将其存储在缓冲区数组buffer中
				
				System.out.println("开始成功.....");
				webSocket.send(buffer);
				System.out.println("发送成功.....");
				
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
	}
	
	
	/**
	 * 出错
	 */
	@Override
	public void onError(org.java_websocket.WebSocket conn, Exception e) {
		e.printStackTrace();
		if (conn != null) {
			conn.close();
		}
	}
	
	
	/**
	 * 测试
	 * @param args
	 * @throws InterruptedException
	 * @throws IOException
	 */
	public static void main(String[] args) throws InterruptedException,
			IOException {

		int port = 8887;
		final WebSocketServerTest server = new WebSocketServerTest(port);
		server.start();
		
	}

}

