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

        List<File> images1 = new ArrayList<File>();
        File img1 = new File("D:\\AndroidTemp\\切图\\校园圈\\img_campus_kol.png");
        images1.add(img1);
        addCampusCircle(1,
                "校园KOL 颜值担当 非你莫属",
                "校园KOL 颜值担当 非你莫属",
                "2018年5月2日 下午2:00",
                "学术交流中心",
                images1);

        List<File> images2 = new ArrayList<File>();
        File img2 = new File("D:\\AndroidTemp\\切图\\校园圈\\img_sxcz.png");
        images2.add(img2);
        addCampusCircle(1,
                "\"书香成职\"系列活动成果展",
                "\"书香成职\"系列活动成果展",
                "2018年5月3日至10日",
                "图书馆",
                images1);

        List<File> images3 = new ArrayList<File>();
        File img3 = new File("D:\\AndroidTemp\\切图\\校园圈\\img_sxcz.png");
        images3.add(img3);
        addCampusCircle(1,
                "\"五四青年节\"文艺汇演",
                "\"五四青年节\"文艺汇演",
                "2018年5月4日 下午6:00",
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
