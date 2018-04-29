package team.coc.test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;


/**
 * web socket 客户端
 * @author JayeLi
 *
 */
public class WebSocketClientTest extends WebSocketClient {

	private static WebSocketClientTest client;// 连接客户端
	
    public WebSocketClientTest(URI serverUri, Draft draft) {
		super(serverUri, draft);
	}

    /**
	 * 打开连接
	 */
	@Override
	public void onOpen(ServerHandshake serverHandshake) {
		client.send("getMusic");
	}
	
	
    /**
     * 关闭连接
     */
	@Override
	public void onClose(int code, String reason, boolean remote) {
		
	}

	
	/**
	 * 连接出错
	 */
	@Override
	public void onError(Exception e) {
		
	}

	
	/**
	 * 接收二进制数据
	 */
	@Override
	public void onMessage(ByteBuffer message ) {
		//判断是否有byte[]
		if (message.hasArray()) {
					
			// 获取byte[]
			byte[] bytes = message.array();
			
			// 写入文件
			try {
				
				FileOutputStream fileOutputStream = new FileOutputStream("D:\\abc.mp3");
				fileOutputStream.write(bytes);
				fileOutputStream.close();
				System.out.println("成功接收文件.....");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 接收JSON数据
	 */
	@Override
	public void onMessage(String message) {
		
	}
	
	
	/**
	 * 所有连接协议<br/><br/>
	 * 
	 * new Draft_10()<br/>
	 * new Draft_17()<br/>
	 * Draft_75()<br/>
	 * Draft_76()<br/><br/>
	 * 
	 * 所有连接后台<br/><br/>
	 * 
	 * 连接Java Web后台<br/>
	 * &emsp;ws://127.0.0.1:8080/JavaWeb_WebScoketServerDemo/websocket/ID1<br/><br/>
	 * 
	 * 连接Java Application后台<br/>
	 * &emsp;ws://127.0.0.1:8887"<br/>
	 * 
	 * @param args
	 */
	public static void main(String[]args) {
    	
        try {
        	
        	//String uri = "ws://127.0.0.1:8080/JavaWeb_WebScoketServerDemo/websocket";
        	String uri = "ws://127.0.0.1:8887";
        	client = new WebSocketClientTest(new URI(uri), new Draft_17());
        	client.connect(); // 连接
        	
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
    }
	
}