package team.coc.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import team.coc.dao.CampusCircleDao;
import team.coc.dao.UserDao;
import team.coc.pojo.CampusCircle;
import team.coc.pojo.User;

import java.util.List;

/**
 * 获取校园圈的相关信息的控制器
 */
public class CampusCircleController {



    /**
     * 分页获取校园圈信息<br>
     * 请求地址URL: http://ip:8080/coc/getCampusCircle.do<br>
     * 请求方式: POST<br>
     * @param strJson - json数据<br>
     * 请求参数：uId : int - 用户ID<br>
     * 请求参数：start : int - 从索引start处开始获取校园圈信息(第一条记录的索引为0)<br>
     * 请求参数：end : int - 从索引end处结束获取校园圈信息<br>
     * 示例：{"uId": 1, start: 1, end: 3}<br><br>
     * @return 返回的json数组示例<br>
     * [{ <br>
     *     id: 1, <br>
     *     cover: '/upload/1234.png' <br>
     *     title: 'title', <br>
     *     content: 'content', <br>
     *     imagesUrl: '', <br>
     *     videoUrl: '', <br>
     *     publishTime: '2018-05-19', <br>
     *     venue: '体育馆', <br>
     *     activityTime: '2018年5月20号' <br>
     * }] <br>
     * id : int - 校园圈ID <br>
     * cover : String - 校园圈信息封面 <br>
     * title : String - 校园圈标题 <br>
     * content : String - 校园圈内容 <br>
     * imagesUrl : String - 校园圈内容之图片地址 <br>
     * videoUrl : String - 校园圈内容之视频地址 <br>
     * publishTime : String - 校园圈发布时间 <br>
     * venue : String - 活动地址 <br>
     * activityTime : String - 活动时间 <br><br>
     * 注 : 失败时返回[{result:'error'}]，无记录时返回[](空JSON数组)<br><br>
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/getCampusCircle"}, method = {RequestMethod.POST})
    public String getCampusCircle(@RequestBody String strJson) throws JSONException {

        // 用户请求时上传的参数
        JSONObject jsonParam = new JSONObject(strJson);
        int uId = jsonParam.getInt("uId"); // 用户ID
        int start = jsonParam.getInt("start"); // 开始查询处索引
        int end = jsonParam.getInt("end"); // 结束查询处索引

        // 返回数据用的JSON数组
        JSONArray json = new JSONArray();

        // 通过用户ID获取用户所在的学校ID
        UserDao userDao = new UserDao();
        User user = userDao.getById(uId);
        if (user != null) {
            int campusId = user.getFaculty().getCampus().getCampusId();

            CampusCircleDao campusCircleDao = new CampusCircleDao();
            List<CampusCircle> campusCircles = campusCircleDao.getCampusCircles(campusId, start, end);

            // 有结果
            if (campusCircles != null && campusCircles.size() > 0) {

                for (CampusCircle campusCircle : campusCircles) {

                    // 将每条记录存入json对象中
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", campusCircle.getId());
                    jsonObject.put("cover", campusCircle.getCover());
                    jsonObject.put("title", campusCircle.getTitle());
                    jsonObject.put("content", campusCircle.getContent());
                    jsonObject.put("imagesUrl", campusCircle.getImagesUrl());
                    jsonObject.put("videoUrl", campusCircle.getVideoUrl());
                    jsonObject.put("publishTime", campusCircle.getPublishTime());
                    jsonObject.put("venue", campusCircle.getVenue());
                    jsonObject.put("activityTime", campusCircle.getActivityTime());

                    json.put(jsonObject);
                }
            }
        } else { // 无此用户ID
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("result", "error");
            json.put(jsonObject);
        }

        return json.toString();
    }

    /**
     * 获取校园圈记录的大小(数量)
     * 请求地址URL: http://ip:8080/coc/getCampusCircleSize.do<br>
     * 请求方式: POST<br>
     * @param strJson - json数据<br>
     * 请求参数：uId : int - 用户ID<br>
     * @return 返回的json数据示例<br>
     * 成功 {size: 0}<br>
     * 失败 {result: 'error'}<br>
     * size : int - 校园圈记录大小(数量) <br>
     * result : String - 请求结果
     * @throws JSONException
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/getCampusCircleSize"}, method = {RequestMethod.POST})
    public String getCampusCircleSize(@RequestBody String strJson) throws JSONException {

        // 用户请求时上传的参数
        JSONObject jsonParam = new JSONObject(strJson);
        int uId = jsonParam.getInt("uId"); // 用户ID

        // 返回结果使用的JSON对象
        JSONObject json = new JSONObject();

        // 通过用户ID获取用户所在的学校ID
        UserDao userDao = new UserDao();
        User user = userDao.getById(uId);
        if (user != null) {
            int campusId = user.getFaculty().getCampus().getCampusId();
            CampusCircleDao campusCircleDao = new CampusCircleDao();
            int size = campusCircleDao.getCampusCircleSize(campusId);
            json.put("size", size);
        } else { // 无此用户ID
            json.put("result", "error");
        }

        return json.toString();
    }
}
