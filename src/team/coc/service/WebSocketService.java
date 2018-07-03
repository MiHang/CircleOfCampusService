package team.coc.service;

import com.common.model.Msg;
import com.common.utils.ByteUtils;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.json.JSONException;
import org.json.JSONObject;
import team.coc.dao.UserDao;
import team.coc.pojo.User;
import team.coc.test.MyServer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * WebSocket服务端
 */
public class WebSocketService extends WebSocketServer {
    UserDao dao=new UserDao();
    static WebSocketService server;//服务器
    Map<WebSocket,String> webSockets=new HashMap<WebSocket,String>();//存储登录用户端口及账号
    ByteUtils utils=new ByteUtils();//字节转换工具类
    List<Msg> info=new ArrayList<Msg>();
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public WebSocketService(int address) {
        super(new InetSocketAddress(address));
    }

    public static void startupWebSocketService() {
        server = new WebSocketService(8888);
        server.start();
        System.out.println("服务器已启动,等待用户连接中");
    }

    public static void main(String[] args) {
        server = new WebSocketService(8888);
        server.start();
        System.out.println("服务器已启动,等待用户连接中");
    }

    @Override
    public void onClose(WebSocket arg0, int arg1, String arg2, boolean arg3) {
        System.out.println(arg0.getRemoteSocketAddress()+"--->连接已关闭  msg：");

        webSockets.remove(arg0);

        System.out.println("当前连接人数"+webSockets.size());

    }


    @Override
    public void onError(WebSocket arg0, Exception arg1) {
        System.out.println("--->连接出错\n"+arg1);
    }

    /**
     * 接收处理用户登录 下线等信息
     */
    @Override
    public void onMessage(WebSocket webSocket, String msg) {
        try {

            JSONObject js=new JSONObject(msg);

            User u= dao.getUserByAccount(js.getString("Account"));
            if(js.getString("Request").equals("Login")){
                if(!webSockets.containsValue(u.getUserName())){

                    webSockets.put(webSocket,u.getUserName());//存储登录信息
                    System.out.println(u.getUserName()+"登录信息已储存");

                    System.out.println(info.size()+"新消息未接收");

                    Iterator<Msg> iterator = info.iterator();   //为了更好的跟踪程序的运行过程
                    while(iterator.hasNext())                      //我将上面的增强for循环改为这里的iterator来实现，本质和
                    {                                              //上面的循环是一样的
                        Msg m=iterator.next();             //可以发现remove被执行后modCount和expectedModCount的值不一样
                        if (m.getReceive().equals(u.getUserName()))
                            System.out.println("发送消息"+m.getText());
                        webSocket.send(utils.toByteArray(m));
                        info.remove(m);

                    }
                }else{
                    System.out.println("已登录");
                }

            }else if(js.getString("Request").equals("Offline")){//处理下线信息
                if(webSockets.containsValue(u.getUserName())){
                    for(Map.Entry<WebSocket, String> entry:webSockets.entrySet()){
                        if(entry.getValue() != null&&entry.getValue().equals(u.getUserName())){
                            webSockets.remove(entry.getKey());
                            System.out.println(entry.getValue()+"下线");
                            break;
                        }

                    }

                    System.out.println("处理后连接数量为:"+webSockets.size());
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

        User u= dao.getUserByAccount(data.getSend());
        data.setUserName(u.getUserName());
        data.setSex(u.getGender());
        if (data.getAudio()!=null){
            addFile(data.getAudio(),"D:/123.wav");
        }
       System.out.println("用户"+data.getSend()+"性别"+data.getSex());
        User receive= dao.getUserByAccount(data.getReceive());
        //判断接收者是否在线
        if(webSockets.containsValue(receive.getUserName())){//在线
            server.sendToPrivateUser(receive.getUserName(),utils.toByteArray(data));
        }else{//存储信息
            info.add(data);
            System.out.println("用户:"+data.getUserName()+"向"+receive.getUserName()+"发送信息,该用户不在线,数据已储存"+data.getDate());
        }
    }

    @Override
    public void onOpen(WebSocket arg0, ClientHandshake arg1) {
        System.out.println("当前连接人数"+webSockets.size());

    }

    public static void addFile(byte[] bfile, String fileURI) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        File dir = null;
        try {
            System.out.println(fileURI.substring(0,fileURI.lastIndexOf("/")));
            dir = new File(fileURI.substring(0,fileURI.lastIndexOf("/")+1).replace("/", "\\"));
            if(!dir.exists()&&dir.isDirectory()){//判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(fileURI);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
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
