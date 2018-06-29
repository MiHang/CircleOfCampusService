package team.coc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import team.coc.service.WebSocketService;

import java.io.*;

/**
 * 初始化服务
 */
@Controller
public class MainController {

    /**
     * WebSocket服务是否启动
     */
    private static boolean isStartup = false;

    /**
     * 启动CircleOfCampusService服务<br>
     * 部署项目后请手动访问一次该地址以启动WebSocket服务<br>
     * 服务地址URL：http://ip:8080/coc/startup.do
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/coc/startup")
    public String startService() {
        System.out.println("############# 进入startup #############");
        if (!isStartup) {
            isStartup = true;

//            int port = 8887; // 监听端口8887
//            WebSocketService server = new WebSocketService(port);
//            server.start();
            WebSocketService server=new WebSocketService(8888);
            server.start();
            System.out.println("服务器已启动,等待用户连接中");


            String noBug = "";
            String str = "------ Circle Of Campus Service Startup Success -------";

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

                noBug = new String(bos.toByteArray(),"UTF-8");
                System.out.println(noBug);
                System.out.println(str);
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("############# startup return:" +
                    "<pre style='font-size:18px;'>" + noBug +
                    "\n\n" + str + "</pre>" + " #############");
            return ("<pre style='font-size:18px;'>" + noBug + "\n\n" + str + "</pre>");
        }

        System.out.println("############# startup return:"+
                "<br/><p>------ Circle Of Campus Service Started. Don't start again! ------</p>"
                + " #############");
        return "<br/><p>------ Circle Of Campus Service Started. Don't start again! ------</p>";
    }

}
