package team.coc.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.UnsupportedEncodingException;

/**
 * 连接测试类<br/>
 * 用户测试与服务器的连接
 */
@Controller
public class DemoController {

    /**
     * 获取json数据测试方法
     * @ResponseBody 表明将返回的结果输出到Response的输出对象out中
     * @return String
     * @throws JSONException
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/test"})
    public String test1() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("msg", "success");
        return json.toString();
    }

    /**
     * 获取字节数组测试方法
     * @return
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/testByte"})
    public byte[] test2() throws JSONException, UnsupportedEncodingException {

        /*
        File file = new File("D:/Picture/Saved Pictures/000.jpg");
        FileInputStream fileInputStream = new FileInputStream(file);

        int size = fileInputStream.available(); // 得到文件大小
        byte data[] = new byte[size];
        fileInputStream.read(data); // 读数据
        fileInputStream.close();
        return data;
        */

        JSONObject json = new JSONObject();
        json.put("msg", "success");
        return json.toString().getBytes("UTF-8");
    }

}
