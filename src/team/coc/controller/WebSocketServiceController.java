package team.coc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.coc.service.WebSocketService;

import javax.servlet.http.HttpServletRequest;
import java.net.UnknownHostException;

/**
 * WebSocket服务端控制器
 */
@Controller
public class WebSocketServiceController{

    /**
     * WebSocket服务是否启动
     */
    private static boolean isStartup = false;

    /**
     * 启动WebSocket服务
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/coc/startWebSocketService")
    public String startService() {

        if (!isStartup) {
            int port = 8887; // 监听端口8887
            WebSocketService server = new WebSocketService(port);
            server.start();
            System.out.println("------ Circle Of Campus Service Start Success-------");
        }

        return "index.jsp";
    }

}
