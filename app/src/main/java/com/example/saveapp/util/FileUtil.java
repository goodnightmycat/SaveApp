package com.example.saveapp.util;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {
    public static String generateFileName(String suffix) {
        return FileManager.getInstance().getImageFilePath()+ File.separator+ System.currentTimeMillis()+"."+suffix;
    }



    public static String generateFileName(String rootPath, String suffix) {
        return rootPath+ File.separator+ System.currentTimeMillis()+"."+suffix;
    }

    public static String copyAssets(Context mContext, String assetPath, String targetPath){
        String resultPath = "";
        AssetManager assetManager = mContext.getResources().getAssets();
        final File mWorkingPath = new File(targetPath);
        // 如果文件路径不存在
        if (!mWorkingPath.exists()) {
            mWorkingPath.mkdirs();
        }

        try {
            // 获得每个文件的名字
            String fileName = "app_login.mp4";
            File outFile = new File(mWorkingPath + "/" + fileName);
            resultPath = outFile.getAbsolutePath();
            // 判断文件是否存在
            if (!outFile.exists()) {
                outFile.createNewFile();
                FileOutputStream out = new FileOutputStream(outFile);
                InputStream in = assetManager.open(fileName);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
            } else {
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultPath;
    }

}
