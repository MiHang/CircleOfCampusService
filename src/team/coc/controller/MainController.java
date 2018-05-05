package team.coc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 初始化服务
 */
@Controller
public class MainController {

    /**
     * 是否已初始化
     */
    private static boolean isInitialized = false;

    /**
     * 初始化coc服务
     * @return
     */
    @RequestMapping(value = "startup")
    public String startup(HttpServletRequest request) {

        if (!isInitialized) {
            isInitialized = true;

            // 控制台输出banner.txt中的内容
            try {
                String fileName = MainController.class.getClassLoader()
                        .getResource("banner.txt").getPath();
                File file = new File(fileName);

                ByteArrayOutputStream bos = new ByteArrayOutputStream((int) file.length());
                BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
                int buf_size = 1024;
                byte[] buffer = new byte[buf_size];
                int len = 0;
                while (-1 != (len = in.read(buffer, 0, buf_size))) {
                    bos.write(buffer, 0, len);
                }

                System.out.println(new String(bos.toByteArray(),"UTF-8"));

            } catch (IOException e) {
                e.printStackTrace();
            }

            return "startWebSocketService.do"; // 启动WebSocket服务
        } else {
            request.setAttribute("msg", "------ Circle Of Campus Service Start Success-------");
            return "index.jsp"; // 访问首页
        }
    }
}
