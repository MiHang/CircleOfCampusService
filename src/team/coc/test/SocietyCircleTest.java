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
 * 上传社团圈信息测试方法
 */
public class SocietyCircleTest {
    public static void main(String[] args) {

        List<File> images1 = new ArrayList<File>();
        File img1 = new File("D:\\AndroidTemp\\首页海报图\\4.png");
        images1.add(img1);
        addSocietyCircle(1,
                "羽毛球社团招新",
                "    学弟学妹们大家好！这里是羽毛球社啦，首先说一句经典：羽毛球社欢迎你！(✿✪‿✪｡)ﾉ\n" +
                        "    \n" +
                        "\t简单介绍一下哈，羽毛球社呢，就是一个关于打羽毛球，了解羽毛球，喜欢羽毛球的一群人组成的一个社团。当然，如果你不了解它，也从未打过它（感觉我们在虐待羽毛球这种生物\"▔□▔），但是你想要去学习这个新技能那么这个社团的大门是为你敞开哒~\\(≧▽≦)/~！\n" +
                        "\t\n" +
                        "\t时间：2018年5月1日 下午5:00\n" +
                        "    地点：高新篮球场",
                "2018年5月6日",
                "篮球场旁",
                images1);

        List<File> images3 = new ArrayList<File>();
        File img3 = new File("D:\\AndroidTemp\\首页海报图\\6.png");
        images3.add(img3);
        addSocietyCircle(1,
                "羽毛球社团周年庆",
                "活动主题：“喜迎佳日,共渡羽毛球社周年庆”\n" +
                        "\n" +
                        "活动开展形式: 唱歌、跳舞、小品、相声、走秀等。\n" +
                        "\n" +
                        "活动时间：2018年06月03日 18:30 - 21:00\n" +
                        "\n" +
                        "活动地点：学术交流中心\n" +
                        "\n" +
                        "主办单位:成都职业技术学院学生羽毛球社",
                "2018年06月03日 18:30",
                "学术交流中心",
                images3);

        List<File> images2 = new ArrayList<File>();
        File img2 = new File("D:\\AndroidTemp\\首页海报图\\5.png");
        images2.add(img2);
        addSocietyCircle(1,
                "激情绽放 \"羽\"你共享 第九届校园羽毛球比赛",
                "一、竞赛时间：2018年06月20日\n" +
                        "\n" +
                        "二、竞赛地点：高新体育馆\n" +
                        "\n" +
                        "三、比赛项目：男子双打、女子双打、混合双打\n" +
                        "\n" +
                        "四、 报名办法\n" +
                        "      1、男子双打、女子双打、混合双打各项目均可组队报名，不限报名队数，参赛选手不可兼项。\n" +
                        "      2、报名时间与地点：\n" +
                        "         各参赛队员于2018年06月12日12：00之前将报名名单交羽毛球社\n" +
                        "      3、温馨提示：每个参赛队一定在赛前做适量的准备活动，避免运动受伤。",
                "2018年06月20日",
                "高新校区体育馆",
                images2);

    }

    public static String addSocietyCircle(int uId,
                                         String title, String content,
                                         String activityTime, String venue, List<File> images) {
        try {
            JSONObject json = new JSONObject();
            json.put("uId", uId);
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
                .url("http://127.0.0.1:8080/coc/addSocietyCircle.do")
                .post(requestBody)
                .build();

        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(50, TimeUnit.SECONDS)
                .build();

        Response response = okHttpClient.newCall(request).execute();

        if (response.isSuccessful()) {
            System.out.println("上传成功：result = " + response.body().string());
        } else {
            System.out.println("上传失败：error = " + response.code());
        }
    }

}
