package team.coc.test;



import com.common.model.Msg;
import com.common.utils.ByteUtils;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;
import team.coc.dao.UserDao;
import team.coc.pojo.User;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.*;


public class MyServer extends WebSocketServer {

	static MyServer server;//服务器
	byte[] doc = null;//存储文件字节
	Map<WebSocket,String> webSockets=new HashMap<WebSocket,String>();//存储登录用户端口及账号
	ByteUtils utils=new ByteUtils();//字节转换工具类
	List<Msg> info=new ArrayList<Msg>();
	SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	public MyServer(int address) {
		super(new InetSocketAddress(address));
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		server=new MyServer(8891);
		server.start();
		System.out.println("服务器已启动,等待用户连接中");

	}

	@Override
	public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3) {
		// TODO Auto-generated method stub
		System.out.println(arg0.getRemoteSocketAddress()+"--->连接已关闭  msg：");

		webSockets.remove(arg0);

		System.out.println("当前连接人数"+webSockets.size());

	}


	@Override
	public void onError(WebSocket arg0, Exception arg1) {
		// TODO Auto-generated method stub
		System.out.println("--->连接出错\n"+arg1);
	}

	/**
	 * 接收处理用户登录 下线等信息
	 */
	@Override
	public void onMessage(WebSocket webSocket, String msg) {
		try {

			JSONObject js=new JSONObject(msg);
			if(js.getString("Request").equals("Login")){
				if(!webSockets.containsValue(js.getString("Account"))){

					webSockets.put(webSocket,js.getString("Account"));//存储登录信息
					System.out.println("未登录,已放入"+webSockets.size());

					System.out.println(info.size()+"新消息未接收");

					for(Msg r:info){//判断服务器是否有该用户未接收的信息 ,有则发送

						if(r.getReceive().equals(js.getString("Account"))){
							System.out.println("发送消息"+r.getText());
							webSocket.send(utils.toByteArray(r));
//							info.remove(index);
						}

					}

				}else{
					System.out.println("已登录");
				}

			}else{//处理下线信息
				if(webSockets.containsValue(js.getString("Account"))){
					for(Map.Entry<WebSocket, String> entry:webSockets.entrySet()){
						if(entry.getValue() != null&&entry.getValue().equals(js.getString("Account"))){
							webSockets.remove(entry.getKey());
						}

					}

					System.out.println("处理后"+webSockets.size());
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * 接收处理用户聊天信息
	 */
	@Override
	public void onMessage(WebSocket arg0, ByteBuffer bytes) {
		Msg data=utils.toT(bytes.array());
		data.setDate(sdf.format(new Date()));

		//根据账号查询用户名与性别
		UserDao dao=new UserDao();
		User u= dao.getUserByAccount(data.getSend());
		data.setUserName(u.getUserName());
		data.setSex(u.getGender());



		System.out.println(data.getSend()+":"+data.getText());
		//判断接收者是否在线
		if(webSockets.containsValue(data.getReceive())){//在线
			server.sendToPrivateUser(data.getReceive(),utils.toByteArray(data));
		}else{//存储信息
			info.add(data);
			System.out.println("接受者用户"+data.getReceive()+"不在线,数据已储存"+data.getDate());
		}
	}

	@Override
	public void onOpen(WebSocket arg0, ClientHandshake arg1) {
		System.out.println("当前连接人数"+webSockets.size());

	}
	/**
	 * 转发二进制数据信息
	 * @param bytes
	 */
	public void sendToAllUser(byte[] bytes) {
		int i=0;
		if(webSockets.size()>0){
			for (Map.Entry<WebSocket,String> entry : webSockets.entrySet()) {
				entry.getKey().send(bytes);
				i++;
			}
			System.out.println(i+"人在线"+"转发"+i+"人");
		}

	}

	/**
	 * 转发二进制数据信息(私发)
	 * @param bytes
	 */
	public void sendToPrivateUser(String Receive,byte[] bytes) {
		int i=0;
		if(webSockets.size()>0){
			for (Map.Entry<WebSocket,String> entry : webSockets.entrySet()) {
				if(entry.getValue().equals(Receive))
					entry.getKey().send(bytes);
				i++;

			}
			System.out.println("私发:"+i+"人在线"+"转发"+i+"人");
		}
	}

}
