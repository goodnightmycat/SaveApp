package com.example.saveapp.face.RealManFaceCheck;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.saveapp.util.GsonUtils;
import com.example.saveapp.util.HttpUtil;
import com.example.saveapp.face.faceMatch.AuthService;

import java.util.ArrayList;
import java.util.List;

/**
 * 在线活体检测
 */
public class FaceVerify {

    /**
     * 重要提示代码中所需工具类
     * FileUtil,Base64Util,HttpUtil,GsonUtils请从
     * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
     * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
     * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
     * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
     * 下载
     */
    public static String faceVerify(String image, String image_type) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceverify";
        String token = AuthService.getAuth();
        if (token == null)
            return null;
        try {
            List<Object> list = new ArrayList<Object>();
            FaceVerifyBean faceVerifyBean = new FaceVerifyBean(image, image_type);
            list.add(faceVerifyBean);
            String param = GsonUtils.toJson(list);
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String result = HttpUtil.post(url, token, "application/json", param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isverify(String image, String image_type) {
        String result = FaceVerify.faceVerify(image, image_type);
        FaceVerifyResultBean faceVerifyResultBean = com.alibaba.fastjson.JSONObject.toJavaObject(JSON.parseObject(result), FaceVerifyResultBean.class);
        String error = String.valueOf(faceVerifyResultBean.getError_code());
        //  if (result!=null){
        if (error.equals("0")) {
            float face_liveness = faceVerifyResultBean.getResult().getFace_liveness();
            Log.i("1", "isverify: "+face_liveness);
            if (face_liveness > 0.393241)
                return true;
        }
        //  }
        return false;
    }
}