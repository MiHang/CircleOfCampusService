package team.coc.test;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 上传校园圈信息测试方法
 */
public class CampusCircleTest {
    public static void main(String[] args) {

        List<File> images2 = new ArrayList<File>();
        File img2 = new File("D:\\AndroidTemp\\首页海报图\\2.png");
        images2.add(img2);
        addCampusCircle(1,
                "\"书香成职\"系列活动成果展",
                "    为了弘扬和传承中华优秀传统文化，培育和践行社会主义核心价值观，宣传贯彻党的十九大精神，营造浓浓的书香文化氛围， 4月23日下午，由学院图书馆主办的学院首届经典诵读大赛活动在高新校区财经分院学术报告厅举行。学院党委书记周鑑、党委副书记、院长王涛、党委副书记、纪委书记邹克俭、以及各分院部（处室）负责人、部分教师代表出席本次活动。\n" +
                        "\t\n" +
                        "    参加此次比赛的七支参赛队伍，全部是各分院初赛的胜出者。参赛节目有《大漠敦煌》、《满江红》、《沁园春•长沙》、《沁园春•雪》、《百年复兴 经典伴随》、《诵•离•合》、《星汉灿烂 若出其里》、《棋哥随手吟》。\n" +
                        "\t\n" +
                        "    经过激烈地竞争，最终财经分院、工房分院、旅游分院分别荣获荣获一、二、三等奖。其他分院获得优胜奖。至此，学院首届经典诵读大赛活动落下了帷幕。",
                "2018年5月3日至10日",
                "图书馆",
                images2);

        List<File> images3 = new ArrayList<File>();
        File img3 = new File("D:\\AndroidTemp\\首页海报图\\3.png");
        images3.add(img3);
        addCampusCircle(1,
                "\"五四青年节\"文艺汇演",
                "一、汇演主题:拥抱青春 祝福明天\n" +
                        "\n" +
                        "二、汇演组织:\n" +
                        "    1.主办：成都职业技术学院政教处\n" +
                        "    2.承办：成都职业技术学院团委\n" +
                        "    3.协办：成都职业技术学院各协会、社团\n" +
                        "\t\n" +
                        "三、汇演日程：\n" +
                        "    1.演出时间：2018年5月4号 下午6:00\n" +
                        "\t\n" +
                        "四、汇演地点：学术交流中心",
                "2018年5月4日 下午6:00",
                "学术交流中心",
                images3);

        List<File> images1 = new ArrayList<File>();
        File img1 = new File("D:\\AndroidTemp\\首页海报图\\1.png");
        images1.add(img1);
        addCampusCircle(1,
                "校园KOL 颜值担当 非你莫属",
                "招聘岗位：校园KOL \n" +
                        "工作职责： \n" +
                        "1、负责在本校中宣传国外留学相关咨询活动 \n" +
                        "2、负责本校与本校团委学生会等学校组织建立合作关系 \n" +
                        "\n" +
                        "我们需要你有什么？ \n" +
                        "1、超强的执行力和团队合作精神 \n" +
                        "2、具有良好的人际沟通能力，善于与人沟通、打交道，勇于突破自己 \n" +
                        "\n" +
                        "你会收获什么？ \n" +
                        "1、开具实习证明，表现优秀者可转正 \n" +
                        "2、固定的岗位薪酬及提成 \n" +
                        "3、定期与各大院校优秀KOL交流机会 \n" +
                        "\n" +
                        "时间：2018年5月2日下午2点\n" +
                        "地点：学术交流中心",
                "2018年5月2日 下午2:00",
                "学术交流中心",
                images1);
    }

    public static String addCampusCircle(int cId,
                                         String title, String content,
                                         String activityTime, String venue, List<File> images) {
        try {
            JSONObject json = new JSONObject();
            json.put("cId", cId);
            json.put("title", title);
            json.put("content", content);
            json.put("activityTime", activityTime);
            json.put("venue", venue);

            upload(json.toString(), images);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传多张图片及参数
     * @param jsonParam
     * @param images
     */
    public static void upload(String jsonParam, List<File> images) throws IOException {

        MediaType mediaType = MediaType.parse("image/png");

        MultipartBody.Builder multipartBodyBuilder = new MultipartBody.Builder();
        multipartBodyBuilder.setType(MultipartBody.FORM);

        // 添加需要上传的参数jsonParam到builder
        if (jsonParam != null){
            multipartBodyBuilder.addFormDataPart("param", jsonParam);
        }

        // 遍历images中所有图片绝对路径到builder，并约定key如“images”作为后台接受多张图片的key
        if (images != null){
            for (File file : images) {
                multipartBodyBuilder.addFormDataPart("images",
                        file.getName(), RequestBody.create(mediaType, file));
            }
        }

        //构建请求体
        RequestBody requestBody = multipartBodyBuilder.build();

        Request request = new Request.Builder()
                .url("http://127.0.0.1:8080/coc/addCampusCircle.do")
                .post(requestBody)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();

        Response response = okHttpClient.newCall(request).execute();

        if (response.isSuccessful()) {
            System.out.println("上传成功：result = " + response.body().string());
        } else {
            System.out.println("上传失败：error = " + response.code());
        }
    }

}
