package com.example.saveapp.face.faceBase;


import com.alibaba.fastjson.JSON;
import com.example.saveapp.util.GsonUtils;
import com.example.saveapp.util.HttpUtil;
import com.example.saveapp.face.faceMatch.AuthService;


/**
 * 人脸注册
 */
public class FaceAdd {

    /**
     * 重要提示代码中所需工具类
     * FileUtil,Base64Util,HttpUtil,GsonUtils请从
     * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
     * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
     * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
     * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
     * 下载
     */
    public static String add(String image, String group_id, String user_id) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add";
        String token = AuthService.getAuth();
        if (token == null) {
            return null;
        }
        try {
            addBean addbean = new addBean(image, "BASE64", group_id, user_id);
            String param = GsonUtils.toJson(addbean);
            System.out.println("======" + param);
            String result = HttpUtil.post(url, token, "application/json", param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isFaceAdd(String image, String group_id, String user_id) {
        //String token="24.a39fb63ea8a3b1d25e33ef6af1bef594.2592000.1563671561.282335-16583245";
        String result = add(image, group_id, user_id);
        if (result != null) {
            String face_token;
            addResultBean ResultBean = com.alibaba.fastjson.JSONObject.toJavaObject(JSON.parseObject(result), addResultBean.class);
            String error = String.valueOf(ResultBean.getError_code());
            if (error.equals("0")) {
                face_token = String.valueOf(ResultBean.getResult().getFace_token());
                System.out.println("上传得到token:" + face_token);
                if (face_token == null) {
                    return false;
                } else {
                    return true;
                }
            }
        }
        return false;
    }
}