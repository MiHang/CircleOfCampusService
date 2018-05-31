package team.coc.test;


import com.common.model.Msg;
import com.common.utils.ByteUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;


public class MyClient extends WebSocketClient {
	static MyClient client;
	public static JTextArea jt;
	static JTextField jtf;
	static ByteUtils utils=new ByteUtils();
	static String send="demo@163.com";
	static String Receive="jayevip@163.com";


	public MyClient(URI serverURI) {
		super(serverURI);

	}

	public static void main(String[] args) throws URISyntaxException {
		// TODO Auto-generated method stub
		JFrame jf=new JFrame(send);
		jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jf.setSize(500, 400);
		jf.setLocationRelativeTo(null);
		jf.setLayout(new BorderLayout());


		jt=new JTextArea();
		jt.setEditable(false);

		jf.add(jt,BorderLayout.CENTER);
		jtf=new JTextField();
		jf.add(jtf,BorderLayout.SOUTH);

		JButton jb=new JButton("发送");
		jf.add(jb,BorderLayout.EAST);

		URI uri=new URI("ws://172.17.166.197:8891");
		client=new MyClient(uri);
		client.connect();

		jf.setVisible(true);
		jb.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if(client.getConnection().isOpen()){
					Msg dataMsg=new Msg();
					dataMsg.setSend(send);
					dataMsg.setReceive(Receive);
					dataMsg.setText(jtf.getText().toString());

					client.send(utils.toByteArray(dataMsg));
					jt.append(send+":"+jtf.getText().toString()+"\n");
				}

			}

			public void mousePressed(MouseEvent e) {

			}

			public void mouseReleased(MouseEvent e) {

			}

			public void mouseEntered(MouseEvent e) {

			}

			public void mouseExited(MouseEvent e) {

			}
		});

	}

	@Override
	public void onClose(int arg0, String arg1, boolean arg2) {
		// TODO Auto-generated method stub


	}

	@Override
	public void onError(Exception arg0) {
		// TODO Auto-generated method stub


	}
	/**
	 * 转发字符串消息
	 */
	@Override
	public void onMessage(String msg) {

		System.out.println("fuwu"+msg);



	}
	/**
	 * 接收聊天消息
	 */
	@Override
	public void onMessage(ByteBuffer bytes) {
		Msg msg =utils.toT(bytes.array());

		jt.append(msg.getUserName()+":"+msg.getText()+msg.getDate()+"\n");
	}
	@Override
	public void onOpen(ServerHandshake arg0) {
		// TODO Auto-generated method stub

		JSONObject js=new JSONObject();
		try {
			js.put("Account", send);
			js.put("Request", "Login");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		client.send(js.toString());



	}

}
