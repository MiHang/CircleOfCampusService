package team.coc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.coc.service.WebSocketService;

import java.net.UnknownHostException;

/**
 * WebSocket服务端控制器
 */
@Controller
public class WebSocketServiceController{

    @ResponseBody
    @RequestMapping(value = {"/startWebSocketService"})
    public String startService() throws UnknownHostException {
        int port = 8887; // 监听端口8887
        WebSocketService server = new WebSocketService(port);
        server.start();
        System.out.println("------ WebSocket Service Start Success-------");
        return "WebSocket Service Start Success";
    }

}
