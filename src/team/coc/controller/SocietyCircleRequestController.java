package team.coc.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import team.coc.dao.SocietyDao;
import team.coc.dao.SocietyRequestDao;
import team.coc.dao.UserDao;
import team.coc.pojo.Society;
import team.coc.pojo.SocietyRequest;
import team.coc.pojo.User;

import java.util.Date;

/**
 * 社团权限申请控制器
 */
@Controller
public class SocietyCircleRequestController {

    /**
     * 添加一条用户申请发布社团圈的权限<br>
     * 请求地址URL: http://ip:8080/coc/addSocietyCircleRequest.do<br>
     * 请求方式: POST <br>
     * @param strJson - json数据 <br>
     * 请求参数：uId : int - 用户ID <br>
     * 请求参数：sId : int - 社团ID <br>
     * 请求参数：reason : String - 申请时间 <br>
     * @return - 返回的json对象示例 {"result": "success/error/pending"}
     * result : String - success - 申请成功<br>
     * result : String - pending - 处理中<br>
     * result : String - error - 申请失败<br>
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/addSocietyCircleRequest"}, method = {RequestMethod.POST})
    public String addSocietyCircleRequest(@RequestBody String strJson) throws JSONException {

        System.out.println("############# 进入 addSocietyCircleRequest #############");

        // 接收用户传来的 json 数据
        JSONObject jsonParam = new JSONObject(strJson);
        int uId = jsonParam.getInt("uId");
        int sId = jsonParam.getInt("sId");
        String reason = jsonParam.getString("reason");

        // 返回数据的JSON对象
        JSONObject json = new JSONObject();

        // 获取社团和用户信息
        SocietyDao societyDao = new SocietyDao();
        UserDao userDao = new UserDao();
        Society society = societyDao.getById(sId);
        User user = userDao.getById(uId);
        if (user != null && society != null) {

            // 保存社团权限申请记录
            SocietyRequestDao societyRequestDao = new SocietyRequestDao();
            SocietyRequest societyRequest = new SocietyRequest(reason, new Date(),
                    1, "符合申请条件", new Date(), society, user);
            boolean isSave = societyRequestDao.save(societyRequest);

            if (isSave) {
                json.put("result", "success");
            } else {
                json.put("result", "error");
            }
        } else {
            json.put("result", "error");
        }

        System.out.println("############# addSocietyCircleRequest return:"+ json.toString() + " #############");
        return json.toString();
    }

}
