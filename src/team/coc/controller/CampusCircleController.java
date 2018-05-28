package team.coc.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.coc.dao.CampusCircleDao;
import team.coc.dao.UserDao;
import team.coc.pojo.CampusCircle;
import team.coc.pojo.User;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.List;
import java.util.UUID;

/**
 * 获取校园圈的相关信息的控制器
 */
@Controller
public class CampusCircleController {

    /**
     * 新增一条校园圈信息
     * @param param
     * @param images - MultipartFile[] 上传的图片
     * @return
     * @throws JSONException
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/addCampusCircle"}, method = {RequestMethod.POST})
    public String addCampusCircle(@RequestParam String param,
                                  @RequestParam MultipartFile[] images,
                                  HttpServletRequest request)
            throws JSONException, IOException {

        // 项目部署的根路径(绝对路径)
        String path = request.getSession().getServletContext().getRealPath("/");

        // 如果存放校园圈图片的文件夹不存则创建
        path += "res\\upload\\campus_circle\\"; // 保存校园圈图片的根路径
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
            System.out.println("创建路径：" + path);
        }

        // 保存图片
        if (images != null) {
            String imagesPath = "";
            for (MultipartFile img : images) {
                // 生成32位uuid通用唯一识别码作为图片的名称, 全小写
                String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();

                // 将InputStream转为byte[]
                InputStream is = img.getInputStream();
                byte[] bytes = new byte[is.available()];
                is.read(bytes);
                is.close();

                // 写入文件
                FileOutputStream fos = new FileOutputStream(path + uuid);
                fos.write(bytes);
                fos.close();

                imagesPath += "res/upload/campus_circle/" + uuid + ";";
            }
            System.out.println("imagesPath = " + imagesPath);
        }

        return "{'result': 'success'}";
    }

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
     *     title: 'title', <br>
     *     content: 'content', <br>
     *     imagesUrl: '', <br>
     *     videoUrl: '', <br>
     *     publishTime: '2018-05-19', <br>
     *     venue: '体育馆', <br>
     *     activityTime: '2018年5月20号' <br>
     * }] <br>
     * id : int - 校园圈ID <br>
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
