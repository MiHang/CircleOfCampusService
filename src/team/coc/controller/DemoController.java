package team.coc.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import team.coc.dao.CampusDao;
import team.coc.dao.UserDao;
import team.coc.pojo.User;

import java.io.UnsupportedEncodingException;
import java.util.List;

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
     * 好友搜索模糊查询
     * @param strJson 搜索关键字 用户名 或 账号
     * @return result 结果 条数 Account  账号信息 UserName 用户名
     * @throws JSONException
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/search"}, method = {RequestMethod.POST})
    public String login(@RequestBody String strJson) throws JSONException {

        JSONObject jsonObject = new JSONObject(strJson); // 用户请求时上传的参数
        UserDao dao=new UserDao();
        //好友查询
        List<User> data=dao.getRequest(jsonObject.getString("Search"));
        JSONObject js=new JSONObject();
        JSONArray ja=new JSONArray();
        js.put("Result",0);
            if (data!=null&&data.size()>0){
                js.put("Result", data.size());
                for(User u:data){
                    JSONObject json = new JSONObject();
                    json.put("Account",u.getEmail());
                    json.put("UserName",u.getUserName());
                    ja.put(json.toString());
                }
            }
            js.put("Info",ja.toString());

        return js.toString();
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
