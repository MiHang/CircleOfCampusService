package team.coc.controller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import team.coc.dao.CampusCircleDao;
import team.coc.dao.CampusDao;
import team.coc.dao.SocietyCircleDao;
import team.coc.dao.UserDao;
import team.coc.pojo.Campus;
import team.coc.pojo.CampusCircle;
import team.coc.pojo.SocietyCircle;
import team.coc.pojo.User;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 社团圈控制器
 */
@Controller
public class SocietyCircleController {

    /**
     * 新增一条社团圈信息 <br>
     * 请求地址URL: http://ip:8080/coc/addSocietyCircle.do <br>
     * 请求方式: POST<br>
     * @param param - json数据
     *      请求参数：uId : int - 用户ID <br>
     *      请求参数：title : String - 标题 <br>
     *      请求参数：content : String - 用户ID <br>
     *      请求参数：venue : String - 活动地点 <br>
     *      请求参数：activityTime : String - 活动时间 <br>
     * @param images - MultipartFile[] 上传的图片
     * @return {result:'success/error'}
     * @throws JSONException
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/addSocietyCircle"}, method = {RequestMethod.POST})
    public String addSocietyCircle(@RequestParam String param,
                                  @RequestParam MultipartFile[] images,
                                  HttpServletRequest request)
            throws JSONException, IOException {

        // 项目部署的根路径(绝对路径)
        String path = request.getSession().getServletContext().getRealPath("/");

        // 如果存放校园圈图片的文件夹不存则创建
        path = path.replace("\\ROOT", "");
        path += "coc\\society_circle\\"; // 保存校园圈图片的根路径
        File file = new File(path);
        if (!file.exists() && !file.isDirectory()) {
            file.mkdirs();
            System.out.println("创建路径：" + path);
        }

        // 返回数据用JSON对象
        JSONObject json = new JSONObject();

        // 保存图片
        if (images != null) {

            // 储存图片地址的JSON数组
            JSONArray imageUrlArr = new JSONArray();

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

                // 将该图片地址存入json数组
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("url", "coc/society_circle/" + uuid);
                imageUrlArr.put(jsonObject);
            }

            JSONObject jsonParam = new JSONObject(param);
            int uId = jsonParam.getInt("uId"); // 用户ID
            String title = jsonParam.getString("title"); // 标题
            String content = jsonParam.getString("content"); // 内容
            String venue = jsonParam.getString("venue"); // 活动地点
            String activityTime = jsonParam.getString("activityTime"); // 活动时间

            UserDao userDao = new UserDao();
            SocietyCircleDao societyCircleDao = new SocietyCircleDao();

            User user = userDao.getById(uId);
            SocietyCircle societyCircle = new SocietyCircle(title, content,
                    imageUrlArr.toString(), new Date(), venue, activityTime, 0, user);
            boolean isSave = societyCircleDao.save(societyCircle);

            if (isSave) {
                json.put("result", "success");
            } else {
                json.put("result", "error");
            }
        }

        return json.toString();
    }

    /**
     * 分页查询某用户发布的社团圈信息<br>
     * 请求地址URL: http://ip:8080/coc/getMyPublishSocietyCircle.do<br>
     * 请求方式: POST<br>
     * @param strJson - json数据<br>
     * 请求参数：uId : int - 用户ID<br>
     * 请求参数：start : int - 从索引start处开始获取某用户发布的社团圈信息(第一条记录的索引为0)<br>
     * 请求参数：end : int - 从索引end处结束获取某用户发布的社团圈信息<br>
     * 示例：{"uId": 1, start: 1, end: 3}<br><br>
     * @return 返回的json数组示例<br>
     * [{ <br>
     *     id: 1, <br>
     *     title: 'title', <br>
     *     content: 'content', <br>
     *     imagesUrl: '', <br>
     *     publishTime: '2018-05-19', <br>
     *     venue: '体育馆', <br>
     *     activityTime: '2018年5月20号' <br>
     *     auditing: 0 <br>
     * }] <br>
     * id : int - 社团圈ID <br>
     * title : String - 社团圈标题 <br>
     * content : String - 社团圈内容 <br>
     * imagesUrl : String - 社团圈内容之图片地址 <br>
     * publishTime : String - 社团圈发布时间 <br>
     * venue : String - 活动地址 <br>
     * activityTime : String - 活动时间 <br>
     * auditing : int - 审核状态<br>
     * 注 : 失败时返回[{result:'error'}]，无记录时返回[](空JSON数组)<br><br>
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/getMyPublishSocietyCircle"}, method = {RequestMethod.POST})
    public String getMyPublishSocietyCircle(@RequestBody String strJson) throws JSONException {

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

            SocietyCircleDao societyCircleDao = new SocietyCircleDao();
            List<SocietyCircle> societyCircles = societyCircleDao.
                    getMyPublishSocietyCircle(user.getUserId(), start, end);

            // 有结果
            if (societyCircles != null && societyCircles.size() > 0) {

                for (SocietyCircle societyCircle : societyCircles) {

                    // 将每条记录存入json对象中
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", societyCircle.getId());
                    jsonObject.put("title", societyCircle.getTitle());
                    jsonObject.put("content", societyCircle.getContent());
                    jsonObject.put("imagesUrl", societyCircle.getImagesUrl());
                    jsonObject.put("publishTime", societyCircle.getPublishTime());
                    jsonObject.put("venue", societyCircle.getVenue());
                    jsonObject.put("activityTime", societyCircle.getActivityTime());
                    jsonObject.put("auditing", societyCircle.getAuditing());

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
     * 分页获取社团圈信息<br>
     * 请求地址URL: http://ip:8080/coc/getSocietyCircle.do<br>
     * 请求方式: POST<br>
     * @param strJson - json数据<br>
     * 请求参数：uId : int - 用户ID<br>
     * 请求参数：start : int - 从索引start处开始获取社团圈信息(第一条记录的索引为0)<br>
     * 请求参数：end : int - 从索引end处结束获取社团圈信息<br>
     * 示例：{"uId": 1, start: 1, end: 3}<br><br>
     * @return 返回的json数组示例<br>
     * [{ <br>
     *     id: 1, <br>
     *     title: 'title', <br>
     *     content: 'content', <br>
     *     imagesUrl: '', <br>
     *     publishTime: '2018-05-19', <br>
     *     venue: '体育馆', <br>
     *     activityTime: '2018年5月20号' <br>
     *     auditing: 0 <br>
     * }] <br>
     * id : int - 社团圈ID <br>
     * title : String - 社团圈标题 <br>
     * content : String - 社团圈内容 <br>
     * imagesUrl : String - 社团圈内容之图片地址 <br>
     * publishTime : String - 社团圈发布时间 <br>
     * venue : String - 活动地址 <br>
     * activityTime : String - 活动时间 <br>
     * auditing : int - 审核状态<br>
     * 注 : 失败时返回[{result:'error'}]，无记录时返回[](空JSON数组)<br><br>
     * @throws JSONException json对象异常
     */
    @ResponseBody
    @RequestMapping(value = {"/coc/getSocietyCircle"}, method = {RequestMethod.POST})
    public String getSocietyCircle(@RequestBody String strJson) throws JSONException {

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

            SocietyCircleDao societyCircleDao = new SocietyCircleDao();
            List<SocietyCircle> societyCircles = societyCircleDao.getSocietyCircle(campusId, start, end);

            // 有结果
            if (societyCircles != null && societyCircles.size() > 0) {

                for (SocietyCircle societyCircle : societyCircles) {

                    // 将每条记录存入json对象中
                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("id", societyCircle.getId());
                    jsonObject.put("title", societyCircle.getTitle());
                    jsonObject.put("content", societyCircle.getContent());
                    jsonObject.put("imagesUrl", societyCircle.getImagesUrl());
                    jsonObject.put("publishTime", societyCircle.getPublishTime());
                    jsonObject.put("venue", societyCircle.getVenue());
                    jsonObject.put("activityTime", societyCircle.getActivityTime());
                    jsonObject.put("auditing", societyCircle.getAuditing());

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
     * 获取某用户已发布的社团圈记录的大小(数量)
     * 请求地址URL: http://ip:8080/coc/getMyPublishSocietyCircleSize.do<br>
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
    @RequestMapping(value = {"/coc/getMyPublishSocietyCircleSize"}, method = {RequestMethod.POST})
    public String getMyPublishSocietyCircleSize(@RequestBody String strJson) throws JSONException {

        // 用户请求时上传的参数
        JSONObject jsonParam = new JSONObject(strJson);
        int uId = jsonParam.getInt("uId"); // 用户ID

        // 返回结果使用的JSON对象
        JSONObject json = new JSONObject();

        // 通过用户ID获取用户所在的学校ID
        UserDao userDao = new UserDao();
        User user = userDao.getById(uId);
        if (user != null) {
            SocietyCircleDao societyCircleDao = new SocietyCircleDao();
            int size = societyCircleDao.getMyPublishSocietyCircleSize(user.getUserId());
            json.put("size", size);
        } else { // 无此用户ID
            json.put("result", "error");
        }

        return json.toString();
    }

    /**
     * 获取社团圈记录的大小(数量)
     * 请求地址URL: http://ip:8080/coc/getSocietyCircleSize.do<br>
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
    @RequestMapping(value = {"/coc/getSocietyCircleSize"}, method = {RequestMethod.POST})
    public String getSocietyCircleSize(@RequestBody String strJson) throws JSONException {

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
            SocietyCircleDao societyCircleDao = new SocietyCircleDao();
            int size = societyCircleDao.getSocietyCircleSize(campusId);
            json.put("size", size);
        } else { // 无此用户ID
            json.put("result", "error");
        }

        return json.toString();
    }

}
