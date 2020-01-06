package com.example.saveapp.face.faceMatch;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.saveapp.util.HttpUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 人脸对比V3版本示例代码-JavaAPI
 *
 */
public class FaceMatch {

    public static boolean ismatch(String image, String face_token) {

        String result=FaceMatch(image,face_token);
        FaceMatchResultBean faceMatchResultBean = com.alibaba.fastjson.JSONObject.toJavaObject(JSON.parseObject(result), FaceMatchResultBean.class);
        String error= String.valueOf(faceMatchResultBean.getError_code());
        System.out.println("错误码："+error);
        if (error.equals("0")) {
            String score= String.valueOf(faceMatchResultBean.getResult().getScore());
            System.out.println("对比分值:"+score);
            double integerScore= Double.valueOf(score);
            if (integerScore>=80)
            return true;
        }
       return false;
    }
    /**
     * 人脸对比示例代码
     */
    public static String FaceMatch(String image, String face_token) {
        final String FACE_MATCH = "https://aip.baidubce.com/rest/2.0/face/v3/match";
        try {
            String token= AuthService.getAuth();
            if (token==null)
                return null;
            List faceMatchs = new ArrayList();
            FaceMatchBean faceMatch1 = new FaceMatchBean(image,"BASE64");
            FaceMatchBean faceMatch2 = new FaceMatchBean(face_token,"FACE_TOKEN");
            faceMatchs.add(faceMatch1);
            faceMatchs.add(faceMatch2);
            String param = JSONObject.toJSONString(faceMatchs);
            System.out.println("======"+param);
            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。
            String result = HttpUtil.post(FACE_MATCH, token, param);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}