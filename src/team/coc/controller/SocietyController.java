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
import team.coc.dao.SocietyDao;
import team.coc.dao.UserDao;
import team.coc.pojo.Society;
import team.coc.pojo.User;

import java.util.List;

/**
 * 社团控制器
 */
@Controller
public class SocietyController {

    /**
     * 获取某用户所在学校的所有社团信息<br>
     * 请求地址URL: http://ip:8080/coc/getAllSociety.do<br>
     * 请求方式: POST <br>
     * @param strJson - json数据 <br>
     * 请求参数：uId : int - 用户ID <br>
     * @return - 返回的json数组示例 [{"societyId":1, "societyName":"棋艺社"}]
     * societyId : int - 社团ID<br>
     * societyName : String - 社团名称<br>
     * 查询无结果返回空JSON数组
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/getAllSociety"}, method = {RequestMethod.POST})
    public String getAllSociety(@RequestBody String strJson) throws JSONException {

        System.out.println("############# 进入 getAllSociety #############");

        // 接收用户传来的 json 数据
        JSONObject jsonParam = new JSONObject(strJson);
        int uId = jsonParam.getInt("uId");

        // 返回数据的JSON数组
        JSONArray json = new JSONArray();

        // 获取用户
        UserDao userDao = new UserDao();
        User user = userDao.getById(uId);
        if (user != null) {

            // 获取校园ID
            int campusId = user.getFaculty().getCampus().getCampusId();

            // 通过校园ID获取该学校的所有社团信息
            SocietyDao societyDao = new SocietyDao();
            List<Society> societies = societyDao.getAllSocietyByCampusId(campusId);

            // 存在结果
            if (societies != null && societies.size() > 0) {
                for (Society society : societies) {
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("societyId", society.getSocietyId());
                    jsonObject.put("societyName", society.getSocietyName());
                    json.put(jsonObject);
                }
            }
        }

        System.out.println("############# getAllSociety return:"+ json.toString() + " #############");
        return json.toString();
    }

}
