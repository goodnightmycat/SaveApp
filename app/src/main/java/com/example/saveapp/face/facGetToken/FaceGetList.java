package com.example.saveapp.face.facGetToken;

import com.alibaba.fastjson.JSON;
import com.example.saveapp.face.GsonUtils;
import com.example.saveapp.face.HttpUtil;
import com.example.saveapp.face.faceMatch.AuthService;


/**
 * 获取用户人脸列表
 */
public class FaceGetList {

    /**
     * 重要提示代码中所需工具类
     * FileUtil,Base64Util,HttpUtil,GsonUtils请从
     * https://ai.baidu.com/file/658A35ABAB2D404FBF903F64D47C1F72
     * https://ai.baidu.com/file/C8D81F3301E24D2892968F09AE1AD6E2
     * https://ai.baidu.com/file/544D677F5D4E4F17B4122FBD60DB82B3
     * https://ai.baidu.com/file/470B3ACCA3FE43788B5A963BF0B625F3
     * 下载
     */
    public static String getList(String user_id, String group_id) {
        // 请求url
        String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceset/face/getlist";
        try {
            String token= AuthService.getAuth();
            if (token==null)
                return null;
            FaceGetListBean faceGetListBean=new FaceGetListBean(user_id,group_id);
            String param = GsonUtils.toJson(faceGetListBean);

            // 注意这里仅为了简化编码每一次请求都去获取access_token，线上环境access_token有过期时间， 客户端可自行缓存，过期后重新获取。

            String result = HttpUtil.post(url, token, "application/json", param);
            System.out.println(result);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getFaceToken(String user_id, String group_id) {
       String result= FaceGetList.getList(user_id,group_id);
        FaceGetListResultBean faceGetListResultBean = com.alibaba.fastjson.JSONObject.toJavaObject(JSON.parseObject(result), FaceGetListResultBean.class);

      /*   List<FaceDetector.Face> list=faceGetListResultBean.getFaceList();
         for(int i=0;i<list.size();i++)
             list.get(i)
*/  //  List<FaceGetListResultBean.Result.Face_list> list=
         String face= String.valueOf(faceGetListResultBean.getResult().getFace_list().get(0));
        FaceGetListResultBean.Result.Face_list list = com.alibaba.fastjson.JSONObject.toJavaObject(JSON.parseObject(face), FaceGetListResultBean.Result.Face_list.class);
        String realResult= String.valueOf(list.getFace_token());
        //   String face=String.valueOf();
        return realResult;
    }
}